package backup.gui.explorer;

import backup.api.FileInfo;
import backup.api.FileInfoPath;
import backup.api.FileManager;

public class FileViewerList extends FileViewer
{
    private final FileInfoTablePanel table;

    public FileViewerList()
    {
        this.table = new FileInfoTablePanel();
        setCenter(table);

        table.getTableView().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> onSelect(newSelection));
    }

    private void onSelect(final FileInfoPath fileInfoPath)
    {
        final FileInfo fileInfo = fileInfoPath == null ? null : fileInfoPath.getFileInfo();
        final String path = fileInfoPath == null ? null : fileInfoPath.getPath();

        notifyListeners(fileInfo,
                        path);
    }

    @Override
    public void setFileManager(final FileManager fileManager)
    {
        table.addFiles(fileManager);
    }
}
