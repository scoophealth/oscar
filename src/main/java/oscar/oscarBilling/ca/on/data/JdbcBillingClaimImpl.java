/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.billing.CA.ON.dao.BillingONDiskNameDao;
import org.oscarehr.billing.CA.ON.dao.BillingONFilenameDao;
import org.oscarehr.billing.CA.ON.dao.BillingONHeaderDao;
import org.oscarehr.billing.CA.ON.model.BillingONDiskName;
import org.oscarehr.billing.CA.ON.model.BillingONFilename;
import org.oscarehr.billing.CA.ON.model.BillingONHeader;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONExtDao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.dao.BillingONRepoDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.BillingONRepo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;

public class JdbcBillingClaimImpl {
	
	private BillingONHeaderDao dao = SpringUtils.getBean(BillingONHeaderDao.class);
	private BillingONCHeader1Dao cheaderDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
	private BillingONItemDao itemDao = SpringUtils.getBean(BillingONItemDao.class);
	private BillingONExtDao extDao = (BillingONExtDao)SpringUtils.getBean(BillingONExtDao.class);
	private BillingONDiskNameDao diskNameDao = SpringUtils.getBean(BillingONDiskNameDao.class);
	private BillingONFilenameDao filenameDao = SpringUtils.getBean(BillingONFilenameDao.class);
	private BillingONRepoDao repoDao = SpringUtils.getBean(BillingONRepoDao.class);

	

	SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat tsFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public int addOneBatchHeaderRecord(BillingBatchHeaderData val) {
		BillingONHeader b = new BillingONHeader();
		b.setDiskId(Integer.parseInt(val.disk_id));
		b.setTransactionId(val.transc_id);
		b.setRecordId(val.rec_id);
		b.setSpecId(val.spec_id);
		b.setMohOffice(val.moh_office);
		b.setBatchId(val.batch_id);
		b.setOperator(val.operator);
		b.setGroupNum(val.group_num);
		b.setProviderRegNum(val.provider_reg_num);
		b.setSpecialty(val.specialty);
		b.sethCount(val.h_count);
		b.setrCount(val.r_count);
		b.settCount(val.t_count);
		b.setBatchDate(new Date());
		b.setCreateDateTime(new Date());
		b.setUpdateDateTime(new Date());
		b.setCreator(val.creator);
		b.setAction(val.action);
		b.setComment(val.comment);
		
		dao.persist(b);
		
		return b.getId();
	}

	public int addOneClaimHeaderRecord(BillingClaimHeader1Data val) {
		BillingONCHeader1 b = new BillingONCHeader1();
		b.setHeaderId(0);
		b.setTranscId(val.transc_id);
		b.setRecId(val.rec_id);
		b.setHin(val.hin);
		b.setVer(val.ver);
		b.setDob(val.dob);
		b.setPayProgram(val.pay_program);
		b.setPayee(val.payee);
		b.setRefNum(val.ref_num);
		b.setFaciltyNum(val.facilty_num);
		if(val.admission_date.length()>0)
			try{
				b.setAdmissionDate(dateformatter.parse(val.admission_date));
			}catch(ParseException e){/*empty*/}
		
		b.setRefLabNum(val.ref_lab_num);
		b.setManReview(val.man_review);
		b.setLocation(val.location);
		b.setDemographicNo(Integer.parseInt(val.demographic_no));
		b.setProviderNo(val.provider_no);
		String apptNo = StringUtils.trimToNull(val.appointment_no);
		
		if( apptNo != null ) {
			b.setAppointmentNo(Integer.parseInt(val.appointment_no));
		}
		else {
			b.setAppointmentNo(null);
		}
		
		b.setDemographicName(val.demographic_name);
		b.setSex(val.sex);
		b.setProvince(val.province);
		if(val.billing_date.length()>0)
			try {
				b.setBillingDate(dateformatter.parse(val.billing_date));
			}catch(ParseException e){/*empty*/}
		if(val.billing_time.length()>0)
			try {
				b.setBillingTime(timeFormatter.parse(val.billing_time));
			}catch(ParseException e){MiscUtils.getLogger().error("Invalid time", e);}

		b.setTotal(val.total);
		b.setPaid(val.paid);
		b.setStatus(val.status);
		b.setComment(val.comment);
		b.setVisitType(val.visittype);
		b.setProviderOhipNo(val.provider_ohip_no);
		b.setProviderRmaNo(val.provider_rma_no);
		b.setApptProviderNo(val.apptProvider_no);
		b.setAsstProviderNo(val.asstProvider_no);
		b.setCreator(val.creator);
		b.setClinic(val.clinic);
		
		cheaderDao.persist(b);
		
		return b.getId();
	}

