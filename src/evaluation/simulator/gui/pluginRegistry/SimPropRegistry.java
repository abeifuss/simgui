package evaluation.simulator.gui.pluginRegistry;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
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

	@SuppressWarnings("unchecked")
	private final Map<String, String>[] pluginLayerMap = new HashMap[7];
	private final Map<String, SimProp> properties = new HashMap<String, SimProp>();
	private final Map<String, String> activePlugins = new HashMap<String, String>();

	private SimPropRegistry() {
		this.scan();
		this.scanPlugins();
	}

	public void dumpConfiguration() {

	}

	public Set<Entry<String, SimProp>> getAllSimProps() {
		return this.properties.entrySet();
	}

	public List<Entry<String, String>> getPluginItems(String pluginName) {

		List<Entry<String, String>> mapIdToName = new LinkedList<Entry<String, String>>();

		Iterator<Entry<String, SimProp>> iter = this.properties.entrySet().iterator();
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
		System.out.println("Register: " + s.getId());
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

			String plugInLayer = (plugin.getId()).split("\\.", 5)[3];
			
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
							property.setId(plugin.getName() + "::"
									+ annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation
									.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation
									.enable_requirements());
							((BoolProp) property).setValue(annotation
									.value());
							property.setEnable(true);
						}
						this.register(property);
					} else if (element.annotationType() == IntSimulationProperty.class) {
						IntSimulationProperty annotation = field
								.getAnnotation(IntSimulationProperty.class);
						property = new IntProp();
						if (annotation != null) {
							property.setId(plugin.getName() + "::"
									+ annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation
									.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation
									.enable_requirements());
							property.setValue_requirements(annotation
									.value_requirements());
							((IntProp) property).setMinValue(annotation
									.min());
							((IntProp) property).setMaxValue(annotation
									.max());
							property.setValue(annotation.value());
							property.setEnable(true);
						}
						this.register(property);
					} else if (element.annotationType() == FloatSimulationProperty.class) {
						FloatSimulationProperty annotation = field
								.getAnnotation(FloatSimulationProperty.class);
						property = new FloatProp();
						if (annotation != null) {
							property.setId(plugin.getName() + "::"
									+ annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation
									.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation
									.enable_requirements());
							((FloatProp) property).setMinValue(annotation
									.min());
							((FloatProp) property).setMaxValue(annotation
									.max());
							((FloatProp) property).setValue(annotation
									.value());
							property.setEnable(true);
						}
						this.register(property);
					} else if (element.annotationType() == DoubleSimulationProperty.class) {
						DoubleSimulationProperty annotation = field
								.getAnnotation(DoubleSimulationProperty.class);
						property = new DoubleProp();
						if (annotation != null) {
							property.setId(plugin.getName() + "::"
									+ annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation
									.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation
									.enable_requirements());
							((DoubleProp) property).setMinValue(annotation
									.min());
							((DoubleProp) property).setMaxValue(annotation
									.max());
							((DoubleProp) property).setValue(annotation
									.value());
							property.setEnable(true);
						}
						this.register(property);
					} else if (element.annotationType() == StringSimulationProperty.class) {
						StringSimulationProperty annotation = field
								.getAnnotation(StringSimulationProperty.class);
						property = new StringProp();
						if (annotation != null) {
							property.setId(plugin.getName() + "::"
									+ annotation.propertykey());
							property.setNamespace(plugin.getName());
							property.setName(annotation.name());
							property.setDescription(annotation
									.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayer(plugInLayer);
							property.setEnable_requirements(annotation
									.enable_requirements());
							((StringProp) property).setValue(annotation
									.value());
							((StringProp) property)
									.setPossibleValues(annotation
											.possibleValues());
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
		
		for (int i = 0; i < 7; i++) {
			this.pluginLayerMap[i] = new HashMap<String, String>();
		}
		
		String[] plugInLayer = { "clientSendStyle", "delayBox", "mixSendStyle",
				"outputStrategy", "plotType", "topology", "trafficSource" };
		
		for ( String key : properties.keySet() ) {
			SimProp simProp = properties.get(key);
			String layer = simProp.getPluginLayer();
			String name = simProp.getNamespace();

			if ( layer.equals(plugInLayer[0]) ){
				this.pluginLayerMap[0].put( name, layer );
			}else if ( layer.equals(plugInLayer[1]) ){
				this.pluginLayerMap[1].put( name, layer );
			}else if ( layer.equals(plugInLayer[2]) ){
				this.pluginLayerMap[2].put( name, layer );
			}else if ( layer.equals(plugInLayer[3]) ){
				this.pluginLayerMap[3].put( name, layer );
			}else if ( layer.equals(plugInLayer[4]) ){
				this.pluginLayerMap[4].put( name, layer );
			}else if ( layer.equals(plugInLayer[5]) ){
				this.pluginLayerMap[5].put( name, layer );
			}else if ( layer.equals(plugInLayer[6]) ){
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
		
		Map<String, String> pluginNameToConfigName = new HashMap<>();
		pluginNameToConfigName.put("delayBox", "TYPE_OF_DELAY_BOX");
		pluginNameToConfigName.put("trafficSource", "TYPE_OF_TRAFFIC_GENERATOR");
		pluginNameToConfigName.put("plotType", "");
		pluginNameToConfigName.put("outputStrategy", "OUTPUT_STRATEGY");
		pluginNameToConfigName.put("topology", "TOPOLOGY_SCRIPT");
		pluginNameToConfigName.put("mixSendStyle", "MIX_SEND_STYLE");
		pluginNameToConfigName.put("clientSendStyle", "CLIENT_SEND_STYLE");
		
		activePlugins.put(pluginNameToConfigName.get(pluginLevel), selectedPlugin);
	}
	
	public Map<String, String> getActivePlugins() {
		return activePlugins;
	}
}
