import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotoComponentModel {
    // a boolean to represent the state of the photo (flipped or not)
    private boolean isFlipped = false;
    // the Image contained in the photo component
    private Image image;
    // the list of annotation of the component
    private List<Annotation> annotationList = new ArrayList<>();
    // the current Stroke Annotation in progress
    private StrokeAnnotation currentStrokeAnnotation;
    // the current Textual Annotation in progress
    private TextAnnotation currentTextAnnotation;
    // the current selected color
    private Color currentColor;
    // the current selected stroke width
    private int currentStroke;
    // the current selected Font
    private Font currentFont;

    public PhotoComponentModel(Image image) {
        this.image = image;
        this.currentColor = Color.BLACK;
        this.currentStroke = 1;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public int getCurrentStroke() {
        return currentStroke;
    }

    public void setCurrentStroke(int currentStroke) {
        this.currentStroke = currentStroke;
    }

    public Font getCurrentFont() {
        return currentFont;
    }

    public void setCurrentFont(Font currentFont) {
        this.currentFont = currentFont;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }

    public List<Annotation> getAnnotationList() {
        return annotationList;
    }
    public void addAnnotation(Annotation annotation){
        annotationList.add(annotation);
    }

    public StrokeAnnotation getCurrentStrokeAnnotation() {
        return currentStrokeAnnotation;
    }

    public void setCurrentStrokeAnnotation(StrokeAnnotation currentStrokeAnnotation) {
        this.currentStrokeAnnotation = currentStrokeAnnotation;
    }

    public TextAnnotation getCurrentTextAnnotation() {
        return currentTextAnnotation;
    }

    public void setCurrentTextAnnotation(TextAnnotation currentTextAnnotation) {
        this.currentTextAnnotation = currentTextAnnotation;
    }

    public void setAnnotationList(List<Annotation> annotationList) {
        this.annotationList = annotationList;
    }
}
