
package dev.trifleneurotic.nightingale.tileengine2d.level;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * The Class LevelTMX.
 */
public class LevelTMX extends DefaultHandler implements Level {
	private int width;
	private int height;

	private String tilesetImageName; 

	private ArrayList matrix_layers = new ArrayList();

	private int tmp_x;
	private int tmp_y;

	/**
	 * Instantiates a new level tmx.
	 * 
	 * @param is the InputStream
	 */
	public LevelTMX(InputStream is) {
		tmp_x = 0;
		tmp_y = 0;
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();
			sp.parse(is, this);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.level.Level#getTileSetImageName()
	 */
	public String getTilesetImageName() {
		return tilesetImageName;
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.level.Level#getColumnNumber()
	 */
	public int getColumnNumber() {
		return width;
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.level.Level#getRowNumber()
	 */
	public int getRowNumber() {
		return height;
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.level.Level#getTile(int, int, int)
	 */
	public byte getTile(int layer, int i, int j) {
		try {
			return ((byte[][])matrix_layers.get(layer))[i][j];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("map")) {
			width = Integer.parseInt(attributes.getValue("width"));
			height = Integer.parseInt(attributes.getValue("height"));
		}
		if (qName.equals("layer")) {
			byte[][] matrix = new byte[width][];
			for (int i = 0; i < matrix.length; i++) {
				matrix[i] = new byte[height];
			}
			matrix_layers.add(matrix);
		}
		if (qName.equals("image")) {
			tilesetImageName = attributes.getValue("source");
		}
		if (qName.equals("tile")) {
			((byte[][])matrix_layers.get(matrix_layers.size()-1))[tmp_x][tmp_y] = Byte.parseByte(attributes.getValue("gid"));
			tmp_x++;
			if (tmp_x > width-1) {
				tmp_x = 0;
				tmp_y++;
				if (tmp_y > height-1) {
					tmp_y = 0;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see southatlantic.tileengine2d.level.Level#getLayerNumber()
	 */
	public int getLayerNumber() {
		return matrix_layers.size();
	}

	public void setTile(int layer, int i, int j, byte value) {
		((byte[][])matrix_layers.get(layer))[i][j] = value;
	}
}
