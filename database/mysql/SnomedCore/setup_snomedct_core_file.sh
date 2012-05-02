sed 's/ (finding)//' $1 > a.txt
sed 's/ (disorder)//' a.txt > b.txt
sed 's/ (procedure)//' b.txt > $1.mysql
