package org.caisi.dao;
import java.util.*;
import org.caisi.model.IssueAdmin;

// use your IDE to handle imports
public interface IssueAdminDAO extends DAO {
    public List getIssueAdmins();
    public IssueAdmin getIssueAdmin(Long issueId);
    public void saveIssueAdmin(IssueAdmin issueAdmin);
    public void removeIssueAdmin(Long issueId);
}
