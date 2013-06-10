package cz.cokrtvac.webgephi.gephi;

import cz.cokrtvac.webgephi.model.xml.layout.LayoutXml;
import cz.cokrtvac.webgephi.model.xml.layout.LayoutsXml;
import cz.cokrtvac.webgephi.model.xml.layout.PropertyXml;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutProperty;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.*;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 4.6.13
 * Time: 15:18
 */
@Singleton
@Startup
public class LayoutsPool {

    @Inject
    private Logger log;

    @Inject
    private GephiScanner gephiScanner;

    private LayoutsXml layouts;
    private Map<String, LayoutXml> layoutXmlsMap = new HashMap<>();
    private Map<String, Layout> layoutsMap = new HashMap<>();

    @PostConstruct
    public void init() {
        log.info("Initializing app setting.");

        initMaps();
        initLayouts();
        log.info("Initializing done.");
    }

    public LayoutsXml getAvailableLayouts() {
        return layouts;
    }

    public Layout getLayout(String uri){
        Layout l = layoutsMap.get(uri);
        l = l.getBuilder().buildLayout();
        l.resetPropertiesValues();
        return l;
    }

    private void initMaps() {
        List<Layout> availableLayouts = gephiScanner.getAvailableLayouts();
        log.debug("Available layouts size: " + String.valueOf(availableLayouts.size()));

        for (Layout l : availableLayouts) {
            LayoutXml layoutXml = LayoutXml.create(l);

            layoutsMap.put(layoutXml.getUri(), l);
            layoutXmlsMap.put(layoutXml.getUri(), layoutXml);
        }
    }

    private void initLayouts() {
        List<LayoutXml> all = new ArrayList<>(layoutXmlsMap.values());
        Collections.sort(all, new Comparator<LayoutXml>() {
            @Override
            public int compare(LayoutXml o1, LayoutXml o2) {
                return o1.getUri().compareTo(o2.getUri());
            }
        });
        layouts = new LayoutsXml();
        layouts.getLayouts().addAll(all);
    }
}
