package com.dsk.consumer.strategy;

import java.io.IOException;

public interface SaveStrategy
{
    Long save(String indexName, String key, String data) throws IOException;
}
