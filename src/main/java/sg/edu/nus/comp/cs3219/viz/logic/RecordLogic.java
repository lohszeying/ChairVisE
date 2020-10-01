package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.*;
import sg.edu.nus.comp.cs3219.viz.storage.repository.*;

import java.util.List;

@Component
public class RecordLogic {
    private AuthorRecordRepository authorRecordRepository;

    private SubmissionRecordRepository submissionRecordRepository;

    private ReviewRecordRepository reviewRecordRepository;

    public RecordLogic(AuthorRecordRepository authorRecordRepository,
                       SubmissionRecordRepository submissionRecordRepository,
                       ReviewRecordRepository reviewRecordRepository) {
        this.authorRecordRepository = authorRecordRepository;
        this.submissionRecordRepository = submissionRecordRepository;
        this.reviewRecordRepository = reviewRecordRepository;
    }

    @Transactional
    public void removeAndPersistAuthorRecordForDataSet(Long versionId, List<AuthorRecord> authorRecordList) {
        if (authorRecordList.isEmpty()){
            return;
        }

        authorRecordRepository.deleteAllByVersionId(versionId);

        authorRecordList.forEach(record -> {
            record.setVersionId(versionId);
        });

        authorRecordRepository.saveAll(authorRecordList);
    }

    @Transactional
    public void removeAndPersistReviewRecordForDataSet(Long versionId, List<ReviewRecord> reviewRecordList) {
        if (reviewRecordList.isEmpty()){
            return;
        }

        reviewRecordRepository.deleteAllByVersionId(versionId);

        reviewRecordList.forEach(record -> {
            record.setVersionId(versionId);
        });

        reviewRecordRepository.saveAll(reviewRecordList);
    }

    @Transactional
    public void removeAndPersistSubmissionRecordForDataSet(Long versionId, List<SubmissionRecord> submissionRecordList) {
        if (submissionRecordList.isEmpty()){
            return;
        }

        submissionRecordRepository.deleteAllByVersionId(versionId);

        submissionRecordList.forEach(record -> {
            record.setVersionId(versionId);
        });

        submissionRecordRepository.saveAll(submissionRecordList);
    }
}
