package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.ResourceLoader;

public class ExploreProfile extends JLabel {
	public ExploreProfile() {
		setIcon(ResourceLoader.loader.getExploreProfileIcon());
	}
}
