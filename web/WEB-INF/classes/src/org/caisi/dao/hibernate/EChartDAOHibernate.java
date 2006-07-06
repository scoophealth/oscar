package org.caisi.dao.hibernate;


import org.caisi.dao.EChartDAO;
import org.caisi.model.EChart;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for the corresponding DAO interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class EChartDAOHibernate extends HibernateDaoSupport implements
		EChartDAO {

	public void saveEncounter(EChart chart) {
		this.getHibernateTemplate().save(chart);
	}

	public EChart getLatestChart(int demographicNo) {
		return (EChart)getHibernateTemplate().find("from EChart c where c.demographicNo = ? order by c.timeStamp desc",new Object[] {String.valueOf(demographicNo)}).get(0);
	}
}
