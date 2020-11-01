package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Exportable(name = "Review Record", nameInDB = "review_record")
@Data
@Entity
public class ReviewRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long r_id;

    @Exportable(name = "Review Id", nameInDB = "r_review_id")
    private int r_review_id;

    @Exportable(name = "Submission Id", nameInDB = "r_submission_id")
    private int r_submission_id;

    // (Each reviewer is given a number for each track he/she is reviewing.
    // For example Animesh reviewed 2 different tracks but 3 papers in total- one from Track 1 and two papers from Track 2. He therefore has 2 uniques numbers assigned
    @Exportable(name = "Num Review Assignment", nameInDB = "r_num_review_assignment", description = "Each reviewer is given a number for each track he/she is reviewing")
    private int r_num_review_assignment;

    @Exportable(name = "Reviewer Name", nameInDB = "r_reviewer_name")
    private String r_reviewer_name;

    // Reviewer selects a field 1-5 to indicate expertise while submitting the review. For example 5: expert, 1: passing knowledge
    @Exportable(name = "Expertise Level", nameInDB = "r_expertise_level", description = "Reviewer selects a field 1-5 to indicate expertise when submitting the review.")
    private double r_expertise_level;

    @Exportable(name = "Confidence Level", nameInDB = "r_confidence_level", description = "Reviewer selects a field 1-5 to indicate confidence level for the review.")
    private double r_confidence_level;

    @Exportable(name = "Review Comment", nameInDB = "r_review_comment")
    private String r_review_comment;

    @Exportable(name = "Overall Evaluation Score", nameInDB = "r_overall_evaluation_score")
    private double r_overall_evaluation_score;

    @Exportable(name = "Review Submission Time", nameInDB = "r_review_submission_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp r_review_submission_time;

    @Exportable(name = "Has Recommended for the Best Paper", nameInDB = "r_has_recommended_for_best_paper")
    private String r_has_recommended_for_best_paper;

    @Column(name = "version_id")
    private Long versionId;

}
