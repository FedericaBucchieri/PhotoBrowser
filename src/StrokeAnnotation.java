import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the free-hand drawing annotation that a user can make when a PhotoComponent is flipped
 */
public class StrokeAnnotation implements Annotation, Serializable{
    // The set of point that compose the annotation
    private List<Point> points = new ArrayList<>();
    // The color of the annotation
    private Color color;
    // the Stroke width of the annotation
    private int stroke;

    public StrokeAnnotation(Color color, int stroke) {
        this.color = color;
        this.stroke = stroke;
    }

    public void addPoint(Point point){
        points.add(point);
    }

    @Override
    public void draw(Graphics2D g, int startX, int startY, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.setStroke(new BasicStroke(stroke));


        if (points.size() > 1) {
            for (int i = 0; i < points.size() - 1; i++) {
                Point startPoint = points.get(i);
                Point endPoint = points.get(i+1);

                // Boundaries check
                if(startPoint.x > startX && startPoint.x < startX + width &&
                    startPoint.y > startY && startPoint.y < startY + height &&
                    endPoint.x > startX && endPoint.x < startX + width &&
                    endPoint.y > startY && endPoint.y < startY + height)
                g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
            }
        }
    }

}
