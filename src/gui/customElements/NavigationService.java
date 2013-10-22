package gui.customElements;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import gui.pluginRegistry.SimPropRegistry;

public class NavigationService {

	public static String getMenu() {
		String head = ""+
				"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n"+
				"<head>\n"+
				"<title> Navigation </title>\n"+
				"</head>\n"+
				"<body>\n"+
				"<h1>Navigation</h1>\n"+
				"<a href=\"VIDEO1\">VIDEOTUTORIAL_TEST</a><br/>";
		String tail = "</p>\n"+
				"</body>\n"+
				"</html>";
		
		return head + content() + tail;
	}

	private static String content() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		
		Map<String, String>[] plugins = gcr.getPlugIns();
		
		String menu = "";
		for (int i=0; i < plugins.length; i++){
			Set<Entry<String, String>> pluginsInLayer = plugins[i].entrySet();
			for (Entry<String, String> entry : pluginsInLayer) {
				menu += "<a href=\"etc/html/plugins/"+entry.getKey().replaceAll("\\.class", "/")+"\">"+entry.getKey().split("\\.", 2)[0]+"</a><br/>\n";
			}
		}
		
		return menu;
	}
}
