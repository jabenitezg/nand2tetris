/********************************************************************************
 * The contents of this file are subject to the GNU General Public License      *
 * (GPL) Version 2 or later (the "License"); you may not use this file except   *
 * in compliance with the License. You may obtain a copy of the License at      *
 * http://www.gnu.org/copyleft/gpl.html                                         *
 *                                                                              *
 * Software distributed under the License is distributed on an "AS IS" basis,   *
 * without warranty of any kind, either expressed or implied. See the License   *
 * for the specific language governing rights and limitations under the         *
 * License.                                                                     *
 *                                                                              *
 * This file was originally developed as part of the software suite that        *
 * supports the book "The Elements of Computing Systems" by Nisan and Schocken, *
 * MIT Press 2005. If you modify the contents of this file, please document and *
 * mark your changes clearly, for the benefit of others.                        *
 ********************************************************************************/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import Hack.Controller.*;
import Hack.CPUEmulator.*;
import HackGUI.*;
import SimulatorsGUI.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import com.sun.org.apache.bcel.internal.util.Class2HTML;

/**
 * The CPU Emulator.
 */
public class MainMenu extends JFrame
{
  public static String InstallDir = new File("").getAbsolutePath() + "\\src\\InstallDir\\";
  private static String str;
  /**
   * The command line CPU Emulator program.
   */
  public MainMenu() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            JToolBar barraHerramientas = new javax.swing.JToolBar();
            JButton cpuEmulator = new javax.swing.JButton();
            JButton hardwareSimulator = new javax.swing.JButton();
            JButton jackCompiler = new javax.swing.JButton();
            JTextField options = setTextField();
            JTextArea result = setTextArea();

            cpuEmulator = setButton("CPUEmulatorMain","CPUEmulator");
            hardwareSimulator = setButton("HardwareSimulatorMain","HardwareSimulator");
            jackCompiler = setButton("JackCompilerMain","JackCompiler");
            barraHerramientas.add(cpuEmulator);
            barraHerramientas.add(hardwareSimulator);
            barraHerramientas.add(jackCompiler);
            JToolBar barraHerramientas2 = new javax.swing.JToolBar();
            barraHerramientas.add(options);
            barraHerramientas.add(result);
            
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            
            setSize(470,210);
            setLocation(250,250);
            
            layout.setHorizontalGroup(
            		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            		.addComponent(barraHerramientas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            		.addComponent(barraHerramientas2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            		);
            
            
            layout.setVerticalGroup(
            		layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            		.addGroup(layout.createParallelGroup()
            				.addComponent(barraHerramientas, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            				.addGap(0, 238, Short.MAX_VALUE)
            				.addComponent(barraHerramientas2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            				.addGap(0, 238, Short.MAX_VALUE))
            );
           
            

        } catch (Exception e) {
        	e.printStackTrace();
        }

  }

  public static void main(String args[]) {

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable() {
          public void run() {
              new MainMenu().setVisible(true);
          }
      });
  }  

  String setFile() {
	  JFileChooser chooser = new JFileChooser();
	  int a = chooser.showDialog(this, "Load Comparison File");
	  return chooser.getSelectedFile().getName();
  }

  static JTextField setTextField() {
	  JTextField textField =  new JTextField();
	  textField.setColumns(20);
	  textField.setMaximumSize(new Dimension(89, 49));
	  return textField;
  }

  static JTextArea setTextArea() {
	  JTextArea textArea =  new JTextArea();
	  textArea.setMaximumSize(new Dimension(89, 49));
	  return textArea;
  }


  static JButton setButton(final String clase, final String text) {
	  Border txtBorder = BorderFactory.createMatteBorder(4,4,4,4,Color.orange);
	  JButton button =  new JButton(); 
	  button.setMaximumSize(new Dimension(89, 49));
	  button.setMinimumSize(new Dimension(89, 49));
	  button.setPreferredSize(new Dimension(89, 49));
	  button.setToolTipText("Run");
	  button.setIcon(null);
	  button.setText(text);
	  button.setBorder(txtBorder);
	  button.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  switch (clase) {
			  	case "CPUEmulatorMain":
			  		String[] args1 = new String[0];
			  		CPUEmulatorMain.main(args1);
			  		break;
			  	case "HardwareSimulatorMain":
			  		String[] args2 = new String[0];
			  		HardwareSimulatorMain.main(args2);
			  		break;
			  	case "JackCompilerMain":
			  		String[] args3 = new String[1];
			  	    String InstallDir = new File("").getAbsolutePath() + "\\src\\InstallDir\\"; 
			  		args3[0] = InstallDir + "jack\\ArrayTest";
			  		//str = setFile();
			  		JackCompilerMain.main(args3);
			  		break;
			  	default:
			  		break;
			} 
    	  }
	  });
	  return button; 
  } 

}
