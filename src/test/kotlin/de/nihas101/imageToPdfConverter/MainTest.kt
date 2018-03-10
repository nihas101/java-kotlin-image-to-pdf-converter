package de.nihas101.imageToPdfConverter

import javafx.application.Platform.runLater
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import java.io.ByteArrayInputStream
import java.io.InputStream


class MainTest : ApplicationTest() {
    @Test
    fun testGUI() {
        Main.main(arrayOf())
        Thread.sleep(1000)
        closeCurrentWindow()
    }

    @Test
    fun test() {
        runLater({
            val old = System.`in`
            try {
                System.setIn(createTestInput())
                Main.main(arrayOf("--noGUI"))
            } finally {
                System.setIn(old)
            }
        })
    }

    private fun createTestInput(): InputStream {
        val data = "exit"
        return ByteArrayInputStream(data.toByteArray(charset("UTF-8")))
    }
}