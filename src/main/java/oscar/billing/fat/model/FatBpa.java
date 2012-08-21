
/** Java class "FatBpa.java" generated from Poseidon for UML.
 *  Poseidon for UML is developed by <A HREF="http://www.gentleware.com">Gentleware</A>.
 *  Generated with <A HREF="http://jakarta.apache.org/velocity/">velocity</A> template engine.
 */
package oscar.billing.fat.model;

import java.util.Date;

import oscar.billing.cad.model.CadAtividadesSaude;
import oscar.billing.cad.model.CadFaixasEtaria;
import oscar.billing.cad.model.CadGrupoAtendimento;
import oscar.billing.cad.model.CadProcedimentos;
import oscar.billing.cad.model.CadTiposAtendimento;

/**
 * <p>
 * 
 * </p>
 */
@Deprecated
public class FatBpa {

  ///////////////////////////////////////
  // attributes


/**
 * <p>
 * Represents ...
 * </p>
 */
    private int nuMes; 

/**
 * <p>
 * Represents ...
 * </p>
 */
    private int nuAno; 

/**
 * <p>
 * Represents ...
 * </p>
 */
    private int nuSequencia; 

/**
 * <p>
 * Represents ...
 * </p>
 */
    private int nuQuantidade; 

/**
 * <p>
 * Represents ...
 * </p>
 */
    private String stRegistro; 

/**
 * <p>
 * Represents ...
 * </p>
 */
    private Date dtAtualizacao; 

/**
 * <p>
 * Represents ...
 * </p>
 */
    private int coOperador; 

   ///////////////////////////////////////
   // associations

/**
 * <p>
 * 
 * </p>
 */
    public CadTiposAtendimento cadTiposAtendimento; 
/**
 * <p>
 * 
 * </p>
 */
    public CadAtividadesSaude cadAtividadesSaude; 
/**
 * <p>
 * 
 * </p>
 */
    public CadFaixasEtaria cadFaixasEtaria; 
/**
 * <p>
 * 
 * </p>
 */
    public CadGrupoAtendimento cadGrupoAtendimento; 
/**
 * <p>
 * 
 * </p>
 */
    public CadProcedimentos cadProcedimentos; 


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
    } // end clear        

} // end FatBpa
