package com.quatro.common;

import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.*;
import javax.servlet.jsp.JspException;
import oscar.Misc;
import java.util.Calendar;

public class DatePickerTag extends BaseInputTag{
	private TextTag dtTextTag = new TextTag();
	private String name=null;
	private String property=null;
	private String value=null;
	private String style=null;
	private String styleClass=null;
	private boolean indexed = false;
	private String width="100%";
	private String openerForm;
	private String openerElement;
	
	public void release() {
	   dtTextTag = null;
	   name=null;
	   property=null;
	   value=null;
	   style=null;
	   styleClass=null;
	   indexed = false;
	   width=null;
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
        String sRootPath="";
        try{
        	sRootPath=Misc.getApplicationName(pageContext.getServletContext().getResource("/").getPath());
	    } catch (Exception e) {}

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
        String sName=prepareName(property, name);
        prepareAttribute(results, "name", sName);
        prepareAttribute(results, "maxlength", "10");
        prepareAttribute(results, "tabindex", getTabindex());
        prepareValue(results, value);
        results.append(this.prepareEventHandlers());
        results.append(this.prepareStyles());
        prepareOtherAttributes(results);
        results.append(this.getElementClose());
        
        results.append("<td");
        prepareAttribute(results, "style", "border:0px;");
        prepareAttribute(results, "width", "28px");
        results.append(this.getElementClose());
        results.append("<a ");

        Calendar rightNow = Calendar.getInstance();
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH) + 1;

        prepareAttribute(results, "onclick", "window.open('/" + sRootPath + "/calendar/oscarCalendarPopup.jsp?" + 
          "type=caisi&openerForm="+ openerForm + "&openerElement=" + sName + "&year=" + year + 
          "&month=" + month +"', '', 'width=300,height=300');");        
        results.append(this.getElementClose());
        results.append("<img");
       	prepareAttribute(results, "src", "/" + sRootPath + "/images/timepicker.jpg");
        results.append(this.prepareStyles());
        results.append(this.getElementClose());
        results.append("</a>");
        results.append("</td></tr></table>");

        return results.toString();
    }
    
    protected void prepareValue(StringBuffer results, String value) throws JspException {
        results.append(" value=\"");
        if (value != null) results.append(value);
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
