package gui.customElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import log.LogLevel;
import log.Logger;

@SuppressWarnings("serial")
public class SimHelpMenuPanel extends JPanel implements HyperlinkListener{

	String navigation;
	InputStream is;
	
	private static Element lastHyperlinkElementEntered;
	static JEditorPane htmlPane;
	
	private static SimHelpMenuPanel instance = null;
	
	private SimHelpMenuPanel(){
		init();
	}
	
	public static SimHelpMenuPanel getInstance() {
		if (instance == null) {
			instance = new SimHelpMenuPanel();
		}
		return instance;
	}
	
	private void init(){
		
		String navigation = NavigationService.getMenu();
		
		try {
			htmlPane = new JEditorPane();
			
			htmlPane.setEditable(false);
			
			HTMLEditorKit kit = new HTMLEditorKit();
			htmlPane.setEditorKit(kit);
			
			StyleSheet styleSheet = kit.getStyleSheet();
			styleSheet.addRule("a { color: blue; text-decoration: none; }");
			
	        htmlPane.addHyperlinkListener(this);
	        initListeners();
	        
	        Document doc = kit.createDefaultDocument();
	        htmlPane.setDocument(doc);
	        htmlPane.setText(navigation);
			
			setLayout(new BorderLayout());
			setBackground(Color.WHITE);
			System.out.println(navigation);
			
			add(new JScrollPane(htmlPane), BorderLayout.CENTER);
		} catch (Exception ex) {
			// ex.printStackTrace();
			System.out.println("problem has occured" + ex.getMessage());
		}
	}

	private void initListeners() {
		htmlPane.addMouseListener( new MouseAdapter() {
			@Override
            public void mouseExited(MouseEvent e) {
                removeHyperlinkHighlight();
            }
		});
		
		htmlPane.addMouseMotionListener( new MouseMotionListener(){
			public void mouseDragged(MouseEvent e) {
            }

			public void mouseMoved(MouseEvent e) {
                Point pt = new Point(e.getX(), e.getY());
                int pos = htmlPane.viewToModel(pt);
                if (pos >= 0) {
                    HTMLDocument hdoc = (HTMLDocument) htmlPane.getDocument();
                    Element elem = hdoc.getCharacterElement(pos);
                    if (elem != null) {
                        AttributeSet a = elem.getAttributes();
                        AttributeSet anchor = (AttributeSet) a.getAttribute(HTML.Tag.A);
                        
                        if (elem != lastHyperlinkElementEntered ) {
                            removeHyperlinkHighlight();
                        }
                        if (anchor != null ) {
                            highlightHyperlink(elem);
                        } 
                    }
                }
            }
		});
	}
	
	private static void removeHyperlinkHighlight() {
        changeColor(lastHyperlinkElementEntered, Color.BLUE);
        lastHyperlinkElementEntered = null;
    }
	
	private static void highlightHyperlink(Element hyperlinkElement) {
        if (hyperlinkElement != lastHyperlinkElementEntered) {
            lastHyperlinkElementEntered = hyperlinkElement;
            changeColor(hyperlinkElement, new Color(128, 0, 128));
        }
    }
	
	private static void changeColor(Element el, Color color) {
        if (lastHyperlinkElementEntered != null) {
            HTMLDocument doc = (HTMLDocument) htmlPane.getDocument();
            int start = el.getStartOffset();
            int end = el.getEndOffset();
            StyleContext ss = doc.getStyleSheet();
            Style style = ss.addStyle("HighlightedHyperlink", null);
            style.addAttribute(StyleConstants.Foreground, color);
            doc.setCharacterAttributes(start, end - start, style, false);
        }
    }

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getInputEvent().getModifiers() == InputEvent.BUTTON1_MASK){
			SimHelpContentPanel.getInstance().loadURL(e.getDescription()+"index.html");
			Logger.Log(LogLevel.DEBUG, "Hyperlink Event for " + e.getDescription());
		}
	}

}