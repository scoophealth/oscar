package oscar.form;

import java.util.Properties;
import oscar.util.*;

public class FrmLabReqPreSet {


        static public Properties set(String LabType,Properties props){

                if (LabType.equals("AR")){
                     ARset(props);
                }


                return props;
        }


        static void  ARset(Properties props){
            props.setProperty("b_urinalysis","checked");
            props.setProperty("h_bloodFilmExam","checked");
            props.setProperty("h_hemoglobin","checked");
            props.setProperty("h_wcbCount","checked");

            props.setProperty("h_hematocrit","checked");
            props.setProperty("i_heterophile","checked");
            props.setProperty("i_rubella","checked");
            props.setProperty("i_prenatal","checked");

            props.setProperty("i_prenatalHepatitisB","checked");
            props.setProperty("i_vdrl","checked");
            props.setProperty("m_urine","checked");
            props.setProperty("otherTest","HIV \n\nVaricella titre");

        }

}

