package backup.gui.common;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LabelTextPanel extends HBox
{
    private final TextField textField;

    public LabelTextPanel(final String labelText)
    {
        final Label label = new Label(labelText);
        textField = new TextField();
        getChildren().add(label);
        getChildren().add(textField);
    }

    public String getText()
    {
        return textField.getText();
    }
}
