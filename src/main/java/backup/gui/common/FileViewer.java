package backup.gui.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.EvictingQueue;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import backup.api.BackupUtil;
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

    private void readFile(final File zipFile) throws Exception
    {
        textArea.setText("");
        textField.setText("");

        if (zipFile != null)
        {
            textField.setText(zipFile.getAbsolutePath());

            final File file = DigestFileUtil.getDigestFile(zipFile);
            textArea.setText(getContent(file));

            textArea.appendText("\n\n");
            textArea.appendText("ERROR FILE\n");
            textArea.appendText("\n\n");

            final File fileErr = DigestFileUtil.getDigestErrFile(zipFile);
            textArea.appendText(getContent(fileErr));

            textArea.positionCaret(0);
        }
    }

    public static FileViewer view(final File zipFile,
                                  final Node node)
    {
        final FileViewer fileViewer = new FileViewer();

        try
        {
            fileViewer.readFile(zipFile);

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

    private static String getContent(final File file) throws Exception
    {
        final int lines = 50;
        final List<String> start = new ArrayList<String>(lines);
        final EvictingQueue<String> end = EvictingQueue.create(lines);

        final LineProcessor<String> callback = new LineProcessor<String>()
        {
            @Override
            public boolean processLine(final String line) throws IOException
            {
                if (start.size() < lines)
                {
                    start.add(line);
                }
                else
                {
                    end.add(line);
                }

                return true;
            }

            @Override
            public String getResult()
            {
                final StringBuilder buf = new StringBuilder();

                for (final String line : start)
                {
                    buf.append(line).append('\n');
                }

                if (end.remainingCapacity() == 0)
                {
                    buf.append("").append('\n');
                    buf.append("... LINES SKIPPED ...").append('\n');
                    buf.append("").append('\n');
                }

                for (final String line : end)
                {
                    buf.append(line).append('\n');
                }

                return buf.toString();
            }
        };

        return Files.readLines(file,
                               BackupUtil.CHARSET_UTF8,
                               callback);
    }
}
