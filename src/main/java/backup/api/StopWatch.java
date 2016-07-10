package backup.api;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class StopWatch
{
    private volatile LocalDateTime startTime;
    private volatile LocalDateTime endTime;

    public StopWatch()
    {
    }

    public StopWatch start()
    {
        startTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return this;
    }

    public StopWatch stop()
    {
        endTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return this;
    }

    public String getStartDate()
    {
        return format(startTime);
    }

    public boolean isStopped()
    {
        return endTime != null;
    }

    public String getEndDate()
    {
        return format(endTime);
    }

    public long getDurationMillis()
    {
        final Duration ret = getDurationX();

        return ret == null ? 0 : ret.toMillis();
    }

    private Duration getDurationX()
    {
        final LocalDateTime startDate = this.startTime;
        final LocalDateTime endDate = this.endTime;

        final Duration ret;

        if (startDate == null)
        {
            ret = null;
        }
        else
        {
            final LocalDateTime endTime = endDate == null ? LocalDateTime.now() : endDate;

            final Duration duration = Duration.between(startDate, endTime);

            ret = duration;
        }

        return ret;
    }

    private String format(final LocalDateTime date)
    {
        final String ret;

        if (date == null)
        {
            ret = "Not set";
        }
        else
        {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            ret = date.format(formatter);
        }

        return ret;
    }

    private String formatTime(final LocalDateTime date)
    {
        final String ret;

        if (date == null)
        {
            ret = "Not set";
        }
        else
        {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            ret = date.format(formatter);
        }

        return ret;
    }

    public String getDuration()
    {
        final LocalDateTime startTime = this.startTime;

        final String ret;

        if (startTime == null)
        {
            ret = "Not started";
        }
        else
        {
            final long durationMillis = getDurationMillis();

            ret = getDuration(durationMillis);
        }

        return ret;
    }

    public static String getDuration(final long duration)
    {
        return DurationFormatUtils.formatDuration(duration, "HH:mm:ss");
    }

    public String getDescription()
    {
        final StringBuilder buf = new StringBuilder();

        final LocalDateTime startTime = this.startTime;

        if (startTime == null)
        {
            buf.append("Not started");
        }
        else
        {
            buf.append("Duration: ").append(getDuration());
            buf.append(" ");

            buf.append("Start: ").append(formatTime(startTime));
            buf.append(" ");

            final LocalDateTime endTime = this.endTime;
            if (endTime != null)
            {
                buf.append("End: ").append(formatTime(endTime));
            }
        }

        return buf.toString();
    }
}
