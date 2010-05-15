"""
 *
 * 
 * Copyright (c) 2001-2005. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 * Author: Joel Legris
 * Email: joel.legris@egadss.org
 * Date: March 21,2005
 *
 *
 """

from java.sql import *
from java.text import *
from java.util import *
from java.lang import *
from java.util import *
from java.io import *
import traceback
import sys
"""
Class Name:RourkeVacsMigrator
Description:

The Baby Rourke forms have been modified such that the immunization flags have been removed from all three pages, 
and have been replaced by text areas for entry of free text.Therefore, this script has been created to migrate the existing 
immunization flag data into the free text fields.Each Immunization field is associated with a corresponding free text field as well 
as a textual description. If an immunization field contains a value of "1", a corresponding textual description is appended to its related free text field.
"""
class RourkeVacsMigrator:
	def __init__(self):
		self.db_username = ""
		self.db_password = ""
		self.db_driver = ""
	        self.db_uri = ""
	        self.tbl = "formRourke"
		self.con = None #java.sql.Connection Object
		#Maps immunization flags to the corresponding free text field
		self.pageMappings={
		'p1_hepB1w':'p1_immunization1w',
		'p1_immuniz1m':'p1_immunization1m',
		'p1_acetaminophen1m':'p1_immunization1m',
		'p1_hepB1m':'p1_immunization1m',
		'p1_acetaminophen2m':'p1_immunization2m',
		'p1_hib2m':'p1_immunization2m',
		'p1_polio2m':'p1_immunization2m',
		'p2_hib4m':'p2_immunization4m',
		'p2_polio4m':'p2_immunization4m',
		'p2_hib6m':'p2_immunization6m',
		'p2_polio6m':'p2_immunization6m',
		'p2_hepB6m':'p2_immunization6m',
		'p2_tbSkin9m':'p2_immunization9m',
		'p2_mmr12m':'p2_immunization12m',
		'p2_varicella12m':'p2_immunization12m',
		'p3_hib18m':'p3_immunization18m',
		'p3_polio18m':'p3_immunization18m',
		'p3_mmr4y':'p3_immunization4y',
		'p3_polio4y':'p3_immunization4y'}

		#Maps database field names to corresponding descriptions
		self.fieldDescMappings={
		'p1_hepB1w':'Hep. B vaccine',
		'p1_immuniz1m':'Immunization',
		'p1_acetaminophen1m':'Acetaminophen',
		'p1_hepB1m':'Hep. B vaccine',
		'p1_acetaminophen2m':'Acetaminophen',
		'p1_hib2m':'HIB',
		'p1_polio2m':'aPDT polio',
		'p2_hib4m':'HIB',
		'p2_polio4m':'aPDT polio',
		'p2_hib6m':'HIB>',
		'p2_polio6m':'aPDT polio',
		'p2_hepB6m':'Hep. B vaccine',
		'p2_tbSkin9m':'TB skin text*',
		'p2_mmr12m':'MMR',
		'p2_varicella12m':'Varicella vaccine*',
		'p3_hib18m':'HIB',
		'p3_polio18m':'aPDT polio',
		'p3_mmr4y':'MMR',
		'p3_polio4y':'aPDT polio'}		
		self.loadConfig()

		
	def createConnection(self):
		"""Assigns a java.sql.Connection object using the configured db parameters
		   If Connection can't be created, program terminates	
		"""
		
		try:
			Class.forName(self.db_driver)
			cs = self.db_uri+"/" + self.db_name + "?user="+self.db_username+"&password="+self.db_password
			print cs
			return DriverManager.getConnection(cs)	
		except:
			traceback.print_exc()
			sys.exit()
				
        def loadConfig(self):
        	"""Loads the properties config file and assigns the db properties to the corresponding class fields"""
        	props = Properties()
        	try:
        		finstream = FileInputStream("db.properties")		
        		props.load(finstream)
        		# set db properties
			self.db_name = props.getProperty('db_name')
			self.db_username = props.getProperty('db_username')
			self.db_password = props.getProperty('db_password')
			self.db_driver = props.getProperty('db_driver')
			self.db_uri = props.getProperty('db_uri')
                except:
                	traceback.print_exc()
                	sys.exit()
                	
        def execTransfer(self):
        	"""Executes the migration of immunization data to the corresponding free text fields"""
        	rs = None
        	statement = None
        	strMsg = "" #Log message string
        	strBorder = "---------------------------------\n"
		strStartMsg = "IMPORT STARTED@" + Date().toString() + "\n" 
		strBorder = "---------------------------------\n"
		log = open("rourketransfer.log","a")
		print strBorder
		log.write(strBorder)
		print strStartMsg
		log.write(strStartMsg)
        	try:
			fldNames = self.fieldDescMappings.keys()
			qry = "select * from " + self.tbl
			con = self.createConnection()
			statement = con.createStatement()
			rs = statement.executeQuery(qry)
			rsmd = rs.getMetaData()
			colCount = rsmd.getColumnCount()

			while rs.next():
				index = 1
				while index < colCount:
					colName = rsmd.getColumnName(index)
					strFlag = rs.getString(index)
					if colName in fldNames:	
						#if given immunization has been flagged,append textual representation of field to its corresponding free text field
						if strFlag == "1":
							freeTextFld = self.pageMappings[colName]
							freeTextData = rs.getString(freeTextFld) + "\n" + self.fieldDescMappings[colName]  
							qryUpdate = "update " + self.tbl + " set " + freeTextFld + "=? " + "where ID = " + rs.getString("ID")
							stUpdate = con.prepareStatement(qryUpdate)
							stUpdate.setString(1,freeTextData)
							stUpdate.executeUpdate()
							msg = "UPDATED RECORD#: " + rs.getString("ID") + " APPENDED: " + self.fieldDescMappings[colName] + " TO FIELD: " + self.pageMappings[colName]
							print msg
							log.write(msg + "\n")
					index +=1
							
		except:
			traceback.print_exc()
			log.close()
	     	log.close()
		
r = RourkeVacsMigrator()
r.execTransfer()