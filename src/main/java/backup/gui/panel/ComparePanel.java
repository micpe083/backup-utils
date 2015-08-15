package backup.gui.panel;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import backup.api.FileManager;
import backup.gui.common.DigestFilePanel;
import backup.gui.explorer.FileExplorer;

public class ComparePanel extends BackupValidatorPanel implements ActionListener
{
    private static final long serialVersionUID = -5533699007173050922L;

    private final DigestFilePanel digestFilePanelOriginal;
    private final DigestFilePanel digestFilePanelCurrent;

    private final JButton missingButton;
    private final JButton newButton;

    public ComparePanel()
    {
        setLayout(new GridLayout(0, 1));

        digestFilePanelOriginal = new DigestFilePanel("Original File:");
        add(digestFilePanelOriginal);

        digestFilePanelCurrent = new DigestFilePanel("Current File:");
        add(digestFilePanelCurrent);

        missingButton = new JButton("Missing");
        missingButton.addActionListener(this);

        newButton = new JButton("New");
        newButton.addActionListener(this);

        final JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(missingButton);
        buttonPanel.add(newButton);
        add(buttonPanel);
    }

    @Override
    public String getTabName()
    {
        return "Compare";
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == missingButton)
        {
            compare(digestFilePanelOriginal,
                    digestFilePanelCurrent);
        }
        else if (e.getSource() == newButton)
        {
            compare(digestFilePanelCurrent,
                    digestFilePanelOriginal);
        }
    }

    private void compare(final DigestFilePanel digestFilePanel1,
                         final DigestFilePanel digestFilePanel2)
    {
        final FileManager fileManager1 = digestFilePanel1.getFileManager();
        final FileManager fileManager2 = digestFilePanel2.getFileManager();

        final FileManager fileManagerMissing = fileManager1.getMissing(fileManager2);

        FileExplorer.createAndShowGUI(fileManagerMissing);
    }

    @Override
    public void execute()
    {
        try
        {
            digestFilePanelOriginal.processFile();

            digestFilePanelCurrent.processFile();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,
                                          "Failed to process directory: " + e.getMessage(),
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);

            LOGGER.error(e.getMessage(), e);
        }
    }
}
