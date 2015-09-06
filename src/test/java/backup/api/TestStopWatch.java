package backup.api;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestStopWatch
{
    @Test
    public void testGetDuration() throws Exception
    {
        assertEquals("00:00:00", StopWatch.getDuration(0));
        assertEquals("00:00:01", StopWatch.getDuration(TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS)));
        assertEquals("00:01:00", StopWatch.getDuration(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)));
        assertEquals("01:00:00", StopWatch.getDuration(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)));

        assertEquals("01:01:01", StopWatch.getDuration(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS) +
                                                       TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES) +
                                                       TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS)));
    }
}
