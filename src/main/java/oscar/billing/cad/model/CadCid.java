
/** Java class "CadCid.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.cad.model;

import oscar.billing.model.Diagnostico;


/**
 * <p>
 *
 * </p>
 */
@Deprecated
public class CadCid {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String coCid;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsCid;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsReduzidocid;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public Diagnostico diagnostico;

	/**
	 * 
	 */
	public CadCid() {
		this.diagnostico = new Diagnostico();

	}

    ///////////////////////////////////////
    
    
    
    // operations

    /**
     * <p>
     * Does ...
     * </p><p>
     *
     * </p>
     */
    public void clear() {
        // your code here
    }

    // end clear        

    /**
     * @return
     */
    public String getCoCid() {
        return coCid;
    }

    /**
     * @return
     */
    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    /**
     * @return
     */
    public String getDsCid() {
        return dsCid;
    }

    /**
     * @return
     */
    public String getDsReduzidocid() {
        return dsReduzidocid;
    }

    /**
     * @param l
     */
    public void setCoCid(String l) {
        coCid = l;
    }

    /**
     * @param diagnostico
     */
    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    /**
     * @param string
     */
    public void setDsCid(String string) {
        dsCid = string;
    }

    /**
     * @param string
     */
    public void setDsReduzidocid(String string) {
        dsReduzidocid = string;
    }
}


// end CadCid
