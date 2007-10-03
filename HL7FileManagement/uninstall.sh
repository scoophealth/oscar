WHOAMI=`id | sed -e 's/(.*//'`
if [ "$WHOAMI" != "uid=0" ] ; then
        echo "Sorry, you need super user access to run this script."
        exit 1
fi

export MULE_HOME=./mule-1.3.3

ant clean

cp $MULE_HOME/bin/muled.bak $MULE_HOME/bin/muled
rm /etc/init.d/muled

update-rc.d muled remove

echo ""
echo ""
echo "Uninstallation successful!!!"
echo ""

exit 1

