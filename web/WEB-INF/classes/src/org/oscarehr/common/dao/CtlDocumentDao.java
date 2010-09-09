/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;
import java.util.List;
import javax.persistence.Query;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.Demographic;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jackson
 */
@Repository
public class CtlDocumentDao extends AbstractDao<CtlDocument>{

    //Logger logger=Logger.getLogger( CtlDocumentDao.class );

    public CtlDocumentDao(){
        super(CtlDocument.class);
    }

    public Demographic getDemographicFromDocumentNo(String segmentID){
                 String q="select d from Demographic d, CtlDocument c where c.module='demographic'"
                         + " and c.moduleId!='-1' and c.moduleId=d.DemographicNo and c.documentNo= "+segmentID;
                 Query query=entityManager.createQuery(q);                 
                 List<Demographic> r=query.getResultList();
                 if(r!=null && r.size()>0){
                     return r.get(0);
                 }else
                     return null;
    }
}
