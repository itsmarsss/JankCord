package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.ResourceLoader;

public class HomeProfile extends JLabel {
	public HomeProfile() {
		setIcon(ResourceLoader.loader.getHomeProfileIcon());
	}
}
