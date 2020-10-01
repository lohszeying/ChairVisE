package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import javax.persistence.*;
import java.util.List;

@Exportable(name = "Version", nameInDB = "version_id")
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

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AuthorRecord> getAuthorRecordList() {
        return authorRecordList;
    }

    public void setAuthorRecordList(List<AuthorRecord> authorRecordList) {
        this.authorRecordList = authorRecordList;
    }

    public List<SubmissionRecord> getSubmissionRecordList() {
        return submissionRecordList;
    }

    public void setSubmissionRecordList(List<SubmissionRecord> submissionRecordList) {
        this.submissionRecordList = submissionRecordList;
    }

    public List<ReviewRecord> getReviewRecordList() {
        return reviewRecordList;
    }

    public void setReviewRecordList(List<ReviewRecord> reviewRecordList) {
        this.reviewRecordList = reviewRecordList;
    }
}
