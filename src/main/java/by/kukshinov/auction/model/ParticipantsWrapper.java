package by.kukshinov.auction.model;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsWrapper {
    private List<Participant> participants;

    public ParticipantsWrapper(
		  List<Participant> participants) {
	   this.participants = participants;
    }

    public ParticipantsWrapper() {
    }

    public List<Participant> getParticipants() {
	   return new ArrayList<>(participants);
    }

    public void setParticipants(
		  List<Participant> participants) {
	   this.participants = participants;
    }
}
