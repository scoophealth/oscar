package oscar;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import oscar.oscarSecurity.*;                                                                                                                                      
public final class CheckSession implements Filter {

    private FilterConfig filterConfig = null;
                                                                                                                                      
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException {
     
            // System.out.println("Checking the session");
	    oscar.oscarSecurity.CookieSecurity cs   = new oscar.oscarSecurity.CookieSecurity(); 
          
            if( !cs.FindThisCookie(((HttpServletRequest)request).getCookies(), cs.providerCookie) && 
                !cs.FindThisCookie(((HttpServletRequest)request).getCookies(), cs.receptionistCookie) &&
                !cs.FindThisCookie(((HttpServletRequest)request).getCookies(), cs.adminCookie) ){ //pass security???
         
            PrintWriter out = response.getWriter();
                out.println("<html><head></head><body>");
                out.println("<h2>Sorry, you are not authorized to view this page.</h2>");
                out.println("</body></html>");
                out.flush();
                return;
	}
                                                                                                                                      
      chain.doFilter(request, response);
    }
                                                                                                                                      
                                                                                                                                      
    public void destroy() {
    }
                                                                                                                                      
                                                                                                                                      
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
                                                                                                                                      
}
                                                                                                                                      
