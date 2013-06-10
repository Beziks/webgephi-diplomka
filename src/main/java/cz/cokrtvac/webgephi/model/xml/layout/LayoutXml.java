package cz.cokrtvac.webgephi.model.xml.layout;

import cz.cokrtvac.webgephi.model.xml.AbstractXml;
import cz.cokrtvac.webgephi.util.CollectionsUtil;
import cz.cokrtvac.webgephi.util.StringUtil;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 3.6.13
 * Time: 18:20
 */
@XmlRootElement(name = "layout")
public class LayoutXml extends AbstractXml {
    private String name;
    private List<PropertyXml> properties;

    @XmlElement(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    public List<PropertyXml> getProperties() {
        if (properties == null) {
            properties = new ArrayList<>();
        }
        return properties;
    }

    public void setProperties(List<PropertyXml> properties) {
        this.properties = properties;
    }

    public void addProperty(PropertyXml prop) {
        if (prop.getUri() == null) {
            prop.setUriPath(concatUri(getUri(), prop.getId()));
        }
        getProperties().add(prop);
    }

    public PropertyXml<?> getProperty(String id){
        id = StringUtil.uriSafe(id).trim();
        for(PropertyXml<?> p : getProperties()){
            String pn = p.getUri();
            if(pn.endsWith("/")){
                pn = pn.substring(0, pn.length() - 1);
            }

            int i = pn.lastIndexOf("/");
            if(i > 0){
                pn = pn.substring(i + 1);
            }
            pn = pn.trim();

            //log.info(pn + " == " + id);

            if(id.equals(pn)){
                return p;
            }
        }

        log.warn("Unknown property: " + id);
        return null;
    }

    @Override
    public String toString() {
        return "LayoutXml{" +
                "name='" + name + '\'' +
                ", uri='" + getUri() + '\'' +
                ", properties=" + CollectionsUtil.toString(getProperties()) +
                '}';
    }

    public static LayoutXml create(Layout l) {
        LayoutXml layoutXml = new LayoutXml();

        layoutXml.setName(l.getBuilder().getName());
        layoutXml.setUriPath(concatUri(LayoutsXml.PATH, layoutXml.getName()));

        for (LayoutProperty p : l.getProperties()) {
            layoutXml.addProperty(PropertyXml.create(p));
        }

        return layoutXml;
    }
}
