// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package TranslatorsGUI;

import Hack.ComputerParts.TextFileGUI;
import Hack.Translators.*;
import HackGUI.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class TranslatorComponent extends JFrame
    implements HackTranslatorGUI
{
    public static String InstallDir = new File("").getAbsolutePath() + "\\src\\InstallDir\\"; 

    public TranslatorComponent(FileFilter filefilter, FileFilter filefilter1)
    {
        ffwdIcon = new ImageIcon(InstallDir + "bin/images/vcrfastforward.gif");
        stopIcon = new ImageIcon(InstallDir + "bin/images/vcrstop.gif");
        singleStepIcon = new ImageIcon(InstallDir + "bin/images/vcrforward.gif");
        rewindIcon = new ImageIcon(InstallDir + "bin/images/vcrrewind.gif");
        fullTranslationIcon = new ImageIcon(InstallDir + "bin/images/hex.gif");
        loadIcon = new ImageIcon(InstallDir + "bin/images/opendoc.gif");
        saveIcon = new ImageIcon(InstallDir + "bin/images/save.gif");
        arrowIcon = new ImageIcon(InstallDir + "bin/images/arrow2.gif");
        sourceFilter = filefilter;
        destFilter = filefilter1;
        init();
        jbInit();
        source.setName("Source");
        destination.setName("Destination");
        sourceFileChooser = new JFileChooser();
        sourceFileChooser.setFileFilter(filefilter);
        destFileChooser = new JFileChooser();
        destFileChooser.setFileFilter(filefilter1);
        source.enableUserInput();
        destination.disableUserInput();
    }

    public void notifyHackTranslatorListeners(byte byte0, Object obj)
    {
        HackTranslatorEvent hacktranslatorevent = new HackTranslatorEvent(this, byte0, obj);
        for(int i = 0; i < listeners.size(); i++)
            ((HackTranslatorEventListener)listeners.elementAt(i)).actionPerformed(hacktranslatorevent);

    }

    public void removeHackTranslatorListener(HackTranslatorEventListener hacktranslatoreventlistener)
    {
        listeners.removeElement(hacktranslatoreventlistener);
    }

    public void addHackTranslatorListener(HackTranslatorEventListener hacktranslatoreventlistener)
    {
        listeners.addElement(hacktranslatoreventlistener);
    }

    public void setWorkingDir(File file)
    {
        sourceFileChooser.setCurrentDirectory(file);
        destFileChooser.setCurrentDirectory(file);
    }

    public void disableStop()
    {
        stopButton.setEnabled(false);
        stopMenuItem.setEnabled(false);
    }

    public void enableStop()
    {
        stopButton.setEnabled(true);
        stopMenuItem.setEnabled(true);
    }

    public void disableFastForward()
    {
        ffwdButton.setEnabled(false);
        ffwdMenuItem.setEnabled(false);
    }

    public void enableFastForward()
    {
        ffwdButton.setEnabled(true);
        ffwdMenuItem.setEnabled(true);
    }

    public void disableSingleStep()
    {
        singleStepButton.setEnabled(false);
        singleStepMenuItem.setEnabled(false);
    }

    public void enableSingleStep()
    {
        singleStepButton.setEnabled(true);
        singleStepMenuItem.setEnabled(true);
    }

    public void disableRewind()
    {
        rewindButton.setEnabled(false);
        rewindMenuItem.setEnabled(false);
    }

    public void enableRewind()
    {
        rewindButton.setEnabled(true);
        rewindMenuItem.setEnabled(true);
    }

    public void disableSave()
    {
        saveButton.setEnabled(false);
        saveDestMenuItem.setEnabled(false);
    }

    public void enableSave()
    {
        saveButton.setEnabled(true);
        saveDestMenuItem.setEnabled(true);
    }

    public void disableFullCompilation()
    {
        fullTranslationButton.setEnabled(false);
        fullTranslationMenuItem.setEnabled(false);
    }

    public void enableFullCompilation()
    {
        fullTranslationButton.setEnabled(true);
        fullTranslationMenuItem.setEnabled(true);
    }

    public void disableLoadSource()
    {
        loadButton.setEnabled(false);
        loadSourceMenuItem.setEnabled(false);
    }

    public void enableLoadSource()
    {
        loadButton.setEnabled(true);
        loadSourceMenuItem.setEnabled(true);
    }

    public void enableSourceRowSelection()
    {
        source.enableUserInput();
    }

    public void disableSourceRowSelection()
    {
        source.disableUserInput();
    }

    public void setSourceName(String s)
    {
        sourceFileChooser.setName(s);
        sourceFileChooser.setSelectedFile(new File(s));
    }

    public void setDestinationName(String s)
    {
        destFileChooser.setName(s);
        destFileChooser.setSelectedFile(new File(s));
    }

    public TextFileGUI getSource()
    {
        return source;
    }

    public TextFileGUI getDestination()
    {
        return destination;
    }

    public void setUsageFileName(String s)
    {
        usageWindow = new HTMLViewFrame(s);
        usageWindow.setSize(450, 430);
    }

    public void setAboutFileName(String s)
    {
        aboutWindow = new HTMLViewFrame(s);
        aboutWindow.setSize(450, 420);
    }

    public void displayMessage(String s, boolean flag)
    {
        if(flag)
            messageLbl.setForeground(Color.red);
        else
            messageLbl.setForeground(UIManager.getColor("Label.foreground"));
        messageLbl.setText(s);
    }

    private void loadSource()
    {
        int i = sourceFileChooser.showDialog(this, "Load Source File");
        if(i == 0)
            notifyHackTranslatorListeners((byte)7, sourceFileChooser.getSelectedFile().getAbsolutePath());
    }

    private void saveDest()
    {
        int i = destFileChooser.showDialog(this, "Save Destination File");
        if(i == 0)
        {
            if(destFileChooser.getSelectedFile().exists())
            {
                Object aobj[] = {
                    "Yes", "No", "Cancel"
                };
                int j = JOptionPane.showOptionDialog(this, "File exists. Replace it ?", "Question", 1, 3, null, aobj, aobj[2]);
                if(j != 0)
                    return;
            }
            String s = destFileChooser.getSelectedFile().getAbsolutePath();
            notifyHackTranslatorListeners((byte)6, s);
        }
    }

    protected void arrangeToolBar()
    {
        toolBar.setSize(new Dimension(1016, 55));
        toolBar.add(loadButton);
        toolBar.add(saveButton);
        toolBar.addSeparator(separatorDimension);
        toolBar.add(singleStepButton);
        toolBar.add(ffwdButton);
        toolBar.add(stopButton);
        toolBar.add(rewindButton);
        toolBar.addSeparator(separatorDimension);
        toolBar.add(fullTranslationButton);
    }

    protected void arrangeMenu()
    {
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(70);
        menuBar.add(fileMenu);
        runMenu = new JMenu("Run");
        runMenu.setMnemonic(82);
        menuBar.add(runMenu);
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(72);
        menuBar.add(helpMenu);
        loadSourceMenuItem = new JMenuItem("Load Source file", 79);
        loadSourceMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                loadSourceMenuItem_actionPerformed(actionevent);
            }

        }
);
        fileMenu.add(loadSourceMenuItem);
        saveDestMenuItem = new JMenuItem("Save Destination file", 83);
        saveDestMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                saveDestMenuItem_actionPerformed(actionevent);
            }

        }
);
        fileMenu.add(saveDestMenuItem);
        fileMenu.addSeparator();
        exitMenuItem = new JMenuItem("Exit", 88);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 8));
        exitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                exitMenuItem_actionPerformed(actionevent);
            }

        }
);
        fileMenu.add(exitMenuItem);
        singleStepMenuItem = new JMenuItem("Single Step", 83);
        singleStepMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                singleStepMenuItem_actionPerformed(actionevent);
            }

        }
);
        runMenu.add(singleStepMenuItem);
        ffwdMenuItem = new JMenuItem("Fast Forward", 70);
        ffwdMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                ffwdMenuItem_actionPerformed(actionevent);
            }

        }
);
        runMenu.add(ffwdMenuItem);
        stopMenuItem = new JMenuItem("Stop", 84);
        stopMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                stopMenuItem_actionPerformed(actionevent);
            }

        }
);
        runMenu.add(stopMenuItem);
        rewindMenuItem = new JMenuItem("Rewind", 82);
        rewindMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                rewindMenuItem_actionPerformed(actionevent);
            }

        }
);
        runMenu.add(rewindMenuItem);
        runMenu.addSeparator();
        fullTranslationMenuItem = new JMenuItem("Fast Translation", 85);
        fullTranslationMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                fullTranslationMenuItem_actionPerformed(actionevent);
            }

        }
);
        runMenu.add(fullTranslationMenuItem);
        usageMenuItem = new JMenuItem("Usage", 85);
        usageMenuItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        usageMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                usageMenuItem_actionPerformed(actionevent);
            }

        }
);
        helpMenu.add(usageMenuItem);
        aboutMenuItem = new JMenuItem("About...", 65);
        aboutMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                aboutMenuItem_actionPerformed(actionevent);
            }

        }
);
        helpMenu.add(aboutMenuItem);
    }

    protected void init()
    {
        toolBar = new JToolBar();
        menuBar = new JMenuBar();
        arrowLabel = new JLabel();
        messageLbl = new JLabel();
        listeners = new Vector();
        ffwdButton = new MouseOverJButton();
        rewindButton = new MouseOverJButton();
        stopButton = new MouseOverJButton();
        singleStepButton = new MouseOverJButton();
        fullTranslationButton = new MouseOverJButton();
        saveButton = new MouseOverJButton();
        loadButton = new MouseOverJButton();
        source = new TextFileComponent();
        destination = new TextFileComponent();
    }

    protected void jbInit()
    {
        getContentPane().setLayout(null);
        loadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                loadButton_actionPerformed(actionevent);
            }

        }
);
        loadButton.setMaximumSize(new Dimension(39, 39));
        loadButton.setMinimumSize(new Dimension(39, 39));
        loadButton.setPreferredSize(new Dimension(39, 39));
        loadButton.setSize(new Dimension(39, 39));
        loadButton.setToolTipText("Load Source File");
        loadButton.setIcon(loadIcon);
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                saveButton_actionPerformed(actionevent);
            }

        }
);
        saveButton.setMaximumSize(new Dimension(39, 39));
        saveButton.setMinimumSize(new Dimension(39, 39));
        saveButton.setPreferredSize(new Dimension(39, 39));
        saveButton.setSize(new Dimension(39, 39));
        saveButton.setToolTipText("Save Destination File");
        saveButton.setIcon(saveIcon);
        singleStepButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                singleStepButton_actionPerformed(actionevent);
            }

        }
);
        singleStepButton.setMaximumSize(new Dimension(39, 39));
        singleStepButton.setMinimumSize(new Dimension(39, 39));
        singleStepButton.setPreferredSize(new Dimension(39, 39));
        singleStepButton.setSize(new Dimension(39, 39));
        singleStepButton.setToolTipText("Single Step");
        singleStepButton.setIcon(singleStepIcon);
        ffwdButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                ffwdButton_actionPerformed(actionevent);
            }

        }
);
        ffwdButton.setMaximumSize(new Dimension(39, 39));
        ffwdButton.setMinimumSize(new Dimension(39, 39));
        ffwdButton.setPreferredSize(new Dimension(39, 39));
        ffwdButton.setSize(new Dimension(39, 39));
        ffwdButton.setToolTipText("Fast Forward");
        ffwdButton.setIcon(ffwdIcon);
        rewindButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                rewindButton_actionPerformed(actionevent);
            }

        }
);
        rewindButton.setMaximumSize(new Dimension(39, 39));
        rewindButton.setMinimumSize(new Dimension(39, 39));
        rewindButton.setPreferredSize(new Dimension(39, 39));
        rewindButton.setSize(new Dimension(39, 39));
        rewindButton.setToolTipText("Rewind");
        rewindButton.setIcon(rewindIcon);
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                stopButton_actionPerformed(actionevent);
            }

        }
);
        stopButton.setMaximumSize(new Dimension(39, 39));
        stopButton.setMinimumSize(new Dimension(39, 39));
        stopButton.setPreferredSize(new Dimension(39, 39));
        stopButton.setSize(new Dimension(39, 39));
        stopButton.setToolTipText("Stop");
        stopButton.setIcon(stopIcon);
        fullTranslationButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                fullTranslationButton_actionPerformed(actionevent);
            }

        }
);
        fullTranslationButton.setMaximumSize(new Dimension(39, 39));
        fullTranslationButton.setMinimumSize(new Dimension(39, 39));
        fullTranslationButton.setPreferredSize(new Dimension(39, 39));
        fullTranslationButton.setSize(new Dimension(39, 39));
        fullTranslationButton.setToolTipText("Fast Translation");
        fullTranslationButton.setIcon(fullTranslationIcon);
        messageLbl.setFont(Utilities.statusLineFont);
        messageLbl.setBorder(BorderFactory.createLoweredBevelBorder());
        messageLbl.setBounds(new Rectangle(0, 672, 1016, 20));
        getContentPane().add(messageLbl, null);
        arrowLabel.setBounds(new Rectangle(290, 324, 88, 71));
        arrowLabel.setIcon(arrowIcon);
        source.setVisibleRows(31);
        destination.setVisibleRows(31);
        source.setBounds(new Rectangle(35, 100, source.getWidth(), source.getHeight()));
        destination.setBounds(new Rectangle(375, 100, destination.getWidth(), destination.getHeight()));
        getContentPane().add(source, null);
        getContentPane().add(destination, null);
        toolBar.setFloatable(false);
        toolBar.setLocation(0, 0);
        toolBar.setLayout(new FlowLayout(0, 3, 0));
        toolBar.setBorder(BorderFactory.createEtchedBorder());
        arrangeToolBar();
        getContentPane().add(toolBar, null);
        toolBar.revalidate();
        toolBar.repaint();
        repaint();
        arrangeMenu();
        setJMenuBar(menuBar);
        setDefaultCloseOperation(3);
        setSize(new Dimension(1024, 741));
        setVisible(true);
        getContentPane().add(arrowLabel, null);
    }

    public void singleStepButton_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)1, null);
    }

    public void ffwdButton_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)2, null);
    }

    public void stopButton_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)3, null);
    }

    public void rewindButton_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)4, null);
    }

    public void fullTranslationButton_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)5, null);
    }

    public void loadButton_actionPerformed(ActionEvent actionevent)
    {
        loadSource();
    }

    public void saveButton_actionPerformed(ActionEvent actionevent)
    {
        saveDest();
    }

    public void loadSourceMenuItem_actionPerformed(ActionEvent actionevent)
    {
        loadSource();
    }

    public void saveDestMenuItem_actionPerformed(ActionEvent actionevent)
    {
        saveDest();
    }

    public void exitMenuItem_actionPerformed(ActionEvent actionevent)
    {
        System.exit(0);
    }

    public void singleStepMenuItem_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)1, null);
    }

    public void ffwdMenuItem_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)2, null);
    }

    public void stopMenuItem_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)3, null);
    }

    public void rewindMenuItem_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)4, null);
    }

    public void fullTranslationMenuItem_actionPerformed(ActionEvent actionevent)
    {
        notifyHackTranslatorListeners((byte)5, null);
    }

    public void usageMenuItem_actionPerformed(ActionEvent actionevent)
    {
        if(usageWindow != null)
            usageWindow.setVisible(true);
    }

    public void aboutMenuItem_actionPerformed(ActionEvent actionevent)
    {
        if(aboutWindow != null)
            aboutWindow.setVisible(true);
    }

    protected static final int TOOLBAR_WIDTH = 1016;
    protected static final int TOOLBAR_HEIGHT = 55;
    private static final int TRANSLATOR_WIDTH = 1024;
    private static final int TRANSLATOR_HEIGHT = 741;
    protected static final Dimension separatorDimension = new Dimension(3, 50);
    private Vector listeners;
    private MouseOverJButton loadButton;
    private MouseOverJButton saveButton;
    private MouseOverJButton ffwdButton;
    private MouseOverJButton stopButton;
    private MouseOverJButton singleStepButton;
    private MouseOverJButton rewindButton;
    private MouseOverJButton fullTranslationButton;
    private ImageIcon ffwdIcon;
    private ImageIcon stopIcon;
    private ImageIcon singleStepIcon;
    private ImageIcon rewindIcon;
    private ImageIcon fullTranslationIcon;
    private ImageIcon loadIcon;
    private ImageIcon saveIcon;
    private ImageIcon arrowIcon;
    protected JToolBar toolBar;
    protected JMenuBar menuBar;
    protected JMenu fileMenu;
    protected JMenu runMenu;
    protected JMenu helpMenu;
    protected JMenuItem loadSourceMenuItem;
    protected JMenuItem saveDestMenuItem;
    protected JMenuItem exitMenuItem;
    protected JMenuItem singleStepMenuItem;
    protected JMenuItem ffwdMenuItem;
    protected JMenuItem stopMenuItem;
    protected JMenuItem rewindMenuItem;
    protected JMenuItem fullTranslationMenuItem;
    protected JMenuItem aboutMenuItem;
    protected JMenuItem usageMenuItem;
    protected JFileChooser sourceFileChooser;
    protected JFileChooser destFileChooser;
    private JLabel arrowLabel;
    private JLabel messageLbl;
    private TextFileComponent source;
    protected TextFileComponent destination;
    protected FileFilter sourceFilter;
    protected FileFilter destFilter;
    private HTMLViewFrame usageWindow;
    private HTMLViewFrame aboutWindow;

}
