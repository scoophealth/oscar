
package oscar.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;


public class FullPathReWrite extends TagSupport {

    

    /**
     * The server name to use instead of request.getServerName().
     */
    protected String server = null;

    /**
     * The target window for this base reference.
     */
    protected String jspPage = null;

    public String getJspPage() {
        return (this.jspPage == null ) ? "" : this.jspPage;
    }

    public void setJspPage(String jspPage) {
        this.jspPage = jspPage;
    }

    /**
     * Process the start of this tag.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String serverName = (this.server == null) ? request.getServerName() : this.server;
        
        String temp = request.getRequestURI();
        int last = temp.lastIndexOf('/');
        String path = temp.substring(0,last);
        
        
        
        String returnTag =  request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"+getJspPage();
        
        JspWriter out = pageContext.getOut();
        try {
            out.write(returnTag);
        } catch (IOException e) {            
            throw new JspException(e.toString());
        }
        
        return EVAL_BODY_INCLUDE;
    }

    
    /**
     * Returns the server.
     * @return String
     */
    public String getServer() {
        return this.server;
    }

    /**
     * Sets the server.
     * @param server The server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

}
