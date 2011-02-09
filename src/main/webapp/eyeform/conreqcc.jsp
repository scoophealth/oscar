<%@page import="org.oscarehr.common.model.ProfessionalSpecialist"%>



           <tr>

                <td class="tite4">
                
                    CC:
                
                	<select name="docName" id="docName">
		                <%java.util.List<ProfessionalSpecialist> dc=(java.util.List<ProfessionalSpecialist>)request.getAttribute("professionalSpecialists");
		                for (int i=0;i<dc.size();i++){
		                	ProfessionalSpecialist lb=(ProfessionalSpecialist)dc.get(i);%>
		                        <option value="<%=lb.getId() %>"><%=lb.getLastName() %>,<%=lb.getFirstName() %></option>
		                <%} %>
                	</select>
                <input type="button" value="add" onclick="addCCName()">
                </td>                 
            
            
            	<td class="tite3"><input  style="with:100%" name="ext_cc" value="<%=request.getAttribute("requestCc") %>"></td>
           
		</tr>