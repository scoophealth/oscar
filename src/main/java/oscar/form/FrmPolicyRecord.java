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

import java.sql.SQLException;
import java.util.Properties;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;

/**
 *
 * @author kimleanhoffman
 */
public class FrmPolicyRecord extends FrmRecord{
   
    public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException{
        Properties props = new Properties();
        Demographic demographic = demographicManager.getDemographic(loggedInInfo, demographicNo);

        if(existingID <= 0 && demographic != null) {
            props.setProperty("demographic_no", String.valueOf(demographicNo));
            props.setProperty("formCreated",oscar.util.DateUtils.getDateTime());

        } else {
            String sql = "SELECT * FROM formNoShowPolicy WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
            FrmRecordHelp frmRec = new FrmRecordHelp();
            frmRec.setDateFormat("dd/MM/yyyy");
            props = frmRec.getFormRecord(sql);
        }

        return props;
    }

    @Override
    public int saveFormRecord(Properties props) throws SQLException {
        String demographicNo = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formNoShowPolicy WHERE demographic_no=" +demographicNo +" AND ID=0";
        FrmRecordHelp frmRec = new FrmRecordHelp();
        frmRec.setDateFormat("dd/MM/yyyy");

        return frmRec.saveFormRecord(props, sql);
    }

    @Override
    public String findActionValue(String submit) throws SQLException {
 		return ((new FrmRecordHelp()).findActionValue(submit));
    }

    @Override
    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
 		return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formNoShowPolicy WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
        return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

}
