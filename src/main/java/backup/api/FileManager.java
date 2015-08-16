package backup.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import backup.api.DigestUtil.DigestAlg;

public class FileManager
{
    //private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);

    private final Map<FileInfo, List<String>> map = new HashMap<FileInfo, List<String>>();

    private final DigestUtil digestUtil = new DigestUtil(DigestAlg.MD5);

    public FileManager()
    {
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

    public void addFile(final String fileInfoStr)
    {
        final FileInfoPath fileInfoPath = digestUtil.toFileInfo(fileInfoStr);

        addFile(fileInfoPath.getFileInfo(),
                fileInfoPath.getPath());
    }

    public FileSum[] getFileSum()
    {
        long countTotal = 0;
        long countUnique = 0;
        long countDuplicate = 0;

        long sizeTotal = 0;
        long sizeUnique = 0;
        long sizeDuplicate = 0;

        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final FileInfo fileInfo = entry.getKey();
            final List<String> paths = entry.getValue();

            final long size = fileInfo.getSize();
            final int ln = paths.size();
            final int dup = ln - 1;

            countTotal += ln;
            countDuplicate += dup;
            countUnique += 1;

            sizeTotal += ln * size;
            sizeDuplicate += dup * size;
            sizeUnique += size;
        }

        final FileSum[] fileSums = new FileSum[]
        {
            FileSum.createCount(countTotal, countUnique, countDuplicate),
            FileSum.createSize(sizeTotal, sizeUnique, sizeDuplicate),
        };

        return fileSums;
    }

    public Map<FileInfo, List<String>> findDups()
    {
        final Map<FileInfo, List<String>> ret = new HashMap<FileInfo, List<String>>();

        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final FileInfo fileInfo = entry.getKey();
            final List<String> paths = entry.getValue();

            final boolean hasDups = paths.size() > 1;

            if (hasDups)
            {
                ret.put(fileInfo,
                        paths);
            }
        }

        if (ret.isEmpty())
        {
            System.out.println("No duplicates found");
        }
        //else
        //{
        //    //count, size = get_file_sum (file_dict)
        //    //f.write ('# Duplicate files found' + '\n')
        //    //f.write ('# Count: ' + str(count) + '\n')
        //    //f.write ('# Size (bytes): ' + str(size) + '\n')
        //    //print_dict (ret, f)
        //}

        return ret;
    }

    public FileManager getFilesWithDups()
    {
        final FileManager fileManager = new FileManager();

        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final FileInfo fileInfo = entry.getKey();
            final List<String> paths = entry.getValue();

            fileManager.map.put(fileInfo, paths);
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

    public void print()
    {
        print(map);
    }

    public void print(final Map<FileInfo, List<String>> map)
    {
        for (final Entry<FileInfo, List<String>> entry : map.entrySet())
        {
            final FileInfo fileInfo = entry.getKey();
            final List<String> paths = entry.getValue();

            System.out.println("### " + digestUtil.toFileInfoStr(fileInfo));

            for (final String path : paths)
            {
                System.out.println("#      " + path);
            }
        }
    }

    public FileManager getMissing(final FileManager other)
    {
        final FileManager ret = new FileManager();

        ret.map.putAll(map);
        ret.map.keySet().removeAll(other.map.keySet());

        System.out.println("missing files: " + ret.map.size());

        print(ret.map);

        return ret;
    }

    public void clear()
    {
        map.clear();
    }

    public void loadDigestFile(final File file) throws IOException
    {
        // TODO: validate file exists

        clear();

        try (final BufferedReader reader = BackupUtil.createReader(file))
        {
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                if (!line.isEmpty() &&
                    !line.startsWith("#"))
                {
                    addFile(line);
                }
            }
        }
    }
}
