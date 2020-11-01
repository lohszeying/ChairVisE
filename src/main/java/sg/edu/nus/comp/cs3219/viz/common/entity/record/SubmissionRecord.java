package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Exportable(name = "Submission Record", nameInDB = "submission_record")
@Data
@Entity
public class SubmissionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long s_id;

    @Exportable(name = "Submission Id", nameInDB = "s_submission_id")
    private int s_submission_id;

    // Track the submission is submitted to; like a full paper or just a poster
    @Exportable(name = "Track Id", nameInDB = "s_track_id", description = "Track the submission is submitted to")
    private int s_track_id;

    // Name for the track referred in col2 (string)
    @Exportable(name = "Track Name", nameInDB = "s_track_name")
    private String s_track_name;

    // Title of the submission
    @Exportable(name = "Title", nameInDB = "s_title")
    private String s_title;

    // time submitted
    @Exportable(name = "Submission Time", nameInDB = "s_submission_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm")
    private Timestamp s_submission_time;

    //  time last updated
    @Exportable(name = "Last Updated Time", nameInDB = "s_last_updated_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm")
    private Timestamp s_last_updated_time;

    // keywords associated with submissions as put by the authors
    @Exportable(name = "Keywords", nameInDB = "keywords")
    private String s_keywords;

    // accept/reject decision
    @Exportable(name = "Is Accepted", nameInDB = "s_is_accepted", description = "Accept/Reject decision")
    private String s_is_accepted;

    // acceptance/rejection mail sent to authors or not?
    @Exportable(name = "Is Notified", nameInDB = "s_is_notified", description = "Acceptance/rejection mail sent to authors or not?")
    private String s_is_notified;

    // review sent in the mails or not?
    @Exportable(name = "Is Reviews Sent", nameInDB = "s_is_reviews_sent", description = "Review sent in the mails or not?")
    private String s_is_reviews_sent;

    // abstract of the submission.
    @Exportable(name = "Submission Abstract", nameInDB = "s_submission_abstract", description = "Abstract of the submission")
    private String s_submission_abstract;

    @Column(name = "version_id")
    private Long versionId;

}
