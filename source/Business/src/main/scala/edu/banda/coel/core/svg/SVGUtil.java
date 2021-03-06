package edu.banda.coel.core.svg;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.swing.JFrame;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import edu.banda.coel.CoelRuntimeException;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class SVGUtil {

	public SVGGraphics2D createBlankCanvas() {
		DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
	    String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
	    Document document = domImpl.createDocument(svgNS, "svg", null);

	    // Create an instance of the SVG Generator.
	    SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
	    ctx.setComment("Generated by COEL Framework with Batik SVG Generator");
	    ctx.setEmbeddedFontsOn(true);
	    return new SVGGraphics2D(ctx, true);
	}

	private Element setSize(SVGGraphics2D canvas, int width, int height) {
		final Element root = canvas.getRoot();
		root.setAttributeNS(null, "preserveAspectRatio", "xMinYMin meet");
	    root.setAttributeNS(null, "viewBox", "0 0 " + width + " " + height);
	    return root;
	}

	public void exportToFile(SVGGraphics2D canvas, String fileName, boolean useCSS) {
		Writer out;
		try {
			FileOutputStream fos = new FileOutputStream(new File(fileName));
			out = new OutputStreamWriter(fos, "UTF-8");
			canvas.stream(out, useCSS);
		} catch (UnsupportedEncodingException e) {
			throw new CoelRuntimeException("Error occured while exporting SVG canvas into the file " + fileName + ".", e);
		} catch (SVGGraphics2DIOException e) {
			throw new CoelRuntimeException("Error occured while exporting SVG canvas into the file " + fileName + ".", e);
		} catch (FileNotFoundException e) {
			throw new CoelRuntimeException("Error occured while exporting SVG canvas into the file " + fileName + ".", e);
		}
	}

	public String exportToString(SVGGraphics2D canvas, boolean useCSS) {
		StringWriter stringWriter = new StringWriter();
		try {
			final Element root = canvas.getRoot();
			canvas.stream(root, stringWriter, useCSS);
		} catch (SVGGraphics2DIOException e) {
			e.printStackTrace();
		}
		return stringWriter.toString();
	}

	public String exportToBase64String(SVGGraphics2D canvas, boolean useCSS) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(baos));  
		try {
			canvas.stream(writer, useCSS);
		} catch (SVGGraphics2DIOException e) {
			e.printStackTrace();
		}  
	    byte[] bytes = baos.toByteArray();  
		return Base64.encodeBase64String(bytes);
	}

	public String exportToBase64WoSpaceString(SVGGraphics2D canvas, boolean useCSS) {
		StringWriter stringWriter = new StringWriter();
		try {
			if (canvas.getSVGCanvasSize() != null) {
				final Element root = setSize(canvas, canvas.getSVGCanvasSize().width, canvas.getSVGCanvasSize().height);
				canvas.stream(root, stringWriter, useCSS);
			} else
				canvas.stream(stringWriter, useCSS);
		} catch (SVGGraphics2DIOException e) {
			e.printStackTrace();
		}
		String str = stringWriter.toString().replaceAll("\\s([\\s])+", " ");
		return Base64.encodeBase64String(str.getBytes());
	}

	public void display(SVGDocument svgDocument) {
	    JSVGCanvas canvas = new JSVGCanvas();
	    JFrame f = new JFrame();
	    f.getContentPane().add(canvas);
	    canvas.setSVGDocument(svgDocument);
	    f.pack();
	    f.setVisible(true);
	}

	public void display(SVGGraphics2D svgCanvas) {
	    JFrame f = new JFrame();
	    JSVGCanvas canvas = new JSVGCanvas();
//	    JPanel panel = new JPanel();
//	    f.getContentPane().add(panel);
	    f.getContentPane().add(canvas);
//	    panel.paintComponents(svgCanvas);
	    canvas.paintComponent(svgCanvas);
	    f.pack();
	    f.setVisible(true);
	}
}