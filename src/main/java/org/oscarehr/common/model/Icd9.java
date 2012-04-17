package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="icd9")
public class Icd9 extends AbstractModel<Integer> implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
     private String icd9;
     private String description;

    public Icd9() {
    }

    public Icd9(String icd9, String description) {
       this.icd9 = icd9;
       this.description = description;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getIcd9() {
        return this.icd9;
    }

    public void setIcd9(String icd9) {
        this.icd9 = icd9;
    }
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}


