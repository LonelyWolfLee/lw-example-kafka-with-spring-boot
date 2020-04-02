package pro.lonelywolf.example.kafka

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.SendResult
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.util.concurrent.ListenableFutureCallback


@Profile("producer")
@Configuration
class KafkaTopicConfig {

  @Bean
  fun topic1(): NewTopic {
    return NewTopic(TOPIC, 1, 1.toShort())
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