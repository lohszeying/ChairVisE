package sg.edu.nus.comp.cs3219.viz.service;

import org.springframework.stereotype.Service;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.storage.repository.VersionRepository;

import java.util.List;

@Service
public class VersionService {

    private final VersionRepository versionRepository;
    private final GateKeeper gateKeeper;

    public VersionService(VersionRepository versionRepository, GateKeeper gateKeeper){
        this.versionRepository = versionRepository;
        this.gateKeeper = gateKeeper;
    }

    public List<Version> findAllForUser(){
//        gateKeeper.verifyLoginAccess();
        UserInfo currentUser = gateKeeper.getCurrentLoginUser();
        return versionRepository.findByDataSet(currentUser.getUserEmail());
    }

    public List<Version> findAllForUserWithRecordType(String recordType) {
//        gateKeeper.verifyLoginAccess();
        UserInfo currentUser = gateKeeper.getCurrentLoginUser();
        return versionRepository.findByDataSetAndRecordType(currentUser.getUserEmail(), recordType);
    }

    public Version saveForUser(Version version){
//        gateKeeper.verifyLoginAccess();
        UserInfo currentUser = gateKeeper.getCurrentLoginUser();
        Version newVersion = new Version(version.getDataSet(), version.getRecordType());

        return versionRepository.save(newVersion);
    }

}
