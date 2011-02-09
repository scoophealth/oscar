--New audit table stores hashes of casemanagement notes--
CREATE TABLE hash_audit (
    `signature` varchar(255) NOT NULL,
    `id` int(10) default 0,
    `type` char(3) NOT NULL,
    `algorithm` varchar(127),
    PRIMARY KEY (signature)
) TYPE=MyISAM;