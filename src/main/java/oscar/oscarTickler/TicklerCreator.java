/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarTickler;

import java.util.Date;
import java.util.List;

import org.caisi.dao.TicklerDAO;
import org.caisi.model.CustomFilter;
import org.caisi.model.Tickler;
import org.oscarehr.util.SpringUtils;


public class TicklerCreator {
	TicklerDAO dao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
	
	
  public TicklerCreator() {
  }

  /**
   * createTickler
   *
   * @param demoNo the demographic no
   * @param provNo the provider no
   * @param message the tickler message
   */
  public void createTickler(String demoNo, String provNo, String message) {
    if (!ticklerExists(demoNo, message)) {
    	Tickler t = new Tickler();
    	t.setDemographic_no(demoNo);
    	t.setMessage(message);
    	t.setStatus('A');
    	t.setUpdate_date(new Date());
    	t.setService_date(new Date());
    	t.setCreator(provNo);
    	t.setPriority("4");
    	t.setTask_assigned_to(provNo);
    	dao.saveTickler(t);
    	

    }
  }
  
  
  public void createTickler(String demoNo, String provNo, String message, String assignedTo) {
	   Tickler t = new Tickler();
    	t.setDemographic_no(demoNo);
    	t.setMessage(message);
    	t.setStatus('A');
    	t.setUpdate_date(new Date());
    	t.setService_date(new Date());
    	t.setCreator(provNo);
    	t.setPriority("4");
    	t.setTask_assigned_to(assignedTo);
    	dao.saveTickler(t);
	  }
  
 
  /**
   * Returns true if a tickler with the specified parameters exists
   *
   * @param demoNo String
   * @param message String
   * @return boolean
   */
  public boolean ticklerExists(String demoNo, String message) {
	  org.oscarehr.casemgmt.dao.TicklerDAO dao = SpringUtils.getBean(org.oscarehr.casemgmt.dao.TicklerDAO.class);
	  CustomFilter filter=  new CustomFilter();
	  filter.setDemographic_no(demoNo);
	  filter.setMessage(message);
	  filter.setStatus("A");
	  List<Tickler> ticklers = dao.getTicklers(filter);
	  return !ticklers.isEmpty();
  }

  /**
   * resolveTicklers
   *
   * @param cdmPatientNos Vector (strings)
   * @param remString String
   */
  public void resolveTicklers(List cdmPatientNos, String remString) {
	  dao.deleteTicklers(cdmPatientNos, remString);  
  }
}
