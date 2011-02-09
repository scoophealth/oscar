/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
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



