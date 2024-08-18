# KafkaOrderOrchestrationSaga

Demonstration of SAGA Orchestration Design Pattern using Spring Boot and Kafka

# Docker Compose YAML Configuration

Below is a Docker Compose YAML configuration for a single Kafka broker running in KRaft mode:

services:
kafka-1:
image: 'bitnami/kafka:3.3.2'
container_name: kafka-1
environment:
KAFKA_PROCESS_ROLES: broker,controller
KAFKA_NODE_ID: 1
KAFKA_LISTENERS: PLAINTEXT://kafka-1:9092,CONTROLLER://kafka-1:9093
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka-1:9093
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT
KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
KAFKA_LOG_DIRS: /var/lib/kafka/data
KAFKA_AUTO_CREATE_TOPICS_ENABLE: 'true'
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
ports:

- "9092:9092"
  volumes:
- ./data:/var/lib/kafka/data
