package framework.core.gui.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardManager {

    public static void copyToClipboard(String string) {
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Clipboard clipboard = toolkit.getSystemClipboard();
	StringSelection strSel = new StringSelection(string);
	clipboard.setContents(strSel, null);
    }
}
