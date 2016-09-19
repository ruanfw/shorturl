#创建数据库
CREATE DATABASE shorturl;

USE shorturl;

#短链接表
CREATE TABLE `short_url` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmtCreated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `gmtModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `account` varchar(64) DEFAULT NULL,
  `shortUrlIndex` varchar(255) DEFAULT NULL,
  `url` varchar(1024) DEFAULT NULL,
  `accountSource` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_su_url` (`url`(255)),
  KEY `idx_su_account` (`account`),
  KEY `idx_su_shortUrlIndex` (`shortUrlIndex`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;


#访问记录表
CREATE TABLE `visitor_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmtCreated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `gmtModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `visitorTime` bigint(20) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `shortUrlIndex` varchar(255) DEFAULT NULL,
  `realUrl` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_vl_shortUrlIndex_visitorTime` (`shortUrlIndex`,`visitorTime`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;


