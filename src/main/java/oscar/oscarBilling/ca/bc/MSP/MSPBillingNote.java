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
 * MSPBillingNote.java
 *
 * Created on July 1, 2004, 1:18 PM
 */

package oscar.oscarBilling.ca.bc.MSP;

import java.util.Date;

import org.oscarehr.billing.CA.BC.dao.BillingNoteDao;
import org.oscarehr.billing.CA.BC.model.BillingNotes;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.Misc;
import oscar.util.ConversionUtils;

/**
 *
 * @author  root
 *
 * This class is used to deal with MSP N01 correspondence notes.
 *
 */
public class MSPBillingNote {

	private BillingNoteDao billingNoteDao = SpringUtils.getBean(BillingNoteDao.class);

	/** Creates a new instance of MSPBillingNote */
	public MSPBillingNote() {
	}

	public void addNote(String billingmaster_no, String provider_no, String note) {
		note = oscar.Misc.removeNewLine(note);

		BillingNotes b = new BillingNotes();
		b.setBillingmasterNo(Integer.parseInt(billingmaster_no));
		b.setProviderNo(provider_no);
		b.setCreatedate(new Date());
		b.setNote(note);
		b.setNoteType(1);
		billingNoteDao.persist(b);
	}

	/**
	 *
	 * @param billingmaster_no billingmaster_no from billingmaster table to get the full note class
	 * @return Returns a Note Class
	 */
	public Note getFullNote(String billingmaster_no) {
		Note n = new Note();

		BillingNoteDao dao = SpringUtils.getBean(BillingNoteDao.class);
		BillingNotes bn = dao.findSingleNote(ConversionUtils.fromIntString(billingmaster_no), 1);
		if (bn != null) {
			n.setBillingnote_no("" + bn.getId());
			n.setBillingmaster_no("" + bn.getBillingmasterNo());
			n.setCreatedate(ConversionUtils.toDateString(bn.getCreatedate()));
			n.setProviderNo(bn.getProviderNo());
			n.setNote(bn.getNote());
		}
		return n;
	}

	public String getNote(String billingmaster_no) {
		return getFullNote(billingmaster_no).getNote();
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
		MiscUtils.getLogger().debug("LOOKATME:" + note);
		String s = "N01" + Misc.forwardZero(dataCenterNum, 5) + Misc.forwardZero(dataCenterSeqNum, 7) + Misc.forwardZero(payeeNum, 5) + Misc.forwardZero(practitionerNum, 5) + Misc.forwardSpace(noteType, 1) + Misc.backwardSpace(Misc.stripLineBreaks(note), 400);
		return s;
	}

	class Note {
		String billingnote_no = null;
		String billingmaster_no = null;
		String createdate = null;
		String provider_no = null;
		String note = "";

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
