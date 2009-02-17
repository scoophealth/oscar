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
