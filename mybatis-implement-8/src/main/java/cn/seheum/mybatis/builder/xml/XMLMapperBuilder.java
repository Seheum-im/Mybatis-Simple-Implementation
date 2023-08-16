package cn.seheum.mybatis.builder.xml;

import cn.seheum.mybatis.builder.BaseBuilder;
import cn.seheum.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @author seheum
 * @date 2023/8/16
 **/
public class XMLMapperBuilder extends BaseBuilder {

    private Element element;
    private String resource;
    private String currentNameSpace;

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream), configuration, resource);
    }

    private XMLMapperBuilder(Document document, Configuration configuration, String resource) {
        super(configuration);
        this.element = document.getRootElement();
        this.resource = resource;
    }


}
