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
package oscar.oscarEncounter.util;

import java.text.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import java.net.*;
import java.applet.Applet;

public class EctFileUtil {

    public String[] loadData(String fileName){
        int s = 50;
        String[] c;
        c = new String[s];
            for (int i=0; i < s; i++) {
               c[i] = "_";
            }
        URL u=null;

        try{
			File file = new File("./oscarEncounter/dataFiles/"+fileName);
			System.out.println(file.getAbsolutePath() );
			if(!file.isFile() || !file.canRead()) {
				throw new IOException();
			}
			RandomAccessFile inFile = new RandomAccessFile(file, "r");
            for (int i=0; i<s ;i++)
            {
                c[i]=inFile.readLine();
            }
        }catch (IOException ee)
        {
            System.err.println("houston, we have a problem: "+ee);
            return c;
        }

        return c;
   }// end loadData()

	public String[] loadData(String fileName, String projecthome){
        int s = 50;
        String[] c;
        c = new String[s];
            for (int i=0; i < s; i++) {
               c[i] = "_";
            }
        URL u=null;

        try{
			File file = new File("./"+projecthome+"/oscarEncounter/dataFiles/"+fileName);
			System.out.println(file.getAbsolutePath() );
			if(!file.isFile() || !file.canRead()) {
				throw new IOException();
			}
			RandomAccessFile inFile = new RandomAccessFile(file, "r");
            for (int i=0; i<s ;i++)
            {
                c[i]=inFile.readLine();
            }
        }catch (IOException ee)
        {
            System.err.println("houston, we have a problem: "+ee);
            return c;
        }

        return c;
   }// end loadData()

}
