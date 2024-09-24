package utils;

import models.Response;

public class Mapper
{
    public static class ResponseMapper
    {
        public static Response getFailedResponse(String message)
        {
            return new Response.Builder()
                        .setSuccess(false)
                        .setMessage(message)
                        .build();
        }

        public static Response getSuccessResponse(String message, Object data)
        {
            return new Response.Builder()
                        .setSuccess(true)
                        .setMessage(message)
                        .setData(data)
                        .build();
        }
    }
}
