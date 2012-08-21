
/** Java class "FatFormularios.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.fat.model;

import java.util.Collection;
import java.util.TreeSet;


/**
 * <p>
 *
 * </p>
 */
@Deprecated
public class FatFormularios {
    ///////////////////////////////////////
    // attributes

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private int coFormulario;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String dsFormulario;

    /**
     * <p>
     * Represents ...
     * </p>
     */
    private String stAtivo;

    ///////////////////////////////////////
    // associations

    /**
     * <p>
     *
     * </p>
     */
    public Collection<FatFormularioProcedimento> fatFormularioProcedimento = new TreeSet<FatFormularioProcedimento>(); // of type FatFormularioProcedimento

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
        coFormulario = 0;
        dsFormulario = "";
        fatFormularioProcedimento.clear();
    }
     // end clear        

    /**
     * @return
     */
    public int getCoFormulario() {
        return coFormulario;
    }

    /**
     * @return
     */
    public String getDsFormulario() {
        return dsFormulario;
    }

    /**
     * @return
     */
    public Collection<FatFormularioProcedimento> getFatFormularioProcedimento() {
        return fatFormularioProcedimento;
    }

    /**
     * @return
     */
    public String getStAtivo() {
        return stAtivo;
    }

    /**
     * @param i
     */
    public void setCoFormulario(int i) {
        coFormulario = i;
    }

    /**
     * @param string
     */
    public void setDsFormulario(String string) {
        dsFormulario = string;
    }

    /**
     * @param collection
     */
    public void setFatFormularioProcedimento(Collection<FatFormularioProcedimento> collection) {
        fatFormularioProcedimento = collection;
    }

    /**
     * @param string
     */
    public void setStAtivo(String string) {
        stAtivo = string;
    }
}
 // end FatFormularios
