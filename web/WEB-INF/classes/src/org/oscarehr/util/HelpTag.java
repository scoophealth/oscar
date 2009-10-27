//Error reading included file Templates/Classes/../Licenses/license-MyLicense.txt
package org.oscarehr.util;

import java.util.Locale;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.Globals;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.util.MessageResources;

/**
 * Creates a anchor tag that pops up a help window to
 * 
 * @author jaygallagher
 */
public class HelpTag extends TagSupport {

    private static MessageResources messages =MessageResources.getMessageResources("org.apache.struts.taglib.bean.LocalStrings");

    /**
     * @return the messages
     */
    public static MessageResources getMessages() {
        return messages;
    }

    /**
     * @param aMessages the messages to set
     */
    public static void setMessages(MessageResources aMessages) {
        messages = aMessages;
    }


    private String keywords = null;
    private String searchLink = "http://www.oscarmanual.org/system/app/pages/search?q=%s&scope=search-site";
    private String classString= null;
    private String key= null;
    private String style=null;

    private String getURL(){
        return getSearchLink().replaceAll("%s",keywords);
    }

    private String getStyleOut(){
        if(style == null) return "";
        return " style=\""+style+"\" ";
    }

    private String getClassStringOut(){
        if(classString == null) return "";
        return " class=\""+classString+"\" ";
    }


    protected String bundle = null;
    protected String localeKey = Globals.LOCALE_KEY;


    public int doStartTag() throws JspException {
        try {



             Object args[] = new Object[] {  };

         // Retrieve the message string we are looking for
         String message = TagUtils.getInstance().message(
                 pageContext,
                 this.bundle,
                 this.localeKey,
                 key,
                 args);

         if (message == null) {
             JspException e =
                 new JspException(
                     messages.getMessage("message.message", "\"" + key + "\""));
             TagUtils.getInstance().saveException(pageContext, e);
             throw e;
         }

         //TagUtils.getInstance().write(pageContext, message);




            JspWriter out = super.pageContext.getOut();
            out.print("<a "+getStyleOut()+" "+getClassStringOut()+" target=\"_blank\" href=\""+getURL()+"\">"+message);
        } catch (Exception p) {
            p.printStackTrace(System.out);
        }
        return (EVAL_BODY_INCLUDE);
    }

    public int doEndTag() throws JspException {
        try {
            JspWriter out = super.pageContext.getOut();
            out.print("</a>");
        } catch (Exception p) {
            p.printStackTrace(System.out);
        }
        return EVAL_PAGE;
    }


    /**
     * @return the keyword
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeywords(String keyword) {
        this.keywords = keyword;
    }

    /**
     * @return the searchLink
     */
    public String getSearchLink() {
        return searchLink;
    }

    /**
     * @param searchLink the searchLink to set
     */
    public void setSearchLink(String searchLink) {
        this.searchLink = searchLink;
    }

    /**
     * @return the classString
     */
    public String getClassString() {
        return classString;
    }

    /**
     * @param classString the classString to set
     */
    public void setClassString(String classString) {
        this.classString = classString;
    }

    /**
     * @param message the message to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }
}


