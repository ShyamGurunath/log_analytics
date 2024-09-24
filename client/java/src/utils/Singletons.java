package utils;

import java.net.http.HttpClient;

public class Singletons
{
    public static class HttpSingleTon
    {
       private static HttpClient client = null;

        public static HttpClient getClient()
        {
            if (client == null)
            {
                client = HttpClient.newHttpClient();
                System.out.printf("Client Created first time \n");
            }
            else
            {
                System.out.printf("Client Already Created \n");
            }
            return client;
        }
    }
}
