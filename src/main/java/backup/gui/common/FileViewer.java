package backup.gui.common;

import java.io.BufferedReader;
import java.io.File;

import backup.api.BackupUtil;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileViewer extends BorderPane
{
    private final TextArea textArea;

    public FileViewer()
    {
        textArea = new TextArea();
        textArea.setEditable(false);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        setCenter(scrollPane);
    }

    private void readFile(final File file) throws Exception
    {
        textArea.setText("");

        if (file != null)
        {
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

    public static FileViewer view(final File file,
                                  final Node node)
    {
        final FileViewer fileViewer = new FileViewer();

        try
        {
            fileViewer.readFile(file);

            final Scene scene = new Scene(fileViewer,
                                          800,
                                          600);

            final Stage stage = new Stage();
            stage.setTitle(file.getAbsolutePath());
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e)
        {
            GuiUtils.showErrorMessage(node,
                                      "Failed to show file: " + file,
                                      "Error",
                                      e);
        }

        return fileViewer;
    }
}
