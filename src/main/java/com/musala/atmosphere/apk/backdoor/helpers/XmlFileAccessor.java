package com.musala.atmosphere.apk.backdoor.helpers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.musala.atmosphere.apk.backdoor.exceptions.XmlAccessException;

/**
 * Provides methods for accessing and modifying XML files.
 * 
 * @author boris.strandjev
 */
public class XmlFileAccessor {
    private File xmlFileLocation;

    /**
     * @param xmlFileLocation
     *        - the path to the xml file which will be operated upon using the instance
     */
    public XmlFileAccessor(File xmlFileLocation) {
        this.xmlFileLocation = xmlFileLocation;
    }

    /**
     * Fetches the value of an attribute of the first element identified by its tag in the associated XML document.
     * 
     * @param tag
     *        - the tag of the element to operate on
     * @param attribute
     *        - the attribute which value to retrieve
     * @return The associated value of the attribute or null if it is not set for the given element
     */
    public String getAttributeValue(String tag, String attribute) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFileLocation);
            NodeList nodeList = document.getElementsByTagName(tag);
            if (nodeList.getLength() == 0) {
                return null;
            } else {
                Node attributeNode = nodeList.item(0).getAttributes().getNamedItem(attribute);
                return (attributeNode != null) ? attributeNode.getNodeValue() : null;
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XmlAccessException("Problem while fetching the value of an xml attribute.", e);
        }
    }

    /**
     * Modifies the value of an attribute of the first element identified by its tag in the associated XML document.
     * <p>
     * If the attribute does not exist for the given element, it is added.
     * 
     * @param tag
     *        - the tag of the element to operate on
     * @param attribute
     *        - the attribute to add
     * @param attrValue
     *        - the value to set
     */
    public void changeAttribute(String tag, String attribute, String attrValue) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFileLocation);
            NodeList nodeList = document.getElementsByTagName(tag);
            if (nodeList.getLength() > 0) {
                ((Element) nodeList.item(0)).setAttribute(attribute, attrValue);
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFileLocation);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new XmlAccessException("Problem while loading the xml in which to set attribute value.", e);
        } catch (TransformerException e) {
            throw new XmlAccessException("Problem while saving updated attribute value.", e);
        }
    }
}
