package oscar.oscarMDS.data;

public class Mnemonics {
    
    public Mnemonics() {
        reportName = "Error";
        units = "";
        referenceRange = "";
    }
    
    public Mnemonics(String rN, String u, String rR) {
        reportName = rN;
        units = u;
        referenceRange = rR;
    }
    
    public void update(Mnemonics newData) {
        if (newData != null) {
            reportName = newData.reportName;
            units = newData.units;
            referenceRange = newData.referenceRange;
        } else {
            reportName = "Error in Mnemonics.update";
            units = "";
            referenceRange = "";
        }
    }
    
    public String reportName;
    public String units;
    public String referenceRange;
    
}

