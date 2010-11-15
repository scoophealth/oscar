/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oscar.oscarRx.service;

import java.util.List;

import oscar.oscarRx.model.Favorites;

/**
 *
 * @author toby
 */
public interface RxPrescriptionMgr {

    public List<Favorites> getFavoritesFromProvider(String providerNo);
    public void setFavoritesForProvider(String providerNo, List<Integer> ids);
    public List<String> getProviders(); 
    public List<String[]> getFavoritesName(List<Favorites> list);
    public void setFavoritesPrivilege(String providerNo, boolean openpublic, boolean writeable);
    public boolean[] getFavoritesPrivilege(String providerNo);
    public String getProviderName(String providerNo);
}
