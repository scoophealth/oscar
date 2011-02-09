
package oscar.oscarTags;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.oscarehr.util.MiscUtils;

import oscar.util.SqlUtilBaseS;

public class TagUtil extends SqlUtilBaseS {
    
    public static ArrayList getIds(String tagName, String className) {
        ArrayList ids = new ArrayList();
        if (className.equals("EDoc")) {
            String sql = "SELECT m.* FROM tagmap m, tagnames n " +
                    "WHERE n.tagname='" + tagName + "' AND m.objectclass='" + className + "' AND n.tagid=m.tagid";
            ResultSet rs = getSQL(sql);
            String id = "";
            try {
                while (rs.next()) {
                    id = rsGetString(rs, "objectid");
                    ids.add(id);
                }
            } catch (SQLException sqe) {
                MiscUtils.getLogger().error("Error", sqe);    
            }
            return ids;
        } else {
            return null;
        }
    }
}
