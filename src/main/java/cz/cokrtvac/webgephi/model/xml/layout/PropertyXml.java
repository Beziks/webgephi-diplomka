package cz.cokrtvac.webgephi.model.xml.layout;

import cz.cokrtvac.webgephi.model.xml.AbstractXml;
import org.gephi.layout.spi.LayoutProperty;

import javax.xml.bind.annotation.*;
import java.lang.reflect.InvocationTargetException;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 3.6.13
 * Time: 18:23
 */
public class PropertyXml<T> extends AbstractXml {
    private String id;
    private Class<?> type;
    private String name;
    private String description;
    private T value;

    public static PropertyXml create(LayoutProperty layoutProperty) {
        PropertyXml xml = new PropertyXml();
        xml.setName(layoutProperty.getProperty().getName());
        xml.setType(layoutProperty.getProperty().getValueType());
        xml.setId(layoutProperty.getCanonicalName());
        xml.setDescription(layoutProperty.getProperty().getShortDescription());
        try {
            xml.setValue(layoutProperty.getProperty().getValue());
        } catch (IllegalAccessException e) {
            // TODO
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return xml;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(required = true)
    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    @XmlElement(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(required = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement(required = true)
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyXml{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}
