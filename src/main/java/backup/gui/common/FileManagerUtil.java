package backup.gui.common;

import java.io.File;
import java.io.IOException;

import backup.api.FileManager;

public final class FileManagerUtil
{
    private FileManagerUtil() {}

    public static void loadDigestFile(final FileChooserComboPanel panel,
                                      final FileManager fileManager) throws IOException
    {
        final File digestFile = panel.getSelectedFileWarn();

        if (digestFile != null)
        {
            fileManager.loadDigestFile(digestFile);
        }
    }
}
