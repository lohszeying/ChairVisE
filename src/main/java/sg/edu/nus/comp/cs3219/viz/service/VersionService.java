package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.common.exception.VersionNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.VersionRepository;

import java.util.List;

@Service
public class VersionService {

    private final VersionRepository versionRepository;

    public VersionService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    @Transactional
    public List<Version> findAllByConferenceId(Long conferenceId) {
        return versionRepository.findAllByConferenceId(conferenceId);
    }

    @Transactional
    public Version findOne(Long conferenceId, Long versionId) {
        return versionRepository.findByConferenceIdAndId(conferenceId, versionId)
                .orElseThrow(() -> new VersionNotFoundException(versionId));
    }

    @Transactional
    public Version saveVersion(Long conferenceId, Version version) {
        version.setConferenceId(conferenceId);
        return versionRepository.save(version);
    }

    @Transactional
    public Version updateVersion(Long conferenceId, Long versionId, Version newVersion) {
        return versionRepository.findByConferenceIdAndId(conferenceId, versionId)
                .map(version -> {
                    version.setDate(newVersion.getDate());
                    return versionRepository.save(version);
                }).orElseThrow(() -> new VersionNotFoundException(versionId));
    }

    @Transactional
    public void deleteVersion(Long conferenceId, Long versionId) {
        boolean success = versionRepository.deleteByConferenceIdAndId(conferenceId, versionId);
        if (!success) throw new VersionNotFoundException(versionId);
    }

}
