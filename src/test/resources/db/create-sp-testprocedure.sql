USE `gallery`;
DROP procedure IF EXISTS `test_procedure`;

DELIMITER $$
USE `gallery`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `test_procedure`(out p_out_int int)
BEGIN
    SELECT 1 INTO p_out_int;
    
END$$

DELIMITER ;

