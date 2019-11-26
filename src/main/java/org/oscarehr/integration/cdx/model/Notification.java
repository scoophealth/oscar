package org.oscarehr.integration.cdx.model;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.model.AbstractModel;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification extends AbstractModel<Integer> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Column(name = "message")
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "generatedAt")
    private Date generatedAt;
    @Column(name = "seenBy")
    private String seenBy;
    @Column(name = "seenAt")
    private Date seenAt;

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = StringUtils.trimToNull(type);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message =StringUtils.trimToNull(message);
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }
    public String getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(String seenBy) {
        this.seenBy = StringUtils.trimToNull(seenBy);
    }

    public Date getSeenAt() {
        return seenAt;
    }

    public void setSeenAt(Date seenAt) {
        this.seenAt = seenAt;
    }

    @Override
    public Integer getId() {
        return(id);
    }


}
