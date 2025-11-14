CREATE TABLE roadmap (
     id SERIAL PRIMARY KEY,
     user_id BIGINT NOT NULL,
     target_occupation VARCHAR(150) NOT NULL,
     description VARCHAR(500),
     CONSTRAINT fk_roadmap_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE checkpoint (
    id SERIAL PRIMARY KEY,
    roadmap_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    checkpoint_order INTEGER NOT NULL,
    CONSTRAINT fk_checkpoint_roadmap FOREIGN KEY (roadmap_id) REFERENCES roadmap(id) ON DELETE CASCADE
);

CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    checkpoint_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    platform VARCHAR(100) NOT NULL,
    url VARCHAR(500),
    description VARCHAR(500),
    duration_hours INTEGER,
    CONSTRAINT fk_course_checkpoint FOREIGN KEY (checkpoint_id) REFERENCES checkpoint(id) ON DELETE CASCADE
);

CREATE INDEX idx_roadmap_user_id ON roadmap(user_id);
CREATE INDEX idx_checkpoint_roadmap_id ON checkpoint(roadmap_id);
CREATE INDEX idx_checkpoint_order ON checkpoint(roadmap_id, checkpoint_order);
CREATE INDEX idx_course_checkpoint_id ON course(checkpoint_id);