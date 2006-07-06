package org.caisi.service.impl;

import org.caisi.dao.EChartDAO;
import org.caisi.model.EChart;
import org.caisi.service.EChartManager;

/**
 * Implements the EChartManager interface 
 * @author Marc Dumontier <a href="mailto:marc@mdumontier.com">marc@mdumontier.com</a>
 *
 */
public class EChartManagerImpl implements EChartManager {
	
	private EChartDAO chartDAO = null;
	
	public void setChartDAO(EChartDAO chartDAO) {
		this.chartDAO = chartDAO;
	}
	
	
	public void saveEncounter(EChart chart) {
		chartDAO.saveEncounter(chart);
	}
	
	public EChart getLatestChart(String demographicNo) {
		return chartDAO.getLatestChart(Integer.valueOf(demographicNo).intValue());
	}
}
