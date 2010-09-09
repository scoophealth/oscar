/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jackson
 */
@Entity
@Table(name = "ctl_document")
@NamedQueries({
    @NamedQuery(name = "CtlDocument.findAll", query = "SELECT c FROM CtlDocument c"),
    @NamedQuery(name = "CtlDocument.findByModule", query = "SELECT c FROM CtlDocument c WHERE c.module = :module"),
    @NamedQuery(name = "CtlDocument.findByModuleId", query = "SELECT c FROM CtlDocument c WHERE c.moduleId = :moduleId"),
    @NamedQuery(name = "CtlDocument.findByDocumentNo", query = "SELECT c FROM CtlDocument c WHERE c.documentNo = :documentNo"),
    @NamedQuery(name = "CtlDocument.findByStatus", query = "SELECT c FROM CtlDocument c WHERE c.status = :status"),
    @NamedQuery(name = "CtlDocument.findById", query = "SELECT c FROM CtlDocument c WHERE c.id = :id")})
public class CtlDocument extends AbstractModel<Integer> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "module")
    private String module;
    @Basic(optional = false)
    @Column(name = "module_id")
    private int moduleId;
    @Basic(optional = false)
    @Column(name = "document_no")
    private int documentNo;
    @Column(name = "status")
    private Character status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    public CtlDocument() {
    }

    public CtlDocument(Integer id) {
        this.id = id;
    }

    public CtlDocument(Integer id, String module, int moduleId, int documentNo) {
        this.id = id;
        this.module = module;
        this.moduleId = moduleId;
        this.documentNo = documentNo;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(int documentNo) {
        this.documentNo = documentNo;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }    

}
