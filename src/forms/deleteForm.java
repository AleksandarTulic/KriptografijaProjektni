package forms;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import controller.MainController;
import helpFunctions.TreeFileSystemController;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class deleteForm {

	private JFrame frame;
	@SuppressWarnings("rawtypes")
	private JComboBox cb;

	@SuppressWarnings("unchecked")
	public deleteForm() {
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
			
			for (int j=0;j<arr.size();j++) {
				if ( !arr.get(j).equals(MainForm.user.getUserName()) && !arr.get(j).equals("hash") 
						&& !arr.get(j).equals("temporary") && !arr.get(j).equals("home") ) {
					cb.addItem(arr.get(j));
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 263, 175);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel l1 = new JLabel("File:");
		l1.setFont(new Font("Tahoma", Font.BOLD, 12));
		l1.setHorizontalAlignment(SwingConstants.CENTER);
		l1.setBounds(10, 11, 60, 22);
		frame.getContentPane().add(l1);
		
		cb = new JComboBox();
		cb.setBounds(85, 12, 126, 22);
		frame.getContentPane().add(cb);
		
		JButton button = new JButton("Delete");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ( cb.getItemAt(cb.getSelectedIndex()) != null ) {
					boolean flag = MainController.deleteFile((String)cb.getItemAt(cb.getSelectedIndex()));
					flag = MainController.deleteFile("hash" + File.separator + cb.getItemAt(cb.getSelectedIndex()));
					
					if ( flag ) {
						MainForm.printMess(frame, "Operacija je uspjesna.");
						
						DefaultTreeModel model = (DefaultTreeModel) MainForm.tree.getModel();
					    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
					    
					    deleteNodes(root, model);
					}else {
						MainForm.printMess(frame, "Operacija nije uspjesna.");
					}
				}
			}
		});
		button.setBackground(Color.WHITE);
		button.setFont(new Font("Tahoma", Font.BOLD, 18));
		button.setBounds(22, 62, 189, 46);
		frame.getContentPane().add(button);
		
		frame.setVisible(true);
	}
	
	private void deleteNodes(DefaultMutableTreeNode node, DefaultTreeModel model) {
		int howMuch = node.getChildCount();
	    
	    for (int i=0;i<howMuch;i++) {
	    	DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
	    	if ( childNode.getChildCount() > 0 ) {
	    		deleteNodes(childNode, model);
	    	}else {
		    	if ( childNode.toString().equals((String)cb.getItemAt(cb.getSelectedIndex())) ) {
		    		model.removeNodeFromParent(childNode);
		    	}
	    	}
	    }
	}

}
