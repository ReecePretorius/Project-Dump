/*
* Filename: NinePuzzle.java
* Details: CSC225 Assignment #4
* */

/* NinePuzzle.java
   CSC 225 - Spring 2017
   Assignment 4 - Template for the 9-puzzle

   This template includes some testing code to help verify the implementation.
   Input boards can be provided with standard input or read from a file.

   To provide test inputs with standard input, run the program with
	java NinePuzzle
   To terminate the input, use Ctrl-D (which signals EOF).

   To read test inputs from a file (e.g. boards.txt), run the program with
    java NinePuzzle boards.txt

   The input format for both input methods is the same. Input consists
   of a series of 9-puzzle boards, with the '0' character representing the
   empty square. For example, a sample board with the middle square empty is

    1 2 3
    4 0 5
    6 7 8

   And a solved board is

    1 2 3
    4 5 6
    7 8 0

   An input file can contain an unlimited number of boards; each will be
   processed separately.

   B. Bird    - 07/11/2014
   M. Simpson - 11/07/2015
*/

import java.util.Scanner;
import java.io.File;
import java.util.*;

public class NinePuzzle{

	//The total number of possible boards is 9! = 1*2*3*4*5*6*7*8*9 = 362880
	public static final int NUM_BOARDS = 362880;
	public static int currRow;
	public static int currCol;

	/*  SolveNinePuzzle(B)
		Given a valid 9-puzzle board (with the empty space represented by the
		value 0),return true if the board is solvable and false otherwise.
		If the board is solvable, a sequence of moves which solves the board
		will be printed, using the printBoard function below.
	*/
	public static boolean SolveNinePuzzle(int[][] B){
		int[][] G = new int[NUM_BOARDS][4];//adjacency list for B.
		int[][] Goal = {//the final state of the puzzle when it is solved.
			{1,2,3},
			{4,5,6},
			{7,8,0}
		};
		
		for (int[] row: G)//fill all indices of the 2d array G with -1's 
		    Arrays.fill(row, -1);
		
		for (int i = 0; i < NUM_BOARDS; i++) {
			int[][] currBoard = getBoardFromIndex(i);
			findZeroPos(currBoard);//finds the position of the Zero in the current board.
			DoMove(currBoard, G, currRow, currCol, i);//does the legal moves and constructs the graph G.
		}	
		
		int[] path = RunBFS(G, Goal, getIndexFromBoard(B));//runs a BFS to find the path from the initial Board B to the Goal Board.
		if (path == null) {//no path found.
			return false;//Board B is not solvable.
		}
		else {
			Stack<Integer> stack = new Stack<Integer>();
			int indexOfB = getIndexFromBoard(B);//
			path[indexOfB] = -1;//mark the index position of the initial board b by setting it to -1.
			int indexOfGoal = getIndexFromBoard(Goal);
			
			while (path[indexOfGoal] != -1) {//add boards to the stack until we find the position marked -1(initial Board B).
				stack.push(path[indexOfGoal]);
				indexOfGoal = path[indexOfGoal];
			}
			while (!stack.isEmpty()) {
				printBoard(getBoardFromIndex(stack.pop()));//prints the boards of all of the needed steps to solve the puzzle B.
			}printBoard(Goal);//prints the solution after all of the steps.
			return true;//returns true i.e. puzzle is solvable.
		}
	}
	
