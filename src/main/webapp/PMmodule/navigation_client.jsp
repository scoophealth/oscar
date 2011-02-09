<script type="text/JavaScript">
function popupLabel(page) { //open a new popup window
  var windowprops = "height=500,width=800,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(page, "QuatroShelter", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
 //   popup.focus();
  }
}
</script>
<%@ include file="/taglibs.jsp"%>
<div id="projecttools" class="toolgroup">
<div class="label"><strong>Navigator</strong></div>
<div class="body">
<div><span><c:out value="${client.formattedName}" />(<c:out
	value="${client.demographicNo}" />)</span> <c:choose>
	<c:when test="${'C' eq tabSummary}">
		<div><b>Summary</b></div>
	</c:when>
	<c:when test="${'V' eq tabSummary}">
		<div><html:link action="/PMmodule/QuatroClientSummary.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Summary</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabHistory}">
		<div><b>History</b></div>
	</c:when>
	<c:when test="${'V' eq tabHistory}">
		<div><html:link action="/PMmodule/ClientHistory.do"
			name="actionParam" style="color:Navy;text-decoration:none;">History</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabIntake}">
		<div><b>Intake</b></div>
	</c:when>
	<c:when test="${'V' eq tabIntake}">
		<div><html:link action="/PMmodule/QuatroIntake.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Intake</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabAdmission}">
		<div><b>Admission</b></div>
	</c:when>
	<c:when test="${'V' eq tabAdmission}">
		<div><html:link action="/PMmodule/QuatroAdmission.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Admission</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabRefer}">
		<div><b>Referral</b></div>
	</c:when>
	<c:when test="${'V' eq tabRefer}">
		<div><html:link action="PMmodule/QuatroRefer.do"
			name="actionParam" style="color:Navy;text-decoration:none;"> Referral</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabDischarge}">
		<div><b>Discharge</b></div>
	</c:when>
	<c:when test="${'V' eq tabDischarge}">
		<div><html:link action="/PMmodule/QuatroDischarge.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Discharge</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabRestriction}">
		<div><b>Service Restriction</b></div>
	</c:when>
	<c:when test="${'V' eq tabRestriction}">
		<div><html:link action="/PMmodule/QuatroServiceRestriction.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Service Restriction</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabComplaint}">
		<div><b>Complaint</b></div>
	</c:when>
	<c:when test="${'V' eq tabComplaint}">
		<div><html:link action="/PMmodule/QuatroComplaint.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Complaint</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabConsent}">
		<div><b>Consent</b></div>
	</c:when>
	<c:when test="${'V' eq tabConsent}">
		<div><html:link action="/PMmodule/QuatroConsent.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Consent</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> <c:choose>
	<c:when test="${'C' eq tabCase}">
		<div><b>Case Management</b></div>
	</c:when>
	<c:when test="${'V' eq tabCase}">
		<div><html:link action="/CaseManagementView2.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Case Management</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
			</c:otherwise>
</c:choose> 
<c:choose>
	<c:when test="${'C' eq tabAttachment}">
		<div><b>Attachment</b></div>
	</c:when>
	<c:when test="${'V' eq tabAttachment}">
		<div><html:link action="/PMmodule/UploadFile.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Attachment</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${'C' eq tabTask}">
		<div><b>Tasks</b></div>
	</c:when>
	<c:when test="${'V' eq tabTask}">
		<div><html:link action="/PMmodule/Task.do"
			name="actionParam" style="color:Navy;text-decoration:none;">Tasks</html:link>
		</div>
	</c:when>
	<c:otherwise>
				&nbsp;
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${'C' eq tabPrintLabel}">
			<div><b>Print Label</b></div>
	</c:when>
	<c:when test="${'V' eq tabPrintLabel}">
		<div>
			<a href='javascript:popupLabel(&quot;<html:rewrite page="/demographic/printBarcodeAction.do?clientId="/><c:out value="${clientId}"></c:out>&quot;)'> Print Label</a>
		</div>
	</c:when>
	<c:otherwise>
			&nbsp;
	</c:otherwise>
</c:choose>
</div>
</div></div>