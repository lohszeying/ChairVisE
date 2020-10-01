package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Exportable(name = "Version", nameInDB = "version_id")
@Data
@Entity
public class Version {

    @Column(name = "data_set")
    private String dataSet;
    @Column(name = "record_type")
    private String recordType;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id")
    private Long id;

    @OneToMany
    @JoinColumn(name = "version_id")
    private List<AuthorRecord> authorRecordList;
    @OneToMany
    @JoinColumn(name = "version_id")
    private List<SubmissionRecord> submissionRecordList;
    @OneToMany
    @JoinColumn(name = "version_id")
    private List<ReviewRecord> reviewRecordList;

    public Version() {
    }

    public Version(String dataSet, String recordType) {
        this.dataSet = dataSet;
        this.recordType = recordType;
    }

}
