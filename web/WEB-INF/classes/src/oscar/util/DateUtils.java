package oscar.util;

import org.apache.log4j.Category;

import java.text.*;
import java.text.DateFormat;
import java.text.ParseException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtils {
    static Category cat = Category.getInstance(DateUtils.class.getName());
    private static SimpleDateFormat sdf;
    private static String formatDate = "dd/MM/yyyy";
    
    public static SimpleDateFormat getDateFormatter() {
        if (sdf == null) {
            sdf = new SimpleDateFormat(formatDate);
        }

        return sdf;
    }

    public static void setDateFormatter(String pattern) {
        sdf = new SimpleDateFormat(pattern);
    }

    public static String getDate() {
        Date date = new Date();

        return DateFormat.getDateInstance().format(date);
    }

	public static String getDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		return sdf.format(date);
	}

    public static String getDateTime() {
        Date date = new Date();

        return DateFormat.getDateTimeInstance().format(date);
    }

    /** Compara uma data com a data atual.
     * @param pDate Data que será comparada com a data atual.
     * @param format Formato da data. Ex: dd/MM/yyyy, yyyy-MM-dd
     * @return  1 - se a data for maior que a data atual.
     * -1 - se a data for menor que a data atual.
     * 0 - se a data for igual que a data atual.
     * -2 - se ocorrer algum erro.
     */
    public static String compareDate(String pDate, String format) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

        try {
            Date date = df.parse(pDate);
            cat.debug("[DateUtils] - compareDate: date = " + date.toString());

            String sNow = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());
            Date now = df.parse(sNow);
            cat.debug("[DateUtils] - compareDate: now = " + now.toString());

            if (date.after(now)) {
                cat.debug("[DateUtils] - compareDate: 1");

                return "1";
            } else if (date.before(now)) {
                cat.debug("[DateUtils] - compareDate: -1");

                return "-1";
            } else {
                cat.debug("[DateUtils] - compareDate: 0");

                return "0";
            }
        } catch (ParseException e) {
            cat.error("[DateUtils] - compareDate: -2", e);

            return "-2";
        }
    }

	/** Compara uma data com outra.
	 * @param pDate Data que será comparada com pDate2.
	 * * @param pDate2 Data que será comparada com pDate.
	 * @param format Formato da data. Ex: dd/MM/yyyy, yyyy-MM-dd
	 * @return  1 - se a data for maior que a data atual.
	 * -1 - se a data for menor que a data atual.
	 * 0 - se a data for igual que a data atual.
	 * -2 - se ocorrer algum erro.
	 */
	public static String compareDate(String pDate, String pDate2, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);

		try {
			Date date = df.parse(pDate);
			cat.debug("[DateUtils] - compareDate: date = " + date.toString());

			//String sNow = df.format(df.parse(pDate2));
			Date now = df.parse(pDate2);
			cat.debug("[DateUtils] - compareDate: now = " + now.toString());

			if (date.after(now)) {
				cat.debug("[DateUtils] - compareDate: 1");

				return "1";
			} else if (date.before(now)) {
				cat.debug("[DateUtils] - compareDate: -1");

				return "-1";
			} else {
				cat.debug("[DateUtils] - compareDate: 0");

				return "0";
			}
		} catch (ParseException e) {
			cat.error("[DateUtils] - compareDate: -2", e);

			return "-2";
		}
	}

    public static String formatDate(String date, String format,
        String formatAtual) {
        try {
            setDateFormatter(formatAtual);

            Date data = getDateFormatter().parse(date);
            cat.debug("[DateUtils] - formatDate: data formatada: " +
                getDateFormatter().format(data));
            setDateFormatter(format);

            return getDateFormatter().format(data);
        } catch (ParseException e) {
            cat.error("[DateUtils] - formatDate: ", e);
        }

        return "";
    }

	public static String formatDate(String date, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();

			Date data = sdf.parse(date);
			cat.debug("[DateUtils] - formatDate: data formatada: " +
			sdf.format(data));
			
			setDateFormatter(format);

			return getDateFormatter().format(data);
		} catch (ParseException e) {
			cat.error("[DateUtils] - formatDate: ", e);
		}

		return "";
	}


    public static String sumDate(String format, String pSum) {

        int iSum = new Integer(pSum).intValue();
        cat.debug("[DateUtils] - sumDate: iSum = " + iSum);

		Calendar calendar = new GregorianCalendar();
		Date now = new Date();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, iSum);
		Date data = calendar.getTime();
		
		setDateFormatter(format);
		
		return getDateFormatter().format(data);

    }
}
