package org.oscarehr.integration.cdx.model;

import org.oscarehr.common.model.AbstractModel;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cdx_messenger")
public class CdxMessenger extends AbstractModel<Integer> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "doc_id")
    private String documentId;
    @Column(name = "author")
    private String author;

    @Column(name = "patient")
    private String patient;
    @Column(name = "recipients")
    private String recipients;
    @Column(name = "document_type")
    private String documentType;
    @Column(name = "category")
    private String category;
    @Column(name = "content")
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_stamp")
    private Date timeStamp;

    @Column(name = "delivery_status")
    private String deliveryStatus;
    @Column(name = "primary_recipient")
    private String primaryRecipient;


    @Column(name = "secondary_recipient")
    private String secondaryRecipient;



    @Column(name = "draft")
    private String draft;

    @PrePersist
    protected void onCreate() {
        timeStamp= new Date();
    }


    @Override
    public Integer getId() {
        return(id);
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getPrimaryRecipient() {
        return primaryRecipient;
    }

    public void setPrimaryRecipient(String primaryRecipient) {
        this.primaryRecipient = primaryRecipient;
    }

    public String getSecondaryRecipient() {
        return secondaryRecipient;
    }

    public void setSecondaryRecipient(String secondaryRecipient) {
        this.secondaryRecipient = secondaryRecipient;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }
}