	public boolean addItemRecord(List lVal, int id) {
	
		boolean retval = true;
		for (int i = 0; i < lVal.size(); i++) {
			BillingItemData val = (BillingItemData) lVal.get(i);
			
			BillingONItem b = new BillingONItem();
			b.setCh1Id(id);
			b.setTranscId(val.transc_id);
			b.setRecId(val.rec_id);
			b.setServiceCode(val.service_code);
			b.setFee(val.fee);
			b.setServiceCount(val.ser_num);
			if(val.service_date.length()>0)
				try {
					b.setServiceDate(dateformatter.parse(val.service_date));
				}catch(ParseException e){/*empty*/}
			b.setDx(val.dx);
			b.setDx1(val.dx1);
			b.setDx2(val.dx2);
			b.setStatus(val.status);
			
			itemDao.persist(b);
		}
		return retval;
	}

	public boolean add3rdBillExt(Map<String,String>mVal, int id) {
		boolean retval = true;
		String[] temp = { "billTo", "remitTo", "total", "payment", "refund", "provider_no", "gst", "payDate", "payMethod"};
		String demoNo = mVal.get("demographic_no");
		String dateTime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");
                mVal.put("payDate", dateTime);
		for (int i = 0; i < temp.length; i++) {
				BillingONExt b = new BillingONExt();
				b.setBillingNo(id);
				b.setDemographicNo(Integer.valueOf(demoNo));
				b.setKeyVal(temp[i]);
				b.setValue(mVal.get(temp[i]));
				b.setDateTime(new Date());
				b.setStatus('1');
				b.setPaymentId(0);
				extDao.persist(b);
		}
		return retval;
	}

	// add disk file
	public int addBillingDiskName(BillingDiskNameData val) {
		BillingONDiskName b = new BillingONDiskName();
		b.setMonthCode(val.monthCode);
		b.setBatchCount(Integer.parseInt(val.batchcount));
		b.setOhipFilename(val.ohipfilename);
		b.setGroupNo(val.groupno);
		b.setCreator(val.creator);
		b.setClaimRecord(val.claimrecord);
		b.setCreateDateTime(new Date());
		b.setStatus(val.status);
		b.setTotal(val.total);
		
		diskNameDao.persist(b);
		
		int retval = b.getId();
		
		if (b.getId() > 0) {
			// add filenames, if needed
			for (int i = 0; i < val.providerohipno.size(); i++) {
				BillingONFilename f = new BillingONFilename();
				f.setDiskId(b.getId());
				f.setHtmlFilename((String)val.htmlfilename.get(i));
				f.setProviderOhipNo((String)val.providerohipno.get(i));
				f.setProviderNo((String)val.providerno.get(i));
				f.setClaimRecord((String)val.vecClaimrecord.get(0));
				f.setStatus((String)val.vecStatus.get(0));
				f.setTotal((String)val.vecTotal.get(0));
				filenameDao.persist(f);
			}

		} else {
			retval = 0;
		}

		return retval;
	}

	public String[] getLatestSoloMonthCodeBatchNum(String providerNo) {
		String[] retval = null;
		
		BillingONDiskName b = diskNameDao.getLatestSoloMonthCodeBatchNum(providerNo);
		if(b != null) {
			retval = new String[2];
			retval[0] = b.getMonthCode();
			retval[1] = "" + b.getBatchCount();
		} else {
			retval=null;
		}
			
		return retval;
	}

	public String[] getLatestGrpMonthCodeBatchNum(String groupNo) {
		String[] retval = null;
		BillingONDiskName b = diskNameDao.findByGroupNo(groupNo);
		if(b != null) {
			retval = new String[2];
			retval[0] = b.getMonthCode();
			retval[1] = "" +b.getBatchCount();
		}else {
			retval = null;
		}

		return retval;
	}

	public String getPrevDiskCreateDate(String diskId) {
		String retval = null;
		Date curDate =null;
		String groupNo = "";
		
		
		BillingONDiskName b= diskNameDao.find(Integer.valueOf(diskId));
		if(b != null) {
			curDate = b.getCreateDateTime();
			groupNo = b.getGroupNo();

			BillingONDiskName x = diskNameDao.getPrevDiskCreateDate(curDate,groupNo);
			if(x != null) {
				Date tmp = x.getCreateDateTime();
				retval = dateformatter.format(tmp);
			}
		}
		return retval;
	}
	

	public String getDiskCreateDate(String diskId) {
		String retval = null;
		
		BillingONDiskName b = diskNameDao.find(Integer.parseInt(diskId));
		if(b != null) {
			Date tmp = b.getCreateDateTime();
			retval = dateformatter.format(tmp);
		}
		return retval;
	}
	

