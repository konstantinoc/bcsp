import java.util.ArrayList;
import java.util.Stack;

public class Base {
	private int[][] base;
	private int N;
	private int M;
	private int K;

	public Base(int n, int m, int k) {
		this.N = n;
		this.M = m;
		this.K = k;
	}
	
	// returns the index of the variable in a specific restriction which has the biggest impact in the Knowledge Base.
	public int bestVarChange(int restriction, Boolean[] currentVars) {
		int bestIndex = -1;
		int minNotSatisfied = 99999;
		
		for(int i=0; i<this.K;i++) {
			currentVars[Math.abs(base[restriction][i])-1] = !currentVars[Math.abs(base[restriction][i])-1];
			int notSat = notSatisfied(currentVars).size();
			if(notSat < minNotSatisfied) {
				minNotSatisfied = notSat;
				bestIndex = i;
			}
			currentVars[i] = !currentVars[i];
		}
		
		return bestIndex;
	}
	
	// Counts how many restrictions are not satisfied
	public ArrayList<Integer> notSatisfied(Boolean[] vars) {
		ArrayList<Integer> noSatisfied = new ArrayList<>();
		for(int i=0; i<base.length;i++) {
			boolean logicValue = false;
			for(int j=0; j<base[i].length; j++) {
				if(this.base[i][j]>0) {
					if(logicValue || vars[Math.abs(this.base[i][j])-1]) {
						logicValue = true;
						break;
					}
				}
				else{
					if(logicValue || !vars[Math.abs(this.base[i][j])-1]) {
						logicValue = true;
						break;
					}
				}
			}
			if(!logicValue) {
				noSatisfied.add(i);
			}
		}
		return noSatisfied;
	}
	
	// Removes all the restrictions where the variable found
	public void removeRestriction(int varName) {
		Stack<Integer> toRemove = new Stack<>();
		
		for(int i=0; i < base.length; i++) {
			if(base[i] != null) {
				for(int j=0; j<base[i].length;j++){
					if (base[i][j] == varName || base[i][j] == (-varName)) {
						// adds the index of the restriction that he must remove
						toRemove.push(i);
						break;
					}
				}
			}
		}
		
		// 'remove' the restrictions
		while(!toRemove.isEmpty()) {
			base[toRemove.pop()] = null;
		}
		
	}
	
	// returns 0 if variable is NOT(e.g -2)
	// Returns 1 if variable is not NOT(e.g 2)
	
	// returns 0 if there is logical not (!) before the variable.
	// returns 1 if there is not logical not before the variable.
	// returns 2 if the variable does not exists.
	public int signOf(int varName) {
		for(int i=0; i<base.length;i++) {
			if(base[i] != null) {
				for(int j=0; j<base[i].length;j++) {
					if (base[i][j] == varName || base[i][j] == (-varName)) {
						if(i > 0)
							return 1;
						return 0;
					}
				}
			}
		}
		return 2;
	}
	
	
	// if there is variables with the same sign in all restriction
	// it gets value and the restriction is removed
	// then repeat until no pure symbols exists
	public boolean hasSameSign(int varName) {
			
		boolean firstTimeFlag = true;
		int firstTimeSign = 1;
		
		for(int i=0; i<this.base.length; i++) {
			if(this.base[i] != null) {
				for(int j=0; j<this.base[i].length;j++) {
					if(Math.abs(this.base[i][j]) == varName) {
						if(firstTimeFlag) {
							firstTimeFlag = false;
							if (this.base[i][j] < 0) {
								firstTimeSign = 0;
							}
						}
						else {
							if(this.base[i][j] > 0 && firstTimeSign!=1 || this.base[i][j] < 0 && firstTimeSign != 0)
								return false;
						}
					}
				}
			}
		}
		return true;
	}
		
	public Boolean[] pureSymbols() {
			
			Boolean[] vars = new Boolean[N];	
			boolean noRemove = true;
			
			for(int i=0; i<N; i++)
				vars[i] = null;
			
			do{
				for(int i=1; i<=N; i++) {
					if(this.hasSameSign(i)) {
						if(this.signOf(i) == 1)
							vars[i-1] = true;
						else if(this.signOf(i) == 0)
							vars[i-1] = false;
						this.removeRestriction(i);
						noRemove = false;
					}
				}
				if(noRemove)
					noRemove =  false;
			}while(noRemove);
			
			return vars;
		}
		
	// check if knowledge base returns true
	public boolean checkKB(Boolean[] vars) {
		for(int i=0; i<this.base.length; i++) {
			boolean logicValue = false;
			if(this.base[i] != null) {
				for(int j=0; j<this.base[i].length; j++) {
					if(this.base[i][j]>0) {
						if(!(vars[Math.abs(this.base[i][j])-1] == null)) {
							if(logicValue || vars[Math.abs(this.base[i][j])-1]) {
								logicValue = true;
								break;
							}
						}
						else {
							logicValue = true;
						}
					}
					else{
						if(!(vars[Math.abs(this.base[i][j])-1] == null)) {
							if(logicValue || !vars[Math.abs(this.base[i][j])-1]) {
								logicValue = true;
								break;
							}
						}
						else {
							logicValue = true;
						}
					}
				}
				if (logicValue == false) {
					return false;
				}
			}
		}
		return true;
	}
		
	public int[][] getBase() {
		return base;
	}
	
	public void setBase(int[][] base) {
		this.base = base;
	}
	
	public int getN() {
		return N;
	}
	
	public void setN(int n) {
		N = n;
	}
		
	public int getM() {
		return M;
	}

	public void setM(int m) {
		M = m;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

}
