DROP DATABASE IF EXISTS calorie_tracker;
CREATE DATABASE calorie_tracker;
USE calorie_tracker;

CREATE TABLE users (
    id       INT          NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    sex      VARCHAR(255) NOT NULL,
    height   DOUBLE       NOT NULL,
    weight   DOUBLE       NOT NULL,
    age      INT          NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE muscle_group (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    image_path VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE exercise (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    MET DOUBLE NOT NULL,
    duration DOUBLE NOT NULL,
    repetitions INT NOT NULL,
    video_path VARCHAR(255) NOT NULL,
    sibling_exercise_id INT,
    PRIMARY KEY (id)
);

CREATE TABLE warmup (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    MET DOUBLE NOT NULL,
    duration DOUBLE NOT NULL,
    repetitions INT NOT NULL,
    video_path VARCHAR(255) NOT NULL,
    sibling_warmup_id INT,
    PRIMARY KEY (id)
);

CREATE TABLE stretch (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    MET DOUBLE NOT NULL,
    duration DOUBLE NOT NULL,
    repetitions INT NOT NULL,
    video_path VARCHAR(255) NOT NULL,
    sibling_stretch_id INT,
    PRIMARY KEY (id)
);

CREATE TABLE muscle_group_2_exercise (
    id INT NOT NULL AUTO_INCREMENT,
    muscle_group_id INT NOT NULL,
    exercise_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (muscle_group_id) REFERENCES muscle_group(id),
    FOREIGN KEY (exercise_id) REFERENCES exercise(id)
);

CREATE TABLE muscle_group_2_warmup (
    id INT NOT NULL AUTO_INCREMENT,
    muscle_group_id INT NOT NULL,
    warmup_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (muscle_group_id) REFERENCES muscle_group(id),
    FOREIGN KEY (warmup_id) REFERENCES warmup(id)
);

CREATE TABLE muscle_group_2_stretch (
    id INT NOT NULL AUTO_INCREMENT,
    muscle_group_id INT NOT NULL,
    stretch_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (muscle_group_id) REFERENCES muscle_group(id),
    FOREIGN KEY (stretch_id) REFERENCES stretch(id)
);

CREATE TABLE training_session (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    date DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE training_session_2_exercise (
    id INT NOT NULL AUTO_INCREMENT,
    training_session_id INT NOT NULL,
    exercise_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (training_session_id) REFERENCES training_session(id),
    FOREIGN KEY (exercise_id) REFERENCES exercise(id)
);

INSERT INTO users
    (username, password, sex, height, weight, age)
VALUES
    ('admin', 'admin', 'female', 165, 55, 25),
    ('hans', 'hans', 'male', 175, 75, 20);

INSERT INTO muscle_group
    (name, image_path)
VALUES
    ('Abs', 'abs.jpg'),
    ('Arms', 'arms.jpg'),
    ('Legs', 'legs.jpg'),
    ('Back and Shoulders', 'back_shoulders.jpg'),
    ('Chest', 'chest.jpg');

-- Abs warmups
INSERT INTO warmup
    (name, MET, duration, repetitions, video_path, sibling_warmup_id)
VALUES
    ('Jumping Jacks', 2.0, 30.0, 1, 'jump-jacks.mp4', null);

-- Abs exercises
INSERT INTO exercise
    (name, MET, duration, repetitions, video_path, sibling_exercise_id)
VALUES
    ('Side planks left', 5.0, 20.0, 1, 'side-planks-left.mp4', 2),
    ('Side planks right', 5.0, 20.0, 1, 'side-planks-right.mp4', 1),
    ('Side bridges left', 5, 3, 20, 'side-bridges-left.mp4', 3),
    ('Side bridges right', 5, 3, 20, 'side-bridges-right.mp4', 4),
    ('Sit-ups', 5.0, 3.0, 20, 'sit-ups.mp4', null),
    ('Abdominal crunches', 6.0, 3.0, 30, 'abdominal-crunches.mp4', null),
    ('Bicycle crunches', 6, 3, 24, 'push-ups.mp4', null),
    ('V-up', 7, 3, 18, 'v-up.mp4', null),
    ('Push-up & rotation', 8, 3, 24, 'push-up-and-rotation.mp4', null),
    ('Russian Twist', 6, 2, 48, 'russian-twist.mp4', null),
    ('Butt Bridge', 5, 3, 30, 'butt-bridge.mp4', null),
    ('Heel touch', 6, 2, 34, 'heel-touch.mp4', null),
    ('Mountain climber', 5, 1, 30, 'mountain-climber.mp4', null),
    ('Crossover crunch', 6, 3, 30, 'crossover-crunch.mp4', null);

-- Abs stretches
INSERT INTO stretch
    (name, MET, duration, repetitions, video_path, sibling_stretch_id)
VALUES
    ('Spine Lumber twist left', 6, 30, 1, 'spine-lumber-twist-left.mp4', 2),
    ('Spine Lumber twist right', 6, 30, 1, 'spine-lumber-twist-right.mp4', 1),
    ('Cobra Stretch', 7, 30, 1, 'cobra-stretch.mp4', false);

-- Abs muscle group exercises
INSERT INTO muscle_group_2_exercise
    (muscle_group_id, exercise_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (1, 9),
    (1, 10),
    (1, 11),
    (1, 12);

-- Abs muscle group warmups
INSERT INTO muscle_group_2_warmup
    (muscle_group_id, warmup_id)
VALUES
    (1, 1);

-- Abs muscle group stretches
INSERT INTO muscle_group_2_stretch
    (muscle_group_id, stretch_id)
VALUES
    (1, 1),
    (1, 2);



