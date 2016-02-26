#!/usr/bin/env bash
# backup.sh
# a script file for OSCAR that copies compressed archives
# that have been date stamped for easy sorting
# to the backup folder usually where a browser can access
# from a given MySQL database and documents folder

# Modified from a script by Jay Gallagher by Thom Luxford
# Adapted for OSCAR 15 by Peter Hutten-Czapski
# Version 15.01

# The script has two modes.  A full backup and a daily backup.
# parameters in your oscar_context.properties file should be in the form:
# db_name= oscar_context
# db_username= oscar
# db_password= password
# backup_path = /usr/oscar_backup/oscar_context/
# BASE_DOCUMENT_DIR: /var/lib/OscarDocument
# BACKUPSERVERS= user@192.168.1.151:~/backup/ user@mybackuphostname.org:~/backup/
# COMMON_BACKUPSERVER_PORT= 22
# 
# The full backup backs up the database and the whole OscarDocument/oscar_context directory, it omits the document_cache.
# The incremental method backups up the database and only files from the document directory that
# are newer than the last full backup. It omits OscarBackup.sql*, oscar.war and document_cache.
# The database is dumped and compressed separately from the documents.
# So to recover you will need the full backup and the latest daily backup files, document and database

USAGE=" call like: backup.sh\n or backup.sh -r to upload "



PATH=$PATH:/bin/:/usr/bin/
SCRIPT_FILE=$(basename "$0")
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
SCRIPT_PATH="${SCRIPT_DIR}/${SCRIPT_FILE}"

BWLIMIT="0" # rsynch bandwith limit in kbps, 0 means no limit
if [ -z "$BWLIMIT" ]; then
  BWLIMIT=0
fi

TMPBACKDIR=/root/backuptmp
TOMCAT_OWNER=tomcat7.tomcat7
CATALINA_HOME=/usr/share/tomcat7
DAYS_TO_KEEP=20
#FILES_TO_ADD=('/var/log/syslog' '/var/log/auth.log' '/var/log/tomcat6/catalina.out' '/var/log/mysql/error.log' '/var/log/puppet.log')

OUTFILE=''
PROPFILE=$CATALINA_HOME/oscar.properties
COMPLETE_BACKUP=0
NO_GZIP_MYSQLDUMP_FLAG=0
REMOTE_UPLOAD_FLAG=0

while getopts "cu:p:d:b:t:f:nr" optionName; do
case "$optionName" in
  f) PROPFILE="$OPTARG";;
  n) NO_GZIP_MYSQLDUMP_FLAG=1;;
  r) REMOTE_UPLOAD_FLAG=1;;
  c) COMPLETE_BACKUP=1;;
  u) db_username="$OPTARG";;
  p) db_password="$OPTARG";;
  d) DATABASE="$OPTARG";;
  b) BASE_DOCUMENT_DIR="$OPTARG";;
  t) TMPBACKDIR="$OPTARG";;
esac
done

