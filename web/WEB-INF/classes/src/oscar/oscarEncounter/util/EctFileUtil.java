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
			//File file = new File("./"+projecthome+"/oscarEncounter/dataFiles/"+fileName);
			File file = new File(projecthome+"/oscarEncounter/dataFiles/"+fileName);
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

	public String[] loadData(String fileName, String projecthome, String path){
        int s = 50;
        String[] c;
        c = new String[s];
            for (int i=0; i < s; i++) {
               c[i] = "_";
            }
        URL u=null;

        try{
			File file = new File("./"+projecthome+"/" + path + "/"+fileName);
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
            System.err.println(ee);
            return c;
        }

        return c;
   }// end loadData()

}
