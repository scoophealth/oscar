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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.marc.shic.core.configuration.IheActorType"%>
<%@page import="org.marc.shic.core.configuration.consent.PolicyActionOutcome"%>
<%@page import="org.marc.shic.core.configuration.consent.DemandPermission"%>
<%@page import="org.oscarehr.sharingcenter.model.CodeValueDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.CodeMappingDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.ValueSetDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.AclDefinitionDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.PolicyDefinitionDataObject"%>
<%@page import="org.oscarehr.sharingcenter.dao.AffinityDomainDao"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>
<%@page import="org.oscarehr.sharingcenter.model.ActorDataObject"%>
<%@page import="org.oscarehr.util.SpringUtils"%>

<%
    String domainId = request.getParameter("id");

    AffinityDomainDataObject affDomain = null;
    String id = "";

    if (!domainId.equals("new")) {

        AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
        try {
            affDomain = affDao.getAffinityDomain(Integer.parseInt(domainId));
            if (affDomain != null) {
                id = String.valueOf(affDomain.getId());
            }
        } catch (NumberFormatException ex) {
            //nothing to do. affDomian remains null and id remains "".
        }
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Oscar Sharing Center</title>

        <link rel="stylesheet" href="${ctx}/library/bootstrap/3.0.0/css/bootstrap.min.css">
        <script src="${ctx}/js/jquery-1.9.1.min.js"></script>
        <script src="${ctx}/library/bootstrap/3.0.0/js/bootstrap.min.js"></script>

        <script>

                    var affDomainDict;
                    //Set the affinity domain dictionary
            <%if (affDomain != null) {%>
            affDomainDict = {
            "oid" : "<%=affDomain.getOid()%>",
                    "name" : "<%=affDomain.getName()%>",
                    "permission" : "<%=affDomain.getPermission()%>",
                    "actors" : [
            <%for (ActorDataObject actor : affDomain.getActors()) {%>
                    {
                    "type" : "<%=actor.getActorType().toString()%>",
                            "oid" : "<%=actor.getOid()%>",
                            "name" : "<%=actor.getName()%>",
                            "secure" : "<%=actor.getSecure()%>",
                            "endpoint" : "<%=actor.getEndpoint()%>",
                            "receivingApp" : "<%=actor.getRemoteIdentification().getApplicationId()%>",
                            "receivingFacility" : "<%=actor.getRemoteIdentification().getFacilityName()%>"
                    },
            <%}%>
                    ],
                    "policies" : [
            <%for (PolicyDefinitionDataObject policy : affDomain.getPolicyDefinitions()) {%>
                    {
                    "displayName" : "<%=policy.getDisplayName()%>",
                            "code" : "<%=policy.getCode()%>",
                            "codeSystem" : "<%=policy.getCodeSystem()%>",
                            "policyDocUrl" : "<%=policy.getPolicyDocUrl()%>",
                            "ackDuration" : "<%=policy.getAckDuration()%>",
                            "acl" :  [
            <%for (AclDefinitionDataObject acl : policy.getAclDefinitions()) {%>
                            {
                            "role" : "<%=acl.getRole()%>",
                                    "permission" : "<%=acl.getPermission().toString()%>",
                                    "actionOutcome" : "<%=acl.getActionOutcome().toString()%>"
                            },
            <%}%>]
                    },
            <%}%>],
                    "valueSetMappings" : [
            <%for (ValueSetDataObject valueSet : affDomain.getValueSets()) {%>
                    {
                    "valueSetId" : "<%=valueSet.getValueSetId()%>",
                            "description" : "<%=valueSet.getDescription()%>",
                            "attribute" : "<%=valueSet.getAttribute()%>"
                    },
            <%}%>],
                    "codeMappings" : [
            <%for (CodeMappingDataObject mapping : affDomain.getCodeMappings()) {%>
                    {
                    "attribute" : "<%=mapping.getAttribute()%>",
                            "description" : "<%=mapping.getDescription()%>",
                            "codes" :  [
            <%for (CodeValueDataObject code : mapping.getCodeValues()) {%>
                            {
                            "codeSystem" : "<%=code.getCodeSystem()%>",
                                    "code" : "<%=code.getCode()%>",
                                    "displayName" : "<%=code.getDisplayName()%>",
                                    "codeSystemName" : "<%=code.getCodeSystemName()%>"
                            },
            <%}%>
                            ]
                    },
            <%}%>
                    ]
            };
            <%}%>

            var globalActorNum = 0;
                    var globalPolicyNum = 0;
                    var globalValueSetNum = 0;
                    var globalCodeMappingNum = 0;
            <% String actorTypeList = "";
                for (IheActorType actor : IheActorType.values()) {
                    actorTypeList += "\"" + actor.toString() + "\",";
                }%>
            var ActorTypeList = [<%= actorTypeList%>];
            <% String demandPermissionList = "";
                for (DemandPermission dp : DemandPermission.values()) {
                    demandPermissionList += "\"" + dp.toString() + "\",";
                }%>
            var DemandPermissionList = [<%= demandPermissionList%>];
            <% String actionOutcomeList = "";
                for (PolicyActionOutcome a : PolicyActionOutcome.values()) {
                    actionOutcomeList += "\"" + a.toString() + "\",";
                }%>
            var ActionOutcomeList = [<%= actionOutcomeList%>];
                    function addNewActorForm(actorNum){

                    $.get("NewActorTemplate.html", function(html){

                    var string = html.replace(new RegExp("NUM", "g"), actorNum);
                            $("#newActorsPanel").append(string);
                            $("#newActorsPanel .panel:last").hide().fadeIn(200);
                            scrollToAnchor("#actorPanel_" + actorNum);
                    });
                    }

            function addNewPolicyForm(actorNum){

            $.get("NewPolicyTemplate.html", function(html){

            var string = html.replace(new RegExp("NUM", "g"), actorNum);
                    $("#newPolicyPanel").append(string);
                    $("#newPolicyPanel .parentPanel:last").hide().fadeIn(200);
                    scrollToAnchor("#policyPanel_" + actorNum);
            });
            }

            function confirmDelete() {
            <%if (affDomain != null) {%>
            var answer = confirm("Are you sure you want to delete \n this Affinity Domain Configuration?");
                    if (answer) {
            $("#action").val("delete");
                    $("#configform").submit();
            }
            <%}%>
            }



            function scrollToAnchor(aid){
            $('html,body').animate({scrollTop: $(aid).offset().top - 40}, 'slow');
            }

            $(document).ready(function(){

            //Set up the click funciton
            //Not all of these buttons are present and many probably won't be in the future
            $("#addActorButton").click(function(){
            addNewActorForm(++globalActorNum);
            });
                    $("#addPolicyButton").click(function(){
            addNewPolicyForm(++globalPolicyNum);
            });
                    $("#addACLButton").click(function(){
            addNewPolicyForm(++globalPolicyNum);
            });
                    $("#addValueSetButton").click(function(){
            addNewValueSetForm(++globalPolicyNum);
            });
                    $("#addCodeMappingButton").click(function(){
            addNewCodeMappingForm(++globalPolicyNum);
            });
                    $("#addCodeValueButton").click(function(){
            addNewCodeMappingForm(++globalPolicyNum);
            });
                    //----------------------

                    //Load the NavBar
                    $("#navBar").load("${ctx}/sharingcenter/globaltemplates/NavBar.jsp");
                    //Set up form validationg here. This is non-functional
                    $("form").submit(function(event){

            $("form input").each(function (){

            $(this).parent().removeClass("has-error");
                    var val = $.trim($(this).val());
                    if (val.length == 0){
            // 							// alert($(this).prev().text() + " requires a value.");
            // 							$(this).parent().addClass("has-error");
            // 							event.preventDefault();
            // 							// return false;
            }
            });
            });
                    //-------------------

                    //Embed all the information into the form from the affinity domain dictionary
                    if (affDomainDict != null){

            $("td[name='form_oid']").text(affDomainDict["oid"]);
                    $("td[name='form_name']").text(affDomainDict["name"]);
                    $("td[name='form_permission']").text(affDomainDict["permission"]);
                    //The actors
                    affDomainDict.actors.forEach(function(actor){

                    globalActorNum++;
                            $.ajax(
                            {
                            url:"NewActorTemplate.html",
                                    success: function(html){

                                    var inputName = "form_actor_" + globalActorNum;
                                            var string = html.replace(new RegExp("NUM", "g"), globalActorNum);
                                            $("#newActorsPanel").append(string);
                                            /*
                                             ActorTypeList.forEach(function(entry){
                                             option = "<option value=\"" + entry + "\">" + entry + "</option>";
                                             $("select[name='" + inputName + "_type']").append(option);
                                             });*/

                                            $("td[name='" + inputName + "_type']").text(actor["type"]);
                                            $("td[name='" + inputName + "_oid']").text(actor["oid"]);
                                            $("td[name='" + inputName + "_name']").text(actor["name"]);
                                            $("td[name='" + inputName + "_endpoint']").text(actor["endpoint"]);
                                            $("td[name='" + inputName + "_receivingApplication']").text(actor["receivingApp"]);
                                            $("td[name='" + inputName + "_receivingFacility']").text(actor["receivingFacility"]);
                                            if (actor["secure"] == "true") $("input[name='" + inputName + "_secure']").attr("checked", "");
                                    },
                                    async: false
                            });
                    });
                    //The policies
                    affDomainDict.policies.forEach(function(policy){

                    globalPolicyNum++;
                            $.ajax(
                            {
                            url: "NewPolicyTemplate.html",
                                    success: function(html){

                                    var aclCount = 0;
                                            var inputName = "form_policy_" + globalPolicyNum;
                                            var string = html.replace(new RegExp("NUM", "g"), globalPolicyNum);
                                            $("#newPolicyPanel").append(string);
                                            $("td[name='" + inputName + "_name']").text(policy["displayName"]);
                                            $("td[name='" + inputName + "_code']").text(policy["code"]);
                                            $("td[name='" + inputName + "_codeSystem']").text(policy["codeSystem"]);
                                            $("td[name='" + inputName + "_url']").text(policy["policyDocUrl"]);
                                            $("td[name='" + inputName + "_duration']").text(policy["ackDuration"]);
                                            //The ACLs in each policy
                                            policy["acl"].forEach(function(acl){

                                    $.ajax(
                                    {
                                    url: "NewACLTemplate.html",
                                            success: function(html) {

                                            aclCount++;
                                                    var inputName = "form_acl_" + globalPolicyNum + "_" + aclCount;
                                                    var string = html.replace(new RegExp("NUM", "g"), aclCount);
                                                    string = string.replace(new RegExp("PN", "g"), globalPolicyNum);
                                                    //$("#newACLPanel_" + globalPolicyNum).append(string);

                                                    DemandPermissionList.forEach(function(entry){
                                                    option = "<option value=\"" + entry + "\">" + entry + "</option>";
                                                            $("select[name='" + inputName + "_role_permission']").append(option);
                                                    });
                                                    ActionOutcomeList.forEach(function(entry){
                                                    option = "<option value=\"" + entry + "\">" + entry + "</option>";
                                                            $("select[name='" + inputName + "_role_action']").append(option);
                                                    });
                                                    $("input[name='" + inputName + "_role_type']").val(acl["role"]);
                                                    $("select[name='" + inputName + "_role_permission']").val(acl["permission"]);
                                                    $("select[name='" + inputName + "_role_action']").val(acl["actionOutcome"]);
                                            },
                                            async: false
                                    }
                                    );
                                    });
                                    },
                                    async: false
                            });
                    });
                    //The value set mappings
                    affDomainDict.valueSetMappings.forEach(function(valueSet){

                    globalValueSetNum++;
                            $.ajax(
                            {
                            url:"NewValueSetTemplate.html",
                                    success: function(html){

                                    var inputName = "form_valueSet_" + globalValueSetNum;
                                            var string = html.replace(new RegExp("NUM", "g"), globalValueSetNum);
                                            $("#newValueSetPanel").append(string);
                                            $("input[name='" + inputName + "_id']").val(valueSet["valueSetId"]);
                                            $("input[name='" + inputName + "_description']").val(valueSet["description"]);
                                            $("input[name='" + inputName + "_attribute']").val(valueSet["attribute"]);
                                    },
                                    async: false
                            });
                    });
                    //The Code mappings
                    affDomainDict.codeMappings.forEach(function(codeMapping){

                    globalCodeMappingNum++;
                            $.ajax(
                            {
                            url: "NewCodeMappingTemplate.html",
                                    success: function(html){

                                    var codeValueCount = 0;
                                            var inputName = "form_codeMapping_" + globalCodeMappingNum;
                                            var string = html.replace(new RegExp("NUM", "g"), globalCodeMappingNum);
                                            $("#newCodeMappingPanel").append(string);
                                            $("input[name='" + inputName + "_attribute']").val(codeMapping["attribute"]);
                                            $("input[name='" + inputName + "_description']").val(codeMapping["description"]);
                                            //The code values for each mapping
                                            codeMapping["codes"].forEach(function(code){

                                    $.ajax(
                                    {
                                    url: "NewCodeValueTemplate.html",
                                            success: function(html) {

                                            codeValueCount++;
                                                    var inputName = "form_codeValue_" + globalCodeMappingNum + "_" + codeValueCount;
                                                    var string = html.replace(new RegExp("NUM", "g"), codeValueCount);
                                                    string = string.replace(new RegExp("PN", "g"), globalCodeMappingNum);
                                                    $("#newCodeValuePanel_" + globalCodeMappingNum).append(string);
                                                    $("input[name='" + inputName + "_code_system']").val(code["codeSystem"]);
                                                    $("input[name='" + inputName + "_code']").val(code["code"]);
                                                    $("input[name='" + inputName + "_display_name']").val(code["displayName"]);
                                                    $("input[name='" + inputName + "_system_name']").val(code["codeSystemName"]);
                                            },
                                            async: false
                                    }
                                    );
                                    });
                                    },
                                    async: false
                            });
                    });
            }
            });</script>
        <style>
            .input-group {
                padding-bottom: "5px";
            }
            .panel-body {
                padding: "5px";
            }
        </style>

    </head>
    <body>
        <div class="container" width="500px">

            <div id="navBar"></div>

            <form id="configform" action="AffinityDomain.do" role="form" method="POST">

                <div class="panel panel-default">
                    <div class="panel-body">
                        <button type="button" class="btn btn-danger pull-left btn-sm" onclick="confirmDelete()">Remove</button>
                        <a href="${ctx}/sharingcenter/affinitydomain/mappings.jsp?id=<%=domainId%>" class="btn btn-info pull-right btn-sm">Mappings</a>
                    </div>
                </div>

                <div class="panel panel-default">

                    <div class="panel-heading">
                        <h2 class="panel-title">Affinity Domain</h2>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <tr>
                                <td>OID:</td>
                                <td name="form_oid"/>
                            </tr>
                            <tr>
                                <td>Name:</td>
                                <td name="form_name"/>
                            </tr>
                            <tr>
                                <td>Permission:</td>
                                <td name="form_permission"/>
                            </tr>
                        </table>
                    </div>
                </div>

                <!--  ACTOR PANEL -->
                <div id="actorsPanel" class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Actors</h3>
                        <div class="clearfix"></div>
                    </div>
                    <!--  END HEADING -->
                    <div id="newActorsPanel" class="panel-body">

                        <!-- new actors go here -->

                    </div>
                    <!-- END PANEL BODY -->
                    <!--  
                    <div class="panel-footer">
                            <div id="addActorButton" class="btn btn-primary btn-sm pull-right">
                                    <span class="pull-right">Add Actor</span>
                            </div>
                            <div class="clearfix"></div>
                    </div>-->
                </div>
                <!--  END PANEL -->


                <!--  POLICY PANEL -->
                <div id="policyPanel" class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Consent Policies</h3>
                        <div class="clearfix"></div>
                    </div>

                    <div id="newPolicyPanel" class="panel-body">
                        <!-- new policies go here -->
                    </div>

                    <!--  
                    <div class="panel-footer">
                            <div id="addPolicyButton" class="btn btn-primary btn-sm pull-right">
                                    <span class="pull-right">Add Policy</span>
                            </div>
                            <div class="clearfix"></div>
                    </div>-->
                </div>
                <!--  END PANEL -->



                <!--  VALUE SET PANEL -->
                <!--<div id="valueSetPanel" class="panel panel-default">
                        <div class="panel-heading">
                                <h3 class="panel-title">Value Set Mappings</h3>
                                <div class="clearfix"></div>
                        </div>

                        <div id="newValueSetPanel" class="panel-body">
                <!-- new Value Set go here -->
                <!--</div>

                <div class="panel-footer">
                        <div id="addValueSetButton" class="btn btn-primary btn-sm pull-right">
                                <span class="pull-right">Add Value Set</span>
                        </div>
                        <div class="clearfix"></div>
                </div>
        </div>-->
                <!--  END PANEL -->

                <!--  CODE MAPPINGS PANEL -->
                <!--<div id="codeMappingPanel" class="panel panel-default">
                        <div class="panel-heading">
                                <h3 class="panel-title">Code Mappings</h3>
                                <div class="clearfix"></div>
                        </div>

                        <div id="newCodeMappingPanel" class="panel-body">
                <!-- new code mappings go here -->
                <!--</div>

                <div class="panel-footer">
                        <div id="addCodeMappingPanel" class="btn btn-primary btn-sm pull-right">
                                <span class="pull-right">Add Code Mapping</span>
                        </div>
                        <div class="clearfix"></div>
                </div>
        </div>-->
                <!--  END PANEL -->


                <input type="hidden" name="configid" id="configid" value="<%= id%>" /> <input type="hidden"
                                                                                              name="action" id="action" value="" />
                <!--  
        <button type="button" class="btn btn-primary pull-right btn-sm">Save</button> &nbsp; &nbsp;-->
            </form>
        </div>
    </body>
</html>