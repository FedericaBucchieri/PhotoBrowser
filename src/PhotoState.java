import java.io.Serializable;
import java.util.List;

/**
 * This class represents the State of each PhotoComponent. It is used to store the state of a PhotoComponent inside a file thanks to Serialization.
 */
public class PhotoState implements Serializable {
    // the url of the photo displayed
    private String imgUrl;
    // the list of annotation already made on the PhotoComponent
    private List<Annotation> annotationList;

    public PhotoState(String imgUrl, List<Annotation> annotationList) {
        this.imgUrl = imgUrl;
        this.annotationList = annotationList;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public List<Annotation> getAnnotationList() {
        return annotationList;
    }
}
