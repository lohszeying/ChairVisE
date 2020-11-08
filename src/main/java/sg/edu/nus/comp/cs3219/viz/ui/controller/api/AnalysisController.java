package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.web.bind.annotation.*;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.AnalysisRequest;
import sg.edu.nus.comp.cs3219.viz.service.AnalysisService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AnalysisController {

    private final AnalysisService analysisService;

    private static final Logger log = Logger.getLogger(AnalysisService.class.getSimpleName());

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/versions/{id}/analysis")
    public List<Map<String, Object>> analysis(@PathVariable Long id, @Valid @RequestBody AnalysisRequest analysisRequest) {
        List<Map<String, Object>> result = analysisService.analyse(analysisRequest);
        log.info("Analysis Result from query: " + result);
        // convert to map with key all in lower case
        return result.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            m.forEach((k, v) -> map.put(k.toLowerCase(), v));
            return map;
        }).collect(Collectors.toList());
    }

}
