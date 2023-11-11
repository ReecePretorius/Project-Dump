/*
* Filename: Stock.java
* Details: CSC225 Assignment #1
* */ 
import java.util.Scanner;
import java.io.File;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;

public class Stock{

	/**
	 * CalculateSpan is a method used to calculate the span of the stock prices, it takes in an array p of stock numbers
	 * and uses a stack to calculate the spans. array indexes are pushed onto the stack and used to compare values at
	 * those indexes of the p Array. if the value is greater than the one at the previous index then the index is 
	 * popped from the stack and a counter incremented to keep track of the largest span up to that point.
	 * 
	 *  whenever the stack becomes empty the current index position i of Array S(Array to be returned) is set to be the count
	 *  this means that every value previous to the current i index in Array A is less than or equal to the current value
	 *  therefore has the greatest span.
	 *  
	 *   if the test for the while loop returns false then the index S[i] is equivalent to the current i value minus 
	 *   the top value on the stack.
	 *   
	 *   At the end the Array S is returned with all the span values for the initial stocks array p passed in.
	 * */
	public static int[] CalculateSpan(int[] p){

	int count = 0;
	
	int[]  S;
	S = new int[p.length]; //creating and initializing the Array S to be returned after span calculation finishes
	
	Stack<Integer> stack = new Stack<Integer>();
	
	stack.push(0); //initialize the stack by pushing the first index of the array onto it.
	
	for(int i = 0; i < p.length; i++) {
		while(!stack.isEmpty() && p[stack.peek()] <= p[i]) { //comparing the values at positions i and i-1 to see which is greater
			count++; //counter to keep track of the span as it increases
			stack.pop();
		}
		if(stack.isEmpty()) { //if the stack becomes empty then the current span is the greatest value i.e. count.
			S[i] = count;
		}
		else {
			S[i] = i - stack.peek(); //if the stack is not empty then it means that the current span is the current i value less the current top of the stack
		}
		stack.push(i); //next index value is pushed onto the stack.			
	}	
	return S; //Array of spans is returned
}
	 
	
	
	public static int[] readInput(Scanner s){
		Queue<Integer> q = new LinkedList<Integer>();
		int n=0;
		if(!s.hasNextInt()){
			return null;
		}
		int temp = s.nextInt();
		while(temp>=0){
			q.offer(temp);
			temp = s.nextInt();
			n++;
		}
		int[] inp = new int[q.size()];
		for(int i=0;i<n;i++){
			inp[i]= q.poll();
		}
		return inp;
	}
	public static void main(String[] args){
		Scanner s;
        Stock m = new Stock();
        if (args.length > 0){
        	try{
        		s = new Scanner(new File(args[0]));
        	} catch(java.io.FileNotFoundException e){
        		System.out.printf("Unable to open %s\n",args[0]);
        		return;
        	}
        	System.out.printf("Reading input values from %s.\n", args[0]);
        }else{
        	s = new Scanner(System.in);
        	System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
        }
            
        int[] price = m.readInput(s);
        System.out.println("The stock prices are:");
        for(int i=0;i<price.length;i++){
        	System.out.print(price[i]+ (((i+1)==price.length)? ".": ", "));
        }

        if(price!=null){
        	int[] span = m.CalculateSpan(price);
        	if(span!=null){
        		System.out.println("The spans are:");
        		for(int i=0;i<span.length;i++){
        			System.out.print(span[i]+ (((i+1)==span.length)? ".": ", "));
        		}
        	}
        }
    }
}