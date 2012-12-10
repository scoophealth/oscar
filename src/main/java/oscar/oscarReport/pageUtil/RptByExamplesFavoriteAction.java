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

package oscar.oscarReport.pageUtil;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ReportByExamplesFavoriteDao;
import org.oscarehr.common.model.ReportByExamplesFavorite;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarReport.bean.RptByExampleQueryBeanHandler;

public class RptByExamplesFavoriteAction extends Action {

	private ReportByExamplesFavoriteDao dao = SpringUtils.getBean(ReportByExamplesFavoriteDao.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RptByExamplesFavoriteForm frm = (RptByExamplesFavoriteForm) form;
		String providerNo = (String) request.getSession().getAttribute("user");
		if (frm.getNewQuery() != null) {
			if (frm.getNewQuery().compareTo("") != 0) {
				frm.setQuery(frm.getNewQuery());
				if (frm.getNewName() != null) frm.setFavoriteName(frm.getNewName());
				else {
					ReportByExamplesFavoriteDao dao = SpringUtils.getBean(ReportByExamplesFavoriteDao.class);
					for (ReportByExamplesFavorite f : dao.findByQuery(frm.getNewQuery())) {
						frm.setFavoriteName(f.getName());
					}
				}
				return mapping.findForward("edit");
			} else if (frm.getToDelete() != null) {
				if (frm.getToDelete().compareTo("true") == 0) {
					deleteQuery(frm.getId());
				}
			}
		} else {
			String favoriteName = frm.getFavoriteName();
			String query = frm.getQuery();

			String queryWithEscapeChar = StringEscapeUtils.escapeSql(query);///queryWithEscapeChar);
			MiscUtils.getLogger().debug("escapeSql: " + queryWithEscapeChar);
			write2Database(providerNo, favoriteName, queryWithEscapeChar);
		}
		RptByExampleQueryBeanHandler hd = new RptByExampleQueryBeanHandler(providerNo);
		request.setAttribute("allFavorites", hd);
		return mapping.findForward("success");
	}

	public void write2Database(String providerNo, String favoriteName, String query) {
		if (query == null || query.compareTo("") == 0) {
			return;
		}

		MiscUtils.getLogger().debug("Fav " + favoriteName + " query " + query);

		ReportByExamplesFavoriteDao dao = SpringUtils.getBean(ReportByExamplesFavoriteDao.class);
		List<ReportByExamplesFavorite> favorites = dao.findByEverything(providerNo, favoriteName, query);
		if (favorites.isEmpty()) {
			ReportByExamplesFavorite r = new ReportByExamplesFavorite();
			r.setProviderNo(providerNo);
			r.setName(favoriteName);
			r.setQuery(query);
			dao.persist(r);
		} else {
			ReportByExamplesFavorite r = favorites.get(0);
			if (r != null) {
				r.setName(favoriteName);
				r.setQuery(query);
				dao.merge(r);
			}
		}

	}

	public void deleteQuery(String id) {
		dao.remove(Integer.parseInt(id));
	}
}
