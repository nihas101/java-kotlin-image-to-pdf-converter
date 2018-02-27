package de.nihas101.imageToPdfConverter

import javafx.embed.swing.JFXPanel
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.swing.SwingUtilities

class ToolkitInitializer {
    companion object {
        fun initalizeToolkit() {
            val latch = CountDownLatch(1)
            SwingUtilities.invokeLater {
                JFXPanel() // initializes JavaFX environment
                latch.countDown()
            }
            latch.await(2, TimeUnit.SECONDS)
        }
    }
}