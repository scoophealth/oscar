#!/bin/bash

# release/make_beta.sh
# makes a debian installer release from source

#===================================================================
# Copyright Peter Hutten-Czapski 2012-2015 released under the GPL v2
#===================================================================


# for OSCAR 14
# v 1 - pre-release
# v 2 - added in oscar_documents incoming docs directories needed
#     - added in code to set the billingcenter 
# removed Replaces: oscar-mcmaster from control
# replaced oscar-mcmaster with oscar-emr in templates etc
# v 3 - added x86_64 detection for wkhtmltopdf
# v 4 - updates for may 2014 alpha
# v 5 - directed to trunk for testing
# v 6 - huge number of bug fixes
# v 7 - added in a few schema changes
# v 8 - changed db switch to useOldAliasMetadataBehavior=true & jdbcCompliantTruncation=false
# v 9 - switched from trunk to true oct2014AlphaMaster
# v 10 - removed the external perscriber settings
# v 11 - updated drugref data to Nov 22, 2014
# v 12 - fixed config to echo url for hcv service, and adjusted min stack to 256k
# v 13 - changed wget to ./wget older version and substituted in TEST PATIENT into the demo and undemo.sql
# v 14 - ported lintian fixes from v63 of the 12_1 release installation script and added wkhtmltopdf dependency
# v 15 - switched to tomcat7, updated drugref to Feb 10 2015, cleaned changelog and disabled updating
# v 16 - added Rourke eForm installation under liscence

