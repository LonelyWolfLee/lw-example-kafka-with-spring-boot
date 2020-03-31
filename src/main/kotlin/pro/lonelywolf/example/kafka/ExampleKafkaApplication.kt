package pro.lonelywolf.example.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.SendResult
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback

const val BOOTSTRAP_SERVER_ADDRESS = "localhost:29092"
const val TOPIC = "myTopic"
const val GROUP_ID = "myGroup"


@SpringBootApplication
@EnableScheduling
class ExampleKafkaApplication

fun main(args: Array<String>) {
  runApplication<ExampleKafkaApplication>(*args)
}

@Profile("producer")
@Configuration
class KafkaTopicConfig {

  @Bean
  fun kafkaAdmin(): KafkaAdmin {
    val configs: MutableMap<String, Any> = HashMap()
    configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER_ADDRESS
    return KafkaAdmin(configs)
  }

  @Bean
  fun topic1(): NewTopic {
    return NewTopic(TOPIC, 1, 1.toShort())
  }
}

@Profile("producer")
@Configuration
class KafkaProducerConfig {

  @Bean
  fun producerFactory(): ProducerFactory<String, String> {
    val configProps: MutableMap<String, Any> = HashMap()
    configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVER_ADDRESS
    configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    return DefaultKafkaProducerFactory(configProps)
  }

  @Bean
  fun kafkaTemplate(): KafkaTemplate<String, String> {
    return KafkaTemplate(producerFactory())
  }
}

@Profile("producer")
@Component
class KafkaProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {

  @Scheduled(fixedDelay = 1000, initialDelay = 500)
  fun send() {
    sendMessage("Now is ${System.currentTimeMillis()}")
  }

  fun sendMessage(message: String) {
    val future = kafkaTemplate.send(TOPIC, message)
    future.addCallback(object : ListenableFutureCallback<SendResult<String, String>> {
      override fun onSuccess(result: SendResult<String, String>?) {
        println("Sent message=[$message] with offset=[${result!!.recordMetadata.offset()}]")
      }

      override fun onFailure(ex: Throwable) {
        println("Unable to send message=[$message] due to : " + ex.message)
      }
    })
  }
}

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
    println("Received Message in group foo: $message")
  }
}