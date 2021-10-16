package forms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.SwingConstants;

import controller.MainController;
import cryptoAlgo.Crypt;
import helpFunctions.TreeFileSystemController;

import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class changeFile {

	private JFrame frame;
	private JTextArea t1;
	@SuppressWarnings("rawtypes")
	private JComboBox cb;
	private JScrollPane sc;

	@SuppressWarnings("unchecked")
	public changeFile() {
		initialize();
		
		Path start = Paths.get("home" + File.separator + MainForm.user.getUserName());
		List<String> files = null;
		try (Stream<Path> stream = Files.walk(start, 1)){
			files = stream.map(String::valueOf).sorted().collect(Collectors.toList());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i=0;i<files.size();i++) {
			ArrayList <String> arr = TreeFileSystemController.splitPath(files.get(i));
			
			if ( arr.size() == 3 ) {
				if ( !arr.get(2).equals("hash") && !arr.get(2).equals("temporary") ) {
					if ( arr.get(2).endsWith(".txt") ) {
						cb.addItem(arr.get(2));
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 557, 334);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel l1 = new JLabel("File:");
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setFont(new Font("Tahoma", Font.BOLD, 12));
		l1.setBounds(10, 21, 63, 14);
		frame.getContentPane().add(l1);
		
		JLabel l2 = new JLabel("Text:");
		l2.setFont(new Font("Tahoma", Font.BOLD, 12));
		l2.setHorizontalAlignment(SwingConstants.CENTER);
		l2.setBounds(10, 81, 63, 14);
		frame.getContentPane().add(l2);
		
		JButton btnChange = new JButton("Change");
		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String algo = Crypt.readAlgo((String)cb.getItemAt(cb.getSelectedIndex()));
					MainController.deleteFile("hash" + File.separator + cb.getItemAt(cb.getSelectedIndex()));
					MainController.deleteFile((String)cb.getItemAt(cb.getSelectedIndex()));
					
					String kre = (String)cb.getItemAt(cb.getSelectedIndex());
					kre = kre.replace(".txt", "");
					MainController.createFile(kre, t1.getText(), algo);
					
					MainForm.printMess(frame, "Operacija je uspjesna.");
					frame.dispose();
				}catch(Exception ee) {
					ee.printStackTrace();
				}
			}
		});
		btnChange.setBackground(Color.WHITE);
		btnChange.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnChange.setBounds(439, 222, 89, 63);
		frame.getContentPane().add(btnChange);
		
		cb = new JComboBox();
		cb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					MainController.downloadFile((String)cb.getItemAt(cb.getSelectedIndex()), MainForm.user.getHomePath() + File.separator + "temporary");
					
					File f = new File(MainForm.user.getHomePath() + File.separator + "temporary" + File.separator + cb.getItemAt(cb.getSelectedIndex()));
					Scanner scan = new Scanner(f);
					
					String text = "";
					while (scan.hasNextLine()) {
						text+=scan.nextLine();
						text+='\n';
					}
					
					scan.close();
					
					t1.setText(text);
				}catch (Exception ee) {
					ee.printStackTrace();
				}
			}
		});
		cb.setBounds(83, 18, 124, 23);
		frame.getContentPane().add(cb);
		
		t1 = new JTextArea();
		t1.setColumns(10);
		
		sc = new JScrollPane(t1);
		sc.setBounds(37, 106, 384, 179);
		frame.getContentPane().add(sc);
		
		frame.setVisible(true);
	}

}
