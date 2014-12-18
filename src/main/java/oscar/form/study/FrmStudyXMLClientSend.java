/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


// javac -classpath .;C:\jakarta-tomcat-4.0.6\common\lib;%CLASSPATH% SAClient.java
/*activation.jar
commons-logging.jar
dom.jar
dom4j.jar
jaxm-api.jar
jaxm-runtime.jar
jaxp-api.jar
mail.jar
sax.jar
saaj-api.jar
saaj-ri.jar
xalan.jar
xercesImpl.jar
xsltc.jar
javac -d . FrmStudyXMLClientSend.java
java -classpath .:%CLASSPATH% oscar.form.study.FrmStudyXMLClientSend /root/oscar_sfhc.properties https://67.69.12.115:8443/OscarComm/DummyReceiver
java -classpath .:%CLASSPATH% oscar.form.study.FrmStudyXMLClientSend /root/oscar_sfhc.properties https://192.168.42.180:15000/ /root/oscarComm/oscarComm.keystore

java -classpath .:%CLASSPATH% oscar.form.study.FrmStudyXMLClientSend /root/oscar_sfhc.properties https://130.113.150.203:15501/ /root/oscarComm/enleague.keystore
java -classpath .:%CLASSPATH% oscar.form.study.FrmStudyXMLClientSend /root/oscar_sfhc.properties https://192.168.42.180:15000/ /root/oscarComm/compete.keystore
java oscar.form.study.FrmStudyXMLClientSend c:\\root\\oscar_sfhc.properties https://67.69.12.115:8443/OscarComm/DummyReceiver
*/

package oscar.form.study;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Vector;

import javax.xml.messaging.URLEndpoint;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.oscarehr.common.dao.StudyDataDao;
import org.oscarehr.common.model.StudyData;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;


public class FrmStudyXMLClientSend {
//	private String DBName = null;
	private String URLService = null;

	Properties param = new Properties();
	Vector studyContent = new Vector();
	Vector studyNo = new Vector();
	String sql = null;
	ResultSet rs = null;

//	Properties studyTableName = null;
	String dateYesterday = null;
	String dateTomorrow = null;

	public static void main (String[] args) throws java.sql.SQLException, java.io.IOException  {
		if (args.length != 3) {
			MiscUtils.getLogger().debug("Please run: java path/FrmStudyXMLClient dbname WebServiceUrl");
			return;
		}
		FrmStudyXMLClientSend aStudy = new FrmStudyXMLClientSend();

		//initial
		aStudy.init(args[0], args[1]);
		aStudy.getStudyContent();
		if (aStudy.studyContent.size() == 0) {return;}

		//loop for each content record
		for (int i = 0; i < aStudy.studyContent.size() ; i++ )	{
			aStudy.sendJaxmMsg((String)aStudy.studyContent.get(i), args[2]);
			aStudy.updateStatus((String)aStudy.studyNo.get(i));
		}

	}

	private synchronized void init (String file, String url) throws java.io.IOException  {
		URLService = url;
		param.load(new FileInputStream(file));

		GregorianCalendar now=new GregorianCalendar();
		now.add(Calendar.DATE, -1);
		dateYesterday = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) ;
		now.add(Calendar.DATE, +2);
		dateTomorrow = now.get(Calendar.YEAR) +"-" +(now.get(Calendar.MONTH)+1) +"-"+now.get(Calendar.DAY_OF_MONTH) ;
	}

	private synchronized void getStudyContent () throws java.sql.SQLException  {
        sql = "SELECT studydata_no, content from studydata where timestamp > '" + dateYesterday + "' and timestamp < '" + dateTomorrow + "' and status='ready' order by studydata_no";
        rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
			studyContent.add(oscar.Misc.getString(rs, "content"));
			studyNo.add(oscar.Misc.getString(rs, "studydata_no"));
		}
        rs.close();
	}

	private synchronized void updateStatus (String studyDataNo)   {
		StudyDataDao dao = SpringUtils.getBean(StudyDataDao.class);
		StudyData s = dao.find(Integer.parseInt(studyDataNo));
		if(s != null) {
			s.setStatus("sent");
			dao.merge(s);
		}
	}


	private void sendJaxmMsg (String aMsg, String u)  {
		try	{
			System.setProperty("javax.net.ssl.trustStore", u);

			SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
			SOAPConnection connection = scf.createConnection();

			MessageFactory mf = MessageFactory.newInstance();
		    SOAPMessage message = mf.createMessage();

			SOAPPart sp = message.getSOAPPart();
		    SOAPEnvelope envelope = sp.getEnvelope();

			SOAPHeader header = envelope.getHeader();
		    SOAPBody body = envelope.getBody();

			SOAPHeaderElement headerElement = header.addHeaderElement(envelope.createName("OSCAR", "DT", "http://www.oscarhome.org/"));
		    headerElement.addTextNode("header");

			SOAPBodyElement bodyElement = body.addBodyElement(envelope.createName("Service"));
		    bodyElement.addTextNode("compete");

			AttachmentPart ap1 = message.createAttachmentPart();
			ap1.setContent(aMsg, "text/plain");

			message.addAttachmentPart(ap1);

			URLEndpoint endPoint = new URLEndpoint (URLService);  //"https://67.69.12.115:8443/OscarComm/DummyReceiver");
			SOAPMessage reply = connection.call(message, endPoint);

			connection.close();
		} catch (Exception e)	{
			MiscUtils.getLogger().error("Error", e);
		}
	}
}
