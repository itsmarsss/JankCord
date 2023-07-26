package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.tools.ResourceLoader;

import java.awt.*;

// AddServerProfile, child of JLabel; this class is for show; returns an image; this is not mandatory
public class AddServerProfile extends JLabel {
	// Constructor
    public AddServerProfile() {
        setIcon(ResourceLoader.loader.getAddProfileIcon());
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
