package org.moreunit.mock.templates;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.inject.Singleton;

@Singleton
public class XmlTemplateDefinitionReader
{

    public MockingTemplates read(InputStream is) throws TemplateException
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance(MockingTemplates.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return (MockingTemplates) unmarshaller.unmarshal(is);
        }
        catch (JAXBException e)
        {
            throw new TemplateException("Could not read XML definition", e);
        }
    }
}