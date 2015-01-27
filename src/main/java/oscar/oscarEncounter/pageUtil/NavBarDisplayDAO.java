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


package oscar.oscarEncounter.pageUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.oscarehr.util.MiscUtils;

/**
 *
 * @author rjonasz
 */

public class NavBarDisplayDAO {
	public final static int ALPHASORT = 0;
	public final static int DATESORT = 1;
	public final static int DATESORT_ASC = 2;

	private String LeftHeading;
	private String RightHeading;
	private String LeftURL;
	private String RightURL;
	private String JavaScript;
	private String PopUpHeader;
	private ArrayList<Item> Items;
	private ArrayList<String> PopUpMenuURLS;
	private ArrayList<String> PopUpMenuNames;
	private String headingColour = null;
	private String reloadUrl = null;
	private String divId = null;
	//Sort the items explicitly.. previously some default sort was applied in LeftNavBarDisplay.jsp.. if true then it will not use that sorting
	private boolean internalDateSort = true;

	/** Creates a new instance of NavBarDisplayDAO */
	public NavBarDisplayDAO() {
		LeftHeading = null;
		RightHeading = null;
		LeftURL = null;
		RightURL = null;
		JavaScript = null;
		PopUpHeader = null;
		Items = new ArrayList<Item>();
		PopUpMenuURLS = new ArrayList<String>();
		PopUpMenuNames = new ArrayList<String>();
	}

	public Map<String, Object> getMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("LeftHeading", LeftHeading);
		map.put("RightHeading", RightHeading);
		map.put("LeftURL", LeftURL);
		map.put("RightURL", RightURL);
		map.put("PopUpHeader", PopUpHeader);
		map.put("Items", Items);
		map.put("PopUpMenuURLS", PopUpMenuURLS);
		map.put("PopUpMenuNames", PopUpMenuNames);
		map.put("headingColour", headingColour);

