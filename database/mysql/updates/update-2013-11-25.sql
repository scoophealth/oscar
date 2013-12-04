CREATE TABLE `ServiceRequestToken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clientId` int(11) DEFAULT NULL,
  `tokenId` varchar(255) NOT NULL,
  `tokenSecret` varchar(255) NOT NULL,
  `callback` varchar(255) NOT NULL,
  `verifier` varchar(255) DEFAULT NULL,
  `providerNo` varchar(25) DEFAULT NULL,
  `scopes` varchar(255) DEFAULT NULL,
  `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `ServiceAccessToken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clientId` int(11) DEFAULT NULL,
  `tokenId` varchar(255) NOT NULL,
  `tokenSecret` varchar(255) NOT NULL,
  `lifetime` int(10) NOT NULL,
  `issued` int(10) NOT NULL,
  `providerNo` varchar(25) DEFAULT NULL,
  `scopes` varchar(255) DEFAULT NULL,
  `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

CREATE TABLE `ServiceClient` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `clientKey` varchar(255) NOT NULL,
  `clientSecret` varchar(255) NOT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);
