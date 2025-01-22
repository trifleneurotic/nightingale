/*
 * Created on 10.09.2007
 *
 */
package cb_painter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;


import java.util.Iterator;

public class CBPainter extends JFrame implements MouseListener, MouseMotionListener {
	private Image image;
	private Dimension tile_grid = new Dimension(1,1);
	private int index = 0;
	private JPanel image_panel;
	private BboxSet bbox_set;
	private Rectangle tmp_rec;
	
	public static void main(String[] args) {
		createAndShowGui();
	}

	private static void createAndShowGui() {
		JFrame frame = new CBPainter();
		frame.pack();
		frame.setVisible(true);
	}
	
	public CBPainter() {
		bbox_set = new BboxSet(1);
		final JFrame parent = this;
		image_panel = new JPanel() {
			public void paint(Graphics g) {
				if (image != null) {
					int tile_width = image.getWidth(null) / tile_grid.width;
					int tile_height = image.getHeight(null) / tile_grid.height;
					int x = (index % tile_grid.width) * tile_width;
					int y = (index / tile_grid.width) * tile_height;
					g.setColor(Color.green);
					g.fillRect(0, 0, tile_width, tile_height);
					g.drawImage(image, 0, 0, tile_width, tile_height, x, y, x + tile_width, y + tile_height, this);
					g.setColor(Color.red);
					ArrayList rectangles = bbox_set.getRectangleList(index);
					if (rectangles != null) {
						Iterator it = rectangles.iterator();
						while (it.hasNext()) {
							Rectangle rec = (Rectangle)it.next();
							g.drawRect(rec.x, rec.y, rec.width, rec.height);
						}
					}
					if (tmp_rec != null) {
						g.setColor(Color.white);
						g.drawRect(tmp_rec.x, tmp_rec.y, tmp_rec.width, tmp_rec.height);
					}
				}
			}
		};
		image_panel.setPreferredSize(new Dimension(200, 200));
		image_panel.setBackground(Color.black);
		image_panel.addMouseListener(this);
		image_panel.addMouseMotionListener(this);

		JPanel control_panel = new JPanel();
		JButton vor_button = new JButton(">");
		vor_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index++;
				if (index >= tile_grid.width*tile_grid.height) {
					index = 0;
				}
				parent.pack();
				parent.repaint();
			}
		});
		JButton zurueck_button = new JButton ("<");
		zurueck_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				index--;
				if (index < 0) {
					index = tile_grid.width*tile_grid.height - 1;
				}
				parent.pack();
				parent.repaint();
			}
		});
		JButton clear_button = new JButton ("Clear");
		clear_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bbox_set.clearFrame(index);
				parent.pack();
				parent.repaint();
			}
		});
		control_panel.add(zurueck_button);
		control_panel.add(vor_button);
		control_panel.add(new JSeparator());
		control_panel.add(clear_button);
		
		JMenuBar menu_bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu_bar.add(menu);
		JMenuItem menu_item = new JMenuItem("Open Image...");
		menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser file_chooser = new JFileChooser();
				int returnVal = file_chooser.showOpenDialog(parent);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					image = Toolkit.getDefaultToolkit().getImage(file_chooser.getSelectedFile().getAbsolutePath()); 
					parent.pack();
					parent.repaint();
				}
			}
		});
		menu.add(menu_item);
		menu_item = new JMenuItem("Save...");
		menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser file_chooser = new JFileChooser();
				int returnVal = file_chooser.showSaveDialog(parent);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					BboxWriter writer = new BboxWriter();
					try {
						writer.writeBboxes(bbox_set, file_chooser.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(parent, e1.getMessage(), e1.getClass().toString(),
								JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
				}
			}
		});
		menu.add(menu_item);
		menu = new JMenu("Settings");
		menu_bar.add(menu);
		menu_item = new JMenuItem("Tile Dimensions");
		menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HashMap parameters = new HashMap();
				parameters.put("x", new Integer(tile_grid.width).toString());
				parameters.put("y", new Integer(tile_grid.height).toString());
				final ConfigDialog config_dialog = new ConfigDialog(parent, parameters);
				config_dialog.addOKButtonActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						HashMap parameters = config_dialog.getParameters();
						tile_grid.setSize(Integer.parseInt((String)parameters.get("x")), 
								Integer.parseInt((String)parameters.get("y")));
						config_dialog.setVisible(false);
						bbox_set = new BboxSet(tile_grid.width*tile_grid.height);
						int tile_width = image.getWidth(null) / tile_grid.width;
						int tile_height = image.getHeight(null) / tile_grid.height;
						image_panel.setPreferredSize(new Dimension(tile_width, tile_height));
						parent.pack();
						parent.repaint();
					}
				});
				config_dialog.addCancelButtonActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						config_dialog.setVisible(false);
					}
				});
				config_dialog.setVisible(true);
			}
		});
		menu.add(menu_item);
		
		this.setJMenuBar(menu_bar);
		Container content_pane = this.getContentPane();
		content_pane.add(image_panel);
		content_pane.add(control_panel);
		content_pane.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(content_pane, 2, 1, 6, 6, 6, 6);

	}
	
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		if ((infoflags | 0) > 0) {
			if (image != null) {
				int tile_width = image.getWidth(null) / tile_grid.width;
				int tile_height = image.getHeight(null) / tile_grid.height;
				image_panel.setPreferredSize(new Dimension(tile_width, tile_height));
				this.pack();
				this.repaint();
			}
		}
		return true;
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {
		bbox_set.mouseExited();
		tmp_rec = null;
		this.repaint();
	}

	public void mousePressed(MouseEvent e) {
		int tile_width = image.getWidth(null) / tile_grid.width;
		int tile_height = image.getHeight(null) / tile_grid.height;
		if (e.getX() <= tile_width && e.getY() <= tile_height) {
			bbox_set.mousePressed(e);
			tmp_rec = new Rectangle(e.getX(), e.getY(), 0, 0);
		}
	}

	public void mouseReleased(MouseEvent e) {
		int tile_width = image.getWidth(null) / tile_grid.width;
		int tile_height = image.getHeight(null) / tile_grid.height;
		if (e.getX() <= tile_width && e.getY() <= tile_height) {
			tmp_rec = null;
			bbox_set.mouseReleased(e, index);
			this.repaint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		tmp_rec.setSize(e.getX()-tmp_rec.x, e.getY()-tmp_rec.y);
		this.repaint();
	}

	public void mouseMoved(MouseEvent arg0) {}
}
