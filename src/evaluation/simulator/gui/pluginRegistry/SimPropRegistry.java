package evaluation.simulator.gui.pluginRegistry;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

import evaluation.simulator.annotations.plugin.AnnotatedPlugin;
import evaluation.simulator.annotations.plugin.PluginAnnotation;
import evaluation.simulator.annotations.simulationProperty.BoolProp;
import evaluation.simulator.annotations.simulationProperty.BoolSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.DoubleProp;
import evaluation.simulator.annotations.simulationProperty.DoubleSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.FloatProp;
import evaluation.simulator.annotations.simulationProperty.FloatSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.IntProp;
import evaluation.simulator.annotations.simulationProperty.IntSimulationProperty;
import evaluation.simulator.annotations.simulationProperty.SimProp;
import evaluation.simulator.annotations.simulationProperty.StringProp;
import evaluation.simulator.annotations.simulationProperty.StringSimulationProperty;
import evaluation.simulator.gui.customElements.accordion.ListEntry;
import evaluation.simulator.log.LogLevel;
import evaluation.simulator.log.Logger;

public class SimPropRegistry {

	private static SimPropRegistry _instance = null;

	public static SimPropRegistry getInstance() {
		if (_instance == null) {
			_instance = new SimPropRegistry();
		}
		return _instance;
	}

//	private final List<String> pluginLayer;

	@SuppressWarnings("unchecked")
	private final Map<String, String>[] pluginLayerMap = new HashMap[7];
	private final Map<String, SimProp> properties = new HashMap<String, SimProp>();
	private final Map<String, String> activePlugins = new HashMap<String, String>();
	private final Map<String, String> activePluginsMapped = new HashMap<String, String>();
	private final Map<String, String> pluginNameToConfigName;
	private final Map<String, String> configNameToPluginName;
	private SimPropRegistry() {
		
		// This map specifies the order of the AccordionEntries
		// in the ConfigTool. Furthermore this map is used to map the display
		// name to the propertyname which is used to specify the active plugin
		pluginNameToConfigName = new LinkedHashMap<String, String>();
		pluginNameToConfigName.put("trafficSource", "TYPE_OF_TRAFFIC_GENERATOR");
		pluginNameToConfigName.put("clientSendStyle", "CLIENT_SEND_STYLE");
		// TODO: insert RecordingScheme
		pluginNameToConfigName.put("delayBox", "TYPE_OF_DELAY_BOX");
		pluginNameToConfigName.put("mixSendStyle", "MIX_SEND_STYLE");
		pluginNameToConfigName.put("outputStrategy", "OUTPUT_STRATEGY");
		// TODO: remove
		pluginNameToConfigName.put("plotType", "PLOT_TYPE");
		pluginNameToConfigName.put("topology", "TOPOLOGY");
		
		configNameToPluginName = new LinkedHashMap<String, String>();
		for ( Entry<String, String> entry : pluginNameToConfigName.entrySet() ){
			configNameToPluginName.put(entry.getValue(), entry.getKey());
		}
		
		this.scan();
		this.scanPlugins();
	}

	public void dumpConfiguration() {

	}

	public Set<Entry<String, SimProp>> getAllSimProps() {
		return properties.entrySet();
	}

	public List<Entry<String, String>> getPluginItems(String pluginName) {

		List<Entry<String, String>> mapIdToName = new LinkedList<Entry<String, String>>();

		Iterator<Entry<String, SimProp>> iter = properties.entrySet().iterator();
		while (iter.hasNext()) {
			SimProp entry = iter.next().getValue();
			if (pluginName.equals(entry.getNamespace())) {
				mapIdToName.add(new ListEntry<String, String>(entry.getId(), entry.getName()));
			}
		}

		return mapIdToName;
	}

	public Map<String, String>[] getPlugIns() {
		return this.pluginLayerMap;
	}

	public SimProp getValue(String key) {

		return this.properties.get(key);
	}

	public void register(SimProp s) {
		Logger.Log(LogLevel.DEBUG, "Register: " + s.getId());
		if (this.properties.containsKey(s.getId())) {

			GraphicsDevice graphicsDevice = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int x = graphicsDevice.getDisplayMode().getWidth();
			int y = graphicsDevice.getDisplayMode().getHeight();

			JOptionPane alert = new JOptionPane("Redefinition of " + s.getId()
					+ " (" + s.getPlugin() + ") detected!");

			JDialog dialog = alert.createDialog(null, "");
			int w = dialog.getWidth();
			int h = dialog.getHeight();
			dialog.setLocation((x / 2) - (w / 2), (y / 2) - (h / 2));
			dialog.setVisible(true);

		} else {
			this.properties.put(s.getId(), s);
		}
	}

