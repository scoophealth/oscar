<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

--%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.dao.Icd9Dao"%>
<%@page import="org.oscarehr.common.model.Icd9"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%

            Icd9Dao icd9dao = (Icd9Dao) SpringUtils.getBean("Icd9DAO");


            String query = request.getParameter("q");

            List<Icd9> Icd9List = icd9dao.getIcd9(query);

            if (Icd9List != null && Icd9List.size() > 0) {
                Iterator<Icd9> iterator = Icd9List.iterator();
                while (iterator.hasNext()) {
                    Icd9 icd9 = iterator.next();
                    String code = icd9.getIcd9();
                    String description = icd9.getDescription();
                    out.println(code + " --> " + description);
                }
            }
%>
