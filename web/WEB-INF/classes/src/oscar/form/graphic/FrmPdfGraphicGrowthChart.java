/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 */

package oscar.form.graphic;

import java.util.Properties;
import java.util.Vector;

/**
 * Class FrmPdfGraphicGrowthChart : all pdf class files are temp. Need OO design
 */
public final class FrmPdfGraphicGrowthChart {

    public static void main(String[] arg) {
        FrmPdfGraphicGrowthChart test = new FrmPdfGraphicGrowthChart();
        test.init(50, 50, 20, 40, 15, 40);
        test.getGraphicXYProp("12/08/2004", "18.5");
    }

    float fEndX = 0f;
    float fEndY = 0f;
    float fStartX = 0f;
    float fStartY = 0f;
    int nMaxPixX = 0;
    int nMaxPixY = 0;
    Properties prop = new Properties();

    public String[] getGraphicXYProp(String xDate, String yHeight) {
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
        System.out.println(xNum + " : " + yNum + "|" + deltaX + ":" + deltaY);
        return ret;
    }

    public Properties getGraphicXYProp(Vector xDate, Vector yHeight) {
        prop = new Properties();
        for (int i = 0; i < xDate.size(); i++) {
            System.out.println(i );
            if (xDate.get(i) != null && yHeight.get(i) != null && !"".equals((String) xDate.get(i))
                    && !"".equals((String) yHeight.get(i))) {
                System.out.println(i + " " + xDate.get(i) + " | " + yHeight.get(i));
                String[] xy = getGraphicXYProp((String) xDate.get(i), (String) yHeight.get(i));
                if (xy != null)
                    prop.setProperty(xy[0], xy[1]);
            }
        }
        return prop;
    }

    public void init(int mx, int my, float fsx, float fex, float fsy, float fey) {
        nMaxPixX = mx;
        nMaxPixY = my;
        fStartX = fsx;
        fEndX = fex;
        fStartY = fsy;
        fEndY = fey;
    }

    public void init(String mx, String my, String fsx, String fex, String fsy, String fey) {
        int tmx = isDigitNum(mx) ? Integer.parseInt(mx) : 0;
        int tmy = isDigitNum(my) ? Integer.parseInt(my) : 0;
        float tfsx = isDigitNum(fsx) ? Float.parseFloat(fsx) : 0f;
        float tfex = isDigitNum(fex) ? Float.parseFloat(fex) : 0f;
        float tfsy = isDigitNum(fsy) ? Float.parseFloat(fsy) : 0f;
        float tfey = isDigitNum(fey) ? Float.parseFloat(fey) : 0f;

        init(tmx, tmy, tfsx, tfex, tfsy, tfey);
    }

    private boolean isDigit(String str) {
        boolean ret = true;
        if (str == null || str.length() == 0)
            return false;

        int N = str.length();
        StringBuffer sb = new StringBuffer(N);
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
        //System.out.println("array length is:" + sb.length);
        return ret;
    }
}