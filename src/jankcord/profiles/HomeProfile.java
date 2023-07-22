package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.ResourceLoader;

import java.io.Serial;

public class HomeProfile extends JLabel {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	public HomeProfile() {
		setIcon(ResourceLoader.loader.getHomeProfileIcon());
	}
}
