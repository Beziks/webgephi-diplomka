package cz.cokrtvac.webgephi.gephi;

import cz.cokrtvac.webgephi.model.xml.layout.LayoutXml;
import cz.cokrtvac.webgephi.model.xml.layout.PropertyXml;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutProperty;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.RankingController;
import org.openide.util.Lookup;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 9.6.13
 * Time: 18:10
 */
public class LayoutsExecutor {
    public static final String DEFAULT_FORMAT = ".gexf";

    @Inject
    private Logger log;

    @Inject
    private LayoutsPool layoutsPool;

    public LayoutsExecutor() {
    }

    public String execute(String graphDocument, LayoutXml layoutXml, Integer repeat) {
        Layout layout = layoutsPool.getLayout(layoutXml.getUri());

        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        //Get models and controllers for this new workspace - will be useful later
        AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
        RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);

        //Import file
        Container container;
        try {
            FileImporter fileImporter = importController.getFileImporter(DEFAULT_FORMAT);
            container = importController.importFile(new StringReader(graphDocument), fileImporter);
            container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED);   //Force UNDIRECTED
        } catch (Exception ex) {
            ex.printStackTrace();
            // TODO
            return "ERROR";
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);

        layout.setGraphModel(graphModel);


        // Set params from xml
        // Has to be here, setGraphModel resets settings - e.g. ForceAtlas2
        applySettings(layout, layoutXml);

        layout.initAlgo();
        for (int i = 0; i < repeat; i++) {
            layout.goAlgo();
        }
        layout.endAlgo();

        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        StringWriter sw = new StringWriter();
        CharacterExporter exporter = (CharacterExporter) ec.getExporter(DEFAULT_FORMAT);
        ec.exportWriter(sw, exporter);

        return sw.toString();
    }

    private Layout applySettings(Layout layout, LayoutXml layoutXml) {
        for (LayoutProperty p : layout.getProperties()) {
            PropertyXml<?> pXml = layoutXml.getProperty(p.getCanonicalName());
            if (pXml != null) {
                try {
                    log.info(p.getProperty().getName() + " | " + p.getProperty().getValue() + " | " + pXml.getValue());
                    p.getProperty().setValue(pXml.getValue());
                } catch (Exception e) {
                    log.error("Property cannot be set.", e);
                }
            }
        }
        return layout;
    }
}
