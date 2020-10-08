package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Exportable(name = "Submission Record", nameInDB = "submission_record")
@Data
@Entity
public class SubmissionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Exportable(name = "Submission Id", nameInDB = "submission_id")
    private String submission_id;

    // Track the submission is submitted to; like a full paper or just a poster
    @Exportable(name = "Track Id", nameInDB = "track_id", description = "Track the submission is submitted to")
    private String track_id;

    // Name for the track referred in col2 (string)
    @Exportable(name = "Track Name", nameInDB = "track_name")
    private String track_name;

    // Title of the submission
    @Exportable(name = "Title", nameInDB = "title")
    private String title;

    // time submitted
    @Exportable(name = "Submission Time", nameInDB = "submission_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date submission_time;

    //  time last updated
    @Exportable(name = "Last Updated Time", nameInDB = "last_updated_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date last_updated_time;

    // keywords associated with submissions as put by the authors
    @Exportable(name = "Keywords", nameInDB = "keywords")
    private String keywords;

    // accept/reject decision
    @Exportable(name = "Is Accepted", nameInDB = "is_accepted", description = "Accept/Reject decision")
    private String is_accepted;

    // acceptance/rejection mail sent to authors or not?
    @Exportable(name = "Is Notified", nameInDB = "is_notified", description = "Acceptance/rejection mail sent to authors or not?")
    private String is_notified;

    // review sent in the mails or not?
    @Exportable(name = "Is Reviews Sent", nameInDB = "is_reviews_sent", description = "Review sent in the mails or not?")
    private String is_reviews_sent;

    // abstract of the submission.
    @Exportable(name = "Submission Abstract", nameInDB = "submission_abstract", description = "Abstract of the submission")
    private String submission_abstract;

    @Column("version_id")
    private Long versionId;

    public SubmissionRecord() {}

    public SubmissionRecord(String submission_id, String track_id, String track_name, String title,
                            Date submission_time, Date last_updated_time, String keywords, String is_accepted,
                            String is_notified, String is_reviews_sent, String submission_abstract) {
        this.id = null;
        this.submission_id = submission_id;
        this.track_id = track_id;
        this.track_name = track_name;
        this.title = title;
        this.submission_time = submission_time;
        this.last_updated_time = last_updated_time;
        this.keywords = keywords;
        this.is_accepted = is_accepted;
        this.is_notified = is_notified;
        this.is_reviews_sent = is_reviews_sent;
        this.submission_abstract = submission_abstract;
    }

}
