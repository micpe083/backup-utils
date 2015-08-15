package backup.gui.common;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import backup.api.BackupUtil;
import backup.api.FileManager;
import backup.gui.explorer.FileExplorer;

public class DigestFilePanel extends JPanel implements ActionListener
{
    private static final long serialVersionUID = 3167744253202577782L;

    private final FileChooserPanel fileChooserDigestFile;

    private final FileManager fileManager = new FileManager();

    private final StatsPanel statsPanel = new StatsPanel();

    private final JButton processButton;
    private final JButton exploreButton;
    private final JButton exploreDupsButton;

    public DigestFilePanel()
    {
        this("Digest file:");
    }

    public DigestFilePanel(final String text)
    {
        setLayout(new GridLayout(0, 1));

        fileChooserDigestFile = new FileChooserPanel(text, JFileChooser.FILES_ONLY);
        add(fileChooserDigestFile);

        BackupUtil.setFile(fileChooserDigestFile);

        add(statsPanel);

        exploreDupsButton = new JButton("Explore Dups");
        exploreDupsButton.addActionListener(this);

        exploreButton = new JButton("Explore All");
        exploreButton.addActionListener(this);

        processButton = new JButton("Process File");
        processButton.addActionListener(this);

        final JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(exploreDupsButton);
        buttonPanel.add(exploreButton);
        buttonPanel.add(processButton);

        add(buttonPanel);
    }

    public void setSelection(final String text)
    {
        fileChooserDigestFile.setSelection(text);
    }

    public FileManager getFileManager()
    {
        return fileManager;
    }

    public void processFile()
    {
        try
        {
            FileManagerUtil.asdf(fileChooserDigestFile, fileManager);

            statsPanel.setFileManager(fileManager);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,
                                          "Failed to process directory: " + e.getMessage(),
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == processButton)
        {
            processFile();
        }
        else if (e.getSource() == exploreDupsButton)
        {
            FileExplorer.createAndShowGUI(fileManager.getFilesWithDups());
        }
        else if (e.getSource() == exploreButton)
        {
            FileExplorer.createAndShowGUI(fileManager);
        }
    }
}
