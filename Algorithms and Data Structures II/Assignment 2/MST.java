/* MST.java
   CSC 226 - Fall 2018
   Problem Set 2 - Template for Minimum Spanning Tree algorithm
   
   The assignment is to implement the mst() method below, using Kruskal's algorithm
   equipped with the Weighted Quick-Union version of Union-Find. The mst() method computes
   a minimum spanning tree of the provided graph and returns the total weight
   of the tree. To receive full marks, the implementation must run in time O(m log m)
   on a graph with n vertices and m edges.
   
   This template includes some testing code to help verify the implementation.
   Input graphs can be provided with standard input or read from a file.
   
   To provide test inputs with standard input, run the program with
       java MST
   To terminate the input, use Ctrl-D (which signals EOF).
   
   To read test inputs from a file (e.g. graphs.txt), run the program with
       java MST graphs.txt
   
   The input format for both methods is the same. Input consists
   of a series of graphs in the following format:
   
       <number of vertices>
       <adjacency matrix row 1>
       ...
       <adjacency matrix row n>
   	
   For example, a path on 3 vertices where one edge has weight 1 and the other
   edge has weight 2 would be represented by the following
   
   3
   0 1 0
   1 0 2
   0 2 0
   	
   An input file can contain an unlimited number of graphs; each will be processed separately.
   
   NOTE: For the purpose of marking, we consider the runtime (time complexity)
         of your implementation to be based only on the work done starting from
	 the mst() method. That is, do not not be concerned with the fact that
	 the current main method reads in a file that encodes graphs via an
	 adjacency matrix (which takes time O(n^2) for a graph of n vertices).
   
   (originally from B. Bird - 03/11/2012)
   (revised by N. Mehta - 10/9/2018)
*/

import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class Edge implements Comparable<Edge> {
	public Node source;
	public Node destination;
	public int weight;

	public Edge(Node src, Node dest, int x) {
		weight = x;
		source = src;
		destination = dest;
	}

	public boolean has(Node x) {
		if (this.source == x || this.destination == x) {
			return true;
		} else {
	  		return false;
		}
	}

	public int compareTo(Edge e) {
		return Integer.compare(this.weight, e.weight);
	}
}

class Node {
	public ArrayList<Edge> edges;
	public int value;
	public UnionFind uf;

	public Node(int x) {
		value = x;
		edges = new ArrayList<Edge>();
	}
}

class UnionFind{
	public UnionFind parent;
	public Node key;
	public int rank;

	public UnionFind(Node x) {
		parent = this;
		key = x;
		rank = 0;
	}


	public static UnionFind makeSet(Node x) {
		return new UnionFind(x);
	}

	public UnionFind find() {
		UnionFind current = this;

		if (current.parent == current) {
			return current;
		} else {
			return current.parent.find(current); 
		}
	}
  
	public UnionFind find(UnionFind last) {
		UnionFind current = this;
		last.parent = current.parent;

		if (current.parent == current) {
			return current;
		} else {
			return current.parent.find(current);
		}
	}

	public UnionFind union(UnionFind other) {
		UnionFind larger;
		UnionFind smaller;
		UnionFind x;
		UnionFind y;

		// Union top nodes
		x = this.find(); 
		y = other.find(); 

		if (x.rank > y.rank) {
			larger = x;
	    	smaller = y;
		} else {
			smaller = y;
			larger = x;
		}

		// Link the smaller node to the larger node
		smaller.parent = larger;

		if (smaller.rank > larger.rank) {
			larger.rank = smaller.rank + 1;
		} else if (smaller.rank == larger.rank) {
			larger.rank += 1;
		} else {
			// Do nothing
		}
		return larger;
	}
}

public class MST {


    /* mst(adj)
       Given an adjacency matrix adj for an undirected, weighted graph, return the total weight
       of all edges in a minimum spanning tree.

       The number of vertices is adj.length
       For vertex i:
         adj[i].length is the number of edges
         adj[i][j] is an int[2] that stores the j'th edge for vertex i, where:
           the edge has endpoints i and adj[i][j][0]
           the edge weight is adj[i][j][1] and assumed to be a positive integer
    */
    static int mst(int[][][] adj) {
	int numVerts = adj.length;

	/* Find a minimum spanning tree using Kruskal's algorithm */
	/* (You may add extra functions if necessary) */
		
	Node[] node = new Node[numVerts];
	UnionFind[] unionFind = new UnionFind[numVerts];
	ArrayList<Edge> edges = new ArrayList<Edge>();

	// Make up a list of nodes
	for (int i = 0; i < numVerts; i++) {
		node[i] = new Node(i);
		unionFind[i] = UnionFind.makeSet(node[i]);
		node[i].uf = unionFind[i];
	}

	// Make up list of edges
	for (int i = 0; i < numVerts - 1; i++) {
		for (int j = i + 1; j < numVerts; j++) {
			System.out.println(adj[i][j]);
			if (adj[i][j][0] > 0) {
				// Add the new edge to edges
				Edge edge = new Edge(node[i], node[j], adj[i][j][1]);
				edges.add(edge);
			}
		} 
	}

	ArrayList<Edge> mst = new ArrayList<Edge>();
	Collections.sort(edges);
	
	for (Edge edge: edges) {
		if (!edge.source.uf.find().equals(edge.destination.uf.find())) {
			mst.add(edge);
			edge.source.uf.union(edge.destination.uf);
			edge.source.uf.union(edge.source.uf);
		} else {
			// Do nothing
		}
	}
		
		
		
	/* Add the weight of each edge in the minimum spanning tree
	   to totalWeight, which will store the total weight of the tree.
	*/
	int totalWeight = 0;
	for (Edge edge : mst) {
    	totalWeight += edge.weight;
    }
		
	return totalWeight;
		
    }


    public static void main(String[] args) {
	/* Code to test your implementation */
	/* You may modify this, but nothing in this function will be marked */

	int graphNum = 0;
	Scanner s;

	if (args.length > 0) {
	    //If a file argument was provided on the command line, read from the file
	    try {
		s = new Scanner(new File(args[0]));
	    }
	    catch(java.io.FileNotFoundException e) {
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    System.out.printf("Reading input values from %s.\n",args[0]);
	}
	else {
	    //Otherwise, read from standard input
	    s = new Scanner(System.in);
	    System.out.printf("Reading input values from stdin.\n");
	}
		
	//Read graphs until EOF is encountered (or an error occurs)
	while(true) {
	    graphNum++;
	    if(!s.hasNextInt()) {
		break;
	    }
	    System.out.printf("Reading graph %d\n",graphNum);
	    int n = s.nextInt();

	    int[][][] adj = new int[n][][];
	    
	    
	    
	    
	    int valuesRead = 0;
	    for (int i = 0; i < n && s.hasNextInt(); i++) {
		LinkedList<int[]> edgeList = new LinkedList<int[]>(); 
		for (int j = 0; j < n && s.hasNextInt(); j++) {
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
	    if (valuesRead < n * n) {
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

	    int totalWeight = mst(adj);
	    System.out.printf("Graph %d: Total weight of MST is %d\n",graphNum,totalWeight);

				
	}
    }

    
}
