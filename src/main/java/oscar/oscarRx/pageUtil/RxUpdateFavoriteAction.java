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


package oscar.oscarRx.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;


public final class RxUpdateFavoriteAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);


    public ActionForward unspecified(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "u", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}


            // Setup variables
            RxUpdateFavoriteForm frm = (RxUpdateFavoriteForm)form;
            int favId = Integer.parseInt(frm.getFavoriteId());

            RxPrescriptionData.Favorite fav = new RxPrescriptionData().getFavorite(favId);

            fav.setFavoriteName(frm.getFavoriteName());
            fav.setCustomName(frm.getCustomName());
            fav.setTakeMin(RxUtil.StringToFloat(frm.getTakeMin()));
            fav.setTakeMax(RxUtil.StringToFloat(frm.getTakeMax()));
            fav.setFrequencyCode(frm.getFrequencyCode());
            fav.setDuration(frm.getDuration());
            fav.setDurationUnit(frm.getDurationUnit());
            fav.setQuantity(frm.getQuantity());
            fav.setDispensingUnits(frm.getDispensingUnits());
            fav.setRepeat(Integer.parseInt(frm.getRepeat()));
            fav.setNosubs(frm.getNosubs());
            fav.setPrn(frm.getPrn());
            fav.setSpecial(frm.getSpecial());
            fav.setCustomInstr(frm.getCustomInstr());

            fav.Save();

            return (mapping.findForward("success"));
    }

    public ActionForward ajaxEditFavorite(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	{
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "u", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
		
            // Setup variables
            int favId = Integer.parseInt(request.getParameter("favoriteId"));

            RxPrescriptionData.Favorite fav = new RxPrescriptionData().getFavorite(favId);
            String favName=request.getParameter("favoriteName");
            String customName=request.getParameter("customName");
            String takeMin=request.getParameter("takeMin");
            String takeMax=request.getParameter("takeMax");
            String freqCode=request.getParameter("frequencyCode");
            String duration=request.getParameter("duration");
            String durationUnit=request.getParameter("durationUnit");
            String quantity=request.getParameter("quantity");
            String dispensingUnits=request.getParameter("dispensingUnits");
            String repeat=request.getParameter("repeat");
            String noSubs=request.getParameter("nosubs");
            String prn=request.getParameter("prn");
            String special=request.getParameter("special");
            String customInstr=request.getParameter("customInstr");
            fav.setFavoriteName(favName);
            fav.setCustomName(customName);
            fav.setTakeMin(RxUtil.StringToFloat(takeMin));
            fav.setTakeMax(RxUtil.StringToFloat(takeMax));
            fav.setFrequencyCode(freqCode);
            fav.setDuration(duration);
            fav.setDurationUnit(durationUnit);
            fav.setQuantity(quantity);
            fav.setDispensingUnits(dispensingUnits);
            fav.setRepeat(Integer.parseInt(repeat));
            if(noSubs.equalsIgnoreCase("true"))
                fav.setNosubs(true);
            else
                fav.setNosubs(false);
            if(prn.equalsIgnoreCase("true"))
                fav.setPrn(true);
            else
                fav.setPrn(false);
            fav.setSpecial(special);
            if(customInstr.equalsIgnoreCase("true"))
                fav.setCustomInstr(true);
            else
                fav.setCustomInstr(false);

            if(request.getParameter("dispenseInternal") != null && request.getParameter("dispenseInternal").length()>0) { 
            	fav.setDispenseInternal(true);
            }
            
            fav.Save();

            return null;
    }
}
