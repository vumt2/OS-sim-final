import java.util.Random;

public class MouseIO {

    public MouseIO(){
    }
    public  int  generateIOBurst(){

    	Random r = new Random();
		int cycles = r.nextInt(50) + 1;
        for(int i=0;i<cycles;i++){
            //IO operations
        }
        System.out.println("Mouse IO  "+ cycles+ " cycles performed");
        return  cycles;
    }
}

