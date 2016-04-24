package backup.gui.common;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import backup.api.BackupUtil;

public final class BackupSettings
{
    public static final String DIGEST_DIR = "digest.dir";

    public static final String DIGEST_OUTPUT_DIR = "digest.output.dir";
    public static final String STAGING_DIR = "staging.dir";

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

    public static synchronized BackupSettings getInstance()
    {
        if (instance == null)
        {
            instance = new BackupSettings();
        }

        return instance;
    }

    public String getOutputDir() throws IOException
    {
        return getDir(DIGEST_OUTPUT_DIR, "digest-output");
    }

    public String getStagingDir() throws IOException
    {
        return getDir(STAGING_DIR, "staging");
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
        try (final Reader reader = BackupUtil.createReader(getConfigFile()))
        {
            properties.load(reader);
        }
    }

    public void save() throws IOException
    {
        try (final Writer writer = BackupUtil.createWriter(getConfigFile()))
        {
            properties.store(writer, "comments");
        }
    }

    private String getDir(final String property,
                          final String defaultDir) throws IOException
    {
        final String dirx = getValue(property);

        final File dir;

        if (dirx == null)
        {
            final File baseDir = getBaseDir();
            dir = new File(baseDir, defaultDir);
        }
        else
        {
            dir = new File(dirx).getCanonicalFile();
        }

        checkDir(dir);

        return dir.getCanonicalPath();
    }

    private static File getConfigFile() throws IOException
    {
        final File configFile = new File(getBaseDir(), "backup-utils.properties");

        if (!configFile.exists())
        {
            configFile.createNewFile();
        }

        return configFile;
    }

    private static File getBaseDir() throws IOException
    {
        final String baseDirStr = System.getProperty("base.dir", ".");

        final File baseDir = new File(baseDirStr).getCanonicalFile();

        checkDir(baseDir);

        return baseDir;
    }

    private static void checkDir(final File dir)
    {
        if (!dir.isDirectory())
        {
            dir.mkdir();
        }
    }
}
