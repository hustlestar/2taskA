package testformyparser;


import customdom.entity.Document;
import customdom.service.impl.BaseDomParser;
import customdom.service.impl.DomFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Hustler on 30.09.2016.
 */
public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        BaseDomParser domParser = DomFactory.getInstance().create();
        domParser.parse(new FileInputStream("src\\testformyparser\\res\\menu.xml"));
        Document document = domParser.getDocument();
        document.printDoc();
        //System.out.println(document.getDocumentElement().getChildren());
    }
}
