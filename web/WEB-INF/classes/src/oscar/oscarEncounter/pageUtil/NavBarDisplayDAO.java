// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

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
    private String JavaScript;
    private ArrayList Items;
    
    /** Creates a new instance of NavBarDisplayDAO */
    public NavBarDisplayDAO() {   
        LeftHeading = null;
        RightHeading = null;        
        JavaScript = null;
        Items = new ArrayList();
    }
    
    public static void main(String[] args) {
	NavBarDisplayDAO dao = new NavBarDisplayDAO();
	Random rand = new Random(System.currentTimeMillis());
	Date d;

	for( int idx = 0; idx < 10; ++idx ) {
		NavBarDisplayDAO.Item item = dao.Item();
		long num = rand.nextLong();
		if( num < 0 ) num *= -1;
		System.out.println("Storing " + num);
		item.setTitle("" + num);
		d = new Date(num);
		item.setDate(d);
		dao.addItem(item);
	}

	dao.sortItems(NavBarDisplayDAO.ALPHASORT);

	System.out.println("Alphabetically Sorted:");
	for( int idx = 0; idx < 10; ++idx)
		System.out.println(idx + ": " + dao.getItem(idx).getTitle());

	dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);

	System.out.println("Chronologically Sorted:");
	for( int idx = 0; idx < 10; ++idx)
		System.out.println(idx + ": " + dao.getItem(idx).getDate().toString());
	 
    }
    
    public void setLeftHeading(String h) {
        LeftHeading = h;
    }
    
    public void setRightHeading(String h) {
        RightHeading = h;
    }
    
    public String getLeftHeading() {
        if( LeftHeading == null )
            return new String("");
        
        return LeftHeading;
    }
    
    public String getRightHeading() {
        if( RightHeading == null )
            return new String("");
        
        return RightHeading;
    }                     
    
    public void setJavaScript(String js) {
        JavaScript = js;
    }
    
    public String getJavaScript() {
        return JavaScript;
    }
    
    public void addItem(Item i) {
        Items.add(i);
    } 
    
    public Item getItem(int idx) {
        return (Item)Items.get(idx);
    }
    
    public Item Item() {
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
    
    /**
     *Item class holds list information for each row in left navbar of encounter form
     */
    public class Item implements Comparable {
        private String title;
        private String URL;
        private String colour;
        private String bgColour;
        private Date date;
        
        public Item() {
            title = "";
            URL = "";
            colour = "";
            bgColour = "";
            date = null;
        }
        
        public void setTitle( String t ) {
            title = t;
        }
        
        public String getTitle() {
            return title;
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
    }
    
    public class Chronologic implements Comparator {
        public int compare( Object o1, Object o2 ) {
            Item i1 = (Item)o1;
            Item i2 = (Item)o2;
            
            return i1.getDate().compareTo(i2.getDate());
        }
    }
    
     public class ChronologicAsc implements Comparator {
        public int compare( Object o1, Object o2 ) {
            Item i1 = (Item)o1;
            Item i2 = (Item)o2;
            Date d1 = i1.getDate();
            Date d2 = i2.getDate();
            
            if( d1.before(d2) )
                return -1;
            else if( d1.after(d2) )
                return 1;
            else 
                return 0;                        
        }
    }
}
