package com.bank.TransactionValidapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.TransactionValidapi.entity.TransactionDetails;



@Repository
public interface TransactionRepository  extends JpaRepository <TransactionDetails, Long>{

}
