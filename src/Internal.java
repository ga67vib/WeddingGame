
import java.util.ArrayList;

/**
 *
 * @author Maxi
 */
public class Internal {
    private String author, title, picturePath, musicPath;
    private ArrayList<String> story;
    private ArrayList<Decision> decisions;

    public Internal(String author, String title, String picturePath, String musicPath, ArrayList<String> story, ArrayList<Decision> decisions) {
        this.author = author;
        this.title = title;
        this.picturePath = picturePath;
        this.musicPath = musicPath;
        this.story = story;
        this.decisions = decisions;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public ArrayList<String> getStory() {
        return story;
    }

    public ArrayList<Decision> getDecisions() {
        return decisions;
    }
    
    
    
}
