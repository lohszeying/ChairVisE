package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Exportable(name = "Author Record", nameInDB = "author_record")
@Data
@Entity
public class AuthorRecord {

    public AuthorRecord(){}

    public AuthorRecord(String submissionId, String firstName, String lastName, String email, String country,
                        String organisation, String webPage, String personId, String isCorresponding){
        this.id = null;
        this.submissionId = submissionId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.organisation = organisation;
        this.webPage = webPage;
        this.personId = personId;
        this.isCorresponding = isCorresponding;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "a_id")
    private Long id;

    @Column(name = "version_id")
    private Long versionId;

    @Exportable(name = "Submission Id", nameInDB = "a_submission_id")
    @Column(name = "a_submission_id")
    private String submissionId;

    @Exportable(name = "First Name", nameInDB = "a_first_name")
    @Column(name = "a_first_name")
    private String firstName;

    @Exportable(name = "Last Name", nameInDB = "a_last_name")
    @Column(name = "a_last_name")
    private String lastName;

    @Exportable(name = "Email", nameInDB = "a_email")
    @Column(name = "a_email")
    private String email;

    @Exportable(name = "Country", nameInDB = "a_country")
    @Column(name = "a_country")
    private String country;

    @Exportable(name = "Organisation", nameInDB = "a_organisation")
    @Column(name = "a_organisation")
    private String organisation;

    @Exportable(name = "Web Page", nameInDB = "a_web_page")
    @Column(name = "a_web_page", columnDefinition = "TEXT")
    private String webPage;

    // author's unique id in user submitted csv file
    @Exportable(name = "Person Id", nameInDB = "a_person_id", description = "Author's unique id in user submitted csv file")
    @Column(name = "a_person_id")
    private String personId;

    // is the author corresponding author for the submission
    @Exportable(name = "Is Corresponding", nameInDB = "a_is_corresponding", description = "Is the author corresponding author for the submission")
    @Column(name = "a_is_corresponding")
    @JsonProperty("isCorresponding")
    private String isCorresponding;

}
