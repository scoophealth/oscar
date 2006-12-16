package org.oscarehr.casemgmt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SetRolesOnOldNotes {
	protected final Log log = LogFactory.getLog(getClass());
	
    protected ApplicationContext ctx = null;
    
    CaseManagementNoteDAO dao = null;
    SessionFactory sf = null;
    
    public SetRolesOnOldNotes() {
    	String[] paths = {"/WEB-INF/applicationContext-test.xml"};
    	ctx = new ClassPathXmlApplicationContext(paths);
    	sf = (SessionFactory) ctx.getBean("sessionFactory");

    	 Session s = sf.openSession();
         TransactionSynchronizationManager
         .bindResource(sf, new SessionHolder(s));
    }
    
    private Map loadProviderRoleMap() throws Exception {
    	Map providerRoleMap = new HashMap();
    	File f= new File("/home/marc/roles.output");
    	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
    	String line = null;
    	while( (line=in.readLine()) != null) {
    		String providerNo =line.substring(0,line.indexOf(','));
    		String roles = line.substring(line.indexOf(',')+1);
    		providerRoleMap.put(providerNo, roles);
    		//System.out.println(providerNo + "-" + roles);
    	}
    	return providerRoleMap;
    }
    public void update1() throws Exception {
    	//load provider/role list
    	Map providerRoleMap = loadProviderRoleMap();
    	
    	//iterate through each note
    	 dao = (CaseManagementNoteDAO)ctx.getBean("CaseManagementNoteDAO");
    	List ids = dao.getAllNoteIds();
    	for(Iterator iter=ids.iterator();iter.hasNext();) {
    		Long id = (Long)iter.next();
    		CaseManagementNote note = dao.getNote(id);
    		if(note.getReporter_caisi_role().equals("")) {
    			//we have the program
    			String providerNo = note.getProvider_no();
    			String roles = (String)providerRoleMap.get(providerNo);
    			if(roles != null && !roles.equals("") && roles.indexOf(',') == -1) {
    				System.out.println("note_id=" + note.getId() + ",programId=" + note.getProgram_no() + ",providerNo=" + providerNo + ",roles=" + roles);
    				note.setReporter_caisi_role(roles);
    				dao.saveNote(note);
    			}
    		}
        	
        	//System.out.println(note.getProvider_no());
    		//System.out.println(note.getProgram_no());
    	}
    	
    }

    public void assignRoleToNote(Long noteId,Map providerRoleMap) {
    	CaseManagementNote note = dao.getNote(noteId);
    	
    	System.out.println(note.getProvider_no());
    }
    
    
    public void close() {
    	SessionHolder holder = (SessionHolder)
        TransactionSynchronizationManager.getResource(sf);
        Session s = holder.getSession();
        s.flush();
        TransactionSynchronizationManager.unbindResource(sf);
        SessionFactoryUtils.releaseSession(s, sf);
    }
    public static void main(String args[]) throws Exception {
    	SetRolesOnOldNotes prog = new SetRolesOnOldNotes();
    	prog.update1();
    	
    	prog.close();
    }
    
}
