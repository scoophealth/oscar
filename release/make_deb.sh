#!/bin/bash

# release/make_deb.sh
# makes a debian installer release from source

#==============================================================
# Copyright Peter Hutten-Czapski 2012 released under the GPL v2
#==============================================================


# TODO Check the dir from which you are being called
# TODO Grep and cat up the updates from 12 and newer

SRC=~/git/oscar

if [ ! -f $SRC/target/oscar-SNAPSHOT.war ] ; then
	echo "This script needs a oscar-SNAPSHOT.war build of OSCAR in ${SRC}/target/"
	echo "please"
	echo "cd ${SRC}"
	echo "and then do a maven build"
	echo "mvn"
	echo "Then change back to this directory and run again"	
	exit 1
fi

# name of properties file and war
PROGRAM=oscar-SNAPSHOT
# The database should be in the oscar_ series, MySQL conventions don't allow . in db_names
db_name=oscar_snapshot
# Versioning conventions need a number so use a dummy lower than current releases
VERSION=8
TODAY=$(date)
build=Trunk
buildDateTime=${TODAY}
REVISION=Git
ICD=9
# currently we support Tomcat 6
TOMCAT=tomcat6
C_HOME=/usr/share/${TOMCAT}/
C_BASE=/var/lib/${TOMCAT}/
tomcat_path=${C_HOME}
DEBNAME="oscar_mcmaster$VERSION-$REVISION"


echo "cleaning up"
rm -R ./${DEBNAME}/
rm -R -f ./oscar_documents

# echo "loading control scripts"
mkdir -p ./${DEBNAME}/DEBIAN/

echo changelog

sed \
-e 's/yyy-x.x/'"$VERSION"'-'"$REVISION"'/' \
-e 's/oscarprogram/'"$PROGRAM"'/' \
-e 's/build xxx/build '"$BUILD"'/' \
-e 's/releasedate/'"$TODAY"'/' \
changelog > ./${DEBNAME}/DEBIAN/changelog


chmod 644 ./${DEBNAME}/DEBIAN/changelog

echo config
sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
-e 's/^db_name.*/db_name='"$db_name"'/' \
-e 's/^VERSION.*/VERSION='"$VERSION"'/' \
-e 's/^REVISION.*/REVISION='"$REVISION"'/' \
-e 's/^buildDateTime.*/buildDateTime=\"'"$buildDateTime"'\"/' \
-e 's/^TOMCAT.*/TOMCAT=\"'"$TOMCAT"'\"/' \
-e 's%^C_HOME.*%C_HOME='"$C_HOME"'%' \
-e 's%^C_BASE.*%C_BASE='"$C_BASE"'%' \
-e 's%^tomcat_path.*%tomcat_path='"$tomcat_path"'%' \
config > ./${DEBNAME}/DEBIAN/config

chmod 755 ./${DEBNAME}/DEBIAN/config

sed -e 's/Version: 8-x.x/Version: '"$VERSION"'-'"$REVISION"'/' \
control > ./${DEBNAME}/DEBIAN/control

chmod 644 ./${DEBNAME}/DEBIAN/control

sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
-e 's/^db_name.*/db_name='"$db_name"'/' \
-e 's/^VERSION.*/VERSION='"$VERSION"'/' \
-e 's/^REVISION.*/REVISION='"$REVISION"'/' \
-e 's%^C_HOME.*%C_HOME='"$C_HOME"'%' \
-e 's%^C_BASE.*%C_BASE='"$C_BASE"'%' \
postinst > ./${DEBNAME}/DEBIAN/postinst

chmod 755 ./${DEBNAME}/DEBIAN/postinst

sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
-e 's/^db_name.*/db_name='"$db_name"'/' \
-e 's/^VERSION.*/VERSION='"$VERSION"'/' \
-e 's/^REVISION.*/REVISION='"$REVISION"'/' \
-e 's%^C_HOME.*%C_HOME='"$C_HOME"'%' \
-e 's%^C_BASE.*%C_BASE='"$C_BASE"'%' \
postrm > ./${DEBNAME}/DEBIAN/postrm

chmod 755 ./${DEBNAME}/DEBIAN/postrm

sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
-e 's/^db_name.*/db_name='"$db_name"'/' \
-e 's/^VERSION.*/VERSION='"$VERSION"'/' \
-e 's/^REVISION.*/REVISION='"$REVISION"'/' \
-e 's%^C_HOME.*%C_HOME='"$C_HOME"'%' \
-e 's%^C_BASE.*%C_BASE='"$C_BASE"'%' \
prerm > ./${DEBNAME}/DEBIAN/prerm

chmod 755 ./${DEBNAME}/DEBIAN/prerm

cp -R rules ./${DEBNAME}/DEBIAN/
chmod 755 ./${DEBNAME}/DEBIAN/rules

cp -R templates ./${DEBNAME}/DEBIAN/

chmod 644 ./${DEBNAME}/DEBIAN/templates
# echo "loading utilities and properties"
mkdir -p ./${DEBNAME}/usr/share/OscarMcmaster/

cp -R demo.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R drugref.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R OfficeCodes.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R OLIS.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R Oscar11_to_oscar_12.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R oscar10_12_to_Oscar11.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R oscar_12_to_oscar_12_1.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R RNGPA.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R special.sql ./${DEBNAME}/usr/share/OscarMcmaster/
cp -R unDemo.sql ./${DEBNAME}/usr/share/OscarMcmaster/

# use the stock properties file as config will fix as needed
cp -R $SRC/src/main/resources/oscar_mcmaster.properties ./${DEBNAME}/usr/share/OscarMcmaster/oscar_mcmaster.properties


cat $SRC/database/mysql/oscarinit.sql \
$SRC/database/mysql/oscarinit_ON.sql \
$SRC/database/mysql/oscardata.sql \
$SRC/database/mysql/oscardataON.sql \
$SRC/database/mysql/icd${ICD}.sql \
$SRC/database/mysql/caisi/initcaisi.sql \
$SRC/database/mysql/caisi/initcaisidata.sql \
$SRC/database/mysql/icd${ICD}_issue_groups.sql \
$SRC/database/mysql/measurementMapData.sql \
$SRC/database/mysql/expire_oscardoc.sql \
> ./${DEBNAME}/usr/share/OscarMcmaster/OscarON${VERSION}.sql


cat $SRC/database/mysql/oscarinit.sql \
$SRC/database/mysql/oscarinit_BC.sql \
$SRC/database/mysql/oscardata.sql \
$SRC/database/mysql/oscardataBC.sql \
$SRC/database/mysql/icd${ICD}.sql \
$SRC/database/mysql/caisi/initcaisi.sql \
$SRC/database/mysql/caisi/initcaisidata.sql \
$SRC/database/mysql/icd${ICD}_issue_groups.sql \
$SRC/database/mysql/measurementMapData.sql \
$SRC/database/mysql/expire_oscardoc.sql \
> ./${DEBNAME}/usr/share/OscarMcmaster/OscarBC${VERSION}.sql

cp -R README.txt ./${DEBNAME}/usr/share/OscarMcmaster/

#cp -R $SRC/utils/wkhtmltopdf-i386 ./${DEBNAME}/usr/share/OscarMcmaster/

echo "getting and loading wars"
mkdir -p ./${DEBNAME}${C_BASE}webapps/

echo "drugref up"

wget http://drugref2.googlecode.com/files/drugref.war
mv drugref.war ./${DEBNAME}${C_BASE}webapps/drugref.war

mv $SRC/target/oscar-SNAPSHOT.war ./${DEBNAME}${C_BASE}webapps/$PROGRAM.war

# send the unpacked war into the webapps folder, then it won't clobber documents when installed

mkdir -p ./${DEBNAME}${C_BASE}webapps/OscarDocument/
git clone git://oscarmcmaster.git.sourceforge.net/gitroot/oscarmcmaster/oscar_documents
mv ./oscar_documents/src/main/webapp/oscar_mcmaster/ ./${DEBNAME}${C_BASE}webapps/OscarDocument/  
cp $SRC/utils/wkhtmltopdf-i386 ./${DEBNAME}${C_BASE}webapps/OscarDocument/wkhtmltopdf-i386
chown -R ${tomcat} ./${DEBNAME}${C_BASE}webapps/OscarDocument


echo "now invoking dpkg -b ${DEBNAME}"

dpkg -b ${DEBNAME}