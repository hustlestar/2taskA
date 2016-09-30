package secondtask.customdom.service;

import secondtask.customdom.entity.Document;
import secondtask.customdom.service.impl.BaseDomParser;
import secondtask.customdom.service.impl.DomFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Hustler on 27.09.2016.
 */
public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        BaseDomParser domParser = DomFactory.getInstance().create();
        domParser.parse(new FileInputStream("src\\res\\menu.xml"));
        Document document = domParser.getDocument();
        //document.printDoc();
        System.out.println(document.getDocumentElement().getChildren());
    }
}
