package edu.banda.coel.core.svg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;

import junit.framework.TestCase;

import org.apache.batik.svggen.SVGGraphics2D;
import org.junit.Test;

import edu.banda.coel.core.svg.SVGUtil;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class SVGUtilTest extends TestCase {

	private SVGUtil svgUtil = new SVGUtil();

	@Test
	public void testExportToFile() {
		SVGGraphics2D canvas = svgUtil.createBlankCanvas();

		// add some lines to canvas

		canvas.setPaint(Color.red);
		canvas.fill(new Rectangle(10, 10, 1000, 100));

		
		canvas.setPaint(Color.blue);
		canvas.fill(new Rectangle(0, 200, 1000, 100));

		// Drawing formats
		canvas.setPaint(Color.black);
		canvas.setFont(canvas.getFont().deriveFont(12.0f));
		
		// First Reactant - Domain 1
		canvas.drawLine(50, 50, 60, 50);
		char [] charArray1 = { '1' };
		canvas.drawChars(charArray1, 0, charArray1.length, 52, 45);
		
		// First Reactant - Domain 2
		canvas.drawLine(65, 50, 90, 50);
		canvas.drawLine(85, 45, 90, 50);
		canvas.drawLine(85, 55, 90, 50);
		char [] charArray2 = { '2' };
		canvas.drawChars(charArray2, 0, charArray2.length, 72, 45);
		
		// Plus Sign "+"
		canvas.setStroke(new BasicStroke(3f));
		canvas.drawLine(110, 50, 120, 50);
		canvas.drawLine(115, 45, 115, 55);
		
		// Second Reactant - Domain 1
		canvas.drawLine(145, 52, 150, 52);
		canvas.drawLine(145, 52, 147, 49);
		canvas.drawLine(145, 52, 147, 55);
		char [] charArray1Complement = { '1', '*' };
		canvas.drawChars(charArray1Complement, 0, charArray1Complement.length, 143, 65);
		
		// Second Reactant - Domain 2
		canvas.drawLine(155, 52, 180, 52);
		canvas.drawLine(155, 48, 180, 48);
		char [] charArray2Complement = { '2', '*' };
		canvas.drawChars(charArray2Complement, 0, charArray2Complement.length, 165, 65);
		canvas.drawChars(charArray2, 0, charArray2.length, 165, 43);
		
		// Second Reactant - Domain 3
		canvas.drawLine(185, 52, 195, 52);
		canvas.drawLine(185, 48, 195, 48);
		canvas.drawLine(185, 48, 195, 48);
		canvas.drawLine(193, 46, 195, 48);
		canvas.drawLine(193, 50, 195, 48);
		char [] charArray3 = { '3' };
		char [] charArray3Complement  = { '3', '*' };
		canvas.drawChars(charArray3Complement, 0, charArray3Complement.length, 185, 65);
		canvas.drawChars(charArray3, 0, charArray3.length, 187, 43);		

		// Bidirectional Arrow
		canvas.drawLine(235,47,260,47);
		canvas.drawLine(255,42,260,47);
		
		canvas.drawLine(235,53,260,53);
		canvas.drawLine(235,53,240,58);
		
		// First Product - Domain 2
		canvas.drawLine(300,50,325,50);
		canvas.drawChars(charArray2, 0, charArray2.length, 310, 46);
		
		// First Product - Domain 3
		canvas.drawLine(330,50,340,50);
		canvas.drawLine(337,47,340,50);
		canvas.drawLine(337,53,340,50);
		canvas.drawChars(charArray3, 0, charArray3.length, 332, 46);

		// Plus Sign "+"
		canvas.drawLine(360, 50, 370, 50);
		canvas.drawLine(365, 45, 365, 55);
		
		// Second Product - Domain 1
		canvas.drawLine(385, 52, 390, 52);
		canvas.drawLine(385, 52, 387, 50);
		canvas.drawLine(385, 52, 387, 54);
		canvas.drawChars(charArray1Complement, 0, charArray1Complement.length, 381, 64);
		
		canvas.drawLine(385, 47, 390, 47);
		canvas.drawChars(charArray1, 0, charArray1.length, 385, 44);
		
		// Second Product - Domain 2
		canvas.drawLine(395,47,420,47);
		canvas.drawLine(395,52,420,52);
		canvas.drawLine(417,44,420,47);
		canvas.drawLine(417,50,420,47);
		canvas.drawChars(charArray2Complement, 0, charArray2Complement.length, 403, 64);
		canvas.drawChars(charArray2, 0, charArray2.length, 403, 44);
		
		// Second Product - Domain 3
		canvas.drawLine(425,52,430,52);
		canvas.drawChars(charArray3Complement, 0, charArray3Complement.length, 424, 64);

		// export to file
		svgUtil.exportToFile(canvas, "test.svg", true);
	}

	@Test
	public void testExportToString() {
		SVGGraphics2D canvas = svgUtil.createBlankCanvas();

		// add some lines to canvas
		canvas.setPaint(Color.red);
		canvas.fill(new Rectangle(10, 10, 1000, 100));
		canvas.setPaint(Color.blue);
		canvas.fill(new Rectangle(0, 200, 1000, 100));

		String svg = svgUtil.exportToString(canvas, true);
		assertNotNull(svg);
	}
}