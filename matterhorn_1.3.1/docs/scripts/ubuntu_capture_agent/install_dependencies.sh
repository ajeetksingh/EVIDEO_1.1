#!/bin/bash

##########################################################################
# Install and configure the necessary dependencies for the capture agent #
##########################################################################

# Checks this script is being run from install.sh
if [[ ! $INSTALL_RUN ]]; then
    echo "You shouldn't run this script directly. Please use the install.sh instead"
    exit 1
fi

# Include the functions
. ${FUNCTIONS}

# Prompt the user to modify the default repositories
while [[ true ]]; do
    echo
    yesno -d no -? "These are the locations were the necessary software is downloaded"\
                   "Do you wish to use a custom software mirror instead of the Ubuntu defaults?" modify

    if [[ "$modify" ]]; then
	while [[ true ]]; do
	    echo
	    choose -t "Please choose which mirror you want to modify" -p "Selection (leave blank to continue installation)" -a\
                       "Archive Mirror\t(current value: ${mirrors[0]:-$DEFAULT_MIRROR})"\
                       "Security Mirror\t(current value: ${mirrors[1]:-$DEFAULT_SECURITY})"\
                       "Partner Mirror\t(current value: ${mirrors[2]:-$DEFAULT_PARTNER})"\
                       answer
	    if [[ "$answer" ]]; then
		read -p "Please enter the mirror URL: " mirrors[$answer]
	    else
		break;
	    fi
	done
    fi
    
    echo -n "Updating package repositories... "

    # Create a backup of the sources.list file and create its own (preserves any existing sources.list.backup)
    mv -n $SRC_LIST $SRC_LIST.$BKP_SUFFIX
    
    # Applies configuration changes
    DIST_NAME=$(cat /etc/lsb-release | grep "^DISTRIB_CODENAME" | cut -d "=" -f 2)
    echo "deb ${mirrors[0]:-$DEFAULT_MIRROR} ${DIST_NAME} main restricted universe multiverse" > $SRC_LIST
    echo "deb ${mirrors[0]:-$DEFAULT_MIRROR} ${DIST_NAME}-updates main restricted universe multiverse" >> $SRC_LIST
    echo "deb ${mirrors[1]:-$DEFAULT_SECURITY} ${DIST_NAME}-security main restricted universe multiverse" >> $SRC_LIST
    echo "deb ${mirrors[2]:-$DEFAULT_PARTNER} ${DIST_NAME} partner" >> $SRC_LIST
    
    apt-get -qq update &> /dev/null
    
    if [[ $? -eq 0 ]]; then
	break
    else
	echo "Error"
	echo "Couldn't update properly from the current Ubuntu mirrors. Reverting..."
	unset mirrors
    fi
done
echo "Done"

# Auto set selections when installing postfix and jdk packages
# The <<EOF tag indicates an input with several lines, ending with an EOF line (this is bash syntax)
# The lines indicate which answers, that otherwise would be prompted to the user in the package configuration, will be answered automatically
# -- First is the package name
# -- Second is the name of the question to be answered
# -- Third is the type of answer this questions expects
# -- Fourth is the answer that will be given to this question
debconf-set-selections <<EOF
postfix postfix/mailname string fax
postfix postfix/main_mailer_type select Internet Site
sun-java5-jdk shared/accepted-sun-dlj-v1-1 boolean true
?sun-java6-jdk shared/accepted-sun-dlj-v1-1 boolean true
EOF

# Changes the default array delimiter: from 'space' to 'newline'
IFS='
'
# Gets the list of the packages to install --those are delimited by newlines, that's why IFS was previously changed
pkgs=( $PKG_LIST )
bad=( $BAD_PKG_LIST )
reason=( $BAD_PKG_REASON )

# Restore the default array delimiter
unset IFS

