package com.csw.task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Element;

import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

public class JsonParser {
    private JsonNode rootNode;
    private XMLBuilder xmlB;

    public JsonParser() {
        xmlB = new XMLBuilder();
    }

    public void parse(String jsonPath, String xmlPath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFilePath = new File(jsonPath);
            rootNode = objectMapper.readValue(jsonFilePath, JsonNode.class);
            createXMLElement(rootNode, null);
//            Element element = createXMLElement(rootNode.getNodeType().toString());
//            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
//            while (fields.hasNext()) {
//                Map.Entry<String, JsonNode> node = fields.next();
//                createXMLElement(node, element);
//            }
            xmlB.build(xmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void createXMLElement(Map.Entry<String, JsonNode> node, Element element) {
        String key = node.getKey();
        JsonNode value = node.getValue();
        createXMLElement(key, value, element);
    }

    private void createXMLElement(String key, JsonNode value, Element element) {
        Element created = null;
        if (value.getNodeType().toString().equalsIgnoreCase("object")) {
            created = xmlB.appendElement(value.getNodeType().toString().toLowerCase(), key, element);
            Iterator<Map.Entry<String, JsonNode>> fields = value.fields();
            while (fields.hasNext()) {
                createXMLElement(fields.next(), created);
            }
        } else if (value.getNodeType().toString().equalsIgnoreCase("array")) {
            created = xmlB.appendElement(value.getNodeType().toString().toLowerCase(), key, element);
            Iterator<Map.Entry<String, JsonNode>> fields = value.fields();
            Iterator<JsonNode> elements = value.elements();
            while (fields.hasNext()) {
                createXMLElement(fields.next(), created);
            }
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                createXMLElement(next, created);
            }
        } else {
            created = xmlB.appendElement(value.getNodeType().toString().toLowerCase(), key, value.toString(), element);
        }
    }


    private void createXMLElement(JsonNode node, Element element) {
        createXMLElement(null, node, element);
    }


    private Element createXMLElement(String tagName) {
        Element element = null;
        if (rootNode.getNodeType().toString().equalsIgnoreCase("Object")) {
            element = xmlB.appendElement("object", null);
        }
        return element;
    }
}
