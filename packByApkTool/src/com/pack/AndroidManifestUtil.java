package com.pack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AndroidManifestUtil {
	public static void change(String srcfilePath, String outFilePath, String metaDataName, String metaDataNewValue) {
		File f = new File(srcfilePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		final String android_value = "android:value";
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			NodeList nl = doc.getElementsByTagName("meta-data");
			int len = nl.getLength();
			org.w3c.dom.Node channelNode = null;
			for (int i = 0; i < len; i++) {
				org.w3c.dom.Node n = nl.item(i);
				// System.out.println(n.getNodeName());
				NamedNodeMap nm = n.getAttributes();
				boolean c = false;
				for (int j = 0; j < nm.getLength(); j++) {
					org.w3c.dom.Node n2 = nm.item(j);
					// System.out.println(n2.getNodeName() + "|" +
					// n2.getNodeValue());
					if (metaDataName.equals(n2.getNodeValue())) {
						c = true;
						break;
					}
				}
				if (c) {
					for (int j = 0; j < nm.getLength(); j++) {
						org.w3c.dom.Node n2 = nm.item(j);
						if (android_value.equals(n2.getNodeName())) {
							channelNode = n2;
							break;
						}
					}
				}
			}
			if (channelNode != null) {
				channelNode.setNodeValue(metaDataNewValue);
				writeXMLFile(doc, outFilePath);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void writeXMLFile(Document doc, String outfile) {

		try {
			FileOutputStream fos = new FileOutputStream(outfile);
			OutputStreamWriter outwriter = new OutputStreamWriter(fos);
			writeXmlFile(doc, outwriter, "utf-8");
			outwriter.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeXmlFile(Document doc, Writer w, String encoding) {
		try {
			Source source = new DOMSource(doc);
			Result result = new StreamResult(w);
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, encoding);
			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
