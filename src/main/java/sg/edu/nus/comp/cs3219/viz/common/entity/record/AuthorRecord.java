package sg.edu.nus.comp.cs3219.viz.common.entity.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import sg.edu.nus.comp.cs3219.viz.common.util.Deserializer.AuthorRecordDeserializer;

import javax.persistence.*;

@JsonDeserialize(using = AuthorRecordDeserializer.class)
@Exportable(name = "Author Record", nameInDB = "author_record")
@Entity
public class AuthorRecord {

    public AuthorRecord(){}

    public AuthorRecord(Version v, String submissionId, String firstName, String lastName, String email, String country,
                        String organisation, String webPage, String personId, String isCorresponding){
        this.id = null;
        this.version = v;
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
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "a_id")
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "data_set", referencedColumnName = "data_set"),
            @JoinColumn(name = "record_type", referencedColumnName = "record_type"),
            @JoinColumn(name = "version", referencedColumnName = "version"),
    })
    private Version version;

    public Version getVersion(){return version;}
    public void setVersion(Version version){this.version = version;}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getWebPage() {
        return webPage;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getIsCorresponding() {
        return isCorresponding;
    }

    public void setIsCorresponding(String isCorresponding) {
        this.isCorresponding = isCorresponding;
    }
}
