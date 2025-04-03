package dataviews;

import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelviewtools.DataDelegate;
import modelviewtools.DataView;

/**
 * This will be added to the OngoingVisitsPanel to display one ongoing visit
 * 
 * @author Ethan Begley
 * @version 1/10/2025
 */
public class OngoingVisit extends DataView {

    private EndButton endButton; // Button that ends the visit
    private DataDelegate model; // Database the button will access
    // This Panel will hold all the sub-components for this view
    private Panel ongoingVisit;

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
    public OngoingVisit(DataDelegate model, int visitorID) {
        this.model = model;
        this.endButton = new EndButton(model);

        setLayout(new GridBagLayout());

        add(endButton);

    }

    /**
     * Button to end the visit this OngoingVisit display tracks
     */
    @SuppressWarnings("serial")
    private class EndButton extends Button implements ActionListener {

        private DataDelegate model; // Data model the app is current using

        /**
         * Constructs a button that can end one visit
         * 
         * @param visitKey
         *            Key of the visit you want this button to be able to end
         */
        public EndButton(DataDelegate model) {
            super("End");
            addActionListener(this);
        }


        /**
         * When button is clicked, ends the ongoing visit belonging to the
         * student specified by visitorID()
         */
        public void actionPerformed(ActionEvent e) {

        }

    }

}
