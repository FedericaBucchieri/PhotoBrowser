import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;

public class PhotoComponent extends JComponent {
    // The model of the PhotoComponent
    private PhotoComponentModel model;
    // The view of the PhotoComponent
    private PhotoComponentUI ui;
    // the width of the PhotoComponent
    private int width;
    // the height of the PhotoComponent
    private int height;
    // The image displayed
    private Image image;
    // the imag url to be displayed
    private String imgUrl;


    public PhotoComponent(File imageFile) {
        // creating Image
        try {
            image = ImageIO.read(imageFile);
            imgUrl = imageFile.getPath();
        } catch (IOException e){
            e.printStackTrace();
        }

        this.model = new PhotoComponentModel(image);

        // Setting sizes
        width = model.getImage().getWidth(this) + Utils.FRAME_BORDER * 2;
        height = model.getImage().getHeight(this) + Utils.FRAME_BORDER * 2;

        this.ui = new PhotoComponentUI();

        // Adding interactions listeners
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    model.setFlipped(!model.isFlipped());
                    repaint();
                }
                else if (e.getClickCount() == 1 && model.isFlipped()) {
                    setFocusable(true);
                    requestFocusInWindow();
                    TextAnnotation textAnnotation = new TextAnnotation(e.getPoint().x, e.getPoint().y, model.getCurrentColor(), model.getCurrentFont());
                    model.setCurrentTextAnnotation(textAnnotation);
                    model.addAnnotation(textAnnotation);
                }
            }
        });


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(model.isFlipped()) {
                    TextAnnotation textAnnotation = model.getCurrentTextAnnotation();
                    textAnnotation.addCharacter(String.valueOf(e.getKeyChar()));
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && model.isFlipped()) {
                    model.getCurrentTextAnnotation().removeCharacter();
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(model.isFlipped()) {
                    if (model.getCurrentStrokeAnnotation() == null) {
                        StrokeAnnotation annotation = new StrokeAnnotation(model.getCurrentColor(),  model.getCurrentStroke());
                        model.setCurrentStrokeAnnotation(annotation);
                        model.addAnnotation(annotation);
                    }
                    model.getCurrentStrokeAnnotation().addPoint(e.getPoint());
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                model.setCurrentStrokeAnnotation(null);
            }
        });

    }

    public void paintComponent(Graphics pen) {
        this.ui.paint((Graphics2D)pen, this);
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setAnnotationColor(Color color){
        model.setCurrentColor(color);
    }

    public void setAnnotationStroke(int stroke){
        model.setCurrentStroke(stroke);
    }

    public void setAnnotationFont(Font font){
        model.setCurrentFont(font);
    }

    public PhotoComponentModel getModel() {
        return model;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Dimension getPreferredSize() {
        return new Dimension(getWidth(), getHeight());
    }
    public Dimension getSize(){ return new Dimension(getWidth(), getHeight()); }
}
