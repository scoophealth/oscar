package oscar.util;


/**
 * @author administrador
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PagerDef {
    public int offset = 0;
    public int length = 0;
    public String pageOffset = "";
    public String url = "";
    public String pagerHeader = "";

    /**
     * Constructor for PagerDef.
     */
    public PagerDef(int offset, int length, String pageOffset, String url,
        String pagerHeader) {
        this.offset = offset;
        this.length = length;
        this.pageOffset = pageOffset;
        this.url = url;
        this.pagerHeader = pagerHeader;
    }
}
