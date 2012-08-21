
/** Java class "CadTiposAtendimento.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.cad.model;

import oscar.billing.fat.model.FatBpa;

/**
 * <p>
 * Bean used for attendance type
 * </p>
 */
@Deprecated
public class CadTiposAtendimento {

	///////////////////////////////////////
	// attributes

	private long coTipoatendimento;
	private String dsTipoatendimento;

	///////////////////////////////////////
	// associations

	public FatBpa fatBpa;

	///////////////////////////////////////
	// operations

	public void clear() {
		coTipoatendimento = 0;
		dsTipoatendimento = "";
	}

	/**
	 * Returns the coTipoatendimento.
	 * @return long
	 */
	public long getCoTipoatendimento() {
		return coTipoatendimento;
	}

	/**
	 * Returns the dsTipoatendimento.
	 * @return String
	 */
	public String getDsTipoatendimento() {
		return dsTipoatendimento;
	}

	/**
	 * Sets the coTipoatendimento.
	 * @param coTipoatendimento The coTipoatendimento to set
	 */
	public void setCoTipoatendimento(long coTipoatendimento) {
		this.coTipoatendimento = coTipoatendimento;
	}

	/**
	 * Sets the dsTipoatendimento.
	 * @param dsTipoatendimento The dsTipoatendimento to set
	 */
	public void setDsTipoatendimento(String dsTipoatendimento) {
		this.dsTipoatendimento = dsTipoatendimento;
	}

} // end CadTiposAtendimento
