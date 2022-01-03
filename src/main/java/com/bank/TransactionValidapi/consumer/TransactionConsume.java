package com.bank.TransactionValidapi.consumer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bank.TransactionValidapi.entity.TransactionDetails;
import com.bank.TransactionValidapi.entity.TransactionReport;
import com.bank.TransactionValidapi.entity.Transfermessage;
import com.bank.TransactionValidapi.repository.MessageRepository;
import com.bank.TransactionValidapi.repository.TransactionReportsRepository;
import com.bank.TransactionValidapi.repository.transactionRepository;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Instant;
import java.util.Properties;


@Service
public class TransactionConsume {
	
	
	@Autowired
    private TransactionReportsRepository validrepo;
	
	@Autowired
    private MessageRepository transfermessage;
	
	@Autowired
    private transactionRepository tranreoistory;
	
	
	
	@KafkaListener(topics = "kafka-testing2")
    public void consume(String message) {
       
        
        
        JSONObject json = new JSONObject(message);
        System.out.println(json.get("AccountNumber").toString());
        
        
        String saccountno = json.get("AccountNumber").toString();

        int validaccno=Integer.parseInt(saccountno);
        
        int searchaccno=0;
        
        
        String raccountno = json.get("RAccountNumber").toString();

        int validraccno=Integer.parseInt(raccountno);
        
        int searchraccno=0;
        
        
        String jamount = json.get("balance").toString();

        int validamount=Integer.parseInt(jamount);
        
        
  
        
        int samount=0;
        int getuserid;
        String report;
        
        
        try {
        	
        	searchaccno=validrepo.getaccountnumber(validaccno);
        	searchraccno=validrepo.getreceiveraccountnumber(validraccno);
        	samount=validrepo.getdeposit(validaccno);
        }
        catch(Exception e) {
        	
        	System.out.println("Not Matched"+e.getMessage());
        }
        

        
        System.out.println("Accno"+searchaccno);
        System.out.println("RAccno"+searchraccno);
        System.out.println("deposit"+samount);
        System.out.println("validamount"+validamount);
        
       
        
        
        
        int sumamount =0;
        
         report=null;
        
        if(validaccno==searchaccno && searchraccno==validraccno && samount >= validamount) {
        	
        	sumamount=samount - validamount;
        	report="Transfered";
        	
        	validrepo.updatebalance(sumamount, searchaccno) ;
        	
        	TransactionDetails trans = new TransactionDetails();
        	
        	trans.setAccountnumber(searchaccno);
        	trans.setReceiveraccount(validraccno);
        	trans.setAmount(validamount);
        	
        	 Instant now = Instant.now();
        	 
        	 trans.setDetails("Debited");
        	 
        	 trans.setTime(now.toString());
        	 
        	 tranreoistory.save(trans);
        	 
        	 
        	
        	
        	
        	
            
        }
        else if(searchaccno==0) {
        	
        	
        	
        	report="not Transfered Sending accountno not matched";
        	
        
        	
        }
        
        else if(searchraccno==0) {
        	
        	report="not Transfered Reciver accountno not matched";
        }
        
        else {
        	
        	report="not enough Balance";
        	
        }
        System.out.println("Message: "+report);
        
        
        
      //message
    	Transfermessage transmess=new Transfermessage();
    	
    	
    	transfermessage.updatemessage(report,1);
    	
    	
        transactionproducer(String.valueOf(validaccno),String.valueOf(validraccno),report,String.valueOf(validamount));
    	

       
        
	}
	
	
	
	
	
	
	
	public void transactionproducer(String sac,String rac,String message,String validamount) {
		
		
		Properties properties = new Properties();

        // kafka bootstrap server
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // producer acks
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // strongest producing guarantee
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
        // leverage idempotent producer from Kafka 0.11 !
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // ensure we don't push duplicates

        Producer<String, String> producer = new KafkaProducer<>(properties);


            try {
                producer.send(newRandomTransaction(sac,rac,message,validamount));
                Thread.sleep(100);


            } catch (InterruptedException e) {

            }

        producer.close();
    }

    public static ProducerRecord<String, String> newRandomTransaction(String accno,String rno,String report,String validamount) {
        // creates an empty json {}
        ObjectNode transactionr = JsonNodeFactory.instance.objectNode();

     
        Instant now = Instant.now();

        // we write the data to the json document
        transactionr.put("SenderAccountnumber", accno);
        transactionr.put("ReceiverAccountnumber", rno);
        transactionr.put("Report", report);
        transactionr.put("Amountb", validamount);
        transactionr.put("time", now.toString());
        return new ProducerRecord<>("kafka-testing7", "1", transactionr.toString());
    }
		
	}
	

	
	
