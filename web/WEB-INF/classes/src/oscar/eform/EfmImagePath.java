/*
 * 
 */


package oscar.eform;

/**
 * Class EfmImageDir is a data class to store the eform image path
 * 2002-12-31
*/
public class EfmImagePath {
    static String eformImagePath = "";

    public static void setEfmImagePath(String s) {
        eformImagePath = s;
    }

    public static String getEfmImagePath() {
        return eformImagePath;
    }
}
