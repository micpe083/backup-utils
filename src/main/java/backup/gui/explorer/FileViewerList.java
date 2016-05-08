package backup.gui.explorer;

import java.util.List;

import backup.api.FileInfo;
import backup.api.FileInfoPath;
import backup.api.FileManager;
import backup.api.filter.FileManagerFilter;
import javafx.collections.ObservableList;

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

    @Override
    public FileManager getSelected(final FileManager fileManager)
    {
        final FileManager fileManagerSelected = new FileManager();

        final ObservableList<FileInfoPath> selectedItems = table.getTableView().getSelectionModel().getSelectedItems();

        for (final FileInfoPath fileInfoPath : selectedItems)
        {
            fileManagerSelected.addFile(fileInfoPath);
        }

        final FileManagerFilter filter = new FileManagerFilter()
        {
            @Override
            public boolean accept(final FileInfo fileInfo,
                                  final List<String> paths)
            {
                return fileManagerSelected.getMap().containsKey(fileInfo);
            }
        };

        final FileManager ret = fileManager.getFileManager(filter);

        return ret;
    }
}
