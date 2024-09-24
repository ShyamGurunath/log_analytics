package models;

public class Response
{
    boolean isSuccess;
    String message;
    Object data;

    private Response() {}


    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public static class Builder
    {
        private final Response response;

        public Builder()
        {
            this.response = new Response();
        }

        public Builder setSuccess(boolean success)
        {
            this.response.isSuccess = success;
            return this;
        }

        public Builder setMessage(String message)
        {
            this.response.message = message;
            return this;
        }

        public Builder setData(Object data)
        {
            this.response.data = data;
            return this;
        }

        public Response build()
        {
            return this.response;
        }
    }
}
