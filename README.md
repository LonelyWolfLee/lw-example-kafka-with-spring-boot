# Apache Kafka Tutorials with Spring Boot

## Reference

* [Apache Kafka](https://kafka.apache.org/)
* [Apache ZooKeeper](https://zookeeper.apache.org/)
* [Spring Boot Kafka](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-kafka)
* [Tutorial from Baeldung](https://www.baeldung.com/spring-kafka)
* [System Requirements for Confluent Platform](https://docs.confluent.io/current/installation/system-requirements.html)

## Prerequisite

* Java 8 or 11 (9, 10 not supported)
* Install ZooKeeper And Kafka

## Installation

### Install separately

##### 1. [required] [Apache ZooKeeper](https://hub.docker.com/r/confluentinc/cp-zookeeper)

```shell script
docker run -d \
  -p 2181:2181 \
  -p 2888:2888 \
  -p 3888:3888 \
  --name zookeeper \
  --network net-zk \
  --restart always \
  -v {HOST_ZOOKEEPER_DATA_VOLUME}:/var/lib/zookeeper/data \
  -v {HOST_ZOOKEEPER_LOG_VOLUME}:/var/lib/zookeeper/log \
  -v {HOST_ZOOKEEPER_SECRETS_VOLUME}:/etc/zookeeper/secrets \
  -e ZOOKEEPER_SERVER_ID=1 \
  -e ZOOKEEPER_SERVERS=localhost:2888:3888 \
  -e ZOOKEEPER_TICK_TIME=2000 \
  -e ZOOKEEPER_INIT_LIMIT=5 \
  -e ZOOKEEPER_CLIENT_PORT=2181 \
  confluentinc/cp-zookeeper:5.4.1
```

##### 2. [required] [Apache Kafka](https://hub.docker.com/r/confluentinc/cp-kafka)

```shell script
docker run -d \
  -p 9092:9092 \
  --name kafka \
  --network net-zk \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://{REACHABLE_HOST_NOT_LOCALHOST}:9092 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_DELETE_TOPIC_ENABLE=true \
  -e KAFKA_AUTO_CREATE_TOPIC_ENABLE=true \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ZOOKEEPER_CONNECT_TIMEOUT_MS=6000 \
  -e KAFKA_DEFAULT_REPLICATION_FACTOR=1 \
  -e KAFKA_MIN_INSYNC_REPLICAS=1 \
  -e KAFKA_NUM_PARTITION=1 \
  confluentinc/cp-kafka:5.4.1
```

같은 network bridge 로 설정이 되어있으므로, zookeeper
`REACHABLE_HOST_IP_NOT_LOCALHOST` 는 실제로 **외부에서 접근이 가능한 host ip** 를 사용 하여야 합니다. `localhost` 로 할 경우 docker 자신의 내부 localhost 로 지정이 되어 접근이 불가능 합니다.  

### All-in-One With Docker Compose

[docker-compose](https://docs.docker.com/compose/install/) 가 설치 되어있다면 테스트 용도로 위의 2개의 과정을 한번에 할 수 있습니다. 우선 `docker-compose.yml` 파일을 작성합니다.
```yaml
version: "2"
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:5.4.1
    ports:
      - 2181:2181
      - 2888:2888
      - 3888:3888
    restart: always

  kafka:
    image: wurstmeister/kafka
    ports:
      - 9092:9092
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181/local
      KAFKA_ADVERTISED_HOST_NAME: localhost
      KAFKA_ADVERTISED_PORT: 9092
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
```

그 다음 `docker-compose.yml` 파일이 있는 경로에서 아래 script 를 실행 합니다.
```shell script
docker-compose up -d
```

`-d`옵션은 docker 가 background 에서 샐행이 되도록 합니다. docker 를 한번에 종료하고 싶을 때에는 아래 script 를 실행 합니다.
```shell script
docker-compose down
```

### Additional Tools

##### 1. ZooKeeper Monitoring
* `Exhibitor` from Netflix : https://github.com/soabase/exhibitor

###### Build Exhibitor

최신 버전의 Exhibitor를 사용하고 싶으시다면 직접 빌드를 해서 사용을 하시면 됩니다. 이떄 사용 가능한 zookeeper의 버전은 3.4.14 까지입니다.(3.5.x 이상은 지원 안함) 현재 공식 WiKi 에서 제공하는 gradle build 파일에 오류가 있습니다 (shadow jar 버전이 틀림). 이를 수정한 `build.gradle` 파일을 공유합니다 (5.x 또는 6.x 버전의 gradle 을 사용하는 경우입니다).

```groovy
buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
      classpath 'com.github.jengelman.gradle.plugins:shadow:5.2.0'
    }
}

apply plugin: 'java'
apply plugin: 'com.github.johnrengelman.shadow'

group = 'exhibitor'
archivesBaseName = 'exhibitor'
version = '1.7.1'

repositories {
    jcenter()
    mavenCentral()
    maven {
        url "https://repository.jboss.org/nexus/content/groups/public/"
    }
}

dependencies {
    compile 'io.soabase.exhibitor:exhibitor-standalone:1.7.1'
}

jar {
    manifest {
        attributes (
            'Main-Class': 'com.netflix.exhibitor.application.ExhibitorMain',
            'Implementation-Version': project.version
        )
    }
}

shadowJar {
    mergeServiceFiles()
}

assemble.dependsOn shadowJar
``` 

Build 명령은 다음과 같습니다.

```shell script
gradle shadowJar
```

실행 및 실행 옵션에 관해서는 [링크](https://github.com/soabase/exhibitor/wiki/Running-Exhibitor)를 참조하세요.

##### 2. Kafka Monitoring
* `Burrow` from LinkedIn : https://github.com/linkedin/Burrow
* `CMAK` from Yahoo (Known as Kafka Manager, zookeeper 3.5 이상 필요) : https://github.com/yahoo/CMAK

## Overview
Apache Kafka® is a distributed streaming platform. 실행 하기 위해서는 zookeeper 설치가 필요합니다.

> ##### Apache 주키퍼(ZooKeeper)란?
> 
> Cluster로 구성된 서버들간의 설정/데이터 를 조율하고 동기화 관리를 해주는 분산 코디네이션 서비스 입니다. ZooKeeper를 사용하면 사용자는 서비스 코디네이션에 신경 쓰지 않고 개발에 집중 할 수 있습니다.
>
> **"ZooKeeper is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services"**
>
> Clustering 된 서비스들의 설정 및 Cluster Node를 관리해주며, 서비스 리더를 채택하고, 데이터 동기화 서비스를 제공합니다.
> ZooKeeper 자체도 Cluster 구성이 가능하며, 이를 앙상블(Ensemble)이라고 부릅니다. 
> ZooKeeper Ensemble은 홀수의 Node로 이루어지며, 과반수 이상의 Node가 장애가 생기 전 까지는 ZooKeeper 서비스를 정상으로 사용 할 수 있습니다.

<p align="center">
  <img src="https://kafka.apache.org/24/images/kafka-apis.png" alt="Apache Kafka Core APIs"/>
</p>

### Key Capabilities
* `Message Queue` 또는 `Enterprise Messaging System` 과 유사한 Record Stream 의 `Publish/Subscribe` 기능
* 내구성 있는 방식으로 `fault-tolerant`를 지키며 Record Stream 을 저장 (디스크 기반 데이터 저장 방식)
* Record Stream 이 발생 할 때 바로 처리 (Real-Time)

### Usage
* System 이나 Application 간에 데이터를 안정적으로 `가져오기(주는 것이 아님)` 위한 Real-Time Streaming data pipeline 구축
* 데이터 스트림을 변환하거나, 데이터에 반응하는 Real-Time Streaming Application 구축

### Concept

* Kafka 는 여러 Data Center 로 걸쳐 있을수 있는 하나 이상의 서버에서 실행
* Kafka cluster 는 `topic` 이라는 카테고리로 record 를 저장
* 각 record는 key, value, timestamp로 구성

### Jargon (용어)
    Kafka Server == Broker
    
##### Producer

데이터를 `topic`에 생성하는 주체입니다. Producer 는 데이터 생성을 `Load Balancing` 하기 위해 `partition`을 분리를 할 수 있습니다. 
어느 topic 과 partition 에 데이터를 생성 할 지는 Producer가 결정 합니다. 
partition 이 늘어나게 되면 데이터 처리 효율은 좋아지지만, 데이터의 순서는 각 partition 내에서만 보장되고 partition 간에는 보장이 되지 않습니다.
partition 의 갯수는 한번 늘리면 줄일 수 없기 때문에 서비스 상황에 따라 잘 선택 해야 합니다.

<p align="center">
  <img src="https://kafka.apache.org/25/images/log_anatomy.png" alt="Apache Kafka Producer"/>
</p>

생성된 데이터는 설정을 통해서 얼마나 오래 유지를 할 지를 정합니다. 
정해진 기간이 지난 데이터는 자동으로 소멸 됩니다. 
저장된 데이터 레코드의 숫자가 아무리 많아도 데이터를 읽어오는 속도는 Linear 하기 때문에 데이터를 오랫동안 저장을 해도 문제가 없습니다.

##### Consumer

데이터를 읽어오는 주체입니다. Consumer 는 데이터 구독을 `Load Balancing` 하기 위해서 `Consumer Group` 을 구성하고 한 group에 여러개의 Consumer를 배치 할 수 있습니다.
각각의 Consumer 는 하나 이상의 partition에 접근 할 수 있으며, 읽어온 위치를 기록하기 위해 Consumer 별로 `offset` 을 기록합니다.
Consumer 가 재 시작 될 때에, 읽은 위치부터 / 처음부터 / 가장 최근의 데이터 부터 읽도록 설정을 할 수 있습니다. 

<p align="center">
  <img src="https://kafka.apache.org/25/images/log_consumer.png" alt="Apache Kafka Consumer" />
</p>

하나의 Consumer Group 은 하나의 topic을 구독하며, 하나의 partition은 Consumer Group 내의 하나의 Consumer 와 연결이 됩니다.
그렇기 때문에 Consumer Group 내의 Consumer 숫자는 topic 의 partition 숫자보다 작게 가져가는 것이 유리합니다. 