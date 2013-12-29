package evaluation.simulator.gui.customElements;

import java.util.Map;
import java.util.TreeMap;

import evaluation.simulator.annotations.property.SimProp;
import evaluation.simulator.gui.helper.ValueComparator;
import evaluation.simulator.gui.pluginRegistry.SimPropRegistry;

public class NavigationService {

	private static String path = "etc/html/pluginHelp/";
	private static Map<String, String> layerMapDisplayNameToConfigName;
	private static Map<String, SimProp> propertyMap;
	private static Map<String, String> registeredPlugins;

	private static String content() {

		layerMapDisplayNameToConfigName = SimPropRegistry.getInstance().getLayerMapDisplayNameToConfigName();
		propertyMap = SimPropRegistry.getInstance().getProperties();
		registeredPlugins = SimPropRegistry.getInstance().getRegisteredPlugins();
		String menu = "";
		
		Map<String, Integer> layerMap = SimPropRegistry.getInstance().getLayerMapDisplayNameToOrder();
		// Sort the map by value (first) and key (second)
		ValueComparator comperatorLayer =  new ValueComparator(layerMap);
		TreeMap<String,Integer> sortedLayerMap = new TreeMap<String,Integer>(comperatorLayer);
		sortedLayerMap.putAll(layerMap);

		for ( String layer : sortedLayerMap.keySet() ){
			menu+=layer+"<br>";
			for ( String prop : propertyMap.keySet() ){
				if ( propertyMap.get(prop).getPluginID().equals("") &&
						( propertyMap.get(prop).isSuperclass() || propertyMap.get(prop).isGlobal() ) &&
						propertyMap.get(prop).getPluginLayerID().equals(layerMapDisplayNameToConfigName.get(layer)) ){

					menu += "<a href=\"etc/html/plugins/"
							+ prop+".html\">&nbsp;&nbsp;&nbsp;&nbsp;"
							+ prop + "</a><br>\n";
				}
			}
			for ( String plugin : registeredPlugins.keySet() ){
				if ( registeredPlugins.get(plugin).equals(layerMapDisplayNameToConfigName.get(layer))){

					// plugins
					menu += "<a href=\"etc/html/plugins/"
							+ plugin+".html\">&nbsp;&nbsp;&nbsp;&nbsp;"
							+ plugin + "</a><br>\n";
					//					for ( String prop : propertyMap.keySet() ){
					//						if ( propertyMap.get(prop).getPluginID().equals(plugin) ){
					//							// plugin dependent properties
					//							menu += "<a href=\"etc/html/plugins/"
					//									+ prop+".html\">"
					//									+ prop + "</a><br/>\n";
					//						}
					//					}
				}
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
