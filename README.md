EVIDEO_1.1
==========

Project Sponsored by NMEICT of MHRD


Installation of EVIDEO_1.1

On Ubuntu/Debian Linux:

1. Prepare

    a. Creating EVIDEO Installation Directory

    sudo mkdir -p /opt/evideo
    sudo chown $USER:$GROUPS /opt/evideo

    b. Clone EVIDEO Project from Github

        git clone https://github.com/evideo-iitj/EVIDEO_1.1.git

    c. Install

    i. Java
        
        sudo apt-get install openjdk-6-jdk
        echo "export JAVA_HOME=/usr/lib/jvm/java-6-openjdk" >> ~/.bashrc
        source ~/.bashrc

    ii. Apache Maven2

        sudo apt-get install maven2

    iii. Apache Felix

        a) Download the latest version of Apache Felix Framework Distribution

        b) Unarchive

            tar xvf org.apache.felix.main.distribution-<version>.tar.gz

        c) Move to Matterhorn Installation Directory

            mv felix-framework-<version> /opt/matterhorn/felix

        d) Configure

           mkdir /opt/matterhorn/felix/load
           cp -rf /opt/matterhorn/matterhorn_1.3.1/docs/felix/* /opt/matterhorn/felix/

2. Configure

   Configure the main config file of your installation /opt/matterhorn/felix/conf/config.properties

     a. Set the Server
     
        org.opencastproject.server.url=http://<the URL of your server with port>
        
     b. Set the storage dir, we use /opt/matterhorn/felix/work in our implementation.
     
        org.opencastproject.storage.dir=/opt/matterhorn/felix/work/

3. Build

   a. Matterhorn

      export MAVEN_OPTS='-Xms256m -Xmx960m -XX:PermSize=64m -XX:MaxPermSize=256m'
      cd /opt/matterhorn/matterhorn_1.3.1
      mvn clean install -DdeployTo=/opt/matterhorn/felix/matterhorn

 b. Third Party Tools

     cd /opt/matterhorn/matterhorn_1.3.1/docs/scripts/3rd_party
     Read README file in 3rd_party folder for additional instructions

4. Run

   a. Export Environment Variables:

     echo "export M2_REPO=/home/$USER/.m2/repository" >> ~/.bashrc
     echo "export FELIX_HOME=/opt/matterhorn/felix" >> ~/.bashrc
     echo "export JAVA_OPTS='-Xms1024m -Xmx1024m -XX:MaxPermSize=256m'" >> ~/.bashrc
     source ~/.bashrc
   
   b. Run Matterhorn

    sh /opt/matterhorn/felix/bin/start_matterhorn.sh

   c. Browse http://localhost:8080/

   d. Stop Matterhorn (optional)

    sh /opt/matterhorn/felix/bin/stop_matterhorn.sh

5. Run Matterhorn as a Service

  Refer Official Matterhorn Server Installation(https://opencast.jira.com/wiki/display/mh13/Install+Source+Linux+v1.3) for more information.

6. Configuring the Matterhorn Datbase

  Refer Configure Matterhorn Database(https://opencast.jira.com/wiki/display/mh13/Configure+Matterhorn+Database+v1.3) for more information.
