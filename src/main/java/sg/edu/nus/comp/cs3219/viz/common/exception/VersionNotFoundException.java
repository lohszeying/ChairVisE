package sg.edu.nus.comp.cs3219.viz.common.exception;

public class VersionNotFoundException extends RuntimeException {

    public VersionNotFoundException(Long id) {
        super("Could not find version " + id);
    }

}
