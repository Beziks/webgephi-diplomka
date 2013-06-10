package cz.cokrtvac.webgephi.model.xml.graph;

import cz.cokrtvac.webgephi.model.entity.GraphEntity;
import cz.cokrtvac.webgephi.model.xml.AbstractXml;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 4.6.13
 * Time: 16:43
 */
@XmlRootElement(name = "graph")
public class GraphDetailXml extends AbstractXml {
    private List<Data> data;
    private String name;
    private GraphDetailXml parent;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(required = true)
    public List<Data> getData() {
        if (data == null) {
            data = new ArrayList<>();
            data.add(new Data("gexf"));
            data.add(new Data("svg"));
        }
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }


    @XmlElement
    public GraphDetailXml getParent() {
        return parent;
    }

    public void setParent(GraphDetailXml parent) {
        this.parent = parent;
    }

    @Override
    public void setUriPath(String path) {
        super.setUriPath(path);
        for (Data d : getData()) {
            d.setUriPath(concatUri(getUri(), d.getType()));
        }
    }

    @Override
    public void setLink(HttpServletRequest req) {
        super.setLink(req);
        for (Data d : getData()) {
            d.setLink(req);
        }
    }

    public static GraphDetailXml create(GraphEntity entity) {
        GraphDetailXml xml = new GraphDetailXml();
        xml.setName(entity.getName());
        xml.setUriPath(concatUri(GraphsXml.PATH, entity.getId()));
        if (entity.getParent() != null) {
            GraphDetailXml xmlParent = new GraphDetailXml();
            xmlParent.setName(entity.getParent().getName());
            xmlParent.setUriPath(concatUri(GraphsXml.PATH, entity.getParent().getId()));
            xml.setParent(xmlParent);
        }
        return xml;
    }

    //=============================================================

    @XmlType(name = "data")
    public static class Data extends AbstractXml {
        private String type;

        public Data() {
        }

        public Data(String type) {
            this.type = type;
        }

        @XmlAttribute
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
