/*
 * 
 */


package oscar.login;

import java.io.*;
import java.util.*;

/**
 * Class LoginViewTypeHlp : read mapping file from loginView.txt
 * 2003-01-05
*/
public class LoginViewTypeHlp {
    static Properties viewtype = null;

    public static void init(String filename) {
        viewtype = new Properties();

        if(viewtype.isEmpty()) {
            try {
			          File file = new File("./" + filename + "/loginView.txt");
			          //System.out.println(file.getAbsolutePath() );
          			if(!file.isFile() || !file.canRead()) {
            				throw new IOException();
          			}

                FileInputStream fis = new FileInputStream(file ) ;
                viewtype.load(fis); 
                fis.close();
            } catch(Exception e) {System.out.println("*** No ViewType File ***"); }
        }
    }

    public static String getViewType(String pro) {
        return viewtype.getProperty(pro);
    }

}