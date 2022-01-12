package com.bank.TransactionValidapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bank.TransactionValidapi.entity.MessageStatus;

public interface MessageRepository extends JpaRepository <MessageStatus, Long>{
	
	
	@Query(value="select mid from messages u where u.mid =:Inumber", nativeQuery=true)
	Integer getmessageid(@Param("Inumber") int Inumber);
	
	
	@Transactional
	@Modifying
	@Query(value="update messages u set u.message =:Dmessage , u.status =:Estatus where u.mid =:Enumber", nativeQuery=true)
	Integer updatemessage(@Param("Dmessage") String Dmessage ,@Param("Estatus") String Estatus, @Param("Enumber") int Enumber);
	

}
