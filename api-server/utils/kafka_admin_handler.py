import logging

from kafka import KafkaAdminClient
from kafka.admin import NewTopic


class KafkaAdminHandler:
    def __init__(self):
        self.kafka_connection = KafkaAdminClient(
            bootstrap_servers="kafka1:9092,kafka2:9093,kafka3:9094",
            client_id='dsk_api_server'
        )
        logging.info("Kafka connection provider initialized")

    def get_kafka_topics(self):
        return self.kafka_connection.list_topics()

    def get_kafka_topic(self, topic_name):
        return self.kafka_connection.describe_topics(topics=[topic_name])

    def create_kafka_topic(self, topic_name):
        return self.kafka_connection.create_topics([NewTopic(name=topic_name, num_partitions=1, replication_factor=1)])