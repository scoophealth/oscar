
package org.caisi.service;

import java.util.List;

import org.caisi.model.IssueAdmin;

// use your IDE to handle imports
public interface IssueAdminManager {
    public List getIssueAdmins();
    public IssueAdmin getIssueAdmin(String issueAdminId);
    public IssueAdmin saveIssueAdmin(IssueAdmin issueAdmin);
    public void removeIssueAdmin(String issueAdminId);
}

