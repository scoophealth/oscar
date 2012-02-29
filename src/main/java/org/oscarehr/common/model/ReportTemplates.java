package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reportTemplates")
public class ReportTemplates extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="templateid")
	private Integer id;

	@Column(name="templatetitle")
	private String templateTitle;

	@Column(name="templatedescription")
	private String templateDescription;

	@Column(name="templatesql")
	private String templateSql;

	@Column(name="templatexml")
	private String templateXml;

	private int active;

	private String type;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getTemplateTitle() {
    	return templateTitle;
    }

	public void setTemplateTitle(String templateTitle) {
    	this.templateTitle = templateTitle;
    }

	public String getTemplateDescription() {
    	return templateDescription;
    }

	public void setTemplateDescription(String templateDescription) {
    	this.templateDescription = templateDescription;
    }

	public String getTemplateSql() {
    	return templateSql;
    }

	public void setTemplateSql(String templateSql) {
    	this.templateSql = templateSql;
    }

	public String getTemplateXml() {
    	return templateXml;
    }

	public void setTemplateXml(String templateXml) {
    	this.templateXml = templateXml;
    }

	public int getActive() {
    	return active;
    }

	public void setActive(int active) {
    	this.active = active;
    }

	public String getType() {
    	return type;
    }

	public void setType(String type) {
    	this.type = type;
    }



}
