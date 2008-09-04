/*
 * RxCloneServiceAsync.java
 *
 * Created on August 19, 2008, 12:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarRx.client.service;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;


/**
 *
 * @author toby
 */
public interface RxCloneServiceAsync {

    public abstract void getProviders(AsyncCallback asyncCallback);

    public abstract void getFavoritesName(String provider_no, AsyncCallback asyncCallback);

    public abstract void cloneFavorites(String provider_no, List<Integer> ids, AsyncCallback asyncCallback);

    public abstract void getCurrentProviderNo(AsyncCallback asyncCallback);

    public abstract void setFavoritesPrivilege(java.lang.String providerNo, boolean openpublic, boolean writeable, AsyncCallback asyncCallback);

    public abstract void getFavoritesPrivilege(java.lang.String providerNo, AsyncCallback asyncCallback);
}
