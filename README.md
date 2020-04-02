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

##### 1. [required] [Apache ZooKeeper](https://github.com/wurstmeister/zookeeper-docker)

```shell script
docker run -d \
  -p 2181:2181 \
  --name zookeeper \
  --restart always \
  wurstmeister/zookeeper
```

##### 2. [required] [Apache Kafka](https://github.com/wurstmeister/kafka-docker)

연결 할 ZooKeeper 를 설정하기 위해서 `ZOOKEEPER_DOCKER_IP`를 찾아서실제 address 로 설정 해주어야 합니.
```shell script
docker inspect zookeeper
```

이때 출력되는 결과에서 `NetworkSettings > Networks > bridge > IPAddress` 를 찾으면 나오는 값 해당 docker 의 ip address 입니다. 이 값을 아래의 script 의 `{ZOOKEEPER_DOCKER_IP}` 에 채워준 후 아래의 docker 를 실행하면 됩니다. 

```shell script
docker run -d \
  -p 9092:9092 \
  --name kafka \
  -e KAFKA_ZOOKEEPER_CONNECT={ZOOKEEPER_DOCKER_IP}:2181/local \
  -e KAFKA_LISTENERS=PLAINTEXT://localhost:9092 \
  wurstmeister/kafka
```

### All-in-One With Docker Compose

[docker-compose](https://docs.docker.com/compose/install/) 가 설치 되어있다면 테스트 용도로 위의 2개의 과정을 한번에 할 수 있습니다. 우선 `docker-compose.yml` 파일을 작성합니다.
```yaml
version: "2"
services:
  zookeeper:
    image: wurstmeister/zookeeper
    ports:
      - 2181:2181
    restart: always

  kafka:
    image: wurstmeister/kafka
    ports:
      - 9092:9092
    environment:
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181/local
      KAFKA_LISTENERS: PLAINTEXT://localhost:9092
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

현재 공식 WiKi 에서 제공하는 gradle build 파일에 오류가 있습니다 (shadow jar 버전이 틀림). 이를 수정한 `build.gradle` 파일을 공유합니다 (5.x 또는 6.x 버전의 gradle 을 사용하는 경우입니다).

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
version = '1.6.0'

repositories {
    jcenter()
    mavenCentral()
    maven {
        url "https://repository.jboss.org/nexus/content/groups/public/"
    }
}

dependencies {
    compile 'io.soabase.exhibitor:exhibitor-standalone:1.6.0'
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