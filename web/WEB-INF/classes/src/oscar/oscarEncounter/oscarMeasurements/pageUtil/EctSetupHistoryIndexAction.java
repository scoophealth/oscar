// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsDao;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public final class EctSetupHistoryIndexAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		EctSessionBean bean = (EctSessionBean) request.getSession()
				.getAttribute("EctSessionBean");
		request.getSession().setAttribute("EctSessionBean", bean);

		if (bean != null) {
			String demo = (String) bean.getDemographicNo();

			//modified for showing recently measurement 10 days data record
			String beforeDate = request.getParameter("beforeDate");
			String afterDate = request.getParameter("afterDate");
			String flag = request.getParameter("processFlag");
            //Calendar beginningOfTime = Calendar.getInstance();
            //beginningOfTime.add(Calendar.YEAR, -1000);
            //if (beforeDate == null) beforeDate = dateF.format(Calendar.getInstance().getTime());
            //if (afterDate == null) afterDate = dateF.format(beginningOfTime.getTime());
            //if (flag == null) flag = "next";
            if (beforeDate == null && afterDate == null && flag == null) {
                request.getSession().setAttribute("EctSessionBean", bean);

                if (bean!=null){
                    oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler hd = new oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler(demo);

                    HttpSession session = request.getSession();
                    session.setAttribute( "measurementsData", hd );
                }
                else{
                    System.out.println("cannot get the EctSessionBean");
                }
                return (mapping.findForward("continue"));
            }

			List<Date> dates = getDatesInRange(beforeDate, afterDate, flag,
					demo);
			HashMap<Date, HashMap> hd = getMeasurementDate(dates, demo);
			Iterator iter = hd.values().iterator();
			LinkedHashSet<String> types = new LinkedHashSet<String>();
			while (iter.hasNext()) {
				HashMap entry = (HashMap) iter.next();
				types.addAll(entry.keySet());
			}
			SortedMap<String,String> typePair=getMeasurementTypeDescriptions(demo);
			HttpSession session = request.getSession();
			session.setAttribute("measurementsDates", dates);
			session.setAttribute("measurementsTypes", typePair);
			session.setAttribute("measurementsData", hd);

		} else {
			System.out.println("cannot get the EctSessionBean");
		}
		return (mapping.findForward("newcontinue"));
	}

	public SortedMap<String,String> getMeasurementTypeDescriptions(String demo){
		MeasurementsDao measuremenDao = (MeasurementsDao) getAppContext()
		.getBean("measurementsDao");
		return measuremenDao.getPatientMeasurementTypeDescriptions(demo);
	}
	public HashMap<Date, HashMap> getMeasurementDate(List<Date> dates,
			String demo) {
		HashMap<Date, HashMap> hd = new HashMap();
		MeasurementsDao measuremenDao = (MeasurementsDao) getAppContext()
				.getBean("measurementsDao");
		for (Date ddate : dates) {
			HashMap<String, EctMeasurementsDataBean> mdata = measuremenDao
					.getMeasurementsByDate(ddate, demo);
			hd.put(ddate, mdata);
		}
		return hd;
	}

	public List<Date> getDatesInRange(String beforeDate, String afterDate,
			String flag, String demo) throws ParseException {

		List<Date> dates = null;
		SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MeasurementsDao measuremenDao = (MeasurementsDao) getAppContext()
				.getBean("measurementsDao");

		if (null != flag && null != beforeDate && "prev".equalsIgnoreCase(flag))
			dates = measuremenDao.getMeasurementsDatesByDateRange(dateF
					.parse(beforeDate), -1, demo);
		else if (null != flag && null != afterDate
				&& "next".equalsIgnoreCase(flag))
			dates = measuremenDao.getMeasurementsDatesByDateRange(dateF
					.parse(afterDate), 1, demo);
		else
			dates = measuremenDao
					.getMeasurementsDatesByDateRange(null, 0, demo);

		return dates;
	}

	private ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet()
				.getServletContext());
	}

}
