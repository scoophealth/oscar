#This script updates the billingservice table with bcma codes/fees that reside in a pipe delimited file with the following structure:
#code|description|msp fee|bcma fee

from java.sql import *
from java.lang import *

#Set your database info!
conStr = "jdbc:mysql://server:3306/database/?user=&password="
driver = "org.gjt.mm.mysql.Driver"
Class.forName(driver)	
conn = DriverManager.getConnection(conStr)
fees = open("bcmafees.csv")
fout = open("bcmaimport.log","w")
code = ""
row = ""
for ln in fees.readlines():
    ln = ln.split("|")
    code = ln[0].strip()
    desc = ln[1].strip()
    msp = ln[2].strip()
    bcma = ln[3].strip()
    rs = conn.createStatement().executeQuery("select billingservice_no,value from billingservice where service_code = '" + code +"' and region = 'BC'")
    if rs.next():
        if msp != "":
            dbmsp = rs.getString("value")
            if msp!=str(dbmsp):
		    fout.write("Updated MSP code:" + code +"\n")
		    qry = "update billingservice set value = ?, billingservice_date = ? where billingservice_no=?"
		    print qry +"\n"
		    st  = conn.prepareStatement(qry)
		    st.setString(1,msp)
		    st.setString(2,"2005-04-01")
		    st.setString(3,rs.getString(1))
		    st.executeUpdate()
    else:
        if bcma != "":
            privcode = "P" + str(code)
            print "Inserted New BCMA Fee:" + privcode
            qry = "insert into billingservice(service_code,description,value,billingservice_date,region) values(?,?,?,'2005-04-01','BC')"
            st = conn.prepareStatement(qry)
            st.setString(1,privcode)
            st.setString(2,desc)
            st.setString(3,bcma)
            st.executeUpdate()
	    fout.write("Inserting NEW BCMA code" + code +"\n")
        if msp != "":
                print "Inserted New MSP Fee:" + code
                qry = "insert into billingservice(service_code,description,value,billingservice_date,region) values(?,?,?,'2005-04-01','BC')"
                st = conn.prepareStatement(qry)
                st.setString(1,code)
                st.setString(2,desc)
                st.setString(3,msp)
                st.executeUpdate()
                fout.write("Inserted new MSP code" + code +"\n")
fees.close()
fout.close()


