package backup.gui.panel;

import java.io.File;
import java.util.concurrent.CancellationException;

import backup.api.BackupUtil;
import backup.api.DigestUtil;
import backup.api.DigestUtil.DigestAlg;
import backup.api.DigestUtil.DigestProgressListener;
import backup.api.StopWatch;
import backup.gui.common.BackupSettings;
import backup.gui.common.DigestSelectorPanel;
import backup.gui.common.FileChooserPanel;
import backup.gui.common.GuiUtils;
import backup.gui.common.LabelTextPanel;
import backup.gui.common.ProgressDialog;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class DigestDirectoryPanel extends BackupUtilsPanel
{
    private final FileChooserPanel fileChooserDigestDir;
    private final FileChooserPanel fileChooserOutputDir;

    private final LabelTextPanel commentsTextPanel;

    private final DigestSelectorPanel digestSelectorPanel;

    public DigestDirectoryPanel() throws Exception
    {
        final VBox pane = new VBox();

        fileChooserDigestDir = new FileChooserPanel("Digest dir:", false);
        fileChooserDigestDir.setSelection(BackupSettings.getInstance().getValue(BackupSettings.DIGEST_DIR));

        fileChooserOutputDir = new FileChooserPanel("Output dir", false);
        fileChooserOutputDir.setSelection(BackupSettings.getInstance().getOutputDir());

        commentsTextPanel = new LabelTextPanel("Comments:");

        digestSelectorPanel = new DigestSelectorPanel();
        pane.getChildren().add(digestSelectorPanel);

        final Button executeButton = new Button("Execute");
        executeButton.setOnAction(event -> execute());

        pane.getChildren().add(fileChooserDigestDir);
        pane.getChildren().add(fileChooserOutputDir);
        pane.getChildren().add(commentsTextPanel);
        pane.getChildren().add(executeButton);

        setCenter(pane);
    }

    @Override
    public String getTabName()
    {
        return "Create Digest";
    }

    private void execute()
    {
        try
        {
            final File digestDir = fileChooserDigestDir.getSelectedFileWarn();
            final File outputDir = fileChooserOutputDir.getSelectedFileWarn();

            if (digestDir == null || outputDir == null)
            {
                return;
            }

            final DigestAlg digestAlg = digestSelectorPanel.getDigestAlg();

            final Task<Void> task = createWorker(digestDir,
                                                 outputDir,
                                                 commentsTextPanel.getText(),
                                                 digestAlg);

            final ProgressDialog dialog = new ProgressDialog(task,
                                                             "Digest dir",
                                                             "Digesting directory: " + digestDir);

            dialog.start();
        }
        catch (Exception e)
        {
            GuiUtils.showErrorMessage(this,
                                      "Failed to process directory: " + e.getMessage(),
                                      "Error",
                                      e);
        }
    }

    private Task<Void> createWorker(final File digestDir,
                                    final File outputDir,
                                    final String comment,
                                    final DigestAlg digestAlg)
    {
        return new Task<Void>()
        {
            @Override
            protected void cancelled()
            {
                super.cancelled();
                updateMessage("Cancelled");
            }

            @Override
            protected void failed()
            {
                super.failed();
                updateMessage("Failed");
            }

            @Override
            protected void succeeded()
            {
                super.succeeded();
                updateMessage("Finished");
            }

            @Override
            protected Void call() throws Exception
            {
                try
                {
                    callx();
                }
                catch (Exception e)
                {
                    LOGGER.error("error processing", e);
                    throw e;
                }

                return null;
            }

            private void callx() throws Exception
            {
                final DigestUtil digestUtil = new DigestUtil(digestAlg);

                final long startTime = System.currentTimeMillis();

                final DigestProgressListener listener = new DigestProgressListener()
                {
                    @Override
                    public void update(final String message)
                    {
                        updateMessage(message);
                    }

                    @Override
                    public void update(final String message,
                                       final long currFileLen,
                                       final long processedBytes,
                                       final long totalSize)
                    {
                        if (isCancelled())
                        {
                            throw new CancellationException("task cancelled");
                        }

                        final StringBuilder buf = new StringBuilder();

                        if (totalSize > 0)
                        {
                            final double pcCompleted = processedBytes / (double) totalSize;
                            final long timeElapsed = System.currentTimeMillis() - startTime;
                            final long timeRemaining = Math.round(timeElapsed / pcCompleted) - timeElapsed;

                            buf.append("Remaining: ").append(StopWatch.getDuration(timeRemaining));
                            buf.append(" / ");

                            buf.append(BackupUtil.getSpeed(processedBytes, timeElapsed));
                            buf.append(" / ");
                        }

                        if (currFileLen >= 0)
                        {
                            buf.append(BackupUtil.humanReadableByteCount(currFileLen));
                            buf.append(" / ");
                        }

                        buf.append(BackupUtil.humanReadableByteCount(processedBytes));
                        buf.append(" / ");
                        buf.append(BackupUtil.humanReadableByteCount(totalSize));
                        buf.append(" ");
                        buf.append(message);

                        updateMessage(buf.toString());

                        updateProgress(processedBytes, totalSize);
                    }
                };

                digestUtil.createDigestFile(digestDir,
                                            outputDir,
                                            comment,
                                            listener);
            }
        };
    }
}
