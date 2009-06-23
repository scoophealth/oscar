/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

public class DocTextValue implements Serializable{
  private int docId;
  private byte[] docText;
  
  public int getDocId() {
	return docId;
  }

  public void setDocId(int docId) {
	this.docId = docId;
  }
  
  public byte[] getDocText() {
	return docText;
  }

  public void setDocText(byte[] docText) {
	  this.docText = docText;
  }
  
  public void setDocTextBlob(Blob docTextBlob) {
	  this.docText = this.toByteArray(docTextBlob);
  } 

  // Don't invoke this.  Used by Hibernate only. 
  public Blob getDocTextBlob() {
	  return Hibernate.createBlob(this.docText); 
  }
  
  private byte[] toByteArray(Blob fromBlob){
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  try {
		  return toByteArrayImpl(fromBlob, baos);
	  }catch(SQLException e){
		  throw new RuntimeException(e);
	  }catch (IOException e){
		  throw new RuntimeException(e);
	  }finally{
		if (baos != null) {
		  try {
			  baos.close();
		  } catch(IOException ex) {
			  
		  }
		}
	  }
  }
  
  private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos)  
       throws SQLException, IOException {
     byte[] buf = new byte[4000];
     InputStream is = fromBlob.getBinaryStream();
     try {
    	 for (;;) {
    	   int dataSize = is.read(buf);
    	   if (dataSize == -1) break;
    	   baos.write(buf, 0, dataSize);
    	 }
     } finally {
    	if (is != null){
    	   try {
    		   is.close();
    	   }catch(IOException ex) {}
    	}
     }

     return baos.toByteArray();
  }
  
}
