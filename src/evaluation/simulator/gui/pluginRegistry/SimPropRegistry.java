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
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

import evaluation.simulator.gui.pluginRegistry.InjectionParser;
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

	@SuppressWarnings("rawtypes")
	private final List<Vector> deferList = new LinkedList<>();
	
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
	
	private final Map<String, Integer>  layerMapDisplayNameToOrder = new HashMap<String, Integer>();
	
	// TODO: Merge both order functions
	public Map<String, Integer> getLayerMapDisplayNameToOrder() {
		return layerMapDisplayNameToOrder;
	}

	public Map<String, Integer> getLayerMapConfigNameToOrder() {
		return layerMapConfigNameToOrder;
	}

	private final Map<String, Integer>  layerMapConfigNameToOrder = new HashMap<String, Integer>();
	
	private SimPropRegistry() {
		numberOfPluginLayers = 0;
		numberOfNonPluginLayers = 0;
		
		this.scanForPluginProperties();
		
		// TODO: Obsolete or even deprecated?
		// this.scanPlugins();
		
		Logger.Log(LogLevel.DEBUG, "Now it's time to process defered plugins");
		this.processDefered();
		this.scanForStaticProperties();
		this.toString();
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

			// 
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
		} else if ( !this.properties.containsKey(s.getPropertyID()) && isGlobal ) {
			Logger.Log(LogLevel.DEBUG,  s.getPropertyID() + " with " + s.getPluginLayerID() + " is forced to be global");
			s.isGlobal(true);
			this.properties.put(s.getPropertyID(), s);
		} else {
			Logger.Log(LogLevel.DEBUG, "Register property (" + s.getPropertyID() + ", " + s.getPluginID() + ", " + s.getPluginLayerID() + ")");
			s.isGlobal(false);
			this.properties.put(s.getPropertyID(), s);
		}
	}
	
	/**
	 * Scans static simulation properties. Only handles PropertyAnnoations with inject parameter.
	 */
	private void scanForStaticProperties() {
		
		Reflections reflections = new Reflections(
				ClasspathHelper.forPackage("evaluation.simulator.plugins"),
				new FieldAnnotationsScanner());
		
		Set<Field> fields = reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.IntSimulationProperty.class);
		fields.addAll(reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.FloatSimulationProperty.class));
		fields.addAll(reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.DoubleSimulationProperty.class));
		fields.addAll(reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.StringSimulationProperty.class));
		fields.addAll(reflections.getFieldsAnnotatedWith(evaluation.simulator.annotations.simulationProperty.BoolSimulationProperty.class));
	
		List<Class<? extends Annotation> > annotationTypes = new LinkedList<Class<? extends Annotation> >();
		annotationTypes.add( IntSimulationProperty.class );
		annotationTypes.add( BoolSimulationProperty.class );
		annotationTypes.add( FloatSimulationProperty.class );
		annotationTypes.add( DoubleSimulationProperty.class );
		annotationTypes.add( StringSimulationProperty.class );
		
		SimProp property;
		boolean globalProperty = true;
		for ( Field field : fields ){
				Annotation[] a = field.getAnnotations();
				for (Annotation element : a) {
					
					if (element.annotationType() == BoolSimulationProperty.class) {
						
						BoolSimulationProperty annotation = field.getAnnotation(BoolSimulationProperty.class);
						if ( !annotation.inject().equals("") ){
							
							InjectionParser injection = new InjectionParser( annotation.inject(), annotation.propertykey() );
							
							String layerDisplayName = injection.getLayerDisplayName();
							String layerConfigName = injection.getLayerConfigName();
							String pluginDisplayName = injection.getPluginDisplayName();
							String pluginConfigName = injection.getPluginConfigName();
							int layerPosition = injection.getLayerPosition();
							int pluginPosition = injection.getPluginPosition();
							
							property = new BoolProp();
							property.setId(annotation.propertykey());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayerID(layerConfigName);
							property.setPluginID(pluginConfigName);
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setValue(annotation.value());
							property.setEnable(true);
							
							globalProperty = annotation.global() || injection.isGlobalProperty();
							property.isGlobal(globalProperty);

							((BoolProp) property).setValue(annotation.value());
							
							properties.put(property.getPropertyID(), property);
							
							if ( !layerMapDisplayNameToConfigName.containsKey(layerDisplayName)){
								Logger.Log( LogLevel.DEBUG , "Register plugin layer (" + layerConfigName + ", " + layerDisplayName + ")");
								Logger.Log( LogLevel.DEBUG, "Set position for injected plugin layer " + layerConfigName + " to " + layerPosition);
								layerMapDisplayNameToConfigName.put(layerDisplayName, layerConfigName);
								layerMapConfigNameToDisplayName.put(layerConfigName, layerDisplayName);
								layerMapDisplayNameToOrder.put( layerDisplayName, layerPosition );
								layerMapConfigNameToOrder.put( layerConfigName, layerPosition );
							}
							
							if ( !globalProperty ){
								registerPlugin(pluginDisplayName, layerDisplayName, true);
							}
						}
					}
					
					if (element.annotationType() == IntSimulationProperty.class) {
						
						IntSimulationProperty annotation = field.getAnnotation(IntSimulationProperty.class);
						if ( !annotation.inject().equals("") ){
							
							InjectionParser injection = new InjectionParser( annotation.inject(), annotation.propertykey() );
							
							String layerDisplayName = injection.getLayerDisplayName();
							String layerConfigName = injection.getLayerConfigName();
							String pluginDisplayName = injection.getPluginDisplayName();
							String pluginConfigName = injection.getPluginConfigName();
							int layerPosition = injection.getLayerPosition();
							int pluginPosition = injection.getPluginPosition();
							
							property = new IntProp();
							property.setId(annotation.propertykey());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayerID(layerConfigName);
							property.setPluginID(pluginConfigName);
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							globalProperty = annotation.global() || injection.isGlobalProperty();
							property.isGlobal(globalProperty);
;
							((IntProp) property).setValue(annotation.value());
							((IntProp) property).setMinValue(annotation.min());
							((IntProp) property).setMaxValue(annotation.max());
							
							properties.put(property.getPropertyID(), property);
							
							if ( !layerMapDisplayNameToConfigName.containsKey(layerDisplayName)){
								Logger.Log( LogLevel.DEBUG , "Register plugin layer (" + layerConfigName + ", " + layerDisplayName + ")");
								Logger.Log( LogLevel.DEBUG, "Set position for injected plugin layer " + layerConfigName + " to " + layerPosition);
								layerMapDisplayNameToConfigName.put(layerDisplayName, layerConfigName);
								layerMapConfigNameToDisplayName.put(layerConfigName, layerDisplayName);
								layerMapDisplayNameToOrder.put( layerDisplayName, layerPosition );
								layerMapConfigNameToOrder.put( layerConfigName, layerPosition );
							}
							
							if ( !globalProperty ){
								registerPlugin(pluginDisplayName, layerDisplayName, true);
							}
						}
					}
					
					if (element.annotationType() == FloatSimulationProperty.class) {
						
						FloatSimulationProperty annotation = field.getAnnotation(FloatSimulationProperty.class);
						if ( !annotation.inject().equals("") ){
							
							InjectionParser injection = new InjectionParser( annotation.inject(), annotation.propertykey() );
							
							String layerDisplayName = injection.getLayerDisplayName();
							String layerConfigName = injection.getLayerConfigName();
							String pluginDisplayName = injection.getPluginDisplayName();
							String pluginConfigName = injection.getPluginConfigName();
							int layerPosition = injection.getLayerPosition();
							int pluginPosition = injection.getPluginPosition();
							
							property = new FloatProp();
							property.setId(annotation.propertykey());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayerID(layerConfigName);
							property.setPluginID(pluginConfigName);
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							globalProperty = annotation.global() || injection.isGlobalProperty();
							property.isGlobal(globalProperty);
							
							((FloatProp) property).setMinValue(annotation.min());
							((FloatProp) property).setMaxValue(annotation.max());
							((FloatProp) property).setValue(annotation.value());

							properties.put(property.getPropertyID(), property);
							
							if ( !layerMapDisplayNameToConfigName.containsKey(layerDisplayName)){
								Logger.Log( LogLevel.DEBUG , "Register plugin layer (" + layerConfigName + ", " + layerDisplayName + ")");
								Logger.Log( LogLevel.DEBUG, "Set position for injected plugin layer " + layerConfigName + " to " + layerPosition);
								layerMapDisplayNameToConfigName.put(layerDisplayName, layerConfigName);
								layerMapConfigNameToDisplayName.put(layerConfigName, layerDisplayName);
								layerMapDisplayNameToOrder.put( layerDisplayName, layerPosition );
								layerMapConfigNameToOrder.put( layerConfigName, layerPosition );
							}
							
							if ( !globalProperty ){
								registerPlugin(pluginDisplayName, layerDisplayName, true);
							}
						}
					}
					
					if (element.annotationType() == DoubleSimulationProperty.class) {
						
						DoubleSimulationProperty annotation = field.getAnnotation(DoubleSimulationProperty.class);
						if ( !annotation.inject().equals("") ){
							
							InjectionParser injection = new InjectionParser( annotation.inject(), annotation.propertykey() );
							
							String layerDisplayName = injection.getLayerDisplayName();
							String layerConfigName = injection.getLayerConfigName();
							String pluginDisplayName = injection.getPluginDisplayName();
							String pluginConfigName = injection.getPluginConfigName();
							int layerPosition = injection.getLayerPosition();
							int pluginPosition = injection.getPluginPosition();
							
							property = new DoubleProp();
							property.setId(annotation.propertykey());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayerID(layerConfigName);
							property.setPluginID(pluginConfigName);
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							globalProperty = annotation.global() || injection.isGlobalProperty();
							property.isGlobal(globalProperty);

							((DoubleProp) property).setMinValue(annotation.min());
							((DoubleProp) property).setMaxValue(annotation.max());
							((DoubleProp) property).setValue(annotation.value());

							properties.put(property.getPropertyID(), property);
							
							if ( !layerMapDisplayNameToConfigName.containsKey(layerDisplayName)){
								Logger.Log( LogLevel.DEBUG , "Register plugin layer (" + layerConfigName + ", " + layerDisplayName + ")");
								Logger.Log( LogLevel.DEBUG, "Set position for injected plugin layer " + layerConfigName + " to " + layerPosition);
								layerMapDisplayNameToConfigName.put(layerDisplayName, layerConfigName);
								layerMapConfigNameToDisplayName.put(layerConfigName, layerDisplayName);
								layerMapDisplayNameToOrder.put( layerDisplayName, layerPosition );
								layerMapConfigNameToOrder.put( layerConfigName, layerPosition );
							}
							
							if ( !globalProperty ){
								registerPlugin(pluginDisplayName, layerDisplayName, true);
							}
						}
					}
					
					if (element.annotationType() == StringSimulationProperty.class) {
						
						StringSimulationProperty annotation = field.getAnnotation(StringSimulationProperty.class);
						if ( !annotation.inject().equals("") ){
							
							InjectionParser injection = new InjectionParser( annotation.inject(), annotation.propertykey() );
							
							String layerDisplayName = injection.getLayerDisplayName();
							String layerConfigName = injection.getLayerConfigName();
							String pluginDisplayName = injection.getPluginDisplayName();
							String pluginConfigName = injection.getPluginConfigName();
							int layerPosition = injection.getLayerPosition();
							int pluginPosition = injection.getPluginPosition();
							
							property = new StringProp();
							property.setId(annotation.propertykey());
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							property.setPluginLayerID(layerConfigName);
							property.setPluginID(pluginConfigName);
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							globalProperty = annotation.global() || injection.isGlobalProperty();
							property.isGlobal(globalProperty);
							
							((StringProp) property).setValue(annotation.value());
							((StringProp) property).setPossibleValues(annotation.possibleValues());

							properties.put(property.getPropertyID(), property);
							
							if ( !layerMapDisplayNameToConfigName.containsKey(layerDisplayName)){
								Logger.Log( LogLevel.DEBUG , "Register plugin layer (" + layerConfigName + ", " + layerDisplayName + ")");
								Logger.Log( LogLevel.DEBUG, "Set position for injected plugin layer " + layerConfigName + " to " + layerPosition);
								layerMapDisplayNameToConfigName.put(layerDisplayName, layerConfigName);
								layerMapConfigNameToDisplayName.put(layerConfigName, layerDisplayName);
								layerMapDisplayNameToOrder.put( layerDisplayName, layerPosition );
								layerMapConfigNameToOrder.put( layerConfigName, layerPosition );
							}
							
							if ( !globalProperty ){
								registerPlugin(pluginDisplayName, layerDisplayName, true);
							}
						}
					}

				}
		}
		
