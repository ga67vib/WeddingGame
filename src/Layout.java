
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Andi
 */
public class Layout {
    
    JFrame frame;
    myPanel panel;
    BufferStrategy bufferStrat;

    BufferedImage background;
    BufferedImage textBox;

    int windowSizeX = 1280;
    int windowSizeY = 720;

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private Cursor cursor;

    private final Main main;
    
    public Layout(Main main) {
        this.main = main;
        
        init();
    }

    private void initCursor() {
        try {
            cursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(new File("res/Cursor.png")), new Point(16, 16), "CustomCursor");
        } catch (IOException e) {
            System.err.println("Couldn't load cursor!");
        }
    }

    private void setBackground(String path) {
        try {
            background = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Couldn't load background!");
        }
    }

    private void init() {
        frame = new JFrame();
        frame.setSize(windowSizeX, windowSizeY);
        frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);
        //frame.setUndecorated(true);

        try {
            textBox = ImageIO.read(new File("res/Textbox.png"));
        } catch (IOException e) {
            System.err.println("Couldn't load Textbox!");
        }

        setBackground("res/background.png");
        initCursor();
        if (cursor != null) {
            frame.setCursor(cursor);
        }

        panel = new myPanel();
        panel.setIgnoreRepaint(true);

        /* frame.addKeyListener(new PlayerController());
        frame.addMouseListener(MC);
        frame.addMouseMotionListener(MC);*/
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        frame.setVisible(true);

        frame.createBufferStrategy(2);
        bufferStrat = frame.getBufferStrategy();
    }
    
    private BufferedImage loadImage(String path) {
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Couldn't load Textbox!");
        }
        
        return image;
    }

    private void paint(String text) {
        Graphics2D graphics = (Graphics2D) panel.getGraphics();

        panel.paintComponent(text, bufferStrat, background, textBox);

        try {
            Thread.sleep(30);
        } catch (Exception e) {
        }
    }


    public void display(Internal internal) {
        //TODO Andi
        //Hab einfach mal alle beschreibenden Kommentare hier rein gepasted
        
        //zeigt Bild, Text, Sound
        setBackground(internal.getPicturePath());  // what if we don't have an image? Load a default?
        // TODO: load sound here
        
        
        //Triggers on Button press nextStep(String path)  //path aus der entscheidung
        
        /** > bild zeigen, klicken, textbox mit semitransparenz(auf bild), mehr klicks, mehr text
            > Buttons am Ende erste zeigen
            > Skip Button, damit die Entscheidungsbuttons erscheinen*/
        paint(internal.getStory().get(0));
    
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

class myPanel extends JPanel {

    Font font;
    private static final Hashtable<TextAttribute, Object> map = new Hashtable<>();

    static {
        map.put(TextAttribute.FAMILY, "Consolas");
        map.put(TextAttribute.SIZE, new Float(30.0));
    }

    public myPanel() {
        super();
        super.setBackground(Color.BLACK);
    }

    private final int drawPosX = 200;
    private final int breakWidth = 1080 - drawPosX;
    private final int startDrawPosY = 70;
    private final int breakHeight = 620;

    public void paintComponent(String text, BufferStrategy bufferStrat, BufferedImage background, BufferedImage textBox) {
        Graphics2D graphics = (Graphics2D) bufferStrat.getDrawGraphics();
        super.paintComponent(graphics);

        paintBackgroundAndBoxes(graphics, background, textBox);

        configureFont(graphics);
        //AttributedString textAtt = new AttributedString(text, map);
        String remains = drawFormattedStringAndReturnRemains(text, graphics);

        //graphics.drawString(text, 50, 300);
        graphics.dispose();
        bufferStrat.show();

        if (!remains.isEmpty()) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
            }
            System.exit(0);
        }
    }

    private String drawFormattedStringAndReturnRemains(String stringText, Graphics2D graphics) {
        AttributedString text = new AttributedString(stringText, map);

        AttributedCharacterIterator paragraph = text.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = graphics.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        float drawPosY = startDrawPosY;

        // Set position to the index of the first
        // character in the paragraph.
        lineMeasurer.setPosition(paragraphStart);

        // Get lines from until the entire paragraph
        // has been displayed.
        while (lineMeasurer.getPosition() < paragraphEnd) {

            TextLayout layout = lineMeasurer.nextLayout(breakWidth);

            // Move y-coordinate by the ascent of the layout
            drawPosY += layout.getAscent();

            // Draw the TextLayout at (drawPosX,drawPosY).
            System.out.println(drawPosX + " / " + drawPosY);
            layout.draw(graphics, drawPosX, drawPosY);

            // Move y-coordinate in preparation for next
            // layout.
            drawPosY += layout.getDescent() + layout.getLeading();

            if (drawPosY > breakHeight) {
                String remainingText = stringText.substring(lineMeasurer.getPosition());
                System.out.println(remainingText);
                return remainingText;
            }
        }
        return "";
    }

    private void paintBackgroundAndBoxes(Graphics2D graphics, BufferedImage background, BufferedImage textBox) {
        graphics.drawImage(background, 0, 0, null);
        graphics.drawImage(textBox, 500, 0, null);
    }

    private void configureFont(Graphics2D graphics) {
        graphics.setFont(font);
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
    }

    private int getTextSize(String text, Graphics2D graphics) {
        FontMetrics metrics = graphics.getFontMetrics(font);

        return metrics.stringWidth(text) + 2;
    }
}
