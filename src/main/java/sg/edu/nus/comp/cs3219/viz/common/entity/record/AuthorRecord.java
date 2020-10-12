package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import lombok.Data;

import javax.persistence.*;

@Exportable(name = "Author Record", nameInDB = "author_record")
@Data
@Entity
public class AuthorRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Exportable(name = "Submission Id", nameInDB = "submission_id")
    private int submission_id;

    @Exportable(name = "First Name", nameInDB = "first_name")
    private String first_name;

    @Exportable(name = "Last Name", nameInDB = "last_name")
    private String last_name;

    @Exportable(name = "Email", nameInDB = "email")
    private String email;

    @Exportable(name = "Country", nameInDB = "country")
    private String country;

    @Exportable(name = "Organisation", nameInDB = "organisation")
    private String organisation;

    @Exportable(name = "Web Page", nameInDB = "web_page")
    private String web_page;

    // author's unique id in user submitted csv file
    @Exportable(name = "Person Id", nameInDB = "person_id", description = "Author's unique id in user submitted csv file")
    private int person_id;

    // is the author corresponding author for the submission
    @Exportable(name = "Is Corresponding", nameInDB = "is_corresponding", description = "Is the author corresponding author for the submission")
    private String is_corresponding;

    @Column(name = "version_id")
    private Long versionId;

}
