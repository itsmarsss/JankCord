package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.tools.ResourceLoader;

// AddServerProfile, child of JLabel; this class is for show; returns an image; this is not mandatory
public class AddServerProfile extends JLabel {
	public AddServerProfile() {
		setIcon(ResourceLoader.loader.getAddProfileIcon());
	}
}
