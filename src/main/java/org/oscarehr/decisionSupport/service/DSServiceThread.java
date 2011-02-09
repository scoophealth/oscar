/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.service;

/**
 *
 * @author apavel
 */
public class DSServiceThread extends Thread {
    private DSService service;
    private String providerNo;

    public DSServiceThread(DSService service, String providerNo) {
        this.service = service;
        this.providerNo = providerNo;
    }

    @Override
    public void run() {
        service.fetchGuidelinesFromService(providerNo);
    }
}
