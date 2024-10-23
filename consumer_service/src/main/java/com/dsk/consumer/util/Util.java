package com.dsk.consumer.util;

import com.dsk.consumer.models.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
