package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.service.VersionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/conferences/{conferenceId}/versions")
public class VersionController {

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping
    public List<Version> all(@PathVariable Long conferenceId){
        return versionService.findAllByConferenceId(conferenceId);
    }

    @PostMapping
    public ResponseEntity<?> newVersion(
            @PathVariable Long conferenceId,
            @RequestBody Version version
    ) throws URISyntaxException {

        Version newVersion = versionService.saveVersion(conferenceId, version);

        return ResponseEntity
                // TODO: might change what URI is returned
                .created(new URI("/version/" + newVersion.getId()))
                .body(newVersion);
    }

    @GetMapping("/{versionId}")
    public Version one(@PathVariable Long conferenceId, @PathVariable Long versionId) {
        return versionService.findOne(conferenceId, versionId);
    }

    @PutMapping("/{versionId}")
    public Version updateVersion(
            @PathVariable Long conferenceId,
            @PathVariable Long versionId,
            @RequestBody Version version
    ) {
        return versionService.updateVersion(conferenceId, versionId, version);
    }

    @DeleteMapping("/{versionId}")
    public ResponseEntity<?> deleteVersion(
            @PathVariable Long conferenceId,
            @PathVariable Long versionId
    ) {
        versionService.deleteVersion(conferenceId, versionId);
        return ResponseEntity.noContent().build();
    }
}
