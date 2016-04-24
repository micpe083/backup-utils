package backup.gui.common;

import java.io.File;

import com.google.common.base.Strings;

import javafx.scene.layout.BorderPane;

public abstract class FileChooserAbstract extends BorderPane
{
    private final String labelText;

    public FileChooserAbstract(String labelText)
    {
        this.labelText = labelText;
    }

    public File getSelectedFile()
    {
        return GuiUtils.getSelectedFile(getSelectedFileStr());
    }

    public File getSelectedFileWarn()
    {
        final File file = getSelectedFile();

        if (file == null)
        {
            GuiUtils.showErrorMessage(this,
                                      getLabelText() + " not selected",
                                      "Error",
                                      null);
        }

        return file;
    }

    public String getLabelText()
    {
        return labelText;
    }

    public void setSelection(final String fileStr)
    {
        final File file = Strings.isNullOrEmpty(fileStr) ? null : new File(fileStr);

        setSelection(file);
    }

    public final String getSelectedFileStr()
    {
        return GuiUtils.getSelectedFileStr(getSelectedFileStr2());
    }

    public abstract void setSelection(File file);

    protected abstract String getSelectedFileStr2();
}
