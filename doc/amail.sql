SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sys_email_account`;
CREATE TABLE `sys_email_account`
(
    `id`       varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '账户id',
    `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '邮箱用户名',
    `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱密码',
    `domain`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '邮箱域名',
    `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      NULL DEFAULT '0' COMMENT '删除标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '邮箱账户表'
  ROW_FORMAT = DYNAMIC;

INSERT INTO sys_email_account(id, username, password, domain) VALUES ('1529078053739827201', 'sqltest1@test.com', 'sqltest', 'test.com');
INSERT INTO sys_email_account(id, username, password, domain) VALUES ('1529078053739827202', 'sqltest2@test.com', 'sqltest', 'test.com');


DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`       varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT '0' COMMENT '删除标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户表'
  ROW_FORMAT = DYNAMIC;

INSERT INTO sys_user(id) VALUES ('1529235917448024071');
INSERT INTO sys_user(id) VALUES ('1529235917448024072');

DROP TABLE IF EXISTS `sys_user_email`;
CREATE TABLE `sys_user_email`
(
    `user_id`  varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `email_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱账户id',
    PRIMARY KEY (`user_id`, `email_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户和邮箱关联表'
  ROW_FORMAT = DYNAMIC;

INSERT INTO sys_user_email(user_id, email_id) VALUES ('1529235917448024071', '1529078053739827201');