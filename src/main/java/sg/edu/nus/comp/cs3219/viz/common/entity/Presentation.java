package sg.edu.nus.comp.cs3219.viz.common.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Presentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String version;

    private String description;

    @Column("user_email")
    private String userEmail;

}
