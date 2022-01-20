package com.bank.TransactionValidapi.config;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProducerConfigbanks {
	
	
	public Properties kafkaproducer()
	{
		Properties properties = new Properties();
		// kafka bootstrap server
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "bankkafka:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		// producer acks
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // strongest producing guarantee
		properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
		properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
		// leverage idempotent producer from Kafka 0.11 !
		properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // ensure we don't push duplicates
		return properties;
	}

}
