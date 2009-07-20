/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 *
 * Created on April 27, 2007, 4:24 PM
 *
 */

package org.oscarehr.phr.dao;

import java.util.List;

import org.oscarehr.phr.model.PHRAction;

/**
 *
 * @author Paul
 */
public interface PHRActionDAO {
    public void save(PHRAction action);
    public Integer saveAndGetId(PHRAction action);
    public void update(PHRAction action);
    public List<PHRAction> getQueuedActions(String providerNo);
    public PHRAction getActionById(String id);
    
    //actionType = -1 for all actions
    //@Deprecated
    public List<PHRAction> getPendingActionsByProvider(String classification, int actionType, String providerNo);
    //@Deprecated
    public List<PHRAction> getPendingActionsByProvider(int actionType, String providerNo);
    public List<PHRAction> getActionsByStatus(int status, String providerNo);
    public List<PHRAction> getActionsByStatus(int status, String providerNo, String classification);
    public List<PHRAction> getActionsByStatus(List<Integer> statuses, String providerNo, String classification);
    public void updatePhrIndexes(String classification, String oscarId, String providerNo, String newPhrIndex);
    public boolean isIndivoRegistered(String classification, String oscarId);
    public String getPhrIndex(String classification, String oscarId);
    public boolean ifActionsWithErrors(String providerNo);
}



    
    