package cz.cokrtvac.webgephi.gephi;

import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.SVGExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.spi.FileImporter;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.RankingController;
import org.openide.util.Lookup;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 9.6.13
 * Time: 18:46
 */
public class ImageExporter {
    public String toSvg(String graphDocument) {
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
            FileImporter fileImporter = importController.getFileImporter(LayoutsExecutor.DEFAULT_FORMAT);
            container = importController.importFile(new StringReader(graphDocument), fileImporter);
            container.getLoader().setEdgeDefault(EdgeDefault.UNDIRECTED);   //Force UNDIRECTED
        } catch (Exception ex) {
            ex.printStackTrace();
            // TODO
            return "ERROR";
        }

        //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);
        //====================================================================

        //Simple SVG export
        ExportController ec = Lookup.getDefault().lookup(ExportController.class);

        SVGExporter svgExporter = (SVGExporter) ec.getExporter("svg");

        StringWriter sw = new StringWriter();
        ec.exportWriter(sw, svgExporter);
        return sw.toString();
    }
}
