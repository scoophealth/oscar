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

/*
 * BillingNote.java
 *
 * Created on August 17, 2004, 1:30 PM
 */

package oscar.oscarBilling.ca.bc.data;

import java.util.Date;
import java.util.List;

import org.oscarehr.billing.CA.BC.dao.BillingNoteDao;
import org.oscarehr.billing.CA.BC.model.BillingNotes;
import org.oscarehr.util.SpringUtils;

import oscar.Misc;
import oscar.entities.Billingmaster;
import oscar.util.ConversionUtils;
import oscar.util.UtilMisc;

/**
 *+------------------+------------+------+-----+---------+----------------+
  | Field            | Type       | Null | Key | Default | Extra          |
  +------------------+------------+------+-----+---------+----------------+
  | billingnote_no   | int(10)    |      | PRI | NULL    | auto_increment |
  | billingmaster_no | int(10)    |      | MUL | 0       |                |
  | createdate       | datetime   | YES  | MUL | NULL    |                |
  | provider_no      | varchar(6) |      | MUL |         |                |
  | note             | text       | YES  |     | NULL    |                |
  | note_type        | int(2)     | YES  |     | NULL    |                |
  +------------------+------------+------+-----+---------+----------------+
 * @author  root
 */
public class BillingNote {

	/** Creates a new instance of BillingNote */
	public BillingNote() {
	}

	//
	public boolean hasNote(String billingmaster_no) {
		BillingNoteDao dao = SpringUtils.getBean(BillingNoteDao.class);
		return !dao.findNotes(ConversionUtils.fromIntString(billingmaster_no), 2).isEmpty();
	}

	public void addNote(String billingmaster_no, String provider_no, String note) {
		note = oscar.Misc.removeNewLine(note);

		BillingNotes n = new BillingNotes();
		n.setBillingmasterNo(ConversionUtils.fromIntString(billingmaster_no));
		n.setProviderNo(provider_no);
		n.setCreatedate(new Date());
		n.setNote(UtilMisc.mysqlEscape(note));
		n.setNoteType(BillingNotes.DEFAULT_NOTE_TYPE);

		BillingNoteDao dao = SpringUtils.getBean(BillingNoteDao.class);
		dao.persist(n);
	}

	public void addNoteFromBillingNo(String billingNo, String provider, String note) {
		BillingmasterDAO dao = SpringUtils.getBean(BillingmasterDAO.class);
		List<Billingmaster> bmdao = dao.getBillingmasterByBillingNo( Integer.parseInt( billingNo ) );
		for( Billingmaster bm : bmdao ) {
			if (bm != null) { 
				addNote(String.valueOf(bm.getBillingmasterNo()), provider, oscar.Misc.removeNewLine(note));
			}
		}
	}

	/**
	 *
	 * @param billingmaster_no billingmaster_no from billingmaster table to get the full note class
	 * @return Returns a Note Class
	 */
	public Note getFullNote(String billingmaster_no) {
		BillingNoteDao dao = SpringUtils.getBean(BillingNoteDao.class);
		BillingNotes notes = dao.findSingleNote(ConversionUtils.fromIntString(billingmaster_no), BillingNotes.DEFAULT_NOTE_TYPE);
		Note result = (notes != null) ? new Note(notes) : new Note();
		return result;
	}

	public String getNote(String billingmaster_no) {
		BillingNoteDao dao = SpringUtils.getBean(BillingNoteDao.class);
		BillingNotes notes = dao.findSingleNote(ConversionUtils.fromIntString(billingmaster_no), BillingNotes.DEFAULT_NOTE_TYPE);
		if (notes == null) return "";
		return notes.getNote();
	}

	/*
	First - REC-CODE-IN (3) must be 'N01'
	Second - DATA-CENTRE-NUM (5)
	Third - DATA-CENTRE-SEQNUM (7)
	Fourth - PAYEE-NUM (5)
	Fifth - PRACTITIONER-NUM (5)
	Sixth - NOTE-DATA-TYPE (1)
	Seventh - NOTE-DATA-LINE (400)
	*/
	public static String getN01(String dataCenterNum, String dataCenterSeqNum, String payeeNum, String practitionerNum, String noteType, String note) {
		String s = "N01" + Misc.forwardZero(dataCenterNum, 5) + Misc.forwardZero(dataCenterSeqNum, 7) + Misc.forwardZero(payeeNum, 5) + Misc.forwardZero(practitionerNum, 5) + Misc.forwardSpace(noteType, 1) + Misc.forwardSpace(note, 400);
		return s;
	}

	class Note {

		String billingnote_no = null;
		String billingmaster_no = null;
		String createdate = null;
		String provider_no = null;
		String note = null;

		public Note() {

		}

		public Note(BillingNotes notes) {
			setBillingnote_no(notes.getId().toString());
			setBillingmaster_no(String.valueOf(notes.getBillingmasterNo()));
			setCreatedate(ConversionUtils.toDateString(notes.getCreatedate()));
			setProviderNo(notes.getProviderNo());
			setNote(notes.getNote());
		}

		/**
		 * Getter for property billingnote_no.
		 * @return Value of property billingnote_no.
		 */
		public java.lang.String getBillingnote_no() {
			return billingnote_no;
		}

		/**
		 * Setter for property billingnote_no.
		 * @param billingnote_no New value of property billingnote_no.
		 */
		public void setBillingnote_no(java.lang.String billingnote_no) {
			this.billingnote_no = billingnote_no;
		}

		/**
		 * Getter for property billingmaster_no.
		 * @return Value of property billingmaster_no.
		 */
		public java.lang.String getBillingmaster_no() {
			return billingmaster_no;
		}

		/**
		 * Setter for property billingmaster_no.
		 * @param billingmaster_no New value of property billingmaster_no.
		 */
		public void setBillingmaster_no(java.lang.String billingmaster_no) {
			this.billingmaster_no = billingmaster_no;
		}

		/**
		 * Getter for property createdate.
		 * @return Value of property createdate.
		 */
		public java.lang.String getCreatedate() {
			return createdate;
		}

		/**
		 * Setter for property createdate.
		 * @param createdate New value of property createdate.
		 */
		public void setCreatedate(java.lang.String createdate) {
			this.createdate = createdate;
		}

		/**
		 * Getter for property provider_no.
		 * @return Value of property provider_no.
		 */
		public java.lang.String getProviderNo() {
			return provider_no;
		}

		/**
		 * Setter for property provider_no.
		 * @param provider_no New value of property provider_no.
		 */
		public void setProviderNo(java.lang.String provider_no) {
			this.provider_no = provider_no;
		}

		/**
		 * Getter for property note.
		 * @return Value of property note.
		 */
		public java.lang.String getNote() {
			return note;
		}

		/**
		 * Setter for property note.
		 * @param note New value of property note.
		 */
		public void setNote(java.lang.String note) {
			this.note = note;
		}

	}

}
