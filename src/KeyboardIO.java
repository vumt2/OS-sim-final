import java.util.Random;

public class KeyboardIO {
    public KeyboardIO(){}
    public  int  generateIOBurst(){

    	Random r = new Random();
		int cycles = r.nextInt(50) + 1;
        for(int i=0;i<cycles;i++){
         //IO operations
        }
        System.out.println("Keyboard IO "+ cycles+ " cycles performed");
        return cycles;
    }

}