package pro.lonelywolf.example.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@SpringBootApplication
class ExampleKafkaApplication {

}

fun main(args: Array<String>) {
  runApplication<ExampleKafkaApplication>(*args)
}

@Component
class MyTopic : NewTopic("my-topic", 1, 1)


@Component
class Producer(private val kafkaTemplate: KafkaTemplate<String, String>) {
  @PostConstruct
  fun sendMessage() {
    kafkaTemplate.send("my-topic", "Hello world")
  }
}

@Component
class Consumer {
  @KafkaListener(topics = ["my-topic"])
  fun processMessage(content: String) {
    println("Receive : $content")
  }
}