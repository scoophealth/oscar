
/** Java class "CadAtividadesSaude.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.cad.model;

import oscar.billing.fat.model.FatBpa;
import oscar.billing.model.Provider;



/**
 * <p>
 *
 * </p>
 */
@Deprecated
public class CadAtividadesSaude {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private long coAtividade;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsAtividade;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public FatBpa fatBpa;

    /**
     * <p>
     *
     * </p>
     */
    public Provider provider;

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
    public long getCoAtividade() {
        return coAtividade;
    }

    /**
     * @return
     */
    public String getDsAtividade() {
        return dsAtividade;
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
    public Provider getProvider() {
        return provider;
    }

    /**
     * @param l
     */
    public void setCoAtividade(long l) {
        coAtividade = l;
    }

    /**
     * @param string
     */
    public void setDsAtividade(String string) {
        dsAtividade = string;
    }

    /**
     * @param bpa
     */
    public void setFatBpa(FatBpa bpa) {
        fatBpa = bpa;
    }

    /**
     * @param provider
     */
    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}
 // end CadAtividadesSaude
