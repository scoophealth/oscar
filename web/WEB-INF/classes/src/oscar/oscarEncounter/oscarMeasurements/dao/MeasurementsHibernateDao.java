package oscar.oscarEncounter.oscarMeasurements.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.model.Measurements;
import oscar.oscarEncounter.oscarMeasurements.model.Measurementtype;

public class MeasurementsHibernateDao extends HibernateDaoSupport implements
		MeasurementsDao {

	public int displayCount = 10;

	public void addMeasurements(Measurements measurements) {
		getHibernateTemplate().merge(measurements);
	}

	public HashMap<String, EctMeasurementsDataBean> getMeasurementsByDate(
			Date date, String demo) {
		String hql = "SELECT mt.type, mt.typeDisplayName, mt.typeDescription,m.dataField,mt.measuringInstruction,m.comments FROM Measurements m,"
				+ "Measurementtype mt WHERE m.dateObserved=? AND m.demographicNo=? AND m.type = mt.type "
				+ "GROUP BY mt.type ORDER BY m.type ASC";
		HashMap<String, EctMeasurementsDataBean> mDataList = new HashMap<String, EctMeasurementsDataBean>();
		List results = getHibernateTemplate().find(hql,
				new Object[] { date, new Integer(demo) });
		Iterator itr = results.iterator();
		while (itr.hasNext()) {
			Object[] row = (Object[]) (itr.next());
			EctMeasurementsDataBean mdb = new EctMeasurementsDataBean();
			mdb.setType((String) row[0]);
			mdb.setTypeDisplayName((String) row[1]);
			mdb.setTypeDescription((String) row[2]);
			mdb.setDataField((String) row[3]);
			mdb.setMeasuringInstrc((String) row[4]);
			mdb.setComments((String) row[5]);
			mDataList.put(mdb.getType(), mdb);

		}

		return mDataList;
	}

	public SortedMap<String, String> getPatientMeasurementTypeDescriptions(String demo) {
		Criteria c1 = getSession().createCriteria(Measurements.class);
		Criteria c2 = getSession().createCriteria(Measurementtype.class);
		List<String> types=(List<String>)c1.setProjection(Projections.projectionList().add(
				Projections.groupProperty("type")))
				.add(Expression.eq("demographicNo",new Integer(demo))).list();
		
		c2 = c2.setProjection(
				Projections.projectionList().add(
						Projections.groupProperty("type")).add(
						Projections.property("typeDescription")))
						.add(Expression.in("type", types));
		List<Object[]> rs = new ArrayList();
		if (types!=null && types.size()>0) rs=c2.list();
		SortedMap<String, String> rm = new TreeMap(new Comparator<String>(){
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}});
		for (Object[] i : rs) {
			rm.put((String) i[0], (String) i[1]);
		}
		
		return rm;
	}

	public List<Date> getMeasurementsDatesByDateRange(Date date, int flag,
			String demo) {
		Criteria c = getSession().createCriteria(Measurements.class);
		c = c.setProjection(
				Projections.projectionList().add(
						Projections.groupProperty("dateObserved"))).add(
				Expression.eq("demographicNo", new Integer(demo)));
		if (flag == 1)
			c = c.add(Expression.gt("dateObserved", date));
		if (flag == -1)
			c = c.add(Expression.lt("dateObserved", date));
		c = c.addOrder(Order.desc("dateObserved")).setMaxResults(displayCount);
		
		List<Date> dateList = new ArrayList();

		for (Object mm : (List<Object>)c.list()) {
			dateList.add((Date)mm);
		}
		return dateList;
	}
}
