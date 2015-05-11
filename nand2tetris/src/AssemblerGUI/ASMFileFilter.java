// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 22/04/2015 07:45:51 a.m.
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package AssemblerGUI;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ASMFileFilter extends FileFilter
{

    public ASMFileFilter()
    {
    }

    public boolean accept(File file)
    {
        if(file.isDirectory())
            return true;
        String s = getExtension(file);
        if(s != null)
            return s.equals("asm");
        else
            return false;
    }

    public static String getExtension(File file)
    {
        String s = null;
        String s1 = file.getName();
        int i = s1.lastIndexOf('.');
        if(i > 0 && i < s1.length() - 1)
            s = s1.substring(i + 1).toLowerCase();
        return s;
    }

    public String getDescription()
    {
        return "ASM Files";
    }
}