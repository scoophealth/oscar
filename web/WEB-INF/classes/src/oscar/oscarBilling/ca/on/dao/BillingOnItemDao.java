package oscar.oscarBilling.ca.on.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.oscarBilling.ca.on.model.BillingOnCHeader1;
import oscar.oscarBilling.ca.on.model.BillingOnItem;

public class BillingOnItemDao extends HibernateDaoSupport {

	public void addBillingOnItem(BillingOnItem billingItem) {
		getHibernateTemplate().merge(billingItem);
	}

        public List<BillingOnItem> getBillingItemById(Integer id) {
            String queryStr = "FROM BillingOnItem b WHERE b.id = "+id;

            @SuppressWarnings("unchecked")
            List<BillingOnItem> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }

        public List<BillingOnItem> getBillingItemByCh1Id(Integer ch1_id) {
            String queryStr = "FROM BillingOnItem b WHERE b.ch1_id = "+ch1_id+" ORDER BY b.id";

            @SuppressWarnings("unchecked")
            List<BillingOnItem> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }

        public List<BillingOnCHeader1> getCh1ByDemographicNo(Integer demographic_no) {
            String queryStr = "FROM BillingOnCHeader1 b WHERE b.demographic_no = "+demographic_no+" ORDER BY b.id";

            @SuppressWarnings("unchecked")
            List<BillingOnCHeader1> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }
}
