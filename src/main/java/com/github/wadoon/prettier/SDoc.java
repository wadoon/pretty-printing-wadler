package com.github.wadoon.prettier;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * @author Alexander Weigl
 * @version 1 (1/7/22)
 */
public abstract class SDoc {
    public abstract void append(Writer target) throws IOException;

    public static final class Text extends SDoc {
        private final String value;

        public Text(String value) {
            this.value = value;
        }

        @Override
        public void append(Writer target) throws IOException {
            target.append(value);
        }

        @Override
        public String toString() {
            return "Text{" +
                    "value='" + value + '\'' +
                    '}';
        }
    }

    public static final SDoc NIL = new SDoc() {
        @Override
        public void append(Writer target) {
            //noop
        }
    };

    public static final class NewLine extends SDoc {
        private final int indent;

        public NewLine(int indent) {
            this.indent = indent;
        }

        @Override
        public void append(Writer target) throws IOException {
            target.write('\n');
            for (int i = 0; i < indent; i++) {
                target.write(' ');
            }
        }

        @Override
        public String toString() {
            return "NewLine{" +
                    "indent=" + indent +
                    '}';
        }
    }

    public static final class List extends SDoc {
        final java.util.List<SDoc> elements;

        public List(java.util.List<SDoc> elements) {
            this.elements = elements;
        }

        public List(int size) {
            this(new ArrayList<>(size));
        }

        @Override
        public void append(Writer target) throws IOException {
            for (SDoc element : elements) {
                element.append(target);
            }
        }

        @Override
        public String toString() {
            return "List{" +
                    "elements=" + elements +
                    '}';
        }
    }
}
