package com.bank.TransactionValidapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "messages")
public class Transfermessage {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	
	@Column(name = "messageid",  length = 20)
    private int messageid;

   
   @Column(name = "message",  length = 20)
   private String message;

public Transfermessage() {
	super();
	// TODO Auto-generated constructor stub
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}



public int getMessageid() {
	return messageid;
}

public void setMessageid(int messageid) {
	this.messageid = messageid;
}



public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}
   
   
   

}
