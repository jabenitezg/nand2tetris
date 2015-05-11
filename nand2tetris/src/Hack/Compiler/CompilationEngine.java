// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 22/04/2015 07:45:52 a.m.
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package Hack.Compiler;

import java.io.PrintStream;
import java.util.*;

// Referenced classes of package Hack.Compiler:
//            SymbolTable, JackException, JackTokenizer, VMWriter

public class CompilationEngine
{

    CompilationEngine()
    {
        classes = new HashSet();
        subroutines = new HashMap();
        subroutineCalls = new Vector();
    }

    public boolean compileClass(JackTokenizer jacktokenizer, VMWriter vmwriter, String s, String s1)
    {
        input = jacktokenizer;
        output = vmwriter;
        fileName = s1;
        expTypes = new int[100];
        expIndex = -1;
        validJack = true;
        jacktokenizer.advance();
        String s2 = null;
        try
        {
            if(isKeywordClass())
                jacktokenizer.advance();
            else
                recoverableError("Expected 'class'", -1, "", s1);
            if(isIdentifier())
            {
                s2 = jacktokenizer.getIdentifier();
                if(!s2.equals(s))
                    recoverableError("The class name doesn't match the file name", -1, "", s1);
                jacktokenizer.advance();
            } else
            {
                recoverableError("Expected a class name", -1, "", s1);
                s2 = s;
            }
            identifiers = new SymbolTable(s2);
            if(isSymbol('{'))
                jacktokenizer.advance();
            else
                recoverableError("Expected {", -1, "", s1);
            compileFieldAndStaticDeclarations();
            compileAllSubroutines();
            if(!isSymbol('}'))
                recoverableError("Expected }", -1, "", s1);
            if(jacktokenizer.hasMoreTokens())
                recoverableError("Expected end-of-file", -1, "", s1);
        }
        catch(JackException jackexception) { }
        finally
        {
            vmwriter.close();
            if(validJack)
                classes.add(s2);
            return validJack;
        }
        //do
        //    ;
        //while(true);
    }