# for OSCAR 15
# v 0 - fixed the Rourke unzip and copy and sent its console output to the install log
# v 1 - fixed patch.sql to reference oscar_15 fixed oscar.properties to match latest and draw from OSCAR 15 branch 
# v 2 - added DEMOGRAPHIC_PATIENT_CLINIC_STATUS=true (REALLY who wants it turned off as default?)
# v 3 - fixed oscar_12_1_1_to_oscar_15.sql, or at least removed the duplicates!
# v 4 - updated schema definitions to update-2015-04-24.sql applied
# v 5 - Now drawing on the OSCAR's RELEASE_15_BETA and update-2015-04-29.sql applied and Jenkins port changed temporarily
# v 6 - Jenkins port reverted to 11042, drugref schema tweaked/updated to May 2015, added ALT_PATIENT_DETAILS_UI property
# v 7 - updated drugref2 to last stable build on Jenkins which now can handle HC zip change May 5 2015
# v 8 - fixed nsert typo in the sql's
# v 9 - fixed oscar_14 to oscar_15 references in the conversion script
#     - modified SQL to reflect update-2015-05-15.sql 
#     - added more debug to purge script
#     - Set SINGLE_PAGE_CHART=true to Show link to single page chart in classic appointment screen
# v 10 - added sort_down_mfg_tagged_generics generic drug flag to drugref properties and, June 24 new API for drugs by DIN in drugref
# v 11 - added cipher settings for server.xml
# v 12 - altered drugref properties file to drugref2.properties and made them use the packaged sql as update with added indices
#      - database updates appended in OscarON15 OscarBC15 and oscar_12_1_1_to_oscar_15 sqls to update-2015-07-08.sql
# v 13 - added unzip as dependency
# v 14 - updated drugref war and drugref and oscar schemas to Oct 7, 2015
# v 15 - substituted new backup.sh, reviewed prerm, config, now will upgrade previous 15 debs without rewriting properties
# v 16 - updated demo and undemo sql's
# v 17 - updated sql's to update-2015-12-08 and update-2015-11-24
# v 18 - updated sql's to update-2016-01-25 and tweaked the quicklist for appts for GP (recalled)
# v 19 - bugfixes to the 12.1 to 15 conversion script and corrected WKHTMLTOPDF command property and removed its dependency as deb
# v 20 - more sql tweaking and addition of legacyMyISAM.sql for upgrade purposes
# v 21 - Fixed demo.sql to properly enroll both demo patients
# v 22 - Changed output to rename as ~, fixed HL7 labs to view, vacancy table fix
# v 23 - Switched this script to standard curl from our hacked wget for accessing files from the web
# v 24 - Updated reOscar to reOscar2.sh, added ichppccode table, missig HRMDocuments column and many indices to patch.sql
# v 25 - More indices added to patch.sql and web.xml added to OscarDocuments and moved OscarDocuments to prevent webshell exploit
# v 26 - Updated Rich Text Letter and moved OscarDocuments to prevent webshell exploit
# v 27 - Bypassed confirmation screen for upgrades so that they can run unattended
# v 28 - Fixes in patch.sql
# v 29 - Changed OscarDocuments to the <context> and added BORN eforms to Ontario Installs, new source for GEOIP data
# v 30 - Updated backup2.sh to use new document directory
#      - oscar.properties to use master Mar 17, 2016 
#      - added sed entries to config to normalise paths in the properties file
#      - removed a lot of redundant echo to properties file in config
#      - area code lookup to handle space in city name
#      - tickler_update forcing to status to ABD in conversion script
#      - Rourke 2014 updated to latest as of April 15, 2016
# v 31 - added reOscar to config and changed way to test/set backup scripts
#      - Rourke 2014 updated to latest as of April 17, 2016
# v 32 - Switched config to set BASE_DOCUMENT_DIR
#      - added weekly tomcat restart cronjob to config
# v 33 - added gateway.cron to oscar-emr and in config
# v 34 - updated drugref to current on 2016-04-22
#      - added default data to faxconfig so it would work out of the box
#      - fixed /etc/default/tomcat7 to use Oracle java
# v 35 - sanity check to ensure setting tomcat to look for an actual Oracle Java
# v 36 - updated OscarDocuments editControl.js to provide backwards compatibility for old RTL  May 16, 2016
#      - added Ontario Lab eform
# v 37 - fixed OscarBC15.sql to at least load!
# v 38 - added mariadb-server as alternate dependency to mysql in control
# v 39 - fixed bug 4385 lastdate spelling line 221 postinst
# v 40 - deleting tailing zero doses and adding leading 0 to decimal doses
#      - added tallMAN scripts for optional use
#      - updated drugref to current on 2016-07-26
# v 41 - adjusted permissions on scripts and oscar.properties
# v 42 - DEB update now updates drugref and the older Rourke installed prior to v 30
# v 43 - fixed missing consent tables for build 437 oscar_emr15-42~437.deb and oscar_mcmaster.properties to match
# v 44 - temporary patch to tickler to get rid of NULL's in catgeory_id REVERTED
# v 45 - added run_rxquery.sh for Medispan reporting without configuring or adding to crontab
# v 46 - switched from repo drugref.sql to one in local directory
# v 47 - outdated property indivica_rx_enhance removed
#      - rx_enhance set to false
#      - fixed patch.sql for ichpicc
# v 48 - moved up table definition `indicatorTemplate` in patch to fix fresh install bug
#      - probing eth1 instead of eth0 for local ip
# v 49 - disabled plugin requirement for root user
#      - removed a tailing / in the properties file for tomcat
#      - updated drugref data to Jan 18, 2017
#      - updated area code lookup to use https
# v 50 - Fixed backupscript to daily delete stale Full backups if $COMPLETE_BACKUP
#      - and to trigger Full backups every $DAYS_TO_KEEP if not
# v 51 - updated patch with missing update-2015-12-07.sql and 2017-01-25.sql
# v 52 - updated patch with UNIQUE constrained temp indices 
#      - added latebreaking properties, at least for consult module
#      - drugref2 to build 12
# v 53 - added missing properties
# v 54 - updated drugref to Feb 17, 2017 and removed addition of index in postinst
# v 55 - fixed order in patch.sql to add LookupList.listTitle prior to filling it
# v 56 - fixed spelling mistake in patch.sql for hort_term

# v 57 - migrated to jenkins/bitbucket pointing to the stable release branch

DEB_SUBVERSION=57

PROGRAM=oscar
PACKAGE=oscar-emr

# The database should be in the oscar_ series, MySQL conventions don't allow . in db_names
db_name=oscar_15

## database switches are needed to provide expected behavior for OSCAR 15
## enforce UTF-8 encoding so that foreign characters are stored 一種語言永遠不夠 
## handle 0000-00-00 date errors by rounding to 0001-01-01
## allow hibernate to alter column names
## tolerate fields without default values that are not named in the query
db_switch=\'?characterEncoding=UTF-8\\\&zeroDateTimeBehavior=round\\\&useOldAliasMetadataBehavior=true\\\&jdbcCompliantTruncation=false\'

# Debian versioning conventions don't allow _ so use .
VERSION=15
PREVIOUS=12_1

# ... and of course Jenkins is using another convention
WGET_VERSION=oscar-stable

