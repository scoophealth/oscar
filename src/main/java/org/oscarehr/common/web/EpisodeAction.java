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
package org.oscarehr.common.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.EpisodeDao;
import org.oscarehr.common.model.Episode;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.BeanUtils;

import oscar.OscarProperties;

public class EpisodeAction extends DispatchAction {

	private EpisodeDao episodeDao = SpringUtils.getBean(EpisodeDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	@Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return this.list(mapping, form, request, response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		Integer demographicNo = Integer.parseInt(request.getParameter("demographicNo"));
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", demographicNo)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		List<Episode> episodes = episodeDao.findAll(demographicNo);
		request.setAttribute("episodes",episodes);
		return mapping.findForward("list");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		String id = request.getParameter("episode.id");
		 
		if(id != null) {
			Episode e = episodeDao.find(Integer.valueOf(id));
			request.setAttribute("episode",e);
		}
		
		String[] codingSystems = OscarProperties.getInstance().getProperty("dxResearch_coding_sys","").split(",");
		List<String> cs = Arrays.asList(codingSystems);
		request.setAttribute("codingSystems",cs);
		request.setAttribute("demographicNo", request.getParameter("demographicNo"));
		return mapping.findForward("form");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		DynaActionForm dform = (DynaActionForm)form;
		Episode episode = (Episode)dform.get("episode");
		Integer id = null;
		try {
			id = Integer.parseInt(request.getParameter("episode.id"));
		} catch(NumberFormatException e) {/*empty*/}
		Episode e = null;
		if(id != null && id.intValue()>0) {
			e = episodeDao.find(Integer.valueOf(id));
		} else {
			e = new Episode();
		}
		BeanUtils.copyProperties(episode, e, new String[]{"id","lastUpdateTime","lastUpdateUser"});
		e.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", e.getDemographicNo())) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		if(id != null && id.intValue()>0) {
			episodeDao.merge(e);
		} else {
			episodeDao.persist(e);
		}
		request.setAttribute("parentAjaxId","episode");
		return mapping.findForward("success");
	}
}
