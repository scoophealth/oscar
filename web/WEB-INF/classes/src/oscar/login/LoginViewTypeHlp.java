/*
 * 
 */


package oscar.login;

import java.io.*;
import java.util.*;

/**
 * Class LoginViewTypeHlp : read mapping file from loginView.properties
 * 2003-01-05
*/

/*
 This class implements the pattern singleton.
 If you need instanciate it, instead 'new LoginViewTypeHlp()',
 use 'LoginViewTypeHlp.getInstance()'

 Use the method getProperty(String) to recover info
 about the login view type
 */
public class LoginViewTypeHlp extends Properties{
	
	public static LoginViewTypeHlp getInstance() {
		return myself;
	}

	static LoginViewTypeHlp myself = new LoginViewTypeHlp();

	private LoginViewTypeHlp() {
		/* getResourceAsStream is a method from Class() */
		InputStream is = getClass().getResourceAsStream("/loginView.properties");
		try { 
			load(is);
			is.close(); 
		} catch(Exception e) {
			System.out.println("*** No ViewType File ***");
			e.printStackTrace();
		}
	}

}

