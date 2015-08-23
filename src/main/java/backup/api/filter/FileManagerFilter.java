package backup.api.filter;

import java.util.List;

import backup.api.FileInfo;

public interface FileManagerFilter
{
    boolean accept(FileInfo fileInfo, List<String> paths);
}
