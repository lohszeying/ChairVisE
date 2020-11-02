package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AnalysisRequest;
import sg.edu.nus.comp.cs3219.viz.common.entity.PresentationSection;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.AuthorRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Exportable;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.ReviewRecord;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.SubmissionRecord;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    private static final Logger log = Logger.getLogger(AnalysisService.class.getSimpleName());

    private static final Map<String, Class> DATABASE_FIELD_NAME_TO_TYPE_MAP = new HashMap<>();

    static {
        populateMapForClass(AuthorRecord.class);
        populateMapForClass(ReviewRecord.class);
        populateMapForClass(SubmissionRecord.class);
    }

    /**
     * Generates a map from field name to type so SQL query can be correctly generated.
     */
    private static void populateMapForClass(Class<?> classToExamine) {
        Arrays.stream(classToExamine.getDeclaredFields())
                .filter(f -> f.getAnnotation(Exportable.class) != null)
                .forEach(field ->
                        DATABASE_FIELD_NAME_TO_TYPE_MAP.put(field.getAnnotation(Exportable.class).nameInDB(), field.getType()));
    }

    private final JdbcTemplate jdbcTemplate;

    public AnalysisService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> analyse(AnalysisRequest analysisRequest) {
        // Check that analysis is done for current user only

        String sql = generateSQL(analysisRequest);

        log.info("Analysis Query: " + sql);
        return jdbcTemplate.queryForList(sql);
    }

    private String addVersionToNestedQuery(PresentationSection.Record record, int version){
        String MAIL_REGEX = "([_A-Za-z0-9-]+)(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";
        String regex = "(\\w+\\S).data_set = \"" + MAIL_REGEX + "\"";
        String strToMatch = record.getName();
        //log.info("strToMatch in record.getName(): " + strToMatch);
        strToMatch = strToMatch.replaceAll(regex, "$1.version_id = " + version);
        //log.info("strToMatch: " + strToMatch);
        return strToMatch;
    }

    private String generateSQL(AnalysisRequest analysisRequest) {
        String selectionsStr = analysisRequest.getSelections().stream()
                .map(s -> s.getExpression() + String.format(" AS `%s`", s.getRename()))
                .collect(Collectors.joining(","));
        if (selectionsStr.isEmpty()) {
            selectionsStr = "*";
        }

        String tablesStr = analysisRequest.getInvolvedRecords().stream()
                // band-aid fix. Total fix is too length
                .map(x -> addVersionToNestedQuery(x, analysisRequest.getVersionId()))
                .collect(Collectors.joining(","));

        String joinersStr = analysisRequest.getJoiners().stream()
                .map(j -> String.format("%s = %s", j.getLeft(), j.getRight()))
                .collect(Collectors.joining(" AND "));

        String filtersStr = analysisRequest.getFilters().stream()
                .map(f -> String.format("%s %s %s", f.getField(), f.getComparator(), wrapValue(f.getField(), f.getValue())))
                .collect(Collectors.joining(" AND "));

        String dataSetVersionFilter = analysisRequest.getInvolvedRecords().stream()
                .filter(r -> !r.isCustomized())
                .map(t -> String.format(" %s.version_id = %d ",
                       /* t.getName(), analysisRequest.getDataSet(), */
                        t.getName(), analysisRequest.getVersionId()))
                .collect(Collectors.joining(" AND "));

        String groupersStr = analysisRequest.getGroupers().stream()
                .map(PresentationSection.Grouper::getField)
                .collect(Collectors.joining(","));

        String sortersStr = analysisRequest.getSorters().stream()
                .map(s -> String.format("%s %s", s.getField(), s.getOrder()))
                .collect(Collectors.joining(","));

        String baseSQL = String.format("SELECT %s FROM %s", selectionsStr, tablesStr);

        if (!dataSetVersionFilter.isEmpty()) {
            baseSQL += String.format(" WHERE %s", dataSetVersionFilter);
        } else {
            baseSQL += " WHERE true";
        }

        if (!joinersStr.isEmpty()) {
            baseSQL += String.format(" AND %s", joinersStr);
        }

        if (!filtersStr.isEmpty()) {
            baseSQL += String.format(" AND %s", filtersStr);
        }

        if (!groupersStr.isEmpty()) {
            baseSQL += String.format(" GROUP BY %s", groupersStr);
        }

        if (!sortersStr.isEmpty()) {
            baseSQL += String.format(" ORDER BY %s", sortersStr);
        }
        return baseSQL;
    }

    // wrap value in '' if it is a non-numerical value
    String wrapValue(String fieldName, String val) {
        if (!DATABASE_FIELD_NAME_TO_TYPE_MAP.containsKey(fieldName)){
            return val;
        }
        Class fieldType = DATABASE_FIELD_NAME_TO_TYPE_MAP.get(fieldName);
        if (Integer.class.equals(fieldType) || int.class.equals(fieldType)
                || Double.class.equals(fieldType) || double.class.equals(fieldType)
                || Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
            return val;
        }
        return String.format("'%s'", val);
    }
}
