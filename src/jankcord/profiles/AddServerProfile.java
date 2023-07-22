package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.ResourceLoader;

public class AddServerProfile extends JLabel {
	public AddServerProfile() {
		setIcon(ResourceLoader.loader.getAddProfileIcon());
	}
}
