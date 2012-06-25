
/*
 * java oscar.form.graphic.FrmPdfGraphicAR
 */

package oscar.form.graphic;

import java.util.List;
import java.util.Properties;

import oscar.util.UtilDateUtilities;

/**
 * Class FrmPdfGraphicAR : bcar2 week-height plot data 2004-01-30
 */
public final class FrmPdfGraphicAR extends FrmPdfGraphic {

	public static void main(String[] arg) {
		FrmPdfGraphicAR test = new FrmPdfGraphicAR();
		test.init(50, 50, 20, 40, 15, 40, "dd/MM/yyyy", "10/12/2004");
		test.getGraphicXYProp("12/08/2004", "18.5");
	}

	String dateFormat = null;
	String fEDB = null;
	float fEndX = 0f;
	float fEndY = 0f;

	FrmGraphicAR frmGrp = new FrmGraphicAR();
	float fStartX = 0f;
	float fStartY = 0f;

	int nMaxPixX = 0;
	int nMaxPixY = 0;
	Properties prop = new Properties();

	//only for "yyyy MM dd"
	public boolean checkDateStr(String strDate) {
		boolean ret = true;
		if (dateFormat == null || strDate == null || strDate.equals("") )
			return false;

		//find the sep
		String[] sep = dateFormat.split("[a-zA-Z]");
		for (int i = 0; i < sep.length; i++) {
			String[] part = strDate.split(sep[i], 2);
			if (!isDigit(part[0])) {
				ret = false;
				break;
			}

			if (part.length > 1)
				strDate = part[1];
		}
		return true;
	}

	public void getGraphicXYProp(String xDate, String yHeight) {
		if (!checkDateStr(xDate) || !isDigitNum(yHeight))
			return;

		if (checkDateStr(xDate)) {
			xDate =
				UtilDateUtilities.DateToString(
					UtilDateUtilities.StringToDate(xDate, dateFormat),
					"yyyy-MM-dd");
		} else {
			return;
		}

		float xNum = frmGrp.getWeekByEDB(fEDB, xDate);
		float yNum = Float.parseFloat(yHeight);

		if (xNum < fStartX || xNum > fEndX || yNum < fStartY || yNum > fEndY)
			return;

		float deltaX = nMaxPixX / (fEndX - fStartX);
		float deltaY = nMaxPixY / (fEndY - fStartY);
		xNum = deltaX * (xNum - fStartX);
		yNum = deltaY * (yNum - fStartY);

		prop.setProperty("" + xNum, "" + yNum);
		return;
	}

	public Properties getGraphicXYProp(List xDate, List yHeight) {
		prop = new Properties();
		for (int i = 0; i < xDate.size(); i++) {
			if (xDate.get(i) != null && yHeight.get(i) != null) {
				getGraphicXYProp(
					(String) xDate.get(i),
					(String) yHeight.get(i));
			}
		}

		return prop;
	}

	public void init(
		int mx,
		int my,
		float fsx,
		float fex,
		float fsy,
		float fey,
		String df,
		String fedb) {
		nMaxPixX = mx;
		nMaxPixY = my;
		fStartX = fsx;
		fEndX = fex;
		fStartY = fsy;
		fEndY = fey;

		dateFormat = df;                
		fEDB =
			checkDateStr(fedb)
				? UtilDateUtilities.DateToString(
					UtilDateUtilities.StringToDate(fedb, dateFormat),
					"yyyy-MM-dd")
				: "0000-00-00";                
	}

        
        public void init(Properties prop){
              init(prop.getProperty("__nMaxPixX"),
                   prop.getProperty("__nMaxPixY"),
                   prop.getProperty("__fStartX"),
                   prop.getProperty("__fEndX"),
                   prop.getProperty("__fStartY"),
                   prop.getProperty("__fEndY"),
                   prop.getProperty("__dateFormat"),
                   prop.getProperty("__finalEDB"));
             
        }
        
        
        
	public void init(
		String mx,
		String my,
		String fsx,
		String fex,
		String fsy,
		String fey,
		String df,
		String fedb) {
		int tmx = isDigitNum(mx) ? Integer.parseInt(mx) : 0;
		int tmy = isDigitNum(my) ? Integer.parseInt(my) : 0;
		float tfsx = isDigitNum(fsx) ? Float.parseFloat(fsx) : 0f;
		float tfex = isDigitNum(fex) ? Float.parseFloat(fex) : 0f;
		float tfsy = isDigitNum(fsy) ? Float.parseFloat(fsy) : 0f;
		float tfey = isDigitNum(fey) ? Float.parseFloat(fey) : 0f;

		init(tmx, tmy, tfsx, tfex, tfsy, tfey, df, fedb);
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
