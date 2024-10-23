package com.dsk.consumer.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Response
{
    boolean success;
    String message;
    Object data;

    private Response() {}

    @JsonProperty
    public Object getData() {
        return data;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public boolean getSuccess() {
        return success;
    }
}
