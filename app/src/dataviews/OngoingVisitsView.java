package dataviews;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import backend.Visit;
import backend.VisitTable;
import backend.Visitor;
import modelviewtools.DataDelegate;

public class OngoingVisitsView extends Panel implements ActionListener {

    // Delegate the panel is using as a reference
    private DataDelegate delegate;

    // All current ongoing visits being displayed
    private final ArrayList<OngoingVisit> ongoingVisits =
        new ArrayList<OngoingVisit>();

    public OngoingVisitsView(DataDelegate delegate) {

        this.delegate = delegate;
        delegate.addListener(this);

    }


    /**
     * TODO CREATE AN ActionEvent SUBCLASS FOR ALL DELEGATE-VIEW INTERACTIONS
     */

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("addedOngoing")) {
            displayOngoingVisit(e.getID());
        }

    }


    private void displayOngoingVisit(int visitorID) {
        Visitor visitor = delegate.fromVisitors(visitorID);
        Visit newOngoing = ((VisitTable)visitor.field("history")).entry(0);
        add(new OngoingVisit(newOngoing.key(), delegate));
    }


    private void removeOngoingVisit(int visitorID) {
        for (OngoingVisit ongoingVisit : ongoingVisits) {

        }
    }

}
