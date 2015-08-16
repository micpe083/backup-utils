package backup.gui.common;

import java.util.Properties;

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
            properties.load(this.getClass().getClassLoader().getResourceAsStream("backup-utils.properties"));
        }
        catch (final Exception e)
        {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static BackupSettings getInstance()
    {
        if (instance == null)
        {
            instance = new BackupSettings();
        }

        return instance;
    }

    public String getString(final String key)
    {
        return properties.getProperty(key);
    }
}
