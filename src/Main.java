
import java.io.IOException;
import java.util.HashSet;
import javafx.util.Pair;
import javax.swing.JOptionPane;

/** 
 *
 * @author Maxi
 */
public class Main {

    public Layout layout;
    private HashSet<Pair<String, String>> titles;
    public static String storyPath = "res/Stories/";

    public Main() {
        this.layout = new Layout(this);
        this.titles = new HashSet<Pair<String, String>>();
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.nextStep(storyPath + "Intro01.txt");
        while(true){
            m.layout.DisplayEndDialogue();
        }
    }

    public void nextStep(String path) {
        Internal a = null;
        try {
            a = TXTReader.readTXT(path);
        } catch (Exception e) {
            fehlerhandling(path,e);
        }
        rememberTitle(a.getTitle(), a.getAuthor());
        layout.display(a);
    }
    
    private void fehlerhandling(String path,Exception e){
        System.err.println(">>>path " + path + " not found, will lead to null pointer exception is a second!!");
        String errorpath=storyPath + "Intro01.txt";
        if(path.equals(errorpath)){
            JOptionPane.showMessageDialog(null,
                "Es ist ein sehr kritischer Fehler aufgetreten, sage uns einfach bescheid\nNachricht:\n"+e.toString(),
                "Kritischer Fehler ist aufgetretten",
		JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }
        JOptionPane.showMessageDialog(null,
                "Es ist ein Fehler aufgetreten, sage uns einfach bescheid;\nNachricht:\n"+e.toString(),
                "Kritischer Fehler ist aufgetretten",
		JOptionPane.WARNING_MESSAGE);
        nextStep(errorpath);
    }

    public void rememberTitle(String title, String author) {
        this.titles.add(new Pair(title, author));
    }

    public void endGame() {
        layout.close();
        System.exit(0);
    }

    public void startQuiz() {
        //TODO Leo
        //Du hast alle titel und autoren in der titles variable, gesetzt von rememberTitle
        for (Pair<String, String> pair : titles) {
            //do something with the information, probably call
            layout.displayQuestion(pair.getValue(), pair.getKey());
            //with some parameters or stuff...
        }
    }

}
