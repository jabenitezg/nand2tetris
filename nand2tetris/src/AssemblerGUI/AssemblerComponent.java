// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 22/04/2015 07:45:51 a.m.
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package AssemblerGUI;

import Hack.Assembler.HackAssemblerGUI;
import Hack.ComputerParts.TextFileGUI;
import HackGUI.MouseOverJButton;
import HackGUI.TextFileComponent;
import TranslatorsGUI.TranslatorComponent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

// Referenced classes of package AssemblerGUI:
//            ASMFileFilter, HACKFileFilter

public class AssemblerComponent extends TranslatorComponent
    implements HackAssemblerGUI
{
    public static String InstallDir = new File("").getAbsolutePath() + "\\src\\InstallDir\\"; 
	
    public AssemblerComponent()
    {
        super(new ASMFileFilter(), new HACKFileFilter());
        comparison.disableUserInput();
        comparison.setName("Comparison");
        compareFileChooser = new JFileChooser();
        compareFileChooser.setFileFilter(destFilter);
    }

    public void setWorkingDir(File file)
    {
        super.setWorkingDir(file);
        compareFileChooser.setCurrentDirectory(file);
    }

    public void disableLoadComparison()
    {
        compareButton.setEnabled(false);
        loadCompareMenuItem.setEnabled(false);
    }

    public void enableLoadComparison()
    {
        compareButton.setEnabled(true);
        loadCompareMenuItem.setEnabled(true);
    }

    public void setComparisonName(String s)
    {
        compareFileChooser.setName(s);
        compareFileChooser.setSelectedFile(new File(s));
    }

    public void showComparison()
    {
        comparison.setVisible(true);
        equalLabel.setVisible(true);
    }

    public void hideComparison()
    {
        comparison.setVisible(false);
        equalLabel.setVisible(false);
    }

    public TextFileGUI getComparison()
    {
        return comparison;
    }

    protected void arrangeMenu()
    {
        super.arrangeMenu();
        fileMenu.removeAll();
        fileMenu.add(loadSourceMenuItem);
        loadCompareMenuItem = new JMenuItem("Load Comparison File", 67);
        loadCompareMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                loadCompareMenuItem_actionPerformed(actionevent);
            }

        }
);
        fileMenu.add(saveDestMenuItem);
        fileMenu.add(loadCompareMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
    }

    protected void init()
    {
        super.init();
        equalIcon = new ImageIcon(InstallDir + "bin/images/equal.gif");
        equalLabel = new JLabel();
        comparison = new TextFileComponent();
        compareButton = new MouseOverJButton();
    }

    private void loadComparison()
    {
        int i = compareFileChooser.showDialog(this, "Load Comparison File");
        if(i == 0)
            notifyHackTranslatorListeners((byte)9, compareFileChooser.getSelectedFile().getAbsolutePath());
    }

    protected void arrangeToolBar()
    {
        super.arrangeToolBar();
        toolBar.addSeparator(separatorDimension);
        toolBar.add(compareButton);
    }

    protected void jbInit()
    {
        super.jbInit();
        equalLabel.setBounds(new Rectangle(632, 324, 88, 71));
        equalLabel.setIcon(equalIcon);
        equalLabel.setVisible(false);
        comparison.setVisibleRows(31);
        comparison.setVisible(false);
        comparison.setBounds(new Rectangle(725, 100, comparison.getWidth(), comparison.getHeight()));
        compareButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
                loadCompareButton_actionPerformed(actionevent);
            }

        }
);
        compareButton.setMaximumSize(new Dimension(39, 39));
        compareButton.setMinimumSize(new Dimension(39, 39));
        compareButton.setPreferredSize(new Dimension(39, 39));
        compareButton.setSize(new Dimension(39, 39));
        compareButton.setToolTipText("Load Comparison File");
        compareButton.setIcon(new ImageIcon(InstallDir + "bin/images/smallequal.gif"));
        getContentPane().add(equalLabel, null);
        getContentPane().add(comparison, null);
    }

    public void loadCompareMenuItem_actionPerformed(ActionEvent actionevent)
    {
        loadComparison();
    }

    public void loadCompareButton_actionPerformed(ActionEvent actionevent)
    {
        loadComparison();
    }

    private ImageIcon equalIcon;
    private MouseOverJButton compareButton;
    private JMenuItem loadCompareMenuItem;
    private JLabel equalLabel;
    private TextFileComponent comparison;
    protected JFileChooser compareFileChooser;
}