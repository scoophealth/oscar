/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.Map.Entry;

import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.dao.PopulationReportDao;
import org.oscarehr.common.model.Mortalities;
import org.oscarehr.common.model.ReportStatistic;
import org.oscarehr.common.model.ShelterPopulation;
import org.oscarehr.common.model.ShelterUsage;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PopulationReportManager {

    private static final int ONE_YEAR = 1;
    private static final int FOUR_YEARS = 4;

    private PopulationReportDao populationReportDao;
    private IssueDAO issueDAO;

    public void setPopulationReportDao(PopulationReportDao populationReportDao) {
        this.populationReportDao = populationReportDao;
    }

    public void setIssueDAO(IssueDAO issueDAO) {
        this.issueDAO = issueDAO;
    }

    public ShelterPopulation getShelterPopulation() {
        int pastYear = populationReportDao.getCurrentAndHistoricalPopulationSize(ONE_YEAR);
        int current = populationReportDao.getCurrentPopulationSize();

        return new ShelterPopulation(pastYear, current);
    }

    public ShelterUsage getShelterUsage() {
        int[] usages = populationReportDao.getUsages(FOUR_YEARS);

        return new ShelterUsage(usages[PopulationReportDao.LOW], usages[PopulationReportDao.MEDIUM], usages[PopulationReportDao.HIGH]);
    }

    public Mortalities getMortalities() {
        int count = populationReportDao.getMortalities(ONE_YEAR);
        int size = populationReportDao.getCurrentAndHistoricalPopulationSize(ONE_YEAR);
        
        if(size==0) {
        	//denominator=0 fix
        	return null;
        }
        
        return new Mortalities(count, size);
    }

    public Map<String, ReportStatistic> getMajorMedicalConditions() {
        Map<String, ReportStatistic> prevalences = new LinkedHashMap<String, ReportStatistic>();

        int populationSize = populationReportDao.getCurrentPopulationSize();

        for (Entry<String, SortedSet<String>> e : PopulationReportCodes.getMajorMedicalConditions().entrySet()) {
            prevalences.put(e.getKey(), new ReportStatistic(populationReportDao.getPrevalence(e.getValue()), populationSize));
        }

        return prevalences;
    }

    public Map<String, ReportStatistic> getMajorMentalIllnesses() {
        Map<String, ReportStatistic> prevalences = new LinkedHashMap<String, ReportStatistic>();

        int populationSize = populationReportDao.getCurrentPopulationSize();

        for (Entry<String, SortedSet<String>> e : PopulationReportCodes.getMajorMentalIllness().entrySet()) {
            prevalences.put(e.getKey(), new ReportStatistic(populationReportDao.getPrevalence(e.getValue()), populationSize));
        }

        return prevalences;
    }

    public Map<String, ReportStatistic> getSeriousMedicalConditions() {
        Map<String, ReportStatistic> incidences = new LinkedHashMap<String, ReportStatistic>();

        int populationSize = populationReportDao.getCurrentPopulationSize();

        for (Entry<String, SortedSet<String>> e : PopulationReportCodes.getSeriousMedicalConditions().entrySet()) {
            incidences.put(e.getKey(), new ReportStatistic(populationReportDao.getIncidence(e.getValue()), populationSize));
        }

        return incidences;
    }

    public Map<String, Map<String, String>> getCategoryCodeDescriptions() {
        Map<String, Map<String, String>> categoryCodeDescription = new LinkedHashMap<String, Map<String, String>>();

        for (Entry<String, SortedSet<String>> e : PopulationReportCodes.getAllCodes().entrySet()) {
            String category = e.getKey();
            SortedSet<String> codes = e.getValue();

            Map<String, String> codeDescriptions = new LinkedHashMap<String, String>();

            for (String code : codes) {
                Issue issue = issueDAO.findIssueByCode(code);
                String description = issue != null?issue.getDescription():"N/A";

                codeDescriptions.put(code, description);
            }

            categoryCodeDescription.put(category, codeDescriptions);
        }

        return categoryCodeDescription;
    }

}
