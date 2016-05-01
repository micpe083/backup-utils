package backup.gui.common;

import java.io.BufferedReader;
import java.io.File;

import backup.api.BackupUtil;
import backup.api.DigestUtil;
import backup.api.DigestFileUtil;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileViewer extends BorderPane
{
    private final TextArea textArea;
    private final TextField textField;

    public FileViewer()
    {
        textArea = new TextArea();
        textArea.setEditable(false);

        textField = new TextField();
        textField.setEditable(false);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        setTop(textField);
        setCenter(scrollPane);
    }

    private void readFile(final File file) throws Exception
    {
        textArea.setText("");
        textField.setText("");

        if (file != null)
        {
            textField.setText(file.getAbsolutePath());
            textArea.setText(getFileContent(file));
        }
    }

    private static String getFileContent(final File file) throws Exception
    {
        final StringBuilder buf = new StringBuilder();

        final boolean isCommentsOnly = file.length() > 10_000_000;

        try (final BufferedReader reader = BackupUtil.createReader(file))
        {
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                final boolean isComment = line.startsWith("#");

                if (isComment || !isCommentsOnly)
                {
                    buf.append(line);
                    buf.append('\n');
                }
            }
        }

        return buf.toString();
    }

    public static FileViewer view(final File zipFile,
                                  final Node node)
    {
        final FileViewer fileViewer = new FileViewer();

        try
        {
            final File file = DigestFileUtil.getDigestFile(zipFile);

            fileViewer.readFile(file);

            final Scene scene = new Scene(fileViewer,
                                          800,
                                          600);

            final Stage stage = new Stage();
            stage.setTitle("File Viewer");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e)
        {
            GuiUtils.showErrorMessage(node,
                                      "Failed to show file: " + zipFile,
                                      "Error",
                                      e);
        }

        return fileViewer;
    }
}
