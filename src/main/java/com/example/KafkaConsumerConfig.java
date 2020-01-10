package com.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${bootstrap.server:kafka-kafka-bootstrap:9093}")
    private String bootstrapServer;

    @Value("${truststore.location}")
    private String truststoreLocation;

    @Value("${truststore.password}")
    private String truststorePassword;

    @Value("${keystore.location}")
    private String keystoreLocation;

    @Value("${keystore.password}")
    private String keystorePassword;

    @Value("${keystore.key}")
    private String keystoreKey;

    @Value("${security.protocol:SSL}")
    private String securityProtocol;

    @Value("${sasl.mechanism:}")
    private String saslMechanism;

    @Value("${consumer.group.id:my-group}")
    private String consumerGroupId;

    @Value("${ssl.endpoint.identification.algorithm:none}")
    private String sslEndpointIdentificationAlgorithm;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put("sasl.mechanism", saslMechanism);
        // Configure SASL_SSL if SSL encryption is enabled, otherwise configure SASL_PLAINTEXT
        props.put("security.protocol", securityProtocol);
        props.put("ssl.truststore.location", truststoreLocation);
        props.put("ssl.truststore.password", truststorePassword);

        props.put("ssl.key.password", keystorePassword);
        props.put("ssl.keystore.password", keystorePassword);
        props.put("ssl.keystore.location", keystoreLocation);

        props.put("ssl.endpoint.identification.algorithm", sslEndpointIdentificationAlgorithm);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> headersKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}