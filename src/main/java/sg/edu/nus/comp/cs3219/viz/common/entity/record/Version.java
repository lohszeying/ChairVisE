package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Exportable(name = "Version", nameInDB = "version_id")
@Data
@Entity
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("ver_date")
    private String date;

    @Column("conference_id")
    private Long conferenceId;

    @OneToMany
    @JoinColumn(name = "version_id")
    private List<AuthorRecord> authorRecordList;
    @OneToMany
    @JoinColumn(name = "version_id")
    private List<SubmissionRecord> submissionRecordList;
    @OneToMany
    @JoinColumn(name = "version_id")
    private List<ReviewRecord> reviewRecordList;

    public Version() {}

}
