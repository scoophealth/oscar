/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.caisi.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.caisi.model.CustomFilter;
import org.caisi.model.Tickler;
import org.caisi.model.TicklerComment;
import org.caisi.model.TicklerUpdate;
import org.oscarehr.common.model.Provider;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


/*
 * Updated by Eugene Petruhin on 16 dec 2008 while fixing #2422864 & #2317933 & #2379840
 * Updated by Eugene Petruhin on 12/18/2008: don't save empty comment and skip updating status & assignee if they are the same 
 */
public class TicklerDAO extends HibernateDaoSupport {

    public void saveTickler(Tickler tickler) {
        getHibernateTemplate().saveOrUpdate(tickler);
    }

    public Tickler getTickler(Long id) {
        return (Tickler)getHibernateTemplate().get(Tickler.class, id);
    }

    public void addComment(Long tickler_id, String provider, String message) {
        Tickler tickler = this.getTickler(tickler_id);
        if (tickler != null && message != null && !"".equals(message)) {
            TicklerComment comment = new TicklerComment();
            comment.setTickler_no(tickler_id.longValue());
            comment.setUpdate_date(new Date());
            comment.setProviderNo(provider);
            comment.setMessage(message);
            tickler.getComments().add(comment);
            this.saveTickler(tickler);
        }
    }

    public void reassign(Long tickler_id, String provider, String task_assigned_to) {
        Tickler tickler = this.getTickler(tickler_id);
        if (tickler != null && !task_assigned_to.equals(tickler.getTask_assigned_to())) {
            String message;
            String former_assignee = tickler.getAssignee().getFormattedName();
            String current_assignee;
            tickler.setTask_assigned_to(task_assigned_to);
            TicklerComment comment = new TicklerComment();
            comment.setTickler_no(tickler_id.longValue());
            comment.setUpdate_date(new Date());
            comment.setProviderNo(provider);
            current_assignee = ((Provider)(getHibernateTemplate().find("from Provider p where p.ProviderNo = ?", task_assigned_to)).get(0)).getFormattedName();
            message = "RE-ASSIGNMENT RECORD: [Tickler \"" + tickler.getDemographic().getFormattedName() + "\" was reassigned from \"" + former_assignee + "\"  to \"" + current_assignee + "\"]";
            comment.setMessage(message);
            tickler.getComments().add(comment);
            this.saveTickler(tickler);
        }
    }

/*
 * Eugene Petruhin, 12/16/2008: getTicklers() entry without any arguments is no longer available due to security and performance concerns.

    public List<Tickler> getTicklers() {
        return (List)getHibernateTemplate().find("from Tickler");
    }

*/
    
    public List<Tickler> getTicklers(CustomFilter filter) {
        String tickler_date_order = filter.getSort_order();
        String query = "from Tickler t where t.service_date >= ? and t.service_date <= ? ";
        ArrayList paramList = new ArrayList();
        query = getTicklerQueryString(query,  paramList,  filter);
        Object params[] = paramList.toArray(new Object[paramList.size()]);
        return (List)getHibernateTemplate().find(query + "order by t.service_date " + tickler_date_order, params);
    }
    
    public int getActiveTicklerCount(String providerNo){
        ArrayList paramList = new ArrayList();
        String query = "select count(*) from Tickler t where t.status = 'A' and t.service_date <= ? and (t.task_assigned_to  = '"+ providerNo + "' or t.task_assigned_to='All Providers')";
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        paramList.add(currentDate.getTime());
        //paramList.add(new Date());
        Object params[] = paramList.toArray(new Object[paramList.size()]);
        Long count = (Long) getHibernateTemplate().find(query ,params).get(0);
        return count.intValue();
 }
    
    public int getNumTicklers(CustomFilter filter){
            ArrayList paramList = new ArrayList();
            String query = "select count(*) from Tickler t where t.service_date >= ? and t.service_date <= ? ";   
            query = getTicklerQueryString(query,  paramList,  filter);
            Object params[] = paramList.toArray(new Object[paramList.size()]);
            listParams(params);
            Long count = (Long) getHibernateTemplate().find(query ,params).get(0);
            return count.intValue();
     }
    
