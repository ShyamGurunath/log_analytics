package com.dsk.consumer.startup;

import java.util.logging.Logger;

public class Startup
{

    private static final Logger logger = Logger.getLogger(Startup.class.getName());

    public static void main(String[] args)
    {
        logger.info("Application Startup started");

        ServerStartup.start();

        logger.info("Application Startup ended");
    }
}
