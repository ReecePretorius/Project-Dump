/*
* Filename: ShortestPaths.java
* Details: CSC226 Assignment #3
* */ 

/* ShortestPaths.java
   CSC 226 - Fall 2018
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
   java ShortestPaths
   
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
   java ShortestPaths file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
   <number of vertices>
   <adjacency matrix row 1>
   ...
   <adjacency matrix row n>
   
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
   
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   NOTE: For the purpose of marking, we consider the runtime (time complexity)
         of your implementation to be based only on the work done starting from
	 the ShortestPaths() method. That is, do not not be concerned with the fact that
	 the current main method reads in a file that encodes graphs via an
	 adjacency matrix (which takes time O(n^2) for a graph of n vertices).
   
   
   (originally from B. Bird - 08/02/2014)
   (revised by N. Mehta - 10/24/2018)
*/

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;

/*Node Class
 * The node class is used to store the vertices and their weights associated with their connected edges
 * to be added to a PriorityQueue using the weights to determine that node's priority.
 * The priorityQueue is a min PriorityQueue.
 * */
class Node implements Comparator<Node>
{
    public int vertex;
    public int weight;
 
    public Node()
    {
    }
 
    public Node(int vertex, int weight)
    {
        this.vertex = vertex;
        this.weight = weight;
    }
 
    @Override
    public int compare(Node vertex1, Node vertex2)
    {
        if (vertex1.weight < vertex2.weight) {
        	return -1;
        } 
        if (vertex1.weight > vertex2.weight) {
        	return 1;
        } 
        return 0;
    }
}

//Do not change the name of the ShortestPaths class
public class ShortestPaths{

	public static int n; //number of vertices.
	public static int dist[];
	public static int prev[];//holds the previous vertices.
	
	
	/*createPath(source, index)
	 * using the prev[] array, this method reads the path from the array and then formats it into
	 * a string with the following format:
	 * 
	 * v1 --> v2 --> ... --> vn.
	 * 
	 * The method returns this string to be printed by the PrintPaths method.
	 */
	private static String createPath(int source, int index){
		String path;
		String oldPath = "";
		String newPath;
		
		if(prev[index] == 0 && index == source) {
			path = "0";
		}
		else if(prev[index] == 0) {
			path = source + " --> " + index;
		}
		else {
			int j = index;
			for(;;) {
				if(j == 0) {
					path = source + oldPath;
					break;
				}
				else {
					path = " --> " + j;
				}
				j = prev[j];
				newPath = path + oldPath;
				oldPath = newPath;
			}
		}
		return path;
	} 
   
	/* ShortestPaths(adj) 
       Given an adjacency list for an undirected, weighted graph, calculates and stores the
       shortest paths to all the vertices from the source vertex.
       
       The number of vertices is adj.length
       For vertex i:
         adj[i].length is the number of edges
         adj[i][j] is an int[2] that stores the j'th edge for vertex i, where:
           the edge has endpoints i and adj[i][j][0]
           the edge weight is adj[i][j][1] and assumed to be a positive integer
       
       All weights will be positive.
    */
    static void ShortestPaths(int[][][] adj, int source){
    	n = adj.length;//number of vertices.
    	dist = new int[n];//array to contain total weights for each path.
    	prev = new int[n];//array to hold indices to previous vertices on the paths.
    	int currVertex;
    	
    	Set<Integer> visited = new HashSet<Integer>();
	
    	for(int v = 0; v < n; v++){//each vertex v in adj.
    		if(v != source){//set distance to all vertices to infinity, except the source vertex.
    			dist[v] = Integer.MAX_VALUE;	
    		}
    	}
    	
    	PriorityQueue<Node> Q = new PriorityQueue<Node>(n, new Node());
		Q.add(new Node(source, 0));
		dist[source] = 0;
		
    	while(!Q.isEmpty()) {
    		currVertex = Q.poll().vertex;
    		visited.add(currVertex);
    		int edgeWeight = -1;
            int updateWeight = -1;
     
            int vIndexLength = adj[currVertex].length;
            for (int i = 0; i < vIndexLength; i++){
            	int destVertex = adj[currVertex][i][0];
                
            	if (!visited.contains(destVertex)){
                    edgeWeight = adj[currVertex][i][1];
                    updateWeight = dist[currVertex] + edgeWeight;
                        
                    if (updateWeight < dist[destVertex]){
                        dist[destVertex] = updateWeight;
                        prev[destVertex] = currVertex;
                        Q.add(new Node(destVertex, dist[destVertex]));
                    }   
                }
            }
    	}
    }
    
    /*PrintPaths(source)
     * This method takes the source vertex as an argument and prints out the paths
     * in a specific format.
     */
    static void PrintPaths(int source){
    	String path;
    	
    	for(int i = 0; i < n; i++) {
    		int totalDist = dist[i];
    		path = createPath(source, i);
    		
			System.out.printf("The path from %d to %d is: %s and the total distance is: %d\n", 
					source, i, path, totalDist);
		}
    }
    
    /* main()
       Contains code to test the ShortestPaths function. You may modify the
       testing code if needed, but nothing in this function will be considered
       during marking, and the testing process used for marking will not
       execute any of the code below.
    */
    public static void main(String[] args) throws FileNotFoundException{
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
	}
	else{
	    //Otherwise, read from standard input
	    s = new Scanner(System.in);
	    System.out.printf("Reading input values from stdin.\n");
	}
	
	int graphNum = 0;
	double totalTimeSeconds = 0;
	
	//Read graphs until EOF is encountered (or an error occurs)
	while(true){
	    graphNum++;
	    if(graphNum != 1 && !s.hasNextInt())
		break;
	    System.out.printf("Reading graph %d\n",graphNum);
	    int n = s.nextInt();
	    int[][][] adj = new int[n][][];
	    
	    int valuesRead = 0;
	    for (int i = 0; i < n && s.hasNextInt(); i++){
		LinkedList<int[]> edgeList = new LinkedList<int[]>(); 
		for (int j = 0; j < n && s.hasNextInt(); j++){
		    int weight = s.nextInt();
		    if(weight > 0) {
			edgeList.add(new int[]{j, weight});
		    }
		    valuesRead++;
		}
		adj[i] = new int[edgeList.size()][2];
		Iterator it = edgeList.iterator();
		for(int k = 0; k < edgeList.size(); k++) {
		    adj[i][k] = (int[]) it.next();
		}
	    }
	    if (valuesRead < n * n){
		System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
		break;
	    }
	    
	    // output the adjacency list representation of the graph
	    for(int i = 0; i < n; i++) {
	    	System.out.print(i + ": ");
	    	for(int j = 0; j < adj[i].length; j++) {
	    	    System.out.print("(" + adj[i][j][0] + ", " + adj[i][j][1] + ") ");
	    	}
	    	System.out.print("\n");
	    }
	    
	    long startTime = System.currentTimeMillis();
	    
	    ShortestPaths(adj, 0);
	    PrintPaths(0);
	    long endTime = System.currentTimeMillis();
	    totalTimeSeconds += (endTime-startTime)/1000.0;
	    
	    //System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
	}
	graphNum--;
	System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
    }
}
