package oscar.eform.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import oscar.eform.model.EformData;
import oscar.eform.model.EformValue;

public class EformDao extends HibernateDaoSupport {

	public void addEformData(EformData eformData) {
		getHibernateTemplate().merge(eformData);
	}

	public void addEformValue(EformValue eformValue) {
		getHibernateTemplate().merge(eformValue);
	}

        public List<EformData> getEformDatabyDemoNo(Integer demoNo) {
            String queryStr = "FROM EformData e WHERE e.demographic_no = "+demoNo;

            @SuppressWarnings("unchecked")
            List<EformData> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }

        public List<EformValue> getEformValuebyDemoNo(Integer demoNo) {
            String queryStr = "FROM EformValue e WHERE e.demographic_no = "+demoNo;

            @SuppressWarnings("unchecked")
            List<EformValue> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }

        public List<EformData> getEformDatabyProvider(String providerNo) {
            String queryStr = "FROM EformData e WHERE e.form_provider = "+providerNo;

            @SuppressWarnings("unchecked")
            List<EformData> rs = getHibernateTemplate().find(queryStr);

            return rs;
        }
}
