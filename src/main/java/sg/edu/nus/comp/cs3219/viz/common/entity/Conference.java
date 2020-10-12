package sg.edu.nus.comp.cs3219.viz.common.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "user_email")
    private String userEmail;

}
