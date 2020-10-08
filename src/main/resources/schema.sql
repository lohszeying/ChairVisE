drop table if exists authorrecord, submissionrecord, reviewrecord, presentation,
    presentationaccesscontrol, presentationsection, version, conference;

CREATE TABLE Conference (
    id int NOT NULL AUTO_INCREMENT,
    title varchar(255),
    description varchar(255),
    user_email varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE Version (
    id int NOT NULL AUTO_INCREMENT,
    ver_date date,
    conference_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (conference_id) REFERENCES Conference (id),
    CONSTRAINT UC_Version UNIQUE (ver_date, conference_id)
);

CREATE TABLE Author_Record (
    id int NOT NULL AUTO_INCREMENT,
    submission_id int,
    first_name varchar(255),
    last_name varchar(255),
    full_name varchar(255),
    email varchar(255),
    country varchar(255),
    organization varchar(255),
    web_page text,
    person_id int,
    is_corresponding boolean,
    version_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES Version (id)
);

CREATE TABLE Submission_Record (
    id int NOT NULL AUTO_INCREMENT,
    submission_id int,
    track_id int,
    track_name varchar(255),
    title text,
    submission_time timestamp,
    last_updated_time timestamp,
    keywords varchar(255),
    decision varchar(6),
    is_notified boolean,
    is_reviews_sent boolean,
    submission_abstract text,
    version_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES Version (id)
);

CREATE TABLE Review_Record (
    id int NOT NULL AUTO_INCREMENT,
    review_id int,
    submission_id int,
    num_review_assignment int,
    reviewer_name varchar(255),
    expertiseLevel double,
    confidenceLevel double,
    review_comment text,
    overall_eval_score int,
    review_submission_date date,
    review_submission_time time,
    best_paper_recommendation boolean,
    version_id int,
    PRIMARY KEY (id),
    FOREIGN KEY (version_id) REFERENCES Version (id)
);

CREATE TABLE Presentation (
    id int NOT NULL AUTO_INCREMENT,
    user_email varchar(255),
    name varchar(255),
    version int,
    description varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE Presentation_Permission (
    id int NOT NULL AUTO_INCREMENT,
    user_email varchar(255),
    presentation_id int,
    permission varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (presentation_id) REFERENCES Presentation (id),
    CONSTRAINT UC_Permission UNIQUE (user_email, presentation_id)
);

CREATE TABLE Presentation_Section (
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
    FOREIGN KEY (presentation_id) REFERENCES Presentation (id)
);
