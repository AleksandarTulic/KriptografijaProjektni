package forms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import certificate.Certificate_;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class LogIn {
	private JFrame frame;
	private JTextField t1;
	private JPasswordField p1;
	private JFileChooser chooser;
	private String certPath;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn window = new LogIn();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LogIn() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 394, 456);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel l1 = new JLabel("");
		l1.setIcon(new ImageIcon(System.getProperty("user.dir") + File.separator + "icons" + File.separator + "icon_1.png"));
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setBounds(118, 23, 131, 131);
		frame.getContentPane().add(l1);
		
		t1 = new JTextField();
		t1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		t1.setBounds(154, 203, 146, 20);
		frame.getContentPane().add(t1);
		t1.setColumns(10);
		
		p1 = new JPasswordField();
		p1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		p1.setBounds(154, 255, 146, 20);
		frame.getContentPane().add(p1);
		
		JButton button1 = new JButton("Log In");
		button1.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				if ( certPath != null ) {
					MainForm.user = new Certificate_(t1.getText(), certPath);
					
					boolean flag = false;
					if ( MainForm.user.checkLogIn(String.valueOf(p1.getPassword())) && chooser != null ) {
						if ( MainForm.user.checkCertificate() ) {
							frame.setVisible(false);
							flag = true;
							MainForm mf = new MainForm(frame);
						}
					}
					
					if ( !flag ) {
						MainForm.printMess(frame, "Operacija nije uspjesna.");
					}
				}
			}
		});
		button1.setBackground(Color.WHITE);
		button1.setFont(new Font("Tahoma", Font.BOLD, 18));
		button1.setBounds(87, 352, 208, 41);
		frame.getContentPane().add(button1);
		
		JLabel l2 = new JLabel("UserName:");
		l2.setFont(new Font("Tahoma", Font.BOLD, 14));
		l2.setBounds(63, 206, 81, 14);
		frame.getContentPane().add(l2);
		
		JLabel l3 = new JLabel("Password:");
		l3.setFont(new Font("Tahoma", Font.BOLD, 14));
		l3.setBounds(72, 258, 72, 14);
		frame.getContentPane().add(l3);
		
		JButton button2 = new JButton("...");
		button2.setBackground(Color.WHITE);
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    chooser.setAcceptAllFileFilterUsed(false);
			    
			    if ( chooser.showOpenDialog(button2) == JFileChooser.APPROVE_OPTION ) {
			    	certPath = chooser.getSelectedFile().getAbsolutePath();
			    }
			}
		});
		button2.setBounds(178, 300, 35, 23);
		frame.getContentPane().add(button2);
		
		JLabel l4 = new JLabel("Path:");
		l4.setFont(new Font("Tahoma", Font.BOLD, 14));
		l4.setBounds(122, 302, 46, 14);
		frame.getContentPane().add(l4);
	}
	
	public void setVisibility(boolean flag) {
		frame.setVisible(flag);
	}
}
