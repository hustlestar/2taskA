package secondtask.sax;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.util.List;
/**
 * Created by Hustler on 24.09.2016.
 */
public class SaxEx {
	public static void main(String[] args) throws SAXException, IOException{
	    XMLReader reader = XMLReaderFactory.createXMLReader();
	    MenuSaxHandler handler = new MenuSaxHandler();
	    reader.setContentHandler(handler);
	    reader.parse(new InputSource("src\\res\\menu.xml"));
	    
	    List<Submenu> submenuList = handler.getDishList();
	    	    
	    for(Submenu submenu : submenuList){
			for (Dish dish : submenu.getList()) {
				System.out.println(dish.getTitle()+" "+dish.getDescription());
			}
		}
	}
}
