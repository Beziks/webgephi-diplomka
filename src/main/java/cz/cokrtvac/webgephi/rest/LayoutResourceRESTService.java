package cz.cokrtvac.webgephi.rest;

import cz.cokrtvac.webgephi.gephi.LayoutsPool;
import cz.cokrtvac.webgephi.model.xml.layout.LayoutXml;
import cz.cokrtvac.webgephi.model.xml.layout.LayoutsXml;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read the contents of the members table.
 */
@Path("/layouts")
@RequestScoped
public class LayoutResourceRESTService {
    @Inject
    private LayoutsPool configurationSingleton;

    @GET
    @Produces("text/xml")
    public LayoutsXml listAllLayouts(@Context HttpServletRequest req) {
        LayoutsXml layoutsXml = configurationSingleton.getAvailableLayouts();
        layoutsXml.setLink(req);
        return layoutsXml;
    }

    @GET
    @Path("/{id}")
    @Produces("text/xml")
    public LayoutXml lookupMemberById(@Context HttpServletRequest req, @PathParam("id") String id) {
        for (LayoutXml l : configurationSingleton.getAvailableLayouts().getLayouts()) {
            if (l.getUri().endsWith(id)) {
                l.setLink(req);
                return l;
            }
        }
        return null;
    }
}
