package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.Conference;
import sg.edu.nus.comp.cs3219.viz.service.ConferenceService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class ConferenceController extends BaseRestController {

    private final ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @GetMapping("/conferences")
    public List<Conference> all() {
        return conferenceService.findAllForUser();
    }

    @PostMapping("/conferences")
    public ResponseEntity<?> newConference(@RequestBody Conference conference) throws URISyntaxException {
        Conference newConference = conferenceService.saveForUser(conference);

        return ResponseEntity
                .created(new URI("/conferences/" + newConference.getId()))
                .body(newConference);
    }

    @GetMapping("/conferences/{id}")
    public Conference one(@PathVariable Long id) {
        return conferenceService.findById(id);
    }

    @PutMapping("/conferences/{id}")
    public ResponseEntity<?> updateConference(@RequestBody Conference newConference, @PathVariable Long id) throws URISyntaxException {
        Conference updatedConference = conferenceService.updateConference(newConference, id);

        return ResponseEntity
                .created(new URI("/conferences/" + newConference.getId()))
                .body(updatedConference);
    }

    @DeleteMapping("/conferences/{id}")
    public ResponseEntity<?> deleteConference(@PathVariable Long id) {
        conferenceService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
