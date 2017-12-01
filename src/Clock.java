
public class Clock {
	
	private long start; //in nanoseconds
	private long end; //in nanoseconds
	private long cpuTimeUsed; // in nanoseconds
	
	public Clock(){
		cpuTimeUsed = 0;
	}
	
	public void clockStart(){
		start = System.nanoTime();
	}
	
	public void clockStop(){
		end = System.nanoTime();
	}
	
	public long getTime(){
		cpuTimeUsed = end - start;
		return cpuTimeUsed;
	}
}

