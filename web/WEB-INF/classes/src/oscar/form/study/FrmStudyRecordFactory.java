package oscar.form.study;

import java.io.IOException;
import java.lang.reflect.*;

public class FrmStudyRecordFactory {
    public FrmStudyRecord factory (String which) throws IOException {
        String fullName = "oscar.form.study.FrmStudy" + which + "Record"; // get reference to the class            
        FrmStudyRecord myClass = null;

        try {
            Class classDefinition = Class.forName( fullName );         
            myClass = (FrmStudyRecord) classDefinition.newInstance(); 
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        
        return myClass;
/*
//String paramString1 = "one"; 
//String paramString2 = "two";            
//Class string = Class.forName("java.lang.String");            // get reference to constructor taking those param types            
//Class[] paramTypes = { string, string };            
//        Constructor constr = c.getConstructor(); //paramTypes);            // use this constructor get an instance            
//        Object[] params = { paramString1, paramString2 };            
        if (which.equalsIgnoreCase("diabete2")) { //keyword - study_name
            return new FrmStudydiabete2Record();
        } else {
            throw new IOException(); 
        }
*/
    }
}