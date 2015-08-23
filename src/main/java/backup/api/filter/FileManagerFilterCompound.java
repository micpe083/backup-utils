package backup.api.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import backup.api.FileInfo;

public class FileManagerFilterCompound implements FileManagerFilter
{
    private final List<FileManagerFilter> filters = new ArrayList<>();

    public FileManagerFilterCompound()
    {
    }

    public FileManagerFilterCompound(final FileManagerFilter... filters)
    {
        this.filters.addAll(Arrays.asList(filters));
    }

    public void add(final FileManagerFilter filter)
    {
        filters.add(filter);
    }

    @Override
    public boolean accept(final FileInfo fileInfo,
                          final List<String> paths)
    {
        boolean accept = false;

        for (final FileManagerFilter filter : filters)
        {
            accept = filter.accept(fileInfo, paths);

            if (!accept)
            {
                break;
            }
        }

        return accept;
    }
}
