package oscar.form;

import java.io.IOException;
import java.lang.reflect.*;

public class FrmRecordFactory {
    public FrmRecord factory (String which) throws IOException {
        String fullName = "oscar.form.Frm" + which + "Record"; // keyword - form_name get reference to the class            
        FrmRecord myClass = null;

        try {
            Class classDefinition = Class.forName( fullName );         
            myClass = (FrmRecord) classDefinition.newInstance(); 
        } catch (InstantiationException e) {
            System.out.println(e);
        } catch (IllegalAccessException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        
        return myClass;
    }
}