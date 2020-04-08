package pro.lonelywolf.example.kafka

import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Profile("consumer")
@Component
class Consumer {
  @KafkaListener(topics = [TOPIC], groupId = GROUP_ID)
  fun listen(message: String) {
    println("Received Message in topic $TOPIC group $GROUP_ID: $message")
  }
}