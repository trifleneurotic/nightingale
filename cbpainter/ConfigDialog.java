package cb_painter;

/*
 * Created on 04.04.2007
 */

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;


/**
 * GUI-List, that contains Objects of type <code>request.ModelSource</code>.
 * @author Johannes Vogt
 */
public class ConfigDialog extends JDialog {
	private HashMap parameters;
	private HashMap textfields;
	
	JButton ok_button;
	JButton cancel_button;
	
	public ConfigDialog(JFrame r, HashMap _parameters) {
		super(r, "", true);
//		JDialog.setDefaultLookAndFeelDecorated(true);
		
		parameters = _parameters;
		textfields = new HashMap();
		
		JPanel p = new JPanel(new SpringLayout());
		Iterator it = parameters.keySet().iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			String value = (String)parameters.get(key);
			JLabel name_label = new JLabel(key, JLabel.TRAILING);
			p.add(name_label);
			JTextField name_field = new JTextField(20);
			name_field.setText(value);
			name_label.setLabelFor(name_field);
			p.add(name_field);
			textfields.put(key, name_field);
		}
		SpringUtilities.makeCompactGrid(p, parameters.size(), 2, 6, 6, 6, 6);
		p.setBorder(BorderFactory.createLoweredBevelBorder());

		JPanel button_panel = new JPanel();
		ok_button = new JButton("OK");
		cancel_button = new JButton("Abbrechen");
		button_panel.add(ok_button);
		button_panel.add(cancel_button);
		button_panel.setBorder(BorderFactory.createLoweredBevelBorder());

		ok_button.requestFocusInWindow();
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		
		JPanel main_panel = new JPanel();
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.Y_AXIS));
		main_panel.add(p);
		main_panel.add(button_panel);
		
		main_panel.setOpaque(true);
		this.setContentPane(main_panel);
		this.getRootPane().setDefaultButton(ok_button);
		this.pack();
	}
	
	public void addOKButtonActionListener(ActionListener actionlistener) {
		ok_button.addActionListener(actionlistener);
	}

	public void addCancelButtonActionListener(ActionListener actionlistener) {
		cancel_button.addActionListener(actionlistener);
	}
	
	public HashMap getParameters() {
		HashMap new_params = new HashMap();
		Iterator it = textfields.keySet().iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			String value = ((JTextField)textfields.get(key)).getText();
			new_params.put(key, value);
		}
		return new_params;
	}
}
