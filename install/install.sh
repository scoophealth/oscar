# This script is going to install oscar

# Where the package untar
FROM=`pwd`

# Copy properties file to root
cp ${FROM}/oscar_mcmaster.properties /root
cp ${FROM}/oscar_security.properties /root
cp ${FROM}/thisStore /root

# Go to parent level
cd /


# Install java, mysql, tomcat and oscar-mcmaster
tar zxf ${FROM}/oscar_mcmaster1.0-tomcat-4.0.6.tar.gz
tar zxf ${FROM}/oscar_mcmaster1.0-mysql-3.23.53a.tar.gz
tar zxf ${FROM}/oscar_mcmaster1.0-java-1.4.0_01.tar.gz
tar zxf ${FROM}/oscar_mcmaster1.0-OscarDocument.tar.gz

# Create shortcut
cd /usr/local
ln -s jakarta-tomcat-4.0.6 tomcat
ln -s mysql-3.23.53a-pc-linux-gnu-i686 mysql
ln -s j2sdk1.4.0_01 java

# Apply Webapps
cd /usr/local/tomcat/webapps/
tar zxf ${FROM}/oscar_mcmaster1.0-webapps.tar.gz


# Add mysql user and group
groupadd mysql
useradd mysql -g mysql

chown mysql:root -R /usr/local/mysql
chown mysql:mysql -R /usr/local/mysql/data
# Ready

cat <<EOF

  *********************************************************************
  
  Now OSCAR has been installed. You have to setup the profile file. 
  Logout linux and relogin to update the profile setting, then run java
  If the libstdc6.so is missing, please install the compat-libstdc++ rpm
  included in the oscar-mcmaster package. 
  Please see ${FROM}/INSTALL for step-by-step instructions
  



  +--------------------------------------------------------+
  | Starting up OSCAR:                                     |
  | [1]  cd /usr/local/mysql                               |
  | [2]  ./bin/safe_mysqld &                               |
  | [3]  cd /usr/local/tomcat/webapps/                     |
  | [4]  ../bin/startup.sh                                 |
  | [5]  open the client browser and run                   |
  |      https://<yourhostname>:8443/oscar_mcmaster/       |
  |                                                        |
  | OSCAR LOGIN |  USER ID   |  PASSWORD   |  PIN          |
  | Admin       | oscaradmin |  mac2002    |  1117         |
  | Doctor      | oscardoc   |  mac2002    |  1117         |
  | Receptionist| oscarrep   |  mac2002    |  1117         | 
  |                                                        |                                                     
  |                                                        |
  | Shutting down OSCAR:                                   |                                                     
  | [1] cd /usr/local/tomcat/webapps/                      |
  | [2] ../bin/shutdown.sh                                 |                                                     
  | [3] cd /usr/local/mysql                                |
  | [4] ../bin/mysqladmin -p<your_db_password> shutdown    |                                                     
  |                                                        |
  +--------------------------------------------------------+



  ********************************************************************




EOF
