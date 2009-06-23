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
package com.quatro.model.security;

public class NoAccessException extends Exception{
	private static final long serialVersionUID = -4444364883235024698L;

	public NoAccessException(String s){
		super(s);
	}
	public NoAccessException()
	{
		super("Access to the requested page has been denied due to insufficient privileges.");
	}
}
