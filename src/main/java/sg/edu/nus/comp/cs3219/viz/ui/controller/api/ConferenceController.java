package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.common.exception.ConferenceNotFoundException;
import sg.edu.nus.comp.cs3219.viz.service.ConferenceService;
import sg.edu.nus.comp.cs3219.viz.service.GateKeeper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ConferenceController extends BaseRestController {

    private final ConferenceService conferenceService;

    private final GateKeeper gateKeeper;

    public ConferenceController(ConferenceService conferenceService,
                                GateKeeper gateKeeper) {
        this.conferenceService = conferenceService;
        this.gateKeeper = gateKeeper;
    }

    @GetMapping("/conferences")
    public List<Conference> all() {
        UserInfo currentUser = gateKeeper.verifyLoginAccess();

        return conferenceService.findAllForUser(currentUser);
    }

    @PostMapping("/conferences")
    public ResponseEntity<?> newConference(@RequestBody Conference conference) throws URISyntaxException {
        UserInfo currentUser = gateKeeper.verifyLoginAccess();

        Conference newConference = conferenceService.saveForUser(conference, currentUser);

        return ResponseEntity
                .created(new URI("/conferences/" + newConference.getId()))
                .body(newConference);
    }

    @GetMapping("/conferences/{id}")
    public Conference one(@PathVariable Long id) {
        Conference conference = conferenceService.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));

        //gateKeeper.verifyAccessForConference(conference, AccessLevel.CAN_READ);

        return conference;
    }

    @PutMapping("/conferences/{id}")
    public ResponseEntity<?> updateConference(@RequestBody Conference newConference, @PathVariable Long id) throws URISyntaxException {

        Conference oldConference = conferenceService.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));
        //gateKeeper.verifyAccessForConference(oldConference, AccessLevel.CAN_WRITE);

        Conference updatedConference = conferenceService.updateConference(oldConference, newConference);
        return ResponseEntity
                .created(new URI("/conferences/" + newConference.getId()))
                .body(updatedConference);
    }

    @DeleteMapping("/conferences/{id}")
    public ResponseEntity<?> deleteConference(@PathVariable Long id) {
        Conference oldConference = conferenceService.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException(id));
        //gateKeeper.verifyDeletionAccessForConference(oldConference);

        conferenceService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
