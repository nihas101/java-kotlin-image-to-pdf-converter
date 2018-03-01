package de.nihas101.imageToPdfConverter.ui

import de.nihas101.imageToPdfConverter.ui.PdfBuilderUI.PdfBuilderUiFactory.createPdfBuilderUI
import de.nihas101.imageToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineInput.CommandLineInputFactory.createCommandLineInput
import de.nihas101.imageToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineInterface.CommandLineInterfaceFactory.createCommandLineInterface
import de.nihas101.imageToPdfConverter.ui.commandLineIO.PdfBuilderCommandLineOutput.CommandLineOutputFactory.createCommandLineOutput
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.io.*

class PdfBuilderUITest {
    @Test
    fun start() {
        val commandLineInput = createCommandLineInput(createTestBufferedReader())
        val commandLineOutput = createCommandLineOutput(createTestOutputStream())
        val commandLineInterface = createCommandLineInterface(commandLineInput, commandLineOutput)
        val pdfBuilderUI = createPdfBuilderUI(commandLineInterface)
        pdfBuilderUI.start()

        val file = File("src/test/resources/images/pdfBuilderUITest.pdf")
        assertEquals(true, file.exists())
        file.deleteOnExit()
    }

    private fun createTestBufferedReader(): BufferedReader {
        return TestBufferedReader()
    }

    class TestBufferedReader(testReader: TestReader = TestReader()) : BufferedReader(testReader) {
        private var state = 0

        override fun readLine(): String {
            return when (state) {
                0 -> ret("src/test/resources/images")
                1 -> ret("no")
                2 -> ret("no")
                3 -> ret("b")
                4 -> ret("yes")
                5 -> ret("src/test/resources/images/pdfBuilderUITest.pdf")
                else -> ret("exit")
            }
        }

        fun ret(string: String): String {
            state++
            return string
        }
    }

    class TestReader : Reader() {
        override fun read(cbuf: CharArray?, off: Int, len: Int): Int {
            /* DO NOTHING */
            return -1
        }

        override fun close() {
            /* DO NOTHING */
        }

    }

    private fun createTestOutputStream(): PrintStream {
        return TestPrintStream()
    }

    class TestPrintStream : PrintStream(TestOutputStream()) {
        var lastPrinted = ""

        override fun println(string: String) {
            print(string + "\n")
        }

        override fun print(string: String) {
            lastPrinted = string
        }
    }

    class TestOutputStream : OutputStream() {
        override fun write(b: Int) {
            /* IGNORE */
        }
    }
}