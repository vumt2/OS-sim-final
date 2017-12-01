import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class OS {
	Scheduler scheduler;
	
	public OS(){
		scheduler = new Scheduler();
	}
	
	public void runCommand(String cmd){
		String[] commands = cmd.split(" ");
		String action = commands[0];
		if(action.equals("proc")){
			proc();
		}
		else if (action.equals("reset")){
			reset();
		}
		else if (action.equals("mem")){
			memory();
		}
		else if (action.equals("load")){
			System.out.println("Loading...");
			if(commands.length == 1){
				System.out.println("No file name.");
			}
			else {
				String fileName = commands[1];
				load(fileName);
			}
		}
		else if (action.equals("exe")){
			System.out.println("Executing...");
			if (commands.length == 1){
				execute(10000);
			}
			else {
				int cycles = Integer.parseInt(commands[1]);
				execute(cycles);
			}
		}
		else if (action.equals("rm")){
			if (commands.length == 1){
				System.out.println("No pid entered.");
			}
			else {
				int pid = Integer.parseInt(commands[1]);
				remove(pid);
				System.out.println("Removed " + pid);
			}
		}
		else if (action.equals("exit")){
			System.exit(0);
		}
		else {
			System.out.println("Unrecognized command. ");
		}
	}
	
	public void remove(int pid){
		scheduler.removeFromReadyQ(pid);
	}
	
	public void execute(int cycles){
		scheduler.runRoundRobin(cycles);
	}
	
	public void memory(){
		System.out.println(scheduler.manager.getMemUsage());
	}
	
	public void load(String fileName){
		File f = new File(fileName);
		try{
			Scanner s = new Scanner(f);
			int mem = s.nextInt();
			ArrayList<Instruction> ins = new ArrayList<Instruction>();
			int totalCycles = 0;
			int pid = scheduler.getNextPid();
			while(s.hasNextLine()){
				String line = s.nextLine();
				String[] lineArr = line.split(" ");
				String op = lineArr[0];
				if(op.equals("YIELD")){
					Instruction i = new Instruction(op);
					ins.add(i);
				}
				else if (op.equals("IO")){
					String type = lineArr[1];
					Instruction i = new Instruction(op, type);
					ins.add(i);
				}
				else if (op.equals("CALCULATE")){
					int insCycles = Integer.parseInt(lineArr[1]);
					totalCycles += insCycles;
					Instruction i = new Instruction(op, insCycles);
					ins.add(i);
				}
			}
			Process p = new Process(fileName, mem, totalCycles, pid, ins);
			scheduler.addToReadyQ((Process) p);
			System.out.println(fileName + " loaded.");
			s.close();
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void proc(){
		System.out.println("Waiting Queue:");
		PriorityQueue<Process> tempWait = new PriorityQueue<Process>();
		while(!scheduler.WaitQ.isEmpty()){
			Process p = scheduler.WaitQ.poll();
			System.out.println("PID: " + p.pid + " File: " + p.fileName
					+ " Cycles Done: " + p.cyclesDone
					+ " Process State: " + p.state
					+ " Num IO: " + p.numIO
					+ " Instruction counter: " + p.insIndex
					);
			tempWait.add(p);
		}
		scheduler.WaitQ = tempWait;
		
		System.out.println("Ready Queue:");
		PriorityQueue<Process> tempReady = new PriorityQueue<Process>();
		while(!scheduler.ReadyQ.isEmpty()){
			Process p = scheduler.ReadyQ.poll();
			System.out.println("PID: " + p.pid + " File: " + p.fileName
					+ " Cycles Done: " + p.cyclesDone
					+ " Process State: " + p.state
					+ " Num IO: " + p.numIO
					+ " Instruction counter:" + p.insIndex
					);
			tempReady.add(p);
		}
		scheduler.ReadyQ = tempReady;
		
	}
	
	public void reset(){
		scheduler.manager.reset();
		scheduler.reset();
		System.out.println("System reset");
	}
	
}