	// Scans the simulation properties
	public void scan() {

		AnnotatedPlugin plugin;

		Reflections reflectionsPlugins = new Reflections(
				ClasspathHelper.forPackage("evaluation.simulator.plugins"),
				new TypeAnnotationsScanner());

		// Look for classes with PluginAnnotation
		Set<Class<?>> types = reflectionsPlugins
				.getTypesAnnotatedWith(evaluation.simulator.annotations.plugin.PluginAnnotation.class);

		for (Class<?> plugInClass : types) {
			
			Logger.Log( LogLevel.DEBUG, "Class: " + plugInClass.getCanonicalName());

			PluginAnnotation pluginAnnotation = plugInClass.getAnnotation(PluginAnnotation.class);

			plugin = new AnnotatedPlugin();
			plugin.setId(plugInClass.getName());
			plugin.setName(pluginAnnotation.name());
			plugin.setDocumentationURL(pluginAnnotation.documentationURL());
			plugin.setPluginLayer(pluginAnnotation.pluginLayer());

			String plugInLayer;
			if ( plugin.getPluginLayer().equals("") ){
				plugInLayer = (plugin.getId()).split("\\.", 5)[3];
			} else {
				plugInLayer = plugin.getPluginLayer();
			}
			
			readFields( plugin, plugInClass.getDeclaredFields(), plugInLayer );
			
			// lookup for super class
			Class<?> superClass = plugInClass.getSuperclass();
			Logger.Log( LogLevel.DEBUG , "Superclass: " + superClass.getCanonicalName());
			readFields( plugin, superClass.getDeclaredFields(), plugInLayer );
			
			// scan for inner classes
			Class<?>[] innerClasses = plugInClass.getClasses();
			for ( Class<?> innerClass : innerClasses ){
				Logger.Log( LogLevel.DEBUG , "Inner class: " + innerClass.getCanonicalName());
				readFields( plugin, innerClass.getDeclaredFields(), plugInLayer );
			}
			
		}

		// call initial dependency-check for per plugin configurations
		DependencyChecker.checkAll(this);
	}

