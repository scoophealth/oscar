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

import java.io.File;
import java.util.Comparator;

/**
 *
 * @author jay
 */
public class FileSortByDate implements Comparator{
    
    /** Creates a new instance of FileSortByDate */
    public FileSortByDate() {
    }

    public int compare(Object object, Object object0) {
        File f1 = (File) object;
        File f2 = (File) object0;
        
        long f1LastMod = f1.lastModified();
        long f2LastMod = f2.lastModified();
  
        if (f1LastMod < f2LastMod) {
            return 1;
        } else if (f2LastMod < f1LastMod) {
            return -1;
        } 
        return 0;
  
    }
    
    
}
