/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.form.pdfservlet;

import java.util.Properties;

public class ONAR2EnhancedPostProcessor implements FrmPDFPostValueProcessor {

	@Override
	public Properties process(Properties p) {
		
		if(p.getProperty("ar2_strep","").equals("NDONE")) {p.setProperty("ar2_strep", "");}
		if(p.getProperty("ar2_strep","").equals("POSSWAB")) {p.setProperty("ar2_strep", "+ve swab");}
		if(p.getProperty("ar2_strep","").equals("POSURINE")) {p.setProperty("ar2_strep", "+ve urine");}
		if(p.getProperty("ar2_strep","").equals("NEGSWAB")) {p.setProperty("ar2_strep", "-ve swab");}
		if(p.getProperty("ar2_strep","").equals("DONEUNK")) {p.setProperty("ar2_strep", "Done. Unknown");}
		if(p.getProperty("ar2_strep","").equals("UNK")) {p.setProperty("ar2_strep", "Unknown if screened");}

		
		return p;
	}

}
