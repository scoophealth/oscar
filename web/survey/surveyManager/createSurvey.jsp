<%@ include file="/survey/taglibs.jsp" %>

<style type="text/css">
<!--
    .surveyPage {
        background-color: #EEEEEE;
        font-family: Arial, Helvetica, sans-serif;
        font-size: 12px;
        border-color: #CCCCCC;
        border-width: 1px;
        border-style: ridge;
        margin: 3px;
    }
    
    .pageTitle {
        background-color: #CCCCCC;
    }
    
    .section {
        background-color: #FFEEEE;
        font-family: Arial, Helvetica, sans-serif;
        font-size: 12px;
        border-color: #CCCCCC;
        border-width: 1px;
        border-style: ridge;
        margin: 5px;
    }
    
    .container {
       margin-left: 10px;
    }
    
	.message {
		color: red;
		background-color: white;
	}
	.error {
		color: red;
		background-color: white;
	}
-->
</style>

<html:form action="/SurveyManager" method="POST" styleId="surveyForm">
	<input type="hidden" name="method" value="create_survey"/>
    <input type="hidden" name="numPages" id="numPages" value="1"/>
    
	<html:hidden property="survey.surveyId"/>
	<br/>
	<table width="100%">
		<logic:messagesPresent message="true">
			<html:messages id="message" message="true" bundle="survey">
	   			<tr><td colspan="3" class="message"><c:out value="${message}"/></td></tr>
	    	</html:messages>
		</logic:messagesPresent>
		<logic:messagesPresent>
	  	  <html:messages id="error" bundle="survey">
            <tr><td colspan="3" class="error"><c:out value="${error}"/></td></tr>
       	 </html:messages>
		</logic:messagesPresent>
        <tr>
            <td class="leftfield">Form Name:&nbsp;&nbsp;
            <html:text property="survey.description" styleClass="formElement"/>&nbsp;&nbsp;
            </td>
        </tr>
        <tr>
        	<td class="leftfield">Template:&nbsp;&nbsp;
        		<html:select property="web.templateId" styleClass="formElement">
        			<html:option value="0">&nbsp;</html:option>
        			<html:options collection="templates" property="surveyId" labelProperty="description"/>
        		</html:select>
        	</td>
        </tr>
        <tr>
            <td><br/><html:submit>Create Form</html:submit></td>
        </tr>
  	</table>
</html:form>