# and the target of mvn 3 has to be different than for mvn 2 so
# for TRUNK and 15 BETA oscar-14.0.0-SNAPSHOT.war
TARGET=oscar-14.0.0-SNAPSHOT.war

echo "grep the build from Jenkins" 

##curl --insecure -SSLv3 -o lastStableBuild https://demo.oscarmcmaster.org:11042/job/$WGET_VERSION/lastStableBuild/

curl -o lastStableBuild http://jenkins.oscar-emr.com:8080/job/$WGET_VERSION/lastStableBuild/
##TEMPORARILY USE THE LAST BUILD REGARDLESS OF STABILITY WHILE CONGURATION CHANGES ARE MADE
##curl -o lastStableBuild http://jenkins.oscar-emr.com:8080/job/$WGET_VERSION/lastBuild/

## <title>may2014alphaMaster #13 [Jenkins]</title>
# oct2014AlphaMaster #1 [Jenkins]
BUILD=$(grep '<title>' lastStableBuild | head -n 1 | sed "s/<title>$WGET_VERSION #\([0-9]*\).*/\1/;s/^[[:space:]]*//;s/[[:space:]]*$//")

echo "+++++++++++++++++++++++"
echo build=$BUILD
buildDateTime=$(grep '       (' lastStableBuild | head -n 1 |sed 's/^[[:space:]]*(//;s/)[[:space:]]*$//')
echo buildDateTime=$buildDateTime
SHA1=$(grep 'Revision' lastStableBuild | head -n 1 |sed 's/.*Revision.* \(.*\)/\1/')
echo SHA1=$SHA1

# you can tick up when a newer build of the installer is made 
# or when the release tag needs to change eg beta to RC
# TRUNK
REVISION=${DEB_SUBVERSION}~${BUILD}
echo REVISION=$REVISION
ICD=9

# For simplicity lets pick Tomcat 7
TOMCAT=tomcat7
C_HOME=/usr/share/${TOMCAT}/
C_BASE=/var/lib/${TOMCAT}/
# FILEREPO=~/Documents/release/
tomcat_path=${C_HOME}
TODAY=$(date)

# used to pick up virgin properties file and if building a deb directly from source
SRC=~/git/oscar


# TODO Check the dir from which you are being called


DEBNAME="oscar_emr$VERSION-$REVISION"


if [ -d "$DEBNAME" ]; then
	echo prexisting directory with this build found
	SKIP_NEW_WAR=true
	rm -R ./${DEBNAME}/
else
	rm *.war
fi

echo "cleaning up"


rm changes
rm tmp*
rm -R -f ./oscar_documents

# echo "loading documents"
mkdir -p ./${DEBNAME}/usr/share/doc/${PACKAGE}/
cp -R copyright ./${DEBNAME}/usr/share/doc/${PACKAGE}/

# echo "loading control scripts"
mkdir -p ./${DEBNAME}/DEBIAN/

echo "changelog"

curl -o changes http://jenkins.oscar-emr.com:8080/job/oscar-stable/changes

sed \
-e 's/yyy-1.0/'"$VERSION"'-'"$BUILD"'/' \
-e 's/package/'"$PROGRAM"'/' \
-e 's/releasedate/'"$TODAY"'/' \
changestemplate > tmp

head -n 1 tmp > tmp2
#grep "^                [a-zA-Z#]" changes | tail -n +3 |sed 's/&039\;/'/;s/^[[:space:]]*/  *   /;s/[[:space:]]*$//' > tmp3
#grep "^                [ a-zA-Z#]" changes | tail -n +13 |sed 's/&#039\;//;s/&nbsp\;/ /;s/&quot\;/\"/;s/&quot\;/\"/;s/&quot\;/\"/;s/&quot\;/\"/;s/id:\;//;s/ID://;s/ID \#//;s/Bug \#//;s/Bug ID //;s/Oscar Host - //;s/\#//;s/^[[:space:]]*/  * /;s/[[:space:]]*$//' >tmp3
# lots of cleanup to extract the pith from the changes and then truncate at 80 columns as per DEBIAN requirement

grep "<\/h2>"  changes | sed 's/^[[:space:]]*/  * /;s/, [0-9]* [0-9]*:[0-9][0-9]:[0-9][0-9] [A|P]M//;s/<\/a><\/h2><ol><li>//;s/OSCAREMR-//;s/[[:space:]]*$//' > tmp3

sed -r 's/(^.{80}).*/\1/' tmp3 > tmp4
tail -n 1 tmp > tmp5


