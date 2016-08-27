/**
 *
 * @author Maxi
 */
public class Layout {

    private Main main;
    
    public Layout(Main main) {
        this.main = main;
    }

    public void display(Internal a) {
        //TODO Andi
        //Hab einfach mal alle beschreibenden Kommentare hier rein gepasted
        
        //zeigt Bild, Text, Sound
        //Triggers on Button press nextStep(String path)  //path aus der entscheidung
        
        /** > bild zeigen, klicken, textbox mit semitransparenz(auf bild), mehr klicks, mehr text
            > Buttons am Ende erste zeigen
            > Skip Button, damit die Entscheidungsbuttons erscheinen*/
    }
    
    public void DisplayEndDialouge(){
        //TODO Andi
        //pop-up, select either:
            //neustart -> m.nextStep(Intro001.txt)
            //ende -> m.endgame()
            //quiz -> m.startQuiz()
    }
    public void displayQuestion(String author, String title){
        //TODO display question and after an answer display the solution, then return nothing
    }
    
    public void close(){
        //weiß nicht, ob man das braucht; halt Fenster schließen und so. Könnte aber auch überflüssig sein.
    }

    
}
