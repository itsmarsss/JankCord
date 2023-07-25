package jankcord.profiles;

import javax.swing.JLabel;

import jankcord.Jankcord;
import jankcord.tools.ResourceLoader;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HomeProfile extends JLabel {
	public HomeProfile() {
		setIcon(ResourceLoader.loader.getHomeProfileIcon());
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Jankcord.getChatBoxArea().setChannelName("~ Select a channel.");
				Jankcord.setOtherID("");
				Jankcord.setNewOtherID(true);
				Jankcord.setInServer(false);
				Jankcord.setInServerCheck(true);
				Jankcord.getChatBoxArea().resetMessages();
				Jankcord.queryForNewMessages();
				Jankcord.getChannelList().initChannelPanel();
				Jankcord.getChannelList().resetDisplays();

				Jankcord.queryForNewFriend();
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
	}
}