    private String getTicklerQueryString(String query, List paramList, CustomFilter filter){
            boolean includeProviderClause = true;
            boolean includeAssigneeClause = true;
            boolean includeStatusClause = true;
            boolean includeClientClause = true;
            boolean includeDemographicClause = true;
            boolean includeProgramClause = true;
            
            if (filter.getStartDate() == null || filter.getStartDate().length() == 0) {
                filter.setStartDate("1900-01-01");
            }
            if (filter.getEndDate() == null || filter.getEndDate().length() == 0) {
                filter.setEndDate("8888-12-31");
            }
            
            if( filter.getProgramId() == null || "".equals(filter.getProgramId()) || filter.getProgramId().equals("All Programs")) {
            		includeProgramClause = false;
            }
            if (filter.getProvider() == null || filter.getProvider().equals("All Providers")) {
                    includeProviderClause=false;
            }
            if (filter.getAssignee() == null || filter.getAssignee().equals("All Providers")) {
                    includeAssigneeClause=false;
            }
            if (filter.getClient() == null || filter.getClient().equals("All Clients")) {
                    includeClientClause=false;
            }
            if (filter.getDemographic_no()==null||filter.getDemographic_no().equals("")||filter.getDemographic_no().equalsIgnoreCase("All Clients")) {
                    includeDemographicClause=false;
            }

            if (filter.getStatus().equals("") || filter.getStatus().equals("Z")) {
                    includeStatusClause = false;
            }

            
            paramList.add(filter.getStart_date());
            paramList.add(new Date(filter.getEnd_date().getTime()+DateUtils.MILLIS_PER_DAY));

            //TODO: IN clause
            if(includeProviderClause) {
                    query = query + " and t.creator IN (";
                    Set pset = filter.getProviders();
                    Provider[] providers = (Provider[])pset.toArray(new Provider[pset.size()]);
                    for(int x=0;x<providers.length;x++) {
                            if(x>0) {
                                    query += ",";
                            }
                            query += "?";
                            paramList.add(providers[x].getProviderNo());
                    }
                    query += ")";
            }

            //TODO: IN clause
            if(includeAssigneeClause) {
                    query = query + " and t.task_assigned_to IN (";
                    Set pset = filter.getAssignees();
                    Provider[] providers = (Provider[])pset.toArray(new Provider[pset.size()]);
                    for(int x=0;x<providers.length;x++) {
                            if(x>0) {
                                    query += ",";
                            }
                            query += "?";
                            paramList.add(providers[x].getProviderNo());
                    }
                    query += ")";
            }
            
            if(includeProgramClause) {
            		query = query + " and t.program_id = ?" ;
            		paramList.add(Integer.valueOf(filter.getProgramId()));
            }
            if(includeStatusClause) {
                    query = query + " and t.status = ?";
                    paramList.add(String.valueOf(filter.getStatus()));
            }
            if(includeClientClause) {
                    query = query + "and t.demographic_no = ?";
                    paramList.add(filter.getClient());
            }
            if(includeDemographicClause) {
                    query = query + "and t.demographic_no = ?";
                    paramList.add(filter.getDemographic_no());
            }
            return query;
    }
   
     
    


    private void listParams(Object params[]){
        for(Object obj: params){
            System.out.print("Object Type:"+obj.getClass().getName());
            System.out.print(" "+obj.toString());
            System.out.println("");
        }
        
    }
    
    
    private void updateTickler(Long tickler_id, String provider, char status) {
        Tickler tickler = this.getTickler(tickler_id);
        if (tickler != null && status != tickler.getStatus()) {
            tickler.setStatus(status);
            TicklerUpdate update = new TicklerUpdate();
            update.setProviderNo(provider);
            update.setStatus(status);
            update.setTickler_no(tickler_id.longValue());
            update.setUpdate_date(new Date());
            tickler.getUpdates().add(update);
            this.saveTickler(tickler);
        }
    }

    public void completeTickler(Long tickler_id, String provider) {
        updateTickler(tickler_id, provider, 'C');
    }

    public void deleteTickler(Long tickler_id, String provider) {
        updateTickler(tickler_id, provider, 'D');
    }

    public void activateTickler(Long tickler_id, String provider) {
        updateTickler(tickler_id, provider, 'A');
    }

}
