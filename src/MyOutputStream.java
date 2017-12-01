import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class MyOutputStream extends OutputStream {
	private JTextArea textArea;
	
	public MyOutputStream(JTextArea t){
		textArea = t;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(int arg) throws IOException {
		textArea.append(String.valueOf((char)arg));
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	
	
}
