import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.Random;
//Multi threading cpu
public class Cpu implements Callable<Process> {
	Process currentProc;
	ArrayList<Process> processList = new ArrayList<Process>();
	Instruction currentIns;
	int cycles = 0;
	MouseIO mIO = new MouseIO();
	KeyboardIO kIO = new KeyboardIO();
	FileIO fIO = new FileIO();
	Semaphore sem = new Semaphore();
	IOSchedule ioSchedule = new IOSchedule();
	
	public Cpu(Process p, int cycles){
		currentProc = p;
		this.cycles = cycles;
	}
	
	//Implement call() for the Callable
	public Process call() throws Exception{
		int remainingCycles = cycles;
		int cyclesDone = 0;
		//Start process p
		currentProc.startProcess(); // start timer
		currentProc.state = "running";
		while ( currentProc.insIndex<currentProc.insList.size() && remainingCycles > 0 ){
			switch (detectIO()){
			//case 1 is mouseIO
			case 1: if(sem.mouseSem > 0){
					sem.mouseWait();
					mIO.generateIOBurst();
					sem.mouseSignal();
				}
				else {
					yield();
					return currentProc;
				}
				break;
			//case 2 is keyboardIO
			case 2: if(sem.keyboardSem > 0){
				sem.keyboardWait();
				kIO.generateIOBurst();
				sem.keyboardSignal();
				} else {
					yield();
					return currentProc;
				}
			 	break;
			case 3: if (sem.fileSem > 0){
				sem.fileWait();
				fIO.generateIOBurst();
				sem.fileSignal();
				
				} else {
					yield();
					return currentProc;
				}
				break;
			default: break;
			}
			
			currentIns = currentProc.insList.get(currentProc.insIndex);
			String op = currentIns.operation;
			if (op.equals("YIELD")){
				yield();
				currentProc.insList.get(currentProc.insIndex).done = true;
				currentProc.insIndex++;
				//yield and return
				break;
			}
			else if (op.equals("CALCULATE")){
				cyclesDone = calculate(remainingCycles);
				remainingCycles -= cyclesDone;
			}
			else if (op.equals("OUT")){
				out();
				currentProc.insList.get(currentProc.insIndex).done = true;
				currentIns.done = true;
			}
			else if (op.equals("I/O")){
				cyclesDone = io();
				remainingCycles -= cyclesDone;
				//if cyclesDone == 0, IO is not ready, put the process to yield
				if (cyclesDone == 0){
					yield();
					currentProc.insList.get(currentProc.insIndex).done = true;
				}
			}
			
			if (currentIns.done){
				currentProc.insIndex++;
			}
			
			currentProc.cyclesDone += cyclesDone;
		}
		
		//Reach the end of the instruction list
		if(currentProc.insIndex == currentProc.insList.size()){
			currentProc.state = "finished";
			currentProc.done = true;
			currentProc.getTimeUsed();
		}
		else {
			yield();
		}
	return currentProc;
	}
	
	public void saveProcessTime(){
		currentProc.timeUsed += currentProc.getTimeUsed();
	} 
	
	public void printProcess(){
		System.out.print("Process: " + currentProc.pid + "\n"
				+ "Program counter: " + currentProc.insIndex + "\n"
				+ "Memory assigned: " + currentProc.mem + "\n"
				+ "Number of IO: " + currentProc.numIO + "\n"
				+ "Cycles done: " + currentProc.cyclesDone + "\n"
				+ "Time used: " + currentProc.timeUsed + "\n");
		
	}
	
	public void printUnfinished(){
		System.out.println("Unfinished processes:");
		for (Process p : processList){
			System.out.println("Process: " + p.pid);
		}
		
	}
	public int calculate(int cycles){
		currentIns = currentProc.insList.get(currentProc.insIndex);
		int cyclesLeft = currentIns.numCycles - currentIns.cyclesDone;
		if (cycles <= cyclesLeft ){
			currentProc.insList.get(currentProc.insIndex).cyclesDone += cycles;
			return cycles;
		}
		else{
			currentProc.insList.get(currentProc.insIndex).done = true;
			return cyclesLeft;
		}
	}
	
	public int io(){
		return ioSchedule.startIO(currentProc);
	}
	
	public void yield(){
		saveProcessTime();
		currentProc.state = "waiting";
	}
	
	public void out(){
		printProcess();
		printUnfinished();
	}
	
	public int detectIO() { // random number 1-10
		Random r = new Random();
		return r.nextInt(10) + 1;
	}
	
	public boolean preemption(){
		return false;
	}
}
