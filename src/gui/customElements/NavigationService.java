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
				"<h1>Navigation</h1>";
		
		String tail = "</p>\n"+
				"</body>\n"+
				"</html>";
		
		return head + content() + tail;
	}

	private static String content() {
		SimPropRegistry gcr = SimPropRegistry.getInstance();
		
		Map<String, String>[] plugins = gcr.getPlugIns();
		
		String foo = "";
		for (int i=0; i < plugins.length; i++){
			Set<Entry<String, String>> test = plugins[i].entrySet();
			for (Entry<String, String> entry : test) {
				foo += "<a href=\"src/"+entry.getValue()+"\">"+entry.getKey()+"</a><br/>\n";
			}
		}
		
		return foo;
	}
}
