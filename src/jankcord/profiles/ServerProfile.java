package jankcord.profiles;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ServerProfile extends JLabel {
	private final String serverID;
	public ServerProfile(Image serverIcon, String serverID) {
		Image scaledIcon = serverIcon.getScaledInstance(106, 106, Image.SCALE_DEFAULT);
		setSize(96, 96);
		setIcon(new ImageIcon(scaledIcon));
		
		this.serverID = serverID;
	}
	public String getServerID() {
		return serverID;
	}
}
