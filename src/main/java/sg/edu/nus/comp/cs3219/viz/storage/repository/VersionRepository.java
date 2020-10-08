package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;

import java.util.List;
import java.util.Optional;

public interface VersionRepository extends JpaRepository<Version, Long> {

    List<Version> findAllByConferenceId(Long conferenceId);

    Optional<Version> findByConferenceIdAndId(Long conferenceId, Long versionId);

    boolean existsByConferenceIdAndId(Long conferenceId, Long versionId);

    boolean deleteByConferenceIdAndId(Long conferenceId, Long versionId);
}
