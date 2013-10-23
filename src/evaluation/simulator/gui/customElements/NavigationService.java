package evaluation.simulator.gui.customElements;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class NavigationService {

	private static String content() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();

		Map<String, String>[] plugins = gcr.getPlugIns();

		String menu = "";
		for (Map<String, String> plugin : plugins) {
			Set<Entry<String, String>> pluginsInLayer = plugin.entrySet();
			for (Entry<String, String> entry : pluginsInLayer) {
				menu += "<a href=\"etc/html/plugins/"
						+ entry.getKey().replaceAll("\\.class", "/") + "\">"
						+ entry.getKey().split("\\.", 2)[0] + "</a><br/>\n";
			}
		}

		return menu;
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
