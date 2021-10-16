package forms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

import controller.MainController;
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

public class uploadSent {

	private JFrame frame;
	@SuppressWarnings("rawtypes")
	private JComboBox cb;

	@SuppressWarnings("unchecked")
	public uploadSent() {
		initialize();
		
		Path start = Paths.get("share");
		List<String> files = null;
		try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)){
			files = stream.map(String::valueOf).sorted().collect(Collectors.toList());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i=0;i<files.size();i++) {
			ArrayList<String> arr = TreeFileSystemController.splitPath(files.get(i));
			if ( arr.size() >= 3 ) {
				if ( arr.get(1).startsWith(MainForm.user.getUserName() + "_") && !arr.get(2).startsWith("res_") ) {
					cb.addItem(arr.get(1) + File.separator + arr.get(2));
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 297, 221);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel l1 = new JLabel("File:");
		l1.setFont(new Font("Tahoma", Font.BOLD, 12));
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setBounds(26, 11, 54, 29);
		frame.getContentPane().add(l1);
		
		cb = new JComboBox();
		cb.setBounds(112, 15, 142, 22);
		frame.getContentPane().add(cb);
		
		JButton button = new JButton("Upload");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( (String) cb.getItemAt(cb.getSelectedIndex()) != null ) {
					String path = (String)cb.getItemAt(cb.getSelectedIndex());
					ArrayList <String> arr = TreeFileSystemController.splitPath(path);
					
					if ( arr.size() == 2 ) {
						boolean flag = MainController.uploadSent(arr.get(1), "share" + File.separator + arr.get(0));
						
						if ( flag ) {
							MainForm.printMess(frame, "Operacija je uspjesna.");
						}else {
							MainForm.printMess(frame, "Operacija nije uspjesna.");
						}
					}
				}
			}
		});
		button.setFont(new Font("Tahoma", Font.BOLD, 18));
		button.setBackground(Color.WHITE);
		button.setBounds(26, 72, 228, 69);
		frame.getContentPane().add(button);
		frame.setVisible(true);
	}

}
