# Apache Kafka Tutorials with Spring Boot

## Reference

* [Apache Kafka](https://kafka.apache.org/)
* [Apache ZooKeeper](https://zookeeper.apache.org/)
* [Spring Boot Kafka](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-kafka)

## Prerequisite

* Java 8 or 11 (9, 10 not supported)
* [Kafka packaged with the Confluent Community](https://hub.docker.com/r/confluentinc/cp-kafka/)
* [Exhibitor - ZooKeeper Management](https://hub.docker.com/r/netflixoss/exhibitor/)

## Overview
Apache Kafka® is a distributed streaming platform

### Key Capabilities
* `Message Queue` 또는 `Enterprise Messaging System` 과 유사한 Record Stream 의 `Publish/Subscribe` 기능
* 내구성 있는 방식으로 `fault-tolerant`를 지키며 Record Stream 을 저장 (디스크 기반 데이터 저장 방식)
* Record Stream 이 발생 할 때 바로 처리 (Real-Time)

<p align="center">
  <img src="https://kafka.apache.org/24/images/kafka-apis.png" alt="Apache Kafka Core APIs"/>
</p>