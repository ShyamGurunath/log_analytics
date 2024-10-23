package strategy;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.internal.ElasticsearchClient;

public class ElasticSearchSaveStrategy implements SaveStrategy
{

    public ElasticSearchSaveStrategy(String url)
    {
        RestClient restClient = RestClient
            .builder(HttpHost.create(url))
            .build();

        // Create the transport with a Jackson mapper
         transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);
    }


    @Override
    public void save(String )
    {

    }
}
