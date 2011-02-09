#This script updates the billingservice table with bcma codes/fees that reside in a pipe delimited file with the following structure:
#code|description|msp fee|bcma fee

from java.sql import *
from java.lang import *
from java.util import *
from java.io import *

#Set your database info!
props = Properties()
finstream = FileInputStream("db.properties")		
props.load(finstream)
# set db properties
db_name = props.getProperty('db_name')
db_username = props.getProperty('db_username')
db_password = props.getProperty('db_password')
db_driver = props.getProperty('db_driver')
db_uri = props.getProperty('db_uri')

Class.forName(db_driver)
conStr = db_uri+"/" + db_name + "?user="+db_username+"&password="+db_password	
conn = DriverManager.getConnection(conStr)
fees = open("bcmafees.csv")
fout = open("bcmaimport.log","w")
code = ""
row = ""
for ln in fees.readlines():
    ln = ln.split("|")
    code = ln[0].strip()
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
        if msp != "":
	        print "Inserted New MSP Fee:" + code
	        qry = "insert into billingservice(service_code,description,value,billingservice_date,region) values(?,?,?,'2005-04-01','BC')"
	        st = conn.prepareStatement(qry)
	        st.setString(1,code)
	        st.setString(2,desc)
	        st.setString(3,msp)
	        st.executeUpdate()
                fout.write("Inserted new MSP code" + code +"\n")
	   
	   
	   
    rs = conn.createStatement().executeQuery("select billingservice_no,value from billingservice where service_code = 'A" + code +"' and region = 'BC'")
    if rs.next():
    	   if bcma != "":
	            dbmsp = rs.getString("value")
	            if msp!=str(dbmsp):
			    fout.write("Updated BCMA code:" + code +"\n")
			    qry = "update billingservice set value = ?, billingservice_date = ? where billingservice_no=?"
			    print qry +"\n"
			    st  = conn.prepareStatement(qry)
			    st.setString(1,bcma)
			    st.setString(2,"2005-04-01")
			    st.setString(3,rs.getString(1))
		    	    st.executeUpdate()
    
    else:
        if bcma != "":
            privcode = "A" + str(code)
            print "Inserted New BCMA Fee:" + privcode
            qry = "insert into billingservice(service_code,description,value,billingservice_date,region) values(?,?,?,'2005-04-01','BC')"
            st = conn.prepareStatement(qry)
            st.setString(1,privcode)
            st.setString(2,desc)
            st.setString(3,bcma)
            st.executeUpdate()
	    fout.write("Inserting NEW BCMA code" + code +"\n")	   
 
fees.close()
fout.close()


