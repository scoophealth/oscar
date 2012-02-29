package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="validations")
public class Validations extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String regularExp;

	private double minValue;

	private double maxValue;

	private int minLength;

	private int maxLength;

	private boolean isNumeric;

	private boolean isTrue;

	private boolean isDate;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getRegularExp() {
    	return regularExp;
    }

	public void setRegularExp(String regularExp) {
    	this.regularExp = regularExp;
    }

	public double getMinValue() {
    	return minValue;
    }

	public void setMinValue(double minValue) {
    	this.minValue = minValue;
    }

	public double getMaxValue() {
    	return maxValue;
    }

	public void setMaxValue(double maxValue) {
    	this.maxValue = maxValue;
    }

	public int getMinLength() {
    	return minLength;
    }

	public void setMinLength(int minLength) {
    	this.minLength = minLength;
    }

	public int getMaxLength() {
    	return maxLength;
    }

	public void setMaxLength(int maxLength) {
    	this.maxLength = maxLength;
    }

	public boolean isNumeric() {
    	return isNumeric;
    }

	public void setNumeric(boolean isNumeric) {
    	this.isNumeric = isNumeric;
    }

	public boolean isTrue() {
    	return isTrue;
    }

	public void setTrue(boolean isTrue) {
    	this.isTrue = isTrue;
    }

	public boolean isDate() {
    	return isDate;
    }

	public void setDate(boolean isDate) {
    	this.isDate = isDate;
    }


}
