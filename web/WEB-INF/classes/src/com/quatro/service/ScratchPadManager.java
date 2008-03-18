package com.quatro.service;

import com.quatro.dao.ScratchPadDao;

public class ScratchPadManager {

    private ScratchPadDao scratchPadDao=null;

    public void setScratchPadDao(ScratchPadDao scratchPadDao) {
        this.scratchPadDao = scratchPadDao;
    }

    public boolean isScratchFilled(String providerNo) {
        return this.scratchPadDao.isScratchFilled(providerNo);
    }

}
