package forms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Desktop;
import java.awt.Font;
import javax.swing.SwingConstants;

import controller.MainController;
import cryptoAlgo.Sha;
import helpFunctions.TreeFileSystemController;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class openFile {

	private JFrame frame;
	private JTextField t1;

	public openFile() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Path start = Paths.get(MainForm.user.getHomePath() + File.separator + "temporary");
				List<String> files = null;
				try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)){
					files = stream.map(String::valueOf).sorted().collect(Collectors.toList());
				}catch (IOException eee) {
					eee.printStackTrace();
				}
				
				for (String i : files) {
					ArrayList <String> arr = TreeFileSystemController.splitPath(i);
					String file = arr.get(arr.size()-1);
					
					MainController.deleteFile("temporary" + File.separator + file);
				}
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 474, 121);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel l1 = new JLabel("File:");
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setFont(new Font("Tahoma", Font.BOLD, 12));
		l1.setBounds(22, 29, 46, 14);
		frame.getContentPane().add(l1);
		
		t1 = new JTextField();
		t1.setToolTipText("Navedite i ekstenziju");
		t1.setBounds(95, 26, 155, 20);
		frame.getContentPane().add(t1);
		t1.setColumns(10);
		
		JButton button = new JButton("Open");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( !t1.getText().equals("") ) {
					File f = new File(MainForm.user.getHomePath() + File.separator + t1.getText());
					if ( f.exists() ) {
						boolean flag = Sha.checkSignatureMD5(t1.getText());
						if ( flag ) {
							MainController.downloadFile(t1.getText(), MainForm.user.getHomePath() + File.separator + "temporary");
							
							File ff = new File(MainForm.user.getHomePath() + File.separator + "temporary" + File.separator + t1.getText());
							
							try {
								Desktop.getDesktop().open(ff);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 18));
		button.setBounds(305, 11, 111, 56);
		frame.getContentPane().add(button);
		frame.setVisible(true);
	}

}