	//A helper method used to create a copy of a board that has been passed in. It returns the copy of the board.
	public static int[][] copyBoard(int[][] B){
		int[][] copy = new int[3][3];
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				copy[i][j] = B[i][j];
			}
		}
		return copy;
	}
	
	//finds the position (i,j) in the 2d array of the zero(0) in currBoard. 
	public static void findZeroPos(int[][] currBoard){
		for(int i = 0;i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(currBoard[i][j] == 0){
					currRow = i;
					currCol = j;
				}
			}
		}
	}
	
	//checks if a legal move is possible, if it is then it creates a tempBoard which is a copy of board B and then performs the swap on the tempBoard
	//therefore not altering the original board B. gets the index from the tempBoard using the given method getIndexFromBoard() and uses that index to add it to the graph G.
	public static void DoMove(int[][] B, int[][] G, int i, int j, int v){
		int[][] tempBoard = new int[3][3];
		int temp, u;
		//up
		if(i > 0){
			tempBoard = copyBoard(B);
			temp = tempBoard[i - 1][j];
			tempBoard[i - 1][j] = tempBoard[i][j];
			tempBoard[i][j] = temp;
			u = getIndexFromBoard(tempBoard);
			G[v][1] = u;
		}
		//left
		if(j > 0){
			tempBoard = copyBoard(B);
			temp = tempBoard[i][j - 1];
			tempBoard[i][j - 1] = tempBoard[i][j];
			tempBoard[i][j] = temp;
			u = getIndexFromBoard(tempBoard);
			G[v][2] = u;
		}
		//down
		if(i < 2){
			tempBoard = copyBoard(B);
			temp = tempBoard[i + 1][j];
			tempBoard[i + 1][j] = tempBoard[i][j];
			tempBoard[i][j] = temp;
			u = getIndexFromBoard(tempBoard);
			G[v][0] = u;
		}
		//right
		if(j < 2){
			tempBoard = copyBoard(B);
			temp = tempBoard[i][j + 1];
			tempBoard[i][j + 1] = tempBoard[i][j];
			tempBoard[i][j] = temp;
			u = getIndexFromBoard(tempBoard);
			G[v][3] = u;
		}		
	}
	
	//BFS code adapted from https://www.codeproject.com/Articles/32212/Introduction-to-Graph-with-Breadth-First-Search-BF
	public static int[] RunBFS(int[][] G, int[][] Goal, int index) {
		int[] edgeTo = new int[NUM_BOARDS];
		boolean[] marked = new boolean[NUM_BOARDS];//a boolean array that marks an index as visited(true) or not_visited(false)
		LinkedList<Integer> list = new LinkedList<Integer>();
		marked[index] = true;
		list.add(index);
		
		while (!list.isEmpty()) {
			int i = list.remove();
			if (getIndexFromBoard(Goal) == i) {
				return edgeTo;
			}else{
				for (int j = 0; j < 4; j++) {
					if (G[i][j] != -1) {
						int v = G[i][j];
						if(marked[v] == false){
							marked[v] = true;
							edgeTo[v] = i;
							list.add(v);
						}
					}
				}
			}
		}
		return edgeTo;
	}
	
	/*  printBoard(B)
		Print the given 9-puzzle board.
	*/
	public static void printBoard(int[][] B){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				System.out.printf("%d ",B[i][j]);
			System.out.println();
		}
		System.out.println();
	}

	public static int getIndexFromBoard(int[][] B){
		int i,j,tmp,s,n;
		int[] P = new int[9];
		int[] PI = new int[9];
		for (i = 0; i < 9; i++){
			P[i] = B[i/3][i%3];
			PI[P[i]] = i;
		}
		int id = 0;
		int multiplier = 1;
		for(n = 9; n > 1; n--){
			s = P[n-1];
			P[n-1] = P[PI[n-1]];
			P[PI[n-1]] = s;

			tmp = PI[s];
			PI[s] = PI[n-1];
			PI[n-1] = tmp;
			id += multiplier*s;
			multiplier *= n;
		}
		return id;
	}

	public static int[][] getBoardFromIndex(int id){
		int[] P = new int[9];
		int i,n,tmp;
		for (i = 0; i < 9; i++)
			P[i] = i;
		for (n = 9; n > 0; n--){
			tmp = P[n-1];
			P[n-1] = P[id%n];
			P[id%n] = tmp;
			id /= n;
		}
		int[][] B = new int[3][3];
		for(i = 0; i < 9; i++)
			B[i/3][i%3] = P[i];
		return B;
	}

	public static void main(String[] args){

		Scanner s;

		if (args.length > 0){
			//If a file argument was provided on the command line, read from the file
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			//Otherwise, read from standard input
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}

		int graphNum = 0;
		double totalTimeSeconds = 0;

		//Read boards until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading board %d\n",graphNum);
			int[][] B = new int[3][3];
			int valuesRead = 0;
			for (int i = 0; i < 3 && s.hasNextInt(); i++){
				for (int j = 0; j < 3 && s.hasNextInt(); j++){
					B[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < 9){
				System.out.printf("Board %d contains too few values.\n",graphNum);
				break;
			}
			System.out.printf("Attempting to solve board %d...\n",graphNum);
			long startTime = System.currentTimeMillis();
			boolean isSolvable = SolveNinePuzzle(B);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;

			if (isSolvable)
				System.out.printf("Board %d: Solvable.\n",graphNum);
			else
				System.out.printf("Board %d: Not solvable.\n",graphNum);
		}
		graphNum--;
		System.out.printf("Processed %d board%s.\n Average Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>1)?totalTimeSeconds/graphNum:0);

	}

}