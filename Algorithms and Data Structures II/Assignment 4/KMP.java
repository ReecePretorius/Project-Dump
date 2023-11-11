/*
* Filename: KMP.java
* Details: CSC226 Assignment #4
* */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class  KMP{
	private static String pattern;
	private static int[] arr;

	
    public KMP(String pattern){
    	this.pattern = pattern;
	    arr = new int[pattern.length()];
	    arr[0] = 0; int i = 1; int j = 0;
	    
	    while(i < pattern.length()){
		    if(pattern.charAt(i) == pattern.charAt(j)){
			    j++;
			    arr[i] = j;
			    i++;
		    }
		    else {
			    if(j != 0){
				    j = arr[j - 1];
			    }
			    else {
					arr[i] = j;
				    i++;
			    }
		    }
	    }
    }
    
    public int search(String txt){
		int len = txt.length();
	    int i = 0; int j = 0;
	    
	    while (i < len){
		    if (txt.charAt(i) == pattern.charAt(j)){
			    i++;
			    j++;
		    }
		    
		    if(j == pattern.length()){
				return i - pattern.length();
		    }
		    else if (i < len && pattern.charAt(j) != txt.charAt(i)){
				if (j != 0){
					j = arr[j - 1];
				}
				else {
					i = i + 1;
				}
		    }
	    }
	    return len;
    }

        
    public static void main(String[] args) throws FileNotFoundException{
    	Scanner s;
    	if (args.length > 0){
    	    try{
    		s = new Scanner(new File(args[0]));
    	    }
    	    catch(java.io.FileNotFoundException e){
    		System.out.println("Unable to open "+args[0]+ ".");
    		return;
    	    }
    	    System.out.println("Opened file "+args[0] + ".");
    	    String text = "";
    	    while(s.hasNext()){
    		text += s.next() + " ";
    	    }
    	    
    	    for(int i = 1; i < args.length; i++){
    		KMP k = new KMP(args[i]);
    		int index = k.search(text);
    		if(index >= text.length()){
    		    System.out.println(args[i] + " was not found.");
    		}
    		else System.out.println("The string \"" + args[i] + "\" was found at index " + index + ".");
    	    }
    	}
    	else{
    	    System.out.println("usage: java SubstringSearch <filename> <pattern_1> <pattern_2> ... <pattern_n>.");
    	}
   }

}