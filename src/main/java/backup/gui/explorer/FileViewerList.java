package backup.gui.explorer;

import backup.api.FileManager;

public class FileViewerList extends FileViewer
{
    private final FileInfoTablePanel table;

    public FileViewerList()
    {
        this.table = new FileInfoTablePanel();
        setCenter(table);
    }

    @Override
    public void setFileManager(final FileManager fileManager)
    {
        table.addFiles(fileManager);
    }
}
