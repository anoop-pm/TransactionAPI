package com.bank.TransactionValidapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bank.TransactionValidapi.entity.Transfermessage;


public interface MessageRepository extends JpaRepository <Transfermessage, Long>{

	
	@Transactional
	@Modifying
	@Query(value="update messages u set u.message =:messages where u.messageid =:messageids", nativeQuery=true)
	Integer updatemessage(@Param("messages") String messages,@Param("messageids") int messageids );
	
}
