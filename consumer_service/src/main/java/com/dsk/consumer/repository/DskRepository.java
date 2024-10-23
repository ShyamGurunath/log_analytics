package com.dsk.consumer.repository;

import com.dsk.consumer.models.Response;
import com.dsk.consumer.util.Config;
import com.dsk.consumer.util.RetryDriver;
import com.dsk.consumer.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class DskRepository
{

    private static final HttpClient client = HttpClient.newHttpClient();

    private static final Logger logger = Logger.getLogger(DskRepository.class.getName());

    private static final RetryDriver<HttpResponse<String>> retryDriver = new RetryDriver<>(5, 5);

    public static List<String> fetchApplications() throws InterruptedException, JsonProcessingException
    {
        Callable<HttpResponse<String>> callable = () ->
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Config.getBaseUrl() + "application/list"))
                    .GET()
                    .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        };
        HttpResponse<String> application_response = retryDriver.retry(callable);

        if (application_response.statusCode() != 200)
        {
            throw new RuntimeException("Failed on fetching application data due to "+application_response.statusCode()+". Reason : "+application_response.body());
        }
        if (application_response.body() == null)
        {
            return List.of();
        }
        logger.info("Response fetch successful from dsk_api_server");
        Response data = Util.convertStringToResponse(application_response.body());
        if (!data.getSuccess())
        {
             if (data.getData() == null || ((List<String>) data.getData()).isEmpty())
             {
                 return List.of();
             }
            throw new RuntimeException("Response is not success due to " + data.getMessage());
        }
        return (List<String>) data.getData();
    }

    public static List<String> getKafkaTopics(List<String> applications) throws InterruptedException, JsonProcessingException
    {
        List<String> topics = new ArrayList<>();
        for (String application : applications)
        {
            Callable<HttpResponse<String>> callable = () ->
            {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(Config.getBaseUrl() + "logger/list?app_name=" + application))
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();
                return client.send(request, HttpResponse.BodyHandlers.ofString());
            };
            HttpResponse<String> logger_response = retryDriver.retry(callable);
            if (logger_response.statusCode() != 200)
            {
                throw new RuntimeException("Failed on fetching logger data due to " + logger_response.statusCode() + ". Reason : " + logger_response.body());
            }
            if (logger_response.body() == null)
            {
                logger.warning("No Response fetched for " + application);
                continue;
            }
            logger.info("Response fetch successful from dsk_api_server");
            Response data = Util.convertStringToResponse(logger_response.body());
            if (!data.getSuccess())
            {
                if (data.getData() == null || ((List<String>) data.getData()).isEmpty())
                {
                    continue;
                }
                throw new RuntimeException("Response is not success due to " + data.getMessage());
            }
            List<Map<String, String>> loggers = (List<Map<String, String>>) data.getData();
            loggers.forEach(l -> topics.add(l.get("kafka_topic")));
        }
        return topics;
    }

}
