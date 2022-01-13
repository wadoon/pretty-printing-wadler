package com.github.wadoon.prettier

import org.junit.jupiter.api.Test

/**
 * @author Alexander Weigl
 * @version 1 (1/13/22)
 */
internal class LayouterTest {
    @Test
    fun test() {
        val doc = Doc.group(
            Doc.text("begin"),
            Doc.nest(
                Doc.space(),
                Doc.group(
                    Doc.text("stmt"),
                    Doc.space(),
                    Doc.text("stmt"),
                    Doc.space(),
                    Doc.text("stmt")
                )
            ),
            Doc.space(),
            Doc.text("end")
        )
        val sdoc: SDoc = Doc.layout(doc, 20)
        println(sdoc)
        println("---")
        println(Doc.asString(doc, 60))
        println("---")
        println(Doc.asString(doc, 50))
        println("---")
        println(Doc.asString(doc, 40))
        println("---")
        println(Doc.asString(doc, 30))
        println("---")
        println(Doc.asString(doc, 20))
        println("---")
        println(Doc.asString(doc, 10))
        println("---")
    }
}