<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="java.util.Date"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.common.model.UserProperty"%>
<%
	UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
   
	String providerNo = request.getParameter("provider_no");
    String sessionDateStr = request.getParameter("date");
    
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date sessionDate = null;
    
    try {
    	sessionDate = formatter.parse(sessionDateStr);
    }catch(ParseException e) {
    	MiscUtils.getLogger().error("Error parsing date",e);
    }
    
    //series level attributes
    String seriesName = request.getParameter("seriesName");
    String seriesSite = request.getParameter("seriesSite");
    String seriesNote = request.getParameter("seriesNote");
    String completed = request.getParameter("completed");
    
    saveOrUpdateProperty(providerNo,seriesName,"seriesName");
    saveOrUpdateProperty(providerNo,seriesSite,"seriesSite");
    saveOrUpdateProperty(providerNo,seriesNote,"seriesNote");
	saveOrUpdateCompleted(providerNo,completed);
    
    
    //session level attributes
    String facilitator = request.getParameter("facilitator");
    String facilitator2 = request.getParameter("facilitator2");
    String sessionSite = request.getParameter("sessionSite");
    
    saveOrUpdateProperty(providerNo,facilitator,"session_" + sessionDateStr + "_facilitator");
    saveOrUpdateProperty(providerNo,facilitator2,"session_" + sessionDateStr + "_facilitator2");
    saveOrUpdateProperty(providerNo,sessionSite,"session_" + sessionDateStr + "_site");
     
    deleteExistingTopics(providerNo,sessionDateStr);
    saveOrUpdateMaxTopics(providerNo,request.getParameter("topics_num"),sessionDateStr);
    int maxTopic = Integer.parseInt(request.getParameter("topics_num"));
    for(int x=1;x<=maxTopic;x++) {
    	String id = request.getParameter("topic_"+x+".id");
  		if(id != null) {
  			String text = request.getParameter("topic_"+x+".text");
        	
  			UserProperty up = new UserProperty();
  			if(id.length()>0 && Integer.parseInt(id)>0) {
        		up = userPropertyDao.find(Integer.parseInt(id));
        	} else {
        		up.setProviderNo(providerNo);
        		up.setName("session_" + sessionDateStr + "_topic" + x);
        	}
        	up.setValue(text);
  			
 			if(up.getId() == null)
 				userPropertyDao.persist(up);
 			else
 				userPropertyDao.merge(up);
 		}
	}

   	//handle removes
   	String[] ids = request.getParameterValues("topic.delete");
   	if(ids != null) {
   		for(String id:ids) {
   			if(id.length()>0) {
       			int topicId = Integer.parseInt(id);
       			userPropertyDao.remove(topicId);    				
   			}
   		}
   	}
%>

<%!

	void saveOrUpdateProperty(String providerNo, String value, String propertyName) {
		UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
		UserProperty up = userPropertyDao.getProp(providerNo, propertyName);
		if(up == null) {
			up = new UserProperty();
			up.setName(propertyName);
			up.setProviderNo(providerNo);
		}
		up.setValue(value);
		userPropertyDao.saveEntity(up);
	}

	void saveOrUpdateCompleted(String providerNo, String completed) {
		UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
		UserProperty up = userPropertyDao.getProp(providerNo, "completed");
		if(up == null) {
			up = new UserProperty();
			up.setName("completed");
			up.setProviderNo(providerNo);
		}
		up.setValue(completed != null && "on".equals(completed)? "true" :"false");
		
		userPropertyDao.saveEntity(up);
	}
	
	void saveOrUpdateMaxTopics(String providerNo, String value, String sessionDateStr) {
		UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
		UserProperty up = userPropertyDao.getProp(providerNo, "session_" + sessionDateStr + "_num_topics");
		if(up == null) {
			up = new UserProperty();
			up.setName("session_" + sessionDateStr + "_num_topics");
			up.setProviderNo(providerNo);
		}
		up.setValue(value);
		
		userPropertyDao.saveEntity(up);
	}
	
	void deleteExistingTopics(String providerNo, String sessionDateStr) {
		UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
		   
		UserProperty up = userPropertyDao.getProp(providerNo, "session_" + sessionDateStr  +"_num_topics");
		if(up != null) {
			int val = Integer.parseInt(up.getValue());
			
			for(int x=1;x<=val;x++) {
				UserProperty tmp = userPropertyDao.getProp(providerNo,"session_" + sessionDateStr + "_topic" + x);
				if(tmp != null) {
					userPropertyDao.remove(tmp.getId());
				}
			}
		}
	}
%>

<html>
<head>
<script>
window.close();
</script>
</head>
<body></body>
</html>