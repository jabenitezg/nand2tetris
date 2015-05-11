// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 22/04/2015 07:45:52 a.m.
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package Hack.Compiler;

import java.util.HashMap;

// Referenced classes of package Hack.Compiler:
//            IdentifierProperties, JackException

public class SymbolTable
{

    public SymbolTable(String s)
    {
        className = s;
        fields = new HashMap();
        statics = new HashMap();
        parameters = new HashMap();
        locals = new HashMap();
        fieldsNumbering = 0;
        staticsNumbering = 0;
        localsNumbering = 0;
        parametersNumbering = 1;
        subroutineType = 0;
    }

    public void startMethod(String s, String s1)
    {
        startSubroutine(s, 1, s1, (short)1);
    }

    public void startFunction(String s, String s1)
    {
        startSubroutine(s, 2, s1, (short)0);
    }

    public void startConstructor(String s)
    {
        startSubroutine(s, 3, className, (short)0);
    }

    public void endSubroutine()
    {
        parameters.clear();
        locals.clear();
        localsNumbering = 0;
        parametersNumbering = 1;
        subroutineType = 0;
        subroutineName = null;
        returnType = null;
    }

    private void startSubroutine(String s, int i, String s1, short word0)
    {
        endSubroutine();
        subroutineName = s;
        subroutineType = i;
        returnType = s1;
        parametersNumbering = word0;
    }

    public void define(String s, String s1, int i)
    {
        switch(i)
        {
        case 0: // '\0'
            statics.put(s, new IdentifierProperties(s1, staticsNumbering));
            staticsNumbering++;
            break;

        case 1: // '\001'
            fields.put(s, new IdentifierProperties(s1, fieldsNumbering));
            fieldsNumbering++;
            break;

        case 2: // '\002'
            parameters.put(s, new IdentifierProperties(s1, parametersNumbering));
            parametersNumbering++;
            break;

        case 3: // '\003'
            locals.put(s, new IdentifierProperties(s1, localsNumbering));
            localsNumbering++;
            break;
        }
    }

    public int getKindOf(String s)
        throws JackException
    {
        byte byte0;
        if(parameters.containsKey(s))
            byte0 = 2;
        else
        if(locals.containsKey(s))
            byte0 = 3;
        else
        if(subroutineType != 2 && fields.containsKey(s))
            byte0 = 1;
        else
        if(statics.containsKey(s))
            byte0 = 0;
        else
            throw new JackException(s);
        return byte0;
    }

    public String getTypeOf(String s)
        throws JackException
    {
        return getIdentifierProperties(s).getType();
    }

    public short getIndexOf(String s)
        throws JackException
    {
        return getIdentifierProperties(s).getIndex();
    }

    public short getNumberOfIdentifiers(int i)
    {
        short word0 = -1;
        switch(i)
        {
        case 0: // '\0'
            word0 = staticsNumbering;
            break;

        case 1: // '\001'
            word0 = fieldsNumbering;
            break;

        case 2: // '\002'
            word0 = (short)(parametersNumbering - 1);
            break;

        case 3: // '\003'
            word0 = localsNumbering;
            break;
        }
        return word0;
    }

    public String getClassName()
    {
        return className;
    }

    public String getSubroutineName()
    {
        return subroutineName;
    }

    public String getReturnType()
    {
        return returnType;
    }

    private IdentifierProperties getIdentifierProperties(String s)
        throws JackException
    {
        IdentifierProperties identifierproperties = null;
        switch(getKindOf(s))
        {
        case 2: // '\002'
            identifierproperties = (IdentifierProperties)parameters.get(s);
            break;

        case 3: // '\003'
            identifierproperties = (IdentifierProperties)locals.get(s);
            break;

        case 1: // '\001'
            identifierproperties = (IdentifierProperties)fields.get(s);
            break;

        case 0: // '\0'
            identifierproperties = (IdentifierProperties)statics.get(s);
            break;
        }
        return identifierproperties;
    }

    public static final int KIND_STATIC = 0;
    public static final int KIND_FIELD = 1;
    public static final int KIND_PARAMETER = 2;
    public static final int KIND_LOCAL = 3;
    public static final int SUBROUTINE_TYPE_UNDEFINED = 0;
    public static final int SUBROUTINE_TYPE_METHOD = 1;
    public static final int SUBROUTINE_TYPE_FUNCTION = 2;
    public static final int SUBROUTINE_TYPE_CONSTRUCTOR = 3;
    private static short staticsNumbering;
    private static short fieldsNumbering;
    private static short parametersNumbering;
    private static short localsNumbering;
    private String className;
    private String subroutineName;
    private int subroutineType;
    private String returnType;
    private HashMap fields;
    private HashMap parameters;
    private HashMap locals;
    private HashMap statics;
}