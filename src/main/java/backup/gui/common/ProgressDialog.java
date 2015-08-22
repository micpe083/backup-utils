package backup.gui.common;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ProgressDialog
{
    private final Dialog<Void> dialog;
    private final Task<Void> task;

    private final TextField timeTextField;

    private LocalTime startDate;
    private LocalTime endDate;

    public ProgressDialog(final Task<Void> task,
                          final String title,
                          final String text)
    {
        this.task = task;

        timeTextField = new TextField();
        dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(text);

        //dialog.setGraphic(new ImageView(this.getClass().getResource("icon.png").toString()));

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        setButtonState(true, false);

        final VBox vbox = new VBox();

        final BorderPane progressPane = new BorderPane();

        final ProgressBar progressBar = new ProgressBar(0);
        progressPane.setCenter(progressBar);

        final ProgressIndicator progressIndicator = new ProgressIndicator(0);
        progressPane.setRight(progressIndicator);

        progressBar.prefWidthProperty().bind(vbox.widthProperty().subtract(progressIndicator.widthProperty()));

        final TextField textField = new TextField();
        textField.setEditable(false);

        vbox.getChildren().add(progressPane);
        vbox.getChildren().add(textField);
        vbox.getChildren().add(timeTextField);

        dialog.getDialogPane().setContent(vbox);

        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(task.progressProperty());

        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().bind(task.progressProperty());

        textField.textProperty().unbind();
        textField.textProperty().bind(task.messageProperty());

        task.messageProperty().addListener(x -> updateTime());

        dialog.setOnCloseRequest(x -> task.cancel());

        task.setOnSucceeded(x -> onFinished());
        task.setOnFailed(x -> onFinished());
        //task.setOnCancelled(x -> );
        //task.setOnRunning(x -> );
    }

    private void updateTime()
    {
        final StringBuilder buf = new StringBuilder();

        final LocalTime startDate = this.startDate;

        if (startDate == null)
        {
            buf.append("Not started");
        }
        else
        {
            buf.append("Duration: ");

            final Duration duration = Duration.between(startDate, LocalTime.now());

            final long hours = duration.toHours();
            final long minutes = duration.toMinutes() - 60 * duration.toHours();
            final long seconds = duration.getSeconds() - 60 * duration.toMinutes();

            buf.append(String.format("%02d", hours));
            buf.append(":");
            buf.append(String.format("%02d", minutes));
            buf.append(":");
            buf.append(String.format("%02d", seconds));
            buf.append(" ");

            buf.append("Start: ").append(startDate);
            buf.append(" ");

            final LocalTime endDate = this.endDate;
            if (endDate != null)
            {
                endDate.truncatedTo(ChronoUnit.SECONDS);
                buf.append("End: ").append(endDate);
            }
        }

        timeTextField.setText(buf.toString());
    }

    private void onFinished()
    {
        endDate = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        updateTime();

        setButtonState(false, true);
    }

    private void setButtonState(final boolean ok,
                                final boolean cancel)
    {
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(ok);
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setDisable(cancel);
    }

    public void start()
    {
        startDate = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

        updateTime();

        new Thread(task).start();

        dialog.showAndWait();
    }
}
