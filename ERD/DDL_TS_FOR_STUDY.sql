drop table if exists study_posts;
drop table if exists study_skills;
drop table if exists study_favor;
drop table if exists skills;

CREATE TABLE study_posts
(
    post_id int PRIMARY KEY AUTO_INCREMENT,
    user_id int NOT NULL,
    post_title varchar(200) NOT NULL,
    post_content text,
    post_viewCnt int DEFAULT 0,
    post_regDate datetime DEFAULT NOW(),
    post_enddate datetime,
    post_startdate datetime,
    post_member int NOT NULL,
    post_position varchar(200)
);

CREATE TABLE study_favor
(
    post_id int NOT NULL,
    user_id int NOT NULL
);


CREATE TABLE study_skills
(
    post_id int NOT NULL,
    skill_id int NOT NULL,
    PRIMARY KEY (post_id, skill_id)
);

CREATE TABLE skills
(
    skill_id int NOT NULL AUTO_INCREMENT,
    skill_name varchar(100) NOT NULL,
    skill_img_url varchar(1000) NOT NULL,
    PRIMARY KEY (skill_id),
    UNIQUE (skill_name)
);



DELETE FROM study_posts;
ALTER TABLE study_posts AUTO_INCREMENT = 1;
INSERT INTO study_posts (user_id, post_title, post_content, post_viewCnt, post_enddate, post_member, post_position)
VALUES
    ( 1,'자바 스터디 구해영!', '자바 스터디 구합니다!', 27, '2023-12-12', 4,'백엔드'),
    ( 1,'스프링 스터디 구해영!', '스프링 스터디 구합니다!', 109, '2023-12-10', 3,'백엔드'),
    ( 1,'css 스터디 구해영!', 'css 스터디 구합니다!', 105, '2023-12-05', 4,'백엔드'),
    ( 1,'html 스터디 구해영!', 'html 스터디 구합니다!', 105,'2023-12-07', 3,'백엔드'),
    ( 1,'html 스터디 구해영!', 'html 스터디 구합니다!', 2,'2023-12-07', 3,'백엔드'),
    ( 1,'html 스터디 구해영!', 'html 스터디 구합니다!', 1,'2023-12-07', 3,'백엔드'),
    ( 1,'html 스터디 구해영!', 'html 스터디 구합니다!', 1,'2023-12-07', 3,'백엔드'),
    ( 1,'html 스터디 구해영!', 'html 스터디 구합니다!', 1,'2023-12-07', 3,'프론트엔드');
SELECT * FROM study_posts;


DELETE FROM study_favor;
INSERT INTO study_favor (post_id, user_id)
    VALUE (17, 2);


DELETE FROM skills;
INSERT INTO skills(skill_id, skill_name, skill_img_url)
VALUES
    (1,'C','<i class="fa-solid fa-c"></i>'),
    (2,'Java','<i class="fa-brands fa-java"></i>'),
    (3,'Javascript','<i class="fa-brands fa-js"></i>'),
    (4,'Python','<i class="fa-brands fa-python"></i>'),
    (5,'React.js','<i class="fa-brands fa-react"></i>'),
    (6,'Node.js','<i class="fa-brands fa-node-js"></i>');

SELECT * FROM skills;



DELETE FROM study_skills;
INSERT INTO study_skills(post_id, skill_id)
VALUES
    (1,1),
    (2,2),
    (3,3),
    (4,4),
    (5,5),
    (6,6),
    (7,7);
SELECT * FROM study_skills;