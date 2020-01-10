package com.example;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${bootstrap.server:kafka-kafka-bootstrap:9093}")
    private String bootstrapServer;

    @Value("${trust.store.location}")
    private String trustStoreLocation;

    @Value("${trust.store.password}")
    private String trustStorePassword;

    @Value("${keystore.location}")
    private String keystoreLocation;

    @Value("${keystore.password}")
    private String keystorePassword;

    @Value("${keystore.key}")
    private String keystoreKey;

    @Value("${security.protocol:SSL}")
    private String securityProtocol;

    @Value("${sasl.mechanism}")
    private String saslMechanism;

    @Value("${ssl.endpoint.identification.algorithm:none}")
    private String sslEndpointIdentificationAlgorithm;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put("sasl.mechanism", saslMechanism);
        // Configure SASL_SSL if SSL encryption is enabled, otherwise configure SASL_PLAINTEXT
        props.put("security.protocol", securityProtocol);
        props.put("ssl.truststore.location", trustStoreLocation);
        props.put("ssl.truststore.password", trustStorePassword);

        props.put("ssl.key.password", keystorePassword);
        props.put("ssl.keystore.password", keystorePassword);
        props.put("ssl.keystore.location", keystoreLocation);

        props.put("ssl.endpoint.identification.algorithm", sslEndpointIdentificationAlgorithm);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}