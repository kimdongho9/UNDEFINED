SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS feed_comments;
DROP TABLE IF EXISTS feed_likes;
DROP TABLE IF EXISTS feed_photos;
DROP TABLE IF EXISTS feed_tag_table;
DROP TABLE IF EXISTS feeds;
DROP TABLE IF EXISTS feed_tags;


/* Create Tables */

CREATE TABLE feeds
(
    feed_id int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    feed_title varchar(200) NOT NULL,
    feed_content text NOT NULL,
    feed_state varchar(50) NOT NULL check (feed_state in ('comp', 'temp', 'del')),
    feed_regdate datetime default now(),
    PRIMARY KEY (feed_id)
);


CREATE TABLE feed_comments
(
    comment_id int NOT NULL AUTO_INCREMENT,
    feed_id int NOT NULL,
    user_id int NOT NULL,
    parent_id int,
    content varchar(300) NOT NULL,
    regdate datetime default now(),
    PRIMARY KEY (comment_id)
);


CREATE TABLE feed_likes
(
    user_id int NOT NULL,
    feed_id int NOT NULL,
    PRIMARY KEY (user_id, feed_id)
);


CREATE TABLE feed_photos
(
    photo_id int NOT NULL AUTO_INCREMENT,
    feed_id int NOT NULL,
    source_name varchar(1000) NOT NULL,
    file_name varchar(1000) NOT NULL,
    PRIMARY KEY (photo_id)
);


CREATE TABLE feed_tags
(
    tag_id int NOT NULL AUTO_INCREMENT,
    tag_name varchar(200) NOT NULL,
    PRIMARY KEY (tag_id)
);


CREATE TABLE feed_tag_table
(
    tag_id int NOT NULL,
    feed_id int NOT NULL,
    PRIMARY KEY (tag_id, feed_id)
);


/* Create Foreign Keys */

ALTER TABLE feeds
    ADD FOREIGN KEY (user_id)
        REFERENCES user (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;

ALTER TABLE feed_comments
    ADD FOREIGN KEY (feed_id)
        REFERENCES feeds (feed_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;


ALTER TABLE feed_likes
    ADD FOREIGN KEY (feed_id)
        REFERENCES feeds (feed_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;


ALTER TABLE feed_photos
    ADD FOREIGN KEY (feed_id)
        REFERENCES feeds (feed_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;


ALTER TABLE feed_tag_table
    ADD FOREIGN KEY (feed_id)
        REFERENCES feeds (feed_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;


ALTER TABLE feed_comments
    ADD FOREIGN KEY (parent_id)
        REFERENCES feed_comments (comment_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;


ALTER TABLE feed_tag_table
    ADD FOREIGN KEY (tag_id)
        REFERENCES feed_tags (tag_id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;


ALTER TABLE feed_comments
    ADD FOREIGN KEY (user_id)
        REFERENCES user (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;


ALTER TABLE feed_likes
    ADD FOREIGN KEY (user_id)
        REFERENCES user (id)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
;


# 샘플
DELETE FROM feeds ;
ALTER TABLE feeds
    AUTO_INCREMENT = 1;
INSERT INTO feeds (user_id, feed_title, feed_content, feed_state)
VALUES (1, 'title1', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title2', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'temp'),
       (1, 'title3', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (2, 'title4', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (3, 'title5', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'del'),
       (1, 'title1', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title2', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'temp'),
       (1, 'title3', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (2, 'title4', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (3, 'title5', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'del'),
       (1, 'title1', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title2', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'temp'),
       (1, 'title3', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (2, 'title4', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (3, 'title5', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'del'),
       (1, 'title1', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title2', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'temp'),
       (1, 'title3', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (2, 'title4', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (3, 'title5', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'del'),
       (1, 'title1', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title2', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'temp'),
       (1, 'title3', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (2, 'title4', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (3, 'title5', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'del'),
       (1, 'title1', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title2', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'temp'),
       (1, 'title3', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (2, 'title4', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (3, 'title5', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'del'),
       (1, 'title1', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title2', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'temp'),
       (1, 'title3', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (2, 'title4', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title5', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'del'),
       (1, 'title1', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title2', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'temp'),
       (1, 'title3', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (2, 'title4', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'comp'),
       (1, 'title5', 'Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi beatae corporis dignissimos dolores dolorum id incidunt ipsa ipsam iure maiores, minima, nobis perferendis perspiciatis provident sint veniam vitae! Eum, voluptatibus.', 'del');



DELETE FROM feed_tags;
ALTER TABLE feed_tags
    AUTO_INCREMENT = 1;
INSERT INTO feed_tags (tag_name)
VALUES ('spring'), ('java'), ('security');


DELETE FROM feed_tag_table;
INSERT INTO feed_tag_table (tag_id, feed_id)
VALUES (1, 1), (2, 1), (1, 3), (3, 3), (1, 4), (2, 4), (3, 4);


DELETE FROM feed_comments;
ALTER TABLE feed_comments
    AUTO_INCREMENT = 1;
INSERT INTO feed_comments (feed_id, user_id, parent_id, content)
VALUES (1, 1, null, 'comment1'),
       (1, 2, 1, 'comment1-1'),
       (1, 3, 1, 'comment1-2'),
       (1, 2, null, 'comment2'),
       (3, 1, null, 'comment1'),
       (3, 2, null, 'comment2'),
       (3, 3, null, 'comment3'),
       (3, 1, 7, 'comment3-1');


DELETE FROM feed_likes;
INSERT INTO feed_likes (user_id, feed_id)
VALUES (1, 1),
       (1, 3),
       (1, 4),
       (2, 1),
       (2, 3);

