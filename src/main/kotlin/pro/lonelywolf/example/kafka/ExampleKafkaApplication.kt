package pro.lonelywolf.example.kafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExampleKafkaApplication

fun main(args: Array<String>) {
  runApplication<ExampleKafkaApplication>(*args)
}
