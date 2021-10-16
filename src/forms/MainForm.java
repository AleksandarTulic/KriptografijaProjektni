package forms;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import certificate.Certificate_;
import helpFunctions.TreeFileSystemController;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainForm {
	public static Certificate_ user;
	public static JTree tree;
	private JFrame frame;
	
	@SuppressWarnings("unused")
	private JFrame logIn;

	public MainForm(JFrame logIn) {
		this.logIn = logIn;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 660, 547);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton b1 = new JButton("Create File");
		b1.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				createFile a = new createFile();
			}
		});
		b1.setFont(new Font("Tahoma", Font.BOLD, 14));
		b1.setBounds(477, 30, 130, 40);
		frame.getContentPane().add(b1);
		
		JButton b2 = new JButton("Open File");
		b2.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				openFile a = new openFile();
			}
		});
		b2.setFont(new Font("Tahoma", Font.BOLD, 14));
		b2.setBounds(477, 81, 130, 40);
		frame.getContentPane().add(b2);
		
		JButton b3 = new JButton("Upload file");
		b3.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				uploadFile a = new uploadFile();
			}
		});
		b3.setFont(new Font("Tahoma", Font.BOLD, 14));
		b3.setBounds(477, 133, 130, 40);
		frame.getContentPane().add(b3);
		
		JButton b4 = new JButton("Download File");
		b4.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				downloadFile a = new downloadFile();
			}
		});
		b4.setFont(new Font("Tahoma", Font.BOLD, 14));
		b4.setBounds(477, 184, 130, 40);
		frame.getContentPane().add(b4);
		
		JButton b5 = new JButton("Change File");
		b5.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				changeFile a = new changeFile();
			}
		});
		b5.setFont(new Font("Tahoma", Font.BOLD, 14));
		b5.setBounds(477, 235, 130, 40);
		frame.getContentPane().add(b5);
		
		JButton b6 = new JButton("Delete File");
		b6.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				deleteForm a = new deleteForm();
			}
		});
		b6.setFont(new Font("Tahoma", Font.BOLD, 14));
		b6.setBounds(477, 286, 130, 40);
		frame.getContentPane().add(b6);
		
		JButton b7 = new JButton("Send File");
		b7.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				shareForm a = new shareForm();
			}
		});
		b7.setFont(new Font("Tahoma", Font.BOLD, 14));
		b7.setBounds(477, 337, 130, 40);
		frame.getContentPane().add(b7);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(MainForm.user.getUserName());
		DefaultTreeModel model = new DefaultTreeModel(root);
	    tree = new JTree(model);
	    
	    TreeFileSystemController tfsc = new TreeFileSystemController();
	    tfsc.buildTree(model, 1);
	    
		JScrollPane sc = new JScrollPane(tree);
		sc.setBounds(10, 11, 432, 475);
		frame.getContentPane().add(sc);
		
		JButton b8 = new JButton("Refresh FS");
		b8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfsc.buildTree(model, 2);
			}
		});
		b8.setFont(new Font("Tahoma", Font.BOLD, 14));
		b8.setBounds(477, 445, 130, 41);
		frame.getContentPane().add(b8);
		
		JButton b9 = new JButton("Upload Sent");
		b9.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
				uploadSent a = new uploadSent();
			}
		});
		b9.setFont(new Font("Tahoma", Font.BOLD, 14));
		b9.setBounds(477, 394, 130, 40);
		frame.getContentPane().add(b9);
		frame.setVisible(true);
	}

	public static void printMess(JFrame f, String mess) {
		JOptionPane.showMessageDialog(f, mess);
	}
}
