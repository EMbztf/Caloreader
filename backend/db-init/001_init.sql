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
    both_sides BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE warmup (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    MET DOUBLE NOT NULL,
    duration DOUBLE NOT NULL,
    repetitions INT NOT NULL,
    video_path VARCHAR(255) NOT NULL,
    both_sides BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE stretch (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    MET DOUBLE NOT NULL,
    duration DOUBLE NOT NULL,
    repetitions INT NOT NULL,
    video_path VARCHAR(255) NOT NULL,
    both_sides BOOLEAN NOT NULL,
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
    (name, MET, duration, repetitions, video_path, both_sides)
VALUES
    ('Jumping Jacks', 2.0, 30.0, 1, 'jump-jacks.mp4', false);

-- Abs exercises
INSERT INTO exercise
    (name, MET, duration, repetitions, video_path, both_sides)
VALUES
    ('Sit-ups', 5.0, 3.0, 20, 'sit-ups.mp4', false),
    ('Side planks', 5.0, 20.0, 1, 'side-planks.mp4', true),
    ('Abdominal crunches', 6.0, 3.0, 30, 'abdominal-crunches.mp4', false),
    ('Bicycle crunches', 6, 3, 24, 'push-ups.mp4', false),
    ('Side bridges', 5, 3, 20, 'side-bridges.mp4', true),
    ('V-up', 7, 3, 18, 'v-up.mp4', false),
    ('Push-up & rotation', 8, 3, 24, 'push-up-and-rotation.mp4', false),
    ('Russian Twist', 6, 2, 48, 'russian-twist.mp4', false),
    ('Butt Bridge', 5, 3, 30, 'butt-bridge.mp4', false),
    ('Heel touch', 6, 2, 34, 'heel-touch.mp4', false),
    ('Mountain climber', 5, 1, 30, 'mountain-climber.mp4', false),
    ('Crossover crunch', 6, 3, 30, 'crossover-crunch.mp4', false);

-- Abs stretches
INSERT INTO stretch
    (name, MET, duration, repetitions, video_path, both_sides)
VALUES
    ('Cobra Stretch', 7, 30, 1, 'cobra-stretch.mp4', false),
    ('Spine Lumber twist', 6, 30, 1, 'spine-lumber-twist.mp4', true);

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



