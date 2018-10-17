CREATE TABLE `album` (
  `id` varchar(32) NOT NULL,
  `name` varchar(200) NOT NULL,
  `path` varchar(500) NOT NULL,
  `createdDate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `lastRefreshed` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO album VALUES ('test', 'somename', '/mypath', NOW(), NOW());

USE `gallery`;
DROP procedure IF EXISTS `test_procedure`;

DELIMITER $$
USE `gallery`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `test_procedure`(out p_out_int int)
BEGIN
    SELECT 1 INTO p_out_int;
    
END$$

DELIMITER ;
