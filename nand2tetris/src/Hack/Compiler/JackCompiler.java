// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 22/04/2015 07:45:52 a.m.
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package Hack.Compiler;

import Hack.Utilities.HackFileFilter;

import java.io.*;

// Referenced classes of package Hack.Compiler:
//            CompilationEngine, JackTokenizer, VMWriter

public class JackCompiler
{
    public static String InstallDir = new File("").getAbsolutePath() + "\\src\\InstallDir\\"; 

    public JackCompiler()
    {
        compilationEngine = new CompilationEngine();
    }

    public boolean compileFile(File file)
    {
        try {
	        String s;
	        String s1;
	        s = file.getName().substring(0, file.getName().indexOf('.'));
	        s1 = file.getParent();
	        JackTokenizer jacktokenizer;
	        File file1;
	        VMWriter vmwriter;
			jacktokenizer = new JackTokenizer(new FileReader(file.getPath()));
	        file1 = new File(s1 + "/" + s + ".vm");
			vmwriter = new VMWriter(new PrintWriter(new FileWriter(file1)));
	        if(compilationEngine.compileClass(jacktokenizer, vmwriter, s, file.getName()))
	            return true;
	        file1.delete();
	        return false;
		} catch (IOException ioexception) {
			// TODO Auto-generated catch block
	        System.err.println("Error reading/writing while compiling " + file);
	        System.exit(-1);
	        ioexception.printStackTrace();
	        return false;
		}
    }

    public boolean compileDirectory(String s)
    {
        boolean flag = true;
        File file = new File(s);
        File afile[] = file.listFiles(new HackFileFilter(".jack"));
        for(int i = 0; i < afile.length; i++)
            flag &= compileFile(afile[i]);

        return flag;
    }

    public boolean verify()
    {
        return compilationEngine.verifySubroutineCalls();
    }

    public static void main(String args[])
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
            System.exit(-1);
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
        System.exit(flag ? 0 : 1);
    }

    private CompilationEngine compilationEngine;
}