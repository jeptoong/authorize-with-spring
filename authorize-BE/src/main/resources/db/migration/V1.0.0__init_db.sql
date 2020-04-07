CREATE DATABASE IF NOT EXISTS `authorize_demo` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `authorize_demo`;

--
-- Table structure for table `account`
--
DROP TABLE IF EXISTS `account`;

CREATE TABLE `account`
(
    `id`                 varchar(32)  NOT NULL,
    `first_name`         varchar(255) NOT NULL,
    `last_name`          varchar(255) NOT NULL,
    `title`              varchar(255) NULL,
    `date_of_birth`      date         NULL,
    `avatar_url`         text         NULL,
    `house_address`      text         NULL,
    `work_address`       text         NULL,
    `notification_token` text         NULL,
    `language`           varchar(2)   NULL,
    `principal_id`       varchar(32)  NULL,
    `is_delete`          tinyint(1)   NULL DEFAULT '0',
    `is_active`          tinyint(1)   NULL DEFAULT '0',
    `create_by`          varchar(32)  NULL,
    `create_at`          timestamp    NULL,
    `update_by`          varchar(32)  NULL,
    `update_at`          timestamp    NULL,
    PRIMARY KEY (id)
#     foreign key (role_id) references role (id) on update restrict on delete restrict,
#     foreign key (establishment_id) references establishment (id) on update restrict on delete restrict
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

--
-- Table structure for table `auth_method`
--

CREATE TABLE `auth_method`
(
    `id`           varchar(32)                            NOT NULL,
    `auth_type`    enum ('EMAIL_PASSWORD','PHONE_NUMBER') NOT NULL,
    `auth_data_1`  text,
    `auth_data_2`  text,
    `auth_data_3`  text,
    `principal_id` int(11)                                NOT NULL,
    PRIMARY KEY (`id`),
    KEY `principal_id` (`principal_id`),
    CONSTRAINT `auth_method_ibfk_1` FOREIGN KEY (`principal_id`) REFERENCES `principal` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  AUTO_INCREMENT = 60
  DEFAULT CHARSET = utf8;