package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionRecord;

public interface SubmissionRecordRepository extends JpaRepository<SubmissionRecord, Long> {

    boolean existsByVersionId(Long versionId);

    void deleteAllByVersionId(Long versionId);
}
