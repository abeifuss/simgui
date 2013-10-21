package evaluation.simulator.gui.customElements;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class NavigationService {

	private static String content() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();

		Map<String, String>[] plugins = gcr.getPlugIns();

		String foo = "";
		for (Map<String, String> plugin : plugins) {
			Set<Entry<String, String>> test = plugin.entrySet();
			for (Entry<String, String> entry : test) {
				foo += "<a href=\"src/" + entry.getValue() + "\">"
						+ entry.getKey() + "</a><br/>\n";
			}
		}

		return foo;
	}

	public static String getMenu() {
		String head = ""
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"
				+ "<head>\n" + "<title> Navigation </title>\n" + "</head>\n"
				+ "<body>\n" + "<h1>Navigation</h1>\n"
				+ "<a href=\"VIDEO1\">VIDEOTUTORIAL_TEST</a><br/>";
		String tail = "</p>\n" + "</body>\n" + "</html>";

		return head + content() + tail;
	}
}
