// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 22/04/2015 07:45:52 a.m.
// Home Page: http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 

package Hack.Compiler;

import Hack.VirtualMachine.VirtualMachine;
import java.io.PrintWriter;

public class VMWriter
    implements VirtualMachine
{

    public VMWriter(PrintWriter printwriter)
    {
        writer = printwriter;
    }

    public void close()
    {
        writer.flush();
        writer.close();
    }

    public void add()
    {
        writer.println("add");
    }

    public void substract()
    {
        writer.println("sub");
    }

    public void negate()
    {
        writer.println("neg");
    }

    public void equal()
    {
        writer.println("eq");
    }

    public void greaterThan()
    {
        writer.println("gt");
    }

    public void lessThan()
    {
        writer.println("lt");
    }

    public void and()
    {
        writer.println("and");
    }

    public void or()
    {
        writer.println("or");
    }

    public void not()
    {
        writer.println("not");
    }

    public void push(String s, short word0)
    {
        writer.println("push " + s + " " + word0);
    }

    public void pop(String s, short word0)
    {
        writer.println("pop " + s + " " + word0);
    }

    public void label(String s)
    {
        writer.println("label " + s);
    }

    public void goTo(String s)
    {
        writer.println("goto " + s);
    }

    public void ifGoTo(String s)
    {
        writer.println("if-goto " + s);
    }

    public void function(String s, short word0)
    {
        writer.println("function " + s + " " + word0);
    }

    public void returnFromFunction()
    {
        writer.println("return");
    }

    public void callFunction(String s, short word0)
    {
        writer.println("call " + s + " " + word0);
    }

    private PrintWriter writer;
}