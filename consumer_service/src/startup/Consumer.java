package startup;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import util.Config;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.DskLogger;
import java.util.stream.Collectors;

public class Consumer implements Runnable
{

    private final DskLogger LOGGER;

    private final Properties _consumerProperties;

    private final Integer _threadNumber;

    private final String topic;

    private final List<Integer> partitions;

    public Consumer(Integer threadNumber, String topic, List<Integer> partitions)
    {
        this._threadNumber = threadNumber;
        this.LOGGER = DskLogger.getLogger(Consumer.class.getName()+"-"+threadNumber);
        this.topic = topic;
        this.partitions = partitions;
        this._consumerProperties = Config.getConsumerProps(System.getProperty("dsk.bootstrap.servers"));
        System.out.println("I am a Kafka startup.Consumer with Thread - "+this._threadNumber);
    }

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
                    //TODO: Implement Es strategy
                    System.out.println("startup.Consumer - " + this._threadNumber + " Key: " + record.key() + ", Value: " + record.value().getPageVisits());
                    System.out.println("startup.Consumer - " + this._threadNumber + " Partition: " + record.partition() + ", Offset:" + record.offset());
                }
                consumer.commitSync();
            }
        }
        catch (WakeupException e)
        {
            System.out.println("Wakeup exception" + e.getMessage());
            // we ignore this as this is an expected exception when closing a consumer
        }
        catch (Exception e)
        {
            System.out.println("Unexpected Exception" + e.getMessage());
        }
        finally
        {
            // this will also commit the offsets if need be.
            System.out.println("The consumer is now gracefully closed.");
        }
    }
}
