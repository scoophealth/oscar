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


package oscar.util;

import java.util.ResourceBundle;

import org.oscarehr.util.MiscUtils;


public class Pager {
    private static int MAX_PAGE_INDEX = 15;
    private static String HEADER = "Result page";
    private static ResourceBundle prop = ResourceBundle.getBundle("oscarResources");

    static {
        prop = ResourceBundle.getBundle("oscarResources");

        try {
            HEADER = prop.getString("pager.header.title");
        } catch (Exception e) {
        	MiscUtils.getLogger().error("can't be a good thing, too bad original author didn't document it", e);
        }

        try {
            MAX_PAGE_INDEX = Integer.parseInt(prop.getString(
                        "pager.max.page.index"));
        } catch (Exception e) {
        	MiscUtils.getLogger().error("can't be a good thing, too bad original author didn't document it", e);
        }
    }

    public static String generate(int offset, int length, int size, String url) {
        if (length > size) {
            String pref;

            if (url.indexOf("?") > -1) {
                pref = "&";
            } else {
                pref = "?";
            }

            String header = "<font face='Helvetica' size='-1'>" + HEADER +
                ": ";

            if (offset > 0) {
                header += ("&nbsp;<a href=\"" + url + pref + "pager.offset=" +
                (offset - size) + "\">" + prop.getString("pager.prev.desc") + "</a>\n");
            }

            int start;
            int radius = MAX_PAGE_INDEX / 2 * size;

            if (offset < radius) {
                start = 0;
            } else if (offset < (length - radius)) {
                start = offset - radius;
            } else {
                start = ((length / size) - MAX_PAGE_INDEX) * size;
            }

            for (int i = start;
                    (i < length) && (i < (start + (MAX_PAGE_INDEX * size)));
                    i += size) {
                if (i == offset) {
                    header += ("<b>" + ((i / size) + 1) + "</b>\n");
                } else {
                    header += ("&nbsp;<a href=\"" + url + pref +
                    "pager.offset=" + i + "\">" + ((i / size) + 1) + "</a>\n");
                }
            }

            if (offset < (length - size)) {
                header += ("&nbsp;<a href=\"" + url + pref + "pager.offset=" +
                (offset + size) + "\">" + prop.getString("pager.next.desc") + "</a>\n");
            }

            header += "</font>";

            return header;
        } else {
            return "";
        }
    }
}
