package com.github.wadoon.prettier

import org.junit.jupiter.api.Test

/**
 * Examples of https://github.com/folktale/text.pretty-printing
 *
 * @author Alexander Weigl
 * @version 1 (23.11.22)
 */
class TextPrettyPrintingExamples {
    @Test
    fun readme() {
        val tree: Tree = Branch(
            Branch(
                Leaf(1),
                Branch(Leaf(2), Leaf(3))
            ),
            Leaf(4)
        )
        println(tree.toString(20))
        println(tree.toString(30))
        println(tree.toString(40))
    }
}

sealed class Tree {
    fun toString(width: Int): String {
        return Doc.asString(toDoc(0), width)
    }

    abstract fun toDoc(indent: Int): Doc?
}

data class Leaf(private val i: Int) : Tree() {
    override fun toDoc(indent: Int): Doc? {
        return Doc.text("Leaf(")
            .append(Doc.text(i))
            .append(Doc.text(")"))
    }
}

data class Branch(private val left: Tree, private val right: Tree) : Tree() {
    override fun toDoc(indent: Int): Doc? {
        return Doc.nest(
            Doc.text("Branch("),
            left.toDoc(indent),
            Doc.text(", "),
            right.toDoc(indent),
            Doc.text(")")
        )
    }
}