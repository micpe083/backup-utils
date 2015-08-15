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

import backup.gui.common.BackupSettings;
import backup.gui.common.FileChooserPanel;

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

    public static void setFile(final FileChooserPanel fileChooserDigestFile)
    {
        fileChooserDigestFile.setSelection(BackupSettings.getInstance().getString(BackupSettings.DIGEST_FILE));
    }
}