	public List getMRIList(String sDate, String eDate, String status) {
		List retval = new Vector();
		BillingDiskNameData obj = null;
		
		try {
			List<BillingONDiskName> results = diskNameDao.findByCreateDateRangeAndStatus(dateformatter.parse(sDate),dateformatter.parse(eDate),status);
	
			for(BillingONDiskName b : results) {
				obj = new BillingDiskNameData();
				obj.setId("" + b.getId());
				obj.setMonthCode(b.getMonthCode());
				obj.setBatchcount("" + b.getBatchCount());
				obj.setOhipfilename(b.getOhipFilename());
				obj.setGroupno(b.getGroupNo());
				obj.setClaimrecord(b.getClaimRecord());
				obj.setCreatedatetime(tsFormatter.format(b.getCreateDateTime()));
				obj.setUpdatedatetime(tsFormatter.format(b.getTimestamp()));
				obj.setStatus(b.getStatus());
				obj.setTotal(b.getTotal());
				
				List<BillingONFilename> ff = filenameDao.findByDiskIdAndStatus(b.getId(),status);
				Vector vecHtmlfilename = new Vector();
				Vector vecProviderohipno = new Vector();
				Vector vecProviderno = new Vector();
				Vector vecClaimrecord = new Vector();
				Vector vecStatus = new Vector();
				Vector vecTotal = new Vector();
				Vector vecFilenameId = new Vector();
					
				for(BillingONFilename f:ff) {	
					vecFilenameId.add("" + f.getId());
					vecHtmlfilename.add(f.getHtmlFilename());
					vecProviderohipno.add(f.getProviderOhipNo());
					vecProviderno.add(f.getProviderNo());
					vecClaimrecord.add(f.getClaimRecord());
					vecStatus.add(f.getStatus());
					vecTotal.add(f.getTotal());
				}
				obj.setVecFilenameId(vecFilenameId);
				obj.setHtmlfilename(vecHtmlfilename);
				obj.setProviderohipno(vecProviderohipno);
				obj.setProviderno(vecProviderno);
				obj.setVecClaimrecord(vecClaimrecord);
				obj.setVecStatus(vecStatus);
				obj.setVecTotal(vecTotal);
				retval.add(obj);	
			}
		} catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
			retval=null;
		}
		return retval;
	}

	public String getOhipfilename(int diskId) {
		BillingONDiskName b = diskNameDao.find(diskId);
		if(b != null) {
			return b.getOhipFilename();
		}
		return "";
	}

	public String getHtmlfilename(int diskId, String providerNo) {
		String obj="";
		List<BillingONFilename> results = filenameDao.findByDiskIdAndProvider(diskId, providerNo);
		for(BillingONFilename result:results) {
			obj = result.getHtmlFilename();
		}
		return obj;
	}

	public BillingDiskNameData getDisknameObj(String diskId) {
		BillingDiskNameData obj = new BillingDiskNameData();
		
		BillingONDiskName b = diskNameDao.find(Integer.valueOf(diskId));
		if(b != null) {
			obj.setId("" + b.getId());
			obj.setMonthCode(b.getMonthCode());
			obj.setBatchcount("" + b.getBatchCount());
			obj.setOhipfilename(b.getOhipFilename());
			obj.setGroupno(b.getGroupNo());
			obj.setClaimrecord(b.getCreator());
			obj.setClaimrecord(b.getClaimRecord());
			obj.setCreatedatetime(tsFormatter.format(b.getCreateDateTime()));
			obj.setStatus(b.getStatus());
			obj.setTotal(b.getTotal());
			obj.setUpdatedatetime(tsFormatter.format(b.getTimestamp()));
			
			List<BillingONFilename> ff = filenameDao.findCurrentByDiskId(b.getId());
			Vector vecHtmlfilename = new Vector();
			Vector vecProviderohipno = new Vector();
			Vector vecProviderno = new Vector();
			Vector vecClaimrecord = new Vector();
			Vector vecStatus = new Vector();
			Vector vecTotal = new Vector();
			Vector vecFilenameId = new Vector();
			for(BillingONFilename f:ff) {
				vecFilenameId.add("" + f.getId());
				vecHtmlfilename.add(f.getHtmlFilename());
				vecProviderohipno.add(f.getProviderOhipNo());
				vecProviderno.add(f.getProviderNo());
				vecClaimrecord.add(f.getClaimRecord());
				vecStatus.add(f.getStatus());
				vecTotal.add(f.getTotal());
			}

			obj.setVecFilenameId(vecFilenameId);
			obj.setHtmlfilename(vecHtmlfilename);
			obj.setProviderohipno(vecProviderohipno);
			obj.setProviderno(vecProviderno);
			obj.setVecClaimrecord(vecClaimrecord);
			obj.setVecStatus(vecStatus);
			obj.setVecTotal(vecTotal);
		}

		return obj;
	}

	public int addRepoDiskName(BillingDiskNameData val) {
		int retval = 0;
		BillingONRepo b = new BillingONRepo();
		b.sethId(Integer.parseInt(val.id));
		b.setCategory("billing_on_diskname");
		b.setContent(val.monthCode + "|" + val.batchcount + "|" + val.ohipfilename + "|" + val.groupno + "|" + val.creator
				+ "|" + val.claimrecord + "|" + val.createdatetime + "|" + val.status + "|" + val.total + "|"
				+ val.updatedatetime);
		b.setCreateDateTime(new Date());
		
		repoDao.persist(b);
		retval = b.getId();
		
		if (b.getId() > 0) {
			// add filenames, if needed
			for (int i = 0; i < val.providerohipno.size(); i++) {
				BillingONRepo r = new BillingONRepo();
				r.sethId(Integer.valueOf((String)val.vecFilenameId.get(i)));
				r.setCategory("billing_on_filename");
				r.setContent(val.id + "|" + val.htmlfilename.get(i) + "|"
						+ val.providerohipno.get(i) + "|" + val.providerno.get(i) + "|" + val.vecClaimrecord.get(0)
						+ "|" + val.vecStatus.get(0) + "|" + val.vecTotal.get(0) + "|" + val.updatedatetime);
				
				r.setCreateDateTime(new Date());
				
				repoDao.persist(r);
			}
		} else {
			retval = 0;
		}
		return retval;
	}

	public boolean updateDiskName(BillingDiskNameData val) {
		BillingONDiskName b = diskNameDao.find(Integer.parseInt(val.getId()));
		if(b != null) {
			b.setCreator(val.creator);
			diskNameDao.merge(b);
		}
		return true;
	}

	public BillingBatchHeaderData getBatchHeaderObj(BillingProviderData providerData, String disk_id) {
		BillingBatchHeaderData obj = new BillingBatchHeaderData();
		
		List<BillingONHeader> bs = dao.findByDiskIdAndProviderRegNum(Integer.parseInt(disk_id),providerData.getOhipNo());
		
		for(BillingONHeader b:bs) {
			obj.setId("" +b.getId());
			obj.setDisk_id(disk_id);
			obj.setTransc_id(b.getTransactionId());
			obj.setRec_id(b.getRecordId());
			obj.setSpec_id(b.getSpecId());
			obj.setMoh_office(b.getMohOffice());

			obj.setBatch_id(b.getBatchId());
			obj.setOperator(b.getOperator());
			obj.setGroup_num(b.getGroupNum());
			obj.setProvider_reg_num(b.getProviderRegNum());
			obj.setSpecialty(b.getSpecialty());
			obj.setH_count(b.gethCount());
			obj.setR_count(b.getrCount());
			obj.setT_count(b.gettCount());
			obj.setBatch_date(dateformatter.format(b.getBatchDate()));

			obj.setCreatedatetime(tsFormatter.format(b.getCreateDateTime()));
			obj.setUpdatedatetime(tsFormatter.format(b.getUpdateDateTime()));
			obj.setCreator(b.getCreator());
			obj.setAction(b.getAction());
			obj.setComment(b.getComment());
		}

		return obj;
	}

	public int addRepoBatchHeader(BillingBatchHeaderData val) {
		BillingONRepo b = new BillingONRepo();
		b.sethId(Integer.parseInt(val.id));
		b.setCategory("billing_on_header");
		b.setContent(val.disk_id + "|" + val.transc_id + "|" + val.rec_id + "|" + val.spec_id + "|" + val.moh_office + "|"
				+ val.batch_id + "|" + val.operator + "|" + val.group_num + "|" + val.provider_reg_num + "|"
				+ val.specialty + "|" + val.h_count + "|" + val.r_count + "|" + val.t_count + "|" + val.batch_date
				+ "|" + val.createdatetime + "|" + val.updatedatetime + "|" + val.creator + "|" + val.action + "|"
				+ val.comment);
		b.setCreateDateTime(new Date());
		repoDao.persist(b);
		
		return b.getId();
	}

	// TODO more update data
	public boolean updateBatchHeaderRecord(BillingBatchHeaderData val) {
		BillingONHeader b = dao.find(Integer.parseInt(val.getId()));
		if(b != null) {
			b.setMohOffice(val.moh_office);
			b.setBatchId(val.batch_id);
			b.setSpecialty(val.specialty);
			b.setCreator(val.creator);
			b.setUpdateDateTime(new Date());
			b.setAction(val.action);
			b.setComment(val.comment);
			dao.merge(b);
		}
		
		return true;
	}
        
}
