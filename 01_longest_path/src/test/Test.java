package test;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Test {
	public static void main(String[] args) throws Exception {
		String dir = "root\n\t12\n\t\t2\n\t\t\t3\n\t4\n\t\t5\n\t\t\t6\n\t\t\t\t\t7\n\t1234567";
		
		System.out.println(new Test().getLongestPath(dir));
	}

	public String getLongestPath(String dir)
		throws Exception
	{
		DirTree tree = new DirTree();

		for (String path : dir.split("\n")) {
			tree.processPathString(path);
		}
		
		return tree.getLongestPath();
	}

	// ================================================================
	/* "root\n\tdir1\n\t\tdir2\n\t\t\tdir3\n\tdir4"
	 *  =>
	 *  root
	 *    dir1
	 *      dir2
	 *      dir3
	 *    dir4
	 *      dir__________5
	 *
	 * given above string, find out the path that consists the longest characters.
	 * That is, root/dir4/dir__________5, in this example
	 *
	 *
	 * Solution:
	 *   1) split string by '\n'
	 *   2) parse the array into tree structure, each node keeps tracking of the level,
	 *      root will be level 0, dir1, dir4 will be level 1, and dir2, dir3, and dir__________5 will be level 3
	 *
	 *      parsing algorithm:
	 *        a) split by '\t'. "\t\t\t\tDir" ==> [][][][][Dir]
	 *        b) based on the size of the array, find out the expecting level: new node level = (size - 1)
	 *        c) assuming the string follow strictly the structure rule, then we can find out the corresponding parent node:
	 *           if the new node level > current node's level ---> current node is the parent
	 *           if the new node level <= current node's level ---> parent is the current node crawling up N times.
	 *           (N = current node's level - new node level + 1)
	 *        d) add new node in, and set current node to the new node, and update level and max path/length
	 *   3) the tree keeps tracking of the max length and the longest path
	 */
	// ================================================================

	public static class DirTree {
		private TreeNode root = null;
		private TreeNode currentNode;
		private String longestPath;
		private Integer maxLen = 0;

		public void processPathString(String pathStr)
			throws Exception
		{
			if (null == root) {
				root = new TreeNode(pathStr);
				//maxLen = pathStr.length();
				currentNode = root;

			} else {
				String[] parts = pathStr.split("\t");
				int level = parts.length - 1;
				String name = parts[level];
				TreeNode tmp = new TreeNode(name);

				while (level <= currentNode.getLevel()) {
					currentNode = currentNode.getParent();
				}

				currentNode.addChild(tmp);
				currentNode = tmp;
			}

			int tmpLen = Math.max(maxLen, currentNode.getAbsolutePath().length());
			if (maxLen < tmpLen) {
				maxLen = tmpLen;
				longestPath = currentNode.getAbsolutePath();
			}
		}

		public String getLongestPath() { return longestPath; }
	}

	// ================================================================
	
	public static class TreeNode {
		private String name;
		private TreeNode parent;
		private ArrayList<TreeNode> children = null;
		private int level = 0;
		
		public TreeNode (String name) {
			this.name = name;
		}

		public TreeNode setParent(TreeNode node)
			throws Exception
		{
			this.parent = node;
			this.level = node.getLevel() + 1;
			
			return this;
		}

		public void addChild(TreeNode n)
			throws Exception
		{
			if (null == children) {
				children = new ArrayList<>();
			}
			
			children.add(n.setParent(this));
		}

		public String getName() { return name; }
		public TreeNode getParent() { return parent; }
		public ArrayList<TreeNode> getChildren() { return children; }
		public int getLevel() { return level; }

		public String getAbsolutePath()
			throws Exception
		{
			return ((null == getParent()
							 ? ""
							 : getParent().getAbsolutePath())
							+ getName()
							+ (null == getChildren()
								 ? ""
								 : "/"));
		}
	}
}
