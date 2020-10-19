package sg.edu.nus.comp.cs3219.viz.ui.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.DBEntityMetaData;
import sg.edu.nus.comp.cs3219.viz.service.DBMetaDataService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DBMetaDataController {

    private DBMetaDataService dbMetaDataService;

    public DBMetaDataController(DBMetaDataService dbMetaDataService) {
        this.dbMetaDataService = dbMetaDataService;
    }

    @GetMapping("/db/entity")
    public List<DBEntityMetaData> getEntityMetaDataList() {
        return dbMetaDataService.getEntityMetaDataList();
    }

}
