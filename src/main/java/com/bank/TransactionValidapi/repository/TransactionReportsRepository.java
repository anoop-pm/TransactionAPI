package com.bank.TransactionValidapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bank.TransactionValidapi.entity.TransactionReport;



public interface TransactionReportsRepository extends JpaRepository <TransactionReport, Long>
{
//
//	@Transactional
//	@Modifying
//	@Query("UPDATE useraccounts SET balance  = :balance WHERE userid = :userid")
//	Integer updatebalance(int balance, int userid);
//
	
	@Query(value="select userid from useraccounts u where u.accountnumber =:Hnumber", nativeQuery=true)
	Integer getuserid(@Param("Hnumber") int Hnumber);
	
	@Query(value="select accountnumber from useraccounts u where u.accountnumber =:Anumber", nativeQuery=true)
	Integer getaccountnumber(@Param("Anumber") int Anumber);
	

	@Query(value="select receiveraccountnumber from accounts u where u.receiveraccountnumber =:Bnumber", nativeQuery=true)
	Integer getreceiveraccountnumber(@Param("Bnumber") int Bnumber);
	
	@Query(value="select deposit from useraccounts u where u.accountnumber =:Cnumber", nativeQuery=true)
	Integer getdeposit(@Param("Cnumber") int Cnumber);
	
	@Transactional
	@Modifying
	@Query(value="update useraccounts u set u.deposit =:Dnumber where u.accountnumber =:Enumber", nativeQuery=true)
	Integer updatebalance(@Param("Dnumber") int Dnumber ,@Param("Enumber") int Enumber);
	
	
	

	 
}