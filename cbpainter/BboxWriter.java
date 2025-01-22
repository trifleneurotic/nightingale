package cb_painter;

import java.awt.Rectangle;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;



public class BboxWriter 
{
	public void writeBboxes(BboxSet bboxes, String filename) throws Exception {
		OutputStream os = new FileOutputStream(filename);

		if (filename.endsWith(".tmx.gz")) {
			os = new GZIPOutputStream(os);
		}

		Writer writer = new OutputStreamWriter(os);
		XMLWriter xmlWriter = new XMLWriter(writer);

		xmlWriter.startDocument();
		writeBboxes(bboxes, xmlWriter, filename);
		xmlWriter.endDocument();

		writer.flush();

		if (os instanceof GZIPOutputStream) {
			((GZIPOutputStream)os).finish();
		}
	}


	private void writeBboxes(BboxSet bbox_set, XMLWriter w, String wp) throws IOException {
		w.startElement("bboxes");

		w.writeAttribute("version", "0.1");

		w.writeAttribute("frame_number", bbox_set.size());

//		writeProperties(map.getProperties(), w);

		ArrayList list = bbox_set.getFrames();
		Iterator it = list.iterator();
		int index = 0;
		while (it.hasNext()) {
			ArrayList frame_boxes = (ArrayList)it.next();
			writeFrameBoxes(frame_boxes, index++, w);
		}

		w.endElement();
	}

	private void writeFrameBoxes(ArrayList frame_boxes, int index, XMLWriter w) throws IOException {
//		Preferences prefs = TiledConfiguration.node("saving");
//		boolean encodeLayerData =
//		prefs.getBoolean("encodeLayerData", true);
//		boolean compressLayerData =
//		prefs.getBoolean("layerCompression", true) &&
//		encodeLayerData;

		w.startElement("frame");

		w.writeAttribute("index", index);  
		if (frame_boxes == null) {
			w.writeAttribute("rectangle_number", 0);
		} else {
			w.writeAttribute("rectangle_number", frame_boxes.size());

//			writeProperties(l.getProperties(), w);

			Iterator it = frame_boxes.iterator();
			while (it.hasNext()) {
				Rectangle rec = (Rectangle)it.next();
				w.startElement("rec");
				w.writeAttribute("x", rec.x);
				w.writeAttribute("y", rec.y);
				w.writeAttribute("width", rec.width);
				w.writeAttribute("height", rec.height);
				w.endElement();
			}
		}
		w.endElement();
	}
}
