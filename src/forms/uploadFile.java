package forms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import controller.MainController;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class uploadFile {

	private JFrame frame;
	private JFileChooser chooser;
	private String cho = "";

	public uploadFile() {
		initialize();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 441, 189);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel l1 = new JLabel("Upload:");
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setFont(new Font("Tahoma", Font.BOLD, 12));
		l1.setBounds(25, 89, 83, 14);
		frame.getContentPane().add(l1);
		
		JLabel l2 = new JLabel("Algoritam:");
		l2.setHorizontalAlignment(SwingConstants.CENTER);
		l2.setFont(new Font("Tahoma", Font.BOLD, 12));
		l2.setBounds(25, 29, 83, 14);
		frame.getContentPane().add(l2);
		
		JComboBox cb = new JComboBox();
		cb.setModel(new DefaultComboBoxModel(new String[] {"DES", "AES"}));
		cb.setBounds(142, 19, 95, 37);
		frame.getContentPane().add(cb);
		
		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if ( chooser.showOpenDialog(button) == JFileChooser.APPROVE_OPTION ) {
			    	cho = chooser.getSelectedFile().getAbsolutePath();
			    }
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setBounds(142, 78, 95, 34);
		frame.getContentPane().add(button);
		
		JButton btnNewButton = new JButton("Upload");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( !cho.equals("") ) {
					String algo = (String)cb.getItemAt(cb.getSelectedIndex());
				
					if ( MainController.uploadFile(cho, algo) ) {
						MainForm.printMess(frame, "Operacija je uspjesna.");
						frame.dispose();
					}else {
						MainForm.printMess(frame, "Operacija nije uspjesna.");
					}
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBounds(269, 61, 141, 51);
		frame.getContentPane().add(btnNewButton);
		frame.setVisible(true);
	}
}
