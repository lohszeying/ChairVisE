package sg.edu.nus.comp.cs3219.viz.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationPermission;

import java.util.List;

public interface PresentationPermissionRepository extends JpaRepository<PresentationPermission, Long> {

    List<PresentationPermission> findAllByPresentationId(Long presentationId);

}
