package org.oscarehr.integration.cdx.dao;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.integration.cdx.model.CdxMessenger;
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

    public CdxMessenger findByDocumentID(String documentId) {
        String sql = "SELECT c FROM CdxMessenger c where c.documentId = :documentId ORDER BY c.timeStamp desc";
        Query query = entityManager.createQuery(sql);
        query.setParameter("documentId", documentId);
        List<CdxMessenger> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results.get(0);
    }


    public void deleteDraftById(int id) {
        Query query = entityManager.createQuery("DELETE FROM CdxMessenger x WHERE x.id="+id);
        query.executeUpdate();
    }


}
