
package org.caisi.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.dao.IssueAdminDAO;
import org.caisi.model.IssueAdmin;
import org.caisi.service.IssueAdminManager;

// use your IDE to handle imports
public class IssueAdminManagerImpl implements IssueAdminManager {
    private static Log log = LogFactory.getLog(IssueAdminManagerImpl.class);
    private IssueAdminDAO dao;
    public void setIssueAdminDAO(IssueAdminDAO dao) {
        this.dao = dao;
    }
    public List getIssueAdmins() {
        return dao.getIssueAdmins();
    }
    public IssueAdmin getIssueAdmin(String issueAdminId) {
        IssueAdmin issueAdmin = dao.getIssueAdmin(Long.valueOf(issueAdminId));
        if (issueAdmin == null) {
            log.warn("UserId '" + issueAdminId + "' not found in database.");
        }
        return issueAdmin;
    }
    public IssueAdmin saveIssueAdmin(IssueAdmin issueAdmin) {
        dao.saveIssueAdmin(issueAdmin);
        return issueAdmin;
    }
    public void removeIssueAdmin(String issueAdminId) {
        dao.removeIssueAdmin(Long.valueOf(issueAdminId));
    }
}

