/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Billingreferral;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Toby
 */
@Repository
public class BillingreferralDao extends AbstractDao<Billingreferral> {

	public BillingreferralDao() {
		super(Billingreferral.class);
	}

	 public Billingreferral getByReferralNo(String referral_no) {
		 String sql = "select br From Billingreferral br WHERE br.referralNo=?";
		 Query query = entityManager.createQuery(sql);
		 query.setParameter(1, referral_no);

		 @SuppressWarnings("unchecked")
		 List<Billingreferral> brs = query.getResultList();
		 if(!brs.isEmpty())
			 return brs.get(0);
		 return null;
	 }

	 public Billingreferral getById(int id) {
		 return find(id);
	 }

	 public List<Billingreferral> getBillingreferrals() {
		 String sql = "SELECT br From Billingreferral br ORDER BY  br.lastName, br.firstName";
		 Query query = entityManager.createQuery(sql);

		 @SuppressWarnings("unchecked")
		 List<Billingreferral> brs = query.getResultList();

		 return brs;
	 }

    public List<Billingreferral> getBillingreferral(String referral_no) {
    	String sql = "SELECT br From Billingreferral br WHERE br.referralNo=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, referral_no);

		@SuppressWarnings("unchecked")
		List<Billingreferral> cList = query.getResultList();

        if (cList != null && cList.size() > 0) {
            return cList;
        } else {
            return null;
        }
    }

    public List<Billingreferral> getBillingreferral(String last_name, String first_name) {
    	String sql = "SELECT br From Billingreferral br WHERE br.lastName like ? and br.firstName like ? order by br.lastName";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, "%"+last_name+"%");
		query.setParameter(2, "%"+first_name+"%");

		@SuppressWarnings("unchecked")
		List<Billingreferral> cList = query.getResultList();

        if (cList != null && cList.size() > 0) {
            return cList;
        } else {
            return null;
        }
    }

    public List<Billingreferral> getBillingreferralByLastName(String last_name) {
    	String sql = "SELECT br From Billingreferral br WHERE br.lastName like ? order by br.lastName";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, "%"+last_name+"%");

		@SuppressWarnings("unchecked")
		List<Billingreferral> cList = query.getResultList();

        if (cList != null && cList.size() > 0) {
            return cList;
        } else {
            return null;
        }
    }


    public List<Billingreferral> getBillingreferralBySpecialty(String specialty) {
    	String sql = "SELECT br From Billingreferral br WHERE br.specialty like ? order by br.lastName";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, "%"+specialty+"%");

		@SuppressWarnings("unchecked")
		List<Billingreferral> cList = query.getResultList();

        if (cList != null && cList.size() > 0) {
            return cList;
        } else {
            return null;
        }
    }

    /*
     * Don't blame me for this one, converted from SQL.
     */
     public List<Billingreferral> searchReferralCode(String codeName, String codeName1, String codeName2, String desc, String fDesc, String desc1, String fDesc1, String desc2, String fDesc2) {
    	String sql = "SELECT b FROM Billingreferral b WHERE b.referralNo LIKE ? or b.referralNo LIKE ? or b.referralNo LIKE ?"
        		+ " or (b.lastName LIKE ? and b.firstName LIKE ?)"
        		+ " or (b.lastName LIKE ? and b.firstName LIKE ?)"
        		+ " or (b.lastName LIKE ? and b.firstName LIKE ?)";

		Query query = entityManager.createQuery(sql);
		query.setParameter(1, codeName);
		query.setParameter(2, codeName1);
		query.setParameter(3, codeName2);
		query.setParameter(4, desc);
		query.setParameter(5, fDesc);
		query.setParameter(6, desc1);
		query.setParameter(7, fDesc1);
		query.setParameter(8, desc2);
		query.setParameter(9, fDesc2);


		@SuppressWarnings("unchecked")
		List<Billingreferral> cList = query.getResultList();

        return cList;
    }

    public void updateBillingreferral(Billingreferral obj) {
    	if(obj.getBillingreferralNo() == null || obj.getBillingreferralNo().intValue() == 0) {
    		persist(obj);
    	} else {
    		merge(obj);
    	}
    }

	 public String getReferralDocName(String referral_no) {
		 Billingreferral br = this.getByReferralNo(referral_no);

		 if(br != null)
			 return br.getLastName() + ", " + br.getFirstName();

		 return "";
	 }
}
