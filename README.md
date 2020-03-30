# Apache Kafka Tutorials with Spring Boot

## Reference

* [Apache Kafka](https://kafka.apache.org/)
* [Apache ZooKeeper](https://zookeeper.apache.org/)
* [Spring Boot Kafka](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-kafka)

## Prerequisite

* Java 8 or 11 (9, 10 not supported)
* [Kafka/ZooKeeper packaged with the Confluent Community](https://docs.confluent.io/3.1.1/cp-docker-images/docs/quickstart.html)
* [Exhibitor - ZooKeeper Management](https://hub.docker.com/r/netflixoss/exhibitor/)

## Installation

### [optional] Docker Machine

만약 Docker 환경이 설치 되어있는 가상 환경을 구축하고 싶다면 docker machine 을 설치해서 아래의 과정을 진행하여 새로운 VirtualBox Machine 을 만들고 해당 Machine 에 접속합니다. VirtualBox 와 Docker Machine 이 설치가 되어있어야 합니다. 

##### Create and configure the Docker Machine
```shell script
docker-machine create --driver virtualbox --virtualbox-memory 6000 confluent
```

##### Configure your terminal window to attach it to your new Docker Machine
```shell script
eval $(docker-machine env confluent)
```

### [required] ZooKeeper

```shell script
docker run -d \
    --net=host \
    --name=zookeeper \
    -e ZOOKEEPER_CLIENT_PORT=32181 \
    confluentinc/cp-zookeeper:3.1.1
```

### [required] Kafka

```shell script
docker run -d \
    --net=host \
    --name=kafka \
    -e KAFKA_ZOOKEEPER_CONNECT=localhost:32181 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:29092 \
    confluentinc/cp-kafka:3.1.1
```

### [optional] Confluent Control Center

```shell script
docker run -d \
  --name=control-center \
  --net=host \
  --ulimit nofile=16384:16384 \
  -p 9021:9021 \
  -v /tmp/control-center/data:/var/lib/confluent-control-center \
  -e CONTROL_CENTER_ZOOKEEPER_CONNECT=localhost:32181 \
  -e CONTROL_CENTER_BOOTSTRAP_SERVERS=localhost:29092 \
  -e CONTROL_CENTER_REPLICATION_FACTOR=1 \
  -e CONTROL_CENTER_MONITORING_INTERCEPTOR_TOPIC_PARTITIONS=1 \
  -e CONTROL_CENTER_INTERNAL_TOPICS_PARTITIONS=1 \
  -e CONTROL_CENTER_STREAMS_NUM_STREAM_THREADS=2 \
  -e CONTROL_CENTER_CONNECT_CLUSTER=http://localhost:28082 \
  confluentinc/cp-enterprise-control-center:3.1.1
```

## Overview
Apache Kafka® is a distributed streaming platform

### Key Capabilities
* `Message Queue` 또는 `Enterprise Messaging System` 과 유사한 Record Stream 의 `Publish/Subscribe` 기능
* 내구성 있는 방식으로 `fault-tolerant`를 지키며 Record Stream 을 저장 (디스크 기반 데이터 저장 방식)
* Record Stream 이 발생 할 때 바로 처리 (Real-Time)

<p align="center">
  <img src="https://kafka.apache.org/24/images/kafka-apis.png" alt="Apache Kafka Core APIs"/>
</p>