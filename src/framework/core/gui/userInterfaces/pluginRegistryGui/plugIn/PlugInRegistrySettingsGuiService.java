package framework.core.gui.userInterfaces.pluginRegistryGui.plugIn;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;

import framework.core.config.Paths;
import framework.core.gui.model.ResourceType;
import framework.core.gui.model.StaticFunctionBean;
import framework.core.gui.model.XMLAttribute;
import framework.core.gui.model.XMLPart;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.model.XMLResourceContainer;
import framework.core.gui.services.AvailableResourcesDAO;
import framework.core.gui.services.PlugInSettingsFilterService;
import framework.core.gui.services.XmlGuiDescriptorService;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch.DescriptionPanel;
import framework.core.gui.userInterfaces.mainGui.StatusBar;
import framework.core.gui.userInterfaces.pluginRegistryGui.RegistryGuiService;
import framework.core.gui.userInterfaces.pluginRegistryGui.plugIn.staticFunctionBranches.MultipleStaticFunctionBoxGuiService;
import framework.core.gui.userInterfaces.pluginRegistryGui.plugIn.staticFunctionBranches.StaticFunctionBranchGuiService;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.PopUpPrompt;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * PlugInRegistrySettingsGuiService is the service that creates the configuration panels for the
 * selected Plugin on demand and by converting the possibleValuesPlugInRegistry.xml file
 * 
 * @author Marius Fink
 * @version 17.11.2012
 */
public class PlugInRegistrySettingsGuiService extends RegistryGuiService implements XMLPartGUIContainer {

	private StaticFunctionBranchGuiService sfRequirementBranch;
	private StaticFunctionBranchGuiService sfCapabilityBranch;
	private static final String PATH_TO_REQUIREMENT = GUIText.getText("pathToPlugInRequirements");
	private static final String PATH_TO_CAPABILITY = GUIText.getText("pathToPlugInCapabilities");
	private JPanel wrapper;
	private XMLResourceContainer bean;

	/**
	 * Constructor of PlugInRegistrySettingsGuiService
	 * 
	 * @param container
	 *            the container with name and xml resource of the
	 */
	public PlugInRegistrySettingsGuiService(XMLResourceContainer container) {
		super(container.getHome() + "/" + GUIText.getText("plugInSettingsXml"),
				ResourceType.PLUGIN,
				container,
				new XmlGuiDescriptorService().createCompleteGuiDescription(container.getHome(),
						new XMLResource(Paths.PLUGIN_REGISTRY_GUI_DESCRIPTOR_XML_FILE_PATH),
						GUIText.getText("plugInSettingsXml")));
		bean = container;
		init();
	}

