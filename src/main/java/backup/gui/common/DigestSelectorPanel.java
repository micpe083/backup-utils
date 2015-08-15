package backup.gui.common;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import backup.api.DigestUtil.DigestAlg;

public class DigestSelectorPanel extends JPanel
{
    private static final long serialVersionUID = 1327242463467592116L;

    private final ButtonGroup group = new ButtonGroup();

    public DigestSelectorPanel()
    {
        super(new FlowLayout());

        final JLabel label = new JLabel("Digest alg:");

        add(label);

        for (final DigestAlg digestAlg : DigestAlg.values())
        {
            final JRadioButton digestButton = new JRadioButton(digestAlg.getName());

            digestButton.setActionCommand(digestAlg.getName());

            group.add(digestButton);

            add(digestButton);

            digestButton.setSelected(true);
        }

    }

    public DigestAlg getDigestAlg()
    {
        final String digest = group.getSelection().getActionCommand();

        return DigestAlg.valueOf(digest);
    }
}
