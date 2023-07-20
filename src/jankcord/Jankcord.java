package jankcord;

// Imports
// AWT
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Swing
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

// Components
import jankcord.components.ChannelList;
import jankcord.components.ChatBoxArea;
import jankcord.components.ServerList;
import jankcord.components.WindowButtons;
import jankcord.newclasses.ResourceLoader;
import jankcord.newclasses.ScrollBarUI;
import jankcord.objects.Message;
import jankcord.objects.User;
import jankcord.profiles.MessageProfile;

// JankCord Class
public class Jankcord {
	// Main Function
	public static void main(String[] args) {
		System.setProperty("sun.java2d.uiScale", "1");

		// new Login(); // should return selfuser upon success

		User selfuser = new User(1, "Marsss", ".");

		new Jankcord(selfuser);
	}

	// Frame dragging
	private int posX = 0, posY = 0;
	private boolean drag = false;

	// Non-fullscreen dimensions and toggle
	private static int oldW;
	private static int oldH;
	private static int oldX;
	private static int oldY;
	private static boolean full = false;

	// Main frame and components
	private static JFrame frame;					// Window holding everything
	private static JPanel viewPanel;
	private static WindowButtons windowButtons;		// Other components...
	private static ServerList serverList;
	private static ChannelList channelList;
	private static ChatBoxArea chatBoxArea;

	private String otherID;
	private User selfUser;

	// JankCord Default Constructor
	public Jankcord(User selfUser) {
		this.selfUser = selfUser;

		// Init
		frame = new JFrame("JankCord");
		viewPanel = new JPanel();
		drawUI();
	}

