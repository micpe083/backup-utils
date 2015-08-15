package backup.api;

public class FileInfoPath
{
    private final FileInfo fileInfo;
    private final String path;

    public FileInfoPath(final FileInfo fileInfo,
                        final String path)
    {
        this.fileInfo = fileInfo;
        this.path = path;
    }

    public FileInfo getFileInfo()
    {
        return fileInfo;
    }

    public String getPath()
    {
        return path;
    }
}
