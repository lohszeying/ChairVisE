package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.*;
import sg.edu.nus.comp.cs3219.viz.storage.repository.*;

import java.util.List;

@Service
public class RecordService {

    private final AuthorRecordRepository authorRecordRepository;

    private final SubmissionRecordRepository submissionRecordRepository;

    private final ReviewRecordRepository reviewRecordRepository;

    public RecordService(AuthorRecordRepository authorRecordRepository,
                         SubmissionRecordRepository submissionRecordRepository,
                         ReviewRecordRepository reviewRecordRepository
    ) {
        this.authorRecordRepository = authorRecordRepository;
        this.submissionRecordRepository = submissionRecordRepository;
        this.reviewRecordRepository = reviewRecordRepository;
    }

    @Transactional
    public List<AuthorRecord> saveAuthorRecordList(Long versionId, List<AuthorRecord> authorRecordList) {
        authorRecordList.forEach(record -> record.setVersionId(versionId));

        return authorRecordRepository.saveAll(authorRecordList);
    }

    @Transactional
    public List<SubmissionRecord> saveSubmissionRecordList(Long versionId, List<SubmissionRecord> submissionRecordList) {
        submissionRecordList.forEach(record -> record.setVersionId(versionId));

        return submissionRecordRepository.saveAll(submissionRecordList);
    }

    @Transactional
    public List<ReviewRecord> saveReviewRecordList(Long versionId, List<ReviewRecord> reviewRecordList) {
        reviewRecordList.forEach(record -> record.setVersionId(versionId));

        return reviewRecordRepository.saveAll(reviewRecordList);
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

    @Transactional
    public void deleteAuthorRecordList(Long versionId) {
        authorRecordRepository.deleteAllByVersionId(versionId);
    }

    @Transactional
    public void deleteSubmissionRecordList(Long versionId) {
        submissionRecordRepository.deleteAllByVersionId(versionId);
    }

    @Transactional
    public void deleteReviewRecordList(Long versionId) {
        reviewRecordRepository.deleteAllByVersionId(versionId);
    }
}
