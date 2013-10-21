package framework.core.gui.userInterfaces.pluginRegistryGui.repertoryPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import framework.core.gui.util.visualUtilities.GUIColors;

public class RepertoryPanelGui {
    private String title;
    private ImageIcon icon;
    private JPanel choosingPanel;
    private JPanel buttonPanel;
    private JPanel contentWrapper;

    public RepertoryPanelGui(String title, ImageIcon icon) {
	this.title = title;
	this.icon = icon;

	init();
    }

    private void init() {
	choosingPanel = new JPanel(new BorderLayout());

	choosingPanel
		.setBorder(BorderFactory.createLineBorder(GUIColors.getColorByName("metagray"), 1));
	JPanel titlePanel = new JPanel(new BorderLayout());
	JPanel titlePanelContent = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
	titlePanelContent.add(new JLabel(icon));
	titlePanelContent.add(new JLabel(title));
	titlePanel.add(titlePanelContent, BorderLayout.CENTER);

	buttonPanel = new JPanel(new FlowLayout());
	titlePanel.add(buttonPanel, BorderLayout.EAST);
	choosingPanel.add(titlePanel, BorderLayout.NORTH);

	contentWrapper = new JPanel(new BorderLayout());
	JScrollPane scrollpane = new JScrollPane(contentWrapper);
	scrollpane.setBorder(null);
	scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	choosingPanel.add(scrollpane, BorderLayout.CENTER);
    }

    public JButton addButton(String tooltip, ImageIcon icon) {
	JButton button = new JButton();
	button.setToolTipText(tooltip);
	button.setMargin(new Insets(1, 1, 1, 1));
	button.setIcon(icon);
	buttonPanel.add(button);
	button.updateUI();

	return button;
    }
    
    public void setContent(Component content) {
	content.setBackground(GUIColors.getColorByName("workspacewhite"));
	contentWrapper.removeAll();
	contentWrapper.add(content);
	contentWrapper.updateUI();
    }

    public Component getComponent() {
	return choosingPanel;
    }

}
