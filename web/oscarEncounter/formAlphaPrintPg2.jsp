<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page import="java.util.*, java.sql.*, oscar.*"  %>
<%@ page import="oscar.oscarEncounter.data.EctAlphaRecord" %>

<HTML>
<HEAD>
<TITLE> ALPHA print pg2</TITLE>
<link rel="stylesheet" href="alphaStyle.css" >
<style media="print">
.hidePrint
{
    display: none;
}
</style>

</HEAD>

<%
    int demoNo = Integer.parseInt(request.getParameter("demoNo"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    EctAlphaRecord rec = new EctAlphaRecord();
    java.util.Properties props = rec.getAlphaRecord(demoNo, formId);
%>

<BODY BGCOLOR="#FFFFFF">

<TABLE BORDER CELLSPACING=1 BORDERCOLOR="#000000" CELLPADDING=4 width="100%">
  <TR>
    <TD VALIGN="TOP"HEIGHT=16 colspan="2">
      <table border="0" width="100%">
        <tr>
          <th ALIGN="CENTER"> <FONT FACE="Arial, Helvetica"><B>ANTENATAL PSYCHOSOCIAL
            HEALTH ASSESSMENT (ALPHA)</B></FONT></Th>
          <td align="right" class="hidePrint"><a href="formAlphaPrintPg1.jsp?demoNo=<%=demoNo%>&formId=<%=formId%>"> prev page </a> | <a href=# onClick="window.print();">PRINT</a> | <a href="formAlpha.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>">Cancel</a>
          </td>
        </tr>
      </table>
    </td>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"ALIGN="CENTER" bgcolor="#eeeeee"><B><FONT FACE="Arial" SIZE="-1">ANTENATAL
      FACTORS</FONT></B></TD>
    <TD WIDTH="50%" VALIGN="TOP" ALIGN="CENTER" bgcolor="#eeeeee"><FONT FACE="Arial" SIZE="-1">
      <B>COMMENTS/PLAN</B></FONT></TD>
  </TR>
  <TR>
    <TD colspan="2" VALIGN="TOP" bgcolor="#f9f9f9"><FONT FACE="Arial" SIZE="-1">
      SUBSTANCE USE</FONT><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"> <B>Alcohol/drug
      abuse (<I>WA, </I>CA)</B><BR>
      •How many drinks of alcohol do you have per week?<BR>
      •Are there times when you drink more than that?<BR>
      •Do you or your partner use recreational drugs?<BR>
      •Do you or your partner have a problem with alcohol or drugs?<BR>
      •Consider CAGE (<B>C</B>ut down, <B>A</B>nnoyed, <B>G</B>uilty, <B>E</B>ye
      opener)</FONT></TD>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"><%= props.getProperty("alcoholDrugAbuse", "") %>&nbsp;</FONT></TD>
  </TR>
  <TR>
    <TD colspan="2" VALIGN="TOP" bgcolor="#f9f9f9"><FONT FACE="Arial" SIZE="-1">
      FAMILY VIOLENCE</FONT><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"> <B>Woman or partner
      experienced or witnessed abuse (physical,&nbsp;emotional, sexual) (<I>CA,
      </I>WA)</B><BR>
      •What was your parents' relationship like?<BR>
      •Did your father ever scare or hurt your mother?<BR>
      •Did your parents ever scare or hurt you?<BR>
      •Were you ever sexually abused as a child?</FONT></TD>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"><%= props.getProperty("abuse", "") %>&nbsp;</FONT></TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"> <B>Current or past
      woman abuse (<I>WA</I>, CA, PD)</B><BR>
      •How do you and your partner solve arguments?<BR>
      •Do you ever feel frightened by what your partner says or does?<BR>
      •Have you ever been hit/pushed/slapped by a partner?<BR>
      •Has your partner ever humiliated you or psychologically abused you in other
      ways?<BR>
      •Have you ever been forced to have sex against your will?</FONT></TD>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"><%= props.getProperty("womanAbuse", "") %>&nbsp;</FONT></TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"> <B>Previous child
      abuse by woman or partner (<I>CA</I>)</B><BR>
      •Do you/your partner have children not living with you? If so, why?<BR>
      •Have you ever had involvement with a child protection agency (i.e., Children's
      Aid Society)?</FONT></TD>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"><%= props.getProperty("childAbuse", "") %>&nbsp;</FONT></TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"> <B>Child discipline
      (<I>CA</I>)</B><BR>
      •How were you disciplined as a child?<BR>
      •How do you think you will discipline your child?<BR>
      •How do you deal with your kids at home when they misbehave?</FONT></TD>
    <TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1"><%= props.getProperty("childDiscipline", "") %>&nbsp;</FONT></TD>
  </TR>
  <TR>
    <TD COLSPAN="2" bgcolor="#f9f9f9"><B><FONT FACE="Arial" SIZE="-2"> <font size="-1">FOLLOW-UP
      PLAN:</font></FONT></B></TD>
  </TR>
  <TR>
    <TD colspan="2" VALIGN="TOP"><FONT FACE="Arial" SIZE="-2">
	  <table border=0>
	      <tr>
      <td>
        <%
            if(props.getProperty("provCounselling", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td> Supportive counselling by provider</td>
      <td>
        <%
            if(props.getProperty("homecare", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Homecare</td>
      <td>
        <%
            if(props.getProperty("assaultedWomen", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Assaulted women's helpline / shelter / counseling</td>
    </tr>
    <tr>
      <td>
        <%
            if(props.getProperty("addAppts", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Additional prenatal appointments </td>
      <td>
        <%
            if(props.getProperty("parentingClasses", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Parenting classes / parents' support group</td>
      <td>
        <%
            if(props.getProperty("legalAdvice", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Legal advice</td>
    </tr>
    <tr>
      <td>
        <%
            if(props.getProperty("postpartumAppts", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Additional postpartum appointments</td>
      <td>
        <%
            if(props.getProperty("addictPrograms", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Addiction treatment programs</td>
      <td>
        <%
            if(props.getProperty("cas", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Children's Aid Society</td>
    </tr>
    <tr>
      <td>
        <%
            if(props.getProperty("babyVisits", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Additional well baby visits </td>
      <td>
        <%
            if(props.getProperty("quitSmoking", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Smoking cessation resources</td>
      <td>
        <%
            if(props.getProperty("other1", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Other:
        <%= props.getProperty("other1Name", "") %>&nbsp;
      </td>
    </tr>
    <tr>
      <td>
        <%
            if(props.getProperty("publicHealth", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Public Health referral </td>
      <td>
        <%
            if(props.getProperty("socialWorker", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Social Worker</td>
      <td>
        <%
            if(props.getProperty("other2", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Other:
        <%= props.getProperty("other2Name", "") %>&nbsp;
      </td>
    </tr>
    <tr>
      <td>
        <%
            if(props.getProperty("prenatalEdu", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Prenatal education services </td>
      <td>
        <%
            if(props.getProperty("psych", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Psychologist / Psychiatrist</td>
      <td>
        <%
            if(props.getProperty("other3", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Other:
        <%= props.getProperty("other3Name", "") %>&nbsp;
      </td>
    </tr>
    <tr>
      <td>
        <%
            if(props.getProperty("nutritionist", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Nutritionist </td>
      <td>
        <%
            if(props.getProperty("therapist", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Psychotherapist / marital / family therapist</td>
      <td>
        <%
            if(props.getProperty("other4", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Other:
        <%= props.getProperty("other4Name", "") %>&nbsp;
      </td>
    </tr>
    <tr>
      <td>
        <%
            if(props.getProperty("resources", "").equalsIgnoreCase("checked='checked'"))
            { %> <img src="graphics/checkmark.gif"> <%  }
            else
            { %> <img src="graphics/notChecked.gif"> <% }
        %>
      </td>
      <td>Community resources / mothers' group </td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr></table>

	</FONT></TD>
  </TR>
  <TR>
    <TD VALIGN="TOP" COLSPAN=2>
      <p><FONT FACE="Arial" SIZE="-1"> <B>COMMENTS:<BR>
        </B></FONT></p>
      <p><%= props.getProperty("comments", "") %>&nbsp;</p>
      <BR>
	</td></tr>
  <tr>
    <td> Date Completed&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
    <td>Signature : </td>
  </tr></table>
  <table border="0"><tr><td>
      <FONT FACE="Arial" SIZE="-1"><P> <FONT SIZE="-2">Copyright &copy; ALPHA Project 1993 Version: May 1998<BR>
        *The ALPHA Guide is available through the Department of Family and Community
        Medicine, University of Toronto.</FONT>
      </FONT></TD>
  </TR>
</TABLE>
<P>

</BODY>
</HTML>
