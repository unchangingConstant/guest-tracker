package dataviews;

import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import backend.Visit.VisitKey;
import modelviewtools.DataDelegate;

/**
 * This will be added to the OngoingVisitsPanel to display one ongoing visit
 * 
 * @author Ethan Begley
 * @version 1/10/2025
 */
public class OngoingVisit extends Panel {

    private EndButton endButton; // Button that ends the visit
    private DataDelegate model; // Database the button will access
    private VisitKey visitKey;

    /**
     * These contraints will be used throughout the class to arrange the
     * OngoingVisit display's components
     */
    private final GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Creates an OngoingVisit display with a visit key and the data model the
     * app is using
     * 
     * @param visitKey
     *            The key of the visit you want to display
     * @param model
     *            The data model this app is using to track visits
     */
    public OngoingVisit(VisitKey visitKey, DataDelegate model) {
        this.model = model;
        this.visitKey = visitKey;
        this.endButton = new EndButton(visitKey, model);

        setLayout(new GridBagLayout());

        add(new Label(model.visitorName(visitKey.visitorID())
            + "is currently visiting"));

        add(endButton);

    }


    public VisitKey visitKey() {
        return visitKey;
    }

    /**
     * Button to end the visit this OngoingVisit display tracks
     */
    @SuppressWarnings("serial")
    private class EndButton extends Button implements ActionListener {

        private VisitKey visitKey; // Visit key of the visit this button can end
        private DataDelegate model; // Data model the app is current using

        /**
         * Constructs a button that can end one visit
         * 
         * @param visitKey
         *            Key of the visit you want this button to be able to end
         */
        public EndButton(VisitKey visitKey, DataDelegate model) {
            super("End");
            addActionListener(this);
        }


        /**
         * When button is clicked, ends the ongoing visit belonging to the
         * student specified by visitorID()
         */
        public void actionPerformed(ActionEvent e) {
            try {
                model.endVisit(visitKey.visitorID());
            }
            catch (Exception e1) {
                System.exit(0);
            }
        }

    }

}
