package backup.gui.common;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GuiUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiUtils.class);

    private GuiUtils() {}

    public static void showErrorMessage(final Node root,
                                        final String message,
                                        final String title,
                                        final Exception e)
    {
        LOGGER.error(e.getMessage(), e);

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

    public static void setBorder(final Node pane)
    {
        final String cssDefault = "-fx-border-color: blue;\n" +
                                  "-fx-border-insets: 5;\n" +
                                  "-fx-border-width: 3;\n" +
                                  "-fx-border-style: dashed;\n";

        pane.setStyle(cssDefault);
    }
}
