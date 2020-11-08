package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.*;
import sg.edu.nus.comp.cs3219.viz.storage.repository.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class RecordService {

    private final AuthorRecordRepository authorRecordRepository;

    private final SubmissionRecordRepository submissionRecordRepository;

    private final ReviewRecordRepository reviewRecordRepository;

    private static final Logger log = Logger.getLogger(AnalysisService.class.getSimpleName());

    public RecordService(AuthorRecordRepository authorRecordRepository,
                         SubmissionRecordRepository submissionRecordRepository,
                         ReviewRecordRepository reviewRecordRepository
    ) {
        this.authorRecordRepository = authorRecordRepository;
        this.submissionRecordRepository = submissionRecordRepository;
        this.reviewRecordRepository = reviewRecordRepository;
    }

    @Transactional
    public Map<String, Boolean> checkRecords(Long versionId) {
        boolean hasAuthorRecord = authorRecordRepository.existsByVersionId(versionId);
        log.info("hasAuthorRecord verId: " + versionId + ", bool: " + hasAuthorRecord);
        boolean hasSubmissionRecord = submissionRecordRepository.existsByVersionId(versionId);
        boolean hasReviewRecord = reviewRecordRepository.existsByVersionId(versionId);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("authorRecord", hasAuthorRecord);
        map.put("submissionRecord", hasSubmissionRecord);
        map.put("reviewRecord", hasReviewRecord);
        return map;
    }

    @Transactional
    public List<AuthorRecord> replaceAuthorRecordList(Long versionId, List<AuthorRecord> authorRecordList) {
        authorRecordRepository.deleteAllByVersionId(versionId);

        authorRecordList.forEach(record -> record.setVersionId(versionId));

        return authorRecordRepository.saveAll(authorRecordList);
    }

    @Transactional
    public List<SubmissionRecord> replaceSubmissionRecordList(Long versionId, List<SubmissionRecord> submissionRecordList) {
        submissionRecordRepository.deleteAllByVersionId(versionId);

        submissionRecordList.forEach(record -> record.setVersionId(versionId));

        return submissionRecordRepository.saveAll(submissionRecordList);
    }

    @Transactional
    public List<ReviewRecord> replaceReviewRecordList(Long versionId, List<ReviewRecord> reviewRecordList) {
        reviewRecordRepository.deleteAllByVersionId(versionId);

        reviewRecordList.forEach(record -> record.setVersionId(versionId));

        return reviewRecordRepository.saveAll(reviewRecordList);
    }

}
