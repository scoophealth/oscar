package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ctl_doc_class")
public class CtlDocClass extends AbstractModel<Integer> implements Serializable {

        @Id
        @Column(name = "id")
        private Integer id = null;
	@Column(name = "reportclass")
	private String reportClass = null;
	@Column(name = "subclass")
	private String subClass = null;


        public CtlDocClass() {}

        public String getReportClass() {
            return this.reportClass;
        }
        public void setReportClass(String s) {
            this.reportClass = s;
        }

        public String getSubClass() {
            return this.subClass;
        }
        public void setSubClass(String s) {
            this.subClass = s;
        }

    @Override
        public Integer getId() {
            return this.id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
}
