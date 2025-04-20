# Kafka Configuration Scenarios

This project contains examples to reproduce the scenarios described in the [official Apache Kafka documentation](https://kafka.apache.org/documentation/) for various producer configuration settings.

## 🔍 What’s Covered

- *delivery.timeout.ms*, *linger.ms*, *batch.size*, and more  

## 🛠️ Requirements

- Kafka 3.9  
- Docker & Docker Compose  
- Wireshark (optional, for packet-level inspection)

## 🚀 Getting Started

Each example is explained with step-by-step instructions and can be run independently.  
Make sure to start the Kafka cluster before running any examples (1 or 2 brokers depending on the scenario).

### ▶️ Run Kafka with 1 Broker

To run the Kafka cluster with 1 broker, use the following command:

```bash
docker compose -f 1-broker-cluster.yaml up
```

To run the Kafka cluster with 2 brokers, use the following command:

```bash
docker compose -f 2-broker-cluster.yaml up
```

## 📌 Notes

- Some examples explicitly create topics; make sure to delete them from Kafka UI or restart containers before rerunning.
- Default configuration values are based on Kafka 3.9.

## 📝 Related Articles

For a full walkthrough and deeper explanations, check out the accompanying Medium articles:

👉 Producer: [[Message Loss in Kafka – Inevitable, But Can Be Made Rare](https://medium.com/your-article-url)](https://medium.com/@alihmzyv/apache-kafka-producer-experiments-network-analysis-db972917bc2c)
