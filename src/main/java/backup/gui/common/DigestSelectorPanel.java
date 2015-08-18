package backup.gui.common;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import backup.api.DigestUtil.DigestAlg;

public class DigestSelectorPanel extends HBox
{
    private final ToggleGroup group = new ToggleGroup();

    public DigestSelectorPanel()
    {
        final Label label = new Label("Digest alg:");

        getChildren().add(label);

        for (final DigestAlg digestAlg : DigestAlg.values())
        {
            final RadioButton digestButton = new RadioButton(digestAlg.getName());

            digestButton.setToggleGroup(group);

            getChildren().add(digestButton);
        }

        group.getToggles().get(0).setSelected(true);
    }

    public DigestAlg getDigestAlg()
    {
        final String digest = ((RadioButton) group.getSelectedToggle()).getText();

        return DigestAlg.valueOf(digest);
    }
}
