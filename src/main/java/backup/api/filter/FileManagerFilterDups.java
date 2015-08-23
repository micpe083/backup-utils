package backup.api.filter;

import java.util.List;

import backup.api.FileInfo;

public class FileManagerFilterDups implements FileManagerFilter
{
    @Override
    public boolean accept(final FileInfo fileInfo,
                          final List<String> paths)
    {
        return paths.size() > 1;
    }
}
