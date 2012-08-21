
/** Java class "FatFormularioProcedimento.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.fat.model;

import oscar.billing.cad.model.CadProcedimentos;


/**
 * <p>
 *
 * </p>
 */
@Deprecated
public class FatFormularioProcedimento {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private int ordem;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public FatFormularios fatFormularios;

    /**
     * <p>
     *
     * </p>
     */
    public CadProcedimentos cadProcedimentos;

	/**
	 * 
	 */
	public FatFormularioProcedimento() {
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
    public CadProcedimentos getCadProcedimentos() {
		if (cadProcedimentos != null) {
			return cadProcedimentos;
		} else {
			this.cadProcedimentos = new CadProcedimentos();
			return cadProcedimentos;
		}        
    }

    /**
     * @return
     */
    public FatFormularios getFatFormularios() {
		if (fatFormularios != null) {
			return fatFormularios;
		} else {
			fatFormularios = new FatFormularios(); 
			return fatFormularios;
		}        
    }

    /**
     * @return
     */
    public int getOrdem() {
        return ordem;
    }

    /**
     * @param procedimentos
     */
    public void setCadProcedimentos(CadProcedimentos procedimentos) {
        cadProcedimentos = procedimentos;
    }

    /**
     * @param formularios
     */
    public void setFatFormularios(FatFormularios formularios) {
        fatFormularios = formularios;
    }

    /**
     * @param i
     */
    public void setOrdem(int i) {
        ordem = i;
    }
}
 // end FatFormularioProcedimento
