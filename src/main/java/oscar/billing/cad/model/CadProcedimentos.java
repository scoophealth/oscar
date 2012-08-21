
/** Java class "CadProcedimentos.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.cad.model;

import java.util.Date;

import oscar.billing.fat.model.FatBpa;
import oscar.billing.fat.model.FatFormularioProcedimento;
import oscar.billing.model.ProcedimentoRealizado;


/**
 * <p>
 *
 * </p>
 */
@Deprecated
public class CadProcedimentos {
	public static final String AMBULATORIAL = "A";
	public static final String ATIVO = "A";
	public static final String INATIVO = "I";
	
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private long coProcedimento;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsProcedimento;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsReduzida;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String stProcedimento;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private Date dtIniciovalidade;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private double vlProcedimento;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public ProcedimentoRealizado procedimentoRealizado;

    /**
     * <p>
     *
     * </p>
     */
    public FatFormularioProcedimento fatFormularioProcedimento;

    /**
     * <p>
     *
     * </p>
     */
    public FatBpa fatBpa;

	/**
	 * 
	 */
	public CadProcedimentos() {

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
    public long getCoProcedimento() {
        return coProcedimento;
    }

    /**
     * @return
     */
    public String getDsProcedimento() {
        return dsProcedimento;
    }

    /**
     * @return
     */
    public String getDsReduzida() {
        return dsReduzida;
    }

    /**
     * @return
     */
    public Date getDtIniciovalidade() {
        return dtIniciovalidade;
    }

    /**
     * @return
     */
    public FatBpa getFatBpa() {
        return fatBpa;
    }

    /**
     * @return
     */
    public FatFormularioProcedimento getFatFormularioProcedimento() {
		if (fatFormularioProcedimento != null) {
			return fatFormularioProcedimento;
		} else {
			fatFormularioProcedimento = new FatFormularioProcedimento();
			return fatFormularioProcedimento;
		}        
    }

    /**
     * @return
     */
    public ProcedimentoRealizado getProcedimentoRealizado() {
		if (procedimentoRealizado != null) {
			return procedimentoRealizado;
		} else {
			procedimentoRealizado = new ProcedimentoRealizado();
			return procedimentoRealizado;
		}        
    }

    /**
     * @return
     */
    public String getStProcedimento() {
        return stProcedimento;
    }

    /**
     * @return
     */
    public double getVlProcedimento() {
        return vlProcedimento;
    }

    /**
     * @param l
     */
    public void setCoProcedimento(long l) {
        coProcedimento = l;
    }

    /**
     * @param string
     */
    public void setDsProcedimento(String string) {
        dsProcedimento = string;
    }

    /**
     * @param string
     */
    public void setDsReduzida(String string) {
        dsReduzida = string;
    }

    /**
     * @param date
     */
    public void setDtIniciovalidade(Date date) {
        dtIniciovalidade = date;
    }

    /**
     * @param bpa
     */
    public void setFatBpa(FatBpa bpa) {
        fatBpa = bpa;
    }

    /**
     * @param procedimento
     */
    public void setFatFormularioProcedimento(
        FatFormularioProcedimento procedimento) {
        fatFormularioProcedimento = procedimento;
    }

    /**
     * @param realizado
     */
    public void setProcedimentoRealizado(ProcedimentoRealizado realizado) {
        procedimentoRealizado = realizado;
    }

    /**
     * @param string
     */
    public void setStProcedimento(String string) {
        stProcedimento = string;
    }

    /**
     * @param d
     */
    public void setVlProcedimento(double d) {
        vlProcedimento = d;
    }
}
 // end CadProcedimentos
