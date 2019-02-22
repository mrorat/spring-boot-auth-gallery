CREATE TABLE `album` (
  `id` varchar(32) NOT NULL,
  `name` varchar(200) NOT NULL,
  `path` varchar(500) NOT NULL,
  `createdDate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `lastRefreshed` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `gallery`.`album_permissions` (
  `userid` VARCHAR(36) NOT NULL,
  `albumid` VARCHAR(36) NOT NULL,
  `created_date` TIMESTAMP NOT NULL DEFAULT current_timestamp(),
  `deleted_date` TIMESTAMP NULL,
  PRIMARY KEY (`userid`, `albumid`),
  INDEX `fk_album_perm_album_idx` (`albumid` ASC),
  CONSTRAINT `fk_album_perm_user`
    FOREIGN KEY (`userid`)
    REFERENCES `gallery`.`user` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_album_perm_album`
    FOREIGN KEY (`albumid`)
    REFERENCES `gallery`.`album` (`albumid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

  CREATE TABLE `gallery`.`login_audit` (
  `loginauditid` INT NOT NULL AUTO_INCREMENT,
  `userid` VARCHAR(36) NULL,
  `created_date` VARCHAR(45) NOT NULL,
  `outcome` ENUM('SUCCESS', 'INVALID_PASSWORD', 'ACCOUNT_LOCKED', 'ACCOUNT_DELETED', 'IP_BLACKLISTED', 'USER_UNKNOWN') NOT NULL,
  PRIMARY KEY (`loginauditid`),
  INDEX `loginaudit_user_fk_idx` (`userid` ASC),
  CONSTRAINT `loginaudit_user_fk`
    FOREIGN KEY (`userid`)
    REFERENCES `gallery`.`user` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);