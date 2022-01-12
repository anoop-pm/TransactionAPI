package com.bank.TransactionValidapi.consumer;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bank.TransactionValidapi.entity.MessageStatus;
import com.bank.TransactionValidapi.entity.TransactionDetails;
import com.bank.TransactionValidapi.entity.TransactionReport;
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
	private MessageRepository messagerepo;

	@Autowired
	private transactionRepository tranreoistory;

	// Consume Data
	@KafkaListener(topics = "kafka-testing2")
	public void consume(String message) {

		JSONObject json = new JSONObject(message);
		System.out.println(json.get("AccountNumber").toString());

		String saccountno = json.get("AccountNumber").toString();

		int validaccno = Integer.parseInt(saccountno);

		int searchaccno = 0;

		String raccountno = json.get("RAccountNumber").toString();

		int validraccno = Integer.parseInt(raccountno);

		int searchraccno = 0;

		String jamount = json.get("balance").toString();

		int validamount = Integer.parseInt(jamount);

		// message data if not exist

		int messageid = 0;
		try {

			messageid = messagerepo.getmessageid(1);
		} catch (Exception e) {
		}

		if (messageid == 0) {
			MessageStatus msg = new MessageStatus();

			msg.setMessage("Transfered");
			msg.setMid(1);
			msg.setStatus("200");

			try {
				Thread.sleep(2000);
				messagerepo.save(msg);
			} catch (Exception e) {

			}
		}

		int samount = 0;
		int getuserid = 0;
		String report;

		try {

			searchaccno = validrepo.getaccountnumber(validaccno);
			searchraccno = validrepo.getreceiveraccountnumber(validraccno);
			samount = validrepo.getdeposit(validaccno);
			getuserid = validrepo.getuserid(validaccno);
		} catch (Exception e) {

			System.out.println("Not Matched" + e.getMessage()); //use logger
		}

		// test data
		System.out.println("Accno" + searchaccno);
		System.out.println("RAccno" + searchraccno);
		System.out.println("deposit" + samount);
		System.out.println("validamount" + validamount);

		int sumamount = 0;

		report = null;
		String status = null;
		String messagess = null;

		if (validaccno == searchaccno && searchraccno == validraccno && samount >= validamount) {

			sumamount = samount - validamount;
			report = "Transfered";
			status = "200";
			messagess = "Transfered Successfully " + validamount + " :,Your Balance is :" + sumamount;

			validrepo.updatebalance(sumamount, searchaccno);

			TransactionDetails trans = new TransactionDetails();

			trans.setSenderaccountnumber(searchaccno);
			trans.setReceiveraccountnumber(validraccno);
			trans.setAmount(validamount);

			Instant now = Instant.now();

			trans.setDetails("Debited");

			trans.setTime(now.toString());
			trans.setUserid(getuserid);

			tranreoistory.save(trans);

		} else if (searchaccno == 0) {

			report = "Not Transfered Sending Account Number not matched, ";
			messagess = "Not Transfered Sending Account Number not matched, ";
			status = "400";

		}

		else if (searchraccno == 0) {

			report = "Not Transfered Receiver Account Number not matched";
			messagess = "Not Transfered Receiver Account Number not matched";
			status = "400";
		}

		else {

			report = "Not enough Balance";
			messagess = "Not enough Balance";
			status = "400";

		}
		System.out.println("Message: " + report);

		messagerepo.updatemessage(messagess, status, 1);

		System.out.println("Messageid" + messageid);

		// Produce Output Data
		transactionproducer(String.valueOf(validaccno), String.valueOf(validraccno), report,
				String.valueOf(validamount));

	}

	public void transactionproducer(String sac, String rac, String message, String validamount) {

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
			producer.send(newRandomTransaction(sac, rac, message, validamount));
			Thread.sleep(100);

		} catch (InterruptedException e) {

		}

		producer.close();
	}

	public static ProducerRecord<String, String> newRandomTransaction(String accno, String rno, String report,
			String validamount) {

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
