package backup.api;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DeleteDuplicatesManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);

    private DeleteDuplicatesManager()
    {
    }

    public static FileManager deleteDuplicates(final FileManager fileManager) throws Exception
    {
        LOGGER.info("delete duplicates file summary: " + fileManager.getFileSum());

        for (final Entry<FileInfo, List<String>> entry : fileManager.getMap().entrySet())
        {
            final List<String> paths = entry.getValue();

            if (paths.size() > 1)
            {
                boolean foundFile = false;

                for (final String pathStr : paths)
                {
                    final Path path = new File(pathStr).toPath();

                    if (Files.exists(path,
                                     LinkOption.NOFOLLOW_LINKS))
                    {
                        if (foundFile)
                        {
                            LOGGER.info("deleting duplicate file: " + path);

                            Files.delete(path);
                        }
                        else
                        {
                            foundFile = true;
                        }
                    }
                }
            }
        }

        return fileManager;
    }
}
