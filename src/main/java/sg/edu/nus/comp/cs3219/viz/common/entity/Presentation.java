package sg.edu.nus.comp.cs3219.viz.common.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Presentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "version_id")
    private Long versionId;

}
