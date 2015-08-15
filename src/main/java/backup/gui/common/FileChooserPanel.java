package backup.gui.common;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileChooserPanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 3997921241126742703L;

    private final JButton fcButton;
    private final JTextField fcTextField;
    private final JFileChooser fc;

    public FileChooserPanel(final String labelText,
                            final int mode)
    {
        super(new BorderLayout());

        fc = new JFileChooser();
        fc.setFileSelectionMode(mode); 

        final JLabel label = new JLabel(labelText);

        fcTextField = new JTextField();
        fcButton = new JButton("Browse...");
        fcButton.addActionListener(this);

        add(label, BorderLayout.WEST);
        add(fcTextField, BorderLayout.CENTER);
        add(fcButton, BorderLayout.EAST);
    }

    public void setSelection(final String text)
    {
        fcTextField.setText(text);
    }

    public String getSelectionStr()
    {
        return fcTextField.getText();
    }

    public File getSelectionFile()
    {
        return new File(getSelectionStr());
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == fcButton)
        {
            final int returnVal = fc.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                final File file = fc.getSelectedFile();

                fcTextField.setText(file.getAbsolutePath());
            }
        }
    }
}
