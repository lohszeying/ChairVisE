package sg.edu.nus.comp.cs3219.viz.common.exception;

public class VersionNotFoundException extends RuntimeException {

    public VersionNotFoundException(Long conferenceId, Long versionId) {
        super(String.format("Could not find conference %d for version %d", conferenceId, versionId));
    }

}
