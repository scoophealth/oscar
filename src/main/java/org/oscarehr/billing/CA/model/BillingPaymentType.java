package org.oscarehr.billing.CA.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name="billing_payment_type")
public class BillingPaymentType extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="payment_type")
	private String paymentType;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getPaymentType() {
    	return paymentType;
    }

	public void setPaymentType(String paymentType) {
    	this.paymentType = paymentType;
    }


}
