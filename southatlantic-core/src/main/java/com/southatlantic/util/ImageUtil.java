
package com.southatlantic.util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * The Class ImageUtil.
 */
public class ImageUtil {
	
	/**
	 * Rotate image.
	 * 
	 * @param img the img
	 * @param degrees the degrees
	 * 
	 * @return the image
	 */
	public static Image rotateImage(Image img, int degrees) {
		BufferedImage sourceBI = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		sourceBI.getGraphics().drawImage(img, 0, 0, null);
		AffineTransform at = new AffineTransform();
		if (degrees < 0) degrees += 360;
		at.rotate(degrees * Math.PI / 180.0, sourceBI.getWidth()/2.0, sourceBI.getHeight()/2.0);
		at.preConcatenate(findTranslation(at, sourceBI, degrees));
		return new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR).filter(sourceBI, null);
	}

	private static AffineTransform findTranslation(AffineTransform at, BufferedImage bi, int degrees) {
		Point2D p2din1 = null, p2din2 = null, p2dout = null;
		if (degrees >= 0 && degrees < 90) {
			p2din1 = new Point2D.Double(0.0, 0.0);
			p2din2 = new Point2D.Double(0.0, bi.getHeight());
		}
		else if (degrees < 180) {
			p2din1 = new Point2D.Double(0.0, bi.getHeight());
			p2din2 = new Point2D.Double(bi.getWidth(), bi.getHeight());
		}
		else if (degrees < 270) {
			p2din1 = new Point2D.Double(bi.getWidth(), bi.getHeight());
			p2din2 = new Point2D.Double(bi.getWidth(), 0);
		}
		else if (degrees < 360) {
			p2din1 = new Point2D.Double(bi.getWidth(), 0);
			p2din2 = new Point2D.Double(0, 0);
		}
		p2dout = at.transform(p2din1, null);
		double ytrans = p2dout.getY();

		p2dout = at.transform(p2din2, null);
		double xtrans = p2dout.getX();

		AffineTransform tat = new AffineTransform();
		tat.translate(-xtrans, -ytrans);
		return tat;
	}
}
