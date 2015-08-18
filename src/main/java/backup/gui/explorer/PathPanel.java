package backup.gui.explorer;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class PathPanel extends BorderPane
{
    private final TextField pathTextField;

    public PathPanel()
    {
        final Label label = new Label("Path:");
        setLeft(label);

        pathTextField = new TextField("-");
        pathTextField.setEditable(false);
        setCenter(pathTextField);
    }

    public void setPath(final String path)
    {
        pathTextField.setText(path);
    }
}
