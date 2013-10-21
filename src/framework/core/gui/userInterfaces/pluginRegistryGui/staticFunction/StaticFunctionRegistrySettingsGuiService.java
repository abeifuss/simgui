package framework.core.gui.userInterfaces.pluginRegistryGui.staticFunction;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import framework.core.config.Paths;
import framework.core.gui.model.ResourceType;
import framework.core.gui.model.XMLPartGUIContainer;
import framework.core.gui.model.XMLResource;
import framework.core.gui.model.XMLResourceContainer;
import framework.core.gui.services.AvailableResourcesDAO;
import framework.core.gui.services.XmlGuiDescriptorService;
import framework.core.gui.userInterfaces.genericXMLGeneratedGUI.genericXMLGUIBranch.DescriptionPanel;
import framework.core.gui.userInterfaces.mainGui.StatusBar;
import framework.core.gui.userInterfaces.pluginRegistryGui.RegistryGuiService;
import framework.core.gui.util.GUIText;
import framework.core.gui.util.components.PopUpPrompt;
import framework.core.gui.util.visualUtilities.ImageLoader;

/**
 * StaticFunctionRegistrySettingsGuiService is the service that creates the configuration panels for
 * the selected Plugin on demand and by converting the possibleValuesStaticFunctionRegistry.xml file
 * 
 * @author Marius Fink
 * @version 17.11.2012
 */
public class StaticFunctionRegistrySettingsGuiService extends RegistryGuiService {

	private XMLResourceContainer bean;

	/**
	 * Creates a new registry gui service for static functions
	 * 
	 * @param container
	 *            the static function bean
	 */
	public StaticFunctionRegistrySettingsGuiService(XMLResourceContainer container) {
		super(container.getHome() + "/" + GUIText.getText("staticFunctionSettingsXml"),
				ResourceType.STATIC_FUNCTION,
				container,
				new XmlGuiDescriptorService().createCompleteGuiDescription(
						container.getHome(),
						new XMLResource(Paths.STATIC_FUNCTION_REGISTRY_GUI_DESCRIPTOR_XML_FILE_PATH),
						"StaticFunctionSettings.xml")
		);
		this.bean = container;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see framework.core.gui.userInterfaces.pluginRegistryGui.RegistryGuiService#getPanel()
	 */
	@Override
	public Component getPanel() {
		guiService.addBottomButton(GUIText.getText("unregisterStaticFunction"), ImageLoader.loadIcon("delete"),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						unregister();
					}
				});

		guiService.addBottomButton(GUIText.getText("registerStaticFunction"), ImageLoader.loadIcon("add"),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						register();
					}
				});

		guiService.addBottomButton(GUIText.getText("save"), ImageLoader.loadIcon("save"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});

		JPanel infoPanel = new DescriptionPanel(GUIText.getText("editingStaticFunctionX", name));
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.add(guiService.createPanel(), BorderLayout.CENTER);
		wrapper.add(infoPanel, BorderLayout.NORTH);
		return wrapper;
	}

	/**
	 * Unregister this resource
	 */
	public void unregister() {
		AvailableResourcesDAO svc = new AvailableResourcesDAO();
		if (svc.isStaticFunctionAlreadyRegistered(bean)) {
			try {
				svc.unregisterResourceById(bean.getId());
				StatusBar.getInstance().showStatus(GUIText.getText("successfullyUnregistered", name, "StaticFunction"));
			} catch (Exception e1) {
				PopUpPrompt.displayIOExceptionMessage(e1);
				StatusBar.getInstance().showStatus(GUIText.getText("failedToUnregister", name, "StaticFunction"));
			}
		} else {
			StatusBar.getInstance().showStatus(GUIText.getText("staticFunctionNotRegistered", name));
		}
	}

	/**
	 * Register this resource
	 */
	public void register() {
		AvailableResourcesDAO svc = new AvailableResourcesDAO();
		if (!svc.isStaticFunctionAlreadyRegistered(bean)) {
			try {
				svc.registerStaticFunction(bean);
				StatusBar.getInstance().showStatus(GUIText.getText("successfullyRegistered", name, "StaticFunction"));
			} catch (IOException e1) {
				PopUpPrompt.displayIOExceptionMessage(e1);
				StatusBar.getInstance().showStatus(GUIText.getText("failedRegister", name, "StaticFunction"));
			}
		} else {
			StatusBar.getInstance().showStatus(GUIText.getText("staticFunctionAlreadyRegistered", name));
		}
	}

	@Override
	protected List<XMLPartGUIContainer> getAdditionalXMLPartContainer() {
		// since there are no additional static branches, there is nothing to add.
		return new LinkedList<XMLPartGUIContainer>();
	}
}
