package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.exception.ConferenceNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRepository;

import java.util.List;

@Service
public class ConferenceService {

    private final GateKeeper gateKeeper;
    private final ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository, GateKeeper gateKeeper) {
        this.conferenceRepository = conferenceRepository;
        this.gateKeeper = gateKeeper;
    }

    public List<Conference> findAllForUser() {
        UserInfo userInfo = gateKeeper.getCurrentLoginUser();
        return conferenceRepository.findByCreatorIdentifier(userInfo.getUserEmail());
    }

    public Conference findById(Long id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));
    }

    public Conference saveForUser(Conference conference) {
        UserInfo userInfo = gateKeeper.getCurrentLoginUser();

        conference.setCreatorIdentifier(userInfo.getUserEmail());

        return conferenceRepository.save(conference);
    }

    public Conference updateConference(Conference newConference, Long id) {
        return conferenceRepository.findById(id)
                .map(conference -> {
                    conference.setName(newConference.getName());
                    conference.setDescription(newConference.getDescription());
                    conference.setDate(newConference.getDate());
                    return conferenceRepository.save(conference);
                })
                .orElseThrow(() -> new ConferenceNotFoundException(id));
    }

    public void deleteById(Long id) {
        conferenceRepository.deleteById(id);
    }
}
