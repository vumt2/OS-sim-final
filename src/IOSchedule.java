
public class IOSchedule {

	MouseIO mIO = new MouseIO();
	KeyboardIO kIO = new KeyboardIO();
	FileIO fIO = new FileIO();
	Semaphore sem = new Semaphore();
	
	public IOSchedule(){

	}
	
	public int startIO(Process current){
		int cyclesDone = 0;
		if(current.insList.get(current.insIndex).io.equals("FileIO")){
			if(sem.fileSem > 0){
				if (sem.fileQ.isEmpty()){
					sem.fileWait();
					cyclesDone = fIO.generateIOBurst();
					sem.fileSignal();
					current.numIO++;
				}
				else if (sem.peekFileQ().pid == current.pid){
					sem.fileWait();
					cyclesDone = fIO.generateIOBurst();
					if (current.done == true){
						sem.pollFileQ();
					}
					sem.fileSignal();
					current.numIO++;
				}
			}
			else{
				sem.addFileQ(current);
			}
		}
		else if (current.insList.get(current.insIndex).io.equals("MouseIO")){
			if(sem.mouseSem > 0){
				if (sem.mouseQ.isEmpty()){
					sem.mouseWait();
					cyclesDone = mIO.generateIOBurst();
					sem.mouseSignal();
					current.numIO++;
				}
				else if (sem.peekMouseQ().pid == current.pid){
					sem.mouseWait();
					cyclesDone = mIO.generateIOBurst();
					if (current.done == true){
						sem.pollMouseQ();
					}
					sem.mouseSignal();
					current.numIO++;
				}
			}
			else{
				sem.addMouseQ(current);
			}
		}
		else if (current.insList.get(current.insIndex).io.equals("KeyboardIO")){
			if(sem.keyboardSem > 0){
				if (sem.keyboardQ.isEmpty()){
					sem.keyboardWait();
					cyclesDone = kIO.generateIOBurst();
					sem.keyboardSignal();
					current.numIO++;
				}
				else if (sem.peekKeyboardQ().pid == current.pid){
					sem.keyboardWait();
					cyclesDone = kIO.generateIOBurst();
					if (current.done == true){
						sem.pollKeyboardQ();
					}
					sem.keyboardSignal();
					current.numIO++;
				}
			}
			else{
				sem.addKeyboardQ(current);
			}
		}
		return cyclesDone;
	}
}
