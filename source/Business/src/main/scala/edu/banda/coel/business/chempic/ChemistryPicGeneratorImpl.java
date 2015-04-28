package edu.banda.coel.business.chempic;

import java.awt.*;
import java.util.List;

import org.apache.batik.svggen.SVGGraphics2D;

import edu.banda.coel.business.chempic.dna.VisualDsdDNAStrandIllustrator;
import edu.banda.coel.core.svg.SVGUtil;
import edu.banda.coel.domain.service.ChemistryPicGenerator;

/**
 * @author Peter Banda
 * @since 2013
 */
public class ChemistryPicGeneratorImpl implements ChemistryPicGenerator {

	private final boolean useBase64;
	private final SVGUtil svgUtil = new SVGUtil();

	private static final int X_MARGIN = 20;
	private static final int Y_MARGIN = 20;
	private static final int PLUS_MARGIN = 20;
	private static final int ARROW_MARGIN = 20;

	public ChemistryPicGeneratorImpl(boolean useBase64) {
		this.useBase64 = useBase64;
	}

	@Override
    public String createDNAStrandSVG(String visualDsdDNAStrandString) {
		SVGGraphics2D canvas = svgUtil.createBlankCanvas();
		VisualDsdDNAStrandIllustrator dnaStrandIllustrator = new VisualDsdDNAStrandIllustrator(X_MARGIN, 0, canvas);

		int originalX = dnaStrandIllustrator.getXPos();

		// determine min/max of y position (no drawing yet)
		dnaStrandIllustrator.processDnaStrand(visualDsdDNAStrandString);
		int maxY = Y_MARGIN + dnaStrandIllustrator.getMaxY() - dnaStrandIllustrator.getMinY();

		// rollback x position and set a new y position reflecting min/max values
		dnaStrandIllustrator.setXPos(originalX);
		dnaStrandIllustrator.setYPos(Y_MARGIN - dnaStrandIllustrator.getMinY());

		// draw
		dnaStrandIllustrator.drawDnaStrand(visualDsdDNAStrandString);

		int maxX = dnaStrandIllustrator.getMaxX();
		canvas.setSVGCanvasSize(new Dimension(maxX + X_MARGIN, maxY + Y_MARGIN));
		if (useBase64)
			return svgUtil.exportToBase64WoSpaceString(canvas, true);
		return svgUtil.exportToString(canvas, true);
	}

	@Override
    public String createDNAReactionSVG(
    	List<String> reactantDNAStrands,
    	List<String> productDNAStrands,
    	boolean bidirectional
    ) {
		SVGGraphics2D canvas = svgUtil.createBlankCanvas();
		VisualDsdDNAStrandIllustrator dnaStrandIllustrator = new VisualDsdDNAStrandIllustrator(X_MARGIN, 0, canvas);

		int originalX = dnaStrandIllustrator.getXPos();

		// determine min/max of y position (no drawing yet)
		processStrands(dnaStrandIllustrator, reactantDNAStrands);
		processStrands(dnaStrandIllustrator, productDNAStrands);
		int maxY = Y_MARGIN + dnaStrandIllustrator.getMaxY() - dnaStrandIllustrator.getMinY();

		// rollback x position and set a new y position reflecting min/max values
		dnaStrandIllustrator.setXPos(originalX);
		dnaStrandIllustrator.setYPos(Y_MARGIN - dnaStrandIllustrator.getMinY());

		dnaStrandIllustrator.resetMaxX();

		// draw reactants
		drawStrandsWithPlusSign(dnaStrandIllustrator, reactantDNAStrands);

		// arrow sign
		drawArrow(dnaStrandIllustrator, bidirectional);

		// draw products
		drawStrandsWithPlusSign(dnaStrandIllustrator, productDNAStrands);

		int maxX = dnaStrandIllustrator.getMaxX();
		canvas.setSVGCanvasSize(new Dimension(maxX + X_MARGIN, maxY + Y_MARGIN));
		if (useBase64)
			return svgUtil.exportToBase64WoSpaceString(canvas, true);
		return svgUtil.exportToString(canvas, true);
	}

	private void drawArrow(
		VisualDsdDNAStrandIllustrator dnaStrandIllustrator,
		boolean bidirectional
	) {
		final Graphics2D canvas =  dnaStrandIllustrator.getCanvas();
		int xPos = dnaStrandIllustrator.getMaxX() + ARROW_MARGIN;
		int yPos = dnaStrandIllustrator.getYPos() + 3;
		canvas.drawLine(xPos, yPos, xPos + 25, yPos);
		canvas.drawLine(xPos + 17, yPos + 5, xPos + 25, yPos);
		canvas.drawLine(xPos + 17, yPos - 5, xPos + 25, yPos);
		if (bidirectional) {
			canvas.drawLine(xPos, yPos, xPos + 8, yPos + 5);
			canvas.drawLine(xPos, yPos, xPos + 8, yPos - 5);			
		}
		dnaStrandIllustrator.setXPos(xPos + 25 + ARROW_MARGIN);
	}

	private void drawStrandsWithPlusSign(
		VisualDsdDNAStrandIllustrator dnaStrandIllustrator,
		List<String> dnaStrands
	) {
		final Graphics2D canvas =  dnaStrandIllustrator.getCanvas();
		int count = 0;
		for (String strand : dnaStrands) {
			if (strand != null) {
				dnaStrandIllustrator.drawDnaStrand(strand);

				if (count < dnaStrands.size() - 1) {
					// plus sign
					int xPos = dnaStrandIllustrator.getMaxX() + PLUS_MARGIN;
					int yPos = dnaStrandIllustrator.getYPos() + 3;
					canvas.drawLine(xPos, yPos, xPos + 10, yPos);
					canvas.drawLine(xPos + 5, yPos - 5, xPos + 5, yPos + 5);
					dnaStrandIllustrator.setXPos(xPos + 10 + PLUS_MARGIN);
				}
			}

			count++;
		}
	}

	// no drawing
	private void processStrands(
		VisualDsdDNAStrandIllustrator dnaStrandIllustrator,
		List<String> dnaStrands
	) {
		final Graphics2D canvas =  dnaStrandIllustrator.getCanvas();
		for (String strand : dnaStrands)
			if (strand != null) {
				dnaStrandIllustrator.processDnaStrand(strand);
			}
	}
}