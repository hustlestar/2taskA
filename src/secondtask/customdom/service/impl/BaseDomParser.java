package secondtask.customdom.service.impl;


import secondtask.customdom.entity.Document;
import secondtask.customdom.entity.Element;
import secondtask.customdom.entity.TagType;
import secondtask.customdom.service.DomParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Hustler on 27.09.2016.
 */
public class BaseDomParser implements DomParser {

    private Document document = new Document();

    @Override
    public Document getDocument() {
        return document;
    }

    private StringBuffer stringBuffer = new StringBuffer();

    /**
     * Метод используется для прочтения документа
     */
    public void parse(FileInputStream inputStream) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (reader.ready()) {
                stringBuffer.append(reader.readLine());
                stringBuffer.append("\n");
            }
            //System.out.println(stringBuffer);
            detectXMLTag(stringBuffer);
        } catch (IOException ex) {
            System.out.println("bad input stream" + ex.getCause());
        } catch (XMLInvalidException e) {
            System.out.println("bad xml my friend" + e.getCause());
        }
    }

    /**
     * Метод используется для поиска вхождения в документ < > и формирования из них пар
     */
    private void detectXMLTag(StringBuffer stringBuffer) throws XMLInvalidException {
        //Pattern pattern = Pattern.compile()
        List<Pair> pairList = new ArrayList<>();

        int openIndex = stringBuffer.indexOf("<");
        int closeIndex = stringBuffer.indexOf(">");
        pairList.add(new Pair(openIndex, closeIndex));

        while (openIndex != -1 && closeIndex != -1) {
            openIndex = stringBuffer.indexOf("<", openIndex + 1);
            closeIndex = stringBuffer.indexOf(">", closeIndex + 1);
            if (openIndex > closeIndex) throw new XMLInvalidException();
            else if (openIndex >= 0 && closeIndex >= 0) pairList.add(new Pair(openIndex, closeIndex));
            //System.out.println(openIndex + "---------" + closeIndex);
        }
        //System.out.println(pairList.get(pairList.size() - 1).getClose());
        createElements(stringBuffer, pairList);
    }

    /**
     * Метод используется для создания элементов, находя теги
     */
    private void createElements(StringBuffer stringBuffer, List<Pair> pairList) {
        int openIndex;
        int closeIndex;
        ArrayList<Element> elements = new ArrayList<>();
        for (int i = 0; i < pairList.size(); i++) {
            openIndex = pairList.get(i).getOpen();
            closeIndex = pairList.get(i).getClose();

            String[] tagNames = stringBuffer.subSequence(openIndex + 1, closeIndex).toString().split(" ");
            //System.out.println(tagNames[0]);
            Element element = new Element();
            element.setName(tagNames[0]);
            if (tagNames[0].matches("/[a-zA-Z0-9:-]+")) element.setType(TagType.CLOSE);
            else if (tagNames[0].startsWith("?")) element.setType(TagType.DECLARATION);
            else if (tagNames[0].startsWith("!--") && tagNames[tagNames.length - 1].endsWith("--"))
                element.setType(TagType.COMMENT);
            else if (tagNames[0].matches("[a-zA-Z0-9:-]+") && !tagNames[tagNames.length - 1].endsWith("/"))
                element.setType(TagType.OPEN);
            else if (tagNames[0].matches("[a-zA-Z0-9:-]+") && tagNames[tagNames.length - 1].endsWith("/"))
                element.setType(TagType.SELFCLOSE);

            if (tagNames.length > 1 && (element.getType() == TagType.OPEN || element.getType() == TagType.SELFCLOSE)) {
                try {
                    attributes(tagNames, element);
                } catch (XMLInvalidException e) {
                    System.out.println("Атрибуты тега не соответсвуют правилам XML в элементе " + e.getMessage());
                }
            }
            //неопознанные типы
            elements.add(element);
        }
        /*for (Element element : elements) {
            System.out.println(element.getName() + element.getType());
        }*/
        fillElements(elements, pairList);
    }

    /**
     * Метод используется для добавления аттрибутов к прочитанным элементам
     */
    private void attributes(String[] tagNames, Element element) throws XMLInvalidException {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i < tagNames.length; i++) {
            stringBuffer.append(tagNames[i]);

            if (stringBuffer.toString().trim().matches("[a-zA-Z:-]+[=][\"][\\wа-яА-Я /.:-]+[\"]")) {
                String[] att = stringBuffer.toString().split("=\"");
                element.addAttribute(att[0].trim(), att[1].substring(0, att[1].length() - 1).trim());
                stringBuffer.delete(0, stringBuffer.length());
            } else if (stringBuffer.toString().trim().matches("[a-zA-Z:-]+[=][\'][\\wа-яА-Я /.:-]+[\']")) {
                String[] att = stringBuffer.toString().split("=\"");
                element.addAttribute(att[0].trim(), att[1].substring(0, att[1].length() - 1).trim());
                stringBuffer.delete(0, stringBuffer.length());
            } else if (i == (tagNames.length - 1)) {
                //System.out.println(stringBuffer);
                if (stringBuffer.toString().trim().matches("[a-zA-Z:-]+[=][\'][\\wа-яА-Я /.:-]+[\'][/]")) {
                    String[] att = stringBuffer.toString().split("=\"");
                    element.addAttribute(att[0].trim(), att[1].substring(0, att[1].length() - 2).trim());
                    stringBuffer.delete(0, stringBuffer.length());
                } else if (stringBuffer.toString().trim().matches("[a-zA-Z:-]+[=][\"][\\wа-яА-Я /.:-]+[\"][/]")) {
                    String[] att = stringBuffer.toString().split("=\"");
                    element.addAttribute(att[0].trim(), att[1].substring(0, att[1].length() - 2).trim());
                    stringBuffer.delete(0, stringBuffer.length());
                } else throw new XMLInvalidException(element.getName());
            } else if (!stringBuffer.toString().matches("[a-zA-Z:-]+[=][\"][\\wа-яА-Я /.:-]+[\"]")) {
                stringBuffer.append(" ");
            } else if (!stringBuffer.toString().matches("[a-zA-Z:-]+[=][\'][\\wа-яА-Я /.:-]+[\']")) {
                stringBuffer.append(" ");
            }
        }
    }

    /**
     * Метод используется для заполения элемента значениями и задания дочерних элементов
     */

    private void fillElements(ArrayList<Element> elements, List<Pair> pairList) {
        int lastOpenTag = 0;
        int count = 0;
        Deque<Integer> deque = new ArrayDeque<>();
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).getType() == TagType.OPEN) {
                if (count == 0) {
                    count++;
                    lastOpenTag = i;
                    deque.add(lastOpenTag);
                    //System.out.println(elements.get(i).getName() + elements.get(i).getType());
                } else if (elements.get(i).getType() == TagType.OPEN && !elements.get(lastOpenTag).isClosed() && count != 0) {
                    elements.get(lastOpenTag).addChildren(elements.get(i));
                    count++;
                    lastOpenTag = i;
                    deque.add(lastOpenTag);
                    //System.out.println(elements.get(i).getName() + elements.get(i).getType());
                } else if (elements.get(i).getType() == TagType.OPEN && elements.get(lastOpenTag).isClosed()) {
                    count++;
                    lastOpenTag = i;
                    int stillOpen = deque.getLast();
                    elements.get(stillOpen).addChildren(elements.get(i));
                    deque.add(lastOpenTag);
                    //System.out.println(elements.get(i).getName() + elements.get(i).getType());
                } else if (elements.get(i).getType() == TagType.SELFCLOSE && !elements.get(lastOpenTag).isClosed() && count != 0) {
                    elements.get(lastOpenTag).addChildren(elements.get(i));
                    //System.out.println(elements.get(i).getName() + elements.get(i).getType());
                } else if (elements.get(i).getType() == TagType.SELFCLOSE && elements.get(lastOpenTag).isClosed()) {
                    int stillOpen = deque.getLast();
                    elements.get(stillOpen).addChildren(elements.get(i));
                    //System.out.println(elements.get(i).getName() + elements.get(i).getType());
                }

            } else if (elements.get(i).getType() == TagType.CLOSE) {
                int x = deque.pollLast();
                elements.get(x).setClosed(true);
                //System.out.println(elements.get(i).getName() + x + elements.get(i).getType());
            }
        }
        for (int i = 0; i < elements.size(); i++) {
            if (i < elements.size() - 2 && elements.get(i).getType() == TagType.OPEN)
                elements.get(i).setValue(stringBuffer.subSequence(pairList.get(i).getClose() + 1, pairList.get(i + 1).getOpen()).toString().trim());
            else elements.get(i).setValue("");
        }
        try {
            finalCheck(elements);
        } catch (XMLInvalidException e) {
            System.out.println("BAD XML, есть незакрыте теги!!!!!!!!!!!!!!!!!!!");
        }
        /*System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Element element : elements) {
            System.out.println(element.getName()+ " " + element.getValue());
        }*/
        document.setDoc(elements);
    }

    /**
     * Метод используется для проверки, остались ли незакрытые теги
     */

    private void finalCheck(ArrayList<Element> elements) throws XMLInvalidException {
        for (Element element : elements) {
            if (element.getType() == TagType.OPEN && !(element.isClosed())) {
                System.out.println(element.getName());
                throw new XMLInvalidException();
            } else {

            }
        }
        System.out.println("OK xml file");
    }
}