	private void readFields(AnnotatedPlugin plugin, Field[] fields, String plugInLayer ) {
		
		SimProp property;
		
		try {
			for (Field field : fields ) {
				
				Annotation[] a = field.getAnnotations();
				for (Annotation element : a) {
					
					
					if (element.annotationType() == BoolSimulationProperty.class) {
						BoolSimulationProperty annotation = field
								.getAnnotation(BoolSimulationProperty.class);
						property = new BoolProp();
						if (annotation != null) {
							// property.setId(f.getName());
							property.setId(annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation.enable_requirements());
							((BoolProp) property).setValue(annotation.value());
							property.setEnable(true);
						}
						this.register(property);
					} else if (element.annotationType() == IntSimulationProperty.class) {
						IntSimulationProperty annotation = field
								.getAnnotation(IntSimulationProperty.class);
						property = new IntProp();
						if (annotation != null) {
							property.setId(annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							((IntProp) property).setMinValue(annotation.min());
							((IntProp) property).setMaxValue(annotation.max());
							property.setValue(annotation.value());
							property.setEnable(true);
						}
						this.register(property);
					} else if (element.annotationType() == FloatSimulationProperty.class) {
						FloatSimulationProperty annotation = field
								.getAnnotation(FloatSimulationProperty.class);
						property = new FloatProp();
						if (annotation != null) {
							property.setId(annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation.enable_requirements());
							((FloatProp) property).setMinValue(annotation.min());
							((FloatProp) property).setMaxValue(annotation.max());
							((FloatProp) property).setValue(annotation.value());
							property.setEnable(true);
						}
						this.register(property);
					} else if (element.annotationType() == DoubleSimulationProperty.class) {
						DoubleSimulationProperty annotation = field
								.getAnnotation(DoubleSimulationProperty.class);
						property = new DoubleProp();
						if (annotation != null) {
							property.setId(annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation.enable_requirements());
							((DoubleProp) property).setMinValue(annotation.min());
							((DoubleProp) property).setMaxValue(annotation.max());
							((DoubleProp) property).setValue(annotation.value());
							property.setEnable(true);
						}
						this.register(property);
					} else if (element.annotationType() == StringSimulationProperty.class) {
						StringSimulationProperty annotation = field
								.getAnnotation(StringSimulationProperty.class);
						property = new StringProp();
						if (annotation != null) {
							property.setId(annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation.enable_requirements());
							((StringProp) property).setValue(annotation.value());
							((StringProp) property).setPossibleValues(annotation.possibleValues());
							property.setEnable(true);
						}
						this.register(property);
					} else {
						Logger.Log(LogLevel.ERROR,
								"GuiConfigRegistry - bad type");
						continue;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void scanPlugins() {
		
		String[] pluginLayer = pluginNameToConfigName.keySet().toArray(new String[0]);
		for (int i = 0; i < pluginNameToConfigName.size(); i++) {
			this.pluginLayerMap[i] = new HashMap<String, String>();
		}
		
		for ( String key : properties.keySet() ) {
			SimProp simProp = properties.get(key);
			String layer = simProp.getPluginLayer(); // aka pluginPackage
			String name = simProp.getNamespace(); // aka pluginName
			
			if ( layer.equals(pluginLayer[0]) ){
				this.pluginLayerMap[0].put( name, layer );
			}else if ( layer.equals(pluginLayer[1]) ){
				this.pluginLayerMap[1].put( name, layer );
			}else if ( layer.equals(pluginLayer[2]) ){
				this.pluginLayerMap[2].put( name, layer );
			}else if ( layer.equals(pluginLayer[3]) ){
				this.pluginLayerMap[3].put( name, layer );
			}else if ( layer.equals(pluginLayer[4]) ){
				this.pluginLayerMap[4].put( name, layer );
			}else if ( layer.equals(pluginLayer[5]) ){
				this.pluginLayerMap[5].put( name, layer );
			}else if ( layer.equals(pluginLayer[6]) ){
				this.pluginLayerMap[6].put( name, layer );
			}else {
				System.err.println( "No such plugin layer: " + layer );
			}
		}
	}

	public void setValue(String key, Object arg0) {

		if (arg0.getClass() == Boolean.class) {
			System.out.println("Boolean");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == Float.class) {
			System.out.println("Float");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == Double.class) {
			System.out.println("Double");
			this.properties.get(key).setValue(arg0);
		}else if (arg0.getClass() == Integer.class) {
			System.out.println("Integer");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == String.class) {
			System.out.println("String");
			this.properties.get(key).setValue(arg0);

		} else {

		}
	}

	public List<SimProp> getSimPropertiesByNamespace(String pluginNamespace) {
		
		List<SimProp> simPropertiesInANamespace = new LinkedList<SimProp>();
		
		for ( String key : properties.keySet() ) {
			SimProp simProp = properties.get(key);
			
			if ( simProp.getNamespace().equals(pluginNamespace) ){
				simPropertiesInANamespace.add( simProp );
			}
		}
		
		return simPropertiesInANamespace;
	}

	public void setActivePlugins(String pluginLevel, String selectedPlugin) {
		Logger.Log(LogLevel.DEBUG, "Set " + pluginLevel + " plugin to " + selectedPlugin);
		activePlugins.put(pluginLevel, selectedPlugin);
		activePluginsMapped.put( pluginNameToConfigName(pluginLevel), selectedPlugin);
	}
	
	public void setActivePluginsMapped(String pluginLevel, String selectedPlugin) {
		Logger.Log(LogLevel.DEBUG, "Set mapped " + pluginNameToConfigName(pluginLevel) + " plugin to " + selectedPlugin);
		activePlugins.put(configNameToPluginName(pluginLevel), selectedPlugin);
		activePluginsMapped.put(pluginLevel, selectedPlugin);
	}
	
	public Map<String, String> getActivePlugins(boolean mapped) {
		if (mapped){
			return activePluginsMapped;
		}else{
			return activePlugins;	
		}
	}

	public List<String> getPluginLevels() {
		return new LinkedList<String>(pluginNameToConfigName.keySet());
	}

	public String pluginNameToConfigName(String pluginLevel) {
		return pluginNameToConfigName.get(pluginLevel);
	}
	
	public String configNameToPluginName(String pluginLevel) {
		return configNameToPluginName.get(pluginLevel);
	}

	public List<String> getConfigNamesForPluginLayers() {
		
		List<String> configNamesForPluginLayers = new LinkedList<String>();
		for ( Entry<String, String> entry : configNameToPluginName.entrySet() )
		{
			configNamesForPluginLayers.add( entry.getKey() );
		}
		return configNamesForPluginLayers;
	}
	
}
