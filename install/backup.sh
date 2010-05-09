#!/bin/sh
# backup.sh

# a script file for OSCAR that copies compressed archives
# that have been date and time stamped for easy sorting
# to the destination folder {DEST} usually where a browser can access
# (it should match backup_path in your oscar.properties)
# and once a month to an archive folder {ARCH}
# from a given MySQL database {DBSE} and documents folder {DOCS}

SRC=$1
DEST=$2
DBSE=$3
DBPWD=$4
DOCS=$5
ARCH=${DEST}/archive
LOG_FILE=${DEST}/daily.log
LOG_ERR=${DEST}/daily.err

# --- log the running of the script
echo "###" `date` "###" 1>> $LOG_FILE
echo "###" `date` "###" 1>> $LOG_ERR

# --- set local variables to today's date and time
YR=$(date +%Y)
MN=$(date +%m)
DY=$(date +%d)
NOW=$(date +%H%M%S)

cd $SRC

# --- check if we are in the correct directory
if [ "$(pwd)" != "${SRC}" ] ; then
  echo "$0: couldn't change directory to ${SRC}" 1>> $LOG_ERR
  echo "No backup made !" 1>> $LOG_ERR
  exit 100
fi

# --- create a sql file of the database
mysqldump ${DBSE} -uroot -p${DBPWD} > OscarBackup.sql

# --- compress up the output and the document directory
tar -czf OscarBackup.tar.gz OscarBackup.sql  2>>$LOG_ERR
tar -czf OscarDocumentBackup.tar.gz ${DOCS}/  2>>$LOG_ERR

# --- encrypt the files
openssl enc -aes-256-cbc -salt -in OscarBackup.tar.gz -out OscarBackup.enc.tar.gz -pass pass:${DBPWD}  2>>$LOG_ERR
openssl enc -aes-256-cbc -salt -in OscarDocumentBackup.tar.gz -out OscarDocumentBackup.enc.tar.gz -pass pass:${DBPWD}  2>>$LOG_ERR
rm -f OscarDocumentBackup.tar.gz  2>>$LOG_ERR
rm -f OscarBackup.tar.gz  2>>$LOG_ERR

# --- every month archive one day worth of the backups
# (this is set for the first day of the month, but you can change this)
if [ "01" = "${DY}" ] ; then

      if [ -d "${ARCH}" ] ; then
       echo "preparing last month's backup for the archival directory" 1>> $LOG_FILE
      else
       echo "the archival directory doesn't exist so we create ${ARCH}" 1>> $LOG_ERR
       mkdir "${ARCH}"  2>>$LOG_ERR
      fi

      mv -f ${DEST}/????-??-${DY}* ${ARCH}/ 2>>$LOG_ERR
fi

# --- remove last months backup from this date
# (you can comment out the following if you need more than a months backups)

for FILE in ${DEST}/????-??-${DY}* ; do
      rm -f ${FILE}  2>>$LOG_ERR
done

# --- copy today's tar gziped files to the backup directory

# --- options for "cp"
# -p preserves ownership, permissions, time etc

OPTIONS="-p"

for FILE in *gz ; do
  cp ${OPTIONS} ${FILE} ${DEST}/${YR}-${MN}-${DY}-${NOW}-${FILE}  1>> $LOG_FILE 2>>$LOG_ERR
done

echo "....done" 1>> $LOG_FILE