cat tmp2 \
tmp4 \
tmp5 \
> changelog.Debian

curl  -o drugrefChanges http://jenkins.oscar-emr.com:8080/job/drugref2/changes

#grep "^                [ a-zA-Z#]" drugrefChanges | sed 's/&#039\;//;s/&nbsp\;/ /;s/&quot\;/\"/;s/&quot\;/\"/;s/&quot\;/\"/;s/&quot\;/\"/;s/id:\;//;s/ID://;s/ID \#//;s/Bug \#//;s/Bug ID //;s/Oscar Host - //;s/\#//;s/^[[:space:]]*/  * /;s/[[:space:]]*$//' >drugrefChangesClean

grep "<\/h2>"  drugrefChanges | sed 's/^[[:space:]]*/  * /;s/, [0-9]* [0-9]*:[0-9][0-9]:[0-9][0-9] [A|P]M//;s/<\/a><\/h2><ol><li>//;s/OSCAREMR-//;s/[[:space:]]*$//' > drugrefChangesClean

echo "+++++++++++++++++++++++"
echo build=$BUILD
buildDateTime=$(grep '       (' lastStableBuild | head -n 1 |sed 's/^[[:space:]]*(//;s/)[[:space:]]*$//')
echo buildDateTime=$buildDateTime
SHA1=$(grep 'Revision' lastStableBuild | head -n 1 |sed 's/.*Revision.* \(.*\)/\1/')
echo SHA1=$SHA1
echo DEBNAME=${DEBNAME}
echo ""
echo "OSCAR changes"
head -n 5 changelog.Debian
echo ""
echo "Drugref2 Changes"
cat drugrefChangesClean
echo "+++++++++++++++++++++++"

rm lastStableBuild
rm drugrefChangesClean

gzip -9 changelog.Debian
mv changelog.Debian.gz ./${DEBNAME}/usr/share/doc/${PACKAGE}/
#  6      4     4
# user   group  world
# r+w    r      r
# 4+2+0  4+0+0  4+0+0  = 644
#chmod 644 ./${DEBNAME}/DEBIAN/changelog

echo config
sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
-e 's/^PACKAGE.*/PACKAGE='"$PACKAGE"'/' \
-e 's/^db_name.*/db_name='"$db_name"'/' \
-e 's/^db_switch.*/db_switch='"$db_switch"'/' \
-e 's/^VERSION.*/VERSION='"$VERSION"'/' \
-e 's/^PREVIOUS.*/PREVIOUS='"$PREVIOUS"'/' \
-e 's/^REVISION.*/REVISION='"$REVISION"'/' \
-e 's/^buildDateTime.*/buildDateTime=\"'"$buildDateTime"'\"/' \
-e 's/^TOMCAT.*/TOMCAT=\"'"$TOMCAT"'\"/' \
-e 's%^C_HOME.*%C_HOME='"$C_HOME"'%' \
-e 's%^C_BASE.*%C_BASE='"$C_BASE"'%' \
-e 's%^tomcat_path.*%tomcat_path='"$tomcat_path"'%' \
config > ./${DEBNAME}/DEBIAN/config

# 7       5     5
# user   group  world
# r+w+x  r+x    r+x
# 4+2+1  4+0+1  4+0+1  = 755
chmod 755 ./${DEBNAME}/DEBIAN/config

sed -e 's/Version: 8-x.x/Version: '"$VERSION"'-'"$REVISION"'/' \
control > ./${DEBNAME}/DEBIAN/control

chmod 644 ./${DEBNAME}/DEBIAN/control

sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
-e 's/^PACKAGE.*/PACKAGE='"$PACKAGE"'/' \
-e 's/^db_name.*/db_name='"$db_name"'/' \
-e 's/^VERSION.*/VERSION='"$VERSION"'/' \
-e 's/^PREVIOUS.*/PREVIOUS='"$PREVIOUS"'/' \
-e 's/^REVISION.*/REVISION='"$REVISION"'/' \
-e 's/^TOMCAT.*/TOMCAT='"$TOMCAT"'/' \
-e 's/^buildDateTime.*/buildDateTime=\"'"$buildDateTime"'\"/' \
-e 's%^C_HOME.*%C_HOME='"$C_HOME"'%' \
-e 's%^C_BASE.*%C_BASE='"$C_BASE"'%' \
postinst > ./${DEBNAME}/DEBIAN/postinst

chmod 755 ./${DEBNAME}/DEBIAN/postinst

sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
-e 's/^PACKAGE.*/PACKAGE='"$PACKAGE"'/' \
-e 's/^db_name.*/db_name='"$db_name"'/' \
-e 's/^VERSION.*/VERSION='"$VERSION"'/' \
-e 's/^PREVIOUS.*/PREVIOUS='"$PREVIOUS"'/' \
-e 's/^REVISION.*/REVISION='"$REVISION"'/' \
-e 's/^TOMCAT.*/TOMCAT=\"'"$TOMCAT"'\"/' \
-e 's/^buildDateTime.*/buildDateTime=\"'"$buildDateTime"'\"/' \
-e 's%^C_HOME.*%C_HOME='"$C_HOME"'%' \
-e 's%^C_BASE.*%C_BASE='"$C_BASE"'%' \
postrm > ./${DEBNAME}/DEBIAN/postrm

chmod 755 ./${DEBNAME}/DEBIAN/postrm

sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
-e 's/^PACKAGE.*/PACKAGE='"$PACKAGE"'/' \
-e 's/^db_name.*/db_name='"$db_name"'/' \
-e 's/^VERSION.*/VERSION='"$VERSION"'/' \
-e 's/^PREVIOUS.*/PREVIOUS='"$PREVIOUS"'/' \
-e 's/^REVISION.*/REVISION='"$REVISION"'/' \
-e 's/^TOMCAT.*/TOMCAT=\"'"$TOMCAT"'\"/' \
-e 's%^C_HOME.*%C_HOME='"$C_HOME"'%' \
-e 's%^C_BASE.*%C_BASE='"$C_BASE"'%' \
prerm > ./${DEBNAME}/DEBIAN/prerm


chmod 755 ./${DEBNAME}/DEBIAN/prerm

cp -R templates ./${DEBNAME}/DEBIAN/

chmod 644 ./${DEBNAME}/DEBIAN/templates

# echo "loading utilities and properties"
mkdir -p ./${DEBNAME}/usr/share/${PACKAGE}/

# echo make up the appropriate source.txt for this build
sed -e 's/SHA1/'"$SHA1"'/' \
-e 's/yyy-x.x/'"$VERSION"'-'"$REVISION"'/' \
-e 's/oscarprogram/'"$PROGRAM"'/' \
-e 's/build xxx/build '"$BUILD"'/' \
source.txt > ./${DEBNAME}/usr/share/${PACKAGE}/source.txt

# echo make up the appropriate rebooting script
sed -e 's/^PROGRAM.*/PROGRAM='"$PROGRAM"'/' \
reOscar2.sh > ./${DEBNAME}/usr/share/${PACKAGE}/reOscar.sh
chmod 711 ./${DEBNAME}/usr/share/${PACKAGE}/reOscar.sh
cp gateway.cron ./${DEBNAME}/usr/share/${PACKAGE}/gateway.cron
chmod 755 ./${DEBNAME}/usr/share/${PACKAGE}/gateway.cron

cd NDSS/
zip ../ndss.zip *
cd ../
cd rbr2014/
zip ../rbr2014.zip *
cd ../

cp -R demo.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R drugref.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R OfficeCodes.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R OLIS.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R Oscar11_to_oscar_12.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R oscar10_12_to_Oscar11.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R oscar_12_to_oscar_12_1.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R rbr2014.zip ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R ndss.zip ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R RourkeEform.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R ndss.sql ./${DEBNAME}/usr/share/${PACKAGE}/

cp -R tallMAN.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R tallMANdrugref.sql ./${DEBNAME}/usr/share/${PACKAGE}/

cp -R ontarioLab.sql ./${DEBNAME}/usr/share/${PACKAGE}/

rm ndss.zip
rm rbr2014.zip

chmod 644 ./${DEBNAME}/usr/share/${PACKAGE}/rbr2014.zip
chmod 644 ./${DEBNAME}/usr/share/${PACKAGE}/ndss.zip

cp -R patch.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R oscar_12_1_to_oscar_12_1_1.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R oscar_12_1_1_to_oscar_15.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R legacyMyISAM.sql ./${DEBNAME}/usr/share/${PACKAGE}/

# use the stock properties file as config will fix as needed
# use the specific one for this build from this makers folder
cp -R oscar_mcmaster.properties ./${DEBNAME}/usr/share/${PACKAGE}/oscar_mcmaster.properties

cp -R OscarON${VERSION}.sql ./${DEBNAME}/usr/share/${PACKAGE}/OscarON${VERSION}.sql

