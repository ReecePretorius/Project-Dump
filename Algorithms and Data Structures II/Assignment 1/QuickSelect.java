/*
* Filename: QuickSelect.java
* Details: CSC226 Assignment #1
* */ 

import java.util.*;
import java.io.*;

public class QuickSelect {
    
    public static int QuickSelect(int[] A, int k) {
    	if(k > A.length) {
    		return -1;
    	}
    	if(A.length == 1) {
    		return A[0];
    	}

        int L[] = new int[A.length];
        int E[] = new int[A.length];
        int G[] = new int[A.length];

    	int pivot = findPivot(A);
    	
    	int temp_less  = 0;
		int temp_greater = 0;
    	for(int j = 0; j <= A.length - 1; j++) {
    		if(A[j] == pivot) {
    			E[0] = pivot;
    		}
    		else if(A[j] < pivot) {
    			L[temp_less] = A[j];
    			temp_less++;
    		}
    		else {
    			G[temp_greater] = A[j];
    			temp_greater++;
    		}
    	}
    	
    	L = resizeArray(L, temp_less);
    	G = resizeArray(G, temp_greater);
    	
    	if(k <= L.length) {
    		return QuickSelect(L, k);
    	}
    	else if(k == L.length + 1) {
    		return pivot;
    	}
    	else {
    		return QuickSelect(G, k - L.length - 1);
    	}
    }
    
    public static int[] resizeArray(int[] arr, int size) {
    	int[] new_array = new int[size];
    	for(int i = 0; i < size; i++) {
    		new_array[i] = arr[i];
    	}
    	return new_array;
    }
    
    public static int findPivot(int[] arr) {
    	if(arr.length <= 7) {
    		Arrays.sort(arr);
    		return(arr[arr.length/2]);
    	}
    	
    	int temp[] = null;
    	int medians[] = new int[(int)Math.ceil((double)(arr.length + 1)/7)];
    	int count = 0;
    	int index = 0;
    	
    	//creates an array filled with all of the small medians
    	while(count < arr.length) {
    		if(arr.length - count < 7) {
    			temp = new int[arr.length - count];
    		}
    		else {
    			temp = new int[7];
    		}
    		
    		for(int j = 0; j < temp.length; j++) {
	    		temp[j] = arr[count];
	    		count++;
	    		
	    		if(j == 6 || j < 6 && count == arr.length) {
	    			Arrays.sort(temp);
	    			medians[index] = temp[temp.length/2];
	    			index++;
	    		}
	    	}
    	}
    	if(medians.length > index) {
    		medians = resizeArray(medians, index);
    	}
    	Arrays.sort(medians);
    	return(medians[medians.length/2]);
    }
    
    public static void main(String[] args) {
        Scanner s;
        int[] array;
        int k;
        if(args.length > 0) {
	    try{
		s = new Scanner(new File(args[0]));
		int n = s.nextInt();
		array = new int[n];
		for(int i = 0; i < n; i++){
		    array[i] = s.nextInt();
		}
	    } catch(java.io.FileNotFoundException e) {
		System.out.printf("Unable to open %s\n",args[0]);
		return;
	    }
	    System.out.printf("Reading input values from %s.\n", args[0]);
        }
	else {
	    s = new Scanner(System.in);
	    System.out.printf("Enter a list of non-negative integers. Enter a negative value to end the list.\n");
	    int temp = s.nextInt();
	    ArrayList<Integer> a = new ArrayList<Integer>();
	    while(temp >= 0) {
		a.add(temp);
		temp=s.nextInt();
	    }
	    array = new int[a.size()];
	    for(int i = 0; i < a.size(); i++) {
		array[i]=a.get(i);
	    }
	    
	    System.out.println("Enter k");
        }
        k = s.nextInt();
        System.out.println("The " + k + "the smallest number in the list is "
			   + QuickSelect(array,k));	
    }
}
