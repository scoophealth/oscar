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
package com.quatro.service;

import org.springframework.transaction.annotation.Transactional;

import com.quatro.dao.ScratchPadDao;

@Transactional
public class ScratchPadManager {

    private ScratchPadDao scratchPadDao=null;

    public void setScratchPadDao(ScratchPadDao scratchPadDao) {
        this.scratchPadDao = scratchPadDao;
    }

    public boolean isScratchFilled(String providerNo) {
        return this.scratchPadDao.isScratchFilled(providerNo);
    }

}
