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
    
    
-->
</style>


<html:form action="/SurveyManager" method="POST"  enctype="multipart/form-data"  styleId="surveyForm">
	<input type="hidden" name="method" value="import_survey"/>
	<table width=100%">
		<tr>
        	<td class="leftfield">File Name:&nbsp;&nbsp;
            <html:file property="web.importFile"/></td>
		</tr>
		<tr>
        	<td>
        		<html:submit value="Import"/>
        		<input type="button" value="Cancel"/>
        	</td>
		</tr>
	</table>
	
</html:form>