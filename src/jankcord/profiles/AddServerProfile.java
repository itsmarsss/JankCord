package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.ResourceLoader;

import java.io.Serial;

public class AddServerProfile extends JLabel {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	public AddServerProfile() {
		setIcon(ResourceLoader.loader.getAddProfileIcon());
	}
}
