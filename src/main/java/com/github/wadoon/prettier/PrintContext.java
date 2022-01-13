package com.github.wadoon.prettier;

/**
 * @author Alexander Weigl
 * @version 1 (1/7/22)
 */
public class PrintContext {
    public boolean spaceToNl = false;
    public int indent = 0;
    public int maxWidth;

    public PrintContext copy() {
        PrintContext c = new PrintContext();
        c.spaceToNl = spaceToNl;
        c.indent = indent;
        c.maxWidth = maxWidth;
        return c;
    }
}
