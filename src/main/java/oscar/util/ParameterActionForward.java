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


package oscar.util;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.struts.action.ActionForward;


public final class ParameterActionForward extends ActionForward {

    private static final String questionMark = "?";
    private static final String ampersand = "&";
    private static final String equals = "=";
    private HashMap parameters = new HashMap();
    private String path;

    
    public ParameterActionForward(ActionForward forward) {
        setName(forward.getName());
        setPath(forward.getPath());
        setRedirect(forward.getRedirect());

    }

    
    public void setPath(String path) {
        this.path = path;

    }


    
    public String getPath() {

        StringBuilder sb = new StringBuilder();

        sb.append(path);

        boolean firstTimeThrough = true;

        if (parameters != null && !parameters.isEmpty()) {
            sb.append(questionMark);

            Iterator it = parameters.keySet().iterator();

            while (it.hasNext()) {

                String paramName = (String)it.next();
                String paramValue = (String)parameters.get(paramName);

                if (firstTimeThrough) {
                    firstTimeThrough = false;
                } else {
                    sb.append(ampersand);
                }

                sb.append(paramName);
                sb.append(equals);
                sb.append(paramValue);

            }
        }

        return sb.toString();
    }


   
    public void addParameter(String paramName, Object paramValue) {
        addParameter(
            paramName,
            paramValue.toString());

    }

    public void addParameter(String paramName, String paramValue) {
        parameters.put(paramName, paramValue);

    }
}
