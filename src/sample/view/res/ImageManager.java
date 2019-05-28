package sample.view.res;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageManager {
    public static final String ICON_ADD = Main.FOLDER_IMAGES + "add.png";
    public static final String ICON_REMOVE = Main.FOLDER_IMAGES + "remove.png";
    public static final String ICON_INFO = Main.FOLDER_IMAGES + "information.png";
    public static final String ICON_PLACEHOLDER = Main.FOLDER_IMAGES + "question-sign.png";
    public static final String ICON_UP = Main.FOLDER_IMAGES + "arrow-up.png";
    public static final String ICON_DOWN = Main.FOLDER_IMAGES + "arrow-down.png";
    public static final String ICON_COPY = Main.FOLDER_IMAGES + "copy.png";
    public static final String ICON_REFRESH = Main.FOLDER_IMAGES + "refresh.png";
    public static final String ICON_SHARE = Main.FOLDER_IMAGES + "share.png";
    public static final String ICON_EDIT = Main.FOLDER_IMAGES + "edition.png";
    public static final String ICON_UPLOAD = Main.FOLDER_IMAGES + "upload.png";
    public static final String ICON_DOUBLE_UP = Main.FOLDER_IMAGES + "double-up.png";
    public static final String ICON_DOUBLE_DOWN = Main.FOLDER_IMAGES + "double-down.png";
    public static final String ICON_RECT_CLOSE = Main.FOLDER_IMAGES + "rect-close.png";
    public static final String ICON_EYE = Main.FOLDER_IMAGES + "eye.png";


    private static final String ICONS_PATH = "./icones";

    private HashMap<String, Image> cache = new HashMap<>();
    private ArrayList<String> userImages = new ArrayList<>();

    public ImageManager() {
        File f = new File(ICONS_PATH);
        if (!f.exists()) {
            if (!f.mkdir()) System.err.println("mkdir failed! check write perm");
            return;
        }

        ArrayList<String> arrayList = Utils.getFileInFolder(ICONS_PATH, ".png");
        userImages.addAll(arrayList);
    }

    public Image getImage(String location) {
        return getImage(location, false);
    }

    public Image getImage(String location, boolean resource) {
        Image i = cache.get(location);
        //System.out.println("TEST: " + getClass());
        //System.out.println("TEST: " + getClass().getResourceAsStream(location));

        if (i == null) {
            try {
                File f;
                if (!resource) {
                    f = new File(location);
                    if (f.exists() && f.isFile()) {
                        i = new Image(f.toURI().toURL().toExternalForm());
                        if (i.getHeight() > 512 || i.getWidth() > 512) i = null;
                        userImages.add(f.getAbsolutePath());
                    }
                } else {
                    i = new Image(getClass().getResourceAsStream(location));
                }

                if (i != null)
                    cache.put(location, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return i;
    }

    public ImageView constructImageViewFrom(String location, int width, int height) {
        return constructImageViewFrom(location, width, height, false);
    }

    public ImageView constructImageViewFrom(String location, int width, int height, boolean resource) {
        Image i = getImage(location, resource);
        ImageView iv = new ImageView(i);
        iv.setFitWidth(width);
        iv.setFitHeight(height);
        return iv;
    }

    public ArrayList<String> getUserImages() {
        return userImages;
    }
}
