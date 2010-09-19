package oscar.eform.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.eform.model.EformValue;

public class EformDao extends HibernateDaoSupport {

	public List<EformValue> getEformValuebyDemoNo(Integer demoNo) {
		String queryStr = "FROM EformValue e WHERE e.demographic_no = "+demoNo;

		@SuppressWarnings("unchecked")
		List<EformValue> rs = getHibernateTemplate().find(queryStr);

		return rs;
	}
}
