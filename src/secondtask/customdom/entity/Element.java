package secondtask.customdom.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hustler on 27.09.2016.
 */
public class Element {
    private String name;
    private String value;
    private TagType type;
    private List<Element> children = new ArrayList<>();
    private boolean closed;
    private int usedTimes = 0;
    private LinkedHashMap<String, String> attributes = new LinkedHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TagType getType() {
        return type;
    }

    public void setType(TagType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Element> getChildren() {
        return children;
    }

    public void addChildren(Element children) {
        this.children.add(children);
    }

    public boolean hasChildren() {
        return children.size() != 0;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(int usedTimes) {
        this.usedTimes = usedTimes;
    }

    public LinkedHashMap<String, String> getAttributes() {
        return attributes;
    }

    public void addAttribute(String attName, String attValue) {
        this.attributes.put(attName, attValue);
    }

    @Override
    public String toString() {
        if (this.getType() == TagType.OPEN && this.hasChildren()) {
            return "Element{name=" + name + ", value=" + value + ", att="+attributes+
                    ", children :" + childPrint();
        } else if (this.getType() == TagType.OPEN) {
            return "\t\tChildElement{name=" + name + ", att="+attributes+
                    ", value=" + value + "\n";
        } else if (this.getType() == TagType.SELFCLOSE) {
            return "\t\t\t\tSelfcloseChild{name=" + name +
                    ", value=" + value +", att="+attributes+ "\n";
        } else return "";
    }

    private String childPrint() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n");
        for (Element child : children) {
            buffer.append("\t");
            buffer.append(child);
            child.setUsedTimes(1);
        }
        return buffer.toString();
    }

}
