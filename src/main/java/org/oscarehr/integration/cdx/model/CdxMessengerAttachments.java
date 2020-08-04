package org.oscarehr.integration.cdx.model;
import org.oscarehr.common.model.AbstractModel;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="cdx_messengerAttachments")
public class CdxMessengerAttachments extends AbstractModel<Integer> {
    public static final String DOCTYPE_DOC = "D";
    public static final String DOCTYPE_EFORM = "E";
    public static final String DOCTYPE_LAB = "L";
    public static final String DELETED = "Y";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int requestId;

    @Column(name = "document_no")
    private int documentNo;

    @Column(name = "doctype")
    private String docType;

    private String deleted;

    @Column(name = "attach_date")
    @Temporal(TemporalType.DATE)
    private Date attachDate;
    @Column(name = "demo_no")
    private int demoNo;

    public CdxMessengerAttachments() {
    }

    public CdxMessengerAttachments(int requestId, int documentNo, String docType, int demoNo) {
        setRequestId(requestId);
        setDocumentNo(documentNo);
        setDocType(docType);
        setDemoNo(demoNo);
        setAttachDate(new Date());
    }



    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(int documentNo) {
        this.documentNo = documentNo;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Date getAttachDate() {
        return attachDate;
    }

    public void setAttachDate(Date attachDate) {
        this.attachDate = attachDate;
    }


    public int getDemoNo() {
        return demoNo;
    }

    public void setDemoNo(int demoNo) {
        this.demoNo = demoNo;
    }




}