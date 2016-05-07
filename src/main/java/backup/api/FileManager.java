package backup.api;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backup.api.FileSum.CountSize;
import backup.api.filter.FileManagerFilter;

/**
 * @author Michael Peterson
 *
 */
public class FileManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);

    private final Map<FileInfo, List<String>> map = new HashMap<FileInfo, List<String>>();

    public FileManager()
    {
    }

    public void addFile(final FileInfoPath fileInfoPath)
    {
        if (fileInfoPath != null)
        {
            addFile(fileInfoPath.getFileInfo(),
                    fileInfoPath.getPath());
        }
    }

    public void addFile(final FileInfo fileInfo,
                        final String path)
    {
        List<String> paths = map.get(fileInfo);

        if (paths == null)
        {
            paths = new ArrayList<String>();
            map.put(fileInfo, paths);
        }

        paths.add(path);
    }

    public Map<FileInfo, List<String>> getMap()
    {
        return map;
    }

    public FileSum getFileSum()
    {
        final FileSum fileSum = new FileSum();

        final CountSize total = fileSum.getTotal();
        final CountSize unique = fileSum.getUnique();
        final CountSize duplicate = fileSum.getDuplicate();

        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final FileInfo fileInfo = entry.getKey();
            final List<String> paths = entry.getValue();

            final long size = fileInfo.getSize();
            final int ln = paths.size();
            final int dup = ln - 1;

            total.incr(ln, ln * size);
            duplicate.incr(dup, dup * size);
            unique.incr(1, size);
        }

        return fileSum;
    }

    public FileManager getFileManager(final FileManagerFilter filter)
    {
        final FileManager fileManager = new FileManager();

        if (filter == null)
        {
            fileManager.map.putAll(map);
        }
        else
        {
            for (final Entry<FileInfo, List<String>> entry : map.entrySet())
            {
                final FileInfo fileInfo = entry.getKey();
                final List<String> paths = entry.getValue();

                if (filter.accept(fileInfo, paths))
                {
                    fileManager.map.put(fileInfo, paths);
                }
            }
        }

        return fileManager;
    }

    public FileManager getUniquePaths()
    {
        final FileManager fileManager = new FileManager();

        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final FileInfo fileInfo = entry.getKey();
            final List<String> paths = entry.getValue();

            fileManager.addFile(fileInfo, paths.get(0));
        }

        return fileManager;
    }

    public FileManager getMissing(final FileManager other)
    {
        final FileManager ret = new FileManager();

        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final FileInfo fileInfo = entry.getKey();

            if (!BackupUtil.shouldExclude(fileInfo.getFilename()))
            {
                final List<String> paths = entry.getValue();

                for (final String path : paths)
                {
                    ret.addFile(fileInfo, path);
                }
            }
        }

        //ret.map.putAll(map);

        ret.map.keySet().removeAll(other.map.keySet());

        LOGGER.info("missing files: " + ret.map.size());

        return ret;
    }

    public void clear()
    {
        map.clear();
    }

    public void loadDigestFile(final File zipFile) throws Exception
    {
        clear();

        final File file = DigestFileUtil.getDigestFile(zipFile);

        try (final BufferedReader reader = BackupUtil.createReader(file))
        {
            String line = null;

            DigestUtil digestUtil = null;

            while ((line = reader.readLine()) != null)
            {
                if (!line.isEmpty() &&
                    !line.startsWith("#"))
                {
                    if (digestUtil == null)
                    {
                        digestUtil = DigestUtil.createMatching(line);
                    }

                    final FileInfoPath fileInfoPath = digestUtil.toFileInfo(line);

                    addFile(fileInfoPath.getFileInfo(),
                            fileInfoPath.getPath());
                }
            }
        }
    }

    public Path getBaseDir()
    {
        String commonDir = null;

        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final List<String> paths = entry.getValue();

            for (final String currPath : paths)
            {
                commonDir = BackupUtil.findCommonPath(commonDir,
                                                      currPath);
            }
        }

        return commonDir == null ? null : Paths.get(commonDir);
    }
}
