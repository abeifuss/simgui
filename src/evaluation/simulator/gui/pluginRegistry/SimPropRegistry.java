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
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

import com.sun.media.Log;

import evaluation.simulator.annotations.plugin.PluginSuperclass;
import evaluation.simulator.annotations.plugin.SimGuiPlugin;
import evaluation.simulator.annotations.plugin.Plugin;
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

	private int numberOfPluginLayers;
	private int numberOfNonPluginLayers;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private final Map<String, String>[] pluginLayerMap = new HashMap[100];
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private final Map<String, String>[] nonPluginLayerMap = new HashMap[100];
	/**
	 * Maps all registered properties.
	 * key:	config name of the property
	 * value: property
	 */
	private final Map<String, SimProp> properties = new HashMap<String, SimProp>();
	/**
	 * Maps the active plugins. Active plungins are those, which are enabled in the GUI.
	 * key: name of plugin level
	 * value: name of the plugin
	 */
	private final Map<String, String> activePlugins = new HashMap<String, String>();
	/**
	 * Maps the active plugins. Active plungins are those, which are enabled in the GUI.
	 * key: name of the plugin
	 * value: name of plugin level 
	 */
	private final Map<String, String> activePluginsMapped = new HashMap<String, String>();
	/**
	 *  Maps between the display name of a plugin layer and the config name.
	 *  key: display name of the plugin layer
	 *  value: config name of the plugin layer
	 */
	private final Map<String, String> layerMapDisplayNameToConfigName = new LinkedHashMap<String, String>();
	/**
	 *  Maps between the display name of a plugin layer and the config name.
	 *  key: config name of the plugin layer
	 *  value: display name of the plugin layer
	 */
	private final Map<String, String> layerMapConfigNameToDisplayName = new LinkedHashMap<String, String>();
	/**
	 * Maps the registered plugins to the corresponding plugin layer.
	 * key: config name of the plugin
	 * value: display name of the plugin layer
	 */
	private final Map<String, String> registeredPlugins = new HashMap<String, String>();
	
	private SimPropRegistry() {
		numberOfPluginLayers = 0;
		numberOfNonPluginLayers = 0;
		this.scanForStaticPaoperties();
		this.scanForPluginProperties();
		this.scanPlugins();
		this.toString();
		this.check();
	}

	// TODO: remove when finished
	private void check() {
		
		Map<String, Integer> numberOfVsiblePluginsInLayer = new HashMap<>();
		numberOfVsiblePluginsInLayer.put("Load Generator",5);
		numberOfVsiblePluginsInLayer.put("Mix Client", 5);
		numberOfVsiblePluginsInLayer.put("Mix Proxy", 2);
		numberOfVsiblePluginsInLayer.put("Mix Server", 19);
		numberOfVsiblePluginsInLayer.put("Topology", 4);
		numberOfVsiblePluginsInLayer.put("Underlay-net", 2);
		numberOfVsiblePluginsInLayer.put("Plotter", 3);
		
		for ( String layer : numberOfVsiblePluginsInLayer.keySet() ){
			int expected = numberOfVsiblePluginsInLayer.get( layer );
			int actual = getPluginsInLayer( layer ).length;
			if ( actual != expected ){
				Logger.Log(LogLevel.ERROR, "Number of plugins in " + layer + " is " + actual + " and not " + expected  + " as expected");
				// System.exit(-1);
			}
		}
		
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
			if (pluginName.equals(entry.getPluginID())) {
				mapIdToName.add(new ListEntry<String, String>(entry.getPropertyID(), entry.getName()));
			}
		}

		return mapIdToName;
	}

	public Map<String, String>[] getPluginLayerMap() {
		return this.pluginLayerMap;
	}

	public SimProp getValue(String key) {

		return this.properties.get(key);
	}

	public void register(SimProp s, boolean isSuperClass, boolean isGlobal, String pluginLayer) {
		if (this.properties.containsKey(s.getPropertyID()) && 
				!this.properties.get( s.getPropertyID() ).getPluginLayerID().equals(this.pluginNameToConfigName(pluginLayer))) {

			GraphicsDevice graphicsDevice = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int x = graphicsDevice.getDisplayMode().getWidth();
			int y = graphicsDevice.getDisplayMode().getHeight();

			if ( !this.properties.get( s.getPropertyID() ).equals(pluginLayer) ) {
				JOptionPane alert = new JOptionPane("Redefinition of property '" + s.getPropertyID()
						+ "' at superclass level detected \n (" +
						this.properties.get( s.getPropertyID() ).getPluginLayerID() + "" +
						", " + this.pluginNameToConfigName(pluginLayer) + ") \n" +
						"Please fix the problem!");
				
				JDialog dialog = alert.createDialog(null, "");
				int w = dialog.getWidth();
				int h = dialog.getHeight();
				dialog.setLocation((x / 2) - (w / 2), (y / 2) - (h / 2));
				dialog.setVisible(true);
				System.exit(-1);
			}else{
				JOptionPane alert = new JOptionPane("Redefinition of " + s.getPropertyID()
						+ " at pluginlevel (" + s.getPluginID() + ") detected!");
				
				JDialog dialog = alert.createDialog(null, "");
				int w = dialog.getWidth();
				int h = dialog.getHeight();
				dialog.setLocation((x / 2) - (w / 2), (y / 2) - (h / 2));
				dialog.setVisible(true);
			}

		} else if ( !this.properties.containsKey(s.getPropertyID()) && isSuperClass) {
			Logger.Log(LogLevel.DEBUG,  "Associate superclass property " + s.getPropertyID() + " with " + s.getPluginLayerID());
			s.isGlobal(true);
			this.properties.put(s.getPropertyID(), s);
		} else {
			Logger.Log(LogLevel.DEBUG, "Register property (" + s.getPropertyID() + ", " + s.getPluginID() + ", " + s.getPluginLayerID() + ")");
			s.isGlobal(false);
			this.properties.put(s.getPropertyID(), s);
		}
	}
	
	// Scans static simulation properties
	private void scanForStaticPaoperties() {
		
		Reflections reflections = new Reflections(
				ClasspathHelper.forPackage("evaluation.simulator.plugins"),
				new FieldAnnotationsScanner());
		
		Set<Field> fields = reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.IntSimulationProperty.class);
		fields.addAll(reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.FloatSimulationProperty.class));
		fields.addAll(reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.DoubleSimulationProperty.class));
		fields.addAll(reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.StringSimulationProperty.class));
		fields.addAll(reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.BoolSimulationProperty.class));
	
		SimProp property;
		boolean globalProperty = true;
		for ( Field field : fields ){
				Annotation[] a = field.getAnnotations();
				for (Annotation element : a) {
					if (element.annotationType() == IntSimulationProperty.class) {
						IntSimulationProperty annotation = field.getAnnotation(IntSimulationProperty.class);
						if ( !annotation.inject().equals("") ){
							String[] injectionArguments = annotation.inject().split("@");
							String layer = "";
							String plugin = "";
							if ( injectionArguments.length == 1) {
								layer = injectionArguments[0];
								plugin = "";
								Logger.Log(LogLevel.DEBUG, "Injected property " + annotation.propertykey() + " into " + layer);
								globalProperty = true;
							}else if ( injectionArguments.length == 2 ) {
								layer = injectionArguments[0];
								plugin = injectionArguments[1];
								Logger.Log(LogLevel.DEBUG, "Injected property " + annotation.propertykey() + " into " + layer + "@" + plugin);
								globalProperty = false;
							} else {
								Logger.Log(LogLevel.ERROR, "Can not inject " + annotation.propertykey() + "! Bad number of injection arguments");
								System.exit(-1);
							}
							property = new IntProp();
							property.setId(annotation.propertykey());
							property.setName(annotation.propertykey());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayerID(layer);
							property.setPluginID(plugin);
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							((IntProp) property).setMinValue(annotation.min());
							((IntProp) property).setMaxValue(annotation.max());
							property.setValue(annotation.value());
							property.setEnable(true);
							property.isGlobal(globalProperty);

							properties.put(property.getName(), property);
							
							if ( !layerMapDisplayNameToConfigName.containsKey("Recoding Scheme")){
								Logger.Log(LogLevel.DEBUG, "Register injected pluginlevel (" + annotation.inject() + ", Recoding Scheme)");
								layerMapDisplayNameToConfigName.put("Recoding Scheme", annotation.inject());	
							}
							
							if ( !globalProperty ){
								registerPlugin(plugin, layer, true);
							}
						}
					}
				}
		}
		
