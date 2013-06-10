package cz.cokrtvac.webgephi.test.integration;

import cz.cokrtvac.webgephi.gephi.LayoutsPool;
import cz.cokrtvac.webgephi.test.integration.util.ArquillianUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;


public class GephiTest extends Arquillian {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ArquillianUtil.createTestArchive();
    }

    @Inject
    private LayoutsPool configurationSingleton;

    @Inject
    private Logger log;

    @Test
    public void testRegister() throws Exception {
        Assert.assertEquals(13, configurationSingleton.getAvailableLayouts().getLayouts().size());
    }

}
