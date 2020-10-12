package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import lombok.Data;

import javax.persistence.*;

@Exportable(name = "Version", nameInDB = "version_id")
@Data
@Entity
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ver_date")
    private String date;

    @Column(name = "conference_id")
    private Long conferenceId;

    public Version() {}

}
