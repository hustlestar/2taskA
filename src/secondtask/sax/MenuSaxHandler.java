package secondtask.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hustler on 24.09.2016.
 */
public class MenuSaxHandler extends DefaultHandler {
    private List<Submenu> submenuList = new ArrayList<>();
    private Submenu submenu;
    private List<Dish> dishList = new ArrayList<>();
    private Dish dish;
    private List<String> description = new ArrayList<>();
    private List<Integer> price = new ArrayList<>();
    private StringBuilder text;
    private static int count;


    public List<Submenu> getDishList() {
        return submenuList;
    }

    public void startDocument() throws SAXException {
        System.out.println("Parsing started.");
    }

    public void endDocument() throws SAXException {
        System.out.println("Parsing ended.");
    }

    public void characters(char[] buffer, int start, int length) {
        text.append(buffer, start, length);
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        text = new StringBuilder();
        if (qName.equals("dish")) {
            dish = new Dish();
            System.out.print("dishcr" + count++);
        } else if (qName.equals("description")) {
            if (attributes.getValue("id") == null || attributes.getValue("id").equals("0")) {
                description = new ArrayList<>();
            }
        } else if (qName.equals("price")) {
            if (attributes.getValue("id") == null || attributes.getValue("id").equals("1")) {
                price = new ArrayList<>();
            }
        } else if (qName.equals("submenu")) {
            submenu = new Submenu();
            dishList = new ArrayList<>();
            submenu.setName(attributes.getValue("name"));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        MenuTagName tagName = MenuTagName.valueOf(qName.toUpperCase().replace("-", "_"));

        switch (tagName) {
            case PHOTO:
                dish.setPhoto(text.toString());
                break;
            case TITLE:
                dish.setTitle(text.toString());
                break;
            case DESCRIPTION:
                dish.setDescription(description);
                description.add(text.toString());
                break;
            case WEIGHT:
                dish.setWeight(text.toString());
                break;
            case PRICE:
                dish.setPrice(price);
                price.add(Integer.parseInt(text.toString()));
                break;
            case DISH:
                dishList.add(dish);
                System.out.println("dish");
                dish = null;
                break;
            case SUBMENU:
                System.out.println("xxx");
                submenu.setList(dishList);
                submenuList.add(submenu);
                dishList = null;
                submenu = null;
                break;
        }
    }

}
