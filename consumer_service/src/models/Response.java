package models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class Response
{
    boolean isSuccess;
    String message;
    List<Map<String, String>> data;

    private Response() {}

    @JsonProperty
    public List<Map<String, String>> getData() {
        return data;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public boolean isSuccess() {
        return isSuccess;
    }
}
