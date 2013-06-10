package cz.cokrtvac.webgephi.test.integration.util;

import cz.cokrtvac.webgephi.util.CollectionsUtil;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 3.6.13
 * Time: 15:26
 */
public class ArquillianUtil {
    private static Logger log = LoggerFactory.getLogger(ArquillianUtil.class.getName());


    public static Archive<?> createTestArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsLibraries(getMavenLibs())

                .addPackages(true, new Filter<ArchivePath>() {
                    @Override
                    public boolean include(ArchivePath archivePath) {
                        System.out.println(archivePath.get());
                        return !archivePath.get().contains("cz/cokrtvac/webgephi/test");
                    }
                }, "cz.cokrtvac.webgephi")

                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                        // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");

        log.debug(archive.toString(true));
        return archive;
    }

    private static File[] getMavenLibs() {
        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");
        resolver.goOffline();
        resolver.includeDependenciesFromPom("pom.xml");
        File[] libs = resolver.resolveAsFiles();

        log.debug("Maven libs: " + CollectionsUtil.toString(libs));
        return libs;
    }
}
