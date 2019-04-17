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
package org.oscarehr.rx.util;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.oscarehr.common.model.Drug;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

public class RxUtil {

	public static String trimSpecial(Drug rx) {
		String special = rx.getSpecial();
		if (special == null || special.trim().length() == 0) return "";

		//if rx has special instruction, remove it from special
		if (rx.getSpecialInstruction() != null && !rx.getSpecialInstruction().equalsIgnoreCase("null") && rx.getSpecialInstruction().trim().length() > 0) {
			special = special.replace(rx.getSpecialInstruction(), "");
		}

		//remove Qty:num
		String regex1 = "Qty:\\s*[0-9]*\\.?[0-9]*\\s*";
		String unitName = rx.getUnitName();
		if (unitName != null && special.indexOf(unitName) != -1) {
			regex1 += "\\Q" + unitName + "\\E";
		}
		Pattern p = Pattern.compile(regex1);
		Matcher m = p.matcher(special);
		special = m.replaceAll("");

		//remove Repeats:num from special
		String regex2 = "Repeats:\\s*[0-9]*\\.?[0-9]*\\s*";
		p = Pattern.compile(regex2);
		m = p.matcher(special);
		special = m.replaceAll("");

		//remove brand name
		String regex3 = rx.getBrandName();
		if (regex3 != null) {
			regex3 = regex3.trim();
			special = special.replace(regex3, "");
		}

		//remove generic name
		String regex4 = rx.getGenericName();
		if (regex4 != null) {
			regex4 = regex4.trim();
			special = special.replace(regex4, "");
		}

		//remove custom name
		String regex5 = rx.getCustomName();
		if (regex5 != null) {
			regex5 = regex5.trim();
			special = special.replace(regex5, "");
		}
		MiscUtils.getLogger().debug("before trimming mitte=" + special);
		String regex6 = "Mitte:\\s*[0-9]+\\s*\\w+";
		p = Pattern.compile(regex6);
		m = p.matcher(special);
		special = m.replaceAll("");
		MiscUtils.getLogger().debug("after trimming mitte special=" + special);
		//assume drug name is before method and drug name is the first part of the instruction.
		String rx_enhance = OscarProperties.getInstance().getProperty("rx_enhance");
		//rx_enhance changes the behavior by not deleting anything up to the words Take, apply..
		if (!(rx_enhance != null && rx_enhance.equals("true"))) {
			if (special.indexOf("Take") != -1) {
				special = special.substring(special.indexOf("Take"));
			} else if (special.indexOf("take") != -1) {
				special = special.substring(special.indexOf("take"));
			} else if (special.indexOf("TAKE") != -1) {
				special = special.substring(special.indexOf("TAKE"));
			} else if (special.indexOf("Apply") != -1) {
				special = special.substring(special.indexOf("Apply"));
			} else if (special.indexOf("apply") != -1) {
				special = special.substring(special.indexOf("apply"));
			} else if (special.indexOf("APPLY") != -1) {
				special = special.substring(special.indexOf("APPLY"));
			} else if (special.indexOf("Rub well in") != -1) {
				special = special.substring(special.indexOf("Rub well in"));
			} else if (special.indexOf("rub well in") != -1) {
				special = special.substring(special.indexOf("rub well in"));
			} else if (special.indexOf("RUB WELL IN") != -1) {
				special = special.substring(special.indexOf("RUB WELL IN"));
			} else if (special.indexOf("Rub Well In") != -1) {
				special = special.substring(special.indexOf("Rub Well In"));
			}
		}

		return special.trim();

	}
	
	public static BufferedImage getWaterMarkImage(String text) {
		
		int width = 200;
		int height = 100;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = img.createGraphics();
        
        String[] lines = text.split("\n");
        height = g.getFontMetrics().getHeight() * lines.length + 20;
        FontMetrics fm = g.getFontMetrics();
        for(String line : lines) {
        		if(width < fm.stringWidth(line)) {
        			width = fm.stringWidth(line);
        		}
        }

        img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        g = img.createGraphics();
        
        
        // white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // draw black circle around clock
        g.setColor(new Color(230,230,230));

        fm = g.getFontMetrics();
        
        int y = 10;//; ((height - fm.getHeight()) / 2) + fm.getDescent();
        //g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(-45), 150, y));
        //g.drawString(text, x, y);
        for (String line : text.split("\n")){
        		int x = (width - fm.stringWidth(line)) / 2;
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    		}
        
        g.dispose();
        return img;
	}

	
}
