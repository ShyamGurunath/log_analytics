package core;

import models.Response;
import utils.Mapper;
import utils.Singletons;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DskLogger
{

    public Response registerApplication(String appName) throws IOException, InterruptedException
     {
        //NOTE: Hit the api server
        // Register the app
        HttpRequest httpRequest = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody()).uri(URI.create("http://127.0.0.1:8000/dsk/logger/registerApplication?appname="+appName)).build();
        HttpResponse<String> response = Singletons.HttpSingleTon.getClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
        {
            return Mapper.ResponseMapper.getFailedResponse("Failed to add appName :" + appName +" due to issue "+ response.statusCode());
        }
        return Mapper.ResponseMapper.getSuccessResponse("Successfully added appName : "+appName, response.body());
     }

//     public Map<String, String> getApplication(String appName) throws IOException, InterruptedException
//     {
//        //NOTE: Hit the api server
//        // get the app
//        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://api.server.dsk.com/getApp/"+appName)).build();
//        httpClient.send(httpRequest, null);
//        return new HashMap<>() {{
//            put("appName", appName);
//            put("appId", "1234565");
//        }};
//     }
//
//
//    public List<Map<String, String>> listApplication() throws IOException, InterruptedException
//    {
//        //NOTE: Hit the api server
//        // get all apps
//        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://api.server.dsk.com/getAllApps")).build();
//        httpClient.send(httpRequest, null);
//        return new ArrayList<>() {{
//            add(new HashMap<>() {{
//                put("appId", "1234565");
//                put("appName", "sample");
//            }});
//        }};
//    }
//
//
//    public boolean createLogger(String appName) throws IOException, InterruptedException
//    {
//        //NOTE: Hit the api server
//        // create a logger
//        // getApplication
//        Map<String, String> application = getApplication(appName);
//        HashMap<String, Object> bodyParams = new HashMap<>()
//        {{
//            put("appId", application.getOrDefault("appId", "null"));
//            put("loggerName", "Shyam");
//        }};
//        HttpRequest httpRequest = HttpRequest.newBuilder().POST(null).uri(URI.create("http://api.server.dsk.com/createlogger")).build();
//        httpClient.send(httpRequest, null);
//        return true;
//    }
//
//
//     public  List<Map<String, String>> listLoggers(String appName) throws IOException, InterruptedException
//    {
//        //NOTE: Hit the api server
//        // get all apps
//        HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(URI.create("http://api.server.dsk.com/listLoggers/"+appName)).build();
//        httpClient.send(httpRequest, null);
//        return new ArrayList<>() {{
//            add(new HashMap<>() {{
//                put("appId", "1234565");
//                put("appName", "sample");
//            }});
//        }};
//    }

}
