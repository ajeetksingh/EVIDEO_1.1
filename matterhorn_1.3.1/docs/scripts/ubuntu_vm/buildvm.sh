#!/bin/sh
# To set variables before calling this script just include them directly on the command line.
# e.g. to override the mirror to use, you could do something like:
#    UBUNTU_MIRROR=http://aifile.usask.ca/apt-mirror/mirror/archive.ubuntu.com/ubuntu/ ./buildvm.sh

HOME=`pwd`
if [ "$MATTERHORN_SVN" = "" ];
  then
  MATTERHORN_SVN="http://opencast.jira.com/svn/MH/tags/1.3.1/"
fi
echo "Building VM with using this SVN url: $MATTERHORN_SVN"
#check for existance of mirror URL
if [ "$UBUNTU_MIRROR" = "" ];
  then
	UBUNTU_MIRROR=http://ubuntu.mirrors.tds.net/ubuntu/
fi
echo "Using ubuntu mirror at: $UBUNTU_MIRROR"


USERNAME="opencast"
PASSWORD="matterhorn"
RELEASE="lucid"

export M2=`pwd`/m2/
export JAVA_HOME=`mvn --version | grep "Java home" | awk '{print $3}'`

#TODO: overwrite parameters with passed in values
#for each param in $# evaluate $i as an item

#install extras that we need if running this script
sudo apt-get -y install ubuntu-vm-builder subversion zip git-core maven2

#check to see if the vmware disk mounting tool is there
if which vmware-mount >/dev/null; then
	echo "VMware Mounter is installed."
else
	echo "VMware Mounter is not installed!"
	exit
fi

#delete the old vm if it exists
if [ -z "$(mount | grep `pwd`/mnt)" ];
 then
	echo "Nothing mounted"
else
	sudo vmware-mount -d mnt
	sleep 2
fi


if [ "$1" = "clean" ]; then 
  echo "==============================="
  echo "====Cleaning up for release===="
  echo "==============================="
  sudo rm -rf vmbackup/
  sudo rm -rf m2/
  sudo rm -rf matterhorn_trunk/
fi
sudo rm -rf ubuntu-vmw6/
sudo rm -rf mnt

echo "=========================="
echo "========Building VM======="
echo "========Please Wait======="
echo "=========================="


if [ ! -e vmbackup ]; then
  #build the ubuntu vm
  sudo ubuntu-vm-builder vmw6 $RELEASE --arch 'i386' --mem '512' --cpus 1 \
  --rootsize '12288' --swapsize '1024' --kernel-flavour='virtual' \
  --hostname 'opencast' --mirror $UBUNTU_MIRROR \
  --components 'main,universe,multiverse' \
  --name 'opencast' --user $USERNAME \
  --pass $PASSWORD --tmpfs=2000 \
  --addpkg acpid \
  --addpkg openjdk-6-jre --addpkg gstreamer0.10-plugins* \
  --addpkg gstreamer0.10-ffmpeg --addpkg gstreamer-tools \
  --addpkg wget --addpkg ntp --addpkg nano --addpkg subversion

  echo "change the vm to use nat networking instead of bridged"
  sed -i 's/bridged/nat/g' ubuntu-vmw6/opencast.vmx
  cp -r ubuntu-vmw6 vmbackup
else 
  #restore from copy
  cp -r vmbackup ubuntu-vmw6
fi

