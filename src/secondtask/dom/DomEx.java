package secondtask.dom;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomEx {

    public static void main(String[] args) throws SAXException, IOException {
        DOMParser parser = new DOMParser();
        parser.parse("src\\res\\menu.xml");
        Document document = parser.getDocument();
        Element root = document.getDocumentElement();

        List<Submenu> submenuList = new ArrayList<>();
        List<Dish> dishList = null;
        NodeList submenuNodes = root.getElementsByTagName("submenu");
        Submenu submenu = null;
        for (int i = 0; i < submenuNodes.getLength(); i++) {
            submenu = new Submenu();
            dishList = new ArrayList<>();
            Element submenuItem = (Element) submenuNodes.item(i);
            submenu.setName(submenuItem.getAttribute("name"));
            NodeList dishNodes = submenuItem.getElementsByTagName("dish");

            dishIterator(dishList, dishNodes);

            submenu.setList(dishList);
            submenuList.add(submenu);
        }

        printSubmenuList(submenuList);
    }

    private static void dishIterator(List<Dish> dishList, NodeList dishNodes) {
        Dish dish;
        List<String> description;
        List<Integer> price;
        for (int j = 0; j < dishNodes.getLength(); j++) {
            dish = new Dish();
            Element dishElement = (Element) dishNodes.item(j);

            dish.setPhoto(getSingleChild(dishElement, "photo").getTextContent().trim());

            dish.setTitle(getSingleChild(dishElement, "title").getTextContent().trim());

            description = new ArrayList<>();
            for (Element descrip : getAllChild(dishElement, "description")) {
                description.add(descrip.getTextContent().trim());
            }
            dish.setDescription(description);

            dish.setWeight(getSingleChild(dishElement, "weight").getTextContent().trim());

            price = new ArrayList<>();
            for (Element pri : getAllChild(dishElement, "price")) {
                price.add(Integer.parseInt(pri.getTextContent().trim()));
            }
            dish.setPrice(price);

            dishList.add(dish);
        }
    }

    private static Element getSingleChild(Element element, String childName) {
        NodeList nlist = element.getElementsByTagName(childName);
        Element child = (Element) nlist.item(0);
        return child;
    }

    private static List<Element> getAllChild(Element element, String childName) {
        NodeList nlist = element.getElementsByTagName(childName);
        List<Element> childList = new ArrayList<>();
        for (int i = 0; i < nlist.getLength(); i++) {
            childList.add((Element) nlist.item(i));
        }
        return childList;
    }

    private static void printSubmenuList(List<Submenu> submenuList) {
        for (Submenu sss : submenuList) {
            System.out.println(sss.getName());
            for (Dish dish : sss.getList()) {
                System.out.println(dish.getTitle() + " - " + dish.getDescription() + " - " + dish.getPrice());
            }
        }
    }
}
