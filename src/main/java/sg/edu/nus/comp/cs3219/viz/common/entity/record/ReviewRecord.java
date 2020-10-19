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
    private Long id;

    @Exportable(name = "Review Id", nameInDB = "review_id")
    private int review_id;

    @Exportable(name = "Submission Id", nameInDB = "submission_id")
    private int submission_id;

    // (Each reviewer is given a number for each track he/she is reviewing.
    // For example Animesh reviewed 2 different tracks but 3 papers in total- one from Track 1 and two papers from Track 2. He therefore has 2 uniques numbers assigned
    @Exportable(name = "Num Review Assignment", nameInDB = "num_review_assignment", description = "Each reviewer is given a number for each track he/she is reviewing")
    private int num_review_assignment;

    @Exportable(name = "Reviewer Name", nameInDB = "reviewer_name")
    private String reviewer_name;

    // Reviewer selects a field 1-5 to indicate expertise while submitting the review. For example 5: expert, 1: passing knowledge
    @Exportable(name = "Expertise Level", nameInDB = "expertise_level", description = "Reviewer selects a field 1-5 to indicate expertise when submitting the review.")
    private double expertise_level;

    @Exportable(name = "Confidence Level", nameInDB = "confidence_level", description = "Reviewer selects a field 1-5 to indicate confidence level for the review.")
    private double confidence_level;

    @Exportable(name = "Review Comment", nameInDB = "review_comment")
    private String review_comment;

    @Exportable(name = "Overall Evaluation Score", nameInDB = "overall_evaluation_score")
    private double overall_evaluation_score;

    @Exportable(name = "Review Submission Time", nameInDB = "review_submission_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp review_submission_time;

    @Exportable(name = "Has Recommended for the Best Paper", nameInDB = "has_recommended_for_best_paper")
    private String has_recommended_for_best_paper;

    @Column(name = "version_id")
    private Long versionId;

}
