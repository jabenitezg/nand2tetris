/*
@echo off

rem  $Id: Assembler.bat,v 1.2 2014/05/10 00:52:43 marka Exp $
rem  mark.armbrust@pobox.com

setlocal
if not "%2"=="" goto :USAGE
if "%~1"=="/?" (
:USAGE
  echo Usage:
  echo     Assembler               Starts the assembler in interactive mode.
  echo     Assembler FILE[.asm]    Assembles FILE.asm to FILE.hack.
  exit -b
)
if not "%~1"=="" (
  set "_arg1=%~f1"
)
pushd "%~dp0"
if "%~1"=="" (
  start javaw -classpath "%CLASSPATH%;bin/classes;bin/lib/Hack.jar;bin/lib/HackGUI.jar;bin/lib/Compilers.jar;bin/lib/AssemblerGUI.jar;bin/lib/TranslatorsGUI.jar" ^
    HackAssemblerMain
) else (
  echo Assembling "%_arg1%"
  java -classpath "%CLASSPATH%;bin/classes;bin/lib/Hack.jar;bin/lib/HackGUI.jar;bin/lib/Compilers.jar;bin/lib/AssemblerGUI.jar;bin/lib/TranslatorsGUI.jar" ^
    HackAssemblerMain "%_arg1%"
)
popd

 
*/

// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 21/04/2015 05:03:52 p.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

import AssemblerGUI.AssemblerComponent;
import Hack.Assembler.HackAssembler;
import Hack.Assembler.HackAssemblerGUI;
import Hack.Translators.HackTranslatorException;

import java.io.File;
import java.io.PrintStream;

import javax.swing.UIManager;

public class HackAssemblerMain
{
    public static String InstallDir = new File("").getAbsolutePath() + "\\src\\InstallDir\\"; 

    public HackAssemblerMain()
    {
    }

    public static void main(String args[])
    {
        if(args.length > 1)
        {
            System.err.println("Usage: java HackAssembler [.asm name]");
            System.exit(-1);
        }
        HackAssembler hackassembler;
        if(args.length == 1)
        {
            try
            {
                hackassembler = new HackAssembler(args[0], 32768, (short)0, true);
            }
            catch(HackTranslatorException hacktranslatorexception)
            {
                System.err.println(hacktranslatorexception.getMessage());
            }
        } else
        {
            try
            {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
            catch(Exception exception) { }
            try
            {
                AssemblerComponent assemblercomponent = new AssemblerComponent();
                assemblercomponent.setAboutFileName(InstallDir + "bin/help/asmAbout.html");
                assemblercomponent.setUsageFileName(InstallDir + "bin/help/asmUsage.html");
                HackAssembler hackassembler1 = new HackAssembler(assemblercomponent, 32768, (short)0, null);
            }
            catch(HackTranslatorException hacktranslatorexception1)
            {
                System.err.println(hacktranslatorexception1.getMessage());
                System.exit(-1);
            }
        }
    }
}
