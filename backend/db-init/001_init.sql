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
      warmup bit default 0 NOT NULL,
      stretch bit default 0 NOT NULL,
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

CREATE TABLE training_session (
      id INT NOT NULL AUTO_INCREMENT,
      name VARCHAR(255) NOT NULL,
      estimated_duration DOUBLE NOT NULL,
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

INSERT INTO exercise
(name, MET, duration, repetitions, video_path, sibling_exercise_id, warmup, stretch)
VALUES
    -- Abs warmups
    ('Jumping Jacks', 5.0, 30.0, 1, 'jump-jacks.mp4', null, true, false),
    -- Abs exercises 14 exercises
    ('Side planks left', 5.0, 20.0, 1, 'side-planks-left.mp4', 3, false, false),
    ('Side planks right', 5.0, 20.0, 1, 'side-planks-right.mp4', 2, false, false),
    ('Side bridges left', 5, 3, 20, 'side-bridges-left.mp4', 5, false, false),
    ('Side bridges right', 5, 3, 20, 'side-bridges-right.mp4', 4, false, false),
    ('Sit-ups', 5.0, 3.0, 20, 'sit-ups.mp4', null, false, false),
    ('Abdominal crunches', 6.0, 3.0, 30, 'abdominal-crunches.mp4', null, false, false),
    ('Bicycle crunches', 6, 3, 24, 'push-ups.mp4', null, false, false),
    ('V-up', 7, 3, 18, 'v-up.mp4', null, false, false),
    ('Push-up & rotation', 8, 3, 24, 'push-up-and-rotation.mp4', null, false, false),
    ('Russian Twist', 6, 2, 48, 'russian-twist.mp4', null, false, false),
    ('Butt Bridge', 5, 3, 30, 'butt-bridge.mp4', null, false, false),
    ('Heel touch', 6, 2, 34, 'heel-touch.mp4', null, false, false),
    ('Mountain climber', 5, 1, 30, 'mountain-climber.mp4', null, false, false),
    ('Crossover crunch', 6, 3, 30, 'crossover-crunch.mp4', null, false, false),
    -- Abs stretches
    ('Spine Lumber twist left', 6, 30, 1, 'spine-lumber-twist-left.mp4', 17, false, true),
    ('Spine Lumber twist right', 6, 30, 1, 'spine-lumber-twist-right.mp4', 16, false, true),
    ('Cobra Stretch', 7, 30, 1, 'cobra-stretch.mp4', null, false, true), -- 18
    -- Arms warmups 3 exercises
    ('Arms circles clockwise', 2.0, 30.0, 1, 'arms-circles-clockwise.mp4', 20, true, false),
    ('Arms circles counter-clockwise', 2.0, 30.0, 1, 'arms-circles-counter-clockwise.mp4', 19, true, false),
    ('Skipping without rope', 2.0, 30.0, 1, 'skipping-without-rope.mp4', null, true, false), -- 21
    -- Arms exercises 10 exercises
    ('Leg Barbell curl left', 6.0, 2.0, 16, 'leg-barbell-curl-left.mp4', 23, false, false),
    ('Leg Barbell curl right', 6.0, 2.0, 16, 'leg-barbell-curl-right.mp4', 22, false, false),
    ('Burpees', 9.0, 3.0, 16 , 'burpees.mp4', null, false, false),
    ('Military push-ups', 8, 3.0, 16, 'military-push-ups.mp4', null, false, false),
    ('Triceps dips', 6, 3.0, 16, 'floor-tricep-dips.mp4', null, false, false),
    ('Diamond push-ups', 8, 3.0, 16, 'diamond-push-ups.mp4', null, false, false),
    ('Push-up hold', 5, 30, 1, 'push-up-hold.mp4', null, false, false),
    ('Push-up & Rotation', 8, 3.0, 16, 'push-up-and-rotation.mp4', null, false, false),
    ('Decline push-ups', 9, 3.0, 16, 'decline-push-ups.mp4', null, false, false),
    ('Incline push-ups', 7, 3.0, 16, 'incline-push-ups.mp4', null, false, false), -- 31
    -- Arms stretches 4 exercises
    ('Triceps Stretch left', 2, 30, 1, 'triceps-stretch.mp4', 33, false, true),
    ('Triceps Stretch right', 2, 30, 1, 'triceps-stretch.mp4', 32, false, true),
    ('Standing Biceps Stretch left', 2, 30, 1, 'standing-biceps-stretch.mp4', 31, false, true),
    ('Standing Biceps Stretch right', 2, 30, 1, 'standing-biceps-stretch.mp4', 30, false, true), -- 35
    -- Legs warmups 1 exercise
    ('Side hops', 5, 30, 1, 'side-hops.mp4', null, true, false), -- 36
    -- Legs exercises 12 exercises
    ('Bottom leg lift left', 5, 3, 16, 'bottom-leg-lift-left.mp4', 38, false, false),
    ('Bottom leg lift right', 5, 3, 16, 'bottom-leg-lift-right.mp4', 37, false, false),
    ('Side Leg circles left', 5, 3, 16, 'side-leg-circles-left.mp4', 40, false, false),
    ('Side Leg circles right', 5, 3, 16, 'side-leg-circles-right.mp4', 39, false, false),
    ('Squat jumps', 8, 3, 16, 'squat-jumps.mp4', null, false, false),
    ('Squats', 7, 3, 16, 'squats.mp4', null, false, false),
    ('Lunges', 7, 3, 16, 'lunges.mp4', null, false, false),
    ('Calf Raises', 6, 2, 16, 'calf-raises.mp4', null, false, false),
    ('Sumo Squat', 7, 3, 16, 'sumo-squat.mp4', null, false, false),
    ('Wall Sit', 5, 30, 1, 'wall-sit.mp4', null, false, false),
    ('Glute Bridge', 5, 30, 1, 'glute-bridge.mp4', null, false, false),
    ('Curtsy Lunges', 7, 3, 16, 'curtsy-lunges.mp4', null, false, false), -- 48
    -- Legs stretches 4 exercises
    ('Left Quad Stretch', 2, 30, 1, 'right-quad-stretch.mp4', 50, false, true),
    ('Right Quad Stretch ', 2, 30, 1, 'right-quad-stretch.mp4', 49, false, true),
    ('Calf Stretch left', 2, 30, 1, 'calf-stretch.mp4', 48, false, true),
    ('Calf Stretch right', 2, 30, 1, 'calf-stretch.mp4', 47, false, true), -- 52
    -- Back & Shoulders warmups 1 exercises
    ('Jumping Jacks', 5, 30, 1, 'jumping-jacks.mp4', null, true, false), -- 53
    -- Back & Shoulders exercises 10 exercises
    ('Hyperextension', 6, 3, 14, 'hyperextension.mp4', null, false, false),
    ('Pike Push-ups', 9, 3, 16, 'pike-push-ups.mp4', null, false, false),
    ('Reverse push-ups', 8, 3, 16, 'reverse-push-ups.mp4', null, false, false),
    ('Inchworms', 8, 3, 16, 'inchworms.mp4', null, false, false),
    ('Floor Y Raises', 7, 3, 16, 'floor-y-raises.mp4', null, false, false),
    ('Floor W Raises', 7, 3, 16, 'floor-w-raises.mp4', null, false, false),
    ('Fllor T Raises', 7, 3, 16, 'floor-t-raises.mp4', null, false, false),
    ('Push-ups', 7, 3, 16, 'push-ups.mp4', null, false, false),
    ('Triceps dips', 6, 3.0, 16, 'floor-tricep-dips.mp4', null, false, false),
    ('Hover push-up', 7, 3, 16, 'hover-push-ups.mp4', null, false, false), -- 63
    -- Back & Shoulders stretches 1 exercises
    ('Childs Pose', 2, 30, 1, 'childs-pose.mp4', null, false, true), -- 64
    -- Chest warmups 2 exercises
    ('Jumping Jacks', 5, 30, 1, 'jumping-jacks.mp4', null, true, false),
    ('Arms circles', 2, 30, 1, 'arms-circles.mp4', null, true, false), -- 66
    -- Chest exercises 11 exercises
    ('Push-ups', 7, 3, 16, 'push-ups.mp4', null, false, false),
    ('Knee push-ups', 5, 3, 16, 'knee-push-ups.mp4', null, false, false),
    ('Wide arm push-ups', 8, 3, 16, 'wide-arm-push-ups.mp4', null, false, false),
    ('Decline push-ups', 9, 3.0, 16, 'decline-push-ups.mp4', null, false, false),
    ('Hindu push-ups', 8, 3, 16, 'hindu-push-ups.mp4', null, false, false),
    ('Staggered push-ups', 8, 3, 16, 'staggered-push-ups.mp4', null, false, false),
    ('Spiderman push-ups', 8, 3, 16, 'spiderman-push-ups.mp4', null, false, false),
    ('Diamond push-ups', 8, 3.0, 16, 'diamond-push-ups.mp4', null, false, false),
    ('Push-up & rotations', 8, 3, 16, 'push-up-rotations.mp4', null, false, false),
    ('Box push-ups', 8, 3, 16, 'box-push-ups.mp4', null, false, false),
    ('Burpees', 9.0, 3.0, 16 , 'burpees.mp4', null, false, false), -- 77
    -- Chest stretches 3 exercises
    ('Shoulders Stretch', 2, 30, 1, 'shoulders-stretch.mp4', null, false, true),
    ('Cobra Stretch', 2, 30, 1, 'cobra-stretch.mp4', null, false, true),
    ('Chest Stretch', 2, 30, 1, 'chest-stretch.mp4', null, false, true); -- 80

INSERT INTO muscle_group_2_exercise
(muscle_group_id, exercise_id)
VALUES
    -- Abs muscle group exercises
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
    (1, 12),
    (1, 13),
    (1, 14),
    (1, 15),
    (1, 16),
    (1, 17),
    (1, 18),
    -- Arms muscle group exercises
    (2, 19),
    (2, 20),
    (2, 21),
    (2, 22),
    (2, 23),
    (2, 24),
    (2, 25),
    (2, 26),
    (2, 27),
    (2, 28),
    (2, 29),
    (2, 30),
    (2, 31),
    (2, 32),
    (2, 33),
    (2, 34),
    (2, 35),
    -- Legs muscle group exercises
    (3, 36),
    (3, 37),
    (3, 38),
    (3, 39),
    (3, 40),
    (3, 41),
    (3, 42),
    (3, 43),
    (3, 44),
    (3, 45),
    (3, 46),
    (3, 47),
    (3, 48),
    (3, 49),
    (3, 50),
    (3, 51),
    (3, 52),
    -- Back & shoulders muscle group exercises
    (4, 53),
    (4, 54),
    (4, 55),
    (4, 56),
    (4, 57),
    (4, 58),
    (4, 59),
    (4, 60),
    (4, 61),
    (4, 62),
    (4, 63),
    -- Chest muscle group exercises
    (5, 64),
    (5, 65),
    (5, 66),
    (5, 67),
    (5, 68),
    (5, 69),
    (5, 70),
    (5, 71),
    (5, 72),
    (5, 73),
    (5, 74),
    (5, 75),
    (5, 76),
    (5, 77),
    (5, 78),
    (5, 79),
    (5, 80);


