package jankcord_admin;

import javax.swing.*;

public class frmControlPanel extends JFrame {
    private JLabel lblAddNewStudent;
    private JTextField textField1;
    private JTextArea txtListOfStudents;
    private JButton updateListButton;
    private JPanel mainPanel;
    private JComboBox comboBox1;
    private JButton startButton;
    private JButton stopButton;
    private JLabel lblSeverStatus;

    public frmControlPanel(){
        setContentPane(mainPanel);
        setTitle("Control Panel");
        setSize(550, 250);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args){
        frmControlPanel myFrame = new frmControlPanel();
    }
}
