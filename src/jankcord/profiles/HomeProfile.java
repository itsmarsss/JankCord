package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.ResourceLoader;

public class HomeProfile extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HomeProfile() {
		setIcon(ResourceLoader.loader.getHomeProfileIcon());
	}
}
