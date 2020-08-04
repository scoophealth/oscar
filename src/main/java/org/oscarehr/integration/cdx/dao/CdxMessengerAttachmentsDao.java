package org.oscarehr.integration.cdx.dao;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.common.model.ConsultDocs;
import org.oscarehr.integration.cdx.model.CdxMessengerAttachments;

import javax.persistence.Query;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")

public class CdxMessengerAttachmentsDao extends AbstractDao<CdxMessengerAttachments> {

    public CdxMessengerAttachmentsDao(){
        super(CdxMessengerAttachments.class);
    }


    public List<CdxMessengerAttachments> findByRequestIdDocNoDocType(Integer requestId, Integer documentNo, String docType,Integer demoNo) {
        String sql = "select x from CdxMessengerAttachments x where x.requestId=? and x.documentNo=? and x.docType=? and x.demoNo=? and x.deleted is NULL";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1,requestId);
        query.setParameter(2,documentNo);
        query.setParameter(3,docType);
        query.setParameter(4,demoNo);

        List<CdxMessengerAttachments> results = query.getResultList();
        return results;
    }

    public List<CdxMessengerAttachments> findByRequestId(Integer requestId) {
        String sql = "select x from CdxMessengerAttachments x where x.requestId=? and x.deleted is NULL";
        Query query = entityManager.createQuery(sql);
        query.setParameter(1,requestId);

        List<CdxMessengerAttachments> results = query.getResultList();
        return results;
    }

    /*public List<Object[]> findLabs(Integer consultationId) {
        String sql = "FROM ConsultDocs cd, PatientLabRouting plr " +
                "WHERE plr.labNo = cd.documentNo " +
                "AND cd.requestId = :consultationId " +
                "AND cd.docType = :docType " +
                "AND cd.deleted IS NULL " +
                "ORDER BY cd.documentNo";
        Query q = entityManager.createQuery(sql);
        q.setParameter("consultationId", consultationId);
        q.setParameter("docType", ConsultDocs.DOCTYPE_LAB);
        return q.getResultList();
    }
    */

}

