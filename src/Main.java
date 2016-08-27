
import java.util.HashSet;
import javafx.util.Pair;

/**
 *
 * @author Maxi
 */
public class Main {

    private LayoutThing lt;
    private HashSet<Pair<String,String>> titles;

    public Main() {
        this.lt = new LayoutThing(this);
        this.titles = new HashSet<Pair<String,String>>();
    }
    
    public static void main(String[] args){
        Main m = new Main();
        m.nextStep("intro01.txt");
    }
    
    public void nextStep(String path){
        Internal a = TXTReader.readTXT(path);
        rememberTitle(a.getTitle(), a.getAuthor());
        lt.display(a);
    }
    
    public void rememberTitle(String title, String author){
        this.titles.add(new Pair(title,author));
    }
    
    public void endGame(){
        lt.close();
        System.exit(0);
    }
    
    public void startQuiz(){
        //TODO Leo
        //Du hast alle titel und autoren in der titles variable, gesetzt von rememberTitle
        for (Pair<String,String> pair : titles){
            //do something with the information, probably call
            lt.displayQuestion();
            //with some parameters or stuff...
        }
    }
    
}
