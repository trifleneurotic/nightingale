
package com.southatlantic.tileengine2d.collision;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.southatlantic.tileengine2d.util.TileMath;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * The Class GeomTable. Manages a list of CollisionGeoms.
 */
public class GeomTable extends DefaultHandler {

	private ArrayList geom_table;
	private ArrayList frame_tmp;
	private boolean pixel = false;

	private int source_tilesize_x;
	private int source_tilesize_y;

	/**
	 * Instantiates an empty GeomTable. 
	 */
	public GeomTable() {
		geom_table = new ArrayList();
	}

	/**
	 * Instantiates a new geom table. Information in millitiles.
	 * 
	 * @param is the xml-InputStream
	 */
	public GeomTable(InputStream is) {
		geom_table = new ArrayList();
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

	/**
	 * Instantiates a new geom table. Information in pixel.
	 * 
	 * @param is the is
	 * @param _source_tilesize_x the _source_tilesize_x
	 * @param _source_tilesize_y the _source_tilesize_y
	 */
	public GeomTable(InputStream is, int _source_tilesize_x, int _source_tilesize_y) {
		geom_table = new ArrayList();
		pixel = true;
		source_tilesize_x = _source_tilesize_x;
		source_tilesize_y = _source_tilesize_y;
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

	/**
	 * Gets the geoms of a specified frame.
	 * 
	 * @param index the index
	 * 
	 * @return the CollisionGeoms
	 */
	public ArrayList getGeomsOfFrame(int index) {
		if (geom_table.size() > index) {
			return (ArrayList)geom_table.get(index);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("frame")) {
			frame_tmp = new ArrayList();
			geom_table.add(frame_tmp);
		}
		if (qName.equals("rec")) {
			int x = Integer.parseInt(attributes.getValue("x"));
			int y = Integer.parseInt(attributes.getValue("y"));
			int width = Integer.parseInt(attributes.getValue("width"));
			int height = Integer.parseInt(attributes.getValue("height"));
			if (pixel) {
				x = TileMath.Px2Mt(x, source_tilesize_x);
				y = TileMath.Px2Mt(y, source_tilesize_y);
				width = TileMath.Px2Mt(width, source_tilesize_x);
				height = TileMath.Px2Mt(height, source_tilesize_y);
			}
			frame_tmp.add(new CollisionGeom(new Rectangle(x, y, width, height)));
		}
	}

	/**
	 * Gets the table.
	 * 
	 * @return the table
	 */
	public ArrayList getTable() {
		return geom_table;
	}

	/**
	 * Adds a CollisionGeom.
	 * 
	 * @param seq_index the seq_index
	 * @param geom the geom
	 */
	public void addGeom(int seq_index, CollisionGeom geom) {
		if (geom_table.size() < seq_index || seq_index < 0) {
			return;
		}
		if (geom_table.size() == seq_index) {
			ArrayList tmp = new ArrayList();
			tmp.add(geom);
			geom_table.add(tmp);
			return;
		}
		((ArrayList)geom_table.get(seq_index)).add(geom);
	}
}
