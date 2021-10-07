import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class PhotoComponentUI {
    // The image to draw
    private Image image;
    // the width of the image to draw
    private int image_width;
    // the height of the image to draw
    private int image_height;


    public void paint(Graphics2D pen, PhotoComponent photo) {

        // Setting rendering Hints
        pen.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        pen.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        pen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // getting needed variables
        PhotoComponentModel model = photo.getModel();
        image = model.getImage();
        image_width = image.getWidth(photo);
        image_height = image.getHeight(photo);

        //Drawing image frame and background
        drawImageFrame(pen, photo);

        if(model.isFlipped()){ // Flipped
            pen.setColor(Color.WHITE);

            // draw white space
            pen.fillRect(Utils.FRAME_BORDER, Utils.FRAME_BORDER, image_width, image_height);

            // draw annotations
            List<Annotation> annotationList = model.getAnnotationList();
            for (Annotation annotation : annotationList) {
                pen.setColor(model.getCurrentColor());
                annotation.draw(pen, Utils.FRAME_BORDER, Utils.FRAME_BORDER, image_width, image_height);
            }
        }
        else { //not Flipped
            // Display the regular image
            pen.drawImage(image, Utils.FRAME_BORDER,  Utils.FRAME_BORDER, image_width, image_height, photo);
        }
    }

    /**
    * This method draws the frame of the image
     * */
    public void drawImageFrame(Graphics2D pen, PhotoComponent photo){
        //1. Background
        pen.setColor(Color.decode("#0d254d"));
        pen.fillRect(0, 0, photo.getWidth(), photo.getHeight());

        //2. PictureFrame
        pen.setColor(Color.LIGHT_GRAY);
        pen.fillRect(0, 0, image_width + (Utils.FRAME_BORDER * 2), image_height + (Utils.FRAME_BORDER * 2));
    }
}
