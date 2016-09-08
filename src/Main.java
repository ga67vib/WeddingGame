
import java.util.HashSet;
import javafx.util.Pair;

/**
 *
 * @author Maxi
 */
public class Main {

    public Layout layout;
    private HashSet<Pair<String, String>> titles;

    public Main() {
        this.layout = new Layout(this);
        this.titles = new HashSet<Pair<String, String>>();
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.nextStep("res/Stories/DB_Bö_002_1.txt");
    }

    public void nextStep(String path) {
        Internal a = null;
        try {
            a = TXTReader.readTXT(path);
        } catch (Exception e) {
            System.err.println(">>>path " + path + " not found, will lead to null pointer exception is a second!!");
        }
        rememberTitle(a.getTitle(), a.getAuthor());
        layout.display(a);
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
            layout.displayQuestion();
            //with some parameters or stuff...
        }
    }

}