		return map;
	}

	public static void main(String[] args) {
		NavBarDisplayDAO dao = new NavBarDisplayDAO();
		Random rand = new Random(System.currentTimeMillis());
		Date d;

		for( int idx = 0; idx < 10; ++idx ) {
			NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
			long num = rand.nextLong();
			if( num < 0 ) num *= -1;
			MiscUtils.getLogger().debug("Storing " + num);
			item.setTitle("" + num);
			d = new Date(num);
			item.setDate(d);
			dao.addItem(item);
		}

		dao.sortItems(NavBarDisplayDAO.ALPHASORT);

		MiscUtils.getLogger().debug("Alphabetically Sorted:");
		for( int idx = 0; idx < 10; ++idx)
			MiscUtils.getLogger().debug(idx + ": " + dao.getItem(idx).getTitle());

		dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);

		MiscUtils.getLogger().debug("Chronologically Sorted:");
		for( int idx = 0; idx < 10; ++idx)
			MiscUtils.getLogger().debug(idx + ": " + dao.getItem(idx).getDate().toString());

	}

	public void setLeftHeading(String h) {
		LeftHeading = h;
	}

	public String getLeftHeading() {
		if( LeftHeading == null )
			return new String("");

		return LeftHeading;
	}

	public void setRightHeadingID(String h) {
		RightHeading = h;
	}

	public String getRightHeadingID() {
		if( RightHeading == null )
			return new String("");

		return RightHeading;
	}

	public void setLeftURL( String url ) {
		LeftURL = url;
	}

	public String getLeftURL() {
		if( LeftURL == null )
			return new String("");

		return LeftURL;
	}

	public void setRightURL( String url ) {
		RightURL = url;
	}

	public String getRightURL() {
		if( RightURL == null )
			return new String("");

		return RightURL;
	}

	public void setMenuHeader(String heading) {
		PopUpHeader = heading;
	}

	public String getMenuHeader() {
		if( PopUpHeader == null )
			return new String("");

		return PopUpHeader;
	}

	public void setJavaScript(String js) {
		JavaScript = js;
	}

	public String getJavaScript() {
		return JavaScript;
	}

	public void addPopUpUrl(String url) {
		PopUpMenuURLS.add(url);
	}

	public String getPopUpUrl(int i) {
		return PopUpMenuURLS.get(i);
	}

	public void addPopUpText(String txt) {
		PopUpMenuNames.add(txt);
	}

	public String getPopUpText(int i) {
		return PopUpMenuNames.get(i);
	}

	public int numPopUpMenuItems() {
		return PopUpMenuURLS.size();
	}

	public void addItem(Item i) {
		Items.add(i);
	}

	public Item getItem(int idx) {
		return Items.get(idx);
	}

	public static Item Item() {
		return new Item();
	}

	public int numItems() {
		return Items.size();
	}

	public void sortItems(int order ) {
		switch(order) {
		case DATESORT:
			Collections.sort(Items, new Chronologic());
			break;
		case DATESORT_ASC:
			Collections.sort(Items, new ChronologicAsc());
			break;
		case ALPHASORT:
			Collections.sort(Items);
		}

	}

	public void sortItems(List list, int order ) {
		switch(order) {
		case DATESORT:
			Collections.sort(list, new Chronologic());
			break;
		case DATESORT_ASC:
			Collections.sort(list, new ChronologicAsc());
			break;
		case ALPHASORT:
			Collections.sort(list);
		}

	}

	/**
	 *Item class holds list information for each row in left navbar of encounter form
	 */
	 public static class Item implements Comparable {
		 private String title;
		 private String value;
		 private String linkTitle;
		 private String URL;
		 private String colour;
		 private String bgColour;
		 private Date date;
		 private boolean URLJavaScript;

		 public Item() {
			 title = "";
			 linkTitle = "";
			 URL = "";
			 colour = "";
			 bgColour = "";
			 date = null;
			 setURLJavaScript(true);
		 }

		 public void setTitle( String t ) {
			 title = t;
		 }

		 public String getTitle() {
			 return title;
		 }

		 public void setLinkTitle(String t) {
			 linkTitle = t;
		 }

		 public String getLinkTitle() {
			 return linkTitle;
		 }

		 public void setURL(String url) {
			 URL = url;
		 }

		 public String getURL() {
			 return URL;
		 }

		 public void setColour(String c ) {
			 colour = c;
		 }

		 public String getColour() {
			 return colour;
		 }

		 public void setBgColour(String c ) {
			 bgColour = c;
		 }

		 public String getBgColour() {
			 return bgColour;
		 }

		 public void setDate(Date d) {
			 date = d;
		 }

		 public Date getDate() {
			 return date;
		 }

		 public boolean isURLJavaScript() {
			 return URLJavaScript;
		 }

		 public void setURLJavaScript(boolean uRLJavaScript) {
			 URLJavaScript = uRLJavaScript;
		 }

		 //default compare is alphabetical
		 public int compareTo( Object o ) throws NullPointerException {
			 if( o == null )
				 throw new NullPointerException();

			 Item i = (Item)o;

			 return title.compareTo(i.getTitle());
		 }

		 public boolean equals( Object o ) {
			 if( o == null )
				 return false;

			 return (compareTo(o) == 0);
		 }

		 public int hashCode() {
			 return title.hashCode();
		 }

		 public String getValue() {
			 return value;
		 }

		 public void setValue(String value) {
			 this.value = value;
		 }
	 }

	 public class Chronologic implements Comparator {
		 public int compare( Object o1, Object o2 ) {
			 Item i1 = (Item)o1;
			 Item i2 = (Item)o2;

			 Date d1 = i1.getDate();
			 Date d2 = i2.getDate();

			 if( d1 == null && d2 != null )
				 return -1;
			 else if( d1 != null && d2 == null )
				 return 1;
			 else if( d1 == null && d2 == null )
				 return 0;
			 else
				 return i1.getDate().compareTo(i2.getDate());
		 }
	 }

	 public class ChronologicAsc implements Comparator {
		 public int compare( Object o1, Object o2 ) {
			 Item i1 = (Item)o1;
			 Item i2 = (Item)o2;
			 Date d1 = i1.getDate();
			 Date d2 = i2.getDate();

			 /*if( d1.before(d2) )
                return 1;
            else if( d1.after(d2) )
                return -1;
            else
                return 0;
			  */
			 if( d1 == null && d2 != null )
				 return -1;
			 else if( d1 != null && d2 == null )
				 return 1;
			 else if( d1 == null && d2 == null )
				 return 0;
			 else
				 return -(i1.getDate().compareTo(i2.getDate()));
		 }
	 }

	 public String getHeadingColour() {
		 return headingColour;
	 }

	 public void setHeadingColour(String headingColour) {
		 this.headingColour = headingColour;
	 }

	 public boolean hasHeadingColour(){
		 boolean hasHeadingColour = false;
		 if (this.headingColour != null){
			 hasHeadingColour = true;
		 }
		 return hasHeadingColour;
	 }

	 public String getReloadUrl() {
		 return reloadUrl;
	 }

	 public void setReloadUrl(String reloadUrl) {
		 this.reloadUrl = reloadUrl;
	 }

	public boolean isInternalDateSort()
	{
		return internalDateSort;
	}

	public void setInternalDateSort(boolean internalDateSort)
	{
		this.internalDateSort = internalDateSort;
	}

	/**
	 * @return the divId
	 */
	public String getDivId() {
		return divId;
	}

	/**
	 * @param divId the divId to set
	 */
	public void setDivId(String divId) {
		this.divId = divId;
	}


}
