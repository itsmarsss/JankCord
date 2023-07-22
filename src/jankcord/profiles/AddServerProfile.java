package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.tools.ResourceLoader;

public class AddServerProfile extends JLabel {
	public AddServerProfile() {
		setIcon(ResourceLoader.loader.getAddProfileIcon());
	}
}
