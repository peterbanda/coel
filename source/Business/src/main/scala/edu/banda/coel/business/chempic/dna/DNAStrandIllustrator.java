package edu.banda.coel.business.chempic.dna;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author © Peter Banda
 * @since 2013
 */
public abstract class DNAStrandIllustrator {

	private static interface Default {
		final int LONG_HORIZONTAL_DOMAIN_LENGTH = 55;
		final int SHORT_HORIZONTAL_DOMAIN_LENGTH = 20;
		final int LONG_DIAGONAL_DOMAIN_HEIGHT = 40;
		final int SHORT_DIAGONAL_DOMAIN_HEIGHT = 15;
		final int GAP_BETWEEN_HORIZONTAL_DOMAINS = 6;
		final int CONCATENATION_GAP = 6;
		final int LINE_WIDTH = 2;
	}

	private int longHorizontalDomainLength = Default.LONG_HORIZONTAL_DOMAIN_LENGTH;
	private int shortHorizontalDomainLength = Default.SHORT_HORIZONTAL_DOMAIN_LENGTH;
	private int longDiagonalDomainLength = Default.LONG_DIAGONAL_DOMAIN_HEIGHT;
	private int shortDiagonalDomainLength = Default.SHORT_DIAGONAL_DOMAIN_HEIGHT;
	private int gapBetweenHorizonalDomains = Default.GAP_BETWEEN_HORIZONTAL_DOMAINS;
	private int concatenationGap = Default.CONCATENATION_GAP;
	
	private int lineWidth = Default.LINE_WIDTH;

	protected int xPos;
	protected int yPos;
	protected boolean drawingEnabled = true;
	protected Graphics2D canvas;

