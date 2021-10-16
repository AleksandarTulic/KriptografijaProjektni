package forms;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.SwingConstants;

import controller.MainController;
import helpFunctions.TreeFileSystemController;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class shareForm {

	private JFrame frame;
	@SuppressWarnings("rawtypes")
	private JComboBox cb1;
	@SuppressWarnings("rawtypes")
	private JComboBox cb2;

	@SuppressWarnings("unchecked")
	public shareForm() {
		initialize();
		
		Path start = Paths.get("home");
		List<String> files = null;
		try (Stream<Path> stream = Files.walk(start, 1)){
			files = stream.map(String::valueOf).sorted().collect(Collectors.toList());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i=0;i<files.size();i++) {
			ArrayList <String> arr = TreeFileSystemController.splitPath(files.get(i));
			String inp = arr.get(arr.size()-1);
			
			if ( !inp.equals("home") && !inp.equals(MainForm.user.getUserName()) ) {
				cb1.addItem(arr.get(arr.size()-1));
			}
		}
		
		start = Paths.get(MainForm.user.getHomePath());
		files = null;
		try (Stream<Path> stream = Files.walk(start, 1)){
			files = stream.map(String::valueOf).sorted().collect(Collectors.toList());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i=0;i<files.size();i++) {
			ArrayList <String> arr = TreeFileSystemController.splitPath(files.get(i));
			
			String inp = arr.get(arr.size()-1);
			if ( !inp.equals(MainForm.user.getUserName()) && !inp.equals("hash") && !inp.equals("temporary") ) {
				cb2.addItem(arr.get(arr.size()-1));
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 439, 208);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		cb1 = new JComboBox();
		cb1.setBounds(69, 22, 111, 22);
		frame.getContentPane().add(cb1);
		
		JLabel l1 = new JLabel("User:");
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setFont(new Font("Tahoma", Font.BOLD, 12));
		l1.setBounds(10, 21, 55, 22);
		frame.getContentPane().add(l1);
		
		JButton button = new JButton("Share");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean flag = MainController.shareFile((String)cb1.getItemAt(cb1.getSelectedIndex()), (String)cb2.getItemAt(cb2.getSelectedIndex()));
				
				if ( flag ) {
					MainForm.printMess(frame, "Operacija je uspjesna.");
				}else {
					MainForm.printMess(frame, "Operacija nije uspjesna.");
				}
			}
		});
		button.setBackground(Color.WHITE);
		button.setFont(new Font("Tahoma", Font.BOLD, 18));
		button.setBounds(280, 91, 111, 40);
		frame.getContentPane().add(button);
		
		JLabel l2 = new JLabel("Document:");
		l2.setHorizontalAlignment(SwingConstants.CENTER);
		l2.setFont(new Font("Tahoma", Font.BOLD, 12));
		l2.setBounds(190, 21, 80, 22);
		frame.getContentPane().add(l2);
		
		cb2 = new JComboBox();
		cb2.setBounds(280, 22, 111, 22);
		frame.getContentPane().add(cb2);
		frame.setVisible(true);
	}
}
