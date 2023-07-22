package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.ResourceLoader;

import java.io.Serial;

public class ExploreProfile extends JLabel {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	public ExploreProfile() {
		setIcon(ResourceLoader.loader.getExploreProfileIcon());
	}
}
