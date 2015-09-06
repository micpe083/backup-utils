package backup.api;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class StopWatch
{
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private volatile Date startDate;
    private volatile Date endDate;

    private volatile LocalTime startTime;
    private volatile LocalTime endTime;

    public StopWatch()
    {
    }

    public StopWatch start()
    {
        startDate = new Date();
        startTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        return this;
    }

    public StopWatch stop()
    {
        endDate = new Date();
        endTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        return this;
    }

    public String getStartDate()
    {
        return format(startDate);
    }

    public boolean isStopped()
    {
        return endDate != null;
    }

    public String getEndDate()
    {
        return format(endDate);
    }

    private String format(final Date date)
    {
        final String ret = date == null ? "Not set" : dateFormat.format(date);

        return ret;
    }

    public String getDuration()
    {
        final StringBuilder buf = new StringBuilder();

        final LocalTime startTime = this.startTime;

        if (startTime == null)
        {
            buf.append("Not started");
        }
        else
        {
            final LocalTime endTime = this.endTime == null ? LocalTime.now() : this.endTime;

            final Duration duration = Duration.between(startTime, endTime);

            addDuration(buf, duration.toMillis());
        }

        return buf.toString();
    }

    public static String getDuration(final long duration)
    {
        final StringBuilder buf = new StringBuilder();

        addDuration(buf, duration);

        return buf.toString();
    }

    private static void addDuration(final StringBuilder buf,
                                    final long duration)
    {
        final long hours = TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS);
        final long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS) - 60 * TimeUnit.HOURS.convert(duration, TimeUnit.MILLISECONDS);
        final long seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) - 60 * TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);

        buf.append(String.format("%02d", hours));
        buf.append(":");
        buf.append(String.format("%02d", minutes));
        buf.append(":");
        buf.append(String.format("%02d", seconds));
    }

    public String getDescription()
    {
        final StringBuilder buf = new StringBuilder();

        final LocalTime startTime = this.startTime;

        if (startTime == null)
        {
            buf.append("Not started");
        }
        else
        {
            buf.append("Duration: ").append(getDuration());
            buf.append(" ");

            buf.append("Start: ").append(startTime);
            buf.append(" ");

            final LocalTime endTime = this.endTime;
            if (endTime != null)
            {
                buf.append("End: ").append(endTime);
            }
        }

        return buf.toString();
    }
}
