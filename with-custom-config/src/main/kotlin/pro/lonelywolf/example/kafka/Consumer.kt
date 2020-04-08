package pro.lonelywolf.example.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.stereotype.Component


@Profile("consumer")
@EnableKafka
@Configuration
class KafkaConsumerConfig {

  @Bean
  fun consumerFactory(): ConsumerFactory<String, String> {
    val props: MutableMap<String, Any> = HashMap()
    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER_ADDRESS
    props[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    return DefaultKafkaConsumerFactory(props)
  }

  @Bean
  fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
    val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
    factory.consumerFactory = consumerFactory()
    return factory
  }
}

@Profile("consumer")
@Component
class Consumer {
  @KafkaListener(topics = [TOPIC], groupId = GROUP_ID)
  fun listen(message: String) {
    println("Received Message in topic $TOPIC group $GROUP_ID: $message")
  }
}