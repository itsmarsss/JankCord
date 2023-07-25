package jankcord.texthelpers;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

// Helps make text components undo-/redo-able
public class UndoRedo {
    // Public undo redo fields
    public final static String UNDO_ACTION = "Undo";
    public final static String REDO_ACTION = "Redo";

    /**
     * Makes a text component undo-/redo-able
     *
     * @param pTextComponent text component to alter
     */
    public static void makeUndoable(JTextComponent pTextComponent) {
        // Declare and create an undo manager
        final UndoManager undoMgr = new UndoManager();

        // Add undoable edit lister; lambda
        pTextComponent.getDocument().addUndoableEditListener((evt) -> undoMgr.addEdit(evt.getEdit()));

        // Put undo action with abstract action into text component's action map
        pTextComponent.getActionMap().put(UNDO_ACTION, new AbstractAction(UNDO_ACTION) {
            public void actionPerformed(ActionEvent evt) {
                // Attempt undo
                try {
                    // Undo 5 times
                    for (int i = 0; i < 5; i++) {
                        // If undoable
                        if (undoMgr.canUndo()) {
                            // Undo
                            undoMgr.undo();
                        }
                    }
                } catch (CannotUndoException e) {
                }
            }
        });

        // Put redo action with abstract action into text component's action map
        pTextComponent.getActionMap().put(REDO_ACTION, new AbstractAction(REDO_ACTION) {
            public void actionPerformed(ActionEvent evt) {
                // Attempt redoable
                try {
                    // Redo 5 times
                    for (int i = 0; i < 5; i++) {
                        // If redoable
                        if (undoMgr.canRedo()) {
                            // Redo
                            undoMgr.redo();
                        }
                    }
                } catch (CannotRedoException e) {
                }
            }
        });

        // Add undo key triggers (ctrl + z)
        pTextComponent.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), UNDO_ACTION);
        // Add redo key triggers (ctrl + y)
        pTextComponent.getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), REDO_ACTION);
    }
}
