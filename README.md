# Apache Kafka Tutorials with Spring Boot

## Reference

* [Apache Kafka](https://kafka.apache.org/)
* [Apache ZooKeeper](https://zookeeper.apache.org/)
* [Spring Boot Kafka](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-kafka)
* [System Requirements for Confluent Platform](https://docs.confluent.io/current/installation/system-requirements.html)

## Prerequisite

* Java 8 or 11 (9, 10 not supported)
* Install ZooKeeper And Kafka

## Installation

### [All in one package using Confluent](https://docs.confluent.io/current/quickstart/index.html)

#### 1. Clone the Confluent Platform Docker Images GitHub Repository and check out the 5.4.1-post branch

```shell script
git clone https://github.com/confluentinc/examples
cd examples
git checkout 5.4.1-post
```

#### 2. Navigate to cp-all-in-one examples directory

```shell script
cd cp-all-in-one/
```

#### 3. Start Confluent Platform using docker compose

```shell script
docker-compose up -d --build
```

### [Install separately](https://docs.confluent.io/3.1.1/cp-docker-images/docs/quickstart.html)

##### 1. [optional] Docker Machine

만약 Docker 환경이 설치 되어있는 가상 환경을 구축하고 싶다면 docker machine 을 설치해서 아래의 과정을 진행하여 새로운 VirtualBox Machine 을 만들고 해당 Machine 에 접속합니다. VirtualBox 와 Docker Machine 이 설치가 되어있어야 합니다. 

###### - Create and configure the Docker Machine
```shell script
docker-machine create --driver virtualbox --virtualbox-memory 6000 confluent
```

###### - Configure your terminal window to attach it to your new Docker Machine
```shell script
eval $(docker-machine env confluent)
```

##### 2. [required] ZooKeeper

```shell script
docker run -d \
    --net=host \
    --name=zookeeper \
    -e ZOOKEEPER_CLIENT_PORT=2181 \
    confluentinc/cp-zookeeper:3.1.1
```

##### 3. [required] Kafka

```shell script
docker run -d \
    --net=host \
    --name=kafka \
    -e KAFKA_ZOOKEEPER_CONNECT=localhost:2181 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:29092 \
    confluentinc/cp-kafka:3.1.1
```

### Additional Tools

##### ZooKeeper Monitoring
* `Exhibitor` from Netflix : https://github.com/soabase/exhibitor

##### Kafka Monitoring
* `Burrow` from LinkedIn : https://github.com/linkedin/Burrow
* `CMAK` from Yahoo : https://github.com/yahoo/CMAK

## Overview
Apache Kafka® is a distributed streaming platform

### Key Capabilities
* `Message Queue` 또는 `Enterprise Messaging System` 과 유사한 Record Stream 의 `Publish/Subscribe` 기능
* 내구성 있는 방식으로 `fault-tolerant`를 지키며 Record Stream 을 저장 (디스크 기반 데이터 저장 방식)
* Record Stream 이 발생 할 때 바로 처리 (Real-Time)

<p align="center">
  <img src="https://kafka.apache.org/24/images/kafka-apis.png" alt="Apache Kafka Core APIs"/>
</p>