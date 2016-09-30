package secondtask.customdom.entity;

import java.util.ArrayList;
/**
 * Created by Hustler on 27.09.2016.
 */
public class Document {
    private ArrayList<Element> doc;

    public Element getDocumentElement(){
        for (int i = 0; i < doc.size(); i++) {
            if (doc.get(i).getType()==TagType.OPEN) return doc.get(i);
        }
        return null;
    }

    public ArrayList<Element> getDoc() {
        return doc;
    }

    public void setDoc(ArrayList<Element> doc) {
        this.doc = doc;
    }

    public void printDoc(){
        for (Element element : doc) {
            if (element.getUsedTimes()==0){
                System.out.print(element);
                element.setUsedTimes(1);
            }
        }
    }
}
