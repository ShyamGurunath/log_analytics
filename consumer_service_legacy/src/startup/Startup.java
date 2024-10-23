package startup;

import models.Response;
import util.RetryDriver;
import util.Util;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.DskLogger;

public class Startup
{

    static HttpClient client = HttpClient.newHttpClient();

    static DskLogger logger = DskLogger.getLogger(Startup.class.getName());

    static RetryDriver<HttpResponse<String>> retryDriver = new RetryDriver<>(5, 5);

    public static void main(String[] args) throws IOException, InterruptedException
    {
        Callable<HttpResponse<String>> callable = () ->
        {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("http://dsk_api_server:9090/"))
            .GET()
            .build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        };
        HttpResponse<String> response = retryDriver.retry(callable);
        if (response.statusCode() == 200)
        {
            logger.info("Response fetch successful from dsk_api_server");
            Response data = Util.convertStringToResponse(response.body());
            if (!data.isSuccess())
            {
                throw new RuntimeException("Response is not success due to " + data.getMessage());
            }
            List<Map<String, String>> logger_data = data.getData();

        }
    }
}
