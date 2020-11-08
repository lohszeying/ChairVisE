

CREATE TABLE conference (
    id int NOT NULL AUTO_INCREMENT,
    title varchar(255),
    description varchar(255),
    user_email varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE version (
    id int NOT NULL AUTO_INCREMENT,
    ver_date date,
    conference_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (conference_id) REFERENCES conference (id) ON DELETE CASCADE,
    CONSTRAINT UC_Version UNIQUE (ver_date, conference_id)
);

CREATE TABLE author_record (
    a_id int NOT NULL AUTO_INCREMENT,
    a_submission_id int,
    a_first_name varchar(255),
    a_last_name varchar(255),
    full_name varchar(255),
    a_email varchar(255),
    a_country varchar(255),
    a_organisation varchar(255),
    a_web_page text,
    a_person_id int,
    a_is_corresponding varchar(3),
    version_id int,
    PRIMARY KEY (a_id),
    FOREIGN KEY (version_id) REFERENCES version (id) ON DELETE CASCADE
);

CREATE TABLE submission_record (
    s_id int NOT NULL AUTO_INCREMENT,
    s_submission_id int,
    s_track_id int,
    s_track_name varchar(255),
    s_title text,
    s_authors text,
    s_submission_time timestamp,
    s_last_updated_time timestamp,
    s_keywords varchar(255),
    s_is_accepted varchar(6),
    s_is_notified varchar(3),
    s_is_reviews_sent varchar(3),
    s_submission_abstract text,
    version_id int,
    PRIMARY KEY (s_id),
    FOREIGN KEY (version_id) REFERENCES version (id) ON DELETE CASCADE
);

CREATE TABLE review_record (
    r_id int NOT NULL AUTO_INCREMENT,
    r_review_id int,
    r_submission_id int,
    r_num_review_assignment int,
    r_reviewer_name varchar(255),
    r_expertise_level double,
    r_confidence_level double,
    r_review_comment text,
    r_overall_evaluation_score int,
    r_review_submission_time timestamp,
    r_has_recommended_for_best_paper varchar(3),
    version_id int,
    PRIMARY KEY (r_id),
    FOREIGN KEY (version_id) REFERENCES version (id) ON DELETE CASCADE
);

CREATE TABLE presentation_permission (
    id int NOT NULL AUTO_INCREMENT,
    user_email varchar(255),
    version_id int,
    permission varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES version (id) ON DELETE CASCADE,
    CONSTRAINT UC_Permission UNIQUE (user_email, version_id)
);

CREATE TABLE presentation_section (
    id int NOT NULL AUTO_INCREMENT,
    version_id int,
    title varchar(255),
    description text,
    type varchar(255),
    data_set varchar(255),
    selections text,
    involved_records text,
    filters text,
    joiners text,
    groupers text,
    sorters text,
    extra_data text,
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES version (id) ON DELETE CASCADE
);
