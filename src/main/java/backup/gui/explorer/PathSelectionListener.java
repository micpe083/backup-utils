package backup.gui.explorer;

import backup.api.FileInfo;

public interface PathSelectionListener
{
    void fileSelected(FileInfo fileInfo, String path);
}
