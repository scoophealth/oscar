package org.oscarehr.integration.cdx.dao;
import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.integration.cdx.model.CdxMessenger;
import org.oscarehr.integration.cdx.model.Notification;
import org.springframework.stereotype.Repository;
import javax.persistence.Query;
import java.util.List;

@Repository

public class CdxMessengerDao extends AbstractDao<CdxMessenger>{

    public CdxMessengerDao(){
        super(CdxMessenger.class);
    }


    public  List<CdxMessenger> findHistory()
    {
        String sql = "SELECT x FROM CdxMessenger x order by x.timeStamp" ;
        Query query = entityManager.createQuery(sql);
        List<CdxMessenger> results = query.getResultList();
        if (results.isEmpty())
            return null;
        else return results;
    }
}
