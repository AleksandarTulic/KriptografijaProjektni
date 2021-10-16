package forms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;

import controller.MainController;
import helpFunctions.Check;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class createFile {

	private JFrame frame;
	private JTextField t1;
	private JTextArea  t2;
	@SuppressWarnings("rawtypes")
	private JComboBox cb;

	public createFile() {
		initialize();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 592, 424);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel l1 = new JLabel("Naziv:");
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setFont(new Font("Tahoma", Font.BOLD, 12));
		l1.setBounds(25, 31, 61, 14);
		frame.getContentPane().add(l1);
		
		t1 = new JTextField();
		t1.setToolTipText("Samo slova i brojeve. Nemojte navoditi ekstenzije.");
		t1.setBounds(96, 29, 178, 20);
		frame.getContentPane().add(t1);
		t1.setColumns(10);
		
		JLabel l2 = new JLabel("Sadrzaj:");
		l2.setHorizontalAlignment(SwingConstants.CENTER);
		l2.setFont(new Font("Tahoma", Font.BOLD, 12));
		l2.setBounds(25, 82, 71, 14);
		frame.getContentPane().add(l2);
		
		t2 = new JTextArea();
		t2.setColumns(10);
		t2.setBounds(35, 107, 323, 255);
		frame.getContentPane().add(t2);
		
		JButton button = new JButton("Execute");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( t1.getText().length() != 0 && t2.getText().length() != 0 ) {
					if ( Check.checkNameFile(t1.getText()) ) {
						MainController.createFile(t1.getText(), t2.getText(), (String)cb.getItemAt(cb.getSelectedIndex()));
					}
				}
			}
		});
		button.setBackground(Color.WHITE);
		button.setFont(new Font("Tahoma", Font.BOLD, 18));
		button.setBounds(387, 297, 171, 65);
		frame.getContentPane().add(button);
		
		cb = new JComboBox();
		cb.setModel(new DefaultComboBoxModel(new String[] {"DES", "AES"}));
		cb.setFont(new Font("Tahoma", Font.BOLD, 12));
		cb.setBounds(387, 28, 171, 22);
		frame.getContentPane().add(cb);
		frame.setVisible(true);
	}
}
