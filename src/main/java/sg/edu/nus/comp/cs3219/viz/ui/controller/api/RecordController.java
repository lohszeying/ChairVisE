package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.AuthorRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.ReviewRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionRecord;
import sg.edu.nus.comp.cs3219.viz.service.RecordService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class RecordController extends BaseRestController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/version/{versionId}/author")
    public ResponseEntity<?> importAuthorRecord(
            @PathVariable Long versionId,
            @RequestBody List<AuthorRecord> authorRecordList
    ) throws URISyntaxException {

        this.recordService.removeAndPersistAuthorRecordForDataSet(versionId, authorRecordList);

        return ResponseEntity.created(new URI("/record/author")).build();
    }

    @PostMapping("/version/{versionId}/review")
    public ResponseEntity<?> importReviewRecord(
            @PathVariable Long versionId,
            @RequestBody List<ReviewRecord> reviewRecordList
    ) throws URISyntaxException {

        this.recordService.removeAndPersistReviewRecordForDataSet(versionId, reviewRecordList);

        return ResponseEntity.created(new URI("/record/review")).build();
    }

    @PostMapping("/version/{versionId}/submission")
    public ResponseEntity<?> importSubmissionRecord(
            @PathVariable Long versionId,
            @RequestBody List<SubmissionRecord> submissionRecords
    ) throws URISyntaxException {

        this.recordService.removeAndPersistSubmissionRecordForDataSet(versionId, submissionRecords);

        return ResponseEntity.created(new URI("/record/review")).build();
    }
}
