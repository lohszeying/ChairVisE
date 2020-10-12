package sg.edu.nus.comp.cs3219.viz.common.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import sg.edu.nus.comp.cs3219.viz.service.AnalysisService;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Data
@Entity
public class PresentationSection {

    private static final Logger log = Logger.getLogger(AnalysisService.class.getSimpleName());

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "presentation_id")
    private Long presentationId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String type;

    // The following field does not worth to be stored as relation in RDBMS
    // we store them as serialized json string

    @Column(columnDefinition = "TEXT")
    private String selections;

    @Column(columnDefinition = "TEXT")
    private String involvedRecords;

    @Column(columnDefinition = "TEXT")
    private String filters;

    @Column(columnDefinition = "TEXT")
    private String joiners;

    @Column(columnDefinition = "TEXT")
    private String groupers;

    @Column(columnDefinition = "TEXT")
    private String sorters;

    @Column(columnDefinition = "TEXT")
    private String extraData;

    @Data
    public static class Selection {
        private String expression;
        private String rename;
    }

    @Data
    public static class Record {
        private String name;
        private boolean isCustomized;
    }

    @Data
    public static class Filter {
        private String field;
        private String comparator;
        private String value;
    }

    @Data
    public static class Joiner {
        private String left;
        private String right;
    }

    @Data
    public static class Grouper {
        private String field;
    }

    @Data
    public static class Sorter {
        private String field;
        private String order;
    }

    public List<Selection> getSelections() {
        return get(groupers, Selection.class);
    }

    public void setSelections(List<Selection> selections) {
        this.selections = set(selections);
    }

    public List<Record> getInvolvedRecords() {
        return get(groupers, Record.class);
    }

    public void setInvolvedRecords(List<Record> involvedRecords) {
        this.involvedRecords = set(involvedRecords);
    }

    public List<Filter> getFilters() {
        return get(groupers, Filter.class);
    }

    public void setFilters(List<Filter> filters) {
        this.filters = set(filters);
    }

    public List<Joiner> getJoiners() {
        return get(groupers, Joiner.class);
    }

    public void setJoiners(List<Joiner> joiners) {
        this.joiners = set(joiners);
    }

    public List<Grouper> getGroupers() {
        return get(groupers, Grouper.class);
    }

    public void setGroupers(List<Grouper> groupers) {
        this.groupers = set(groupers);
    }

    public List<Sorter> getSorters() {
        return get(sorters, Sorter.class);
    }

    public void setSorters(List<Sorter> sorters) {
        this.sorters = set(sorters);
    }

    public Map<String, Object> getExtraData() {
        try {
            return objectMapper.readValue(extraData, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            log.severe(e.getMessage());
            return new HashMap<>();
        }
    }

    public void setExtraData(Map<String, Object> extraData) {
        try {
            this.extraData = objectMapper.writeValueAsString(extraData);
        } catch (JsonProcessingException e) {
            log.severe(e.getMessage());
        }
    }

    private String set(List<?> data) {
        try {
            return objectMapper.writeValueAsString(extraData);
        } catch (JsonProcessingException e) {
            log.severe(e.getMessage());
            return "";
        }
    }

    private <T> List<T> get(String data, Class<T> type) {
        try {
            return objectMapper.readValue(data, new TypeReference<T>() {});
        } catch (IOException e) {
            log.severe(e.getMessage());
            return new ArrayList<T>();
        }
    }
}
