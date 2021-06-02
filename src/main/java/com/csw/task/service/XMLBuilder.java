package com.csw.task.service;

import com.csw.task.service.exception.XMLBuildingException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class XMLBuilder {

    private Document document;

    public XMLBuilder() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            this.document = documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new XMLBuildingException(e);
        }

    }

    public Element appendElement(String tagName, String name) {
        return appendElement(tagName, name, null);
    }

    public Element appendElement(String tagName, String name, String value, Element parent) {
        Element element = createElement(tagName, name, value);
        if (parent == null) {
            document.appendChild(element);
        } else {
            parent.appendChild(element);
        }
        return element;
    }

    public Element appendElement(String tagName, String name, Element parent) {
        return appendElement(tagName, name, null, parent);
    }

    private Element createElement(String tagName, String name, String value) {
        Element element = document.createElement(tagName);
        if (!StringUtils.isEmpty(name)) {
            element.setAttribute("name", name);
        }
        if (!StringUtils.isEmpty(value) && !value.equals("null")) {
            element.setNodeValue(value);
            element.setTextContent(value);
        }
        return element;
    }

    public void build(String path) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            createFile(path);
            StreamResult streamResult = new StreamResult(path);
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            throw new XMLBuildingException(e);
        }
    }

    private void createFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch(Exception e) {
            throw new XMLBuildingException("Output File path is not correct or permission denied to maniulate it",e);
        }

    }

}
