package com.dsk.consumer.startup;

import com.dsk.consumer.repository.DskRepository;
import com.dsk.consumer.strategy.ElasticSearchSaveStrategy;
import com.dsk.consumer.strategy.SaveStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ServerStartup
{
    private static List<String> cached_topics = new ArrayList<>();

    private static Logger logger = Logger.getLogger(ServerStartup.class.getName());

    private static final SaveStrategy saveStrategy = new ElasticSearchSaveStrategy("http://elasticsearch:9200");

    private static final List<Thread> threads = new ArrayList<>();

    static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    public static void start()
    {
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    List<String> applications = DskRepository.fetchApplications();

                    if (!applications.isEmpty())
                    {
                        List<String> topics = DskRepository.getKafkaTopics(applications);

                        if (!topics.isEmpty())
                        {

                            if (cached_topics.isEmpty()) {
                                logger.info("Fetched New topics, so starting the consumer threads");
                                cached_topics.addAll(topics);
                                startConsumerThreads(topics);
                            }

                            if (cached_topics.size() != topics.size()) {
                                logger.info("Fetched New topics, so removing existing threads & adding it all new");
                                for (Thread thread : threads) {
                                    thread.interrupt();
                                }
                                cached_topics = topics;
                                startConsumerThreads(topics);
                            }
                        }
                    }

                }
                catch (Exception e)
                {
                    logger.severe("Exception occured while caching : "+e.getMessage());
                    System.exit(1);
                }
            }
        };

       executor.scheduleWithFixedDelay(runnable, 0, 30, TimeUnit.SECONDS);

    }

    private static void startConsumerThreads(List<String> topics)
    {
        logger.info("Starting consumer threads");
        for (int i = 0; i < cached_topics.size(); i++)
        {
            Consumer consumer = new Consumer(i, topics.get(i), List.of(0), saveStrategy);
            Thread thread = new Thread(consumer);
            threads.add(thread);
            logger.info("Created consumer thread for Consumer - "+i+" with topic "+topics.get(i));
            thread.start();
        }
        logger.info("Consumer threads started");
    }
}
