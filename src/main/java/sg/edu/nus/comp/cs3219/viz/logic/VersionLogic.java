package sg.edu.nus.comp.cs3219.viz.logic;

import org.springframework.stereotype.Component;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.UserInfo;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Version;
import sg.edu.nus.comp.cs3219.viz.storage.repository.VersionRepository;

import java.util.List;

@Component
public class VersionLogic {

    private final VersionRepository versionRepository;

    public VersionLogic(VersionRepository versionRepository){
        this.versionRepository = versionRepository;
    }

    public List<Version> findAllForUser(UserInfo userInfo){
        return versionRepository.findByDataSet(userInfo.getUserEmail());
    }

    public List<Version> findAllForUserWithRecordType(UserInfo userInfo, String recordType){
        return versionRepository.findByDataSetAndRecordType(userInfo.getUserEmail(), recordType);
    }

    public Version saveForUser(Version version, UserInfo userInfo){

        Version newVersion = new Version(version.getDataSet(), version.getRecordType());

        return versionRepository.save(newVersion);
    }

}
