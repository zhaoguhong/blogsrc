-- 创建用户表
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 创建存储过程
CREATE PROCEDURE `test`(in userId BIGINT(20),out userName VARCHAR(200))
BEGIN
 set userName = (SELECT user_name FROM user WHERE id = userId);
END