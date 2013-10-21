package framework.core.gui.userInterfaces.configurationGui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import framework.core.config.Paths;
import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLResource;
import framework.core.gui.services.StaticFunctionAdditionService;
import framework.core.gui.services.XMLUserDialogLoadingService;
import framework.core.gui.services.XMLUserDialogSavingService;
import framework.core.gui.userInterfaces.configurationGui.compositionBranch.CompositionBranchGuiService;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.GenericXMLGUIService;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * ConfigurationGUIService is the service for the general config gui
 * 
 * @author Marius Fink
 * @version 18.09.2012
 */
public class ConfigurationGuiService {

	private GenericXMLGUIService configSvc;

	/**
	 * Constructor of ConfigurationGUIService
	 */
	public ConfigurationGuiService() {
		init();
	}

	/**
	 * Initializes this component by instantiating the generic xml gui
	 */
	private void init() {
		// SVC: Main settings (Generic)
		XMLResource xml_settings = new XMLResource(Paths.GENERAL_CONFIG_GUI_DESCRIPTOR_XML_FILE_PATH);
		xml_settings.setRootNode("guiInfo/possibleGuiValues/");
		configSvc = new GenericXMLGUIService(xml_settings);
		final String rootNode = xml_settings.getPropertyAsString("rootNode");

		// This is an additional branch
		CompositionBranchGuiService dependencyBranchPanel = new CompositionBranchGuiService();
		// adds this branch to the branches of the GenericXMlGuiService to be treated equally while
		// saving and loading.
		configSvc.addXmlPartGenerator(dependencyBranchPanel);
		// now add the gui
		configSvc.addIndividualTab(dependencyBranchPanel.getJPanel(), GUIText.getText("composition"));

		configSvc.addBottomButton(GUIText.getText("loadConfiguration"), ImageLoader.loadIcon("open"),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						XMLUserDialogLoadingService loader = new XMLUserDialogLoadingService();
						Collection<XMLPart> newXmlParts = loader.loadNewXmlParts();
						configSvc.setCurrentXMLParts(newXmlParts);
					}
				});

		configSvc.addBottomButton(GUIText.getText("saveAs"), ImageLoader.loadIcon("export"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Collection<XMLPart> currentXMLParts = configSvc.getCurrentXMLParts();
				StaticFunctionAdditionService.addAvailableStaticFunctions(currentXMLParts);
				
				XMLUserDialogSavingService saveAsSaver = new XMLUserDialogSavingService(currentXMLParts,
						rootNode);
				saveAsSaver.save();
			}
		});

		configSvc.addBottomButton(GUIText.getText("save"), ImageLoader.loadIcon("save"), new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Collection<XMLPart> currentXMLParts = configSvc.getCurrentXMLParts();
				StaticFunctionAdditionService.addAvailableStaticFunctions(currentXMLParts);
				
				XMLUserDialogSavingService staticSaver = new XMLUserDialogSavingService(currentXMLParts,
						rootNode, Paths.GENERAL_CONFIG_XML_FILE_PATH);
				staticSaver.save();
			}
		});
		
		//Initially load the current config
		XMLResource current = new XMLResource(Paths.GENERAL_CONFIG_XML_FILE_PATH);
		configSvc.setCurrentXMLParts(current.getAllXMLParts());
	}

	/**
	 * @return the panel
	 */
	public Component createPanel() {
		return configSvc.createPanel();
	}
}
