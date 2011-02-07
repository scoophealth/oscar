<tr>
            <td colspan=2 class="tite4">
            <table width="100%">
                    <tr>
                        <td width="30%" class="tite4">
                           Ocular Examination:
                        </td>
                        <td>
                            <input type="button" class="btn" value="current hx" onclick="currentProAdd('cHis',document.forms[0].specialProblems);" />&nbsp;
                            <input type="button" class="btn" value="past ocular hx" onclick="currentProAdd('pHis',document.forms[0].specialProblems);" />&nbsp;
                            <input type="button" class="btn" value="ocular meds" onclick="currentProAdd('oMeds',document.forms[0].specialProblems);" />&nbsp;

                            <input type="button" class="btn" value="diagnostic notes" onclick="currentProAdd('dTest',document.forms[0].specialProblems);" />&nbsp;
                            <input type="button" class="btn" value="past ocular proc" onclick="currentProAdd('oProc',document.forms[0].specialProblems);" />&nbsp;
                            <input type="button" class="btn" value="specs hx" onclick="currentProAdd('specs',document.forms[0].specialProblems);" />&nbsp;

                            <input type="button" class="btn" value="impression" onclick="currentProAdd('impress',document.forms[0].specialProblems);" />&nbsp;
                            <input type="button" class="btn" value="follow-up" onclick="currentProAdd('followup',document.forms[0].specialProblems);" />&nbsp;
                            <input type="button" class="btn" value="proc" onclick="currentProAdd('probooking',document.forms[0].specialProblems);" />
                            <input type="button" class="btn" value="test" onclick="currentProAdd('testbooking',document.forms[0].specialProblems);" />&nbsp;

                         </td>
                    </tr>
                </table>
            </td>
       </tr>
       
       <tr>
            <td colspan="2">
                <table>
                	<tr>
                		<td>
                			<select name="fromlist" multiple="multiple" size="9" ondblclick="addSpecialExam();">
                			</select>
							<input style="vertical-align: middle;" type="button" value="add" onclick="addSpecialExam();">
                		</td>
               			 <td>
                			<textarea cols="62" rows="8"  name="ext_specialProblem"><%=request.getAttribute("ext_specialProblem") %></textarea>
                		</td>
                	</tr>
                </table>
            </td>
       </tr>
       
       <tr>
	       <td colspan=2 class="tite4">
	            <table width="100%">
	                    <tr>
	                        <td width="30%" class="tite4">
	                           Send ticker:
	                        </td>
	                     </tr>
	            </table>
	        </td>
        </tr>

        <tr>
        	<td colspan="2">
        		<table>
        			<tr>
       					<td><input type="checkbox" name="ackdoc" checked> remind me to complete it</td>
					    <td>
					    	<input type="checkbox" name="ackfront" checked>remind
						    <select name="providerl">       
       						</select>
					      to arrange it
       					</td>
				       <td><input type="button" name="sendtickler" value="send tickler" onclick="sendSepcialTickler();"></td>
       				</tr>
       			</table>
      		 </td>
       </tr>

                        
                        