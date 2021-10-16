package helpFunctions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import forms.MainForm;

public class TreeFileSystemController {
	public void buildTree(DefaultTreeModel dtm, int type) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) dtm.getRoot();
		
		Path start = Paths.get("home" + File.separator + MainForm.user.getUserName());
		List<String> files = null;
		try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)){
			files = stream.map(String::valueOf).sorted().collect(Collectors.toList());
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		List <String> fi = new ArrayList<String>();
		for (int i=0;i<files.size();i++) {
			String a = files.get(i).replace("home" + File.separator + MainForm.user.getUserName() + File.separator, "");

			if ( !a.equals("home" + File.separator + MainForm.user.getUserName()) )
				fi.add(a);
		}
		
		for (int i=0;i<fi.size();i++) {
			ArrayList<String> elements = splitPath(fi.get(i));
			
			DefaultMutableTreeNode node = root;
			
			for (String j : elements) {
				int index = childIndex(node, j);

				if (index < 0) {
					if ( type == 1 ) {
						DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(j);
	                	node.insert(newChild, node.getChildCount());
	                	node = newChild;
					}else {
						dtm.insertNodeInto(new DefaultMutableTreeNode(j), node, node.getChildCount());
					}
	            }
	            else {
	                node = (DefaultMutableTreeNode) node.getChildAt(index);
	            }
			}
		}
	}
	
	private int childIndex(DefaultMutableTreeNode node, String childValue) {
        Enumeration<TreeNode> children = node.children();
        DefaultMutableTreeNode child = null;
        int index = -1;

        while (children.hasMoreElements() && index < 0) {
            child = (DefaultMutableTreeNode) children.nextElement();

            if (child.getUserObject() != null && childValue.equals(child.getUserObject())) {
                index = node.getIndex(child);
            }
        }

        return index;
    }
	
	public static ArrayList<String> splitPath(String a){
		String aa = "";
		ArrayList<String> val = new ArrayList<String>();
		for (int i=0;i<a.length();i++) {
			if ( a.charAt(i) == File.separatorChar ) {
				val.add(aa);
				aa = "";
			}else {
				aa+=a.charAt(i);
			}
		}
		
		if ( !aa.equals("") ) {
			val.add(aa);
		}
		
		return val;
	}
}
