package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.entity.Presentation;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationNotFoundException;
import sg.edu.nus.comp.cs3219.viz.common.exception.PresentationSectionNotFoundException;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationRepository;
import sg.edu.nus.comp.cs3219.viz.storage.repository.PresentationSectionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PresentationSectionService {

    private final PresentationRepository presentationRepository;
    private final PresentationSectionRepository presentationSectionRepository;

    public PresentationSectionService(PresentationRepository presentationRepository,
                                      PresentationSectionRepository presentationSectionRepository) {
        this.presentationRepository = presentationRepository;
        this.presentationSectionRepository = presentationSectionRepository;
    }

    public List<PresentationSection> findAllByPresentation(Long presentationId) {
        Presentation presentation = presentationRepository.findById(presentationId)
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));

        return presentationSectionRepository.findAllByPresentation(presentation);
    }

    public PresentationSection saveForPresentation(Long presentationId, PresentationSection presentationSection) {
        Presentation presentation = presentationRepository.findById(presentationId)
                .orElseThrow(() -> new PresentationNotFoundException(presentationId));

        PresentationSection newPresentationSection = new PresentationSection();
        newPresentationSection.setPresentation(presentation);
        newPresentationSection.setTitle(presentationSection.getTitle());
        newPresentationSection.setDescription(presentationSection.getDescription());
        newPresentationSection.setType(presentationSection.getType());
        newPresentationSection.setDataSet(presentationSection.getDataSet());
        newPresentationSection.setSelections(presentationSection.getSelections());
        newPresentationSection.setInvolvedRecords(presentationSection.getInvolvedRecords());
        newPresentationSection.setFilters(presentationSection.getFilters());
        newPresentationSection.setJoiners(presentationSection.getJoiners());
        newPresentationSection.setGroupers(presentationSection.getGroupers());
        newPresentationSection.setSorters(presentationSection.getSorters());
        newPresentationSection.setExtraData(presentationSection.getExtraData());

        return presentationSectionRepository.save(newPresentationSection);
    }

    public Optional<PresentationSection> findById(Long id) {
        return presentationSectionRepository.findById(id);
    }

    public PresentationSection updatePresentation(Long presentationId, Long sectionId, PresentationSection newPresentationSection) {
        PresentationSection oldPresentationSection = this.findById(sectionId)
                .orElseThrow(() -> new PresentationSectionNotFoundException(presentationId, sectionId));

        oldPresentationSection.setTitle(newPresentationSection.getTitle());
        oldPresentationSection.setDescription(newPresentationSection.getDescription());
        oldPresentationSection.setDataSet(newPresentationSection.getDataSet());
        oldPresentationSection.setSelections(newPresentationSection.getSelections());
        oldPresentationSection.setInvolvedRecords(newPresentationSection.getInvolvedRecords());
        oldPresentationSection.setFilters(newPresentationSection.getFilters());
        oldPresentationSection.setJoiners(newPresentationSection.getJoiners());
        oldPresentationSection.setGroupers(newPresentationSection.getGroupers());
        oldPresentationSection.setSorters(newPresentationSection.getSorters());
        oldPresentationSection.setExtraData(newPresentationSection.getExtraData());

        return presentationSectionRepository.save(oldPresentationSection);
    }

    public void deleteById(Long presentationId, Long sectionId) {
        PresentationSection presentationSection = this.findById(sectionId)
                .orElseThrow(() -> new PresentationSectionNotFoundException(presentationId, sectionId));

        Presentation presentation = presentationSection.getPresentation();

        presentationSectionRepository.deleteById(sectionId);
    }
}
