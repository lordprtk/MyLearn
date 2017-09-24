package com.datastructure.tree;

public class BinaryTree {
	public static void main(String[] args){

		BinaryTree bt = new BinaryTree();
		bt.runTree();
	}

	public void runTree(){
		Tree root = new Tree(10);
		root.insert(5);
		root.insert(15);
	}
	
	class Tree{
		int val;
		Tree left;
		Tree right;

		public Tree(int val){
			this.val=val;
		}

		public void insert(int val){
			//if(this.val)
		}
	}
}
