package secondtask.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StaXEx {
    public static void main(String[] args) throws FileNotFoundException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try {
            InputStream input = new FileInputStream("src\\res\\menu.xml");

            XMLStreamReader reader = inputFactory.createXMLStreamReader(input);

            List<Submenu> menu = process(reader);

            for (Submenu submenu : menu) {
                System.out.println(submenu.getName());
                for (Dish dish : submenu.getList()) {
                    System.out.println(dish.getTitle() + " - " + dish.getDescription());
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static List<Submenu> process(XMLStreamReader reader) throws XMLStreamException {
        List<Submenu> menu = new ArrayList<Submenu>();
        List<Dish> dishList = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<Integer> price = new ArrayList<>();
        Submenu submenu = null;
        Dish dish = null;
        MenuTagName elementName = null;

        while (reader.hasNext()) {

            int type = reader.next();

            switch (type) {
                case XMLStreamConstants.START_ELEMENT:
                    elementName = MenuTagName.getElementTagName(reader.getLocalName());
                    switch (elementName) {
                        case DISH:
                            dish = new Dish();
                            break;
                        case DESCRIPTION:
                            String id = reader.getAttributeValue(null, "ID");
                            if (id == null || id.equals("0")) {
                                description = new ArrayList<>();
                            }
                            break;
                        case PRICE:
                            id = reader.getAttributeValue(null, "ID");
                            if (id == null || id.equals("1")) {
                                price = new ArrayList<>();
                            }
                            break;
                        case SUBMENU:
                            submenu = new Submenu();
                            dishList = new ArrayList<>();
                            submenu.setName(reader.getAttributeValue(null, "name"));
                            break;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (text.isEmpty()) {
                        break;
                    }
                    switch (elementName) {
                        case PHOTO:
                            dish.setPhoto(text);
                            break;
                        case TITLE:
                            dish.setTitle(text);
                            break;
                        case DESCRIPTION:
                            dish.setDescription(description);
                            description.add(text);
                            break;
                        case WEIGHT:
                            dish.setWeight(text);
                            break;
                        case PRICE:
                            dish.setPrice(price);
                            price.add(Integer.parseInt(text));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    elementName = MenuTagName.getElementTagName(reader.getLocalName());
                    switch (elementName) {
                        case DISH:
                            dishList.add(dish);
                            dish = null;
                            break;
                        case SUBMENU:
                            submenu.setList(dishList);
                            menu.add(submenu);
                            dishList = null;
                            submenu = null;
                            break;
                    }
            }
        }
        return menu;
    }
}
