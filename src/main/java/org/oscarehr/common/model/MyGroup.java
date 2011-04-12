package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="mygroup")
public class MyGroup  extends AbstractModel<MyGroupPrimaryKey> {

	@EmbeddedId     
	private MyGroupPrimaryKey id;
	
	@Column(name="last_name")
    private String lastName;
	@Column(name="first_name")
    private String firstName;
	@Column(name="vieworder")
    private String viewOrder;

    public MyGroup() {
    	//empty
    }

	
    public MyGroup(MyGroupPrimaryKey id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }
    public MyGroup(MyGroupPrimaryKey id, String lastName, String firstName, String vieworder) {
       this.id = id;
       this.lastName = lastName;
       this.firstName = firstName;
       this.viewOrder = vieworder;
    }
   
    @Override
    public MyGroupPrimaryKey getId() {
        return this.id;
    }
    
    public void setId(MyGroupPrimaryKey id) {
        this.id = id;
    }
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getViewOrder() {
        return this.viewOrder;
    }
    
    public void setViewOrder(String viewOrder) {
        this.viewOrder = viewOrder;
    }




}


