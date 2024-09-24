import core.DskLogger;
import models.Response;

import java.io.IOException;

public class Main {
    private static DskLogger logger = new DskLogger();
    public static void main(String[] args) throws IOException, InterruptedException
    {
        Response response = logger.registerApplication("Nano");
        System.out.printf("Response isSuccess: %s\n", response.isSuccess());
        System.out.printf("Response Message: %s\n", response.getMessage());
        System.out.printf("Response data: %s\n", response.getData());
    }
}