	/**
	 * Initializes.
	 */
	private void init() {
		guiService.addBottomButton(GUIText.getText("unregisterPlugIn"), ImageLoader.loadIcon("delete"),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						unregister();
					}
				});

		guiService.addBottomButton(GUIText.getText("registerPlugIn"), ImageLoader.loadIcon("add"),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						register();
					}
				});

		guiService.addBottomButton(GUIText.getText("save"), ImageLoader.loadIcon("save"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

		sfRequirementBranch = new StaticFunctionBranchGuiService(StaticFunctionBranchGuiService.REQUIREMENTS_BRANCH);
		guiService.addIndividualTab(sfRequirementBranch.getComponent(), GUIText.getText("staticFunctionRequirements"));

		sfCapabilityBranch = new StaticFunctionBranchGuiService(StaticFunctionBranchGuiService.CAPABILITY_BRANCH);
		guiService.addIndividualTab(sfCapabilityBranch.getComponent(), GUIText.getText("staticFunctionCapabilities"));

		JPanel infoPanel = new DescriptionPanel(GUIText.getText("editingPlugInX", name));
		wrapper = new JPanel(new BorderLayout());
		wrapper.add(guiService.createPanel(), BorderLayout.CENTER);
		wrapper.add(infoPanel, BorderLayout.NORTH);
	}

	/**
	 * @return the panel with the settings of the specified plug in
	 */
	public Component getPanel() {
		return wrapper;
	}

	/**
	 * Register this resource
	 */
	public void register() {
		AvailableResourcesDAO svc = new AvailableResourcesDAO();
		if (!svc.isPlugInAlreadyRegistered(bean)) {
			try {
				svc.registerPlugIn(bean);
				StatusBar.getInstance().showStatus(GUIText.getText("successfullyRegistered", name, "Plug-in"));
			} catch (IOException e1) {
				PopUpPrompt.displayIOExceptionMessage(e1);
				StatusBar.getInstance().showStatus(GUIText.getText("failedRegister", name, "Plug-in"));
			}
		} else {
			StatusBar.getInstance().showStatus(GUIText.getText("plugInAlreadyRegistered", name));
		}
	}

	/**
	 * Unregister this resource
	 */
	public void unregister() {
		AvailableResourcesDAO svc = new AvailableResourcesDAO();
		if (svc.isPlugInAlreadyRegistered(bean)) {
			try {
				svc.unregisterResourceById(bean.getId());
				StatusBar.getInstance().showStatus(GUIText.getText("successfullyUnregistered", name, "Plug-in"));
			} catch (Exception e1) {
				PopUpPrompt.displayIOExceptionMessage(e1);
				StatusBar.getInstance().showStatus(GUIText.getText("failedToUnregister", name, "Plug-in"));
			}
		} else {
			StatusBar.getInstance().showStatus(GUIText.getText("plugInNotRegistered", name));
		}
	}

	@Override
	protected List<XMLPartGUIContainer> getAdditionalXMLPartContainer() {
		List<XMLPartGUIContainer> containers = new LinkedList<XMLPartGUIContainer>();
		containers.add(PlugInRegistrySettingsGuiService.this);
		return containers;
	}

	@Override
	public Collection<XMLPart> getCurrentXMLParts() {
		List<XMLPart> parts = new LinkedList<XMLPart>();

		Collection<XMLPart> capParts = extractPartsFromTo(sfCapabilityBranch, PATH_TO_CAPABILITY, 0);
		Collection<XMLPart> reqParts = extractPartsFromTo(sfRequirementBranch, PATH_TO_REQUIREMENT, capParts.size());

		parts.addAll(capParts);
		parts.addAll(reqParts);

		return parts;
	}

	private Collection<XMLPart> extractPartsFromTo(StaticFunctionBranchGuiService svc, String path, int start) {
		start++;
		List<XMLPart> parts = new LinkedList<XMLPart>();
		for (Entry<Integer, MultipleStaticFunctionBoxGuiService> entry : svc.getBoxServices().entrySet()) {
			MultipleStaticFunctionBoxGuiService guiService = entry.getValue();
			for (StaticFunctionBean bean : guiService.getStaticFunctions()) {
//				XMLAttribute p = new XMLAttribute();
//				p.setAttributeName("source");
//				p.setLocation(path + "[" + start + "]");
//				p.setValue(bean.getHome() + "/" + GUIText.getText("staticFunctionSettingsXml"));
//				parts.add(p); HOTFIX - NO! WE LOAD THEM BY HAND!

				XMLAttribute idAttr = new XMLAttribute();
				idAttr.setAttributeName("id");
				idAttr.setLocation(path + "[" + start + "]");
				idAttr.setValue(bean.getId());
				parts.add(idAttr);
				start++;
			}
		}

		return parts;
	}

	@Override
	public void setCurrentXMLParts(Collection<XMLPart> toBeDisplayed) {
		PlugInSettingsFilterService filter = new PlugInSettingsFilterService(toBeDisplayed);

		List<StaticFunctionBean> reqBeans = filter.filterForRequirements();
		List<StaticFunctionBean> capBeans = filter.filterForCapabilities();

		// deligate to branches!
		sfRequirementBranch.addStaticFunctions(reqBeans);
		sfCapabilityBranch.addStaticFunctions(capBeans);
	}
}
