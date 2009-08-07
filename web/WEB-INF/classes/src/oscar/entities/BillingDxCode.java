package oscar.entities;

/**
 *  +-------------------+------------+------+-----+---------+----------------+
    | Field             | Type       | Null | Key | Default | Extra          |
    +-------------------+------------+------+-----+---------+----------------+
    | diagnosticcode_no | int(5)     | NO   | PRI | NULL    | auto_increment |
    | diagnostic_code   | varchar(5) | NO   | MUL |         |                |
    | description       | text       | YES  |     | NULL    |                |
    | status            | char(1)    | YES  |     | NULL    |                |
    | region            | varchar(5) | YES  |     | NULL    |                |
    +-------------------+------------+------+-----+---------+----------------+

 * @author jaygallagher
 */
public class BillingDxCode {
    private int diagnosticcodeNo;
    private String diagnosticCode = null;
    private String description = null;
    private String status = null;
    private String region = null;

    /**
     * @return the diagnosticcodeNo
     */
    public int getDiagnosticcodeNo() {
        return diagnosticcodeNo;
    }

    /**
     * @param diagnosticcodeNo the diagnosticcodeNo to set
     */
    public void setDiagnosticcodeNo(int diagnosticcodeNo) {
        this.diagnosticcodeNo = diagnosticcodeNo;
    }

    /**
     * @return the diagnosticCode
     */
    public String getDiagnosticCode() {
        return diagnosticCode;
    }

    /**
     * @param diagnosticCode the diagnosticCode to set
     */
    public void setDiagnosticCode(String diagnosticCode) {
        this.diagnosticCode = diagnosticCode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

}
