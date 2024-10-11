package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Response;

public class Util
{
    static ObjectMapper mapper = new ObjectMapper();

    public static boolean noNull(Object o)
    {
        return o == null || o.toString().isEmpty();
    }

    public static Response convertStringToResponse(String response) throws JsonProcessingException
    {
       return mapper.readValue(response, Response.class);
    }
}
