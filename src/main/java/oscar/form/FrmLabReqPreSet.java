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


package oscar.form;

import java.util.Properties;

public class FrmLabReqPreSet {


        static public Properties set(String LabType,Properties props){

                if (LabType.equals("AR")){
                     ARset(props);
                }
                else if( LabType.equals("AnteNatal") ) {
                    AnteNatalSet(props);
                }else if (LabType.equals("DM")){
                    DMSet(props);
                }

                return props;
        }

        static void DMSet(Properties props){
            props.setProperty("b_glucose","checked");
            props.setProperty("b_glucose_fasting","checked");
            props.setProperty("b_hba1c","checked");
            props.setProperty("b_creatinine","checked");
            props.setProperty("b_uricAcid","checked");
            props.setProperty("b_alt","checked");
            props.setProperty("b_alkPhosphatase","checked");
            props.setProperty("b_lipidAssessment","checked");
            props.setProperty("aci","\n Fast for 12 hours");
        }
        
        static void AnteNatalSet(Properties props) {
            props.setProperty("b_urinalysis","checked");
            props.setProperty("h_cbc", "checked");
            props.setProperty("i_rubella", "checked");
            props.setProperty("i_prenatal", "checked");
            props.setProperty("m_urine", "checked");
            props.setProperty("o_otherTests1", "Hep B s Ag");
            props.setProperty("o_otherTests2", "VDRL");
            props.setProperty("o_otherTests3", "HIV");
            props.setProperty("o_otherTests4", "Varicella titre");
            props.setProperty("o_otherTests5", "Ferritin");
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
