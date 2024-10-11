package util;

import java.util.logging.Level;
import java.util.logging.DskLogger;
import java.util.concurrent.Callable;

public class RetryDriver<T>
{
    static DskLogger logger = DskLogger.getLogger(RetryDriver.class.getName());

    int max_attempts;
    int retry_delay;
    int retry_count;

    public RetryDriver(int max_attempts, int retry_delay)
    {
        this.max_attempts = max_attempts;
        this.retry_count = retry_delay;
    }

    public T retry(Callable<T> callable) throws InterruptedException
    {
        T result = null;
        for (int i = 0; i < this.max_attempts; i++)
        {
            try
            {
               result = callable.call();
               break;
            }
            catch (Exception e)
            {
                this.retry_count++;
                if (this.retry_count >= this.max_attempts)
                {
                    throw new RuntimeException("Max retries reached");
                }
                logger.log(Level.SEVERE, "Attempt " + this.retry_count + ". Retrying after " + this.retry_delay + " seconds");
                Thread.sleep(this.retry_delay * 1000L);
            }
        }
        if (result == null)
        {
            throw new RuntimeException("Result is null");
        }
        return result;
    }
}
