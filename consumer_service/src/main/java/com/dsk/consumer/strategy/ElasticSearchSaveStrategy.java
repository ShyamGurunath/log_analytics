package com.dsk.consumer.strategy;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Map;

public class ElasticSearchSaveStrategy implements SaveStrategy
{

    private final ElasticsearchClient esClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ElasticSearchSaveStrategy(String url)
    {
        RestClient restClient = RestClient
            .builder(HttpHost.create(url))
            .build();
        ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());
        this.esClient = new ElasticsearchClient(transport);
    }

    @Override
    public Long save(String indexName, String key, String data) throws IOException
    {
        Map<String, Object> jsonData = objectMapper.readValue(data, Map.class);
        IndexResponse response = esClient.index(i -> i
            .index(indexName)
            .id(key)
                .document(jsonData));
        return response.version();
    }
}
