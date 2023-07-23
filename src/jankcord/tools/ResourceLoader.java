package jankcord.tools;

import javax.swing.ImageIcon;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ResourceLoader {
    public static final ResourceLoader loader = new ResourceLoader();

    // ServerList Icons
    private final ImageIcon homeProfileIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/home.png"));
    private final ImageIcon addProfileIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/add.png"));
    private final ImageIcon exploreProfileIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/explore.png"));
    private final ImageIcon tempProfileIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/temp.png"));

    // FriendList Icons
    private final ImageIcon friendProfileIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/friend.png"));

    // Window Icons
    private final ImageIcon icon1 = new ImageIcon(getClass().getClassLoader().getResource("resources/icons/icon1.png"));
    private final ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("resources/icons/icon2.png"));
    private final ImageIcon icon3 = new ImageIcon(getClass().getClassLoader().getResource("resources/icons/icon3.png"));
    private final ImageIcon icon4 = new ImageIcon(getClass().getClassLoader().getResource("resources/icons/icon4.png"));

    private final ArrayList<Image> icons = new ArrayList<>(Arrays.asList(
            icon1.getImage(),
            icon2.getImage(),
            icon3.getImage(),
            icon4.getImage()));

    // Getters
    public ImageIcon getTempProfileIcon() {
        return tempProfileIcon;
    }

    public ImageIcon getHomeProfileIcon() {
        return homeProfileIcon;
    }

    public ImageIcon getAddProfileIcon() {
        return addProfileIcon;
    }

    public ImageIcon getExploreProfileIcon() {
        return exploreProfileIcon;
    }

    public ImageIcon getFriendProfileIcon() {
        return friendProfileIcon;
    }

    public ImageIcon getIcon1() {
        return icon1;
    }

    public ImageIcon getIcon2() {
        return icon2;
    }

    public ImageIcon getIcon3() {
        return icon3;
    }

    public ImageIcon getIcon4() {
        return icon4;
    }

    public ArrayList<Image> getIcons() {
        return icons;
    }
}
