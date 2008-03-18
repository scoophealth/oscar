package oscar.oscarEncounter.oscarMeasurements.util;

import java.util.ArrayList;

import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;
import oscar.oscarEncounter.oscarMeasurements.MeasurementInfo;

/**
 */
public class MeasurementHelper {

    public static boolean flowSheetRequiresWork(String demographic_no, MeasurementFlowSheet mFlowsheet) throws Exception {
        MeasurementInfo mi = new MeasurementInfo(demographic_no);
        ArrayList<String> measurements = mFlowsheet.getMeasurementList();
        mi.getMeasurements(measurements);
        return mFlowsheet.getMessages(mi).getWarnings().size() != 0;
    }
}
