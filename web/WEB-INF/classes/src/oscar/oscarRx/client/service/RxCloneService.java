/*
 * RxCloneService.java
 *
 * Created on August 19, 2008, 12:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarRx.client.service;
import com.google.gwt.user.client.rpc.RemoteService;
import java.util.List;

/**
 *
 * @author toby
 */
public interface RxCloneService extends RemoteService{
    public List getProviders();
    public List<String[]> getFavoritesName(String provider_no);
    public void cloneFavorites(String provider_no, List<Integer> ids); 
    public String getCurrentProviderNo();
    public void setFavoritesPrivilege(String providerNo, boolean openpublic, boolean writeable);
    public boolean[] getFavoritesPrivilege(String providerNo);
}
