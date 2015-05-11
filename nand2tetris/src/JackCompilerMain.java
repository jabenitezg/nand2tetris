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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Hack.Compiler.JackCompiler;
import Hack.Controller.*;
import Hack.CPUEmulator.*;
import HackGUI.*;
import SimulatorsGUI.*;

import javax.swing.*;

/**
 * The CPU Emulator.
 */
public class JackCompilerMain
{
  public static String InstallDir = new File("").getAbsolutePath() + "\\src\\InstallDir\\"; 
  /**
   * The command line CPU Emulator program.
   */
  public static void main(String[] args) {
	        String[] arg1 = new String[1];
        	if ( !compile(arg1) ) {
        		System.out.println("Compile error");
        	} else {
        		System.out.println("Compile OK");
        	}; 
  }

  public static boolean compile(String args[])
  {
      if(args.length != 1)
      {
          try
          {
              BufferedReader bufferedreader = new BufferedReader(new FileReader(new File(InstallDir + "bin/help/compiler.txt")));
              String s;
              while((s = bufferedreader.readLine()) != null) 
                  System.out.println(s);
              System.out.println("");
          }
          catch(FileNotFoundException filenotfoundexception) { }
          catch(IOException ioexception) { }
          System.out.println("Usage: java JackCompiler <Jack-dir or Jack-file-name>");
          return false;
      }
      JackCompiler jackcompiler = new JackCompiler();
      File file = new File(args[0]);
      if(!file.exists())
      {
          System.err.println("Could not find file or directory: " + args[0]);
          System.exit(-1);
      }
      boolean flag;
      if(file.isDirectory())
          flag = jackcompiler.compileDirectory(args[0]);
      else
          flag = jackcompiler.compileFile(file);
      flag &= jackcompiler.verify();
      return flag;
  }

}
