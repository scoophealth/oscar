CREATE TABLE IF NOT EXISTS `OscarJob` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255),
    `description` VARCHAR(255),
    `oscarJobTypeId` INTEGER,
    `cronExpression` VARCHAR(255),
    `providerNo` VARCHAR(10),
    `enabled` TINYINT(1) NOT NULL,
    `updated` DATETIME NOT NULL
);


CREATE TABLE IF NOT EXISTS `OscarJobType` (
    `id` int AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255),
    `description` VARCHAR(255),
    `className` VARCHAR(255),
    `enabled` TINYINT(1) NOT NULL,
    `updated` DATETIME NOT NULL
);

