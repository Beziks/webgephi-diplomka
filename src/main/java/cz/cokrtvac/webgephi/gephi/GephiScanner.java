package cz.cokrtvac.webgephi.gephi;

import cz.cokrtvac.webgephi.InitializationException;
import cz.cokrtvac.webgephi.annotations.Available;
import org.gephi.layout.spi.Layout;
import org.gephi.layout.spi.LayoutBuilder;
import org.reflections.Reflections;
import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 3.6.13
 * Time: 14:26
 */
public class GephiScanner {
    @Inject
    private Logger log;

    public List<Layout> getAvailableLayouts() {
        List<Layout> out = new ArrayList<Layout>();

        Reflections reflections = new Reflections("org.gephi");
        Set<Class<? extends LayoutBuilder>> layoutBuilders = reflections.getSubTypesOf(LayoutBuilder.class);

        for (Class<?> c : layoutBuilders) {
            try {
                if (LayoutBuilder.class.isAssignableFrom(c)) {
                    LayoutBuilder builder = (LayoutBuilder) c.newInstance();
                    Layout layout = builder.buildLayout();
                    layout.resetPropertiesValues();
                    out.add(layout);
                }
            } catch (Exception e) {
                String msg = "Error during scanning GEPHI LAYOUTs";
                log.error(msg, e);
                throw new InitializationException(e);
            }
        }
        return out;
    }
}
