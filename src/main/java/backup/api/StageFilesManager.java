package backup.api;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backup.gui.common.BackupSettings;

public final class StageFilesManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);

    private StageFilesManager()
    {
    }

    public static FileManager stageMissing(final FileManager source,
                                           final FileManager target) throws Exception
    {
        final FileManager missing = source.getMissing(target);
        final FileManager missingUnique = missing.getUniquePaths();

        return stage(missingUnique);
    }

    public static FileManager stage(final FileManager fileManager) throws Exception
    {
        LOGGER.info("staging file summary: " + fileManager.getFileSum());

        final Path baseDir = fileManager.getBaseDir();

        LOGGER.info("staging files from: " + baseDir);

        final Path stagingDir = new File(BackupSettings.getInstance().getStagingDir(),
                                         BackupUtil.getFilenameTimestamp()).toPath();

        LOGGER.info("staging files to: " + stagingDir);

        for (final Entry<FileInfo, List<String>> entry : fileManager.getMap().entrySet())
        {
            final List<String> paths = entry.getValue();

            final Path from = new File(paths.get(0)).toPath();
            final Path toRelativePath = baseDir.relativize(from);
            final Path to = stagingDir.resolve(toRelativePath);

            // exclude Thumbs.db files
            if (to.toFile().getName().endsWith("Thumbs.db"))
            {
                continue;
            }

            LOGGER.info("copying: " + from + " -> " + to);

            com.google.common.io.Files.createParentDirs(to.toFile());

            Files.copy(from,
                       to,
                       StandardCopyOption.COPY_ATTRIBUTES);
        }

        return fileManager;
    }
}
