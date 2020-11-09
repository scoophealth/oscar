package org.oscarehr.integration.cdx.dao;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.integration.cdx.model.CdxClinics;
import org.oscarehr.integration.cdx.model.CdxMessenger;
import org.oscarehr.integration.cdx.model.CdxProvenance;
import org.oscarehr.integration.cdx.model.Notification;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.List;

@Repository

public class CdxMessengerDao extends AbstractDao<CdxMessenger>{

    public CdxMessengerDao(){
        super(CdxMessenger.class);
    }
    public CdxMessenger getCdxMessenger(int id) {
        return find(id);
    }

    public  List<CdxMessenger> findHistory()
    {
        String sql = "SELECT x FROM CdxMessenger x where x.draft<>'Y' order by x.timeStamp desc " ;
        Query query = entityManager.createQuery(sql);
        List<CdxMessenger> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results;
    }

    public  List<CdxMessenger> findDraft()
    {
        String sql = "SELECT x FROM CdxMessenger x where x.draft='Y' order by x.timeStamp desc " ;
        Query query = entityManager.createQuery(sql);
        List<CdxMessenger> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results;
    }



    public void deleteDraftById(int id) {
        Query query = entityManager.createQuery("DELETE FROM CdxMessenger x WHERE x.id="+id);
        query.executeUpdate();
    }


}
