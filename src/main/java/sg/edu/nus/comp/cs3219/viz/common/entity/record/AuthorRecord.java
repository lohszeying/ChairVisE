package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import lombok.Data;

import javax.persistence.*;

@Exportable(name = "Author Record", nameInDB = "author_record")
@Data
@Entity
public class AuthorRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long a_id;

    @Exportable(name = "Submission Id", nameInDB = "a_submission_id")
    private int a_submission_id;

    @Exportable(name = "First Name", nameInDB = "a_first_name")
    private String a_first_name;

    @Exportable(name = "Last Name", nameInDB = "a_last_name")
    private String a_last_name;

    @Exportable(name = "Email", nameInDB = "a_email")
    private String a_email;

    @Exportable(name = "Country", nameInDB = "a_country")
    private String a_country;

    @Exportable(name = "Organisation", nameInDB = "a_organisation")
    private String a_organisation;

    @Exportable(name = "Web Page", nameInDB = "a_web_page")
    private String a_web_page;

    // author's unique id in user submitted csv file
    @Exportable(name = "Person Id", nameInDB = "a_person_id", description = "Author's unique id in user submitted csv file")
    private int a_person_id;

    // is the author corresponding author for the submission
    @Exportable(name = "Is Corresponding", nameInDB = "a_is_corresponding", description = "Is the author corresponding author for the submission")
    private String a_is_corresponding;

    @Column(name = "version_id")
    private Long versionId;

}
