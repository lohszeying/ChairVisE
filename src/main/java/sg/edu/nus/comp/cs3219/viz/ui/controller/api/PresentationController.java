package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.service.PresentationService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class PresentationController extends BaseRestController {

    private final PresentationService presentationService;

    public PresentationController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @GetMapping("/presentations")
    public List<Presentation> all() {
        return presentationService.findAllForUser();
    }

    @PostMapping("/presentations")
    public ResponseEntity<?> newPresentation(@RequestBody Presentation presentation) throws URISyntaxException {
        Presentation newPresentation = presentationService.saveForUser(presentation);

        return ResponseEntity
                .created(new URI("/presentations/" + newPresentation.getId()))
                .body(newPresentation);
    }

    @GetMapping("/presentations/{id}")
    public Presentation one(@PathVariable Long id) {
        return presentationService.findById(id)
                .orElseThrow(() -> new PresentationNotFoundException(id));
    }

    @PutMapping("/presentations/{id}")
    public ResponseEntity<?> updatePresentation(@RequestBody Presentation newPresentation, @PathVariable Long id) throws URISyntaxException {
        Presentation updatedPresentation = presentationService.updatePresentation(id, newPresentation);

        return ResponseEntity
                .created(new URI("/presentations/" + newPresentation.getId()))
                .body(updatedPresentation);
    }

    @DeleteMapping("/presentations/{id}")
    public ResponseEntity<?> deletePresentation(@PathVariable Long id) {
        presentationService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