//		Field[] fieldArray = fields.toArray( new Field[fields.size()]);
//		readFields(null, fieldArray, null, false);
	
	}

	// Scans plugin dependent simulation properties
	public void scanForPluginProperties() {

		SimGuiPlugin plugin;

		// TODO: Seems not to work properly
		Reflections reflectionsPlugins = new Reflections(
				ClasspathHelper.forPackage("evaluation.simulator.plugins"),
				new TypeAnnotationsScanner());

		// Look for classes with PluginAnnotation
		Set<Class<?>> types = reflectionsPlugins.getTypesAnnotatedWith(evaluation.simulator.annotations.plugin.Plugin.class);

		for (Class<?> pluginClass : types) {

			Plugin pluginAnnotation = pluginClass.getAnnotation(Plugin.class);

			plugin = new SimGuiPlugin();
			plugin.setId(pluginClass.getName());
			plugin.setName(pluginAnnotation.name());
			plugin.setDocumentationURL(pluginAnnotation.documentationURL());
			plugin.setPluginLayer(pluginAnnotation.pluginLayer());
			plugin.isVisible(pluginAnnotation.vilible());
			plugin.makeFieldsGlobal(pluginAnnotation.makeFieldsGlobal());

			boolean autodetectPluginLayer = pluginAnnotation.pluginLayer().equals("");
			
			if ( !autodetectPluginLayer ){
				
				registerPlugin( plugin.getName(), pluginAnnotation.pluginLayer(), plugin.isVisible() );
				readFields( plugin, pluginClass.getDeclaredFields(), pluginAnnotation.pluginLayer(), false );
			
			}else{ // AUTODETECT PLUGINLAYER
			
				Class<?> superClass = pluginClass.getSuperclass();
				
				// first find the annotated superclass in a recursive manner
				Class<?> tmp = pluginClass;
				while ( !tmp.isAnnotationPresent( PluginSuperclass.class ) && 
						!tmp.getCanonicalName().equals("java.lang.Object") ){
					tmp = tmp.getSuperclass();
				}
				
				if ( tmp.isAnnotationPresent( PluginSuperclass.class) ){
					registerPlugin( plugin.getName(), tmp.getAnnotation( PluginSuperclass.class ).key(), plugin.isVisible() );
				}else{
					Logger.Log(LogLevel.ERROR, plugin.getName() + " is not caped by a superclass");
				}
				
				// invalid inheritance
				if ( superClass.isAnnotationPresent( Plugin.class ) && 
						superClass.getAnnotation( Plugin.class ).name() != pluginAnnotation.name() ){
					Logger.Log( LogLevel.ERROR , "Check the annotation in class " + superClass.getCanonicalName() +
							" it should be @Plugin( name = \""+ plugin.getName() +"\" ... ) or a valid @PluginSuperclass annotation.");
				}
				
				// valid shared superclass
				// resgister plugin with superclass
				if ( superClass.isAnnotationPresent( PluginSuperclass.class ) ){
					String pluginLayerName = superClass.getAnnotation( PluginSuperclass.class ).pluginLayerName();
					if ( !layerMapDisplayNameToConfigName.containsKey( pluginLayerName )){
						String name = superClass.getAnnotation( PluginSuperclass.class ).pluginLayerName();
						String key = superClass.getAnnotation( PluginSuperclass.class ).key();
						Logger.Log( LogLevel.DEBUG , "Register pluginlevel (" + key + ", " + name + ")");
						layerMapDisplayNameToConfigName.put(name, key);
						
						String fakePlugins = superClass.getAnnotation( PluginSuperclass.class ).fakePlugins();
						if ( !fakePlugins.equals("") ){
							String[] fakedPlugins = fakePlugins.split(",");
							for (int i = 0; i < fakedPlugins.length; i++ ){
								Logger.Log(LogLevel.DEBUG, "Inject fake plugin " + fakedPlugins[i] + " to " + pluginLayerName );
								registerPlugin(fakedPlugins[i], superClass.getAnnotation( PluginSuperclass.class ).key(), true);
							}
							// pluginLayerNameToConfigName.put(superClass.getAnnotation( PluginSuperclass.class ).key(), pluginLayerName);
						}
						
						readFields( plugin, superClass.getDeclaredFields(), pluginLayerName, true );
					}else{
						readFields( plugin, pluginClass.getDeclaredFields(), pluginLayerName, false );
					}
				}else{
					Logger.Log(LogLevel.ERROR, "Plugin " + plugin.getName() + "'s superclass has no @PluginSuperclass annotation");
				}
				
				// This map is used to map the display name of to the
				// propertyname which is used to identify the active plugin
				for ( Entry<String, String> entry : layerMapDisplayNameToConfigName.entrySet() ){
					layerMapConfigNameToDisplayName.put(entry.getValue(), entry.getKey());
				}
			}
		}

		// call initial dependency-check for per plugin configurations
		DependencyChecker.checkAll(this);
	}

	private void registerPlugin(String plugin, String plugInLayer, boolean isVisible) {
		if ( isVisible ){
			Logger.Log(LogLevel.DEBUG, "Register plugin (" + plugin + ", " + plugInLayer + ")");
			
			GraphicsDevice graphicsDevice = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int x = graphicsDevice.getDisplayMode().getWidth();
			int y = graphicsDevice.getDisplayMode().getHeight();
			
			// Check if this plugin is known for a different pluginlayer
			if ( registeredPlugins.containsKey(plugin )){
				if ( !registeredPlugins.get(plugin).equals(plugInLayer) ){
					JOptionPane alert = new JOptionPane("Reuse of Plugin name '" + plugin +
							"' in plugin layers: \n" +
							registeredPlugins.get(plugin) + " \n" +
							plugInLayer + " \n" +
							"Please fix the problem!");
					
					JDialog dialog = alert.createDialog(null, "");
					int w = dialog.getWidth();
					int h = dialog.getHeight();
					dialog.setLocation((x / 2) - (w / 2), (y / 2) - (h / 2));
					dialog.setVisible(true);
					System.exit(-1);
				} 
			} else{
				registeredPlugins.put(plugin, plugInLayer);
			}
		} else{
			Logger.Log(LogLevel.DEBUG, "Plugin (" + plugin + ", " + plugInLayer + ") is set to invisible");
		}
		
	}
	
	private Map<String, String> getRegisteredPlugins(){
		return registeredPlugins;
	}

	private void readFields(SimGuiPlugin plugin, Field[] fields, String plugInLayer, boolean isSuperClass ) {
		
		// Skip invisible plugins
		if ( !plugin.isVisible() && !plugin.makeFieldsGlobal() ){
			return;
		}
		
		SimProp property;
		
		try {
			for (Field field : fields ) {
				
				Annotation[] a = field.getAnnotations();
				for (Annotation element : a) {
					
					boolean isGlobal = false;
					if (element.annotationType() == BoolSimulationProperty.class) {
						BoolSimulationProperty annotation = field
								.getAnnotation(BoolSimulationProperty.class);
						property = new BoolProp();
						if (annotation != null) {
							// property.setId(f.getName());
							property.setId(annotation.propertykey());
							isGlobal = isSuperClass || annotation.global() || plugin.makeFieldsGlobal();
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
							}else{
								property.setPluginID("");
							}
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							if (plugin.makeFieldsGlobal()){
								property.setPluginLayerID(plugInLayer);
							} else {
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}
							property.setEnable_requirements(annotation.enable_requirements());
							((BoolProp) property).setValue(annotation.value());
							property.setEnable(true);
							property.isGlobal(isGlobal);
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else if (element.annotationType() == IntSimulationProperty.class) {
						IntSimulationProperty annotation = field
								.getAnnotation(IntSimulationProperty.class);
						property = new IntProp();
						if (annotation != null) {
							isGlobal = isSuperClass || annotation.global() || plugin.makeFieldsGlobal();
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
							}else{
								property.setPluginID("");
							}
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							if (plugin.makeFieldsGlobal()){
								property.setPluginLayerID(plugInLayer);
							} else {
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							((IntProp) property).setMinValue(annotation.min());
							((IntProp) property).setMaxValue(annotation.max());
							property.setValue(annotation.value());
							property.setEnable(true);
							property.isGlobal(true);
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else if (element.annotationType() == FloatSimulationProperty.class) {
						FloatSimulationProperty annotation = field
								.getAnnotation(FloatSimulationProperty.class);
						property = new FloatProp();
						if (annotation != null) {
							isGlobal = isSuperClass || annotation.global() || plugin.makeFieldsGlobal();
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
							}else{
								property.setPluginID("");
							}
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							if (plugin.makeFieldsGlobal()){
								property.setPluginLayerID(plugInLayer);
							} else {
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}
							property.setEnable_requirements(annotation.enable_requirements());
							((FloatProp) property).setMinValue(annotation.min());
							((FloatProp) property).setMaxValue(annotation.max());
							((FloatProp) property).setValue(annotation.value());
							property.setEnable(true);
							property.isGlobal(isGlobal);
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else if (element.annotationType() == DoubleSimulationProperty.class) {
						DoubleSimulationProperty annotation = field
								.getAnnotation(DoubleSimulationProperty.class);
						property = new DoubleProp();
						if (annotation != null) {
							isGlobal = isSuperClass || annotation.global() || plugin.makeFieldsGlobal();
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
							}else{
								property.setPluginID("");
							}
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							if (plugin.makeFieldsGlobal()){
								property.setPluginLayerID(plugInLayer);
							} else {
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}
							property.setEnable_requirements(annotation.enable_requirements());
							((DoubleProp) property).setMinValue(annotation.min());
							((DoubleProp) property).setMaxValue(annotation.max());
							((DoubleProp) property).setValue(annotation.value());
							property.setEnable(true);
							property.isGlobal(isGlobal);
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else if (element.annotationType() == StringSimulationProperty.class) {
						StringSimulationProperty annotation = field
								.getAnnotation(StringSimulationProperty.class);
						property = new StringProp();
						if (annotation != null) {
							isGlobal = isSuperClass || annotation.global() || plugin.makeFieldsGlobal();
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
							}else{
								property.setPluginID("");
							}
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							if (plugin.makeFieldsGlobal()){
								property.setPluginLayerID(plugInLayer);
							} else {
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}
							property.setEnable_requirements(annotation.enable_requirements());
							((StringProp) property).setValue(annotation.value());
							((StringProp) property).setPossibleValues(annotation.possibleValues());
							property.setEnable(true);
							property.isGlobal(isGlobal);
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else { 
						Logger.Log(LogLevel.ERROR, this + "Bad property type for field" + field.getName() );
						continue;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private String getPluginLayer(String pluginName) {
		
		if ( registeredPlugins.containsKey(pluginName) ){
			return registeredPlugins.get(pluginName);
		}
		Logger.Log(LogLevel.ERROR, "Plugin " + pluginName + " is unknown");
		return null;
	}

	public void scanPlugins() {
		
		// Get the display names of plugin layers
		String[] pluginLayer = layerMapConfigNameToDisplayName.keySet().toArray(new String[0]);
		for (int i = 0; i < layerMapConfigNameToDisplayName.size(); i++) {
			this.pluginLayerMap[i] = new HashMap<String, String>();
		}
			
		for ( Entry<String, String> entry : getRegisteredPlugins().entrySet() ) {
			
			String name = entry.getKey();
			String layer = entry.getValue();
			
			boolean insertFlag = false;
			for (int i = 0; i < layerMapDisplayNameToConfigName.size(); i++){
				
				// Append plugins config name and corresponding layers display name 
				// if plugin's layer matches
				if ( layer.equals(pluginLayer[i]) ){
					this.pluginLayerMap[i].put( name, layer );
					insertFlag = true;
					numberOfPluginLayers = Math.max(numberOfPluginLayers, i+1);
				}
			}
			
			if ( !insertFlag ) {
				Logger.Log(LogLevel.ERROR, "No such plugin layer: " + layer );
			}
		}
		
		// Static part, non real plugins
		this.nonPluginLayerMap[numberOfNonPluginLayers] = new HashMap<String, String>();
		this.nonPluginLayerMap[numberOfNonPluginLayers].put( "test", "RECODING_SCHEME" );
		numberOfNonPluginLayers++;
	}
	
	public int getNumberOfPluginLayers(){
		return numberOfPluginLayers;
	}
	
	public int getNumberOfNonPluginLayers(){
		return numberOfNonPluginLayers;
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

	public List<SimProp> getGlobalSimPropertiesByPluginLayer(String pluginLayer) {
		
		// TODO: order simproperties here
		
		List<SimProp> simPropertiesInANamespace = new LinkedList<SimProp>();
		
		for ( String key : properties.keySet() ) {
			SimProp simProp = properties.get(key);
			
			if ( simProp.isSuperclass() && simProp.getPluginLayerID().equals(
							this.layerMapDisplayNameToConfigName.get(pluginLayer)
							) ){
				simPropertiesInANamespace.add( simProp );
			}
		}
		
		return simPropertiesInANamespace;
	}
	
	public List<SimProp> getSimPropertiesByPluginOrPluginLayer(String pluginName, String pluginLayer) {
		
		// TODO: order simproperties here
		
		List<SimProp> simPropertiesInANamespace = new LinkedList<SimProp>();
		
		for ( String key : properties.keySet() ) {
			SimProp simProp = properties.get(key);
			
			if ( simProp.getPluginID().equals(pluginName) ||
					( simProp.isSuperclass() && simProp.getPluginLayerID().equals(
							this.layerMapDisplayNameToConfigName.get(pluginLayer)
							)) ){
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
	
	public void setActivePluginsMapped(String plugLayer, String selectedPlugin) {
		Logger.Log(LogLevel.DEBUG, "Set mapped " + pluginNameToConfigName(plugLayer) + " plugin to " + selectedPlugin);
		activePlugins.put(configNameToPluginName(plugLayer), selectedPlugin);
		activePluginsMapped.put(plugLayer, selectedPlugin);
	}
	
	public Map<String, String> getActivePlugins(boolean mapped) {
		if (mapped){
			return activePluginsMapped;
		}else{
			return activePlugins;	
		}
	}

	public List<String> getPluginLevels() {
		return new LinkedList<String>(layerMapDisplayNameToConfigName.keySet());
	}

	public String pluginNameToConfigName(String pluginLayer) {
		return layerMapDisplayNameToConfigName.get(pluginLayer);
	}
	
	public String configNameToPluginName(String pluginName) {
		return layerMapConfigNameToDisplayName.get(pluginName);
	}

	public List<String> getConfigNamesForPluginLayers() {
		
		List<String> configNamesForPluginLayers = new LinkedList<String>();
		for ( Entry<String, String> entry : layerMapConfigNameToDisplayName.entrySet() )
		{
			configNamesForPluginLayers.add( entry.getKey() );
		}
		return configNamesForPluginLayers;
	}
	
	public String toString(){
		
		String tree = "";
		for ( String layer : layerMapDisplayNameToConfigName.keySet() ){
			System.err.println( layerMapDisplayNameToConfigName.get(layer) );
			for ( String prop : properties.keySet() ){
				if ( properties.get(prop).getPluginID().equals("") && properties.get(prop).isSuperclass() && properties.get(prop).getPluginLayerID().equals(layerMapDisplayNameToConfigName.get(layer)) ){
					System.err.println("(" + prop + ")" );
				}
			}
			for ( String plugin : registeredPlugins.keySet() ){
				if ( registeredPlugins.get(plugin).equals(layerMapDisplayNameToConfigName.get(layer))){
					System.err.println("----" + plugin);
					for ( String prop : properties.keySet() ){
						if ( properties.get(prop).getPluginID().equals(plugin)){
							System.err.println("------" + prop );
						}
					}
				}
			}
		}
		
		return tree;
	}

	public String[] getPluginsInLayer( String pluginLayer ) {
		
		// TODO: order jcombobox items for accordion entries here!
		
		List<String> tmp = new LinkedList<>();
		
		for ( String layer : layerMapDisplayNameToConfigName.keySet() ){
			if ( layer.equals( pluginLayer )){
				for ( String plugin : registeredPlugins.keySet() ){
					if ( registeredPlugins.get(plugin).equals(layerMapDisplayNameToConfigName.get(layer))){
						tmp.add(plugin);
					}
				}
			}
		}
		
		String[] ret = new String[tmp.size()];
		ret = tmp.toArray(ret);
		return ret;
	}
}
