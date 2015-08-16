package backup.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backup.gui.panel.BackupUtilsPanel;
import backup.gui.panel.ComparePanel;
import backup.gui.panel.DigestDirectoryPanel;
import backup.gui.panel.ExplorePanel;

public class BackupUtilsGui extends JPanel
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(BackupUtilsGui.class);

    private static final long serialVersionUID = -4788522561229748501L;

    private final JTabbedPane tabbedPane = new JTabbedPane();

    public BackupUtilsGui()
    {
        super(new BorderLayout());

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        addTab(tabbedPane, new ExplorePanel());
        addTab(tabbedPane, new DigestDirectoryPanel());
        addTab(tabbedPane, new ComparePanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void addTab(final JTabbedPane tabbedPane,
                        final BackupUtilsPanel panel)
    {
        tabbedPane.addTab(panel.getTabName(), panel);
    }

    private static void createAndShowGUI()
    {
        // Create and set up the window.
        JFrame frame = new JFrame("Backup Validator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add content to the window.
        frame.add(new BackupUtilsGui(), BorderLayout.CENTER);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(final String[] args)
    {
        LOGGER.info("asdf");

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    //UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                }
                catch (Exception e)
                {
                    LOGGER.error("Error setting look and feel", e);
                }

                createAndShowGUI();
            }
        });
    }
}
