package sg.edu.nus.comp.cs3219.viz.ui.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PointcutDeclarations {

    @Pointcut("execution(* sg.edu.nus.comp.cs3219.viz.ui.controller.api.ConferenceController.*(*))")
    public void forConferenceMethods() {}

    @Pointcut("execution(* sg.edu.nus.comp.cs3219.viz.ui.controller.api.VersionController.*(*))")
    public void forVersionMethods() {}

    @Pointcut("execution(* sg.edu.nus.comp.cs3219.viz.ui.controller.api.RecordController.*(*))")
    public void forRecordMethods() {}

    @Pointcut("execution(* sg.edu.nus.comp.cs3219.viz.ui.controller.api.PresentationController.*(*))")
    public void forPresentationMethods() {}

    @Pointcut("execution(* sg.edu.nus.comp.cs3219.viz.ui.controller.api.PresentationPermissionController.*(*))")
    public void forPresentationAccessControlMethods() {}

    @Pointcut("execution(* sg.edu.nus.comp.cs3219.viz.ui.controller.api.PresentationSectionController.*(*))")
    public void forPresentationSectionMethods() {}

    @Pointcut("execution(* sg.edu.nus.comp.cs3219.viz.ui.controller.api.AnalysisController.*(*))")
    public void forAnalysisMethods() {}

    @Pointcut("forConferenceMethods() || forVersionMethods() || forRecordMethods() || " +
            "forPresentationMethods() || forPresentationAccessControlMethods() || forPresentationSectionMethods() ||" +
            "forAnalysisMethods()")
    public void forAuthentication() {}
}
