package forms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

import controller.MainController;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class downloadFile {

	private JFrame frame;
	private JTextField t1;
	private String cho = "";
	private JFileChooser chooser;

	public downloadFile() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 424, 154);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel l1 = new JLabel("File:");
		l1.setFont(new Font("Tahoma", Font.BOLD, 12));
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setBounds(20, 25, 72, 14);
		frame.getContentPane().add(l1);
		
		t1 = new JTextField();
		t1.setBounds(102, 23, 121, 20);
		frame.getContentPane().add(t1);
		t1.setColumns(10);
		
		JLabel l2 = new JLabel("Download:");
		l2.setHorizontalAlignment(SwingConstants.CENTER);
		l2.setFont(new Font("Tahoma", Font.BOLD, 12));
		l2.setBounds(20, 74, 76, 14);
		frame.getContentPane().add(l2);
		
		JButton b1 = new JButton("...");
		b1.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if ( chooser.showOpenDialog(b1) == JFileChooser.APPROVE_OPTION ) {
			    	cho = chooser.getSelectedFile().getAbsolutePath();
			    }
			}
		});
		b1.setFont(new Font("Tahoma", Font.BOLD, 14));
		b1.setBounds(102, 61, 121, 42);
		frame.getContentPane().add(b1);
		
		JButton b2 = new JButton("Download");
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( !t1.getText().equals("") && !cho.equals("") ) {
					if ( MainController.downloadFile(t1.getText(), cho) ) {
						MainForm.printMess(frame, "Operacija je uspjesna.");
						frame.dispose();
					}else {
						MainForm.printMess(frame, "Operacija nije uspjesna.");
					}
				}
			}
		});
		b2.setFont(new Font("Tahoma", Font.BOLD, 18));
		b2.setBounds(251, 41, 143, 62);
		frame.getContentPane().add(b2);
		frame.setVisible(true);
	}
}
