alter table intake_answer_element add label varchar(255);

DROP TABLE IF EXISTS `intake_node_js`;
CREATE TABLE `intake_node_js` (
        `id` integer not null auto_increment,
        `question_id` varchar(255) not null,
        `location` varchar(255) not null,
        primary key(id)
);

