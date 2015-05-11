// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 22/04/2015 07:45:52 a.m.
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package Hack.Compiler;


public class IdentifierProperties
{

    public IdentifierProperties(String s, short word0)
    {
        type = s;
        index = word0;
    }

    public String getType()
    {
        return type;
    }

    public short getIndex()
    {
        return index;
    }

    private String type;
    private short index;
}