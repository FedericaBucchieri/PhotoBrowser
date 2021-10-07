import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the textual annotation that a user can make when a PhotoComponent is flipped
 */
public class TextAnnotation implements Annotation, Serializable{
    // the text of the annotation
    private String text;
    // the x coordinate of the starting point of the first line of the text
    private final int x;
    // the y coordinate of the starting point of the first line of the text
    private final int y;
    // the text color
    private final Color color;
    // the text font
    private Font font;

    public TextAnnotation(int x, int y, Color color, Font font) {
        this.x = x;
        this.y = y;
        this.text = "";
        this.color = color;
        this.font = font;
    }

    public void addCharacter(String c) {
        text = text.concat(c);
    }

    /**
     * This method is used to edit a text annotation. Created to allow the user to use the backspace
     */
    public void removeCharacter(){
        if(text.length() > 1) {
            if(text.charAt(text.length()-2) == '\n')
                text = text.substring(0, text.length() - 3);
            else
                text = text.substring(0, text.length() - 2);
        }
        else
            text = "";
    }

    @Override
    public void draw(Graphics2D g, int startX, int startY, int width, int height) {
        int lastSpaceIndex = 0;
        int lastBackSlash = 0;
        String[] splitted = text.split("\n");

        g.setColor(color);
        g.setFont(font);

        // Checking where is the last blank space
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ')
                lastSpaceIndex = i;
        }

        // checking where is the last backSlash
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n')
                lastBackSlash = i;
        }

        // calculating the current width of the last line
        int currentWidth = g.getFontMetrics().stringWidth(splitted[splitted.length-1]);

        // boundaries check
        if((x + currentWidth) >= (startX + width)){
            if(lastSpaceIndex != 0 && lastBackSlash < lastSpaceIndex)
                text = text.substring(0, lastSpaceIndex) + "\n" + text.substring(lastSpaceIndex + 1);
            else
                text = text + "\n";
        }

        for(int i = 0, newLineY = y; i < splitted.length; i++, newLineY+= g.getFontMetrics().getHeight()){
            if(newLineY < startY + height)
                g.drawString(splitted[i], x, newLineY);
        }
    }


}
