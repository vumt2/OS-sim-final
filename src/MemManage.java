import java.util.HashMap;
import java.util.Map;

public class MemManage {
	static final int TOTALMEM = 4096; //4096MB 
	int memRemain;
	Map<Integer, Integer> active = new HashMap<Integer, Integer>();
	Map<Integer, String> swapped = new HashMap<Integer, String>();
	
	public MemManage(){
		memRemain = TOTALMEM;
	}
	
	public boolean allocMem(int memRequested, int pid){
		if(memRemain >= memRequested){
			memRemain = memRemain - memRequested;
			active.put(pid, memRequested);
			return true;
		}
		else{ return false;}
	}
	
	public boolean deallocMem(int pid){
		if(active.containsKey(pid)){
			memRemain = memRemain + active.get(pid);
			active.remove(pid);
			return true;
		}
		else{
			if(swapped.containsKey(pid)){
				return true;
			} else {
				return false;
			}
		}
	}
	
	public int getMemRemain(){
		return memRemain;
	}
	
	public void reset(){
		memRemain = TOTALMEM;
		active.clear();
	}
	
	public String getMemUsage(){
		String temp = "Total Mem: " + TOTALMEM + " Remaining: " + memRemain + "\n";
		for (Map.Entry<Integer, Integer> entry : active.entrySet()){
			temp += "Process: " + entry.getKey() + " usage: " + entry.getValue() + "\n";
		}
		
		for (Map.Entry<Integer, String> entry : swapped.entrySet()){
			temp += "Process: " + entry.getKey() + " " + entry.getValue() + "\n";
		}
		
		return temp;
	}
}
