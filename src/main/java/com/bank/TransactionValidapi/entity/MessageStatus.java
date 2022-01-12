package com.bank.TransactionValidapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


	@Entity
	@Table(name = "messages")
	public class MessageStatus {
		
		  @Id
		    @GeneratedValue(strategy = GenerationType.IDENTITY)
		    private Long id;
		     
		   @Column(name = "mid",  length = 100)
		    private int mid;
		   
		   @Column(name = "status",  length = 100)
		    private String status;
		   
		   @Column(name = "message",  length = 200)
		    private String message;

		

		public MessageStatus() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public int getMid() {
			return mid;
		}

		public void setMid(int mid) {
			this.mid = mid;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
		   


}

