package jankcord.components.JankList;

import jankcord.objects.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class JankList extends JList {
    private int[] prev;

    public JankList(DefaultListModel<?> list, JLabel update) {
        super(list);

        setBackground(new Color(43, 45, 49));
        setForeground(new Color(219, 222, 225));
        setFont(new Font("Whitney", Font.BOLD, 30));
        setSelectionBackground(new Color(71, 82, 196));
        setSelectionForeground(new Color(255, 255, 255));
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isShiftDown()) {
                    setSelectedIndices(prev);
                    System.out.println("ye");
                }

                if (getSelectedIndices().length <= 10) {
                    update.setText("You can add " + (10 - getSelectedIndices().length) + " more friends.");
                    prev = getSelectedIndices();
                } else {
                    setSelectedIndices(prev);
                }

            }
        });
    }

    public int[] getPrev() {
        return prev;
    }

    public void setPrev(int[] prev) {
        this.prev = prev;
    }
}
