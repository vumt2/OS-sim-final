import java.util.ArrayList;

public class Process implements Comparable<Process>{
	String fileName;
	int mem;
	int numCycles;
	int cyclesDone;
	int pid;
	int numIO;
	int rrLevel; //roundrobin level
	String state;
	Clock c = new Clock();
	boolean done;
	boolean swap;
	long timeUsed;
	ArrayList<Instruction> insList; // list of instructions
	int insIndex; // index for insList
	
	public Process(String fileName, int mem, int numCycles, int pid, ArrayList<Instruction> insList) {
		super();
		this.fileName = fileName;
		this.mem = mem;
		this.numCycles = numCycles;
		this.pid = pid;
		done = false;
		swap = false;
		timeUsed = 0;
		this.insList = insList;
		cyclesDone = 0;
		numIO = 0;
		state = null;
		rrLevel = 0;
	}
	
	public void startProcess(){
		c.clockStart();
	}
	
	public long getTimeUsed(){
		c.clockStop();
		return timeUsed = timeUsed + c.getTime();
	}
	
	public int compareTo(Process other){
		if(Integer.compare(this.rrLevel,other.rrLevel) == 0){
			return Integer.compare(this.pid, other.pid);
		};
		return Integer.compare(this.rrLevel,other.rrLevel);
	}

}
