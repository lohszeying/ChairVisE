package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Long> {

    List<Version> findAllByConferenceId(Long conferenceId);

}
