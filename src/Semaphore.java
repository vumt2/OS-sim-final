import java.util.PriorityQueue;
public class Semaphore {
	int mouseSem;
	int keyboardSem;
	int fileSem;
	PriorityQueue<Process> mouseQ = new PriorityQueue<Process>();
	PriorityQueue<Process> keyboardQ = new PriorityQueue<Process>();
	PriorityQueue<Process> fileQ = new PriorityQueue<Process>();
	
	public Semaphore(){
		mouseSem = 1;
	    keyboardSem = 1;
	    fileSem = 1;
	}
	
	public void mouseWait(){
		mouseSem--;
	}
	
	public void mouseSignal(){
		mouseSem++;
	}
	
	public void keyboardWait(){
		keyboardSem--;
	}
	
	public void keyboardSignal(){
		keyboardSem++;
	}
	
	public void fileWait(){
		fileSem--;
	}
	
	public void fileSignal(){
		fileSem++;
	}
	
	public void addMouseQ(Process p){
		mouseQ.add(p);
	}
	
	public void pollMouseQ(){
		mouseQ.poll();
	}
	
	public Process peekMouseQ(){
		return mouseQ.peek();
	}
	
	public void addKeyboardQ(Process p){
		keyboardQ.add(p);
	}
	
	public void pollKeyboardQ(){
		keyboardQ.poll();
	}
	
	public Process peekKeyboardQ(){
		return keyboardQ.peek();
	}
	
	public void addFileQ(Process p){
		fileQ.add(p);
	}
	
	public void pollFileQ(){
		fileQ.poll();
	}
	
	public Process peekFileQ(){
		return fileQ.peek();
	}
}
