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
    private String submission_id;

    @Exportable(name = "First Name", nameInDB = "first_name")
    private String first_name;

    @Exportable(name = "Last Name", nameInDB = "last_name")
    private String last_name;

    @Exportable(name = "Email", nameInDB = "email")
    private String email;

    @Exportable(name = "Country", nameInDB = "country")
    private String country;

    @Exportable(name = "Organization", nameInDB = "organization")
    private String organization;

    @Exportable(name = "Web Page", nameInDB = "web_page")
    private String web_page;

    // author's unique id in user submitted csv file
    @Exportable(name = "Person Id", nameInDB = "person_id", description = "Author's unique id in user submitted csv file")
    private String person_id;

    // is the author corresponding author for the submission
    @Exportable(name = "Is Corresponding", nameInDB = "is_corresponding", description = "Is the author corresponding author for the submission")
    private boolean is_corresponding;

    @Column("version_id")
    private Long versionId;

    public AuthorRecord(){}

    public AuthorRecord(String submission_id, String first_name, String last_name, String email, String country,
                        String organization, String web_page, String person_id, String is_corresponding){
        this.submission_id = submission_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.country = country;
        this.organization = organization;
        this.web_page = web_page;
        this.person_id = person_id;
        this.is_corresponding = is_corresponding.equals("yes");
    }

}
