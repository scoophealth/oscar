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

package oscar.oscarMessenger.pageUtil;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.oscarehr.common.dao.MessageListDao;
import org.oscarehr.common.dao.MessageTblDao;
import org.oscarehr.common.dao.OscarCommLocationsDao;
import org.oscarehr.common.dao.forms.FormsDao;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.MessageTbl;
import org.oscarehr.common.model.OscarCommLocations;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarMessenger.data.MsgDisplayMessage;
import oscar.util.ConversionUtils;

public class MsgDisplayMessagesBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String providerNo;
	private Vector<String> messageid;
	private Vector<String> messagePosition;
	private Vector<String> status;
	private Vector<String> date;
	private Vector<String> ime;
	private Vector<String> sentby;
	private Vector<String> subject;
	private Vector<String> attach;

	private String currentLocationId;

	/*
	* edit 2006-0811-01 by wreby
	*/
	private String filter;

	//Just sets the filter keyword after ensuring that the user is not trying to
	// insert any stray single quotes, since that is the escape character in SQL
	public void setFilter(String filter) {
		if (filter == null || filter.equals("")) {
			this.filter = null;
		} else {
			//get rid of the stray single quotes, since that is the SQL escape character
			filter.replaceAll("'", "''");
			this.filter = filter;
		}
	}

	public void clearFilter() {
		filter = null;
	}

	public String getFilter() {
		if (filter == null) {
			filter = "";
		}

		return filter;
	}

	public String getSQLSearchFilter(String[] colsToSearch) {
		if (filter == null || colsToSearch.length == 0) {
			return "";
		} else {
			String search = " and (";
			int numOfCols = colsToSearch.length;
			for (int i = 0; i < numOfCols - 1; i++) {
				search = search + colsToSearch[i] + " like '%" + filter + "%' or ";
			}
			search = search + colsToSearch[numOfCols - 1] + " like '%" + filter + "%') ";
			return search;
		}
	}

	// end edit 2006-08011-01 by wreby

	public Vector<String> getAttach() {
		return attach;
	}

	public String getCurrentLocationId() {
		if (currentLocationId == null) {
			OscarCommLocationsDao dao = SpringUtils.getBean(OscarCommLocationsDao.class);
			for (OscarCommLocations l : dao.findByCurrent1(1)) {
				currentLocationId = "" + l.getId();
				break;
			}
		}
		return currentLocationId;
	}

	/**
	 * Used to set message ids to be viewed on the DisplayMessages.jsp
	 * @param messageid Vector, Contains all the messageids to be displayed
	 */
	public void setMessageid(Vector<String> messageid) {
		this.messageid = messageid;
	}

	/**
	 * calls getMessageIDS and getInfo which are used to fill the Vectors
	 * with the Message headers for the current provider No
	 * @return Vector, Contains the messageids for use on the DisplayMessage.jsp
	 */
	public Vector<String> getMessageid() {
		getMessageIDs();
		getInfo();

		return this.messageid;
	}

	public Vector<String> getDelMessageid() {
		getDeletedMessageIDs();
		getInfo();
		estDeletedInbox();
		return this.messageid;
	}

	public Vector<String> getSentMessageid() {
		getSentMessageIDs();
		estSentItemsInbox();
		return this.messageid;
	}

	/**
	 * Used to set the Status vector. either read, new, del
	 * @param status Vector, Strings either read , new or del
	 */
	public void setStatus(Vector<String> status) {
		this.status = status;
	}

	/**
	 * Will check to see if the status has already been set, if not it will intialize the
	 * Vectors of this Bean with getMessageIDs and get Info
	 * @return Vector Strings either read, new or del
	 */
	public Vector<String> getStatus() {
		if (status == null) {
			getMessageIDs();
			getInfo();
		}
		return this.status;
	}

	public void setDate(Vector<String> date) {
		this.date = date;
	}

	/**
	 * Will check to see if the date has already been set, if not it will intialize the
	 * Vectors of this Bean with getMessageIDs and get Info
	 * @return Vector Strings either read, new or del
	 */
	public Vector<String> getDate() {
		if (date == null) {
			getMessageIDs();
			getInfo();
		}
		return this.date;
	}

	/**
	 * used to set the sentby Vector
	 * @param sentby Vector contains Strings of who sent the message
	 */
	public void setSentby(Vector<String> sentby) {
		this.sentby = sentby;
	}

	/**
	 * Will check to see if the sentby has already been set, if not it will intialize the
	 * Vectors of this Bean with getMessageIDs and get Info
	 * @return Vector Strings either read, new or del
	 */
	public Vector<String> getSentby() {
		if (sentby == null) {
			getMessageIDs();
			getInfo();
		}
		return this.sentby;
	}

	/**
	 * used to set the subject Vector of the messages
	 * @param subject Vector, contains Strings of subjects
	 */
	public void setSubject(Vector<String> subject) {
		this.subject = subject;
	}

	/**
	 * Will check to see if the subject has already been set, if not it will intialize the
	 * Vectors of this Bean with getMessageIDs and get Info
	 * @return Vector Strings either read, new or del
	 */
	public Vector<String> getSubject() {
		if (subject == null) {
			getMessageIDs();
			getInfo();
		}
		return this.subject;
	}

	/**
	 * Used to set the providerNo that will determine what this bean will fill itself with
	 * @param providerNo String, provider No
	 */
	public void setProviderNo(String providerNo) {

		this.providerNo = providerNo;
	}

	/**
	 * gets the current provider No
	 * @return the provider no
	 */
	public String getProviderNo() {
		return this.providerNo;
	}

	/**
	 * This method uses the ProviderNo and searches for messages for this providerNo
	 * in the messagelisttbl
	 */
	void getMessageIDs() {
		String providerNo = this.getProviderNo();

		messageid = new Vector<String>();
		status = new Vector<String>();
		messagePosition = new Vector<String>();
		int index = 0;

		MessageListDao dao = SpringUtils.getBean(MessageListDao.class);
		for (MessageList ml : dao.findByProviderNoAndLocationNo(providerNo, ConversionUtils.fromIntString(getCurrentLocationId()))) {
			messagePosition.add(Integer.toString(index));
			messageid.add("" + ml.getMessage());
			status.add(ml.getStatus());
			index++;
		}
	}

	public String getOrderBy(String order) {
		String orderBy = null;
		if (order == null) {
			orderBy = " m.messageid desc";
		} else {
			String desc = "";
			if (order.charAt(0) == '!') {
				desc = " desc ";
				order = order.substring(1);
			}
			Hashtable<String, String> orderTable = new Hashtable<String, String>();
			orderTable.put("status", "status");
			orderTable.put("from", "sentby");
			orderTable.put("subject", "thesubject");
			orderTable.put("date", "thedate");
			orderTable.put("sentto", "sentto");
			orderTable.put("linked", "isnull");

			orderBy = orderTable.get(order);
			if (orderBy == null) {
				orderBy = "message";
			}
			orderBy += desc + ", m.messageid desc";
		}
		MiscUtils.getLogger().debug("order " + order + " orderby " + orderBy);
		return orderBy;
	}

	public Vector<MsgDisplayMessage> estInbox(String orderby, int page) {
		String providerNo = this.getProviderNo();
		Vector<MsgDisplayMessage> msg = new Vector<MsgDisplayMessage>();

		String[] searchCols = { "m.thesubject", "m.themessage", "m.sentby", "m.sentto" };
		
		int recordsToDisplay = 25;
		int fromRecordNum = (recordsToDisplay * page) - recordsToDisplay;
		String limitSql = " limit " + fromRecordNum + ", " + recordsToDisplay;
				
		try {
			//String sql = ("select map.messageID is null as isnull, map.demographic_no, ml.message, ml.status," + " m.thesubject, m.thedate, m.theime, m.attachment, m.pdfattachment, m.sentby  " + "from messagelisttbl ml, messagetbl m " + "(select map.messageID, map.demographic_no from msgDemoMap map where map.messageID = m.messageid limit 1) as map" + " " + "where ml.provider_no = '" + providerNo + "' " + "and status not like \'del\' and remoteLocation = '" + getCurrentLocationId() + "' " + " and ml.message = m.messageid "                       
                        String sql = "(select m.messageid, (select map.demographic_no from msgDemoMap map where map.messageID = m.messageid limit 1) as demographic_no, ml.message, ml.status,  m.thesubject, m.thedate, m.theime, m.attachment, m.pdfattachment, m.sentby  from messagelisttbl ml, messagetbl m  where ml.provider_no = '" + providerNo + "' " + "and status not like \'del\' and remoteLocation = '" + getCurrentLocationId() + "' " + " and ml.message = m.messageid "
			        + getSQLSearchFilter(searchCols) + " order by " + getOrderBy(orderby) + limitSql + ")";

			FormsDao dao = SpringUtils.getBean(FormsDao.class);
			for (Object[] o : dao.runNativeQuery(sql)) {
				String demographic_no = String.valueOf(o[1]);
				String message = String.valueOf(o[2]);
				String status = String.valueOf(o[3]);
				String thesubject = String.valueOf(o[4]);
				String thedate = String.valueOf(o[5]);
				String theime = String.valueOf(o[6]);
				String attachment = String.valueOf(o[7]);
				String pdfattachment = String.valueOf(o[8]);
				String sentby = String.valueOf(o[9]);

				oscar.oscarMessenger.data.MsgDisplayMessage dm = new oscar.oscarMessenger.data.MsgDisplayMessage();
				dm.status = status;
				dm.messageId = message;
				dm.thesubject = thesubject;
				dm.thedate = thedate;
				dm.theime = theime;
				dm.sentby = sentby;
				dm.demographic_no = demographic_no;
				String att = attachment;
				String pdfAtt = pdfattachment;

				if (att == null || att.equalsIgnoreCase("null")) {
					dm.attach = "0";
				} else {
					dm.attach = "1";
				}
				if (pdfAtt == null || pdfAtt.equalsIgnoreCase("null")) {
					dm.pdfAttach = "0";
				} else {
					dm.pdfAttach = "1";
				}

				msg.add(dm);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return msg;
	}

	public Vector<MsgDisplayMessage> estDemographicInbox() {
		return estDemographicInbox(null, null);
	}

	//INBOX
	public Vector<MsgDisplayMessage> estDemographicInbox(String orderby, String demographic_no) {
		Vector<MsgDisplayMessage> msg = new Vector<MsgDisplayMessage>();
		int index = 0;

		String[] searchCols = { "m.thesubject", "m.themessage", "m.sentby", "m.sentto" };
		
		try {
			String sql = "select map.messageID is null as isnull, map.demographic_no, m.messageid, m.thesubject, m.thedate, m.theime, m.attachment, m.pdfattachment, m.sentby  " + "from  messagetbl m, msgDemoMap map where map.demographic_no = '" + demographic_no + "'  " + "and m.messageid = map.messageID " + getSQLSearchFilter(searchCols) + " order by " + getOrderBy(orderby);
			FormsDao dao = SpringUtils.getBean(FormsDao.class);
			List<Object[]> os = dao.runNativeQuery(sql);
			for (Object[] o : os) {
				String demo_no = String.valueOf(o[1]);
				String messageid = String.valueOf(o[2]);
				String thesubject = String.valueOf(o[3]);
				String thedate = String.valueOf(o[4]);
				String theime = String.valueOf(o[5]);
				String attachment = String.valueOf(o[6]);
				String pdfattachment = String.valueOf(o[7]);
				String sentby = String.valueOf(o[8]);

				oscar.oscarMessenger.data.MsgDisplayMessage dm = new oscar.oscarMessenger.data.MsgDisplayMessage();
				dm.status = "    ";
				dm.messageId = messageid;
				dm.messagePosition = Integer.toString(index);
				dm.thesubject = thesubject;
				dm.thedate = thedate;
				dm.theime = theime;
				dm.sentby = sentby;
				dm.demographic_no = demo_no;

				String att = attachment;
				String pdfAtt = pdfattachment;
				if (att == null || att.equals("null")) {
					dm.attach = "0";
				} else {
					dm.attach = "1";
				}
				if (pdfAtt == null || pdfAtt.equals("null")) {
					dm.pdfAttach = "0";
				} else {
					dm.pdfAttach = "1";
				}

				boolean isLast = index == os.size();
				if (isLast) {
					dm.isLastMsg = true;
				}

				msg.add(dm);

				index++;

			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return msg;
	}

	public Vector<MsgDisplayMessage> estDeletedInbox() {
		return estDeletedInbox(null, 1);
	}
	
	public int getTotalMessages(int type){
		String providerNo = this.getProviderNo();
		MessageListDao messageListDao = SpringUtils.getBean(MessageListDao.class);
		
		int total = messageListDao.messagesTotal(type, providerNo, Integer.parseInt(getCurrentLocationId()), filter);
		
		return total;
	}

	public Vector<MsgDisplayMessage> estDeletedInbox(String orderby, int page) {
		
		String providerNo = this.getProviderNo();
		Vector<MsgDisplayMessage> msg = new Vector<MsgDisplayMessage>();
		String[] searchCols = { "m.thesubject", "m.themessage", "m.sentby", "m.sentto" };
		
		int recordsToDisplay = 25;
		int fromRecordNum = (recordsToDisplay * page) - recordsToDisplay;
		String limitSql = " limit " + fromRecordNum + ", " + recordsToDisplay;
		
		try {
			String sql = "select map.messageID is null as isnull, map.demographic_no, ml.message, ml.status, m.thesubject, m.thedate, m.theime, m.attachment, m.pdfattachment, m.sentby  from messagelisttbl ml, messagetbl m " + " left outer join msgDemoMap map on map.messageID = m.messageid " + " where provider_no = '" + providerNo + "' and status like \'del\' and remoteLocation = '" + getCurrentLocationId() + "' " + " and ml.message = m.messageid " + getSQLSearchFilter(searchCols) + " order by "
			        + getOrderBy(orderby) + limitSql;

			FormsDao dao = SpringUtils.getBean(FormsDao.class);
			for (Object[] o : dao.runNativeQuery(sql)) {
				String demographic_no = String.valueOf(o[1]);
				String message = String.valueOf(o[2]);
				String thesubject = String.valueOf(o[4]);
				String thedate = String.valueOf(o[5]);
				String theime = String.valueOf(o[6]);
				String attachment = String.valueOf(o[7]);
				String pdfattachment = String.valueOf(o[8]);
				String sentby = String.valueOf(o[9]);

				oscar.oscarMessenger.data.MsgDisplayMessage dm = new oscar.oscarMessenger.data.MsgDisplayMessage();
				dm.status = "deleted";
				dm.messageId = message;
				dm.thesubject = thesubject;
				dm.thedate = thedate;
				dm.theime = theime;
				dm.sentby = sentby;
				dm.demographic_no = demographic_no;

				String att = attachment;
				String pdfAtt = pdfattachment;
				if (att == null || att.equals("null")) {
					dm.attach = "0";
				} else {
					dm.attach = "1";
				}
				if (pdfAtt == null || pdfAtt.equals("null")) {
					dm.pdfAttach = "0";
				} else {
					dm.pdfAttach = "1";
				}
				msg.add(dm);
				
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return msg;
	}

	/**
	 * This method uses the ProviderNo and searches for messages for this providerNo
	 * in the messagelisttbl
	 */
	void getDeletedMessageIDs() {
		String providerNo = this.getProviderNo();
		messageid = new Vector<String>();
		status = new Vector<String>();
		try {
			MessageListDao dao = SpringUtils.getBean(MessageListDao.class);
			for (MessageList ml : dao.findByProviderNoAndLocationNo(providerNo, ConversionUtils.fromIntString(getCurrentLocationId()))) {
				messageid.add("" + ml.getMessage());
				status.add("deleted");
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
	}

	/**
	 * This method uses the ProviderNo and searches for messages for this providerNo
	 * in the messagelisttbl
	 */
	void getSentMessageIDs() {
		String providerNo = this.getProviderNo();

		messageid = new Vector<String>();
		status = new Vector<String>();
		sentby = new Vector<String>();
		date = new Vector<String>();
		ime = new Vector<String>();
		subject = new Vector<String>();

		MessageTblDao dao = SpringUtils.getBean(MessageTblDao.class);

		for (MessageTbl m : dao.findByProviderAndSendBy(providerNo, ConversionUtils.fromIntString(getCurrentLocationId()))) {
			messageid.add("" + m.getId());
			status.add("sent");
			sentby.add(m.getSentBy());
			date.add(ConversionUtils.toDateString(m.getDate()));
			ime.add(ConversionUtils.toDateString(m.getTime()));
			subject.add(m.getSubject());
		}
	}

	public Vector<MsgDisplayMessage> estSentItemsInbox() {
		return estSentItemsInbox(null, 1);
	}

	public Vector<MsgDisplayMessage> estSentItemsInbox(String orderby, int page) {

		String providerNo = this.getProviderNo();
		Vector<MsgDisplayMessage> msg = new Vector<MsgDisplayMessage>();
		String[] searchCols = { "m.thesubject", "m.themessage", "m.sentby", "m.sentto" };
		
		int recordsToDisplay = 25;
		int fromRecordNum = (recordsToDisplay * page) - recordsToDisplay;
		String limitSql = " limit " + fromRecordNum + ", " + recordsToDisplay;
				
		try {
			String sql = "select map.messageID is null as isnull, map.demographic_no, m.messageid as status, m.messageid, thedate, theime, thesubject, sentby, sentto, attachment, pdfattachment from messagetbl m left outer join msgDemoMap map on map.messageID = m.messageid where sentbyNo = '" + providerNo + "' and sentByLocation = '" + getCurrentLocationId() + "'  " + getSQLSearchFilter(searchCols) + " order by " + getOrderBy(orderby) + limitSql;

			FormsDao dao = SpringUtils.getBean(FormsDao.class);
			for (Object[] o : dao.runNativeQuery(sql)) {
				String demographic_no = String.valueOf(o[1]);
				String status = String.valueOf(o[2]);
				String thedate = String.valueOf(o[4]);
				String theime = String.valueOf(o[5]);
				String thesubject = String.valueOf(o[6]);
				String sentby = String.valueOf(o[7]);
				String sentto = String.valueOf(o[8]);
				String attachment = String.valueOf(o[9]);
				String pdfattachment = String.valueOf(o[10]);

				oscar.oscarMessenger.data.MsgDisplayMessage dm = new oscar.oscarMessenger.data.MsgDisplayMessage();
				dm.status = "sent";
				dm.messageId = status;
				dm.thesubject = thesubject;
				dm.thedate = thedate;
				dm.theime = theime;
				dm.sentby = sentby;
				dm.sentto = sentto;
				dm.demographic_no = demographic_no;
				String att = attachment;
				String pdfAtt = pdfattachment;
				if (att == null || att.equals("null")) {
					dm.attach = "0";
				} else {
					dm.attach = "1";
				}
				if (pdfAtt == null || pdfAtt.equals("null")) {
					dm.pdfAttach = "0";
				} else {
					dm.pdfAttach = "1";
				}

				msg.add(dm);

			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return msg;
	}

	/**
	 * This method uses the Vector initialized by getMessageIDs and fills the Vectors with
	 * the Message header Info
	 */
	void getInfo() {

		sentby = new Vector<String>();
		date = new Vector<String>();
		subject = new Vector<String>();
		attach = new Vector<String>();
		String att;
		try {

			//make search string
			List<Integer> ids = new ArrayList<Integer>();
			for (String i : messageid) {
				ids.add(ConversionUtils.fromIntString(i));
			}

			MessageTblDao dao = SpringUtils.getBean(MessageTblDao.class);
			for (MessageTbl m : dao.findByIds(ids)) {
				sentby.add(m.getSentBy());
				date.add(ConversionUtils.toDateString(m.getDate()));
				ime.add(ConversionUtils.toTimeString(m.getTime()));
				subject.add(m.getSubject());
				att = m.getAttachment();
				if (att == null || att.equals("null")) {
					attach.add("0");
				} else {
					attach.add("1");
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
	}
}
