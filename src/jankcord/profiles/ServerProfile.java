package jankcord.profiles;

import jankcord.Jankcord;
import jankcord.objects.GroupChat;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ServerProfile extends JLabel {
	private GroupChat groupChat;
	public ServerProfile(Image serverIcon, GroupChat groupChat) {
		Image scaledIcon = serverIcon.getScaledInstance(106, 106, Image.SCALE_FAST);
		setSize(96, 96);
		setIcon(new ImageIcon(scaledIcon));
		
		this.groupChat = groupChat;

		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!Jankcord.getOtherID().equals(groupChat.getId())) {
					Jankcord.setOtherID(groupChat.getId());
					Jankcord.setNewOtherID(true);
					Jankcord.setInServer(true);

					Jankcord.getChatBoxArea().resetMessages();
					Jankcord.queryForNewMessages();
					Jankcord.getChannelList().clear();

					Jankcord.getChatBoxArea().setChannelName(groupChat.getChatName());
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

	public GroupChat getGroupChat() {
		return groupChat;
	}

	public void setGroupChat(GroupChat groupChat) {
		this.groupChat = groupChat;
	}
}
