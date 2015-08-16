package backup.gui.panel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import backup.api.DigestUtil;
import backup.api.DigestUtil.DigestAlg;
import backup.gui.common.BackupSettings;
import backup.gui.common.DigestSelectorPanel;
import backup.gui.common.FileChooserPanel;


public class DigestDirectoryPanel extends BackupUtilsPanel implements ActionListener
{
    private static final long serialVersionUID = -2862618620912356842L;

    private final FileChooserPanel fileChooserDigestDir;
    private final FileChooserPanel fileChooserOutputDir;

    private final DigestSelectorPanel digestSelectorPanel;

    public DigestDirectoryPanel()
    {
        setLayout(new GridLayout(0, 1));

        fileChooserDigestDir = new FileChooserPanel("Digest dir:", JFileChooser.DIRECTORIES_ONLY);
        fileChooserDigestDir.setSelection(BackupSettings.getInstance().getString(BackupSettings.DIGEST_DIR));

        fileChooserOutputDir = new FileChooserPanel("Output dir", JFileChooser.DIRECTORIES_ONLY);
        fileChooserOutputDir.setSelection(BackupSettings.getInstance().getString(BackupSettings.DIGEST_OUTPUT_DIR));

        digestSelectorPanel = new DigestSelectorPanel();
        add(digestSelectorPanel);

        add(fileChooserDigestDir);
        add(fileChooserOutputDir);
    }

    @Override
    public String getTabName()
    {
        return "Create Digest";
    }

    @Override
    public void execute()
    {
        try
        {
            final File digestDir = fileChooserDigestDir.getSelectionFile();
            final File outputDir = fileChooserOutputDir.getSelectionFile();

            final DigestAlg digestAlg = digestSelectorPanel.getDigestAlg();

            final DigestUtil digestUtil = new DigestUtil(digestAlg);

            digestUtil.createDigestFile(digestDir,
                                        outputDir);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this,
                                          "Failed to process directory: " + e.getMessage(),
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);

            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
    }
}
