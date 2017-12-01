
public class Instruction {
	String operation;
	String io;
	int numCycles;
	int cyclesDone = 0;
	int time = 0;
	boolean done = false;
	
	public Instruction(String op, String io, int cycles){
		operation = op;
		this.io = io;
		numCycles = cycles;
	}
	
	public Instruction(String op, int cycles){
		operation = op;
		numCycles = cycles;
		io = null;
	}
	
	public Instruction(String op, String io){
		operation = op;
		this.io = io;
		numCycles = 0;
	}
	
	public Instruction(String op){
		operation = op;
		io = null;
		numCycles = 0;
	}

	public String toString(){
		return operation;
	}

}