#mount the vm image
mkdir mnt
#Yay, *.vmdk.  The script now uses the random file name as the output vmdk name...
sudo vmware-mount ubuntu-vmw6/*.vmdk 1 mnt
if [ $? -ne 0 ]
 then
	echo "Unable to mount drive, fatal error!"
	sudo vmware-mount -d mnt
	exit
else
	echo "Drive mounted."
fi

echo "=========================="
echo "==Copying Setup Scripts==="
echo "=========================="

#set the mirror that the vm should be using to download sources, making sure multiverse is in there for aac/etc
echo "deb http://archive.ubuntu.com/ubuntu $RELEASE main restricted universe multiverse" > sources.list
echo "deb http://archive.ubuntu.com/ubuntu $RELEASE-updates main restricted universe multiverse" >> sources.list
echo "deb http://archive.canonical.com/ubuntu $RELEASE partner" >> sources.list
echo "deb http://security.ubuntu.com/ubuntu $RELEASE-security main restricted universe multiverse" >> sources.list
echo "deb http://ppa.launchpad.net/openjdk/ppa/ubuntu lucid main" >> sources.list

#copy sources list into the vm image
sudo mv sources.list mnt/etc/apt/sources.list

sudo ln -s /opt/matterhorn/felix/bin/start_matterhorn.sh mnt/home/$USERNAME/startup.sh
sudo ln -s /opt/matterhorn/felix/bin/shutdown_matterhorn.sh mnt/home/$USERNAME/shutdown.sh
sudo ln -s /opt/matterhorn/felix/bin/matterhorn_init_d.sh mnt/etc/init.d/matterhorn

sudo mkdir mnt/opt/matterhorn

sudo cp matterhorn_setup.sh mnt/home/$USERNAME/matterhorn_setup.sh
#change matterhorn_setup.sh to use the correct svn repo path"
sudo sed -i "s'OC_URL=.*$'OC_URL=$MATTERHORN_SVN'" mnt/home/$USERNAME/matterhorn_setup.sh
sudo echo "if [ -e /home/$USERNAME/matterhorn_setup.sh -a ! -e /home/$USERNAME/.matterhorn_setup.complete ]; then" >> mnt/home/$USERNAME/.bashrc
sudo echo "  /home/$USERNAME/matterhorn_setup.sh && touch /home/$USERNAME/.matterhorn_setup.complete" >> mnt/home/$USERNAME/.bashrc
sudo echo "fi" >> mnt/home/$USERNAME/.bashrc

echo "============================"
echo "==Installing Apache Felix==="
echo "============================"

if [ ! -e felix-framework-3.2.2 ]; then
  wget http://archive.apache.org/dist/felix/org.apache.felix.main.distribution-3.2.2.tar.gz
  tar -xzf org.apache.felix.main.distribution-3.2.2.tar.gz
  cd felix-framework-3.2.2
  svn co --force $MATTERHORN_SVN/docs/felix ./
  svn revert -R *
else
  cd felix-framework-3.2.2
  svn up
fi
cd ..

#copy felix files to vm
sudo cp -rv felix-framework-3.2.2 mnt/opt/matterhorn/felix

echo "=========================="
echo "=====Fetching Opencast===="
echo "=========================="

#check out svn
if [ -e matterhorn_source ]; then
  cd matterhorn_source
  svn up
  cd ..
else
  svn co $MATTERHORN_SVN matterhorn_source
fi

sudo cp -r matterhorn_source mnt/opt/matterhorn/

#Nuke the existing config
sudo sed -i "s/\$USER/$USERNAME/g" mnt/opt/matterhorn/felix/bin/matterhorn_init_d.sh
sudo chown -R $USER:$USER mnt/opt/matterhorn/
export OC_REV=`svn info matterhorn_source | awk /Revision/ | cut -d " " -f 2`

echo "=========================="
echo "=====Building Opencast===="
echo "=========================="

#get maven to update whatever dependancies we might have for opencast
cd matterhorn_source
export MAVEN_OPTS='-Xms256m -Xmx960m -XX:PermSize=64m -XX:MaxPermSize=150m'
mvn install -U -DskipTests -Dmaven.repo.local=$M2/repository -DdeployTo=$HOME/mnt/opt/matterhorn/felix/matterhorn -P admin,dist,engage,worker,workspace,serviceregistry,directory-db,capture,oaipmh
cd ..

#copy the maven repo across
sudo cp -r $M2 mnt/home/$USERNAME/.m2

echo "=========================="
echo "========Final Setup======="
echo "=========================="

# copy mediainfo 0.7.19
sudo cp mediainfo mnt/usr/local/bin/
sudo cp libmediainfo.a mnt/usr/local/lib/
sudo cp libmediainfo.la mnt/usr/local/lib/

#give opencast user rights for /opt/matterhorn
sudo chown -R 1000:1000 mnt/opt/matterhorn

#write environment variables to login file
echo "export OC=/opt/matterhorn" >> mnt/home/$USERNAME/.bashrc
echo "export MATTERHORN_HOME=/opt/matterhorn" >> mnt/home/$USERNAME/.bashrc
echo "export FELIX_HOME=\$MATTERHORN_HOME/felix" >> mnt/home/$USERNAME/.bashrc
echo "export M2_REPO=/home/$USERNAME/.m2/repository" >> mnt/home/$USERNAME/.bashrc
echo "export OC_URL=$MATTERHORN_SVN" >> mnt/home/$USERNAME/.bashrc
echo "export JAVA_HOME=/usr/lib/jvm/default-java" >> mnt/home/$USERNAME/.bashrc
echo "export MAVEN_OPTS=\"-Xms256m -Xmx512m -XX:PermSize=64m -XX:MaxPermSize=128m\"" >> mnt/home/$USERNAME/.bashrc

#lets set opencast to own her files
sudo chown -R 1000:1000 mnt/home/$USERNAME

#unmount the vm disk image and cleanup
sudo vmware-mount -d mnt
sleep 2
sudo rmdir mnt

echo "================================="
echo "=====Image Built, compressing===="
echo "================================="

#archive it all for download
echo "Building archive opencast-$OC_REV.zip."
mv ubuntu-vmw6/*.vmdk ubuntu-vmw6/disk0.vmdk
sed -i "s/ide0:0.fileName =.*/ide0:0.fileName = \"disk0.vmdk\"/" ubuntu-vmw6/opencast.vmx
sudo chown -R `id -u`:`id -g` ubuntu-vmw6
mv ubuntu-vmw6 opencast-$OC_REV
echo "Console Username: $USERNAME" > opencast-$OC_REV/README
echo "Console Password: $PASSWORD" >> opencast-$OC_REV/README
zip -db -r -9 opencast-$OC_REV.zip opencast-$OC_REV
7z a -t7z -m0=lzma -mx=9 -mfb=64 -md=32m -ms=on opencast-$OC_REV.7z opencast-$OC_REV

echo "==========================================================="
echo "================Compression Complete, signing=============="
echo "=====This is optional, kill the process to skip signing===="
echo "==========================================================="

gpg --armor -b opencast-$OC_REV.zip
gpg --armor -b opencast-$OC_REV.7z
md5sum opencast-$OC_REV* > opencast-$OC_REV.md5
gpg --armor -b opencast-$OC_REV.md5
