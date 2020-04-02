package pro.lonelywolf.example.kafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
class ExampleKafkaApplication

fun main(args: Array<String>) {
  runApplication<ExampleKafkaApplication>(*args)
}

