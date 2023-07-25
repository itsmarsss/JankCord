package jankcord.components.JankList;

import jankcord.objects.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// JankCord's list, child of JList
public class JankList extends JList {
    // Store a version of the previous list before an action
    private int[] prev;

    // Constructor, with list of objects, label to update, and update template
    public JankList(DefaultListModel<?> list, JLabel updateLabel, String template) {
        // Super; set list object
        super(list);

        // Set Jlist properties
        setBackground(new Color(43, 45, 49));
        setForeground(new Color(219, 222, 225));
        setFont(new Font("Whitney", Font.BOLD, 30));
        setSelectionBackground(new Color(71, 82, 196));
        setSelectionForeground(new Color(255, 255, 255));
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add mouse listener; action when list item is being selected
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if shift is down
                if (e.isShiftDown()) {
                    // Shift based multiselect is unsupported
                    // Undo what user did
                    setSelectedIndices(prev);
                }

                // Check if 20 members has been reached including current user, max group chat size is 20
                if (getSelectedIndices().length <= 19) {
                    // Update label, inform user of available slots
                    updateLabel.setText(String.format(template, (19 - getSelectedIndices().length)));
                    // Update previous selection values
                    prev = getSelectedIndices();
                } else { // Otherwise
                    // Undo what user did
                    setSelectedIndices(prev);
                }
            }
        });
    }

    // Getter for prev
    public int[] getPrev() {
        return prev;
    }

    // Setter for prev
    public void setPrev(int[] prev) {
        this.prev = prev;
    }
}
