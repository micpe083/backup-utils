package backup.gui.common;

import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import backup.api.StopWatch;

public class ProgressDialog
{
    private final Dialog<Void> dialog;
    private final Task<Void> task;

    private final TextField timeTextField;
    private final TextField messageTextField;

    private final StopWatch stopWatch = new StopWatch();

    public ProgressDialog(final Task<Void> task,
                          final String title,
                          final String text)
    {
        this.task = task;

        timeTextField = createTextField();

        dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(text);
        dialog.setResizable(true);

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

        messageTextField = createTextField();

        vbox.getChildren().add(progressPane);
        vbox.getChildren().add(messageTextField);
        vbox.getChildren().add(timeTextField);

        dialog.getDialogPane().setContent(vbox);

        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(task.progressProperty());

        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().bind(task.progressProperty());

        messageTextField.textProperty().unbind();
        messageTextField.textProperty().bind(task.messageProperty());

        task.messageProperty().addListener(x -> updateTime());

        dialog.setOnCloseRequest(x -> task.cancel());

        task.setOnSucceeded(x -> onFinished());
        task.setOnFailed(x -> onFinished());
        //task.setOnCancelled(x -> );
        //task.setOnRunning(x -> );
    }

    private TextField createTextField()
    {
        final TextField textField = new TextField();
        textField.setEditable(false);
        textField.textProperty().addListener((x, y, z) -> textField.setTooltip(new Tooltip(z)));
        return textField;
    }

    private void updateTime()
    {
        timeTextField.setText(stopWatch.getDescription());
    }

    private void onFinished()
    {
        stopWatch.stop();

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
        stopWatch.start();

        updateTime();

        new Thread(task).start();

        dialog.showAndWait();
    }
}
