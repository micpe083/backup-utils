package backup.gui.common;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import backup.api.BackupUtil;

public final class BackupSettings
{
    public static final String DIGEST_FILE = "digest.file";

    public static final String DIGEST_DIR = "digest.dir";
    public static final String DIGEST_OUTPUT_DIR = "digest.output.dir";

    public static final String COPY_SCRIPT_BASE_DIR_FROM = "copy.script.base.dir.from";
    public static final String COPY_SCRIPT_BASE_DIR_TO = "copy.script.base.dir.to";
    public static final String COPY_SCRIPT_OUTPUT_DIR = "copy.script.base.dir.output";

    private static BackupSettings instance;

    private final Properties properties = new Properties();

    private BackupSettings()
    {
        try
        {
            load();
        }
        catch (final Exception e)
        {
            // use empty settings if non found
        }
    }

    private static File getFile()
    {
        final File file = new File(BackupSettings.class.getClassLoader().getResource("backup-utils.properties").getFile());

        return file;
    }

    public static synchronized BackupSettings getInstance()
    {
        if (instance == null)
        {
            instance = new BackupSettings();
        }

        return instance;
    }

    public void setValue(final String key,
                         final String value)
    {
        properties.setProperty(key, value);
    }

    public String getValue(final String key)
    {
        return properties.getProperty(key);
    }

    public void load() throws IOException
    {
        try (final Reader reader = BackupUtil.createReader(getFile()))
        {
            properties.load(reader);
        }
    }

    public void save() throws IOException
    {
        try (final Writer writer = BackupUtil.createWriter(getFile()))
        {
            properties.store(writer, "comments");
        }
    }
}
