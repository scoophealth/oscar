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

//The main report object, data fields filled in from XML, managed by the ReportManager.java


package oscar.oscarReport.reportByTemplate;

import java.util.ArrayList;
import java.util.Map;

import org.oscarehr.util.MiscUtils;

import oscar.util.StringUtils;

/*
 * Created on December 19, 2006, 10:46 AM
 *@apavel (Paul)
 */

public class ReportObjectGeneric implements ReportObject {
    private String templateId = "";
    private String title = "";
    private String description = "";
    private String type = "";
    private int active;
    private String category;
    
    private ArrayList parameters = new ArrayList(0);
    
    private boolean sequence;
     
    
    public ReportObjectGeneric() {
    }
    
    public ReportObjectGeneric(String templateId, String title) {
        this.setTemplateId(templateId);
        this.setTitle(title);
    }
    
    public ReportObjectGeneric(String templateId, String title, String description) {
        this.setTemplateId(templateId);
        this.setTitle(title);
        this.setDescription(description);
    }
    
    public ReportObjectGeneric(String templateId, String title, String description, String category) {
        this(templateId,title,description);
        this.category = category;
    }
    
    
    public ReportObjectGeneric(String templateId, String title, String description, String type, ArrayList parameters) {
        this.setTemplateId(templateId);
        this.setTitle(title);
        this.setDescription(description);
        this.setType(type);
        this.setParameters(parameters);
    }
    
    public ReportObjectGeneric(String templateId, String title, String description, String type, ArrayList parameters, String category) {
       this(templateId,title,description,type,parameters);
       this.category = category;
    }
    
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public ArrayList getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList parameters) {
        this.parameters = parameters;
    }
    
    public String getPreparedSQL(Map parameters) {
        String sql = (new ReportManager()).getSQL(this.templateId);
        int cursor1 = 0;
        while ((cursor1 = sql.indexOf("{")) != -1) {
            int cursor2 = sql.indexOf("}", cursor1);
            String paramId = sql.substring(cursor1+1, cursor2);

            String[] substValues = (String[]) parameters.get(paramId);
            if (substValues == null) { //if type textlist or this param isn't in the request
                substValues = (String[]) parameters.get(paramId + ":list");
                if (substValues != null) {
                    substValues[0] = substValues[0].replaceAll(" ", "");
                    substValues = StringUtils.splitToStringArray(substValues[0], ",");
                } else if (parameters.get(paramId + ":check") != null) {
                    substValues = new String[0];
                } else return "";
            }
            if (substValues.length == 1) //if one valuemnmmnm
                sql = sql.substring(0, cursor1) + substValues[0] + sql.substring(cursor2+1);
            else { //if multiple values
                //DynamicElement curelement = getDynamicElement(dynamicElementId);
                if (cursor1 != 0 && (sql.charAt(cursor1-1) == '\'' || sql.charAt(cursor1-1) == '\"')) {
                    sql = sql.substring(0, cursor1) + StringUtils.join(substValues, sql.charAt(cursor1-1) + "," + sql.charAt(cursor1-1)) + sql.substring(cursor2+1);
                } else {
                    sql = sql.substring(0, cursor1) + StringUtils.join(substValues, ",") + sql.substring(cursor2+1);
                }
                
            }
        }
        MiscUtils.getLogger().debug("<REPORT BY TEMPLATE> SQL: " + sql);
        return sql;
    }
    
    public String getPreparedSQL(int sequenceNo, Map parameters) {
        String sql = (new ReportManager()).getSQL(this.templateId);
        
        String parts[] = sql.split(";");
        
        if(parts.length <= sequenceNo) {
        	return null;
        }
        sql = parts[sequenceNo];
        
        
        int cursor1 = 0;
        while ((cursor1 = sql.indexOf("{")) != -1) {
            int cursor2 = sql.indexOf("}", cursor1);
            String paramId = sql.substring(cursor1+1, cursor2);

            String[] substValues = (String[]) parameters.get(paramId);
            if (substValues == null) { //if type textlist or this param isn't in the request
                substValues = (String[]) parameters.get(paramId + ":list");
                if (substValues != null) {
                    substValues[0] = substValues[0].replaceAll(" ", "");
                    substValues = StringUtils.splitToStringArray(substValues[0], ",");
                } else if (parameters.get(paramId + ":check") != null) {
                    substValues = new String[0];
                } else return "";
            }
            if (substValues.length == 1) //if one valuemnmmnm
                sql = sql.substring(0, cursor1) + substValues[0] + sql.substring(cursor2+1);
            else { //if multiple values
                //DynamicElement curelement = getDynamicElement(dynamicElementId);
                if (cursor1 != 0 && (sql.charAt(cursor1-1) == '\'' || sql.charAt(cursor1-1) == '\"')) {
                    sql = sql.substring(0, cursor1) + StringUtils.join(substValues, sql.charAt(cursor1-1) + "," + sql.charAt(cursor1-1)) + sql.substring(cursor2+1);
                } else {
                    sql = sql.substring(0, cursor1) + StringUtils.join(substValues, ",") + sql.substring(cursor2+1);
                }
                
            }
        }
        MiscUtils.getLogger().debug("<REPORT BY TEMPLATE> SQL: " + sql);
        return sql;
    }
    
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

	public boolean isSequence() {
		return sequence;
	}

	public void setSequence(boolean sequence) {
		this.sequence = sequence;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
}

