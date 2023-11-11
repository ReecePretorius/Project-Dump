/*
* Filename: AVL_BST.java
* Details: CSC225 Assignment #3
* */

public class AVL_BST{
    /*
    * checkAVL is a method that takes in a tree that has been constructed using the createBST() method
    * it then checks to see if the given BST is an tree that meets the balance requirements of an
    * AVL tree.
    */
    public static boolean checkAVL(BST b){ //b in this case is myTree
				boolean checkAVLTree = b.checkSubtrees(b.root);//uses the checkSubTrees method as a helper to determine if the tree b is AVL or not.
				if(checkAVLTree == true){ //The tree b is AVL
            return true;
				}
				else{ //The tree b is not AVL
	    			return false;
				}
    }

    /*
    * createBST() creates a BST called myTree and uses a for loop to iterate through the given array
    * and inserts the values one at a time using the insert method in the BST class.
    * returns myTree when it is complete.
    */
    public static BST createBST(int[] a){
        BST myTree = new BST();
        for(int i = 0; i < a.length; i++){
            myTree.insert(a[i]);
        }
        //myTree.inorder(); //prints an in order traversal of myTree
        return myTree;
    }

    public static void main(String[] args){
        //test cases from assignment pdf as well as a couple extra tests to check specific cases.
        
        int[] A = {5,2,8,6,1,9,52,3};//true
        int[] B = {82,85,153,195,124,66,200,193,185,243,73,153,76};//false
        int[] C = {5,3,7,1};//true
        int[] D = {5,1,98,100,-3,-5,55,3,56,50};//true
        int[] E = {297,619,279,458,324,122,505,549,83,186,131,71};//false
        int[] F = {78};//true
		int[] G = {1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0,839,57,16,524,160,168,760,153,32,692,861,226,306,114,151,379,520,4,438,709,391,495,938,860,984,690,204,218,426,55,12,34,45,3,22,8,65,67,89,88,766,54,53,0};
        int[] H = {80,90,84,96,120,40,14,70,60,50};//false
        
        BST b = createBST(A);
        System.out.println(checkAVL(b));
        BST c = createBST(B);
        System.out.println(checkAVL(c));
        BST d = createBST(C);
        System.out.println(checkAVL(d));
        BST e = createBST(D);
        System.out.println(checkAVL(e));
        BST f = createBST(E);
        System.out.println(checkAVL(f));
        BST g = createBST(F);
        System.out.println(checkAVL(g));
		BST h = createBST(G);
		System.out.println(checkAVL(h));
        BST i = createBST(H);
        System.out.println(checkAVL(i));
        
    }
}

/*
* A Node class for the BST implementation
*/
class bstNode{
    bstNode left;
    bstNode right;
    int data;

    //constructor for the bstNode class
    public bstNode(int data){
        this.left = null;
        this.right = null;
        this.data = data;
    }
}

class BST{
    public bstNode root;

    public BST(){
        root = null;
    }

    /*
    * a public method that invokes a private method checkSubTrees(), with the current root
    * and an initial height of 0.
    */
    public boolean checkSubtrees(bstNode root){
        int height = 0;//initializes a new height instance every time it checks the subtree balance
        boolean isTreeAVL = checkSubtrees(root, height);
        return isTreeAVL;//returns true or false depending on the results of recursive checkSubtrees();
    }

    /*
    * A private recursive method that checks the left and right subtrees to see if they
    * meet the balance requirements of an AVL Tree. It is passed the root and a temporary height variable
    * It calls a getHeight() method that computes the heights of the left and right trees of
    * the current root/Node and then recursively calls itself (checkSubtrees()) with the new root and heights
    * that it calculated. Finally it checks the height of the ROOT Node of the tree to see if the overall tree is balanced
    * if all three of these calls return True then the Tree and its subtrees meet the requirements for an AVL Tree.
    */
    private static boolean checkSubtrees(bstNode newRoot, int height){
        if(newRoot == null){
						height = 0;
            return true;
        }
        int newLeftHeight = getHeight(newRoot.left);
        int newRightHeight = getHeight(newRoot.right);

        boolean leftSubtree = checkSubtrees(newRoot.left, newLeftHeight);
        boolean rightSubtree = checkSubtrees(newRoot.right, newRightHeight);
        boolean isRootBalanced = compareHeights(newLeftHeight, newRightHeight);
        if((isRootBalanced && leftSubtree && rightSubtree) == true){ //balance properties for AVL tree
            return true;
        }
        else{
            return false;
        }
    }

    /*
    * A private method compareHeights() that takes the left height and right height and finds its absolute value
    * if the result is less than or equal to one then the hegihts dont differ by more than one in height
    * and therefore is balanced. (this method is only used to check if the root node is balanced).
    */
    private static boolean compareHeights(int leftHeight, int rightHeight){
        int leftMinusRight = leftHeight - rightHeight;

        if(leftMinusRight < 0){ //absolute value of the negative result
                leftMinusRight = leftMinusRight * -1;
        }
        if(leftMinusRight <= 1){ //node balance property for AVL trees (no more than one height apart)
                return true;
        }
        else{
                return false;
        }
    }

    /*
    * A private method getHeight(), it recursively gets the heights of the left and right tree
    * of the current root/Node that it is looking at, it returns the greater of the two heights plus one for the root node.
    */
    private static int getHeight(bstNode root){
        if(root == null){ //base case
                return 0;
            }

        int leftHeight = getHeight(root.left); //recursively gets left height
        int rightHeight = getHeight(root.right); //recursively gets right height
        if(leftHeight > rightHeight){ //left is max
                return(leftHeight + 1);
        }
        else{//right is max
                return(rightHeight + 1);
        }
    }

    /*
    * A public method insert() that invokes the private recursive insert method.
    */
    public void insert(int value){
        root = insert(root, value);
    }

    /*
    * A private recursive method insert(), it takes in the root of the tree and the value to be inserted
    * if the value is larger than the value in the root then it is inserted recursively to the right of that node
    * if the value is less then it is recursively inserted to the left of that node.
    */
    private bstNode insert(bstNode root, int value){
        if(root == null){ //case if the tree is empty ::first insert::
            root = new bstNode(value);
        }
				else{
            if(value >= root.data){ //recursively insert as the right child.
                root.right = insert(root.right, value);
            }
            else{ //recursively insert as the left child.
                root.left = insert(root.left, value);
            }
        }
        return root;
    }

    /*//method to print the trees in sorted order to check if insertion executed correctly
    public void inorder(){
        printInOrder(root);
    }
    
    private void printInOrder(bstNode root){
        if (root != null){
            printInOrder(root.left);
            System.out.print(root.data + " ");
            printInOrder(root.right);
        }
    }
    */
}