	// render frame and viewPanel
	private void drawUI() {
		// Frame Icon
		List<Image>icons = new ArrayList<Image>();
		icons.add(new ImageIcon("src/resources/Icon1.png").getImage());
		icons.add(new ImageIcon("src/resources/Icon2.png").getImage());
		icons.add(new ImageIcon("src/resources/Icon3.png").getImage());
		icons.add(new ImageIcon("src/resources/Icon4.png").getImage());
		frame.setIconImages(icons);

		// Frame Init
		frame.setUndecorated(true);
		frame.getContentPane().setLayout(null);
		frame.setMinimumSize(new Dimension(1880, 1000));
		frame.getContentPane().setBackground(new Color(32, 34, 37));
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int)(screenDim.getWidth()/1.5), (int)(screenDim.getHeight()/1.5));
		frame.setLocation((int)screenDim.getWidth()/2-frame.getWidth()/2, (int)screenDim.getHeight()/2-frame.getHeight()/2);

		// Entire View

		// View Init
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(frame);
		cr.setSnapSize(new Dimension(1, 1));
		cr.setMinimumSize(new Dimension(1880, 1000));
		cr.setMaximumSize(screenDim);

		viewPanel.setLayout(null);
		viewPanel.setLocation(5, 5);
		viewPanel.setBackground(new Color(32, 34, 37));
		viewPanel.setSize(frame.getWidth()-10, frame.getHeight()-10);

		// Drag 'n' Drop
		viewPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(e.getY() < 50 && e.getY() > 10) {
					drag = true;
					posX = e.getX();
					posY = e.getY();
				} else {
					drag = false;
				}
			}
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					doFullscreen();
				} 
			}
		});
		viewPanel.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(drag) {
					if(full) {
						frame.setSize(oldW, oldH);
						frame.setLocation(oldX, oldY);
						full = false;
						resize();
					}
					frame.setLocation(e.getXOnScreen()-posX, e.getYOnScreen()-posY);
				}
			}
		});

		frame.getContentPane().add(viewPanel);

		// Logo
		JLabel logoLabel = new JLabel("JankCord");
		logoLabel.setName("JankCordLogo");
		logoLabel.setSize(130, 30);
		logoLabel.setLocation(18, 10);
		logoLabel.setForeground(new Color(114, 118, 125));
		logoLabel.setFont(new Font("Whitney", Font.BOLD, 28));

		viewPanel.add(logoLabel);

		// Add Other Components
		windowButtons = new WindowButtons();
		serverList = new ServerList();
		channelList = new ChannelList();
		chatBoxArea = new ChatBoxArea();

		viewPanel.add(windowButtons);
		viewPanel.add(serverList);
		viewPanel.add(channelList);
		viewPanel.add(chatBoxArea);

		for(Component i : viewPanel.getComponents())
			System.out.println(i.getName());

		// Friend list panel
		JScrollPane friendsScrollPane = new JScrollPane();
		friendsScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(new Color(47, 49, 54), new Color(32, 34, 37), true));

		// Channel list panel
		JScrollPane channelScrollPane = new JScrollPane();
		channelScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI(new Color(47, 49, 54), new Color(32, 34, 37), true));

		frame.setVisible(true);

		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(Jankcord::queryForNewMessage, 0, 1, TimeUnit.SECONDS);
	}

	private static void queryForNewMessage() {
		// Query api endpoint

		ArrayList<Message> messages = new ArrayList<>();

		messages.add(new Message(new User(1, "Joe", "."), "content", 12039));

		for(int i = 0; i < messages.size(); i++) {
			chatBoxArea.addMessage(messages.get(i), i);
		}


		ArrayList<User> friends = new ArrayList<>();

		friends.add(new User(1, "Bob", "https://i.imgur.com/B5XcGDg.jpeg"));
		friends.add(new User(1, "Bob1", "."));
		friends.add(new User(1, "Bob2", "."));
		friends.add(new User(1, "Bob3", "."));
		friends.add(new User(1, "Bob4", "."));
		friends.add(new User(1, "Bob5", "."));
		friends.add(new User(1, "Bob6", "."));
		friends.add(new User(1, "Bob7", "."));
		friends.add(new User(1, "Bob8", "."));
		friends.add(new User(1, "Bob9", "."));

		for(int i = 0 ; i < friends.size(); i++) {
			channelList.addChannel(friends.get(i), i+2);
		}


		ArrayList<User> members = new ArrayList<>();

		members.add(new User(1, "Bob8", "https://i.imgur.com/B5XcGDg.jpeg"));
		members.add(new User(1, "Bob9", "."));

		for(int i = 0 ; i < members.size(); i++) {
			chatBoxArea.addMember(members.get(i), i);
		}
	}

	public static void doFullscreen() {
		if(full) {
			frame.setSize(oldW, oldH);
			frame.setLocation(oldX, oldY);
			full = false;
		} else {
			oldW = frame.getWidth();
			oldH = frame.getHeight();
			oldX = frame.getX();
			oldY = frame.getY();
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			full = true;
		}
		resize();
	}

	public static void resize() {
		viewPanel.setLocation(5, 5);
		viewPanel.setSize(frame.getWidth()-10, frame.getHeight()-10);

		windowButtons.setLocation(Jankcord.viewPanel.getWidth()-186, 0);

		serverList.setSize(serverList.getWidth(), Jankcord.viewPanel.getHeight()-50);

		channelList.setSize(channelList.getWidth(), Jankcord.viewPanel.getHeight()-50);
		channelList.getChannelScrollPane().setSize(477, channelList.getHeight()-110);

		chatBoxArea.setSize(Jankcord.getViewPanel().getWidth()-646, Jankcord.getViewPanel().getHeight()-50);
		chatBoxArea.getChatBoxTopBarPanel().setSize(chatBoxArea.getWidth(), 106);
		chatBoxArea.getChatBoxScrollPane().setSize(chatBoxArea.getWidth()-540, chatBoxArea.getHeight()-256);
		
		chatBoxArea.getTypePanel().setSize(chatBoxArea.getChatBoxScrollPane().getWidth()-60, 100);
		chatBoxArea.getTypePanel().setLocation(30, chatBoxArea.getHeight()-125);
		chatBoxArea.getTypeScrollPane().setSize(chatBoxArea.getTypePanel().getWidth()-20, chatBoxArea.getTypePanel().getHeight()-16);
		chatBoxArea.reline();
		
		chatBoxArea.getMembersScrollPane().setSize(540, chatBoxArea.getHeight()-106);
		chatBoxArea.getMembersScrollPane().setLocation(chatBoxArea.getChatBoxScrollPane().getWidth()+5, 106);
		for(MessageProfile mp : chatBoxArea.getMessageProfiles()) {
			mp.setPreferredSize(new Dimension(Jankcord.getViewPanel().getWidth()-646, 100));
		}
	}

	public static JPanel getViewPanel() {
		return viewPanel;
	}

	public static JFrame getFrame() {
		return frame;
	}

	public User getSelfUser() {
		return selfUser;
	}

	public void setSelfUser(User selfUser) {
		this.selfUser = selfUser;
	}
}
