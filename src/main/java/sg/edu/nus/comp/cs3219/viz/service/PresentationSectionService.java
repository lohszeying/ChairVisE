package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationSectionNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationSectionRepository;

import java.util.List;

@Service
public class PresentationSectionService {

    private final PresentationSectionRepository presentationSectionRepository;

    public PresentationSectionService(PresentationSectionRepository presentationSectionRepository) {
        this.presentationSectionRepository = presentationSectionRepository;
    }

    @Transactional
    public List<PresentationSection> findAllByVersion(Long versionId) {

        return presentationSectionRepository.findAllByVersionId(versionId);
    }

    @Transactional
    public PresentationSection savePresentationSection(Long versionId, PresentationSection presentationSection) {

        presentationSection.setVersionId(versionId);
        return presentationSectionRepository.save(presentationSection);
    }

    @Transactional
    public PresentationSection updatePresentationSection(Long versionId, Long sectionId, PresentationSection newPresentationSection) {
        return presentationSectionRepository.findById(sectionId)
                .map(presentationSection -> {
                    presentationSection.setTitle(newPresentationSection.getTitle());
                    presentationSection.setDescription(newPresentationSection.getDescription());
                    presentationSection.setSelections(newPresentationSection.getSelections());
                    presentationSection.setInvolvedRecords(newPresentationSection.getInvolvedRecords());
                    presentationSection.setFilters(newPresentationSection.getFilters());
                    presentationSection.setJoiners(newPresentationSection.getJoiners());
                    presentationSection.setGroupers(newPresentationSection.getGroupers());
                    presentationSection.setSorters(newPresentationSection.getSorters());
                    presentationSection.setExtraData(newPresentationSection.getExtraData());
                    return presentationSectionRepository.save(presentationSection);
                })
                .orElseThrow(() -> new PresentationSectionNotFoundException(versionId, sectionId));
    }

    @Transactional
    public void deletePresentationSection(Long versionId, Long sectionId) {
        presentationSectionRepository.findById(sectionId)
                .orElseThrow(() -> new PresentationSectionNotFoundException(versionId, sectionId));

        presentationSectionRepository.deleteById(sectionId);
    }
}