    boolean verifySubroutineCalls()
    {
        validJack = true;
        Iterator iterator = subroutineCalls.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Object aobj[] = (Object[])iterator.next();
            String s = (String)aobj[0];
            boolean flag = ((Boolean)aobj[1]).booleanValue();
            short word0 = ((Short)aobj[2]).shortValue();
            String s1 = (String)aobj[3];
            String s2 = (String)aobj[4];
            int i = ((Integer)aobj[5]).intValue();
            if(classes.contains(s.substring(0, s.indexOf("."))))
                if(!subroutines.containsKey(s))
                {
                    recoverableError((flag ? "Method " : "Function or constructor ") + s + " doesn't exist", i, s2, s1);
                } else
                {
                    Object aobj1[] = (Object[])subroutines.get(s);
                    int j = ((Integer)aobj1[0]).intValue();
                    short word1 = ((Short)aobj1[1]).shortValue();
                    if(flag && j != 1)
                        recoverableError((j != 2 ? "Constructor " : "Function ") + s + " called as a method", i, s2, s1);
                    else
                    if(!flag && j == 1)
                        recoverableError("Method " + s + " called as a function/constructor", i, s2, s1);
                    if(word0 != word1)
                        recoverableError("Subroutine " + s + " (declared to accept " + word1 + " parameter(s)) called with " + word0 + " parameter(s)", i, s2, s1);
                }
        } while(true);
        return validJack;
    }

    private void compileAllSubroutines()
        throws JackException
    {
        while(isKeywordMethod() || isKeywordFunction() || isKeywordConstructor()) 
            try
            {
                if(isKeywordMethod())
                    compileMethod();
                else
                if(isKeywordFunction())
                    compileFunction();
                else
                    compileConstructor();
            }
            catch(JackException jackexception)
            {
                while(!isKeywordMethod() && !isKeywordFunction() && !isKeywordConstructor() && input.hasMoreTokens()) 
                    input.advance();
            }
    }

    private void compileMethod()
        throws JackException
    {
        SymbolTable _tmp = identifiers;
        compileSubroutine(1);
    }

    private void compileFunction()
        throws JackException
    {
        SymbolTable _tmp = identifiers;
        compileSubroutine(2);
    }

    private void compileConstructor()
        throws JackException
    {
        SymbolTable _tmp = identifiers;
        compileSubroutine(3);
    }

    private void compileSubroutine(int i)
        throws JackException
    {
        subroutineType = i;
        ifCounter = 0;
        whileCounter = 0;
        input.advance();
        String s = null;
        if(isKeywordVoid())
            s = "void";
        else
            s = getType();
        int j = input.getLineNumber();
        input.advance();
        String s1 = null;
        String s2;
        if(isIdentifier())
        {
            s1 = input.getIdentifier();
            s2 = identifiers.getClassName() + "." + s1;
            if(subroutines.containsKey(s2))
                recoverableError("Subroutine " + s1 + " redeclared", -1, "", fileName);
            input.advance();
        } else
        {
            recoverableError("Expected a type followed by a subroutine name", -1, "", fileName);
            s2 = identifiers.getClassName() + "." + "unknownname";
        }
        switch(i)
        {
        case 1: // '\001'
            identifiers.startMethod(s1, s);
            break;

        case 2: // '\002'
            identifiers.startFunction(s1, s);
            break;

        case 3: // '\003'
            identifiers.startConstructor(s1);
            if(!s.equals(identifiers.getClassName()))
                recoverableError("The return type of a constructor must be of the class type", j);
            break;
        }
        if(isSymbol('('))
            input.advance();
        else
            recoverableError("Expected (");
        short word0 = compileParametersList();
        input.advance();
        compileSubroutineBody(s2);
        identifiers.endSubroutine();
        if(s1 != null)
            subroutines.put(s2, ((Object) (new Object[] {
                new Integer(i), new Short(word0)
            })));
    }

    private void compileSubroutineBody(String s)
        throws JackException
    {
        if(isSymbol('{'))
            input.advance();
        else
            recoverableError("Expected {");
        compileLocalsDeclarations();
        SymbolTable _tmp = identifiers;
        short word0 = identifiers.getNumberOfIdentifiers(3);
        output.function(s, word0);
        SymbolTable _tmp1 = identifiers;
        if(subroutineType == 1)
        {
            output.push("argument", (short)0);
            output.pop("pointer", (short)0);
        } else
        {
            SymbolTable _tmp2 = identifiers;
            if(subroutineType == 3)
            {
                SymbolTable _tmp3 = identifiers;
                short word1 = identifiers.getNumberOfIdentifiers(1);
                output.push("constant", word1);
                output.callFunction("Memory.alloc", (short)1);
                output.pop("pointer", (short)0);
            }
        }
        if(compileStatements(true))
            recoverableError("Program flow may reach end of subroutine without 'return'");
        input.advance();
    }

    private short compileParametersList()
        throws JackException
    {
        short word0 = 0;
        if(isSymbol(')'))
            return word0;
        for(boolean flag = true; flag;)
        {
            word0++;
            String s = getType();
            input.advance();
            String s1 = null;
            if(isIdentifier())
            {
                s1 = input.getIdentifier();
                input.advance();
            } else
            {
                recoverableError("Expected a type followed by a variable name");
            }
            SymbolTable _tmp = identifiers;
            identifiers.define(s1, s, 2);
            if(isSymbol(')'))
                flag = false;
            else
            if(isSymbol(','))
                input.advance();
            else
                terminalError("Expected ) or , in parameters list");
        }

        return word0;
    }

    private void compileFieldAndStaticDeclarations()
        throws JackException
    {
        for(boolean flag = true; flag;)
            if(isKeywordField())
            {
                SymbolTable _tmp = identifiers;
                compileDeclarationLine(1);
            } else
            if(isKeywordStatic())
            {
                SymbolTable _tmp1 = identifiers;
                compileDeclarationLine(0);
            } else
            {
                flag = false;
            }

    }

    private void compileLocalsDeclarations()
        throws JackException
    {
        for(; isKeywordLocal(); compileDeclarationLine(3))
        {
            SymbolTable _tmp = identifiers;
        }

    }

    private void compileDeclarationLine(int i)
        throws JackException
    {
        input.advance();
        String s = getType();
        boolean flag = true;
        do
        {
            if(!flag)
                break;
            input.advance();
            if(isIdentifier())
            {
                identifiers.define(input.getIdentifier(), s, i);
                input.advance();
            } else
            {
                recoverableError("Expected a type followed by comma-seperated variable names");
            }
            if(isSymbol(';'))
            {
                input.advance();
                flag = false;
            } else
            if(!isSymbol(','))
                terminalError("Expected , or ;");
        } while(true);
    }

    private void skipToEndOfStatement()
    {
        for(; !isSymbol(';') && !isSymbol('}') && input.hasMoreTokens(); input.advance());
        if(isSymbol(';') && input.hasMoreTokens())
            input.advance();
    }

    private boolean compileStatements(boolean flag)
        throws JackException
    {
        boolean flag1 = !flag;
        while(!isSymbol('}')) 
        {
            if(!flag && !flag1)
            {
                warning("Unreachable code");
                flag1 = true;
            }
            if(isKeywordDo())
                try
                {
                    compileDo();
                }
                catch(JackException jackexception)
                {
                    skipToEndOfStatement();
                }
            else
            if(isKeywordLet())
                try
                {
                    compileLet();
                }
                catch(JackException jackexception1)
                {
                    skipToEndOfStatement();
                }
            else
            if(isKeywordWhile())
                flag = compileWhile(flag);
            else
            if(isKeywordReturn())
            {
                try
                {
                    compileReturn();
                }
                catch(JackException jackexception2)
                {
                    skipToEndOfStatement();
                }
                flag = false;
            } else
            if(isKeywordIf())
                flag = compileIf(flag);
            else
            if(isSymbol(';'))
            {
                recoverableError("An empty statement is not allowed");
                input.advance();
            } else
            {
                String s = "Expected statement(do, let, while, return or if)";
                if(isIdentifier())
                {
                    recoverableError(s);
                    skipToEndOfStatement();
                } else
                {
                    terminalError(s);
                }
            }
        }
        return flag;
    }

    private void compileDo()
        throws JackException
    {
        input.advance();
        compileSubroutineCall();
        output.pop("temp", (short)0);
        if(isSymbol(';'))
            input.advance();
        else
            recoverableError("Expected ;");
    }

    private void compileSubroutineCall()
        throws JackException
    {
        if(!isIdentifier())
            terminalError("Expected class name, subroutine name, field, parameter or local or static variable name");
        String s = input.getIdentifier();
        int i = input.getLineNumber();
        input.advance();
        if(isSymbol('.'))
            compileExternalSubroutineCall(s);
        else
            compileInternalSubroutineCall(s, i);
    }

    private void compileExternalSubroutineCall(String s)
        throws JackException
    {
        String s1 = null;
        String s2 = null;
        boolean flag = true;
        input.advance();
        int i = input.getLineNumber();
        try
        {
            s1 = identifiers.getTypeOf(s);
            int j = identifiers.getKindOf(s);
            short word1 = identifiers.getIndexOf(s);
            pushVariable(j, word1);
        }
        catch(JackException jackexception)
        {
            s1 = s;
            flag = false;
        }
        if(isIdentifier())
        {
            s2 = s1 + "." + input.getIdentifier();
            input.advance();
        } else
        {
            terminalError("Expected subroutine name");
        }
        short word0 = compileExpressionList();
        output.callFunction(s2, (short)(word0 + (flag ? 1 : 0)));
        Object aobj[] = {
            s2, new Boolean(flag), new Short(word0), fileName, identifiers.getSubroutineName(), new Integer(i)
        };
        subroutineCalls.addElement(((Object) (aobj)));
    }

    private void compileInternalSubroutineCall(String s, int i)
        throws JackException
    {
        String s1 = null;
        s1 = identifiers.getClassName() + "." + s;
        output.push("pointer", (short)0);
        short word0 = compileExpressionList();
        output.callFunction(s1, (short)(word0 + 1));
        if(subroutineType == 2)
        {
            recoverableError("Subroutine " + s1 + " called as a method from within a function", i);
        } else
        {
            Object aobj[] = {
                s1, Boolean.TRUE, new Short(word0), fileName, identifiers.getSubroutineName(), new Integer(i)
            };
            subroutineCalls.addElement(((Object) (aobj)));
        }
    }

    private void skipFromParensToBlockStart()
    {
        for(; !isSymbol(';') && !isSymbol('}') && !isSymbol('{') && input.hasMoreTokens(); input.advance());
    }

    private boolean compileWhile(boolean flag)
        throws JackException
    {
        int i = whileCounter;
        whileCounter++;
        input.advance();
        if(isSymbol('('))
            input.advance();
        else
            recoverableError("Expected (");
        try
        {
            output.label("WHILE_EXP" + i);
            compileNewExpression(1);
            output.not();
            if(isSymbol(')'))
                input.advance();
            else
                recoverableError("Expected )");
        }
        catch(JackException jackexception)
        {
            skipFromParensToBlockStart();
            if(!isSymbol('{'))
                throw jackexception;
        }
        if(isSymbol('{'))
            input.advance();
        else
            recoverableError("Expected {");
        output.ifGoTo("WHILE_END" + i);
        flag = compileStatements(flag);
        if(isSymbol('}'))
            input.advance();
        else
            recoverableError("Expected }");
        output.goTo("WHILE_EXP" + i);
        output.label("WHILE_END" + i);
        return flag;
    }

    private boolean compileIf(boolean flag)
        throws JackException
    {
        int i = ifCounter;
        ifCounter++;
        input.advance();
        if(isSymbol('('))
            input.advance();
        else
            recoverableError("Expected (");
        try
        {
            compileNewExpression(1);
            if(isSymbol(')'))
                input.advance();
            else
                recoverableError("Expected )");
        }
        catch(JackException jackexception)
        {
            skipFromParensToBlockStart();
            if(!isSymbol('{'))
                throw jackexception;
        }
        if(isSymbol('{'))
            input.advance();
        else
            recoverableError("Expected {");
        output.ifGoTo("IF_TRUE" + i);
        output.goTo("IF_FALSE" + i);
        output.label("IF_TRUE" + i);
        boolean flag1 = compileStatements(flag);
        if(isSymbol('}'))
            input.advance();
        else
            recoverableError("Expected }");
        if(isKeywordElse())
        {
            input.advance();
        } else
        {
            output.label("IF_FALSE" + i);
            return true;
        }
        if(isSymbol('{'))
            input.advance();
        else
            recoverableError("Expected {");
        output.goTo("IF_END" + i);
        output.label("IF_FALSE" + i);
        boolean flag2 = compileStatements(flag);
        if(isSymbol('}'))
            input.advance();
        else
            recoverableError("Expected }");
        output.label("IF_END" + i);
        return flag1 || flag2;
    }

    private void compileReturn()
        throws JackException
    {
        input.advance();
        if(subroutineType == 3 && !isKeywordThis())
            recoverableError("A constructor must return 'this'");
        if(isSymbol(';'))
        {
            if(!identifiers.getReturnType().equals("void"))
                recoverableError("A non-void function must return a value");
            output.push("constant", (short)0);
            output.returnFromFunction();
            input.advance();
        } else
        {
            if(identifiers.getReturnType().equals("void"))
                recoverableError("A void function must not return a value");
            compileNewExpression(1);
            output.returnFromFunction();
            if(isSymbol(';'))
                input.advance();
            else
                recoverableError("Expected ;");
        }
    }

    private void compileLet()
        throws JackException
    {
        input.advance();
        if(!isIdentifier())
            terminalError("Expected field, parameter or local or static variable name");
        String s = input.getIdentifier();
        int i;
        short word0;
        String s1;
        try
        {
            i = identifiers.getKindOf(s);
            word0 = identifiers.getIndexOf(s);
            s1 = identifiers.getTypeOf(s);
        }
        catch(JackException jackexception)
        {
            recoverableError(s + " is not defined as a field, parameter or local or static variable");
            SymbolTable _tmp = identifiers;
            i = 0;
            word0 = 0;
            s1 = "int";
        }
        input.advance();
        if(isSymbol('='))
        {
            input.advance();
            int j = compileNewExpression(1);
            popVariable(i, word0);
            if(j != 1)
                if(s1.equals("int") && j != 3 && j != 2)
                    recoverableError("an int value is expected", input.getLineNumber() - 1);
                else
                if(s1.equals("char") && j != 4 && j != 2)
                    recoverableError("a char value is expected", input.getLineNumber() - 1);
                else
                if(s1.equals("boolean") && j != 2 && j != 3 && j != 5)
                    recoverableError("a boolean value is expected", input.getLineNumber() - 1);
        } else
        if(isSymbol('['))
        {
            input.advance();
            compileNewExpression(2);
            if(isSymbol(']'))
                input.advance();
            else
                terminalError("Expected ]");
            pushVariable(i, word0);
            output.add();
            if(isSymbol('='))
                input.advance();
            else
                terminalError("Expected =");
            compileNewExpression(1);
            output.pop("temp", (short)0);
            output.pop("pointer", (short)1);
            output.push("temp", (short)0);
            output.pop("that", (short)0);
        } else
        {
            terminalError("Expected [ or =");
        }
        if(isSymbol(';'))
            input.advance();
        else
            recoverableError("Expected ;");
    }

    private short compileExpressionList()
        throws JackException
    {
        if(isSymbol('('))
            input.advance();
        else
            terminalError("Expected (");
        short word0 = 0;
        if(isSymbol(')'))
        {
            input.advance();
        } else
        {
            for(boolean flag = true; flag;)
            {
                compileNewExpression(1);
                word0++;
                if(isSymbol(','))
                    input.advance();
                else
                if(isSymbol(')'))
                {
                    flag = false;
                    input.advance();
                } else
                {
                    terminalError("Expected , or ) in expression list");
                }
            }

        }
        return word0;
    }

    private int compileNewExpression(int i)
        throws JackException
    {
        expIndex++;
        setExpType(i);
        compileExpression();
        expIndex--;
        return expTypes[expIndex + 1];
    }

    private void compileExpression()
        throws JackException
    {
        boolean flag = false;
        compileTerm();
        do
        {
            JackTokenizer _tmp = input;
            if(input.getTokenType() == 2)
            {
                char c = input.getSymbol();
                flag = c == '+' || c == '-' || c == '*' || c == '/' || c == '&' || c == '|' || c == '>' || c == '<' || c == '=';
                if(flag)
                {
                    input.advance();
                    compileTerm();
                    switch(c)
                    {
                    case 43: // '+'
                        output.add();
                        break;

                    case 45: // '-'
                        output.substract();
                        break;

                    case 42: // '*'
                        output.callFunction("Math.multiply", (short)2);
                        break;

                    case 47: // '/'
                        output.callFunction("Math.divide", (short)2);
                        break;

                    case 38: // '&'
                        output.and();
                        break;

                    case 124: // '|'
                        output.or();
                        break;

                    case 62: // '>'
                        output.greaterThan();
                        break;

                    case 60: // '<'
                        output.lessThan();
                        break;

                    case 61: // '='
                        output.equal();
                        break;
                    }
                }
            }
        } while(flag);
    }

    private void compileTerm()
        throws JackException
    {
        switch(input.getTokenType())
        {
        case 4: // '\004'
            compileIntConst();
            break;

        case 5: // '\005'
            compileStringConst();
            break;

        case 1: // '\001'
            compileKeywordConst();
            break;

        case 3: // '\003'
            compileIdentifierTerm();
            break;

        case 2: // '\002'
        default:
            if(isSymbol('-'))
            {
                input.advance();
                compileTerm();
                output.negate();
                break;
            }
            if(isSymbol('~'))
            {
                input.advance();
                compileTerm();
                output.not();
                break;
            }
            if(isSymbol('('))
            {
                input.advance();
                compileNewExpression(1);
                if(isSymbol(')'))
                    input.advance();
                else
                    terminalError("Expected )");
            } else
            {
                terminalError("Expected - or ~ or ( in term");
            }
            break;
        }
    }

    private void compileIntConst()
        throws JackException
    {
        if(input.getIntValue() > 32767)
            recoverableError("Integer constant too big");
        short word0 = (short)input.getIntValue();
        output.push("constant", word0);
        if(getExpType() < 2)
            setExpType(2);
        else
        if(getExpType() > 4)
            recoverableError("a numeric value is illegal here");
        input.advance();
    }

    private void compileStringConst()
        throws JackException
    {
        if(getExpType() == 1)
            setExpType(6);
        else
            recoverableError("A string constant is illegal here");
        String s = input.getStringValue();
        short word0 = (short)s.length();
        output.push("constant", word0);
        output.callFunction("String.new", (short)1);
        for(short word1 = 0; word1 < word0; word1++)
        {
            output.push("constant", (short)s.charAt(word1));
            output.callFunction("String.appendChar", (short)2);
        }

        input.advance();
    }

    private void compileKeywordConst()
        throws JackException
    {
        int i = input.getKeywordType();
        switch(i)
        {
        case 18: // '\022'
            output.push("constant", (short)0);
            output.not();
            break;

        case 19: // '\023'
        case 20: // '\024'
            output.push("constant", (short)0);
            break;

        case 21: // '\025'
            SymbolTable _tmp = identifiers;
            if(subroutineType == 2)
                recoverableError("'this' can't be referenced in a function");
            output.push("pointer", (short)0);
            break;

        default:
            terminalError("Illegal keyword in term");
            break;
        }
        switch(i)
        {
        default:
            break;

        case 18: // '\022'
        case 19: // '\023'
            if(getExpType() <= 2)
                setExpType(5);
            else
                recoverableError("A boolean value is illegal here");
            break;

        case 20: // '\024'
            if(getExpType() == 1)
                setExpType(8);
            else
                recoverableError("'null' is illegal here");
            break;

        case 21: // '\025'
            if(getExpType() == 1)
                setExpType(7);
            else
                recoverableError("'this' is illegal here");
            break;
        }
        input.advance();
    }

    private void compileIdentifierTerm()
        throws JackException
    {
        if(getExpType() == 6)
            recoverableError("Illegal casting into String constant");
        String s = input.getIdentifier();
        int i = input.getLineNumber();
        input.advance();
        if(isSymbol('['))
        {
            input.advance();
            compileNewExpression(2);
            try
            {
                pushVariable(identifiers.getKindOf(s), identifiers.getIndexOf(s));
            }
            catch(JackException jackexception)
            {
                recoverableError(s + " is not defined as a field, parameter or local or static variable", i);
            }
            output.add();
            output.pop("pointer", (short)1);
            output.push("that", (short)0);
            if(isSymbol(']'))
                input.advance();
            else
                terminalError("Expected ]");
        } else
        if(isSymbol('('))
            compileInternalSubroutineCall(s, i);
        else
        if(isSymbol('.'))
            compileExternalSubroutineCall(s);
        else
            try
            {
                int j = identifiers.getKindOf(s);
                short word0 = identifiers.getIndexOf(s);
                if(subroutineType == 2 && j == 1)
                    recoverableError("A field may not be referenced in a function", i);
                pushVariable(j, word0);
                String s1 = identifiers.getTypeOf(s);
                if(s1.equals("int"))
                {
                    if(getExpType() <= 2)
                        setExpType(3);
                    else
                    if(getExpType() > 3)
                        recoverableError("An int value is illegal here", i);
                } else
                if(s1.equals("char"))
                {
                    if(getExpType() <= 2)
                        setExpType(4);
                    else
                    if(getExpType() > 4 || getExpType() == 3)
                        recoverableError("A char value is illegal here", i);
                } else
                if(s1.equals("boolean"))
                    if(getExpType() <= 2)
                        setExpType(5);
                    else
                    if(getExpType() != 5)
                        recoverableError("A boolean value is illegal here", i);
            }
            catch(JackException jackexception1)
            {
                recoverableError(s + " is not defined as a field, parameter or local or static variable", i);
            }
    }

    private int getExpType()
    {
        return expTypes[expIndex];
    }

    private int setExpType(int i)
    {
        return expTypes[expIndex] = i;
    }

    private String getType()
        throws JackException
    {
        String s = null;
        JackTokenizer _tmp = input;
        if(input.getTokenType() == 1)
            switch(input.getKeywordType())
            {
            case 5: // '\005'
                s = "int";
                break;

            case 6: // '\006'
                s = "boolean";
                break;

            case 7: // '\007'
                s = "char";
                break;

            default:
                terminalError("Expected primitive type or class name");
                break;
            }
        else
        if(isIdentifier())
            s = input.getIdentifier();
        else
            terminalError("Expected primitive type or class name");
        return s;
    }

    private void pushVariable(int i, short word0)
        throws JackException
    {
        switch(i)
        {
        case 2: // '\002'
            output.push("argument", word0);
            break;

        case 3: // '\003'
            output.push("local", word0);
            break;

        case 1: // '\001'
            output.push("this", word0);
            break;

        case 0: // '\0'
            output.push("static", word0);
            break;

        default:
            terminalError("Internal Error: Illegal kind");
            break;
        }
    }

    private void popVariable(int i, short word0)
        throws JackException
    {
        switch(i)
        {
        case 3: // '\003'
            output.pop("local", word0);
            break;

        case 2: // '\002'
            output.pop("argument", word0);
            break;

        case 1: // '\001'
            output.pop("this", word0);
            break;

        case 0: // '\0'
            output.pop("static", word0);
            break;
        }
    }

    private boolean isKeywordClass()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 1) return false; else return true;
    }

    private boolean isKeywordStatic()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 10) return false; else return true;
    }

    private boolean isKeywordField()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 11) return false; else return true;
    }

    private boolean isKeywordLocal()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 9) return false; else return true;

    }

    private boolean isIdentifier()
    {
        JackTokenizer _tmp = input;
        return input.getTokenType() == 3;
    }

    private boolean isSymbol(char c)
    {
        JackTokenizer _tmp = input;
        return input.getTokenType() == 2 && input.getSymbol() == c;
    }

    private boolean isKeywordMethod()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 2) return false; else return true;

    }

    private boolean isKeywordFunction()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 3) return false; else return true;

    }

    private boolean isKeywordConstructor()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 4) return false; else return true;

    }

    private boolean isKeywordVoid()
    {
        //input;
        if(input.getTokenType() != 1) return false; 

        //input;
        if(input.getKeywordType() != 8) return false; else return true;

    }

    private boolean isKeywordDo()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 13) return false; else return true;

    }

    private boolean isKeywordLet()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 12) return false; else return true;

       
    }

    private boolean isKeywordWhile()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 16) return false; else return true;

    }

    private boolean isKeywordReturn()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 17) return false; else return true;

    }

    private boolean isKeywordIf()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 14) return false; else return true;

    }

    private boolean isKeywordElse()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 15) return false; else return true;

    }

    private boolean isKeywordThis()
    {
        //input;
        if(input.getTokenType() != 1) return false;

        //input;
        if(input.getKeywordType() != 21) return false; else return true;

    }

    private void terminalError(String s)
        throws JackException
    {
        terminalError(s, -1, null, null);
    }

    private void terminalError(String s, int i)
        throws JackException
    {
        terminalError(s, i, null, null);
    }

    private void terminalError(String s, int i, String s1, String s2)
        throws JackException
    {
        recoverableError(s, i, s1, s2);
        throw new JackException(generateMessage(s, i, s1, s2));
    }

    private void warning(String s)
    {
        warning(s, -1, null, null);
    }

    private void warning(String s, int i)
    {
        warning(s, i, null, null);
    }

    private void warning(String s, int i, String s1, String s2)
    {
        System.err.println(generateMessage("Warning: " + s, i, s1, s2));
    }

    private void recoverableError(String s)
    {
        recoverableError(s, -1, null, null);
    }

    private void recoverableError(String s, int i)
    {
        recoverableError(s, i, null, null);
    }

    private void recoverableError(String s, int i, String s1, String s2)
    {
        System.err.println(generateMessage(s, i, s1, s2));
        validJack = false;
    }

    private String generateMessage(String s, int i, String s1, String s2)
    {
        if(s2 == null)
            s2 = fileName;
        if(s1 == null)
            s1 = identifiers.getSubroutineName();
        if(i == -1)
            i = input.getLineNumber();
        return "In " + s2 + " (line " + i + "): " + ("".equals(s1) ? "" : "In subroutine" + (s1 != null ? " " + s1 : "") + ": ") + s;
    }

    private static final int GENERAL_TYPE = 1;
    private static final int NUMERIC_TYPE = 2;
    private static final int INT_TYPE = 3;
    private static final int CHAR_TYPE = 4;
    private static final int BOOLEAN_TYPE = 5;
    private static final int STRING_TYPE = 6;
    private static final int THIS_TYPE = 7;
    private static final int NULL_TYPE = 8;
    private JackTokenizer input;
    private VMWriter output;
    private SymbolTable identifiers;
    private HashMap subroutines;
    private HashSet classes;
    private Vector subroutineCalls;
    private int ifCounter;
    private int whileCounter;
    private int subroutineType;
    private int expTypes[];
    private int expIndex;
    private String fileName;
    private boolean validJack;
}