#!/bin/bash

WHOAMI=`id | sed -e 's/(.*//'`
if [ "$WHOAMI" != "uid=0" ] ; then
        echo "Sorry, you need super user access to run this script."
        exit 1
fi

if [ ! "$JAVA_HOME" ]; then
  echo "Please set JAVA_HOME before calling this script"
  exit 1
fi

if [ ! "$MULE_HOME" ]; then
  echo "Please set MULE_HOME before calling this script"
  exit 1
fi


if [ -e $MULE_HOME/bin/muled ]; then

  if [ -e $JAVA_HOME/bin/javac ]; then
 
    STAY_IN_LOOP="true"
    
    while true; do    
      echo "Please specify the number of different message formats you will be using [1,2,3] followed by [ENTER]: "
      read FORMATS
  
      case $FORMATS in
      [1])
        cp ./conf/mule-config_one-format.xml ./conf/mule-config.xml
        break
      ;;
      [2])
        cp ./conf/mule-config_two-formats.xml ./conf/mule-config.xml
        break
      ;;
      [3])
        cp ./conf/mule-config_three-formats.xml ./conf/mule-config.xml
        break
      ;;
      esac    
      echo ""
      echo "Please enter a valid option."
    done

    ant clean
    ant

    cp $MULE_HOME/bin/muled $MULE_HOME/bin/muled.bak
    eval "sed 's:MULE_HOME_REPLACE:$MULE_HOME:' $MULE_HOME/bin/muled > ./muled"
    eval "sed 's:JAVA_HOME_REPLACE:$JAVA_HOME:' ./muled > /etc/init.d/muled"
    rm ./muled

    chown root:root /etc/init.d/muled
    chmod 755 /etc/init.d/muled

    update-rc.d muled defaults

  else
    echo ""
    echo "The specified JAVA_HOME does not contain the JDK.... exiting"
    echo ""
    exit 1
  fi

else
  echo ""
  echo "The specified MULE_HOME deos not point to the correct directory.... exiting"
  echo ""
  exit 1
fi

echo ""
echo ""
echo "Installation successful, mule will start on the next system boot."
echo "You can start mule now by running the following command:"
echo "        sudo /etc/init.d/muled start"
echo ""

exit 1
