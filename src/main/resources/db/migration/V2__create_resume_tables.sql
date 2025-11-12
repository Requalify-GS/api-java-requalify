CREATE TABLE resume (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    occupation VARCHAR(150) NOT NULL,
    summary VARCHAR(1000) NOT NULL,
    CONSTRAINT fk_resume_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE resume_skills (
   resume_id BIGINT NOT NULL,
   skills VARCHAR(255),
   CONSTRAINT fk_skills_resume FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE
);

CREATE TABLE education (
   id SERIAL PRIMARY KEY,
   resume_id BIGINT NOT NULL,
   institution VARCHAR(200) NOT NULL,
   course VARCHAR(200) NOT NULL,
   education_level VARCHAR(50) NOT NULL,
   start_date DATE NOT NULL,
   end_date DATE,
   in_progress BOOLEAN,
   CONSTRAINT fk_education_resume FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE
);

CREATE TABLE experience (
    id SERIAL PRIMARY KEY,
    resume_id BIGINT NOT NULL,
    company VARCHAR(200) NOT NULL,
    position VARCHAR(150) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    current_job BOOLEAN,
    description VARCHAR(1000),
    CONSTRAINT fk_experience_resume FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE
);

CREATE TABLE certification (
   id SERIAL PRIMARY KEY,
   resume_id BIGINT NOT NULL,
   name VARCHAR(200) NOT NULL,
   issuing_organization VARCHAR(200) NOT NULL,
   CONSTRAINT fk_certification_resume FOREIGN KEY (resume_id) REFERENCES resume(id) ON DELETE CASCADE
);