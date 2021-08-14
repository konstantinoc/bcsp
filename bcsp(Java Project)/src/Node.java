
public class Node {
	private Node[] children;
	private Boolean[] vars;
	
	public Node(Boolean[] vars) {
		this.children = new Node[2];
		this.vars = vars;
	}

	public void findChildren() {
		Boolean[] temp1 = new Boolean[vars.length];
		Boolean[] temp2 = new Boolean[vars.length];
		
		for(int j=0; j<vars.length;j++) {
			temp1[j] = vars[j];
		}
		for(int j=0; j<vars.length;j++) {
			temp2[j] = vars[j];
		}
		
		for(int i=0; i<vars.length;i++) {
			if(vars[i] == null) {	
				
				temp1[i] = false;
				children[0] = new Node(temp1);
				
				temp2[i] = true;
				children[1] = new Node(temp2);
				
				return;
			}

		}
	}
		
	public void printVars() {
		for(Boolean b:vars)
			System.out.print(b + " ");
	}
	
	public boolean hasChild() {
		for(int i=0; i<children.length; i++) {
			if(children[i] != null)
				return true;
		}
		return false;
	}
	
	public Node[] getChildren() {
		return children;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public Boolean[] getVars() {
		return vars;
	}

	public void setVars(Boolean[] vars) {
		this.vars = vars;
	}
	
	
	
}
