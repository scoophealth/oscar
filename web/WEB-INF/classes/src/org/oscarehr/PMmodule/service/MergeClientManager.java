package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.MergeClientDao;
import org.oscarehr.PMmodule.model.ClientMerge;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.web.formbean.ClientSearchFormBean;

public class MergeClientManager {
	private MergeClientDao mergeClientDao;
	private ClientDao	clientDao;

	public void setMergeClientDao(MergeClientDao mergeClientDao) {
		this.mergeClientDao = mergeClientDao;
	}

	public void merge(ClientMerge cmObj) {
		mergeClientDao.merge(cmObj);
	}

	public void unMerge(ClientMerge cmObj) {
		mergeClientDao.unMerge(cmObj);
	}

	public Integer getHead(Integer demographic_no) {
		return mergeClientDao.getHead(demographic_no);
	}

	public List getTail(Integer demographic_no) {
		return mergeClientDao.getTail(demographic_no);
	}
	public ClientMerge getClientMerge(Integer demographic_no) {
		return mergeClientDao.getClientMerge(demographic_no);
	}
	public List  searchMerged(ClientSearchFormBean criteria){
		List lst=this.clientDao.search(criteria, false,false);
		List result = new ArrayList();
		Iterator items =lst.iterator();
		while(items.hasNext()){
			Demographic client=(Demographic)items.next();
			if(!client.getSubRecord().isEmpty()) {
				Iterator subs = client.getSubRecord().iterator();
				while(subs.hasNext()){
					Integer cId=(Integer)subs.next();
					Demographic mergedClient= clientDao.getClientByDemographicNo(cId); 
					result.add(mergedClient);
					result.add(client);
				}				
			}
		}
		return result;
	}
	
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
}