for (( i = 0; i < ${#bad[@]}; i++ )); do
    unset install required

    # Check the line ends with a '+', meaning these packages are required for the system
    if [[ "$(echo ${bad[$i]} | grep " +$")" ]]; then
	required=true
	# Strip out the trailing " +"
	bad[$i]="${bad[$i]%% +}"
    fi

    # Check if the packages are installed before asking the user
    for item in $(echo "${bad[$i]}" | cut -d ' ' -f 1-); do
        if [[ -z "$(dpkg -l | grep "$item")" ]]; then
            install="$install $item "
        fi
    done

    if [[ "$install" ]]; then
	if [[ "$required" ]]; then
	    unset ok confirm
	    while [[ ! "$ok" ]]; do
		yesno -d yes -? "${reason[$i]}" -h "? for details" "Do you wish to install the required package(s): ${install}?" ok
		if [[ ! "$ok" ]]; then
		    echo "These packages are essential for the system to work. This script will abort if they are not installed"
		    yesno -d no "Are you sure you DON'T want to install $install?" confirm
		    if [[ "$confirm" ]]; then
			echo "Aborting installation..."
			exit 1
		    fi
		fi
	    done
	else
            yesno -d no -? "${reason[$i]}" -h "? for details" "Do you wish to install ${install}?" ok
	fi
	
        if [[ "$ok" ]]; then
            pkgs[${#pkgs[@]}]="$install"
        fi
    fi 
done

# Check which required packages are already installed
for (( i = 0; i < ${#pkgs[@]}; i++ )); do
    if [[ -z "$(dpkg -l | grep " ${pkgs[$i]} ")" ]]; then
	noinst[${#noinst[@]}]=${pkgs[$i]}
    fi
done

if [[ "${noinst[@]}" ]]; then
# Install the required 3rd party packages
    echo "Installing third party packages from Ubuntu repository, this may take some time... "
    apt-get -y --force-yes install ${noinst[@]}

    if [[ $? -ne 0 ]]; then
	echo "Error!"
	echo "Failed to download the necessary packages. Aborting..."
	exit 1
    fi
    echo "Done"
else
    echo "All the necessary dependencies were already installed"
fi

# Back up the installed packages
for (( i=0; i < ${#noinst[@]}; i++ )); do
    echo "${noinst[$i]}" >> $PKG_BACKUP
done

# Find the version of java to use. 
found_java=false
for java_version in $JAVA_PATTERNS
do
    # The location we would expect to find java. 
    java_location="$JAVA_PREFIX/`ls $JAVA_PREFIX | grep ^$java_version$`"
    # Check to make sure that it found the $java_version and that it is a valid directory. 
    if [[ "$java_location" != "$JAVA_PREFIX/" && -d $java_location ]]; then
        echo Found java at $java_location
        JAVA_PATTERN="$java_version"
        found_java=true
    fi
done

if ! $found_java ; then
    echo "Haven't found a valid install of java ($JAVA_PATTERNS)in $JAVA_PREFIX so exiting."
    exit 1
fi

# Set up java-6-sun as the default alternative
echo -n "Setting up $JAVA_PATTERN as the default jvm... "
update-java-alternatives -s $JAVA_PATTERN 2> /dev/null
echo "Done"

# Define JAVA_HOME variable
export JAVA_HOME=$JAVA_PREFIX/`ls $JAVA_PREFIX | grep ^$JAVA_PATTERN$`

# Log the list of installed packages
echo >> $LOG_FILE
echo "# Installed packages" >> $LOG_FILE
[[ -e $PKG_BACKUP ]] && echo "$(cat $PKG_BACKUP)" >> $LOG_FILE

# Setup felix
echo -n "Downloading Felix... "
while [[ true ]]; do 
    if [[ ! -s ${FELIX_FILENAME} ]]; then
	wget -q ${FELIX_URL}
    fi
    # On success, uncompress the felix files in their location
    if [[ $? -eq 0 ]]; then
	echo -n "Uncompressing... "
	dir_name=$(tar tzf ${FELIX_FILENAME} | grep -om1 '^[^/]*')
	tar xzf ${FELIX_FILENAME}
	if [[ $? -eq 0 ]]; then
	    rm -rf $FELIX_HOME
	    mv ${dir_name%/} -T $FELIX_HOME
	    mv $FELIX_FILENAME $CA_DIR
	    #mkdir -p ${FELIX_HOME}/load
	    echo "Done"
	    break
	fi
    fi
    # Else, ask for the actions to take
    echo
    yesno -d yes "Error retrieving the Felix files from the web. Retry?" retry
    if [[ "$retry" ]]; then
    	echo -n "Retrying... "
    else
    	echo "You must download Felix manually and install it under $OC_DIR, in order for matterhorn to work"
	break
    fi
done

# Setup jv4linfo
if [[ ! -e "$JV4LINFO_PATH/$JV4LINFO_LIB" ]]; then
    mkdir -p $JV4LINFO_DIR
    cd $JV4LINFO_DIR

    echo -n "Installing jv4linfo... "
    if [[ ! -e "$JV4LINFO_JAR" ]]; then
	wget -q $JV4LINFO_URL/$JV4LINFO_JAR
    fi
    jar xf $JV4LINFO_JAR
    cd jv4linfo/src
    # The ant build script has a hardcoded path to the openjdk, this sed line will
    # switch it to be whatever is defined in JAVA_HOME
    sed -i '74i\\t<arg value="-fPIC"/>' build.xml
    sed -i "s#\"/usr/lib/jvm/java-6-openjdk/include\"#\"$JAVA_HOME/include\"#g" build.xml
    
    ant -lib ${JAVA_HOME}/lib &> /dev/null
    if [[ "$?" -ne 0 ]]; then
	echo "Error building libjv4linfo.so"
	exit 1
    fi
    cp ../lib/$JV4LINFO_LIB $JV4LINFO_PATH
    
    cd $WORKING_DIR
    echo "Done"
else
    echo "libjv4linfo.so already installed"
fi

# Setup ntdp
echo 
ask -d "$DEFAULT_NTP_SERVER" "Which NTP server would you like to use?" server
sed -i "s#^server .*#server $server#" $NTP_CONF
echo "NTP server set to $server"
echo "Consider editing the file $NTP_CONF for manually changing the default NTP server or adding more servers to the list"
