package cz.cokrtvac.webgephi.model.xml.layout;

import cz.cokrtvac.webgephi.model.xml.AbstractXml;
import cz.cokrtvac.webgephi.util.CollectionsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 4.6.13
 * Time: 10:35
 */
@XmlRootElement(name = LayoutsXml.PATH)
@XmlAccessorType(XmlAccessType.NONE)
public class LayoutsXml extends AbstractXml {
    public static final String PATH = "layouts";
    private List<LayoutXml> layouts;

    public LayoutsXml() {
        setUriPath(PATH);
    }

    @XmlElement(name = "layout")
    public List<LayoutXml> getLayouts() {
        if (layouts == null) {
            layouts = new ArrayList<>();
        }
        return layouts;
    }

    public void setLayouts(List<LayoutXml> layouts) {
        this.layouts = layouts;
    }

    @Override
    public String toString() {
        return "LayoutsXml " + CollectionsUtil.toString(getLayouts());
    }

    @Override
    public void setLink(HttpServletRequest req) {
        super.setLink(req);
        for (LayoutXml l : getLayouts()) {
            l.setLink(req);
        }
    }
}
