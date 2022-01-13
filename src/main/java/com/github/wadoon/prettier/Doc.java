package com.github.wadoon.prettier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Weigl
 * @version 1 (1/7/22)
 */
public abstract class Doc {
    public abstract int len(StringBuilder target, PrintContext ctx);

    public abstract SDoc reduce(boolean flat, int indent, int curWidth, int maxWidth);

    public abstract int size(boolean flat, int indent, int width);

    public static final Doc NIL = new Doc() {
        @Override
        public int len(StringBuilder target, PrintContext ctx) {
            return 0;
        }

        @Override
        public SDoc reduce(boolean flat, int indent, int curWidth, int maxWidth) {
            return SDoc.NIL;
        }

        @Override
        public int size(boolean flat, int indent, int width) {
            return 0;
        }

        @Override
        public String toString() {
            return "Nil";
        }
    };

    public static final class Text extends Doc {
        private final String value;

        public Text(String value) {
            this.value = value;
        }

        @Override
        public int len(StringBuilder target, PrintContext ctx) {
            return value.length();
        }

        @Override
        public SDoc reduce(boolean flat, int indent, int curWidth, int maxWidth) {
            return new SDoc.Text(value);
        }

        @Override
        public int size(boolean flat, int indent, int width) {
            return width + value.length();
        }

        @Override
        public String toString() {
            return "Text(" + value + ")";
        }
    }

    public static final class SpaceOrNl extends Doc {
        int spaces = 1;

        @Override
        public int len(StringBuilder target, PrintContext ctx) {
            return spaces;
        }

        @Override
        public SDoc reduce(boolean flat, int indent, int curWidth, int maxWidth) {
            if (!flat)
                return new SDoc.NewLine(indent);
            else
                return new SDoc.Text(" ");
        }

        @Override
        public int size(boolean flat, int indent, int width) {
            return flat ? width + spaces : 1;
        }

        @Override
        public String toString() {
            return "_";
        }
    }

    private abstract static class MDoc extends Doc {
        protected final List<Doc> elements = new ArrayList<>(1);

        public MDoc(Doc... elements) {
            this(Arrays.asList(elements));
        }

        public MDoc(List<? extends Doc> elements) {
            this.elements.addAll(elements);
        }

        @Override
        public int len(StringBuilder target, PrintContext ctx) {
            return elements.stream().mapToInt(it -> len(target, ctx)).sum();
        }

        protected SDoc reduce0(boolean flat, int indent, int width, int maxWidth) {
            SDoc.List l = new SDoc.List(elements.size());
            for (Doc element : elements) {
                int newWidth = element.size(flat, indent, width);
                if (newWidth > maxWidth) {
                    throw new IllegalStateException();
                }
                SDoc other = element.reduce(flat, indent, width, maxWidth);
                l.elements.add(other);
                width = newWidth;
            }
            return l;
        }

        @Override
        public int size(boolean flat, int indent, int width) {
            for (Doc element : elements) {
                if (element instanceof SpaceOrNl && !flat)
                    return indent;
                width= element.size(flat, indent, width);
            }
            return width;
        }

        public void print(String s) {
            add(Doc.text(s));
        }

        public void println() {
            elements.add(Doc.space());
        }

        public void add(Doc accept) {
            elements.add(accept);
        }
    }

    public static final class Sequence extends MDoc {
        public Sequence(Doc... elements) {
            super(elements);
        }

        public Sequence(List<? extends Doc> elements) {
            super(elements);
        }

        @Override
        public String toString() {
            return elements.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" · ", "", ""));
        }

        @Override
        public SDoc reduce(boolean flat, int indent, int curWidth, int maxWidth) {
            return reduce0(flat, indent, curWidth, maxWidth);
        }


    }

    public static final class Nest extends MDoc {
        public int depth = 4;

        public Nest(Doc... elements) {
            super(elements);
        }

        public Nest(List<? extends Doc> elements) {
            super(elements);
        }

        public Nest(int depth, Doc... elements) {
            super(elements);
            this.depth = depth;
        }

        public Nest(List<? extends Doc> elements, int depth) {
            super(elements);
            this.depth = depth;
        }

        @Override
        public int len(StringBuilder target, PrintContext ctx) {
            return 0;
        }

        @Override
        public SDoc reduce(boolean flat, int indent, int curWidth, int maxWidth) {
            return reduce0(flat, indent + depth, curWidth, maxWidth);
        }

        @Override
        public int size(boolean flat, int indent, int width) {
            return super.size(flat, indent + depth, width);
        }

        @Override
        public String toString() {
            return elements.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" · ", "< ", " >"));
        }


    }

    public static final class Group extends MDoc {
        public Group(Doc... elements) {
            super(elements);
        }

        public Group(List<? extends Doc> elements) {
            super(elements);
        }

        @Override
        public int len(StringBuilder target, PrintContext ctx) {
            return 0;
        }

        @Override
        public SDoc reduce(boolean flat, int indent, int curWidth, int maxWidth) {
            try {
                return reduce0(true, indent, curWidth, maxWidth);
            } catch (IllegalStateException e) {
                return reduce0(false, indent, curWidth, maxWidth);
            }
        }


        @Override
        public String toString() {
            return elements.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" · ", "[ ", " ]"));
        }
    }

    public static Doc seq(Doc... docs) {
        return new Sequence(docs);
    }

    public static Doc text(String value) {
        return new Text(value);
    }

    public static Doc space() {
        return new SpaceOrNl();
    }

    public static Doc group(Doc... stmt) {
        return new Group(stmt);
    }

    public static Doc nest(Doc... docs) {
        return new Nest(docs);
    }
}
