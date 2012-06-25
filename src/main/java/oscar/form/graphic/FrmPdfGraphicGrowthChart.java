
package oscar.form.graphic;

import java.util.List;
import java.util.Properties;


/**
 * Class FrmPdfGraphicGrowthChart : all pdf class files are temp. Need OO design
 */
public final class FrmPdfGraphicGrowthChart extends FrmPdfGraphic {   

    float fEndX = 0f;
    float fEndY = 0f;
    float fStartX = 0f;
    float fStartY = 0f;
    int nMaxPixX = 0;
    int nMaxPixY = 0;
    Properties prop = new Properties();

    private String[] getGraphicXYProp(String xDate, String yHeight) {
        String[] ret = new String[2];
        float xNum = Float.parseFloat(xDate);
        float yNum = Float.parseFloat(yHeight);

        if (xNum < fStartX || xNum > fEndX || yNum < fStartY || yNum > fEndY)
            return null;

        float deltaX = nMaxPixX / (fEndX - fStartX);
        float deltaY = nMaxPixY / (fEndY - fStartY);
        xNum = deltaX * (xNum - fStartX);
        yNum = deltaY * (yNum - fStartY);

        ret[0] = "" + xNum;
        ret[1] = "" + yNum;
        
        return ret;
    }

    public Properties getGraphicXYProp(List xDate, List yHeight) {
        prop = new Properties();
        for (int i = 0; i < xDate.size(); i++) {

            if (xDate.get(i) != null && yHeight.get(i) != null && !"".equals(xDate.get(i))
                    && !"".equals(yHeight.get(i))) {

                String[] xy = getGraphicXYProp((String) xDate.get(i), (String) yHeight.get(i));
                if (xy != null)
                    prop.setProperty(xy[0], xy[1]);
            }
        }
        return prop;
    }

    public void init(Properties prop) {
        String str = prop.getProperty("__nMaxPixX");
        nMaxPixX = isDigitNum(str) ? Integer.parseInt(str) : 0;
        
        str = prop.getProperty("__nMaxPixY");
        nMaxPixY = isDigitNum(str) ? Integer.parseInt(str) : 0;
        
        str = prop.getProperty("__fStartX");
        fStartX = isDigitNum(str) ? Float.parseFloat(str) : 0f;
        
        str = prop.getProperty("__fEndX");
        fEndX = isDigitNum(str) ? Float.parseFloat(str) : 0f;
        
        str = prop.getProperty("__fStartY");
        fStartY = isDigitNum(str) ? Float.parseFloat(str) : 0f;
        
        str = prop.getProperty("__fEndY");
        fEndY = isDigitNum(str) ? Float.parseFloat(str) : 0f;
    }   

    private boolean isDigit(String str) {
        boolean ret = true;
        if (str == null || str.length() == 0)
            return false;

        int N = str.length();
        StringBuilder sb = new StringBuilder(N);
        for (int i = 0; i < N; i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9')
                continue;
            else {
                ret = false;
                break;
            }
        }
        return ret;
    }

    private boolean isDigitNum(String str) {
        boolean ret = true;
        if (str == null || str.length() == 0)
            return false;

        String[] sb = str.split("[0-9.]");
        if (sb.length > 0) {
            ret = false;
        }

        return ret;
    }
}
