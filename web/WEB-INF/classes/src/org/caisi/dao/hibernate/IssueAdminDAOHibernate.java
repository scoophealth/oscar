
package org.caisi.dao.hibernate;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.dao.IssueAdminDAO;
import org.caisi.model.IssueAdmin;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

// use your IDE to handle imports
public class IssueAdminDAOHibernate extends HibernateDaoSupport implements
IssueAdminDAO {
    private Log log = LogFactory.getLog(IssueAdminDAOHibernate.class);
    public List getIssueAdmins() {
         return getHibernateTemplate().find("from IssueAdmin");
    }
    public IssueAdmin getIssueAdmin(Long id) {
         return (IssueAdmin) getHibernateTemplate().get(IssueAdmin.class, id);
    }
    public void saveIssueAdmin(IssueAdmin issueAdmin) {
         getHibernateTemplate().saveOrUpdate(issueAdmin);
         
         if (log.isDebugEnabled()) {
             log.debug("issueAdminId set to:"+issueAdmin.getId());
         }
    }
    public void removeIssueAdmin(Long id) {
      Object issueAdmin = getHibernateTemplate().load(IssueAdmin.class, id);
         getHibernateTemplate().delete(issueAdmin);
    }
}

