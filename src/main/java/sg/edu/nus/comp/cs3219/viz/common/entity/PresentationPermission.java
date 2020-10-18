package sg.edu.nus.comp.cs3219.viz.common.entity;

import lombok.Data;
import sg.edu.nus.comp.cs3219.viz.common.datatransfer.Permission;

import javax.persistence.*;

@Data
@Entity
public class PresentationPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "presentation_id")
    private Long presentationId;

    // access level for the user
    @Enumerated(EnumType.STRING)
    private Permission permission;

}