//		Field[] fieldArray = fields.toArray( new Field[fields.size()]);
//		readFields(null, fieldArray, null, false);
	
	}

	// Scans plugin dependent simulation properties
	@SuppressWarnings("unused")
	public void scanForPluginProperties() {

		SimGuiPlugin plugin;

		// TODO: Seems not to work properly, scans all packages
		Reflections reflectionsPlugins = new Reflections(
				ClasspathHelper.forPackage("evaluation.simulator.plugins"),
				new TypeAnnotationsScanner());

		// Look for classes with PluginAnnotation
		Set<Class<?>> types = reflectionsPlugins.getTypesAnnotatedWith(evaluation.simulator.annotations.plugin.Plugin.class);

		for (Class<?> pluginClass : types) {

			Plugin pluginAnnotation = pluginClass.getAnnotation(Plugin.class);

			plugin = new SimGuiPlugin();
			plugin.setId(pluginClass.getName());
			plugin.setName(pluginAnnotation.pluginKey());
			plugin.setDocumentationURL(pluginAnnotation.documentationURL());
			plugin.setPluginLayer(pluginAnnotation.pluginLayerKey());
			plugin.isVisible(pluginAnnotation.vilible());
			plugin.isGlobal(pluginAnnotation.global());
			plugin.allowGlobalFields(pluginAnnotation.allowGlobalFields());

			// This is the direct superclass. The direct superclass does not need
			// to be annotated with @PluginSuperclass. It also can be annotated with
			// @Plugin but then it must have the same name
			Class<?> directSuperlass = pluginClass.getSuperclass();
			
			// Find the annotated superclass
			Class<?> pluginSuperclass = plugin.getPluginSuperclass( pluginClass );
			
			// If there is a plugin layer provided by the plugin annotation, a plugin can 
			// be registered drectly ( e.g. StopAndGoMessage.java ). 
			// Otherwise the we try to find the pluginSuperclasse an use its name ( e.g. StopAndGo.java ).
			boolean autodetectPluginLayer = pluginAnnotation.pluginLayerKey().equals("");
			
			if ( !autodetectPluginLayer ){
				Logger.Log(LogLevel.DEBUG, "Disable autodetect for plugin " + plugin.getName() );
				
				// we need this for invisible plugins
				plugin.setFallbackLayer(pluginAnnotation.pluginLayerKey());
				
				// Although autodetection is disabled, it is a good idea to check if there is a superclass 
				// with @PluginSuperclass annotation.
				// Reason: This superclass might have some properties! 
				if ( pluginSuperclass != null ) {
					
					PluginSuperclass pluginSuperclassAnnotation = pluginSuperclass.getAnnotation( PluginSuperclass.class );
					
					// prefer pluginLayer from @PluginSuperclass annotation
					// let's overwrite it!
					String pluginSuperclassLayerKey = pluginSuperclassAnnotation.layerKey();
					String pluginSuperclassLayerName = pluginSuperclassAnnotation.layerName();
					plugin.setPluginLayer( pluginSuperclassLayerKey );
					
					// we need this for invisible plugins
					plugin.setFallbackLayer(pluginSuperclassLayerKey);
					
					// Process PluginSuperclass fields if not already done
					if ( !layerMapDisplayNameToConfigName.containsKey( pluginSuperclassLayerName )){
						
						// We can/must read PluginSuperclass fields without registering a plugin
						readFields( plugin, pluginSuperclass.getDeclaredFields(), pluginSuperclassLayerKey, true );
						layerMapDisplayNameToConfigName.put( pluginSuperclassLayerName, pluginSuperclassLayerKey);
						layerMapConfigNameToDisplayName.put( pluginSuperclassLayerKey, pluginSuperclassLayerName );
					}
				}
				
				// We have to defer the plugin registration, since now it is not clear
				// if there is another class of the same plugin that has a PluginSuperclass,
				// which has higher priority when setting the display name of the layer.
				deferedReadFields( plugin, pluginClass.getDeclaredFields(), pluginAnnotation.pluginLayerKey(), false );
				
				// Now it is safe to register the plugin and read the fields
				// registerPlugin( plugin.getName(), plugin.getPluginLayer(), plugin.isVisible() );
				// readFields( plugin, pluginClass.getDeclaredFields(), pluginAnnotation.pluginLayerKey(), false );
			
			}else{ // AUTODETECT PLUGINLAYER (Lookup superclass)
				Logger.Log(LogLevel.DEBUG, "Enable autodetect for plugin " + plugin.getName() + ", PluginSuperclass=" + pluginSuperclass.getSimpleName());
				
				String layerDisplayName = "";
				String layerConfigName = "";
				
				// Process PluginSuperclass information
				if ( pluginSuperclass != null ){
					
					PluginSuperclass pluginSuperclassAnnotation = pluginSuperclass.getAnnotation( PluginSuperclass.class );
					
					// Case: Invalid inheritance
					// If the direct superclass has a normal plugin annotation, the
					// provided name must match the actual plugin name.
					if (directSuperlass.isAnnotationPresent( Plugin.class ) && 	
						 directSuperlass.getAnnotation( Plugin.class ).pluginKey() != pluginAnnotation.pluginKey() ){
							 
						Logger.Log( LogLevel.ERROR , "Check the annotation in class " + directSuperlass.getCanonicalName() +
								" it should be @Plugin( name = \""+ plugin.getName() +"\" ... ) or a valid @PluginSuperclass annotation.");
					}
					
					// Set the pluginLayer
					String pluginSuperclassLayerKey = pluginSuperclassAnnotation.layerKey();
					String pluginSuperclassLayerName = pluginSuperclassAnnotation.layerName();
					plugin.setPluginLayer( pluginSuperclassLayerKey );
					
					// we need this for invisible plugins
					plugin.setFallbackLayer(pluginSuperclassLayerKey);
					
					// Process PluginSuperclass fields if not already done
					if ( !layerMapDisplayNameToConfigName.containsKey( pluginSuperclassLayerName )){
						Logger.Log(LogLevel.DEBUG, "Register PluginSuperclass for " + pluginSuperclassLayerKey);
						
						// register the plugin layer manually
						Logger.Log(LogLevel.DEBUG, "Associate plugin layer (" + 
								pluginSuperclassLayerKey + ", " + pluginSuperclassLayerName + ")");
						
						readFields( plugin, pluginSuperclass.getDeclaredFields(), pluginSuperclassLayerKey, true );
						layerMapDisplayNameToConfigName.put( pluginSuperclassLayerName, pluginSuperclassLayerKey);
						layerMapConfigNameToDisplayName.put( pluginSuperclassLayerKey, pluginSuperclassLayerName );
						
						int layerPosition = pluginSuperclass.getAnnotation( PluginSuperclass.class ).position();
						
						Logger.Log( LogLevel.DEBUG, "Set position for plugin layer " + pluginSuperclassLayerKey + " to " + layerPosition);
						layerMapDisplayNameToOrder.put( pluginSuperclassLayerName, layerPosition );
						layerMapConfigNameToOrder.put( pluginSuperclassLayerKey, layerPosition );
						
						// PluginSuperclasses can provide fake plugins. Fake plugins will occur in the
						// jcombobox of the corresponding plugin level (see TopologyScript.java)
						String fakePlugins = pluginSuperclass.getAnnotation( PluginSuperclass.class ).fakePlugins();
						if ( !fakePlugins.equals("") ){
							String[] fakedPlugins = fakePlugins.split(",");
							for (int i = 0; i < fakedPlugins.length; i++ ){
								Logger.Log(LogLevel.DEBUG, "Inject fake plugin " + fakedPlugins[i] + " to " + layerConfigName );
								registerPlugin(fakedPlugins[i], pluginSuperclass.getAnnotation( PluginSuperclass.class ).layerKey(), true);
							}
						}
						
					}else{
						Logger.Log(LogLevel.DEBUG, plugin.getName() + " is caped by a superclass " + pluginSuperclass.getAnnotation( PluginSuperclass.class ).layerKey());
						registerPlugin( plugin.getName(), pluginSuperclass.getAnnotation( PluginSuperclass.class ).layerKey(), plugin.isVisible() );	
					}

					
				}
				else{
					// The plugin is not O.K.! Each plugin must be caped by a pluginSuperclass
					Logger.Log(LogLevel.ERROR, plugin.getName() + " is not caped by a superclass");
					System.exit(-1);
				}

				PluginSuperclass pluginSuperclassAnnotation = pluginSuperclass.getAnnotation( PluginSuperclass.class );
				String pluginSuperclassLayerName = pluginSuperclassAnnotation.layerName();
				
				deferedReadFields( plugin, pluginClass.getDeclaredFields(), layerConfigName, false );
			}
		}

		// call initial dependency-check for per plugin configurations
		DependencyChecker.checkAll(this);
	}

	
	private void deferedReadFields(SimGuiPlugin plugin, Field[] declaredFields, String pluginLayerKey, boolean isSuperclass) {
		
		Logger.Log(LogLevel.DEBUG, "Defer " + plugin.getName() );
		
		Vector<Object> deferInformation = new Vector<Object>();
		deferInformation.add( plugin );
		deferInformation.add( declaredFields );
		deferInformation.add( pluginLayerKey);
		deferInformation.add( isSuperclass );
		
		deferList.add( deferInformation );
	}
	
	private void processDefered(){
		
		for ( Vector<Object> deferInformation : deferList ){
			
			SimGuiPlugin plugin = (SimGuiPlugin) deferInformation.get( 0 );
			Field[] declaredFields = (Field[]) deferInformation.get( 1 );
			String pluginLayerKey = (String) deferInformation.get( 2 );
			boolean isSuperclass = (boolean) deferInformation.get( 3 );
			
			registerPlugin( plugin.getName(), plugin.getPluginLayer(), plugin.isVisible() );
			readFields( plugin, declaredFields, pluginLayerKey, isSuperclass );
		}
		
	}

	/**
	 * 
	 * Registers a plugin name with a plugin layer.
	 * 
	 * @param plugin A plugin name e.g. 
	 * @param plugInLayer A player name e.g. 
	 * @param isVisible A boolean which specifies whether the plugin name will be
	 * registered with a plugin layer (ture) or not (false). If a plugin is not
	 * registerd with a plugin layer, it will be invisible whithin the gui. 
	 */
	private void registerPlugin(String plugin, String plugInLayer, boolean isVisible) {
		if ( isVisible ){
			
			GraphicsDevice graphicsDevice = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int x = graphicsDevice.getDisplayMode().getWidth();
			int y = graphicsDevice.getDisplayMode().getHeight();
			
			// Check if this plugin is already known for a different pluginlayer. 
			// In this case show an alert an exit. The Pluginprogrammer has to fix ist
			if ( registeredPlugins.containsKey(plugin) ){
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
				
			} 
			// Otherwise, register the plugin the pluginlayer.
			// Plugins which are registered to a pluginlayer will occur in the 
			// pluginlayer's corresponging jcombobox!
			// Plugins which are not registered are not visible in the gui.
			else { 
				Logger.Log(LogLevel.DEBUG, "Register plugin (" + plugin + ", " + plugInLayer + ")");
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
		if ( !plugin.isVisible() && !(plugin.isGlobal() || plugin.allowGlobalFields()) ){
			Logger.Log(LogLevel.DEBUG, 
					plugin.getName() + " is ignored due to isVisible=" + 
					plugin.isVisible()	+ " and makeFieldsGlobal=" + 
					plugin.isGlobal() + " and allowGlobalFields=" + plugin.allowGlobalFields());
			return;
		}
		
		SimProp property;
		
		try {
			for (Field field : fields ) {
				
				Annotation[] a = field.getAnnotations();
				for (Annotation element : a) {
					
					boolean isGlobal = false;
					if (element.annotationType() == BoolSimulationProperty.class) {
						BoolSimulationProperty annotation = field.getAnnotation(BoolSimulationProperty.class);
						property = new BoolProp();
						if (annotation != null) {
							if ( !annotation.inject().equals("") ){
								Logger.Log(LogLevel.DEBUG, "Skip " + annotation.propertykey() + " from " + plugin.getName() + " has injection annotation");
								continue;
							}

							// Uninjected properties are global under following conditions:
							// They are PluginSuperclass properties OR the if plugin allows global Properties.
							// If the plugin allows global properties each property which is annotated as global
							// will become a global plugin layer property ( e.g. LineChartPlotterCf.java )
							isGlobal = isSuperClass || ( plugin.isGlobal() || annotation.global() );
							
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
								property.isGlobal( false );
							}else{
								property.setPluginID("");
								property.isGlobal( true );
							}
							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " is global=" +
									property.isGlobal() + " so plugin is " + property.getPluginID() );
							
							property.isSuperclass( isSuperClass );
							property.setPluginLayerID(plugInLayer);
							
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							
							// This is why we have to defer all readFields() calls
							// Explanation: We call getPluginLayer() but the internal list which is used by this
							// function builds up dynamically. Therefore, it is possible to get an empty string or
							// null, if we have bad timing!
							// CAUTION: do not touch these expressions, we make use of lazy evaluation
							String possiblePluginLayer = getPluginLayer(plugin.getName());
							if ( possiblePluginLayer != null && !possiblePluginLayer.isEmpty() ){
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}else{
								if ( !plugin.getFallbackLayer().isEmpty() ){
									Logger.Log(LogLevel.DEBUG, "Fallback to " + plugin.getFallbackLayer());
									property.setPluginLayerID(plugin.getFallbackLayer());
								}else{
									Logger.Log(LogLevel.ERROR, "Can not determine the Layer for " + property.getName() + "! Reason unknown");
									System.exit(-1);
								}
							}

							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " set layer to " + getPluginLayer(plugin.getName()));
							
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							((BoolProp) property).setValue(annotation.value());
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else if (element.annotationType() == IntSimulationProperty.class) {
						IntSimulationProperty annotation = field
								.getAnnotation(IntSimulationProperty.class);
						property = new IntProp();
						if (annotation != null) {
							if ( !annotation.inject().equals("") ){
								Logger.Log(LogLevel.DEBUG, "Skip " + annotation.propertykey() + " from " + plugin.getName() + " has injection annotation");
								continue;
							}

							// Uninjected properties are global under following conditions:
							// They are PluginSuperclass properties OR the if plugin allows global Properties.
							// If the plugin allows global properties each property which is annotated as global
							// will become a global plugin layer property ( e.g. LineChartPlotterCf.java )
							isGlobal = isSuperClass || ( plugin.isGlobal() || annotation.global() );
							
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
								property.isGlobal( false );
							}else{
								property.setPluginID("");
								property.isGlobal( true );
							}
							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " is global=" +
									property.isGlobal() + " so plugin is " + property.getPluginID() );
							
							property.isSuperclass( isSuperClass );
							property.setPluginLayerID(plugInLayer);
							
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							
							// This is why we have to defer all readFields() calls
							// Explanation: We call getPluginLayer() but the internal list which is used by this
							// function builds up dynamically. Therefore, it is possible to get an empty string or
							// null, if we have bad timing!
							// CAUTION: do not touch these expressions, we make use of lazy evaluation
							String possiblePluginLayer = getPluginLayer(plugin.getName());
							if ( possiblePluginLayer != null && !possiblePluginLayer.isEmpty() ){
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}else{
								if ( !plugin.getFallbackLayer().isEmpty() ){
									Logger.Log(LogLevel.DEBUG, "Fallback to " + plugin.getFallbackLayer());
									property.setPluginLayerID(plugin.getFallbackLayer());
								}else{
									Logger.Log(LogLevel.ERROR, "Can not determine the Layer for " + property.getName() + "! Reason unknown");
									System.exit(-1);
								}
							}

							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " set layer to " + getPluginLayer(plugin.getName()));
							
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							((IntProp) property).setValue(annotation.value());
							((IntProp) property).setMinValue(annotation.min());
							((IntProp) property).setMaxValue(annotation.max());
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else if (element.annotationType() == FloatSimulationProperty.class) {
						FloatSimulationProperty annotation = field
								.getAnnotation(FloatSimulationProperty.class);
						property = new FloatProp();
						if (annotation != null) {
							if ( !annotation.inject().equals("") ){
								Logger.Log(LogLevel.DEBUG, "Skip " + annotation.propertykey() + " from " + plugin.getName() + " has injection annotation");
								continue;
							}

							// Uninjected properties are global under following conditions:
							// They are PluginSuperclass properties OR the if plugin allows global Properties.
							// If the plugin allows global properties each property which is annotated as global
							// will become a global plugin layer property ( e.g. LineChartPlotterCf.java )
							isGlobal = isSuperClass || ( plugin.isGlobal() || annotation.global() );
							
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
								property.isGlobal( false );
							}else{
								property.setPluginID("");
								property.isGlobal( true );
							}
							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " is global=" +
									property.isGlobal() + " so plugin is " + property.getPluginID() );
							
							property.isSuperclass( isSuperClass );
							property.setPluginLayerID(plugInLayer);
							
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							
							// This is why we have to defer all readFields() calls
							// Explanation: We call getPluginLayer() but the internal list which is used by this
							// function builds up dynamically. Therefore, it is possible to get an empty string or
							// null, if we have bad timing!
							// CAUTION: do not touch these expressions, we make use of lazy evaluation
							String possiblePluginLayer = getPluginLayer(plugin.getName());
							if ( possiblePluginLayer != null && !possiblePluginLayer.isEmpty() ){
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}else{
								if ( !plugin.getFallbackLayer().isEmpty() ){
									Logger.Log(LogLevel.DEBUG, "Fallback to " + plugin.getFallbackLayer());
									property.setPluginLayerID(plugin.getFallbackLayer());
								}else{
									Logger.Log(LogLevel.ERROR, "Can not determine the Layer for " + property.getName() + "! Reason unknown");
									System.exit(-1);
								}
							}

							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " set layer to " + getPluginLayer(plugin.getName()));
							
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							((FloatProp) property).setMinValue(annotation.min());
							((FloatProp) property).setMaxValue(annotation.max());
							((FloatProp) property).setValue(annotation.value());
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else if (element.annotationType() == DoubleSimulationProperty.class) {
						DoubleSimulationProperty annotation = field
								.getAnnotation(DoubleSimulationProperty.class);
						property = new DoubleProp();
						if (annotation != null) {
							if ( !annotation.inject().equals("") ){
								Logger.Log(LogLevel.DEBUG, "Skip " + annotation.propertykey() + " from " + plugin.getName() + " has injection annotation");
								continue;
							}

							// Uninjected properties are global under following conditions:
							// They are PluginSuperclass properties OR the if plugin allows global Properties.
							// If the plugin allows global properties each property which is annotated as global
							// will become a global plugin layer property ( e.g. LineChartPlotterCf.java )
							isGlobal = isSuperClass || ( plugin.isGlobal() || annotation.global() );
							
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
								property.isGlobal( false );
							}else{
								property.setPluginID("");
								property.isGlobal( true );
							}
							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " is global=" +
									property.isGlobal() + " so plugin is " + property.getPluginID() );
							
							property.isSuperclass( isSuperClass );
							property.setPluginLayerID(plugInLayer);
							
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							
							// This is why we have to defer all readFields() calls
							// Explanation: We call getPluginLayer() but the internal list which is used by this
							// function builds up dynamically. Therefore, it is possible to get an empty string or
							// null, if we have bad timing!
							// CAUTION: do not touch these expressions, we make use of lazy evaluation
							String possiblePluginLayer = getPluginLayer(plugin.getName());
							if ( possiblePluginLayer != null && !possiblePluginLayer.isEmpty() ){
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}else{
								if ( !plugin.getFallbackLayer().isEmpty() ){
									Logger.Log(LogLevel.DEBUG, "Fallback to " + plugin.getFallbackLayer());
									property.setPluginLayerID(plugin.getFallbackLayer());
								}else{
									Logger.Log(LogLevel.ERROR, "Can not determine the Layer for " + property.getName() + "! Reason unknown");
									System.exit(-1);
								}
							}

							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " set layer to " + getPluginLayer(plugin.getName()));
							
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							((DoubleProp) property).setMinValue(annotation.min());
							((DoubleProp) property).setMaxValue(annotation.max());
							((DoubleProp) property).setValue(annotation.value());
						}
						this.register(property, isSuperClass, isGlobal, plugInLayer);
					} else if (element.annotationType() == StringSimulationProperty.class) {
						StringSimulationProperty annotation = field
								.getAnnotation(StringSimulationProperty.class);
						property = new StringProp();
						if (annotation != null) {
							if ( !annotation.inject().equals("") ){
								Logger.Log(LogLevel.DEBUG, "Skip " + annotation.propertykey() + " from " + plugin.getName() + " has injection annotation");
								continue;
							}

							// Uninjected properties are global under following conditions:
							// 1. The property is a PluginSuperclass property.
							// The corresponding plugin is global.
							// The property itself is global ( e.g. LineChartPlotterCf.java )
							isGlobal = isSuperClass || ( plugin.isGlobal() || annotation.global() );
							
							property.setId(annotation.propertykey());
							if ( !isGlobal ){
								property.setPluginID(plugin.getName());
								property.isGlobal( false );
							}else{
								property.setPluginID("");
								property.isGlobal( true );
							}
							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " is global=" +
									property.isGlobal() + " so plugin is " + property.getPluginID() );
							
							property.isSuperclass( isSuperClass );
							property.setPluginLayerID(plugInLayer);
							
							property.setName(annotation.name());
							property.setDescription(annotation.description());
							property.setTooltip(annotation.propertykey());
							
							// This is why we have to defer all readFields() calls
							// Explanation: We call getPluginLayer() but the internal list which is used by this
							// function builds up dynamically. Therefore, it is possible to get an empty string or
							// null, if we have bad timing!
							// CAUTION: do not touch these expressions, we make use of lazy evaluation
							String possiblePluginLayer = getPluginLayer(plugin.getName());
							if ( possiblePluginLayer != null && !possiblePluginLayer.isEmpty() ){
								property.setPluginLayerID(getPluginLayer(plugin.getName()));
							}else{
								if ( !plugin.getFallbackLayer().isEmpty() ){
									Logger.Log(LogLevel.DEBUG, "Fallback to " + plugin.getFallbackLayer());
									property.setPluginLayerID(plugin.getFallbackLayer());
								}else{
									Logger.Log(LogLevel.ERROR, "Can not determine the Layer for " + property.getName() + "! Reason unknown");
									System.exit(-1);
								}
							}

							Logger.Log(LogLevel.DEBUG, annotation.propertykey() + " set layer to " + getPluginLayer(plugin.getName()));
							
							property.setEnable_requirements(annotation.enable_requirements());
							property.setValue_requirements(annotation.value_requirements());
							property.setEnable(true);
							
							((StringProp) property).setValue(annotation.value());
							((StringProp) property).setPossibleValues(annotation.possibleValues());
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
		
		numberOfNonPluginLayers++;
	}
	
	public int getNumberOfPluginLayers(){
		return numberOfPluginLayers;
	}
	
	public int getNumberOfNonPluginLayers(){
		return numberOfNonPluginLayers;
	}

	public void setValue(String key, Object arg0) {

		Logger.Log(LogLevel.DEBUG, "key " + key + " arg " + arg0);
		
		if (arg0.getClass() == Boolean.class) {
			//System.out.println("Boolean");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == Float.class) {
			//System.out.println("Float");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == Double.class) {
			//System.out.println("Double");
			this.properties.get(key).setValue(arg0);
		}else if (arg0.getClass() == Integer.class) {
			//System.out.println("Integer");
			this.properties.get(key).setValue(arg0);
		} else if (arg0.getClass() == String.class) {
			//System.out.println("String");
			this.properties.get(key).setValue(arg0);

		} else {

		}
	}

	public List<SimProp> getGlobalSimPropertiesByPluginLayer(String pluginLayer) {
		
		// TODO: order simproperties here
		
		List<SimProp> simPropsInPluginLayer = new LinkedList<SimProp>();
		
		for ( String key : properties.keySet() ) {
			SimProp simProp = properties.get(key);
			
			if ( ( simProp.isSuperclass() || simProp.isGlobal() ) 
					&& simProp.getPluginLayerID().equals(this.layerMapDisplayNameToConfigName.get(pluginLayer)
							) ){
				
				simPropsInPluginLayer.add( simProp );
			}
		}
		
		return simPropsInPluginLayer;
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
				if ( properties.get(prop).getPluginID().equals("") && 
						( properties.get(prop).isSuperclass() || properties.get(prop).isGlobal() ) && 
						properties.get(prop).getPluginLayerID().equals(layerMapDisplayNameToConfigName.get(layer)) ){
					System.err.println("(" + prop + ")" );
				}
			}
			for ( String plugin : registeredPlugins.keySet() ){
				if ( registeredPlugins.get(plugin).equals(layerMapDisplayNameToConfigName.get(layer))){
					System.err.println("----" + plugin);
					for ( String prop : properties.keySet() ){
						if ( properties.get(prop).getPluginID().equals(plugin) ){
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
