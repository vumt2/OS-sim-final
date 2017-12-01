import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Scheduler {
	int pid;
	MemManage manager = new MemManage();
	Map<Integer, Integer> roundRobin = new HashMap<Integer, Integer>();
	PriorityQueue<Process> ReadyQ = new PriorityQueue<Process>();
	PriorityQueue<Process> WaitQ = new PriorityQueue<Process>();
	
	public Scheduler(){
		pid = 0;
		roundRobin.put(0, 50); //roundRobin level 0, 50 cycles
		roundRobin.put(1, 100);//roundRobin level 1, 100 cycles
		roundRobin.put(2, 200);//roundRobin level 2, 200 cycles
	}
	
	public void runRoundRobin(int cycles){
		
		int cyclesDone = 0;
		
		while ((cyclesDone < cycles) && (!ReadyQ.isEmpty() || !WaitQ.isEmpty())){
				
			while(!ReadyQ.isEmpty() && cyclesDone<cycles){
				
				Process current = ReadyQ.poll();
				int oldCycles = current.cyclesDone;
				int rrCycles = roundRobin.get(current.rrLevel); // rr means roundrobin
				
				if((cycles - cyclesDone) < rrCycles){
					rrCycles = (cycles - cyclesDone);
				}
				//call swapProcess if current process's swap == true
				if(current.swap){
					current = swapProcess(current);
				}
				//run current process using Java's multithreading (Callable, Future and Executor)
				ExecutorService exec = Executors.newCachedThreadPool();
				Callable<Process> myCpu = new Cpu(current, rrCycles);
				Future<Process> submit = exec.submit(myCpu);
				
				//Get the resulting process
				try {
	                current= submit.get();
	            } catch (InterruptedException e) {
	                e.getMessage();
	            } catch (ExecutionException e) {
	                e.getMessage();
	            }
	
				exec.shutdown();
				
				cyclesDone += (current.cyclesDone - oldCycles); //cycles after run - cycles before run (oldCycles)
				
				if(!current.done){
					if(current.rrLevel < 2 ){
						current.rrLevel++;
					}
						ReadyQ.add(current);
				}
				else{
					System.out.println("Process " + current.pid + " finished.");
					manager.deallocMem(current.pid);
					}
			}
			
			//If all processes in ReadyQ is finished and the cyclesDone < cycles, add processes from WaitQ
			if ( cyclesDone < cycles ) {
				while(!WaitQ.isEmpty()){
					Process p = WaitQ.peek();
					p.state = "Ready";
					moveToReadyQ(p);
				}
			}
		}
	}
	
	// This method swaps the processes at the end of the queue to make memory for the current process
	public Process swapProcess(Process p){
		ArrayList<Process> temp = new ArrayList<Process>();
		while(!ReadyQ.isEmpty()){
			temp.add(ReadyQ.poll());
		}
		int index = temp.size() - 1;
		while(manager.getMemRemain() < p.mem){
			temp.get(index).swap = true;
			manager.deallocMem(temp.get(index).pid);
			manager.swapped.put(temp.get(index).pid, "Swapped");
			System.out.println(pid + " swapped.");
			index--;
		}
		for (int i = 0; i<temp.size(); i++){
			ReadyQ.add(temp.get(i));
		}
		manager.allocMem(p.mem, p.pid);
		p.swap = false;
		manager.swapped.remove(p.pid);
		return p;
	}
	
	public void reset(){
		pid = 0;
		ReadyQ.clear();
		WaitQ.clear();
	}
	
	public boolean moveToReadyQ(Process p){
		WaitQ.remove(p);
		addToReadyQ(p);
		return true;
	}

	public void addToReadyQ(Process p){
		if(p.mem < manager.getMemRemain()){
			manager.allocMem(p.mem, p.pid);
			ReadyQ.add(p);
			System.out.println("Added process " + pid + " to ReadyQ");
		}
		else{
			manager.swapped.put(p.pid, "Swapped");
			p.swap = true;
			WaitQ.add(p);
			System.out.println("Added process " + pid + " to WaitQ");
		}
	}
	
	public boolean moveToWaitQ(Process p){
		ReadyQ.remove(p);
		WaitQ.add(p);
		return true;
	}
	
	public boolean removeFromReadyQ(int pid){
		PriorityQueue<Process> temp = new PriorityQueue<Process>();
		while(!ReadyQ.isEmpty()){
			Process tempProc = ReadyQ.poll();
			if (tempProc.pid != pid){
				temp.add(tempProc);
			}
			else { manager.deallocMem(pid);}			
		}
		ReadyQ = temp;
		return true;
	}
	
	public int getNextPid(){
		return pid++;
	}
}
