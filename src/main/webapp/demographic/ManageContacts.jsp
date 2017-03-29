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

<%@ page import="java.util.List"%>
<%@ page import="org.oscarehr.common.model.Contact"%>
<%@page import="org.oscarehr.common.model.DemographicContact"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
  String demographic_no = request.getParameter("demographic_no");
  if(demographic_no == null) {
	  demographic_no = (String)request.getAttribute("demographic_no");
  }

%>


<%@ include file="/taglibs.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Demographic Contact Manager</title>
<!--I18n-->
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css" />

<script src="<c:out value="../js/jquery.js"/>"></script>
<script>
	jQuery.noConflict();
</script>


<script type="text/javascript">
<!--
//if (document.all || document.layers)  window.resizeTo(790,580);
function newWindow(file,window) {
  msgWindow=open(file,window,'scrollbars=yes,width=760,height=520,screenX=0,screenY=0,top=0,left=10');
  if (msgWindow.opener == null) msgWindow.opener = self;
}
//-->
</script>

<script>
function addContact() {
	var total = jQuery("#contact_num").val();
	total++;
	jQuery("#contact_num").val(total);
	jQuery.ajax({url:'contact.jsp?search=Search&id='+total,async:false, success:function(data) {
		  jQuery("#contact_container").append(data);
	}});
}

function addContactExisting() {
    var total = jQuery("#contact_num").val();
    total++;
    jQuery("#contact_num").val(total);
    jQuery.ajax({url:'contact.jsp?search=&id='+total,async:false, success:function(data) {
    jQuery("#contact_container").append(data);
    }});
}


function deleteContact(id) {
	var contactId = jQuery("input[name='contact_"+id+".id']").val();
	jQuery("form[name='contactForm']").append("<input type=\"hidden\" name=\"contact.delete\" value=\""+contactId+"\"/>");
	jQuery("#contact_"+id).remove();

}

function addProContact() {
	var total = jQuery("#procontact_num").val();
	total++;
	jQuery("#procontact_num").val(total);
	jQuery.ajax({url:'procontact.jsp?search=Search&id='+total,async:false, success:function(data) {
		  jQuery("#procontact_container").append(data);
	}});
}

function addProContactExisting() {
    var total = jQuery("#procontact_num").val();
    total++;
    jQuery("#procontact_num").val(total);
    jQuery.ajax({url:'procontact.jsp?search=&id='+total,async:false, success:function(data) {
              jQuery("#procontact_container").append(data);
    }});
}


function deleteProContact(id) {
	var contactId = jQuery("input[name='procontact_"+id+".id']").val();
	jQuery("form[name='contactForm']").append("<input type=\"hidden\" name=\"procontact.delete\" value=\""+contactId+"\"/>");
	jQuery("#procontact_"+id).remove();

}

function doPersonalSearch(id) {
	var type = jQuery("select[name='contact_"+id+".type']").val();
	if(type == '<%=DemographicContact.TYPE_DEMOGRAPHIC%>') {
		search_demographic('contact_'+id+'.contactName','contact_'+id+'.contactId');
	}
	if(type == '<%=DemographicContact.TYPE_CONTACT%>') {
		search_contact('contact_'+id+'.contactName','contact_'+id+'.contactId');
	}
}

function doProfessionalSearch(id) {
	var type = jQuery("select[name='procontact_"+id+".type']").val();
	if(type == '<%=DemographicContact.TYPE_PROVIDER%>') {
		search_provider('procontact_'+id+'.contactName','procontact_'+id+'.contactId');
	}
	if(type == '<%=DemographicContact.TYPE_CONTACT%>') {
		search_procontact('procontact_'+id+'.contactName','procontact_'+id+'.contactId');
	}
	if(type == '<%=DemographicContact.TYPE_PROFESSIONALSPECIALIST%>') {
		search_professionalSpecialist('procontact_'+id+'.contactName','procontact_'+id+'.contactId');
	}
}

function updTklrList() {
    clearInterval(check_demo_no);
}

function search_demographic(nameEl, valueEl) {
    var url = '../ticklerPlus/demographicSearch2.jsp?outofdomain=false&form=contactForm&elementName='+nameEl+'&elementId='+valueEl;
    var popup = window.open(url,'demographic_search');
    demo_no_orig = document.contactForm.elements[valueEl].value;
    //check_demo_no = setInterval("if (demo_no_orig != document.contactForm.elements[valueEl].value) updTklrList()",100);

		if (popup != null) {
		if (popup.opener == null) {
				popup.opener = self;
		}
		popup.focus();
		}
}

function search_provider(nameEl, valueEl) {
    var url = '../provider/receptionistfindprovider.jsp?custom=true&form=contactForm&elementName='+nameEl+'&elementId='+valueEl;
    var popup = window.open(url,'demographic_search');
    demo_no_orig = document.contactForm.elements[valueEl].value;
    //check_demo_no = setInterval("if (demo_no_orig != document.contactForm.elements[valueEl].value) updTklrList()",100);

		if (popup != null) {
		if (popup.opener == null) {
				popup.opener = self;
		}
		popup.focus();
		}
}

function search_contact(nameEl, valueEl) {
    var url = 'contactSearch.jsp?form=contactForm&elementName='+nameEl+'&elementId='+valueEl;
    var popup = window.open(url,'demographic_search');
    demo_no_orig = document.contactForm.elements[valueEl].value;
    //check_demo_no = setInterval("if (demo_no_orig != document.contactForm.elements[valueEl].value) updTklrList()",100);

		if (popup != null) {
		if (popup.opener == null) {
				popup.opener = self;
		}
		popup.focus();
		}
}

