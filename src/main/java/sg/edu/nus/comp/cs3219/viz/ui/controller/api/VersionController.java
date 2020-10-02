package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.service.VersionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class VersionController extends BaseRestController{

    private final VersionService versionService;

    public VersionController(VersionService versionService) {
        this.versionService = versionService;
    }

    @GetMapping("/version")
    public List<Version> all(){
        return versionService.findAllForUser();
    }

    @GetMapping("/version/{recordType}")
    public List<Version> allVersionByRecordType(@PathVariable String recordType){
        return versionService.findAllForUserWithRecordType(recordType);
    }

    @PostMapping("/version")
    public ResponseEntity<?> newVersion(@RequestBody Version version) throws URISyntaxException {
        Version newVersion = versionService.saveForUser(version);

        return ResponseEntity
                // TODO: might change what URI is returned
                .created(new URI("/version/" + newVersion.getId()))
                .body(newVersion);
    }
}
