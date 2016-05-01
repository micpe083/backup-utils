package backup.gui.explorer;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.BorderPane;
import backup.api.FileInfo;
import backup.api.FileManager;

public abstract class FileViewer extends BorderPane
{
    private final List<PathSelectionListener> listeners = new ArrayList<PathSelectionListener>();

    public abstract void setFileManager(FileManager fileManager);

    public abstract FileManager getSelected();

    public void addListener(final PathSelectionListener l)
    {
        synchronized (listeners)
        {
            listeners.add(l);
        }
    }

    public void removeListener(final PathSelectionListener l)
    {
        synchronized (listeners)
        {
            listeners.remove(l);
        }
    }

    protected void notifyListeners(final FileInfo fileInfo, final String path)
    {
        synchronized (listeners)
        {
            for (final PathSelectionListener pathSelectionListener : listeners)
            {
                pathSelectionListener.fileSelected(fileInfo, path);
            }
        }
    }
}
