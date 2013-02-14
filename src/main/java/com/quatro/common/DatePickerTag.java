/**
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 */
package com.quatro.common;

import java.util.Calendar;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.taglib.html.TextTag;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;

public class DatePickerTag extends BaseInputTag {
	private TextTag dtTextTag = new TextTag();
	private String style = null;
	private String styleClass = null;
	private boolean indexed = false;
	private String width = "100%";
	private String openerForm;
	private String openerElement;

	public void release() {
		dtTextTag = null;
		style = null;
		styleClass = null;
		indexed = false;
		width = null;
	}

	public int doStartTag() throws JspException {
		TagUtils.getInstance().write(this.pageContext, this.renderInputElement1());

		return (EVAL_BODY_AGAIN);
	}

	public int doEndTag() throws JspException {
		dtTextTag.doEndTag();
		return EVAL_PAGE;
	}

	protected String renderInputElement1() throws JspException {
		String sRootPath = "";
		try {
			sRootPath = Misc.getApplicationName(pageContext.getServletContext().getResource("/").getPath());
		} catch (Exception e) {
			MiscUtils.getLogger().debug("Not sure if this is okay or not, some one else left this blank.", e);
		}

		StringBuffer results = new StringBuffer("<table");
		prepareAttribute(results, "cellpadding", "0");
		prepareAttribute(results, "style", "border:0px;");
		prepareAttribute(results, "cellspacing", "0");
		prepareAttribute(results, "width", getWidth());
		results.append(this.getElementClose());
		results.append("<tr>");
		results.append("<td");
		prepareAttribute(results, "style", "border:0px;");
		results.append(this.getElementClose());

		results.append("<input");
		prepareAttribute(results, "style", "width:100%;");
		prepareAttribute(results, "type", "text");
		String sName = prepareName(property, name);
		prepareAttribute(results, "name", sName);
		prepareAttribute(results, "maxlength", "10");
		prepareAttribute(results, "tabindex", getTabindex());
		prepareAttribute(results, "onblur", "onCalBlur('" + sName + "');");
		prepareAttribute(results, "onkeypress", "onCalKeyPress(event,'" + sName + "');");
		prepareValue(results);
		results.append(this.prepareEventHandlers());
		results.append(this.prepareStyles());
		prepareOtherAttributes(results);
		results.append(this.getElementClose());
		results.append("</td>");

		results.append("<td");
		prepareAttribute(results, "style", "border:0px;display:none");
		prepareAttribute(results, "width", "1px");
		results.append(this.getElementClose());

		results.append("<input");
		prepareAttribute(results, "style", "width:1px;display:none");
		prepareAttribute(results, "type", "text");
		String sName1 = prepareName(property, name) + "_cal1";
		prepareAttribute(results, "name", sName1);
		prepareAttribute(results, "maxlength", "1px");
		prepareAttribute(results, "tabindex", getTabindex());
		prepareValue(results);
		results.append(this.prepareEventHandlers());
		results.append(this.prepareStyles());
		prepareOtherAttributes(results);
		results.append(this.getElementClose());
		results.append("</td>");

		results.append("<td");
		prepareAttribute(results, "style", "border:0px");
		prepareAttribute(results, "width", "1px");
		results.append(this.getElementClose());
		results.append("<a href='javascript:void1();' ");

		Calendar rightNow = Calendar.getInstance();
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH) + 1;

		prepareAttribute(results, "onclick", "return openDatePickerCalendar('/" + sRootPath + "/calendar/CalendarPopup.jsp?" + "openerForm=" + openerForm + "&openerElement=" + sName + "&year=" + year + "&month=" + month + "');");

		results.append(this.getElementClose());
		results.append("<img");
		prepareAttribute(results, "src", "/" + sRootPath + "/images/timepicker.jpg");
		prepareAttribute(results, "border", "0");
		results.append(this.prepareStyles());
		results.append(this.getElementClose());
		results.append("</a>");
		results.append("</td></tr></table>");

		return results.toString();
	}

	protected void prepareValue(StringBuffer results) throws JspException {
		results.append(" value=\"");
		if (value != null) {
			results.append(value);
		} else {
			Object value = TagUtils.getInstance().lookup(pageContext, name, property, null);
			results.append(this.formatValue(value));
		}
		results.append('"');
	}

	protected String prepareName(String property, String pre_name) throws JspException {
		if (property == null) return null;

		if (indexed) {
			StringBuffer results = new StringBuffer();
			prepareIndex(results, pre_name);
			results.append(property);
			return results.toString();
		}
		return property;
	}

	protected String formatValue(Object value) {
		if (value == null) return "";

		String value2 = TagUtils.getInstance().filter(value.toString());
		return value2.replace('-', '/');
	}

	public TextTag getDtTextTag() {
		return dtTextTag;
	}

	public void setDtTextTag(TextTag dtTextTag) {
		this.dtTextTag = dtTextTag;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getOpenerElement() {
		return openerElement;
	}

	public void setOpenerElement(String openerElement) {
		this.openerElement = openerElement;
	}

	public String getOpenerForm() {
		return openerForm;
	}

	public void setOpenerForm(String openerForm) {
		this.openerForm = openerForm;
	}

}
