package sg.edu.nus.comp.cs3219.viz.common.exception;

public class PresentationPermissionNotFoundException extends RuntimeException {

    public PresentationPermissionNotFoundException(Long presentationId, Long permissionId) {
        super(String.format("Could not find permission %d for presentation %d", permissionId, presentationId));
    }

}
