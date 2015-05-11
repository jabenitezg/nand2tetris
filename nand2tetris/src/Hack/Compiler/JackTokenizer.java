// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 22/04/2015 07:45:52 a.m.
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package Hack.Compiler;

import java.io.*;
import java.util.Hashtable;

public class JackTokenizer
{

    public JackTokenizer(Reader reader)
    {
        try
        {
            parser = new StreamTokenizer(reader);
            parser.parseNumbers();
            parser.slashSlashComments(true);
            parser.slashStarComments(true);
            parser.ordinaryChar(46);
            parser.ordinaryChar(45);
            parser.ordinaryChar(60);
            parser.ordinaryChar(62);
            parser.ordinaryChar(47);
            parser.wordChars(95, 95);
            parser.nextToken();
            initKeywords();
            initSymbols();
        }
        catch(IOException ioexception)
        {
            System.out.println("JackTokenizer failed during initialization operation");
            System.exit(-1);
        }
    }

    public void advance()
    {
        try
        {
            switch(parser.ttype)
            {
            case -2: 
                tokenType = 4;
                intValue = (int)parser.nval;
                break;

            case -3: 
                String s = parser.sval;
                if(keywords.containsKey(s))
                {
                    tokenType = 1;
                    keyWordType = ((Integer)keywords.get(s)).intValue();
                } else
                {
                    tokenType = 3;
                    identifier = s;
                }
                break;

            case 34: // '"'
                tokenType = 5;
                stringValue = parser.sval;
                break;

            default:
                tokenType = 2;
                symbol = (char)parser.ttype;
                break;
            }
            lineNumber = parser.lineno();
            parser.nextToken();
        }
        catch(IOException ioexception)
        {
            System.out.println("JackTokenizer failed during advance operation");
            System.exit(-1);
        }
    }

    public int getTokenType()
    {
        return tokenType;
    }

    public int getKeywordType()
    {
        return keyWordType;
    }

    public char getSymbol()
    {
        return symbol;
    }

    public int getIntValue()
    {
        return intValue;
    }

    public String getStringValue()
    {
        return stringValue;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public boolean hasMoreTokens()
    {
        StreamTokenizer _tmp = parser;
        return parser.ttype != -1;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    private void initKeywords()
    {
        keywords = new Hashtable();
        keywords.put("class", new Integer(1));
        keywords.put("method", new Integer(2));
        keywords.put("function", new Integer(3));
        keywords.put("constructor", new Integer(4));
        keywords.put("int", new Integer(5));
        keywords.put("boolean", new Integer(6));
        keywords.put("char", new Integer(7));
        keywords.put("void", new Integer(8));
        keywords.put("var", new Integer(9));
        keywords.put("static", new Integer(10));
        keywords.put("field", new Integer(11));
        keywords.put("let", new Integer(12));
        keywords.put("do", new Integer(13));
        keywords.put("if", new Integer(14));
        keywords.put("else", new Integer(15));
        keywords.put("while", new Integer(16));
        keywords.put("return", new Integer(17));
        keywords.put("true", new Integer(18));
        keywords.put("false", new Integer(19));
        keywords.put("null", new Integer(20));
        keywords.put("this", new Integer(21));
    }

    private void initSymbols()
    {
        symbols = new Hashtable();
        symbols.put("(", "(");
        symbols.put(")", ")");
        symbols.put("[", "[");
        symbols.put("]", "]");
        symbols.put("{", "{");
        symbols.put("}", "}");
        symbols.put(",", ",");
        symbols.put(";", ";");
        symbols.put("=", "=");
        symbols.put(".", ".");
        symbols.put("+", "+");
        symbols.put("-", "-");
        symbols.put("*", "*");
        symbols.put("/", "/");
        symbols.put("&", "&");
        symbols.put("|", "|");
        symbols.put("~", "~");
        symbols.put("<", "<");
        symbols.put(">", ">");
    }

    public static final int TYPE_KEYWORD = 1;
    public static final int TYPE_SYMBOL = 2;
    public static final int TYPE_IDENTIFIER = 3;
    public static final int TYPE_INT_CONST = 4;
    public static final int TYPE_STRING_CONST = 5;
    public static final int KW_CLASS = 1;
    public static final int KW_METHOD = 2;
    public static final int KW_FUNCTION = 3;
    public static final int KW_CONSTRUCTOR = 4;
    public static final int KW_INT = 5;
    public static final int KW_BOOLEAN = 6;
    public static final int KW_CHAR = 7;
    public static final int KW_VOID = 8;
    public static final int KW_VAR = 9;
    public static final int KW_STATIC = 10;
    public static final int KW_FIELD = 11;
    public static final int KW_LET = 12;
    public static final int KW_DO = 13;
    public static final int KW_IF = 14;
    public static final int KW_ELSE = 15;
    public static final int KW_WHILE = 16;
    public static final int KW_RETURN = 17;
    public static final int KW_TRUE = 18;
    public static final int KW_FALSE = 19;
    public static final int KW_NULL = 20;
    public static final int KW_THIS = 21;
    private StreamTokenizer parser;
    private Hashtable keywords;
    private Hashtable symbols;
    private int tokenType;
    private int keyWordType;
    private char symbol;
    private int intValue;
    private int lineNumber;
    private String stringValue;
    private String identifier;
}