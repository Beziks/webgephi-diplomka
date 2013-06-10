package cz.cokrtvac.webgephi.model.xml.graph;

import cz.cokrtvac.webgephi.model.xml.AbstractXml;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 4.6.13
 * Time: 19:52
 */
@XmlRootElement(name = GraphsXml.PATH)
@XmlAccessorType(XmlAccessType.NONE)
public class GraphsXml extends AbstractXml {
    public static final String PATH = "graphs";

    private List<GraphDetailXml> graphs;

    public GraphsXml(){
        setUriPath(PATH);
    }

    @XmlElement(name = "graph")
    public List<GraphDetailXml> getGraphs() {
        if(graphs == null){
            graphs = new ArrayList<>();
        }
        return graphs;
    }

    public void setGraphs(List<GraphDetailXml> graphs) {
        this.graphs = graphs;
    }
}
