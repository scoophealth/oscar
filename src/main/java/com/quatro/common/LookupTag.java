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

/*
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.taglib.html.*;
*/
import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.BaseInputTag;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;

public class LookupTag extends BaseInputTag {
	//	private String name=null;
	private String formProperty = null;
	private String codeProperty = null;
	private String bodyProperty = null;

	private String codeValue = null;
	private String bodyValue = null;

	private String bodyStyle = null;
	private String bodyStyleClass = null;

	private boolean indexed = false;
	private boolean showCode = true;
	private boolean showBody = true;

	private String width = "100%";
	private String codeWidth = "30%";
	private String codeMaxlength = null;
	private String bodyMaxlength = null;
	private String tableName = null;
	protected String accept = null;

	public int doStartTag() throws JspException {
		TagUtils.getInstance().write(this.pageContext, this.renderInputElement());
		return (EVAL_BODY_AGAIN);
	}

	protected String renderInputElement() throws JspException {
		String sRootPath = "";
		try {
			//        	ServletRequest request =  pageContext.getRequest();
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
		if (showCode == true) {
			prepareAttribute(results, "width", getCodeWidth());
		} else {
			prepareAttribute(results, "width", "1px");
		}
		results.append(this.getElementClose());
		results.append("<input");
		if (showCode == true) {
			prepareAttribute(results, "style", "width:100%;");
		} else {
			prepareAttribute(results, "style", "width:1px;");
		}
		prepareAttribute(results, "type", "text");
		prepareAttribute(results, "name", prepareName(codeProperty, name));
		prepareAttribute(results, "accesskey", getAccesskey());
		prepareAttribute(results, "accept", getAccept());
		prepareAttribute(results, "maxlength", getCodeMaxlength());
		prepareAttribute(results, "tabindex", getTabindex());
		prepareCodeValue(results);
		results.append(this.prepareEventHandlers());
		results.append(this.prepareStyles());
		prepareOtherAttributes(results);
		results.append(this.getElementClose());
		results.append("</td>");

		results.append("<td");
		prepareAttribute(results, "style", "border:0px;");
		results.append(this.getElementClose());
		results.append("<input");
		if (showBody == true) {
			prepareAttribute(results, "style", "width:100%;");
		} else {
			prepareAttribute(results, "style", "width:1px;");
		}
		prepareAttribute(results, "type", "text");
		prepareAttribute(results, "name", prepareName(bodyProperty, name));
		prepareAttribute(results, "maxlength", getBodyMaxlength());
		prepareBodyValue(results);
		results.append(this.prepareBodyStyles());
		results.append(" readonly ");
		results.append(this.getElementClose());
		results.append("</td>");

		results.append("<td");
		prepareAttribute(results, "style", "border:0px;");
		prepareAttribute(results, "width", "70px");
		results.append(this.getElementClose());
		results.append("<a ");
		prepareAttribute(results, "onclick", "return showLookup('" + tableName + "', '', '', " + "'" + formProperty + "','" + prepareName(codeProperty, name) + "','" + prepareName(bodyProperty, name) + "', true, '" + sRootPath + "');");
		prepareAttribute(results, "href", "javascript:void1();");
		results.append(this.getElementClose());
		results.append("<img");
		prepareAttribute(results, "src", "/" + sRootPath + "/images/microsoftsearch.gif");
		prepareAttribute(results, "border", "0");
		results.append(this.getElementClose());
		results.append("</a>");

		results.append("<a ");
		prepareAttribute(results, "onclick", "return clearLookupValue('" + formProperty + "','" + prepareName(codeProperty, name) + "','" + prepareName(bodyProperty, name) + "');");
		prepareAttribute(results, "href", "javascript:void1();");
		results.append(this.getElementClose());

		results.append("<img");
		prepareAttribute(results, "src", "/" + sRootPath + "/images/Reset16.gif");
		prepareAttribute(results, "border", "0");
		results.append(this.getElementClose());

		results.append("</a>");

		results.append("</td></tr></table>");

		return results.toString();
	}

	protected String prepareBodyStyles() {
		StringBuffer styles = new StringBuffer();
		prepareAttribute(styles, "style", bodyStyle);
		prepareAttribute(styles, "class", bodyStyleClass);
		return styles.toString();
	}

	protected void prepareCodeValue(StringBuffer results) throws JspException {
		results.append(" value=\"");
		if (codeValue != null) {
			results.append(codeValue);
		} else {
			Object value = TagUtils.getInstance().lookup(pageContext, name, codeProperty, null);
			results.append(this.formatValue(value));
		}
		results.append('"');
	}

	protected void prepareBodyValue(StringBuffer results) throws JspException {
		results.append(" value=\"");
		if (bodyValue != null) {
			results.append(bodyValue);
		} else {
			Object value = TagUtils.getInstance().lookup(pageContext, name, bodyProperty, null);
			results.append(this.formatValue(value));
		}
		results.append('"');
	}

	protected String formatValue(Object value) {
		if (value == null) return "";
		return TagUtils.getInstance().filter(value.toString());
	}

	protected String prepareName(String property, String pre_name) throws JspException {
		if (property == null) return null;

		if (indexed) {
			StringBuffer results = new StringBuffer();
			prepareIndex(results, pre_name);
			results.append(property);
			return results.toString();
		}
		return pre_name + "_" + property;
	}

	/*	
		public int doEndTag() throws JspException {
			return EVAL_PAGE;
		}
	*/
	public void release() {
		name = null;
		codeProperty = null;
		bodyProperty = null;
		codeValue = null;
		bodyValue = null;
		bodyStyle = null;
		bodyStyleClass = null;
		indexed = false;
		showCode = true;
		showBody = true;
		codeWidth = null;
		codeMaxlength = null;
		width = null;
		bodyMaxlength = null;
		tableName = null;
	}

	public boolean isShowCode() {
		return showCode;
	}

	public void setShowCode(boolean showCode) {
		this.showCode = showCode;
	}

	public boolean isShowBody() {
		return showBody;
	}

	public void setShowBody(boolean showBody) {
		this.showBody = showBody;
	}

	public String getBodyProperty() {
		return bodyProperty;
	}

	public void setBodyProperty(String bodyProperty) {
		this.bodyProperty = bodyProperty;
	}

	public String getCodeProperty() {
		return codeProperty;
	}

	public void setCodeProperty(String codeProperty) {
		this.codeProperty = codeProperty;
	}

	public String getBodyMaxlength() {
		return bodyMaxlength;
	}

	public void setBodyMaxlength(String bodyMaxlength) {
		this.bodyMaxlength = bodyMaxlength;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getCodeMaxlength() {
		return codeMaxlength;
	}

	public void setCodeMaxlength(String codeMaxlength) {
		this.codeMaxlength = codeMaxlength;
	}

	public String getCodeWidth() {
		return codeWidth;
	}

	public void setCodeWidth(String codeWidth) {
		this.codeWidth = codeWidth;
	}

	public String getBodyValue() {
		return bodyValue;
	}

	public void setBodyValue(String bodyValue) {
		this.bodyValue = bodyValue;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}

	public String getBodyStyleClass() {
		return bodyStyleClass;
	}

	public void setBodyStyleClass(String bodyStyleClass) {
		this.bodyStyleClass = bodyStyleClass;
	}

	public String getBodyStyle() {
		return bodyStyle;
	}

	public void setBodyStyle(String bodyStyle) {
		this.bodyStyle = bodyStyle;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFormProperty() {
		return formProperty;
	}

	public void setFormProperty(String formProperty) {
		this.formProperty = formProperty;
	}

}
