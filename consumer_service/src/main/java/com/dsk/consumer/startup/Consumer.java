package com.dsk.consumer.startup;

import com.dsk.consumer.strategy.SaveStrategy;
import com.dsk.consumer.util.Config;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Consumer implements Runnable
{

    private final Logger LOGGER;

    private final Properties _consumerProperties;

    private final Integer _threadNumber;

    private final String topic;

    private final List<Integer> partitions;

    private final SaveStrategy _strategy;

    public Consumer(Integer threadNumber, String topic, List<Integer> partitions, SaveStrategy strategy)
    {
        this._threadNumber = threadNumber;
        this.LOGGER = Logger.getLogger(Consumer.class.getName()+"-"+threadNumber);
        this.topic = topic;
        this.partitions = partitions;
        this._consumerProperties = Config.getConsumerProps("kafka1:9092;kafka2:9093;kafka3:9094");
        this._strategy = strategy;
        LOGGER.info("I am a Kafka startup.Consumer with Thread - "+this._threadNumber+" with save strategy "+strategy.getClass().getName());
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run()
    {
        LOGGER.log(Level.INFO, "startup.Consumer - "+this._threadNumber+" - Started Polling Data ");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(this._consumerProperties))
        {
            List<TopicPartition> topicPartitions = this.partitions
                    .stream()
                    .map((partition) -> new TopicPartition(topic, partition))
                    .collect(Collectors.toList());
            consumer.assign(topicPartitions);

            while (true)
            {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(5000));
                for (ConsumerRecord<String, String> record : records)
                {
                   LOGGER.info("Consumer - " + this._threadNumber + " Key: " + record.key() + ", Value: " + record.value());
                   LOGGER.info("startup.Consumer - " + this._threadNumber + " Partition: " + record.partition() + ", Offset:" + record.offset());
                   try
                   {
                       this._strategy.save(record.topic(), record.key(), record.value());
                   }
                   catch (Exception e)
                   {
                       LOGGER.log(Level.SEVERE, e.getMessage(), e);
                       break;
                   }
                }
                consumer.commitSync();
            }
        }
        catch (WakeupException e)
        {
            LOGGER.info("Wakeup exception" + e.getMessage());
            // we ignore this as this is an expected exception when closing a consumer
        }
        catch (Exception e)
        {
            LOGGER.info("Unexpected Exception" + e.getMessage());
        }
        finally
        {
            // this will also commit the offsets if need be.
            LOGGER.info("The consumer is now gracefully closed.");
        }
    }
}
