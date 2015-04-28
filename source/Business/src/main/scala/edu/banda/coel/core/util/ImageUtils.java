package edu.banda.coel.core.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.Collection;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import com.banda.core.util.RenderedImageUtil;

/**
 * @author © Peter Banda
 * @since 2012
 */
public class ImageUtils {

	/**
	 * The rgb of pixel.
	 */
	private static final int WHITE_RGB = Integer.MAX_VALUE;
	private static final int BLACK_RGB = 0;
	private static final Color[] PALETTE = new Color[] {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, 
		                                                Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.DARK_GRAY, Color.PINK};

	public static Color[] generateColors(int n) {
		Color[] cols = new Color[n];
		for (int i = 0; i < n; i++) {
			cols[i] = Color.getHSBColor((float) i / (float) n, 0.85f, 1.0f);
		}
		return cols;
	}

    public static BufferedImage getComponentAsImage(Component myComponent) {
        Dimension size = myComponent.getSize();
        BufferedImage myImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = myImage.createGraphics();
        myComponent.paint(g2);
        return myImage;
    }

    public static BufferedImage getRandomImage(int width, int height) {
    	Random random = new Random();
		BufferedImage myImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < 1000; i++) {
			myImage.setRGB(random.nextInt(width), new Random().nextInt(height), random.nextInt());			
		}
		return myImage;
    }

    public static BufferedImage generateSimpleAcChart(int width, int height, Collection<Collection<Double>> speciesRunHistorySequences) {
		return generateChart(width, height, speciesRunHistorySequences);
    }

    public static BufferedImage generateChart(int width, int height, Collection<Collection<Double>> serieses) {
    	double min = Double.MAX_VALUE;
    	double max = Double.MIN_VALUE;
    	int maxStep = 0;
    	for (Collection<Double> series : serieses) {
    		maxStep = 0;
			for (double dot : series) {
				min = Math.min(min, dot);
				max = Math.max(max, dot);
				maxStep++;
			}
    	}
		int numberOfDigitsInMaxStep = String.valueOf(maxStep).length();
		BufferedImage myImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = myImage.createGraphics();

		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, myImage.getWidth(), myImage.getHeight());
		graphics.setPaint(Color.BLACK);
		int startx = 30;
		int starty = myImage.getHeight() - 15;
		graphics.drawString(String.valueOf(takeThreeDigitsIfPossible(min)), 0, starty);
		graphics.drawString(String.valueOf(takeThreeDigitsIfPossible(max)), 0, 10);
		graphics.drawString("0", startx, myImage.getHeight());
		graphics.drawString(String.valueOf(maxStep), myImage.getWidth() - (numberOfDigitsInMaxStep * 8), myImage.getHeight());
		graphics.drawLine(startx, starty + 1, startx, 0);
		graphics.drawLine(startx, starty + 1, myImage.getWidth() - 1, starty + 1);

		double scaleSize = Math.abs(max - min);
		double stepSize = (double) (width - startx)/ maxStep;
		int seriesIndex = 0;
		Color[] colors = PALETTE;// generateColors(serieses.length);
		int colorsNum = colors.length;
    	for (Collection<Double> series : serieses) {
    		graphics.setPaint(colors[seriesIndex % colorsNum]);
    		int step = 0;
    		int previousValuePos = 0;
    		int previousStepPos = 0;
			for (double value : series) {
				int valuePos = (int) Math.round((max - value) * starty / scaleSize);
				int stepPos = (int) Math.round(step * stepSize);
				if (step == 0) {
					graphics.drawLine(stepPos + startx, valuePos, stepPos + startx, valuePos);					
				} else {
					graphics.drawLine(previousStepPos + startx, previousValuePos, stepPos + startx, valuePos);
				}
				previousStepPos = stepPos;
				previousValuePos = valuePos;
				step++;
			}
			seriesIndex++;
		}
		return myImage;
    }

    // Optimize me: use log instead
    private static Number takeThreeDigitsIfPossible(double value) {
    	if (value >= 100) {
    		return (int) Math.floor(value);
    	} else if (value >= 10) {
    		return (double) Math.floor(value * 10) / 10;
    	} else {
    		return (double) Math.floor(value * 100) / 100;
    	}
    }

    public static void setComponentAsImage(Component myComponent, String fileName, String format) {
    	RenderedImageUtil.saveImage(getComponentAsImage(myComponent), fileName, format);
    }

	public static String toBase64String(RenderedImage image, String format) {
		byte[] imageInByte = RenderedImageUtil.getImageAsBytes(image, format);
		return Base64.encodeBase64String(imageInByte);
	}

	public static String toBase64StringWithoutNewlines(RenderedImage image, String format) {
		byte[] imageInByte = RenderedImageUtil.getImageAsBytes(image, format);
		String base64String = Base64.encodeBase64String(imageInByte);
		return base64String.replaceAll("\r\n", "");
	}

	public static String toBase64URLSafeStringWithoutNewlines(RenderedImage image, String format) {
		byte[] imageInByte = RenderedImageUtil.getImageAsBytes(image, format);
		String base64String = Base64.encodeBase64URLSafeString(imageInByte);
		return base64String.replaceAll("\r\n", "");
	}
}
