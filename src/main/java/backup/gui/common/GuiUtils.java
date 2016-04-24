package backup.gui.common;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TitledPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public final class GuiUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiUtils.class);

    private GuiUtils() {}

    public static void showErrorMessage(final Node root,
                                        final String message,
                                        final String title,
                                        final Exception e)
    {
        final StringBuilder buf = new StringBuilder();
        buf.append(title).append(" - ").append(message);

        if (e != null)
        {
            buf.append(" - err: ").append(e.getMessage());
        }

        LOGGER.error(buf.toString(), e);

        final Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static ScrollPane createScrollPane(final Node node)
    {
        final ScrollPane scrollPane = new ScrollPane(node);

        scrollPane.fitToHeightProperty().set(true);
        scrollPane.fitToWidthProperty().set(true);

        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);

        return scrollPane;
    }

    public static TitledPane createTitledPane(final String title,
                                              final Node content,
                                              final boolean expanded)
    {
        final TitledPane titledPane = new TitledPane(title, content);
        titledPane.setExpanded(expanded);
        return titledPane;
    }

    public static void setBorder(final Node pane)
    {
        final String cssDefault = "-fx-border-color: blue;\n" +
                                  "-fx-border-insets: 5;\n" +
                                  "-fx-border-width: 3;\n" +
                                  "-fx-border-style: dashed;\n";

        pane.setStyle(cssDefault);
    }

    public static String getSelectedFileStr(final String selectedFileStr)
    {
        return Strings.isNullOrEmpty(selectedFileStr) ? null : selectedFileStr;
    }

    public static File getSelectedFile(final String selectedFileStr)
    {
        return Strings.isNullOrEmpty(selectedFileStr) ? null : new File(selectedFileStr);
    }

    public static void setFile(final FileChooserComboPanel fileChooserDigestFile) throws Exception
    {
        fileChooserDigestFile.setSelection(BackupSettings.getInstance().getOutputDir());
    }

    public static File getFile(final File file,
                               final String descr,
                               final Node node) throws Exception
    {
        if (file == null)
        {
            GuiUtils.showErrorMessage(node,
                                      descr + " not selected",
                                      "Error",
                                      null);
        }

        return file;
    }

    public static File selectFile(final boolean isFile,
                                  final File selectedFile,
                                  final Scene scene)
    {
        final File file;

        if (isFile)
        {
            final FileChooser fileChooser = new FileChooser();

            if (selectedFile != null && selectedFile.getParentFile().isDirectory())
            {
                fileChooser.setInitialDirectory(selectedFile.getParentFile());
            }

            file = fileChooser.showOpenDialog(scene.getWindow());
        }
        else
        {
            final DirectoryChooser fileChooser = new DirectoryChooser();

            if (selectedFile != null && selectedFile.isDirectory())
            {
                fileChooser.setInitialDirectory(selectedFile);
            }

            file = fileChooser.showDialog(scene.getWindow());
        }

        return file;
    }
}