function search_procontact(nameEl, valueEl) {
    var url = 'procontactSearch.jsp?form=contactForm&elementName='+nameEl+'&elementId='+valueEl;
    var popup = window.open(url,'demographic_search');
    demo_no_orig = document.contactForm.elements[valueEl].value;
    //check_demo_no = setInterval("if (demo_no_orig != document.contactForm.elements[valueEl].value) updTklrList()",100);

		if (popup != null) {
		if (popup.opener == null) {
				popup.opener = self;
		}
		popup.focus();
		}
}

function search_professionalSpecialist(nameEl, valueEl) {
    var url = 'professionalSpecialistSearch.jsp?form=contactForm&elementName='+nameEl+'&elementId='+valueEl;
    var popup = window.open(url,'demographic_search');
    demo_no_orig = document.contactForm.elements[valueEl].value;
    //check_demo_no = setInterval("if (demo_no_orig != document.contactForm.elements[valueEl].value) updTklrList()",100);

		if (popup != null) {
		if (popup.opener == null) {
				popup.opener = self;
		}
		popup.focus();
		}
}


function setSelect(id,type,name,val) {
	jQuery("select[name='"+type+"_"+id+"."+name+"']").each(function() {
		jQuery(this).val(val);
	});
}

function setSelectExisting(id,type,name,val) {
    jQuery("select[name='"+type+"_"+id+"."+name+"']").attr('disabled', 'disabled').each(function() {
            jQuery(this).val(val);
    });
}

function setInput(id,type,name,val) {
	jQuery("input[name='"+type+"_"+id+"."+name+"']").each(function() {
		jQuery(this).val(val);
	});
}

function setChecked(id,type,name) {
	jQuery("input[name='"+type+"_"+id+"."+name+"']").each(function() {
		jQuery(this).attr('checked','true');
	});
}

function setTextarea(id,type,name,val) {
	jQuery("textarea[name='"+type+"_"+id+"."+name+"']").each(function() {
		jQuery(this).val(val);
	});
}

jQuery(document).ready(function() {
	<%
		@SuppressWarnings("unchecked")
		List<DemographicContact> dcs = (List<DemographicContact>) request.getAttribute("contacts");
		if(dcs != null) {
			for(DemographicContact dc:dcs) {
				%>
					addContactExisting();
					var num = jQuery("#contact_num").val();
					setInput(num,'contact','id','<%=dc.getId()%>');
					setSelect(num,'contact','role','<%=dc.getRole()%>');
					setSelectExisting(num,'contact','type','<%=dc.getType()%>');
					setSelect(num,'contact','consentToContact','<%=dc.isConsentToContact()?"1":"0"%>');
					setSelect(num,'contact','active','<%=dc.isActive()?"1":"0"%>');
					setSelect(num,'contact','programId','<%=dc.getProgramNo()!=null?dc.getProgramNo():"0"%>');
					setInput(num,'contact','contactId','<%=dc.getContactId()%>');
					setInput(num,'contact','contactName','<%=dc.getContactName()%>');
					setTextarea(num,'contact','note','<%=dc.getNote()!=null?dc.getNote():""%>');
					<%if(dc.getSdm() != null && dc.getSdm().equals("true")) {%>setChecked(num,'contact','sdm');<%}%>
					<%if(dc.getEc() != null && dc.getEc().equals("true")) {%>setChecked(num,'contact','ec');<%}%>
			    <%
			}
		}

		@SuppressWarnings("unchecked")
		List<DemographicContact> pdcs = (List<DemographicContact>) request.getAttribute("procontacts");
		if(pdcs != null) {
			for(DemographicContact dc:pdcs) {
				%>
					addProContactExisting();
					var num = jQuery("#procontact_num").val();
					setInput(num,'procontact','id','<%=dc.getId()%>');
					setSelect(num,'procontact','role','<%=dc.getRole()%>');
					setSelect(num,'procontact','consentToContact','<%=dc.isConsentToContact()?"1":"0"%>');
					setSelect(num,'procontact','programId','<%=dc.getProgramNo()!=null?dc.getProgramNo():"0"%>');
					setSelect(num,'procontact','active','<%=dc.isActive()?"1":"0"%>');
					setSelectExisting(num,'procontact','type','<%=dc.getType()%>');
					setInput(num,'procontact','contactId','<%=dc.getContactId()%>');
					setInput(num,'procontact','contactName','<%=dc.getContactName()%>');
			    <%
			}
		}
	%>
	});

</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle">
<!--  -->
<table class="MainTable" id="scrollNumber1">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Manage Contacts</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><oscar:nameage demographicNo="<%=demographic_no%>" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="contact" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp; <a
			href="javascript:window.close();">Close Window</a></td>
		<td valign="top" class="MainTableRightColumn">

		<form method="post" name="contactForm" id="contactForm" action="Contact.do">
			<input type="hidden" name="method" value="saveManage"/>
			<input type="hidden" name="demographic_no" value="<%=demographic_no%>"/>

			<b>Personal Contacts:</b>
			<br />
			<div id="contact_container"></div>
			<input type="hidden" id="contact_num" name="contact_num" value="0"/>
			<a href="#" onclick="addContact();">[ADD]</a>

			<br/><br/>
			<b>Professional Contacts:</b>
			<br />
			<div id="procontact_container"></div>
			<input type="hidden" id="procontact_num" name="procontact_num" value="0"/>
			<a href="#" onclick="addProContact();">[ADD]</a>

			<br/>

			<input type="submit" value="Submit" />
			&nbsp;&nbsp;
			<input type="button" name="cancel" value="Cancel" onclick="window.close()" />

		</form>

	</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
</body>
</html:html>
