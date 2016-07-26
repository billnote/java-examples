package com.bill.examples.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.iso_relax.verifier.VerifierFactory;
import org.iso_relax.verifier.VerifierHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlPraserExample {
	private static String basePath = XmlPraserExample.class.getClassLoader().getResource("").getPath() + File.separator;

	public static void main(String[] args)
			throws SAXException, DocumentException, VerifierConfigurationException, IOException {
		String vast3XSDPath = basePath + "vast3.xsd";
		String vast4XSDPath = basePath + "vast_v4.0_modified.xsd";

		String adXml = basePath + "vast-ad3.xml";
		System.out.println(validateXMLSchema(vast3XSDPath, adXml));

		adXml = basePath + "vast-ad4.xml";
		System.out.println(validateXMLSchema(vast4XSDPath, adXml));

		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(adXml);
			System.out.println(validate(document, vast4XSDPath));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < 10; i++) {
			performanceTest();
		}
	}

	public static void performanceTest() {
		String vast4XSDPath = basePath + "vast_v4.0_modified.xsd";
		String adXml = basePath + "vast-ad4.xml";
		int runtimes = 100000;
		try {
			Validator validator = createValidator(vast4XSDPath);
			long start = System.currentTimeMillis();
			for (int i = 0; i < runtimes; i++) {
				validate(validator, adXml);
			}
			long end = System.currentTimeMillis();
			System.out.println("Validator use time:" + (end - start));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Verifier verifier = createVerifier(vast4XSDPath);
			long start = System.currentTimeMillis();
			for (int i = 0; i < runtimes; i++) {
				SAXReader reader = new SAXReader();
				validate(verifier, reader.read(adXml));
			}
			long end = System.currentTimeMillis();
			System.out.println("Verifier use time:" + (end - start));
		} catch (VerifierConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Validator createValidator(String xsdPath) throws SAXException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(new File(xsdPath));
		return schema.newValidator();
	}

	public static Verifier createVerifier(String xsdPath)
			throws VerifierConfigurationException, SAXException, IOException {
		// (1) use autodetection of schemas
		VerifierFactory factory = new com.sun.msv.verifier.jarv.TheFactoryImpl();
		org.iso_relax.verifier.Schema schema = factory.compileSchema(xsdPath);

		// (2) configure a Vertifier
		Verifier verifier = schema.newVerifier();
		verifier.setErrorHandler(new ErrorHandler() {
			public void error(SAXParseException saxParseEx) {
				System.out.println("Error during validation." + saxParseEx);
			}

			public void fatalError(SAXParseException saxParseEx) {
				System.out.println("Fatal error during validation." + saxParseEx);
			}

			public void warning(SAXParseException saxParseEx) {
				System.out.println(saxParseEx);
			}
		});

		return verifier;
	}

	public static boolean validate(Validator validator, String xmlPath) {
		try {
			validator.validate(new StreamSource(new File(xmlPath)));
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
			return false;
		} catch (SAXException e1) {
			System.out.println("SAX Exception: " + e1.getMessage());
			return false;
		}
		return true;
	}

	public static boolean validate(Verifier verifier, Document document) {
		VerifierHandler handler;
		try {
			handler = verifier.getVerifierHandler();
			SAXWriter writer = new SAXWriter(handler);
			writer.write(document);
			return handler.isValid();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static boolean validateXMLSchema(String xsdPath, String xmlPath) {
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(xsdPath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(xmlPath)));
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
			return false;
		} catch (SAXException e1) {
			System.out.println("SAX Exception: " + e1.getMessage());
			return false;
		}
		return true;
	}

	public static boolean validate(Document document, String schemaURI) throws Exception {

		// (1) use autodetection of schemas
		VerifierFactory factory = new com.sun.msv.verifier.jarv.TheFactoryImpl();
		org.iso_relax.verifier.Schema schema = factory.compileSchema(schemaURI);

		// (2) configure a Vertifier
		Verifier verifier = schema.newVerifier();
		verifier.setErrorHandler(new ErrorHandler() {
			public void error(SAXParseException saxParseEx) {
				System.out.println("Error during validation." + saxParseEx);
			}

			public void fatalError(SAXParseException saxParseEx) {
				System.out.println("Fatal error during validation." + saxParseEx);
			}

			public void warning(SAXParseException saxParseEx) {
				System.out.println(saxParseEx);
			}
		});

		// (3) starting validation by resolving the dom4j document into sax
		VerifierHandler handler = verifier.getVerifierHandler();
		SAXWriter writer = new SAXWriter(handler);
		writer.write(document);

		return handler.isValid();

	}
}
