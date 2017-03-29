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
<%@ include file="/taglibs.jsp" %>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
	    <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <title>Edit Intake</title>
        <script type="text/javascript">
            function add(id,nodeTemplateId,parentId,pos,psize){
               var eURL = "AddToIntake.jsp?id="+id+"&node="+nodeTemplateId+"&parentId="+parentId+"&pos="+pos+"&pSize="+psize;
               popup('525','375',eURL,'intakeAdd');
            }
            
            function del(id, pid){
	       var ans = confirm("Delete this node (and all its children) ?");
	       if (ans) {
		   var eURL = "DeleteIntake.jsp?id="+id+"&parentId="+pid;
		   popup('200','300',eURL,'intakeDel');
	       }
            }
            
            function editlabel(id, nid){
               var eURL = "EditLabel.jsp?id="+id+"&nid="+nid;
               popup('200','300',eURL,'lbledit');
            }
            
            function reorder(id){
               var eURL = "ReorderNode.jsp?id="+id;
               popup('300','400',eURL,'rodrnode');
            }
            
            function saveform(form_type){
               var eURL = "SaveForm.jsp";
		if(form_type > 0) {
			eURL +="?published=1&form_type=" + form_type;
		}
              
               popup('200','300',eURL,'sveform');
            }

            function exportform() {
            	var eURL = "ExportForm.jsp";
            	popup('200','300',eURL,'exportform');
            }

            function importform() {
            	var eURL = "ImportForm.jsp";
            	popup('300','400',eURL,'importform');
            }
	    
	    	function resetform(){
               var eURL = "ResetForm.jsp";
               popup('200','300',eURL,'rstform');
            }

		    function deleteform() {
				var eURL = "DeleteForm.jsp";	
				popup('200','300',eURL,'delform');	
	    	}
	    
	    </script>
        <style type="text/css">
            @import "<html:rewrite page="/css/genericIntake.css"/>";
        </style>
        <script language="javascript" type="text/javascript" src="<html:rewrite page="/share/javascript/Oscar.js"/>" ></script>
        <script type="text/javascript">
        <!--
        var djConfig = {
            isDebug: false,
            parseWidgets: false,
            searchIds: ["layoutContainer", "topPane", "clientPane", "bottomPane", "clientTable", "admissionsTable"]
        };

        var programMaleOnly =[];
        var programFemaleOnly =[];
        var programTransgenderOnly =[];

	

        // -->

        function openSurvey(ctl) {
            var formId = ctl.options[ctl.selectedIndex].value;
            if (formId == 0) {
                return;
            }
            var id = document.getElementById('formInstanceId').value;
            var url = '<html:rewrite action="/PMmodule/Forms/SurveyExecute.do"/>?method=survey&type=provider&formId=' + formId + '&formInstanceId=' + id + '&clientId=' + 10;
            ctl.selectedIndex = 0;
            popupPage(url);
        }

        function popupPage(varpage) {
            var page = "" + varpage;
            windowprops = "height=600,width=700,location=no,"
                    + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
            window.open(page, "", windowprops);
        }
        </script>
        
        <script type="text/javascript" src="<html:rewrite page="/dojoAjax/dojo.js"/>"></script>
        <script type="text/javascript" src="<html:rewrite page="/js/AlphaTextBox.js"/>"></script>
        <script type="text/javascript">
        <!--
        dojo.require("dojo.widget.*");
        dojo.require("dojo.validate.*");
        // -->
        </script>
        <script type="text/javascript" src="<html:rewrite page="/js/genericIntake.js" />"></script>
        <script type="text/javascript" src="<html:rewrite page="/js/checkDate.js"/>"></script>
        
        
    </head>
    <body>
        <form>
        <%
        
        WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
        
        String id = request.getParameter("id");
        if (id == null){ id = "0";};
        int iNum = Integer.parseInt(id);
	
        List<IntakeNode> lis = genericIntakeManager.getIntakeNodes();
        if (lis != null){ %>
        <script>
			function change_form(value) {
				location.href='EditIntake.jsp?id=' + value;
			}
        </script>
	<select onchange="change_form(this.options[this.selectedIndex].value)">
                    <% for (IntakeNode i : lis) {
			   String frmLabel = i.getForm_version()!=null ? i.getLabelStr()+" ("+i.getForm_version()+")" : i.getLabelStr();
			   if (i.getPublish_date()!=null) frmLabel += " PUBLISHED " +i.getPublishDateStr()+ " BY " +i.getPublish_by();
			   %>
	    <option <%=i.getId()==iNum?"selected":""%> value="<%=i.getId()%>"><%=frmLabel%></option>
                    <% } %>
	</select>
	<%}
	if (session.getAttribute("publisher")==null) session.setAttribute("publisher", request.getParameter("pub"));
	
	IntakeNode iNode = (IntakeNode) session.getAttribute("intakeNode");
        if (iNode==null || !iNode.getId().equals(iNum)) {
        	if(iNum > 0) {
        		iNode = genericIntakeManager.getIntakeNode(iNum);
        	}
        }
        
		if(iNode != null) {
        	session.setAttribute("form_version", getFrmVersion(iNode.getLabelStr(), lis));
	        goRunner(iNode,out);
    	    session.setAttribute("intakeNode", iNode);
	
     		if(iNode.getPublish_date() == null) {
    	 		//this intake has never been published..safe to delete
    	 		out.write(" <input type=\"button\" value=\"Delete Form\" onclick=\"deleteform();\" />");
     		}
		
			out.write("<p>&nbsp;</p>");
			out.write("Publish as: ");
	%>
		<select name="form_type">
			<option value="0" default></option>
			<option value="1">Registration Intake</option>
			<option value="2">Follow Up Intake</option>
			<option value="3">General Form</option>			
		</select>
	<%
			out.write(" <input type=\"button\" value=\"Save Form\" onclick=\"saveform(form_type.options[form_type.selectedIndex].value);\" /><br/><br/>");

		    out.write("<br>");
		    out.write(" <input type=\"button\" value=\"Import\" onclick=\"importform();\" />&nbsp;");
		    out.write(" <input type=\"button\" value=\"Export\" onclick=\"exportform();\" />");
		    out.write("<br>");
			out.write(" <input type=\"button\" value=\"Reset\" onclick=\"resetform();\" />");
		    out.write("<p>&nbsp;</p>");
		    out.write(" <input type=\"button\" value=\"Close\" onclick=\"window.close();\" />");
     
		}
		    %>
     
        </form>
    </body>