DATABASE="${DATABASE:-$(sed '/^\#/d' $PROPFILE | grep 'db_name'  | tail -n 1 | cut -d "=" -f2- | cut -d "?" -f1 - | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')}"
BACKUPDIR=$(sed '/^\#/d' $PROPFILE | grep 'backup_path'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')
BACKUPSERVERS=$(grep BACKUPSERVERS $PROPFILE | sed -e "s/.*[=:] //" -e "s/#.*//")
COMMON_BACKUPSERVER_PORT=$(grep COMMON_BACKUPSERVER_PORT $PROPFILE | sed -e "s/.*[=:] //" -e "s/#.*//")
db_username="${db_username:-$(sed '/^\#/d' $PROPFILE | grep 'db_username'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')}"
db_password="${db_password:-$(sed '/^\#/d' $PROPFILE | grep 'db_password'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')}"
BASE_DOCUMENT_DIR="${BASE_DOCUMENT_DIR:-$(sed '/^\#/d' $PROPFILE | grep 'BASE_DOCUMENT_DIR'  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')}"
DAYS_TO_KEEP="${DAYS_TO_KEEP:-$(sed '/^\#/d' $PROPFILE | grep 'DAYS_TO_KEEP'  | tail -n 1 | cut -d '=' -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//')}"

LOG_PATH=$BACKUPDIR
if [ -z "$LOG_PATH" ]; then
  LOG_PATH='/var/log'
fi
mkdir -p ${LOG_PATH}
LOG_FILE="backup.log"
if [ -z "$LOG_FILE" ]; then
  LOG_FILE='syslog'
fi
BACKUP_SCRIPT_USER=$(whoami)
if [ -z "$BACKUP_SCRIPT_USER" ]; then
  BACKUP_SCRIPT_USER='root'
fi

LOG_ERR="${LOG_PATH}/${LOG_FILE}"

function getTimeStamp() {
        date '+%F-%T' 2>> $LOG_ERR
}
function errorExit() {
        echo -e "ERROR: $1\n`getTimeStamp`" >> $LOG_ERR
        exit
}
function confirmVar() {
  if [ -z "$1" ]; then
  errorExit "USAGE: $USAGE"
  fi
}
function checkFile { # returns true if File exists and size is greater than 0, false otherwise
        FULL_PATH_TO_FILE=$1
        if [ -s $FULL_PATH_TO_FILE ]
        then
               return 0 #true
        else
               return 1 #false
        fi
}
confirmVar ${db_password}
export DB_PASSWORD_6606913a="${db_password}"
TOMCAT_HOME='/var/lib/tomcat7'
confirmVar ${DATABASE}
WAR_FILE="${TOMCAT_HOME}/webapps/oscar.war"
confirmVar ${BACKUPDIR}
ARCH="${BACKUPDIR}/archive"
#--opt is shorthand for --add-drop-table --add-locks --create-options --disable-keys --extended-insert --lock-tables --quick --set-charset.
DUMP_OPTIONS="--opt --log-error=${LOG_ERR}"
#if you do bin logging you can use the following"--single-transaction --routines --log-error=${LOG_ERR} --master-data=1 --flush-logs"
ENC_OPTIONS="-aes-256-cbc -salt"
confirmVar ${BWLIMIT}
RSYNC_OPTIONS="--partial -v -v"
KEXALGORITHMS="diffie-hellman-group-exchange-sha256,diffie-hellman-group-exchange-sha1,diffie-hellman-group14-sha1"
confirmVar $KEXALGORITHMS

SSH_OPTIONS="ssh -C -o 'Protocol=2' -o 'KexAlgorithms $KEXALGORITHMS'"

DY=$(date +%d)
DATE_TIME="`getTimeStamp`"

echo " >> ${DATE_TIME} Backup Start" >> $LOG_ERR
mkdir -pv ${BACKUPDIR} >> $LOG_ERR 2>&1
echo "PROPFILE=${PROPFILE} DATABASE=${DATABASE} BACKUPDIR=${BACKUPDIR}" >> $LOG_ERR
if [ -z "${DAYS_TO_KEEP}" ]; then
  DAYS_TO_KEEP=60
fi
if [ -z "${COMMON_BACKUPSERVER_PORT}" ]; then
  COMMON_BACKUPSERVER_PORT=22
fi

#confirmVar $BASE_DOCUMENT_DIR
if [ -z "${BASE_DOCUMENT_DIR}" ]; then
  BASE_DOCUMENT_DIR="${TOMCAT_HOME}/webapps/OscarDocument"
fi
echo "BASE_DOCUMENT_DIR=${BASE_DOCUMENT_DIR} DAYS_TO_KEEP=${DAYS_TO_KEEP}" >> $LOG_ERR

#Check for a Full.OscarBackup
test $COMPLETE_BACKUP = 0 && if [ $(ls -tr ${BACKUPDIR}/Full.OscarBackup.${DATABASE}.*.tar.gz.enc 2>> $LOG_ERR|tail -n1) ]; then
  #find the most recent
  LAST_FULL_BACKUP_FILE=$(ls -tr ${BACKUPDIR}/Full.OscarBackup.${DATABASE}.*.tar.gz.enc | tail -n1) >> $LOG_ERR 2>&1
  #size greater than zero, if not, override
  checkFile $LAST_FULL_BACKUP_FILE || COMPLETE_BACKUP=1
else
  #no full backup files that match the pattern, create
  COMPLETE_BACKUP=1
fi
#test

echo ${DATABASE}
echo "Log file =$LOG_ERR"

if [ ${NO_GZIP_MYSQLDUMP_FLAG} == 1 ]; then
  mysqldump ${DUMP_OPTIONS} ${DATABASE} -u${db_username} -p${db_password} > $BASE_DOCUMENT_DIR/oscar_mcmaster/OscarBackup.sql 2>> $LOG_ERR
  mysqldump ${DUMP_OPTIONS} drugref -u${db_username} -p${db_password} > $BASE_DOCUMENT_DIR/oscar_mcmaster/drugref.sql 2>> $LOG_ERR
else
  mysqldump ${DUMP_OPTIONS} ${DATABASE} -u${db_username} -p${db_password}|gzip --rsyncable -c -9 > $BASE_DOCUMENT_DIR/oscar_mcmaster/OscarBackup.sql.gz 2>> $LOG_ERR
  mysqldump ${DUMP_OPTIONS} drugref -u${db_username} -p${db_password}|gzip --rsyncable -c -9 > $BASE_DOCUMENT_DIR/oscar_mcmaster/drugref.sql.gz 2>> $LOG_ERR
fi
cp -u $WAR_FILE $BASE_DOCUMENT_DIR/oscar_mcmaster/ 2>> $LOG_ERR
cp -u $PROPFILE $BASE_DOCUMENT_DIR/oscar_mcmaster/ 2>> $LOG_ERR
cp -u $SCRIPT_PATH $BASE_DOCUMENT_DIR/oscar_mcmaster/ 2>> $LOG_ERR

CACHEDIR="$BASE_DOCUMENT_DIR/oscar_mcmaster/document_cache"
mkdir -pv $CACHEDIR >> $LOG_ERR 2>&1
CACHEDIR_TAG="${CACHEDIR}/CACHEDIR.TAG"
checkFile ${CACHEDIR_TAG} || echo -e "Signature: 8a477f597d28d172789f06886806bc55\n# This file is a cache directory tag\n" > ${CACHEDIR_TAG}

if [ ${COMPLETE_BACKUP} = 1 ]; then 
  echo "NEW Full.OscarBackup" >> $LOG_ERR
  OUTFILE=${BACKUPDIR}/Full.OscarBackup.${DATABASE}.${DATE_TIME}.tar.gz.enc 2>> $LOG_ERR
  cd $BASE_DOCUMENT_DIR 2>> $LOG_ERR
  tar --exclude-caches -c oscar_mcmaster 2>> $LOG_ERR | gzip -v -c -9 2>> $LOG_ERR | openssl enc $ENC_OPTIONS -pass env:DB_PASSWORD_6606913a > $OUTFILE 2>> $LOG_ERR
  ls -l $OUTFILE --time-style=long-iso | awk '{ print  "<OUTFILE SIZE=\""$5 "\" filename=\""  $8 "\" /> "}' >> $LOG_ERR
	if [ ${REMOTE_UPLOAD_FLAG} == 1 ]; then
		for REMOTE_SERVER in ${BACKUPSERVERS[@]}
		do
		nice rsync -t $RSYNC_OPTIONS --bwlimit=${BWLIMIT} --rsh="$SSH_OPTIONS -p $COMMON_BACKUPSERVER_PORT" $OUTFILE $REMOTE_SERVER >> $LOG_ERR 2>&1
		done
	fi
  chown $TOMCAT_OWNER $OUTFILE 2>> $LOG_ERR
  ##Purge older back up files
find ${BACKUPDIR} -name "OscarBackup.${DATABASE}.*.tar.gz.enc" -type f -mtime +$DAYS_TO_KEEP|sort|head --lines='-2'| xargs /bin/rm -vf >> $LOG_ERR 2>&1
else
  LAST_FULL_BACKUP_FILE=$(ls -tr ${BACKUPDIR}/Full.OscarBackup.${DATABASE}.*.tar.gz.enc | tail -n1) 2>> $LOG_ERR
  confirmVar $LAST_FULL_BACKUP_FILE
  OUTFILE="${BACKUPDIR}/OscarBackup.${DATABASE}.${DATE_TIME}.Document.tar.gz.enc" 2>> $LOG_ERR
  DB_OUTFILE="${BACKUPDIR}/OscarBackup.${DATABASE}.${DATE_TIME}.Database.tar.gz.enc" 2>> $LOG_ERR
  cd $BASE_DOCUMENT_DIR/oscar_mcmaster 2>> $LOG_ERR
  tar -c OscarBackup.sql* 2>> $LOG_ERR | gzip -v -c -9 2>> $LOG_ERR | openssl enc $ENC_OPTIONS -pass env:DB_PASSWORD_6606913a > $DB_OUTFILE 2>> $LOG_ERR
  find . -type f -newer $LAST_FULL_BACKUP_FILE -print|grep -Ev "^\./document_cache|^\./OscarBackup.sql$|^\./OscarBackup.sql.gz$|^\./oscar.war" > filelist.txt 2>> $LOG_ERR
  tar -c -T filelist.txt 2>> $LOG_ERR | gzip -v -c -9 2>> $LOG_ERR | openssl enc $ENC_OPTIONS -pass env:DB_PASSWORD_6606913a > $OUTFILE 2>> $LOG_ERR

  getTimeStamp >> $LOG_ERR 2>&1
  if [ ${REMOTE_UPLOAD_FLAG} == 1 ]; then
    for REMOTE_SERVER in ${BACKUPSERVERS[@]} 
		do
		  ls -l $DB_OUTFILE --time-style=long-iso | awk '{ print  "<DB_OUTFILE SIZE=\""$5 "\" filename=\""  $8 "\" /> "}' >> $LOG_ERR
		  nice rsync $RSYNC_OPTIONS --bwlimit=${BWLIMIT} --rsh="$SSH_OPTIONS -p $COMMON_BACKUPSERVER_PORT" $DB_OUTFILE $REMOTE_SERVER >> $LOG_ERR 2>&1
		  getTimeStamp >> $LOG_ERR 2>&1
		  ls -l $OUTFILE --time-style=long-iso | awk '{ print  "<OUTFILE SIZE=\""$5 "\" filename=\""  $8 "\" /> "}' >> $LOG_ERR
		  nice rsync $RSYNC_OPTIONS --bwlimit=${BWLIMIT} --rsh="$SSH_OPTIONS -p $COMMON_BACKUPSERVER_PORT" $OUTFILE $REMOTE_SERVER >> $LOG_ERR 2>&1
		  getTimeStamp >> $LOG_ERR 2>&1
    done
    for REMOTE_SERVER in ${BACKUPSERVERS[@]} 
    do
      ls -l $LAST_FULL_BACKUP_FILE --time-style=long-iso | awk '{ print  "<LAST_FULL_BACKUP_FILE SIZE=\""$5 "\" filename=\""  $8 "\" /> "}' >> $LOG_ERR
      killall -q -v -u $BACKUP_SCRIPT_USER -o 1h rsync - >> $LOG_ERR 2>&1 && sleep 60 >> $LOG_ERR 2>&1  
      test ${BWLIMIT} -gt 1 && BWLIMIT=$(($BWLIMIT * 3 / 4))
      nice rsync --append-verify --backup -t $RSYNC_OPTIONS --bwlimit=${BWLIMIT} --rsh="$SSH_OPTIONS -p $COMMON_BACKUPSERVER_PORT" $LAST_FULL_BACKUP_FILE $REMOTE_SERVER >> $LOG_ERR 2>&1
      getTimeStamp >> $LOG_ERR 2>&1
    done
  else
    echo "Not Uploading, REMOTE_UPLOAD_FLAG=0" >> $LOG_ERR
    ls -l $DB_OUTFILE --time-style=long-iso | awk '{ print  "<DB_OUTFILE SIZE=\""$5 "\" filename=\""  $8 "\" /> "}' >> $LOG_ERR
    ls -l $OUTFILE --time-style=long-iso | awk '{ print  "<OUTFILE SIZE=\""$5 "\" filename=\""  $8 "\" /> "}' >> $LOG_ERR
    ls -l $LAST_FULL_BACKUP_FILE --time-style=long-iso | awk '{ print  "<LAST_FULL_BACKUP_FILE SIZE=\""$5 "\" filename=\""  $8 "\" /> "}' >> $LOG_ERR
    getTimeStamp >> $LOG_ERR 2>&1
  fi
  chown $TOMCAT_OWNER $OUTFILE 2>> $LOG_ERR
  chown $TOMCAT_OWNER $DB_OUTFILE 2>> $LOG_ERR
  ####ARCHIVE THE Database snapshot on the FIRST OF THE MONTH BACKUP
  if [ "$DY" == "01" ]; then
    mkdir -pv $ARCH >> $LOG_ERR 2>&1
    cp -vb $DB_OUTFILE ${ARCH}/OscarBackup.${DATABASE}.${DATE_TIME}.Database.tar.gz.enc.archive >> $LOG_ERR 2>&1
  fi
  ##Purge older back up files 
find ${BACKUPDIR} -name "OscarBackup.${DATABASE}.*.tar.gz.enc" -type f -mtime +$DAYS_TO_KEEP|sort |head --lines='-2'| xargs /bin/rm -vf >> $LOG_ERR 2>&1
fi

chown -R $TOMCAT_OWNER $BASE_DOCUMENT_DIR/oscar_mcmaster 2>> $LOG_ERR

DATE_TIME="`getTimeStamp`"

echo " << ${DATE_TIME} Backup Stop" >> $LOG_ERR
echo "" >> $LOG_ERR
