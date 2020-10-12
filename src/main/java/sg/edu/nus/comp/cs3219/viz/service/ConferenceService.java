package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.exception.ConferenceNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.ConferenceRepository;

import java.util.List;

@Service
public class ConferenceService {

    private final AuthService authService;
    private final ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository, AuthService authService) {
        this.conferenceRepository = conferenceRepository;
        this.authService = authService;
    }

    @Transactional
    public List<Conference> findAllForUser() {
        UserInfo userInfo = authService.getCurrentLoginUser();
        return conferenceRepository.findAllByUserEmail(userInfo.getUserEmail());
    }

    @Transactional
    public Conference findById(Long conferenceId) {
        return conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new ConferenceNotFoundException(conferenceId));
    }

    @Transactional
    public Conference saveForUser(Conference conference) {
        UserInfo userInfo = authService.getCurrentLoginUser();

        conference.setUserEmail(userInfo.getUserEmail());

        return conferenceRepository.save(conference);
    }

    @Transactional
    public Conference updateConference(Conference newConference, Long conferenceId) {
        return conferenceRepository.findById(conferenceId)
                .map(conference -> {
                    conference.setTitle(newConference.getTitle());
                    conference.setDescription(newConference.getDescription());
                    return conferenceRepository.save(conference);
                })
                .orElseThrow(() -> new ConferenceNotFoundException(conferenceId));
    }

    @Transactional
    public void deleteById(Long conferenceId) {
        conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new ConferenceNotFoundException(conferenceId));

        conferenceRepository.deleteById(conferenceId);
    }
}
