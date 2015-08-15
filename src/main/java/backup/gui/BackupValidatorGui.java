package backup.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import backup.gui.panel.BackupValidatorPanel;
import backup.gui.panel.ComparePanel;
import backup.gui.panel.DigestDirectoryPanel;
import backup.gui.panel.ExplorePanel;

public class BackupValidatorGui extends JPanel
{
    private static final long serialVersionUID = -4788522561229748501L;

    private final JTabbedPane tabbedPane = new JTabbedPane();

    public BackupValidatorGui()
    {
        super(new BorderLayout());

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        addTab(tabbedPane, new ExplorePanel());
        addTab(tabbedPane, new DigestDirectoryPanel());
        addTab(tabbedPane, new ComparePanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void addTab(final JTabbedPane tabbedPane,
                        final BackupValidatorPanel panel)
    {
        tabbedPane.addTab(panel.getTabName(), panel);
    }

    private static void createAndShowGUI()
    {
        // Create and set up the window.
        JFrame frame = new JFrame("Backup Validator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add content to the window.
        frame.add(new BackupValidatorGui(), BorderLayout.CENTER);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
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
                    e.printStackTrace();
                }

                createAndShowGUI();
            }
        });
    }
}
