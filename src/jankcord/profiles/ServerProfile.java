package jankcord.profiles;

import jankcord.Jankcord;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ServerProfile extends JLabel {
	private final String serverID;
	public ServerProfile(Image serverIcon, String serverID) {
		Image scaledIcon = serverIcon.getScaledInstance(106, 106, Image.SCALE_DEFAULT);
		setSize(96, 96);
		setIcon(new ImageIcon(scaledIcon));
		
		this.serverID = serverID;

		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!Jankcord.getOtherID().equals(serverID)) {
					Jankcord.setOtherID(serverID);
					Jankcord.setNewOtherID(true);
					Jankcord.setInServer(true);

					Jankcord.getChatBoxArea().resetMessages();
					Jankcord.queryForNewMessages();
					Jankcord.getChannelList().clear();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
	}
	public String getServerID() {
		return serverID;
	}
}
