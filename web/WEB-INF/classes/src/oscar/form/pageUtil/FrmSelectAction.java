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
package oscar.form.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;


public class FrmSelectAction extends Action {

	private static Logger logger=MiscUtils.getLogger();
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        FrmSelectForm frm = (FrmSelectForm) form;                
        request.getSession().setAttribute("FrmSelectForm", frm);
        
                                
        if(frm.getForward()!=null){
            try{
                DBHandler db = new DBHandler();                        

                EncounterFormDao encounterFormDao=(EncounterFormDao)SpringUtils.getBean("encounterFormDao");
                List<EncounterForm> encounterForms=encounterFormDao.findAll();
                
                if (frm.getForward().compareTo("add")==0) {
                    logger.debug("the add button is pressed");

                    int maxCurrentDisplayCount=0;
                    // find the largest current display number
                    for (EncounterForm encounterForm : encounterForms)
                    {
                    	maxCurrentDisplayCount=Math.max(maxCurrentDisplayCount, encounterForm.getDisplayOrder());
                    }

                    String[] selectedAddTypes = frm.getSelectedAddTypes();                    
                    if(selectedAddTypes != null){
                    	// for every item selected to add, find it in the form list, then change the display number to the next highest number
                        for(int i=0; i<selectedAddTypes.length; i++){
                        	for (EncounterForm encounterForm : encounterForms)
                        	{
                        		if (encounterForm.getFormName().equals(selectedAddTypes[i]))
                        		{
                        			maxCurrentDisplayCount++;
                        			encounterForm.setDisplayOrder(maxCurrentDisplayCount);
                        			encounterFormDao.merge(encounterForm);
                        		}
                        	}                        	
                        }
                    }
                }
                else if (frm.getForward().compareTo("delete")==0){
                    logger.debug("the delete button is pressed");
                    String[] selectedDeleteTypes = frm.getSelectedDeleteTypes();  
                    if(selectedDeleteTypes != null){
                    	
                    	// for every item selected to delete,
                    	// go through the forms list and set this form display to 0
                    	// and any other form with a greater number should be decremented by 1
                        for(int i=0; i<selectedDeleteTypes.length; i++){
                        	List<EncounterForm> tempForms=encounterFormDao.findByFormName(selectedDeleteTypes[i]);
                        	for(EncounterForm tempForm : tempForms)
                        	{
                        		int oldDisplayOrder=tempForm.getDisplayOrder();
	                        	for(EncounterForm encounterForm : encounterForms)
	                        	{
	                        		if (encounterForm.getDisplayOrder()>oldDisplayOrder)
	                        		{
	                        			encounterForm.setDisplayOrder(encounterForm.getDisplayOrder()-1);
	                        			encounterFormDao.merge(encounterForm);
	                        		}
	                        		else if (encounterForm.getDisplayOrder()>oldDisplayOrder)
	                        		{
	                        			encounterForm.setDisplayOrder(0);
	                        			encounterFormDao.merge(encounterForm);
	                        		}
	                        	}
                        	}
                        	
                        	
//                            logger.debug(selectedDeleteTypes[i]);
//                            String sql = "UPDATE encounterForm SET hidden ='0' WHERE form_name='" + selectedDeleteTypes[i] + "'";
//                            logger.debug(" sql statement "+sql);
//                            db.RunSQL(sql);                                
                        }
//                        String sql = "SELECT hidden FROM encounterForm WHERE hidden <> '0' ORDER BY hidden";
//                        ResultSet rs = db.GetSQL(sql);
//                        int i=1;
//                        while(rs.next()){
//                            sql = "UPDATE encounterForm SET hidden='"+i+"' WHERE hidden='"+db.getString(rs,"hidden")+"'";
//                            db.RunSQL(sql);
//                            i++;
//                        }
                        
                    }
                    
                    //need to update the order number under the hidden column!!!!!
                }
                else if (frm.getForward().compareTo("up")==0){
                    logger.debug("The Move UP button is pressed!");
                    String[] selectedMoveUpTypes = frm.getSelectedDeleteTypes();
                    if(selectedMoveUpTypes != null){
                        for(int i=0; i<selectedMoveUpTypes.length; i++){
                            String sql = "Select hidden from encounterForm where form_name ='"+selectedMoveUpTypes[i] + "'";                            
                            ResultSet rs = db.GetSQL(sql);
                            if(rs.next()){
                                int form_order = rs.getInt("hidden");
                                String upperOrder = Integer.toString(form_order - 1);
                                rs.close();
                                
                                if (form_order>1){
                                    sql = "Select form_name from encounterForm where hidden ='"+ upperOrder + "'";
                                    rs = db.GetSQL(sql);
                                    while (!rs.next()){
                                        upperOrder= Integer.toString(Integer.parseInt(upperOrder) - 1);
                                        if(form_order>1){
                                            sql = "Select form_name from encounterForm where hidden ='"+ upperOrder + "'";
                                            rs = db.GetSQL(sql);
                                        }
                                    }                                    
                                        String upperItem = db.getString(rs,"form_name");
                                        sql = "UPDATE encounterForm SET hidden ='" + form_order + "' WHERE form_name='" + upperItem + "'";
                                        db.RunSQL(sql);
                                        sql = "UPDATE encounterForm SET hidden ='" + upperOrder + "' WHERE form_name='" + selectedMoveUpTypes[i] + "'";
                                        db.RunSQL(sql);
                                        rs.close();                                    
                                }
                            }
                        }
                    }
                }
                
                else if (frm.getForward().compareTo("down")==0){
                    logger.debug("The Move DOWN button is pressed!");
                    String[] selectedMoveDownTypes = frm.getSelectedDeleteTypes();
                    if(selectedMoveDownTypes != null){
                        for(int i=selectedMoveDownTypes.length-1; i>=0; i--){
                            String sql = "Select hidden from encounterForm where form_name ='"+selectedMoveDownTypes[i] + "'";                            
                            ResultSet rs = db.GetSQL(sql);                            
                            if(rs.next()){
                                int form_order = rs.getInt("hidden");
                                String lowerOrder = Integer.toString(form_order + 1);
                                rs.close();
                                sql = "Select * from encounterForm where hidden <> '0'";
                                rs = db.GetSQL(sql);
                                rs.last();
                                int nbRows = rs.getRow();
                                rs.close();
                                
                                if (form_order<nbRows && form_order>0){
                                    logger.debug("form_order: " + form_order);                                    
                                    sql = "Select form_name from encounterForm where hidden ='"+ lowerOrder + "'";
                                    rs = db.GetSQL(sql);
                                    if(rs.next()){
                                        String lowerItem = db.getString(rs,"form_name");
                                        sql = "UPDATE encounterForm SET hidden ='" + form_order + "' WHERE form_name='" + lowerItem + "'";
                                        db.RunSQL(sql);
                                        sql = "UPDATE encounterForm SET hidden ='" + lowerOrder + "' WHERE form_name='" + selectedMoveDownTypes[i] + "'";
                                        db.RunSQL(sql);
                                    }
                                    rs.close();                                    
                                }
                            }
                        }
                    }
                }
            }
           
            catch(SQLException e)
            {
                logger.error("Error", e);
            }
     
        }                          
        
        return mapping.findForward("success");
    }
     
}
