#This script updates the billingservice table with by changing all Private 'P' prepended codes with 'A'
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
print conStr
conn = DriverManager.getConnection(conStr)
qry = "select billingservice_no,service_code from billingservice where service_code like 'P%' and region = 'BC'"
print qry
rs = conn.createStatement().executeQuery(qry)
while rs.next():
	pk = rs.getString(1)
	code = rs.getString(2)
	newCode =  "A" + code[1:]
	qry = "update billingservice set service_code = ? where billingservice_no=?"
	print qry +"\n"
	st  = conn.prepareStatement(qry)
	st.setString(1,newCode)
	st.setString(2,pk)
	print 	"update billingservice set service_code = ",newCode," where billingservice_no=",pk
	st.executeUpdate()

