DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    id int NOT NULL AUTO_INCREMENT,
    username varchar(100) NOT NULL,
    password varchar(100) NOT NULL,
    name varchar(80) NOT NULL,
    email varchar(80),
    regdate datetime DEFAULT now(),
    provider varchar(40),
    providerId varchar(200),
    PRIMARY KEY (id),
    UNIQUE (username)
);
DELETE FROM user;
ALTER TABLE user AUTO_INCREMENT = 1;


INSERT INTO user (username, password, name, email)
VALUES ('USER1', '$2a$10$6gVaMy7.lbezp8bGRlV2fOArmA3WAk2EHxSKxncnzs28/m3DXPyA2', '회원1', 'user1@mail.com'),
       ('USER2', '$2a$10$7LTnvLaczZbEL0gabgqgfezQPr.xOtTab2NAF/Yt4FrvTSi0Y29Xa', '회원2', 'user2@mail.com'),
       ('ADMIN1', '$2a$10$53OEi/JukSMPr3z5RQBFH.z0TCYSUDPtxf1/8caRyRVdDNdHA9QHi', '관리자1', 'admin1@mail.com')
;


SELECT * FROM user;


DROP TABLE IF EXISTS chatbot;
CREATE TABLE chatbot
(
    chatbot_id int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    context longtext NOT NULL,
    isFromChat bit NOT NULL,
    reg_date datetime DEFAULT now(),
    PRIMARY KEY (chatbot_id)
);

DELETE FROM chatbot;
ALTER TABLE chatbot AUTO_INCREMENT = 1;

SELECT * FROM chatbot;

-- portfolio
drop table if exists portfolio;

CREATE TABLE portfolio
(
    postId int PRIMARY KEY auto_increment,
    userId int,
    content longtext NOT NULL ,
    title longtext NOT NULL,
    experience longtext,  # 경력 (내가 일한 년수)
    userPr longtext, #나를 어필할수있는 한문장
    increaseViewCount int DEFAULT 0,
    regdate datetime DEFAULT NOW()
);

DELETE FROM portfolio;
ALTER TABLE portfolio AUTO_INCREMENT = 1;

INSERT INTO portfolio( userId, content, title, experience, userPr, increaseViewCount, regdate)
VALUES
    ( 1,'나를 소개하지','내이름은 코난탐정이죠', '10년','나 노래잘해(최지은)','40',  '2023-12-13'),
    ( 2,'너를 소개하지','내이름은 코난탐정이죠', '11년','나 노래잘해(킴동호)','42',  '2023-12-14'),
    ( 3,'얘를 소개하지','내이름은 코난탐정이죠', '12년','나 노래잘해(유상곤)','43',  '2023-12-15'),
    ( 4,'자기를 소개하지','내이름은 코난탐정이죠', '13년','나 노래잘해(강준우)','44',  '2023-12-12');

SELECT * FROM portfolio;


