DROP TABLE IF EXISTS candidate_skills;
DROP TABLE IF EXISTS candidates;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS account;


CREATE TABLE IF NOT EXISTS candidates(
    id int(11) NOT NULL,
    first_name varchar(50),
    last_name varchar(50),
    phone varchar(20),
    email varchar(100),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS skills(
    id int(11) NOT NULL,
    name varchar(100),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS candidate_skills(
    candidate_id int(11) NOT NULL,
    skill_id int(11) NOT NULL,
    CONSTRAINT fk_candidates FOREIGN KEY(candidate_id) REFERENCES candidates(id),
    CONSTRAINT fk_skills FOREIGN KEY(skill_id) REFERENCES skills(id)
);

CREATE TABLE IF NOT EXISTS account(
    id int(11) NOT NULL,
    balancer int(11) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO candidates(id, first_name, last_name, phone, email) VALUES (121, 'Tony', 'Snowden', '(649)555-5500', 'tony.s@outlook.com');
INSERT INTO candidates(id, first_name, last_name, phone, email) VALUES (120, 'Sue', 'Taylor', '(415)555-9857', 'sue.t@outlook.com');
INSERT INTO candidates(id, first_name, last_name, phone, email) VALUES (119, 'Thomas', 'Smith', '(415)555-4312', 'thomas.s@outlook.com');

INSERT INTO skills(id, name) VALUES(1, 'Java');
INSERT INTO skills(id, name) VALUES(2, 'Linux');
INSERT INTO skills(id, name) VALUES(3, 'JBoss');

INSERT INTO candidate_skills(candidate_id, skill_id) VALUES(121, 1);
INSERT INTO candidate_skills(candidate_id, skill_id) VALUES(120, 1);
INSERT INTO candidate_skills(candidate_id, skill_id) VALUES(120, 2);
INSERT INTO candidate_skills(candidate_id, skill_id) VALUES(120, 3);
INSERT INTO candidate_skills(candidate_id, skill_id) VALUES(119, 1);

INSERT INTO account (id, balancer) VALUES (1, 100);

SELECT * FROM candidates;
SELECT * FROM skills;
SELECT * FROM candidate_skills;
SELECT * FROM account;
