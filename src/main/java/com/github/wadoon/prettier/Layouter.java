package com.github.wadoon.prettier;

import java.io.IOException;
import java.io.StringWriter;

import static com.github.wadoon.prettier.Doc.*;

/**
 * https://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.34.2200&rep=rep1&type=pdf
 * @author Alexander Weigl
 * @version 1 (1/7/22)
 */
public class Layouter {
    public static void main(String[] args) {
        Layouter layouter = new Layouter();
        Doc doc =
                group(
                        text("begin"),
                        nest(
                                space(),
                                group(
                                        text("stmt"),
                                        space(),
                                        text("stmt"),
                                        space(),
                                        text("stmt")
                                )
                        ),
                        space(),
                        text("end")
                );
        System.out.println(doc);
        SDoc sdoc = layouter.layout(doc, 20);
        System.out.println(sdoc);
        System.out.println("---");
        System.out.println(layouter.asString(doc, 60));
        System.out.println("---");
        System.out.println(layouter.asString(doc, 50));
        System.out.println("---");
        System.out.println(layouter.asString(doc, 40));
        System.out.println("---");
        System.out.println(layouter.asString(doc, 30));
        System.out.println("---");
        System.out.println(layouter.asString(doc, 20));
        System.out.println("---");
        System.out.println(layouter.asString(doc, 10));
        System.out.println("---");
    }

    private String asString(Doc doc, int width) {
        return print(layout(doc, width));
    }

    private String print(SDoc layout) {
        StringWriter sw = new StringWriter();
        try {
            layout.append(sw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    private SDoc layout(Doc doc, int width) {
        return doc.reduce(false, 0, 0, width);
    }
}
