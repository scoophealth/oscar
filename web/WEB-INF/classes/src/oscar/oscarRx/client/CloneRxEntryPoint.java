/*
 * CloneRxEntryPoint.java
 *
 * Created on August 19, 2008, 10:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarRx.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import oscar.oscarRx.client.service.*;

/**
 *
 * @author toby
 */
public class CloneRxEntryPoint implements EntryPoint {

    //Create popup widget
    private final PopupPanel popup = new PopupPanel(true);
    private int mouseX,  mouseY = 0;
    private final int COPY_TO = 0;
    private final int COPY_FROM = 1;
    private final int COPY_BOTH = 2;    //Store all info for one provider's favorites
    private List<String[]> srcFavorites = new ArrayList<String[]>();
    private List<String[]> desFavorites = new ArrayList<String[]>();    //Store current selected providerNo
    private String srcProviderNo,  desProviderNo = "";
    private ListBox srcFavList,  desFavList,  desProList = null; //Holders to widget
    private Label message = new Label();
    private Button copyToButton, copyBothButton = null;

    /** Creates a new instance of CloneRxEntryPoint */
    public CloneRxEntryPoint() {
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable arg0) {
                message.setText("Problematic retrieving data, please refresh page.");
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(Object arg0) {
                srcProviderNo = (String) arg0;
            }
        };
        getService().getCurrentProviderNo(callback);
    }

    /** 
    The entry point method, called automatically by loading a module
    that declares an implementing class as an entry-point
     */
    public void onModuleLoad() {

        // Create HorizontalPanel
        final HorizontalPanel horPanel = new HorizontalPanel();
        horPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        horPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        horPanel.setWidth("100%");
        horPanel.setHeight("90%");
        RootPanel.get("copyfavorites").add(horPanel);

        message.setWidth("100%");
        RootPanel.get("copyfavorites").add(message);


        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable exception) {
                message.setText("Problematic retrieving data, please refresh page.");
                throw new UnsupportedOperationException(this.getClass() + " onModuleLoad() failed!");
            }

            public void onSuccess(Object arg0) {
                //Create left panel on HorizontalPanel
                DecoratedTabPanel tabPanelSrc = createTabPanel((List) arg0, 0);
                horPanel.add(tabPanelSrc);

                //Create middle panel on HorizontalPanel
                VerticalPanel midPanel = createVerticalPanel();
                horPanel.add(midPanel);

                //Create right panel on HorizontalPanel
                DecoratedTabPanel tabPanelDest = createTabPanel((List) arg0, 1);
                horPanel.add(tabPanelDest);
            }
        };

        getService().getProviders(callback);

    }

    public DecoratedTabPanel createTabPanel(List obj, int type) {
        DecoratedTabPanel tabPanel = new DecoratedTabPanel();
        tabPanel.setWidth("100%");
        tabPanel.setAnimationEnabled(false);

        // Add list for providers
        final ListBox proList = new ListBox(true);
        if (type == 0) {
            createSettingPanel(tabPanel);
        } else {
            desProList = proList;
            createProvidersListBox(tabPanel, proList, obj);
        }
        // Add list for favourites prescriptions
        final ListBox favList = new ListBox(true);
        createFavoritesListBox(tabPanel, favList);

        // Click choose provider and fetch favorites
        fetchFavsFromProvider(proList, favList, type);

        //Click choose favorite and popup descriptions
        fetchDetailFromFavorite(favList, type);

        if (type == 0) {
            tabPanel.selectTab(1);
        } else {
            tabPanel.selectTab(0);
        }
        return tabPanel;
    }

    public VerticalPanel createVerticalPanel() {
        VerticalPanel vPanel = new VerticalPanel();
        vPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        // Create some buttons
        Button diffButton = new Button();
        diffButton.setText("Diff");
        diffButton.setWidth("90px");
        Button rightButton = new Button();
        rightButton.setText("Copy  ->");
        rightButton.setWidth("90px");
        copyToButton = rightButton;
        final Button leftButton = new Button();
        leftButton.setText("<-  Copy");
        leftButton.setWidth("90px");
        Button twowayButton = new Button();
        twowayButton.setText("<-Copy->");
        twowayButton.setWidth("90px");
        copyBothButton = twowayButton;

        diffButton.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                message.setText("");
                compareAndHighlightFav();
            }
        });

        addClickListenerToButton(rightButton, COPY_TO);
        addClickListenerToButton(leftButton, COPY_FROM);
        addClickListenerToButton(twowayButton, COPY_BOTH);

        vPanel.add(diffButton);
        vPanel.add(rightButton);
        vPanel.add(leftButton);
        vPanel.add(twowayButton);

        return vPanel;
    }

    public void addClickListenerToButton(Button button, final int type) {
        button.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                if (!compareSelectedFavorites(type)) {
                    message.setText("Your selection have duplicated favorites, please " +
                            "use [Diff] button to see which favorites can be selected!");
                } else {
                    message.setText("");
                    copyFavs(type);
                }
            }
        });
    }

    public void createProvidersListBox(DecoratedTabPanel tabPanel, ListBox proList, List obj) {
        proList.setWidth("100%");
        proList.setVisibleItemCount(20);
        proList.setMultipleSelect(false);
        for (int i = 0; i < obj.size(); i++) {
            proList.addItem((String) obj.get(i));
        }
        tabPanel.add(proList, "Open Provider");
    }

    public void createSettingPanel(DecoratedTabPanel tabPanel) {
        final RadioButton publicRadio = new RadioButton("myState", "Public");
        final RadioButton privateRadio = new RadioButton("myState", "Private");
        privateRadio.setChecked(true);
        final CheckBox writableCheckBox = new CheckBox("Writeable");

        Button apply = new Button("Apply");

        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable arg0) {
                message.setText("Problematic retrieving data, please refresh page.");
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(Object arg0) {
                publicRadio.setChecked(((boolean[]) arg0)[0]);
                privateRadio.setChecked(!((boolean[]) arg0)[0]);
                writableCheckBox.setEnabled(((boolean[]) arg0)[0]);
                writableCheckBox.setChecked(((boolean[]) arg0)[1]);
            }
        };

        getService().getFavoritesPrivilege(srcProviderNo, callback);

        publicRadio.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                writableCheckBox.setEnabled(true);
                message.setText("Select 'Public' will allow other providers " +
                        "to see or copy favorite prescriptions from you.");
            }
        });
        privateRadio.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                writableCheckBox.setChecked(false);
                writableCheckBox.setEnabled(false);
                message.setText("Select 'Private' will make other providers " +
                        "can not see or copy your favorite prescriptions.");
            }
        });
        writableCheckBox.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                message.setText("Check off 'Writeable' allow other providers " +
                        "copy their favorite prescriptions to you.");
            }
        });
        apply.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                boolean openpublic = publicRadio.isChecked() ? true : false;
                boolean writeable = writableCheckBox.isChecked() ? true : false;
                updateState(openpublic, writeable);
            }
        });
        HorizontalPanel settingPanel = new HorizontalPanel();

        settingPanel.add(publicRadio);
        settingPanel.add(privateRadio);
        settingPanel.add(writableCheckBox);
        settingPanel.add(apply);
        tabPanel.add(settingPanel, "My Setting");

    }

    public void updateState(final boolean openpublic, boolean writeable) {
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable arg0) {
                message.setText("Problematic retrieving data, please refresh page.");
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(Object arg0) {
                if (openpublic) {
                    boolean me = false;
                    for (int i = 0; i < desProList.getItemCount(); i++) {
                        if (desProList.getItemText(i).equalsIgnoreCase(srcProviderNo)) {
                            me = true;
                            break;
                        }
                    }
                    if (!me) {
                        desProList.addItem(srcProviderNo);
                    }
                } else {
                    for (int i = 0; i < desProList.getItemCount(); i++) {
                        if (desProList.getItemText(i).equalsIgnoreCase(srcProviderNo)) {
                            desProList.removeItem(i);
                        }
                    }
                    desFavList.clear();
                }
            }
        };
        getService().setFavoritesPrivilege(srcProviderNo, openpublic, writeable, callback);
    }

    public void createFavoritesListBox(DecoratedTabPanel tabPanel, ListBox favList) {
        // Add FocusPanel to catch mouse
        FocusPanel focus = new FocusPanel();
        focus.setWidth("100%");
        tabPanel.add(focus, "Favorite Prescriptions");
        // Add list on FocusPanel
        favList.setWidth("100%");
        favList.setVisibleItemCount(20);
        favList.setMultipleSelect(true);
        focus.add(favList);
        focus.addMouseListener(new MouseListener() {

            public void onMouseDown(Widget arg0, int arg1, int arg2) {
            }

            public void onMouseEnter(Widget arg0) {
            }

            public void onMouseLeave(Widget arg0) {
            }

            public void onMouseMove(Widget arg0, int arg1, int arg2) {
                mouseX = Math.abs(arg1 + arg0.getAbsoluteLeft());
                mouseY = Math.abs(arg2 + arg0.getAbsoluteTop());
            }

            public void onMouseUp(Widget arg0, int arg1, int arg2) {
            }
        });
    }

    public void fetchFavsFromProvider(final ListBox proList, final ListBox favList, final int type) {

        if (type == 0) {
            refreshFavoriteList(favList, type, srcProviderNo);
        } else {
            proList.addClickListener(new ClickListener() {

                public void onClick(Widget arg0) {
                    final String provider_no = proList.getItemText(proList.getSelectedIndex());
                    AsyncCallback callback = new AsyncCallback() {

                        public void onFailure(Throwable arg0) {
                            message.setText("Problematic retrieving data, please refresh page.");
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        public void onSuccess(Object arg0) {
                            if(!((boolean[]) arg0)[1]){
                                    copyToButton.setEnabled(false);
                                    copyBothButton.setEnabled(false);
                            }
                            else{
                                copyToButton.setEnabled(true);
                                copyBothButton.setEnabled(true);
                            }
                        }
                    };
                    getService().getFavoritesPrivilege(provider_no, callback);
                    refreshFavoriteList(favList, type, provider_no);
                }
            });
        }
    }

    public void refreshFavoriteList(final ListBox favList, final int type, final String provider_no) {
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable arg0) {
                message.setText("Problematic retrieving data, please refresh page.");
                throw new UnsupportedOperationException(this.getClass() + " refreshFavoriteList() failed!");
            }

            public void onSuccess(Object arg0) {
                if (type == 0) {
                    srcFavorites.clear();
                    srcFavorites.addAll((List<String[]>) arg0);
                    srcFavList = favList;
                    srcProviderNo = provider_no;
                }
                if (type == 1) {
                    desFavorites.clear();
                    desFavorites.addAll((List<String[]>) arg0);
                    desFavList = favList;
                    desProviderNo = provider_no;
                }
                String favoriteName = null;
                favList.clear();
                Iterator ite = ((List<String[]>) arg0).iterator();
                for (; ite.hasNext();) {
                    favoriteName = ((String[]) (ite.next()))[1];
                    favList.addItem(favoriteName);
                }
            }
        };
        getService().getFavoritesName(provider_no, callback);
    }

    public void fetchDetailFromFavorite(final ListBox favList, final int type) {
        favList.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                int index = favList.getSelectedIndex();
                int height = 0;
                String desc = null;
                if (type == 0) {
                    desc = srcFavorites.get(index)[2];
                } else {
                    desc = desFavorites.get(index)[2];
                }
                if (desc.length() <= 30) {
                    height = 20;
                } else {
                    height = desc.length() / 30 * 20;
                }
                popup.setWidth("200px");
                popup.setHeight(new Integer(height).toString() + "px");
                popup.setPopupPosition(mouseX, mouseY);
                popup.setAnimationEnabled(false);
                popup.setWidget(new HTML(desc));
                popup.show();
            }
        });
    }

    public void compareAndHighlightFav() {
        boolean exist = true;
        if (srcFavorites.size() == 0 && desFavorites.size() != 0) {
            for (int i = 0; i < desFavorites.size(); i++) {
                desFavList.setItemSelected(i, exist);
            }
            return;
        }
        if (desFavorites.size() == 0 && srcFavorites.size() != 0) {
            for (int i = 0; i < srcFavorites.size(); i++) {
                srcFavList.setItemSelected(i, exist);
            }
            return;
        }

        for (int i = 0; i < srcFavorites.size(); i++) {
            srcFavList.setItemSelected(i, false);
            String srcFav = srcFavorites.get(i)[3].trim();
            for (int j = 0; j < desFavorites.size(); j++) {
                exist = true;
                String desFav = desFavorites.get(j)[3].trim();
                if (srcFav.equalsIgnoreCase(desFav)) {
                    break;
                }
                exist = false;
            }
            if (!exist) {
                srcFavList.setItemSelected(i, true);
            }
        }

        for (int i = 0; i < desFavorites.size(); i++) {
            desFavList.setItemSelected(i, false);
            String srcFav = desFavorites.get(i)[3].trim();
            for (int j = 0; j < srcFavorites.size(); j++) {
                exist = true;
                String desFav = srcFavorites.get(j)[3].trim();
                if (srcFav.equalsIgnoreCase(desFav)) {
                    break;
                }
                exist = false;
            }
            if (!exist) {
                desFavList.setItemSelected(i, true);
            }
        }
    }

    public boolean compareSelectedFavorites(int type) {
        boolean ret = true;
        if (type == COPY_TO || type == COPY_BOTH) {
            for (int i = 0; i < srcFavList.getItemCount(); i++) {
                if (!srcFavList.isItemSelected(i)) {
                    continue;
                }
                for (int j = 0; j < desFavorites.size(); j++) {
                    if (srcFavorites.get(i)[3].trim().equalsIgnoreCase(desFavorites.get(j)[3].trim())) {
                        ret = false;
                        return ret;
                    }
                }
            }
        }

        if (type == COPY_FROM || type == COPY_BOTH) {
            for (int i = 0; i < desFavList.getItemCount(); i++) {
                if (!desFavList.isItemSelected(i)) {
                    continue;
                }
                for (int j = 0; j < srcFavorites.size(); j++) {
                    if (desFavorites.get(i)[3].trim().equalsIgnoreCase(srcFavorites.get(j)[3].trim())) {
                        ret = false;
                        return ret;
                    }
                }
            }
        }
        return ret;
    }

    public void copyFavs(int type) {
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable arg0) {
                message.setText("Problematic retrieving data, please refresh page.");
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(Object arg0) {
                deSelectSrc();
                deSelectDes();
                refreshFavoriteList(srcFavList, 0, srcProviderNo);
                refreshFavoriteList(desFavList, 1, desProviderNo);
            }
        };

        if (type == COPY_TO) {
            getService().cloneFavorites(desProviderNo, getSrcIDs(), callback);
        }
        if (type == COPY_FROM) {
            getService().cloneFavorites(srcProviderNo, getDesIDs(), callback);
        }
        if (type == COPY_BOTH) {
            getService().cloneFavorites(desProviderNo, getSrcIDs(), callback);
            getService().cloneFavorites(srcProviderNo, getDesIDs(), callback);
        }
    }

    public List<Integer> getSrcIDs() {
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < srcFavList.getItemCount(); i++) {
            if (srcFavList.isItemSelected(i)) {
                ids.add(new Integer(srcFavorites.get(i)[4]));
            }
        }
        return ids;
    }

    public void deSelectSrc() {
        for (int i = 0; i < srcFavList.getItemCount(); i++) {
            srcFavList.setItemSelected(i, false);
        }
    }

    public List<Integer> getDesIDs() {
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < desFavList.getItemCount(); i++) {
            if (desFavList.isItemSelected(i)) {
                ids.add(new Integer(desFavorites.get(i)[4]));
            }
        }
        return ids;
    }

    public void deSelectDes() {
        for (int i = 0; i < desFavList.getItemCount(); i++) {
            desFavList.setItemSelected(i, false);
        }
    }

    public static RxCloneServiceAsync getService() {
        // Create the client proxy. Note that although you are creating the
        // service interface proper, you cast the result to the asynchronous
        // version of
        // the interface. The cast is always safe because the generated proxy
        // implements the asynchronous interface automatically.
        RxCloneServiceAsync service = (RxCloneServiceAsync) GWT.create(RxCloneService.class);
        // Specify the URL at which our service implementation is running.
        // Note that the target URL must reside on the same domain and port from
        // which the host page was served.
        //
        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "service/rxcloneservice";
        endpoint.setServiceEntryPoint(moduleRelativeURL);
        return service;
    }
}
