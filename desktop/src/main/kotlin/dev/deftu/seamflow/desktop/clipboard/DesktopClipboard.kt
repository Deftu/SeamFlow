package dev.deftu.seamflow.desktop.clipboard

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

class DesktopClipboard {
    private val clipboard = Toolkit.getDefaultToolkit().systemClipboard

    fun readTextOrNull(): String? {
        return runCatching {
            val data = clipboard.getData(DataFlavor.stringFlavor) as? String
            data?.takeIf { it.isNotBlank() }
        }.getOrNull()
    }

    fun writeText(text: String) {
        clipboard.setContents(StringSelection(text), null)
    }
}