</html>

<%!

void  goRunner(IntakeNode in,JspWriter out) throws Exception{
//String ret = in.getLabelStr()+ " : "+ in.getId()+" <br/>";
    IntakeNode pIntake = in.getParent();
    IntakeNode node = in;
    String pId = "";
    if (pIntake != null){
        pId = ""+pIntake.getId();
    }
    
    
    String si = ""+in.getChildren().size();
  
    
    boolean hasChildren = false;
    
    if (in.getChildren() != null && in.getChildren().size() > 1){
        hasChildren = true;
    }
    
    if (in.getId().equals(1) || in.getId().equals(2)) {
	in.setLabel(in.getNodeTemplate().getLabel());
    }
    String labelId = "";
    if (in.getLabel() != null){
        labelId = ""+in.getLabel().getId();
    }
    String exitElement = "</blockquote>";
    
    if (node.isIntake()) {
        out.write("<h1>");
        out.write(in.getLabelStr());
//out.write("id:"+in.getId() + ":"+in.getLabelStr()+ " : "+in.getNodeTemplate().getId()+" x:"+in.getIndex()+" "+in.getType()+" "+pId+" ");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"add('"+in.getId()+"','1','"+pId+"','"+in.getIndex()+"','"+si+"');\">+</a>");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
        if(hasChildren){
            out.write(" <a href=\"javascript: void(0);\" onclick=\"reorder('"+in.getId()+"');\">[reorder children]</a>");
        }
        out.write("</h1>");
        exitElement = "";
        
    } else if (node.isPage()) {
        out.write("<blockquote>");
        out.write(in.getLabelStr());
//out.write("id:"+in.getId() + ":"+in.getLabelStr()+ " : "+in.getNodeTemplate().getId()+" x:"+in.getIndex()+" "+in.getType()+" "+pId+" ");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"add('"+in.getId()+"','"+in.getNodeTemplate().getId()+"','"+pId+"','"+in.getIndex()+"','"+si+"');\">+</a>");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
    } else if (node.isSection()) {
        out.write("<div  style=\"border:1px solid #84A3D1;\">");
        out.write("<h3 style=\"background:#85AEEC ; border:1px solid #84A3D1; color:#000000;  font-size:12px; font-weight:bold; margin-top: 0;\">");
        out.write(in.getLabelStr());
//out.write("id:"+in.getId() + ":"+in.getLabelStr()+ " : "+in.getNodeTemplate().getId()+" x:"+in.getIndex()+" "+in.getType()+" "+pId+" ");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"add('"+in.getId()+"','4','"+pId+"','"+in.getIndex()+"','"+si+"');\">+</a>");
	out.write(" <a href=\"javascript: void(0);\" onclick=\"del('"+in.getId()+"','"+pId+"');\">-</a>");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
        if(hasChildren){
            out.write(" <a href=\"javascript: void(0);\" onclick=\"reorder('"+in.getId()+"');\">[reorder children]</a>");
        }
        
        out.write("</h3>");
        exitElement = "</div>";
    } else if (node.isQuestion()) {
        out.write("<blockquote>");
        out.write("<h3 style=\"background:grey ; border:1px solid grey; color:#000000;  font-size:12px; font-weight:bold; margin-top: 0;\">");
        out.write(in.getLabelStr());
	if (in.getMandatory()) {
	    out.write("<font color=red>*</font>");
	}
//out.write("id:"+in.getId() + ":"+in.getLabelStr()+ " : "+in.getNodeTemplate().getId()+" x:"+in.getIndex()+" "+in.getType()+" "+pId+" ");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"add('"+in.getId()+"','5','"+pId+"','"+in.getIndex()+"','"+si+"');\">+</a>");
	out.write(" <a href=\"javascript: void(0);\" onclick=\"del('"+in.getId()+"','"+pId+"');\">-</a>");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
        if(hasChildren){
            out.write(" <a href=\"javascript: void(0);\" onclick=\"reorder('"+in.getId()+"');\">[reorder children]</a>");
        }
	if (in.getEq_to_id()==null || in.getEq_to_id()<0) {
	    out.write(" <font color=red>(new)</font>");
	}
        out.write("</h3>");
    } else if (node.isAnswerCompound()) {
        out.write("<div  style=\"border:1px solid blue;\">");
        out.write("<h3 style=\" border:1px solid #84A3D1; color:#000000;  font-size:12px; font-weight:bold; margin-top: 0;\">");
        out.write("Inline group of questions :"+in.getLabelStr());
	if (in.getMandatory()) {
	    out.write("<font color=red>*</font>");
	}
//out.write("+ answer scalar choice + answer scalar text + answer scalar note")
//out.write("id:"+in.getId() + ":"+in.getLabelStr()+ " : "+in.getNodeTemplate().getId()+" x:"+in.getIndex()+" "+in.getType()+" "+pId+" ");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"add('"+in.getId()+"','6','"+pId+"','"+in.getIndex()+"','"+si+"');\">+</a>");
	out.write(" <a href=\"javascript: void(0);\" onclick=\"del('"+in.getId()+"','"+pId+"');\">-</a>");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
        if(hasChildren){
            out.write(" <a href=\"javascript: void(0);\" onclick=\"reorder('"+in.getId()+"');\">[reorder children]</a>");
        }
        out.write("</h3>");
        exitElement = "</div>";
    } else if (node.isAnswerChoice()) {
	if (node.isAnswerBoolean()) {
	    out.write("<blockquote>");
	    out.write("<label><input type=\"checkbox\"/>"+in.getLabelStr()+ "</label>");
	    out.write(" <a href=\"javascript: void(0);\" onclick=\"del('"+in.getId()+"','"+pId+"');\">-</a>");
	    out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
        } else {
	    Set intakeAnswerElements = in.getNodeTemplate().getAnswerElements();
	    Object[] elements = intakeAnswerElements.toArray();
	    
	    out.write("<blockquote>");
	    out.write("<label>"+in.getLabelStr()+" <select>");
	    for (int i=0; i<elements.length; i++) {
		IntakeAnswerElement answerElement = (IntakeAnswerElement) elements[i];
			if(answerElement.getLabel()==null) {
				out.write("<option>"+answerElement.getElement()+"</option>");
			} else {
				out.write("<option>"+answerElement.getLabel()+"</option>");
			}
	    }
	    out.write("</select></label>");
	    out.write(" <a href=\"javascript: void(0);\" onclick=\"del('"+in.getId()+"','"+pId+"');\">-</a>");
	    out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
	}
	if (in.getEq_to_id()==null || in.getEq_to_id()<0) {
	    out.write(" <font color=red>(new)</font>");
	}
//out.write("id:"+in.getId() + ":"+in.getLabelStr()+ " : "+in.getNodeTemplate().getId()+" x:"+in.getIndex()+" "+in.getType()+" "+pId+" ");
//out.write(" <a href=\"javascript: void(0);\" onclick=\"add('"+in.getId()+"','"+in.getNodeTemplate().getId()+"','"+pId+"','"+in.getIndex()+"','"+si+"');\">+</a>");
        
    } else if (node.isAnswerText()) {
        out.write("<blockquote>");
        out.write("<label>"+in.getLabelStr()+ "<input type=\"text\"  /> </label>");
	out.write(" <a href=\"javascript: void(0);\" onclick=\"del('"+in.getId()+"','"+pId+"');\">-</a>");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
	if (in.getEq_to_id()==null || in.getEq_to_id()<0) {
	    out.write(" <font color=red>(new)</font>");
	}
        
//out.write("id:"+in.getId() + ":"+in.getLabelStr()+ " : "+in.getNodeTemplate().getId()+" x:"+in.getIndex()+" "+in.getType()+" "+pId+" ");
//out.write(" <a href=\"javascript: void(0);\" onclick=\"add('"+in.getId()+"','"+in.getNodeTemplate().getId()+"','"+pId+"','"+in.getIndex()+"','"+si+"');\">+</a>");
        
    } else if (node.isAnswerNote()) {
        out.write("<blockquote>");
        out.write(in.getLabelStr()+"<textarea rows=\"5\" cols=\"50\"></textarea>");
//out.write("id:"+in.getId() + ":"+in.getLabelStr()+ " : "+in.getNodeTemplate().getId()+" x:"+in.getIndex()+" "+in.getType()+" "+pId+" ");
//out.write(" <a href=\"javascript: void(0);\" onclick=\"add('"+in.getId()+"','"+in.getNodeTemplate().getId()+"','"+pId+"','"+in.getIndex()+"','"+si+"');\">+</a>");
	out.write(" <a href=\"javascript: void(0);\" onclick=\"del('"+in.getId()+"','"+pId+"');\">-</a>");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
	if (in.getEq_to_id()==null || in.getEq_to_id()<0) {
	    out.write(" <font color=red>(new)</font>");
	}
    } else if (node.isAnswerDate()) {
        out.write("<blockquote>");
        out.write("<label>"+in.getLabelStr()+ "<input type=\"text\"  /> (DATE)</label>");
	out.write(" <a href=\"javascript: void(0);\" onclick=\"del('"+in.getId()+"','"+pId+"');\">-</a>");
        out.write(" <a href=\"javascript: void(0);\" onclick=\"editlabel('"+labelId+"','"+in.getId()+"');\">[edit]</a>");
	if (in.getEq_to_id()==null || in.getEq_to_id()<0) {
	    out.write(" <font color=red>(new)</font>");
	}
            	
    } else {
        throw new IllegalStateException("No html adapter for type: " + node.getType());
    }
    
    for (IntakeNode iN : in.getChildren()){
        goRunner(iN,out);
    }
    
    out.write(exitElement);
}

// with deletion, we can't use the count. we need the max for
// that label, and add 1.
int getFrmVersion(String frmLabel, List<IntakeNode> iList) {
	int max_form_version=0;	
    for (int i=0; i<iList.size(); i++) {
		String iLabel = iList.get(i).getLabelStr().trim();
		if (iLabel != null && iLabel.equals(frmLabel.trim())) {
			if(iList.get(i).getForm_version() == null) continue;
			int tmp = iList.get(i).getForm_version().intValue();
			if(tmp > max_form_version) max_form_version=tmp;		
		}
    }
    return ++max_form_version;
}

%>
