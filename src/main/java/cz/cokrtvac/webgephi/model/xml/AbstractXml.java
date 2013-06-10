package cz.cokrtvac.webgephi.model.xml;

import cz.cokrtvac.webgephi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: Vaclav Cokrt, beziks@gmail.com
 * Date: 9.6.13
 * Time: 19:24
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class AbstractXml {
    public static final String URI_BASE = "http://webgephi.cz";
    protected Logger log = LoggerFactory.getLogger(getClass());

    private String uriBase = URI_BASE;
    private String linkBase = null;

    private String path;

    private String uri;
    private String link;

    @XmlAttribute(required = true)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @XmlAttribute(required = false)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPath() {
        return path;
    }

    public void setUriAndLink(String path, HttpServletRequest req){
        setUriPath(path);
        setLink(req);
    }

    public void setUriPath(String path) {
        String p = StringUtil.uriSafe(path);
        if(p.startsWith(uriBase)){
            p = p.substring(uriBase.length());
        }
        this.path = p;
        this.uri = concatUri(uriBase, this.path);
    }

    public void setLink(HttpServletRequest req){
        if(path == null){
            log.error("Uri path is null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        String port = ":" + req.getServerPort();
        if(port.equals(":80")){
            port = "";
        }

        linkBase = req.getScheme() + "://" + req.getServerName() + port + req.getContextPath() + req.getServletPath();
        log.info(linkBase);
        setLink(concatUri(linkBase, path));
    }

    public static String concatUri(String begin, String end) {
        begin = StringUtil.uriSafe(begin);
        end = StringUtil.uriSafe(end);
        if (!begin.endsWith("/")) {
            begin = begin + "/";
        }

        if (end.startsWith("/")) {
            end = end.substring(1);
        }
        return begin + end;
    }

    public static String concatUri(Object begin, Object end){
        return concatUri(String.valueOf(begin), String.valueOf(end));
    }
}
