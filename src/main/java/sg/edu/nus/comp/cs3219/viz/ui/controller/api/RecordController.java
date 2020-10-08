package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.AuthorRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.ReviewRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionRecord;
import sg.edu.nus.comp.cs3219.viz.service.RecordService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/versions/{versionId}")
public class RecordController extends BaseRestController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/authorRecord")
    public ResponseEntity<?> saveAuthorRecordList(
            @PathVariable Long versionId,
            @RequestBody List<AuthorRecord> authorRecordList
    ) throws URISyntaxException {

        List<AuthorRecord> authorRecords = this.recordService.saveAuthorRecordList(versionId, authorRecordList);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/authorRecord"))
                .body(authorRecords);
    }

    @PostMapping("/submissionRecord")
    public ResponseEntity<?> saveSubmissionRecordList(
            @PathVariable Long versionId,
            @RequestBody List<SubmissionRecord> submissionRecordList
    ) throws URISyntaxException {

        List<SubmissionRecord> submissionRecords = this.recordService.saveSubmissionRecordList(versionId, submissionRecordList);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/submissionRecord"))
                .body(submissionRecords);
    }

    @PostMapping("/reviewRecord")
    public ResponseEntity<?> saveReviewRecordList(
            @PathVariable Long versionId,
            @RequestBody List<ReviewRecord> reviewRecordList
    ) throws URISyntaxException {

        List<ReviewRecord> reviewRecords = this.recordService.saveReviewRecordList(versionId, reviewRecordList);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/reviewRecord"))
                .body(reviewRecords);
    }

    @PutMapping("/authorRecord")
    public ResponseEntity<?> replaceAuthorRecordList(
            @PathVariable Long versionId,
            @RequestBody List<AuthorRecord> authorRecordList
    ) throws URISyntaxException {

        List<AuthorRecord> authorRecords = this.recordService.replaceAuthorRecordList(versionId, authorRecordList);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/authorRecord"))
                .body(authorRecords);
    }

    @PutMapping("/submissionRecord")
    public ResponseEntity<?> replaceSubmissionRecordList(
            @PathVariable Long versionId,
            @RequestBody List<SubmissionRecord> submissionRecordList
    ) throws URISyntaxException {

        List<SubmissionRecord> submissionRecords = this.recordService.replaceSubmissionRecordList(versionId, submissionRecordList);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/submissionRecord"))
                .body(submissionRecords);
    }

    @PutMapping("/reviewRecord")
    public ResponseEntity<?> replaceReviewRecordList(
            @PathVariable Long versionId,
            @RequestBody List<ReviewRecord> reviewRecordList
    ) throws URISyntaxException {

        List<ReviewRecord> reviewRecords = this.recordService.replaceReviewRecordList(versionId, reviewRecordList);

        return ResponseEntity
                .created(new URI("/versions/" + versionId + "/reviewRecord"))
                .body(reviewRecords);
    }

    @DeleteMapping("/authorRecord")
    public ResponseEntity<?> deleteAuthorRecordList(@PathVariable Long versionId) {
        this.recordService.deleteAuthorRecordList(versionId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/submissionRecord")
    public ResponseEntity<?> deleteSubmissionRecordList(@PathVariable Long versionId) {
        this.recordService.deleteSubmissionRecordList(versionId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reviewRecord")
    public ResponseEntity<?> deleteReviewRecordList(@PathVariable Long versionId) {
        this.recordService.deleteReviewRecordList(versionId);

        return ResponseEntity.noContent().build();
    }
}
