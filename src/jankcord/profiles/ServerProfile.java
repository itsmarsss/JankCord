package jankcord.profiles;

import java.awt.Image;
import java.io.Serial;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ServerProfile extends JLabel {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String serverID;
	public ServerProfile(Image serverIcon, String serverID) {
		Image scaledIcon = serverIcon.getScaledInstance(106, 106, Image.SCALE_DEFAULT);
		setSize(96, 96);
		setIcon(new ImageIcon(scaledIcon));
		
		setServerID(serverID);
	}
	public String getServerID() {
		return serverID;
	}
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
}
