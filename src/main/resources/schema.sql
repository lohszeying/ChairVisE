drop table if exists author_record, submission_record, review_record, presentation_permission,
    presentation_section, presentation, version, conference;

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
    id int NOT NULL AUTO_INCREMENT,
    submission_id int,
    first_name varchar(255),
    last_name varchar(255),
    full_name varchar(255),
    email varchar(255),
    country varchar(255),
    organisation varchar(255),
    web_page text,
    person_id int,
    is_corresponding varchar(3),
    version_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES version (id)
);

CREATE TABLE submission_record (
    id int NOT NULL AUTO_INCREMENT,
    submission_id int,
    track_id int,
    track_name varchar(255),
    title text,
    submission_time timestamp,
    last_updated_time timestamp,
    keywords varchar(255),
    is_accepted varchar(6),
    is_notified varchar(3),
    is_reviews_sent varchar(3),
    submission_abstract text,
    version_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES version (id)
);

CREATE TABLE review_record (
    id int NOT NULL AUTO_INCREMENT,
    review_id int,
    submission_id int,
    num_review_assignment int,
    reviewer_name varchar(255),
    expertise_level double,
    confidence_level double,
    review_comment text,
    overall_evaluation_score int,
    review_submission_time timestamp,
    has_recommended_for_best_paper varchar(3),
    version_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES version (id)
);

CREATE TABLE presentation (
    id int NOT NULL AUTO_INCREMENT,
    user_email varchar(255),
    version_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES version (id)
);

CREATE TABLE presentation_permission (
    id int NOT NULL AUTO_INCREMENT,
    user_email varchar(255),
    presentation_id int,
    permission varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (presentation_id) REFERENCES presentation (id),
    CONSTRAINT UC_Permission UNIQUE (user_email, presentation_id)
);

CREATE TABLE presentation_section (
    id int NOT NULL AUTO_INCREMENT,
    presentation_id int,
    title varchar(255),
    description text,
    type varchar(255),
    selections varchar(255),
    involved_records varchar(255),
    filters varchar(255),
    joiners varchar(255),
    groupers varchar(255),
    sorters varchar(255),
    extra_data varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (presentation_id) REFERENCES presentation (id)
);