	public DNAStrandIllustrator(int xPos, int yPos, Graphics2D canvas) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.canvas = canvas;
	}

	public abstract void drawDnaStrand(String dnaStrand);

	// no drawing just moving x and y position
	public abstract void processDnaStrand(String dnaStrand);

	protected void drawDomain(
		Color color,
		DNADomainType type, 
		DNADomainOrientation orientation,
		String domainLabel,
		boolean placeTextAbove
	) {
		if (!drawingEnabled)
			return;

		int domainLength = getDomainLength(type, orientation);
		canvas.setPaint(color);
		canvas.setStroke(new BasicStroke(lineWidth));
		int xTextPos = xPos;
		int yTextPos = yPos;
		double rotate = 0;
		switch (orientation) {
			case DownRightDiagonal:
				canvas.drawLine(xPos, yPos, xPos + domainLength, yPos + domainLength);
				yTextPos = placeTextAbove ? yPos : yPos + 2 * domainLength / 3;
				xTextPos = xPos + (domainLength / 2);
				rotate = 45;
				break;
			case UpRightDiagonal:
				canvas.drawLine(xPos, yPos, xPos + domainLength, yPos - domainLength);
				yTextPos = placeTextAbove ? yPos - 2 * domainLength / 3 : yPos;
				xTextPos = xPos + (domainLength / 2);
				rotate = -45;
				break;
			case DownLeftDiagonal:
				canvas.drawLine(xPos, yPos, xPos - domainLength, yPos + domainLength);
				yTextPos = placeTextAbove ? yPos : yPos + 2 * domainLength / 3;
				xTextPos = xPos - (domainLength / 2);
				rotate = -45;
				break;
			case UpLeftDiagonal:
				canvas.drawLine(xPos, yPos, xPos - domainLength, yPos - domainLength);
				yTextPos = placeTextAbove ? yPos - 2 * domainLength / 3 : yPos;
				xTextPos = xPos - (domainLength / 2);
				rotate = 45;
				break;
			case Horizontal:
				canvas.drawLine(xPos, yPos, xPos + domainLength, yPos);
				yTextPos = placeTextAbove ? yPos - 3 : yPos;
				xTextPos = xPos + (domainLength / 2);
				break;
		}
		canvas.setPaint(Color.BLACK);
		canvas.setStroke(new BasicStroke());
		int fontHeight = canvas.getFont().getSize();
		if (!placeTextAbove) {
			yTextPos += fontHeight;
		}
		double fontWidth = canvas.getFontMetrics().stringWidth(domainLabel); 
		xTextPos -= fontWidth / 2;
		canvas.rotate(Math.toRadians(rotate), xTextPos + fontWidth / 2, yTextPos - fontHeight / 2);
		canvas.drawString(domainLabel, xTextPos, yTextPos);
		canvas.rotate(-Math.toRadians(rotate), xTextPos + fontWidth / 2, yTextPos - fontHeight / 2);
	}

	protected int getDomainLength(DNADomainType type, DNADomainOrientation orientation) {
		int domainLength = 0;
		switch (type) {
			case Long:
				if (orientation == DNADomainOrientation.Horizontal) {
					domainLength = longHorizontalDomainLength;
				} else {
					domainLength = longDiagonalDomainLength;
				}
				break;
			case Short:
				if (orientation == DNADomainOrientation.Horizontal) {
					domainLength = shortHorizontalDomainLength;
				} else {
					domainLength = shortDiagonalDomainLength;
				}
				break;
			case Gap:
				if (orientation == DNADomainOrientation.Horizontal) {
					domainLength = concatenationGap;
				} else
					throw new RuntimeException("The orientation " + orientation + " for the Gap domain type is not supported.");
				break;
		}
		return domainLength;
	}
	
	protected void movePosition(DNADomainType type, DNADomainOrientation orientation) {
		int domainLength = getDomainLength(type, orientation);
		switch (orientation) {
			case Horizontal:
				xPos += domainLength;
				break;
			case UpLeftDiagonal:
				xPos -= domainLength;
				yPos -= domainLength;
				break;
			case UpRightDiagonal:
				xPos += domainLength;
				yPos -= domainLength;
				break;
			case DownLeftDiagonal:
				xPos -= domainLength;
				yPos += domainLength;
				break;
			case DownRightDiagonal:
				xPos += domainLength;
				yPos += domainLength;
				break;
		}
	}

	public int getLongHorizontalDomainLength() {
		return longHorizontalDomainLength;
	}

	public void setLongHorizontalDomainLength(int longHorizontalDomainLength) {
		this.longHorizontalDomainLength = longHorizontalDomainLength;
	}

	public int getLongDiagonalDomainLength() {
		return longDiagonalDomainLength;
	}

	public void setLongDiagonalDomainLength(int longDiagonalDomainLength) {
		this.longDiagonalDomainLength = longDiagonalDomainLength;
	}

	public int getShortHorizontalDomainLength() {
		return shortHorizontalDomainLength;
	}

	public void setShortHorizontalDomainLength(int shortHorizontalDomainLength) {
		this.shortHorizontalDomainLength = shortHorizontalDomainLength;
	}

	public int getShortDiagonalDomainLength() {
		return shortDiagonalDomainLength;
	}

	public void setShortDiagonalDomainLength(int shortDiagonalDomainLength) {
		this.shortDiagonalDomainLength = shortDiagonalDomainLength;
	}

	public int getGapBetweenHorizonalDomains() {
		return gapBetweenHorizonalDomains;
	}

	public void setGapBetweenHorizonalDomains(int gapBetweenHorizonalDomains) {
		this.gapBetweenHorizonalDomains = gapBetweenHorizonalDomains;
	}

	public int getConcatenationGap(){
		return concatenationGap;
	}
	
	public void setConcatenationGap(int concatenationGap){
		this.concatenationGap = concatenationGap;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setXPos(int xPos) {
		this.xPos = xPos;
	}

	public void setYPos(int yPos) {
		this.yPos = yPos;
	}

	public Graphics2D getCanvas() {
		return canvas;
	}
}