#!/usr/bin/env bash
COLOR=false

while getopts "c" o; do
    case "${o}" in
        c)
            COLOR=true
            ;;
        *)
            echo "${OPTARG} is not valid for this."
            exit 1
            ;;
    esac
done

if [ ${COLOR} == true ]; then
	red='\033[0;31m'
	green='\033[0;32m'
	reset='\033[0m'
	yellow='\033[1;33m'
	blue='\033[1;34m'
else
	red=''
	green=''
	reset=''
	yellow=''
	blue=''
fi

fileCount=0
totalFiles=0
propertyCount=0

function addListing(){ file=$1
	propertyNames=($(grep "OscarProperties.getInstance();" ""${file}""|grep -E -o "[[:alnum:]]+[[:space:]]?=[[:space:]]?OscarProperties\.getInstance\(\);"|grep -E -o "^.[^[:space:]=]+"|sort|uniq))
	unset LINENUM
	unset PROPERTY

	if [ -z "${propertyNames[0]}" ]; then
		if [ -n "${propertyNames}" ]; then
			fileCount=$(expr ${fileCount} + 1)
			LINENUM=("${LINENUM[@]}" $(grep -o -n -E "OscarProperties\.getInstance\(\)\.getProperty\(\"[[:alnum:]_]+\"" "${file}"|grep -E -o "(:|)[0-9]+(:|)"|grep -E -o "[0-9]+"))
			PROPERTY=("${PROPERTY[@]}" $(grep -o -E "OscarProperties\.getInstance\(\)\.getProperty\(\"[[:alnum:]_]+\"" "${file}"|grep -o -E "\"[[:alnum:]_]+\""|grep -E -o "[^\"]+"))
			for p in ${!PROPERTY[@]}; do
				PROPLINE=("${PROPLINE[@]}" "${green}${PROPERTY[${p}]}${yellow}=${red}${file}${yellow}:${blue}${LINENUM[${p}]}${reset}")
			done
		fi
	else
		for i in ${!propertyNames[@]}; do
			fileCount=$(expr ${fileCount} + 1)
			LINENUM=("${LINENUM[@]}" $(grep -o -n -E "(${propertyNames[${i}]}|OscarProperties\.getInstance\(\))\.getProperty\(\"[[:alnum:]_]+\"" "${file}"|grep -E -o "(:|)[0-9]+(:|)"|grep -E -o "[0-9]+"))
			PROPERTY=("${PROPERTY[@]}" $(grep -o -E "(${propertyNames[${i}]}|OscarProperties\.getInstance\(\))\.getProperty\(\"[[:alnum:]_]+\"" "${file}"|grep -o -E "\"[[:alnum:]_]+\""|grep -E -o "[^\"]+"))
			for p in ${!PROPERTY[@]}; do
				PROPLINE=("${PROPLINE[@]}" "${green}${PROPERTY[${p}]}${yellow}=${red}${file}${yellow}:${blue}${LINENUM[${p}]}${reset}")
			done
		done
	fi
}

fileFind=($(find -follow))
for i in ${fileFind[@]}; do
	totalFiles=$(expr ${totalFiles} + 1)
	addListing ${i}
done

sort=($(for i in ${PROPLINE[@]}; do echo "${i}"; done|sort|uniq))
count=0

for i in ${!sort[@]}; do
	count=${i}
done

i=0
while [ "${i}" -le "$count" ]; do
	echo -e "${sort[${i}]}"
	i=$(expr ${i} + 1)
	propertyCount=${i}
done

echo -e "${blue}${totalFiles} ${reset}files scanned."
echo -e "${blue}${propertyCount} ${reset}instances of OscarProperties, found in ${blue}${fileCount} ${reset}file(s)"

