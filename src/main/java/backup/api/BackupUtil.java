package backup.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Strings;

public final class BackupUtil
{
    private static final String CHARSET_UTF8 = "UTF-8";

    private BackupUtil() {}

    public static BufferedWriter createWriter(final File file) throws UnsupportedEncodingException, FileNotFoundException
    {
        final FileOutputStream fos = new FileOutputStream(file);
        final OutputStreamWriter osw = new OutputStreamWriter(fos, BackupUtil.CHARSET_UTF8);
        final BufferedWriter writer = new BufferedWriter(osw);
        return writer;
    }

    public static BufferedReader createReader(final File file) throws UnsupportedEncodingException, FileNotFoundException
    {
        final FileInputStream fis = new FileInputStream(file);
        final InputStreamReader isr = new InputStreamReader(fis, BackupUtil.CHARSET_UTF8);
        final BufferedReader reader = new BufferedReader(isr);
        return reader;
    }

    public static String findCommonPath(final String path1x,
                                        final String path2x)
    {
        if (Strings.isNullOrEmpty(path1x) &&
            Strings.isNullOrEmpty(path2x))
        {
            return null;
        }

        final String path1 = Strings.isNullOrEmpty(path1x) ? path2x : path1x;
        final String path2 = Strings.isNullOrEmpty(path2x) ? path1x : path2x;

        int idx = -1;
        for (int i = 0; i < Math.min(path1.length(), path2.length()); i++)
        {
            final char c1 = path1.charAt(i);
            final char c2 = path2.charAt(i);

            if (c1 == '\\' || c1 == '/')
            {
                idx = i;
            }

            if (c1 != c2)
            {
                break;
            }
        }

        return path1.substring(0, idx + 1);
    }

    public static String getFilenameTimestamp()
    {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static String formatNumber(final long val)
    {
        return NumberFormat.getNumberInstance(Locale.US).format(val);
    }

    public static String humanReadableByteCount(final long bytes)
    {
        return humanReadableByteCount(bytes, true);
    }

    public static String getSizeAll(final long bytes)
    {
        return humanReadableByteCount(bytes, true) + " " +
               humanReadableByteCount(bytes, false) + " " +
               formatNumber(bytes) + " B";
    }

    public static String getSpeed(final long bytes,
                                  final long timeElapsed)
    {
        final String ret;

        if (timeElapsed > 0)
        {
            final long bytesPerSec = Math.round(bytes / (double) TimeUnit.SECONDS.convert(timeElapsed, TimeUnit.MILLISECONDS));

            ret = BackupUtil.humanReadableByteCount(bytesPerSec) + "/s";
        }
        else
        {
            ret = "?";
        }

        return ret;
    }

    private static String humanReadableByteCount(final long bytes,
                                                 final boolean si)
    {
        final int unit = si ? 1000 : 1024;
        if (bytes < unit)
        {
            return bytes + " B";
        }

        final int exp = (int) (Math.log(bytes) / Math.log(unit));

        final String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
