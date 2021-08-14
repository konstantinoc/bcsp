import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length != 3) {
			syntaxError();
		}
		Base KB = initializeKnowledgeBase(args[1]);
		
		if(args[0].equals("depth")) {
			long startTime = System.currentTimeMillis();
			Boolean[] tb = depth(KB);
			long finishTime = System.currentTimeMillis();
			
			System.out.println("Time: " + (float)(finishTime-startTime)/1000 +" seconds");
			String out = arrayToString(tb);
			
			writeInFile(out, args[2]);
		}
		else if(args[0].equals("walksat")) {
				long startTime = System.currentTimeMillis();
				Boolean[] vars = walkSat(KB);
				long finishTime = System.currentTimeMillis();
				System.out.println("Time: " + (float)(finishTime-startTime)/1000 +" seconds");
				
				String out = arrayToString(vars);
				
				writeInFile(out, args[2]);
			}
		
		else {
			syntaxError();
		}
	}
	
	// solves the problem with WalkSAT algorithm.
	public static Boolean[] walkSat(Base KB){
		int counter = 0;
		Boolean[] vars = initializeRandom(KB.getN());
		Random rand = new Random();
		
		// restrictions where are not satisfied
		ArrayList<Integer> t0 =  KB.notSatisfied(vars);
		
		long stopTime = System.currentTimeMillis() + 60*1000;
		// while there are't any dissatisfied  restriction
		while(t0.size() != 0){
			counter++;
			// random not satisfied restriction
			int randRestriction = t0.get((int)Math.floor(Math.random()*t0.size()));
			
			// random integer. We use it for the chance.
			int chance = rand.nextInt(101);
			
			// chance to change a random variable from a not satisfied restriction(95%)
			if(chance < 95) {
				int randIndex = rand.nextInt(KB.getK()-1);
				vars[Math.abs(KB.getBase()[randRestriction][randIndex])-1] = !vars[Math.abs(KB.getBase()[randRestriction][randIndex])-1]; 
			}
			// chance to change the variable with the best impact from a not satisfied restriction(5%)
			else {
				int bestVarToChange = KB.bestVarChange(randRestriction, vars);
				vars[bestVarToChange] = !vars[bestVarToChange]; 
			}
			
			t0 =  KB.notSatisfied(vars);
			
			if (System.currentTimeMillis() > stopTime) {
				System.out.println("\nNo Solution found. Out of time(60 seconds)!\nSteps = " + counter);
				return null;
			}
		}
		
		
		System.out.println("\nSolution found with WalkSAT!\nSteps = " + counter);
		return vars;
	}
	
	// Gets as argument the array's size and return an array with random boolean value in every index
	private static Boolean[] initializeRandom(int totalVars) {
		Boolean[] vars = new Boolean[totalVars];
		for(int i=0; i<totalVars;i++)
			vars[i] = null;
		
		Random rand = new Random();
		for(int i=0; i<vars.length; i++) {
			vars[i] = rand.nextBoolean();
		}
		return vars;
	}

	// solves the problem with depth algorithm using CPLL.
	public static Boolean[] depth(Base KB) {
		int counter = 0;
		Stack<Node> stack = new Stack<>();
		
		// if there is variables with the same sign in all restriction
		// it gets value  and the restriction is removed
		// then repeat until no pure symbols exists
		Boolean[] vars = KB.pureSymbols();
		
		// push the first node in a stack
		stack.push(new Node(vars));
		
		while(!stack.isEmpty()) {
			counter++;
			Node currentNode = stack.pop();
			
			currentNode.findChildren();
			long stopTime = System.currentTimeMillis() + 60*1000;
			// if knowledge base return true and all variables have value
			if(KB.checkKB(currentNode.getVars()) && !currentNode.hasChild()) {
				System.out.println("\nSolution found with depth(CPLL)!");
				System.out.println("Steps: " + counter);
				return currentNode.getVars();
			}
			
			// push the new nodes(children) in the stack
			for(int i=0; i<2;i++) {
				if(KB.checkKB(currentNode.getChildren()[i].getVars())) {
					stack.push(currentNode.getChildren()[i]);
				}
			}
			if (System.currentTimeMillis() > stopTime) {
				System.out.println("\nNo Solution found. Out of time(60 seconds)!\n + Steps:" + counter);
				return null;
			}
		}
		System.out.println("\nNO SOLUTION EXIST. Proved by depth first(CPLL)\nSteps:" + counter);
		return null;
	}
	
	public static Base initializeKnowledgeBase(String inFile) {
		Base KB = null;
		int[][] tempBase = null;
		try {
		      File myObj = new File(inFile);
		      Scanner myReader = new Scanner(myObj);
		      
		      int N = myReader.nextInt();
		      int M = myReader.nextInt();
		      int K = myReader.nextInt();
		      
		      KB = new Base(N,M,K);
		      KB.setN(N);
		      tempBase = new int[M][K];

		      int j = 0;
		      int i = 0;
		      
		      while (myReader.hasNextInt()) {
		    	 if(j<K) {
		    		 tempBase[i][j] = myReader.nextInt();
		    		 j++;
		    	 }else {
		    		 i++;
		    		 j=0;
		    		 tempBase[i][j] = myReader.nextInt();
		    		 j++;
		    	 }
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("Cant read from file '"+ inFile +"'.");
		      Runtime.getRuntime().halt(0);
		      e.printStackTrace();
		    }	
		KB.setBase(tempBase);
		
		return KB;
	}

	public static String arrayToString(Boolean[] arr) {
		String s = "";
		if(arr != null) {
			for(Boolean b:arr) {
				if(b)
					s += "1 ";
				else
					s += "-1 ";
			}
			return s;
		}
		
		// if the array is null
		return "NO SOLUTION";
	}
	
	public static void writeInFile(String string, String outFile) {
		try {
		      FileWriter myWriter = new FileWriter(outFile);
		      myWriter.write(string);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file "+ outFile+ ".");
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}

	public static void syntaxError() {
		System.out.println("Syntax Error\n");
		System.out.println("Use the following syntax: \n");
		System.out.println("bcsp.exe <method> <inputfile> <outputfile>\n");
		System.out.println("Where:");
		System.out.println("<Method> is either 'depth' or 'walksat' (without the quotes)");
		System.out.println("<inputfile> is the name of the file with the problem decription");
		System.out.println("<outputfile> is the name of the output file with the solution");
		Runtime.getRuntime().halt(0);
	}
}
