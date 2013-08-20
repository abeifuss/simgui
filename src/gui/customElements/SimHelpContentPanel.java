package gui.customElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import javax.swing.JPanel;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import log.LogLevel;
import log.Logger;

@SuppressWarnings("serial")
public class SimHelpContentPanel extends JPanel {

	XHTMLPanel panelx;
	
	private static SimHelpContentPanel instance = null;
	
	private SimHelpContentPanel(){
		init();
	}
	
	public static SimHelpContentPanel getInstance() {
		if (instance == null) {
			instance = new SimHelpContentPanel();
		}
		return instance;
	}
	
	private void init(){
		
		try {
			panelx = new XHTMLPanel();
			setLayout(new BorderLayout());
			setBackground(Color.WHITE);
			panelx.setDocument(new File("etc/html/index.html"));
			
			add(new FSScrollPane(panelx));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Some problem has occured" + ex.getMessage());
		}
		
	}
	
	public void loadURL(String url){
		Logger.Log(LogLevel.DEBUG, "Loading help-page: "+url );
		try {
			panelx.setDocument(new File(url));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
