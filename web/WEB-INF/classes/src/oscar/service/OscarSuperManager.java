package oscar.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import oscar.dao.OscarSuperDao;

/**
 * Oscar super manager implementation created to extract database access code
 * from JSP files. This class should be injected with every scope named DAO
 * class that extends OscarSuperDao. Use methods of this manager in JSP/Action
 * code to perform database access: <br>
 * <br>
 * <code>
	<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

	// update
	int rowsAffected = oscarSuperManager.update("appointmentDao", "delete",
		new Object[] {request.getParameter("appointment_no")});

	// select
	List<Map> resultList = oscarSuperManager.find("appointmentDao", "search_waitinglist",
		new Object[] {request.getParameter("demographic_no")});
	for (Map wl : resultList) {
		out.print(wl.get("name"));
	}
 * </code>
 * 
 * @author Eugene Petruhin
 * 
 */
public class OscarSuperManager {

	private Map<String, OscarSuperDao> oscarDaoMap = new TreeMap<String, OscarSuperDao>();

	private OscarSuperDao appointmentSuperDao;

	private OscarSuperDao receptionistSuperDao;

	private OscarSuperDao providerSuperDao;

	public void setAppointmentSuperDao(OscarSuperDao appointmentDao) {
		this.appointmentSuperDao = appointmentDao;
	}

	public void setReceptionistSuperDao(OscarSuperDao receptionistDao) {
		this.receptionistSuperDao = receptionistDao;
	}

	public void setProviderSuperDao(OscarSuperDao providerDao) {
		this.providerSuperDao = providerDao;
	}

	/**
	 * Enables every injected dao in this manager to be accessed by its name.<br>
	 * Don't forget to update this function then you add a dao field.
	 */
	public void init() {
		oscarDaoMap.put("appointmentDao", appointmentSuperDao);
		oscarDaoMap.put("receptionistDao", receptionistSuperDao);
		oscarDaoMap.put("providerDao", providerSuperDao);

		// making sure all daos have been injected properly
		for (String daoName : oscarDaoMap.keySet()) {
			if (oscarDaoMap.get(daoName) == null) {
				throw new IllegalStateException(
						"Dao with specified name has not been injected into OscarSuperManager: " + daoName);
			}
		}
	}

	/**
	 * Directs a call to a specified dao object that executes a parameterized
	 * select query identified by a query name.<br>
	 * Returned collection item is an automatically populated Map.
	 * 
	 * @param daoName
	 *            dao class key
	 * @param queryName
	 *            sql query key
	 * @param params
	 *            sql query parameters
	 * @return List of Map objects created for each result set row
	 */
	@SuppressWarnings("unchecked")
	public List<Map> find(String daoName, String queryName, Object[] params) {
		return getDao(daoName).executeSelectQuery(queryName, params);
	}

	/**
	 * Directs a call to a specified dao object that executes a parameterized
	 * select query identified by a query name.<br>
	 * Returned collection item is a value object populated by a row mapper
	 * identified by the same query name.
	 * 
	 * @param daoName
	 *            dao class key
	 * @param queryName
	 *            sql query key
	 * @param params
	 *            sql query parameters
	 * @return List of value objects created for each result set row by a row
	 *         mapper
	 */
	@SuppressWarnings("unchecked")
	public List populate(String daoName, String queryName, Object[] params) {
		return getDao(daoName).executeRowMappedSelectQuery(queryName, params);
	}

	/**
	 * Directs a call to a specified dao object that executes a parameterized
	 * insert/update/delete query identified by a query name.<br>
	 * 
	 * @param daoName
	 *            dao class key
	 * @param queryName
	 *            sql query key
	 * @param params
	 *            sql query parameters
	 * @return number of affected rows
	 */
	public int update(String daoName, String queryName, Object[] params) {
		return getDao(daoName).executeUpdateQuery(queryName, params);
	}

	/**
	 * Makes sure a requested dao is found or reported missing.
	 * 
	 * @param daoName
	 *            dao name
	 * @return dao instance
	 */
	private OscarSuperDao getDao(String daoName) {
		OscarSuperDao oscarSuperDao = oscarDaoMap.get(daoName);
		if (oscarSuperDao != null) {
			return oscarSuperDao;
		}
		throw new IllegalArgumentException("OscarSuperManager contains no dao with specified name: " + daoName);
	}
}
