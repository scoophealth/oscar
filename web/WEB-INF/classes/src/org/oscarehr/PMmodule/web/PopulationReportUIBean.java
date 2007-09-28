/*
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.PMmodule.web;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.caisi.dao.IssueGroupDao;
import org.caisi.model.IssueGroup;
import org.caisi.model.Role;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.RoleDAO;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.web.PopulationReportDataObjects.EncounterTypeDataGrid;
import org.oscarehr.PMmodule.web.PopulationReportDataObjects.EncounterTypeDataRow;
import org.oscarehr.PMmodule.web.PopulationReportDataObjects.RoleDataGrid;
import org.oscarehr.common.dao.PopulationReportDAO;
import org.oscarehr.util.EncounterUtil;
import org.springframework.context.ApplicationContext;

public class PopulationReportUIBean {

    private ProgramDao programDao = null;
    private RoleDAO roleDAO = null;
    private IssueGroupDao issueGroupDao = null;
    private PopulationReportDAO populationReportDAO = null;

    private int programId = -1;
    private Date startDate = null;
    private Date endDate = null;

    public PopulationReportUIBean(ApplicationContext applicationContext, int programId, Date startDate, Date endDate) {

        this.programId = programId;
        this.startDate = startDate;
        this.endDate = endDate;

        programDao = (ProgramDao)applicationContext.getBean("programDao");
        roleDAO = (RoleDAO)applicationContext.getBean("roleDAO");
        issueGroupDao = (IssueGroupDao)applicationContext.getBean("issueGroupDao");
        populationReportDAO = (PopulationReportDAO)applicationContext.getBean("populationReportDAO");
    }

    private Set<IssueGroup> allIssueGroups = null;

    public Set<IssueGroup> getIssueGroups() {

        if (allIssueGroups == null) allIssueGroups = new TreeSet<IssueGroup>(issueGroupDao.findAll());
        return(allIssueGroups);
    }

    private Program program = null;

    public Program getProgram() {

        if (program == null) program = programDao.getProgram(programId);
        return(program);
    }

    private List<Role> allRoles = null;

    public List<Role> getRoles() {

        if (allRoles == null) allRoles = roleDAO.getRoles();
        return(allRoles);
    }

    public RoleDataGrid getRoleDataGrid() {

        RoleDataGrid roleDataGrid = new RoleDataGrid();

        for (Role role : getRoles()) {
            roleDataGrid.put(role, getEncounterTypeDataGrid(role));
        }

        return(roleDataGrid);
    }

    private EncounterTypeDataGrid getEncounterTypeDataGrid(Role role) {

        EncounterTypeDataGrid result = new EncounterTypeDataGrid();

        for (EncounterUtil.EncounterType encounterType : EncounterUtil.EncounterType.values()) {
            result.put(encounterType, getEncounterTypeDataRow(role, encounterType));
        }

        return(result);
    }

    private EncounterTypeDataRow getEncounterTypeDataRow(Role role, EncounterUtil.EncounterType encounterType) {

        EncounterTypeDataRow result = new EncounterTypeDataRow();

        Map<Integer, Integer> counts = populationReportDAO.getCaseManagementNoteCountGroupedByIssueGroup(programId, (int)role.getId().longValue(), encounterType, startDate, endDate);

        for (IssueGroup issueGroup : getIssueGroups()) {
            Integer count = counts.get(issueGroup.getId());
            result.put(issueGroup, (count != null?count:0));
        }

        return(result);
    }
}