cp -R OscarBC${VERSION}.sql ./${DEBNAME}/usr/share/${PACKAGE}/OscarBC${VERSION}.sql
cp -R README.txt ./${DEBNAME}/usr/share/${PACKAGE}/

cp -R RNGPA.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R special.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R unDemo.sql ./${DEBNAME}/usr/share/${PACKAGE}/
cp -R OLIS.sql ./${DEBNAME}/usr/share/${PACKAGE}/

cp -R server.xml ./${DEBNAME}/usr/share/${PACKAGE}/

cp -R run_query.sh ./${DEBNAME}/usr/share/${PACKAGE}/run_rxquery.sh
chmod 711 ./${DEBNAME}/usr/share/${PACKAGE}/run_rxquery.sh

# now the backup scripts
cp -R backup2.sh ./${DEBNAME}/usr/share/${PACKAGE}/oscar_backup.sh
chmod 711 ./${DEBNAME}/usr/share/${PACKAGE}/oscar_backup.sh
mkdir -p ./${DEBNAME}/usr/share/${PACKAGE}/oscar_backup/
cp -R restore2.sh ./${DEBNAME}/usr/share/${PACKAGE}/restore.sh
chmod 711 ./${DEBNAME}/usr/share/${PACKAGE}/restore.sh

echo "getting and loading wars"
mkdir -p ./${DEBNAME}${C_BASE}webapps/

echo "build directory made to receive wars"


if [ "${SKIP_NEW_WAR}" = "true" ] ; then
	echo skipping redownloading of wars
else
    curl -o drugref2-1.0-SNAPSHOT.war http://jenkins.oscar-emr.com:8080/job/drugref2/lastSuccessfulBuild/org.drugref\$drugref2/artifact/org.drugref/drugref2/1.0-SNAPSHOT/drugref2-1.0-SNAPSHOT.war
    echo "drugref war up"
	curl -o $TARGET http://jenkins.oscar-emr.com:8080/job/oscar-stable/lastStableBuild/artifact/target/$TARGET
	
##TEMPORARILY USE THE LAST BUILD REGARDLESS OF STABILITY WHILE CONGURATION CHANGES ARE MADE
##	curl -o $TARGET http://jenkins.oscar-emr.com:8080/job/oscar-stable/lastBuild/artifact/target/$TARGET
    echo "oscar war up"
fi

cp drugref2-1.0-SNAPSHOT.war ./${DEBNAME}${C_BASE}webapps/drugref.war
cp $TARGET ./${DEBNAME}${C_BASE}webapps/$PROGRAM.war

##cd ../
##mvn -Dmaven.test.skip=true verify

# send the unpacked war into the webapps folder, then it won't clobber documents when installed

#mkdir -p ./${DEBNAME}${C_BASE}webapps/OscarDocument/

#until git clone git://git.code.sf.net/p/oscarmcmaster/oscar_documents; do
#  echo "Git clone disrupted, retrying in 2 seconds..."
#  sleep 2
#done

 
mkdir -p ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/${PROGRAM}/
cp -r OscarDocument/oscar/ ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/

echo "now adding in default inbox directories"
mkdir -p ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/${PROGRAM}/incomingdocs/
mkdir -p ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/${PROGRAM}/incomingdocs/1/Fax
mkdir -p ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/${PROGRAM}/incomingdocs/1/File
mkdir -p ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/${PROGRAM}/incomingdocs/1/Mail
mkdir -p ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/${PROGRAM}/incomingdocs/1/Refile

echo "now adding in Ontario Lab eform"
cp -R labDecisionSupport.js ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/${PROGRAM}/eform/images/
cp -R 4422-84labReq.png ./${DEBNAME}/usr/share/${PACKAGE}/OscarDocument/${PROGRAM}/eform/images/


echo "now invoking dpkg -b ${DEBNAME}"

dpkg -b ${DEBNAME}
echo ""
echo ""
echo "remember to"
echo scp ${DEBNAME}.deb peter_hc@frs.sourceforge.net:\"/home/frs/project/oscarmcmaster/Oscar\\ Debian\\+Ubuntu\\ deb\\ Package/\"
echo ""
echo "they you can"
echo wget http://sourceforge.net/projects/oscarmcmaster/files/Oscar\\ Debian\\+Ubuntu\\ deb\\ Package/${DEBNAME}.deb
echo "" 
md5sum ${DEBNAME}.deb





