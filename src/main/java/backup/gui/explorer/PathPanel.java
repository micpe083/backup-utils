package backup.gui.explorer;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PathPanel extends JPanel
{
    private static final long serialVersionUID = 6522214165973532373L;

    private final JTextField pathTextField;

    public PathPanel()
    {
        super(new BorderLayout());

        final JLabel label = new JLabel("Path:");
        add(label, BorderLayout.WEST);

        pathTextField = new JTextField("-");
        pathTextField.setEditable(false);
        add(pathTextField, BorderLayout.CENTER);
    }

    public void setPath(final String path)
    {
        pathTextField.setText(path);
    }
}
