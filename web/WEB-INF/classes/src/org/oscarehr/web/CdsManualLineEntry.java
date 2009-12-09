package org.oscarehr.web;

import javax.servlet.http.HttpServletRequest;

public class CdsManualLineEntry {
	public int multipleAdmissions;
	public int cohort0;
	public int cohort1;
	public int cohort2;
	public int cohort3;
	public int cohort4;
	public int cohort5;
	public int cohort6;
	public int cohort7;
	public int cohort8;
	public int cohort9;
	public int cohort10;
	
	/**
	 * expects the request parameters to be of the form
	 * 7-02.ma, 7-02.c0, 7-02.c1 where 7-02 is the section
	 * 
	 * to be used with outputCdsManualLineEntryTable()
	 */
	public static CdsManualLineEntry getCdsManualLineEntry(HttpServletRequest request, String section)
	{
		CdsManualLineEntry cdsManualLineEntry=new CdsManualLineEntry();
		
		cdsManualLineEntry.multipleAdmissions=Integer.parseInt(request.getParameter(section+".ma"));
		cdsManualLineEntry.cohort0=Integer.parseInt(request.getParameter(section+".c0"));
		cdsManualLineEntry.cohort1=Integer.parseInt(request.getParameter(section+".c1"));
		cdsManualLineEntry.cohort2=Integer.parseInt(request.getParameter(section+".c2"));
		cdsManualLineEntry.cohort3=Integer.parseInt(request.getParameter(section+".c3"));
		cdsManualLineEntry.cohort4=Integer.parseInt(request.getParameter(section+".c4"));
		cdsManualLineEntry.cohort5=Integer.parseInt(request.getParameter(section+".c5"));
		cdsManualLineEntry.cohort6=Integer.parseInt(request.getParameter(section+".c6"));
		cdsManualLineEntry.cohort7=Integer.parseInt(request.getParameter(section+".c7"));
		cdsManualLineEntry.cohort8=Integer.parseInt(request.getParameter(section+".c8"));
		cdsManualLineEntry.cohort9=Integer.parseInt(request.getParameter(section+".c9"));
		cdsManualLineEntry.cohort10=Integer.parseInt(request.getParameter(section+".c10"));
		
		return(cdsManualLineEntry);
	}
	
	public static String outputCdsManualLineEntryTable(String section)
	{
		//	<table>
		//		<tr>
		//			<td>multiple admissions</td>
		//			<td>cohort 0</td>
		//			<td>1</td>
		//			<td>2</td>
		//			<td>3</td>
		//			<td>4</td>
		//			<td>5</td>
		//			<td>6</td>
		//			<td>7</td>
		//			<td>8</td>
		//			<td>9</td>
		//			<td>10</td>
		//		</tr>
		//		<tr>
		//			<td><input type="text" name="7-02.ma" /></td>
		//			<td><input type="text" name="7-02.c0" /></td>
		//			<td><input type="text" name="7-02.c1" /></td>
		//			<td><input type="text" name="7-02.c2" /></td>
		//			<td><input type="text" name="7-02.c3" /></td>
		//			<td><input type="text" name="7-02.c4" /></td>
		//			<td><input type="text" name="7-02.c5" /></td>
		//			<td><input type="text" name="7-02.c6" /></td>
		//			<td><input type="text" name="7-02.c7" /></td>
		//			<td><input type="text" name="7-02.c8" /></td>
		//			<td><input type="text" name="7-02.c9" /></td>
		//			<td><input type="text" name="7-02.c10" /></td>
		//		</tr>
		//	</table>
	
		StringBuilder sb=new StringBuilder();
		
		sb.append("<table>");
		sb.append("<tr>");

		sb.append("<td>Multiple Admissions</td>");
		for (int i=0; i<=10; i++)
		{
			sb.append("<td>Cohort "+i+"</td>");			
		}
		
		sb.append("</tr>");
		sb.append("<tr>");

		sb.append("<td><input type=\"text\" name=\""+section+".ma\" /></td>");
		for (int i=0; i<=10; i++)
		{
			sb.append("<td><input type=\"text\" name=\""+section+".c"+i+"\" /></td>");
		}
		
		sb.append("</tr>");
		sb.append("</table>");
		
		return(sb.toString());
	}
}
