package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.*;
import sg.edu.nus.comp.cs3219.viz.storage.repository.*;

import java.util.List;

@Service
public class RecordService {

    private final AuthorRecordRepository authorRecordRepository;

    private final SubmissionRecordRepository submissionRecordRepository;

    private final ReviewRecordRepository reviewRecordRepository;

    private final GateKeeper gateKeeper;

    public RecordService(AuthorRecordRepository authorRecordRepository,
                         SubmissionRecordRepository submissionRecordRepository,
                         ReviewRecordRepository reviewRecordRepository,
                         GateKeeper gateKeeper
    ) {
        this.authorRecordRepository = authorRecordRepository;
        this.submissionRecordRepository = submissionRecordRepository;
        this.reviewRecordRepository = reviewRecordRepository;
        this.gateKeeper = gateKeeper;
    }

    @Transactional
    public void removeAndPersistAuthorRecordForDataSet(Long versionId, List<AuthorRecord> authorRecordList) {
//        gateKeeper.verifyLoginAccess();
        UserInfo userInfo = gateKeeper.getCurrentLoginUser();

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
//        gateKeeper.verifyLoginAccess();
        UserInfo userInfo = gateKeeper.getCurrentLoginUser();

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
//        gateKeeper.verifyLoginAccess();
        UserInfo userInfo = gateKeeper.getCurrentLoginUser();

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
