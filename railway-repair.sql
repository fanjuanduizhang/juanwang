-- ============================================================
-- SurveyKing Railway MySQL 修复脚本
-- 用途：补建缺失的核心表，恢复 admin 登录功能
-- 执行位置：Railway MySQL 控制台 → Database → Data → 查询框
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 组织机构表（t_user 的 dept_id 引用此表）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_dept` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `parent_id` varchar(64) NOT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `short_name` varchar(64) NOT NULL COMMENT '简称',
  `code` varchar(64) DEFAULT NULL COMMENT '数据权限类型',
  `manager_id` varchar(64) DEFAULT NULL COMMENT '扩展字段',
  `sort_code` int DEFAULT NULL,
  `property_json` varchar(256) DEFAULT NULL COMMENT '扩展字段',
  `status` varchar(20) DEFAULT NULL COMMENT '扩展字段',
  `remark` varchar(256) DEFAULT NULL COMMENT '扩展字段',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='组织机构';

INSERT IGNORE INTO `t_dept` (`id`, `parent_id`, `name`, `short_name`, `code`, `manager_id`, `sort_code`, `property_json`, `status`, `remark`, `is_deleted`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES ('1', '0', '卷王问卷', 'surveyking', 'surveyking', '1457995481966747649', NULL, NULL, NULL, NULL, 0, '2021-11-21 14:12:08', NULL, '2021-11-21 14:22:58', '1457995481966747649');

-- ----------------------------
-- 2. 账号表（登录凭据，最关键！）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_account` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `user_type` varchar(100) NOT NULL DEFAULT 'SysUser' COMMENT '用户类型',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `auth_type` varchar(20) NOT NULL DEFAULT 'PWD' COMMENT '认证方式',
  `auth_account` varchar(100) NOT NULL COMMENT '登录账号',
  `auth_secret` varchar(255) DEFAULT NULL COMMENT '密码(BCrypt)',
  `secret_salt` varchar(32) DEFAULT NULL COMMENT '加密盐值',
  `status` int NOT NULL DEFAULT '1' COMMENT '用户状态',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_auth_account` (`auth_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录账号';

-- admin / 123456 (BCrypt加密)
INSERT IGNORE INTO `t_account` (`id`, `user_type`, `user_id`, `auth_type`, `auth_account`, `auth_secret`, `secret_salt`, `status`, `is_deleted`, `create_at`, `create_by`, `update_at`, `update_by`) VALUES ('1', 'SysUser', '1457995481966747649', 'PWD', 'admin', '$2a$10$vZk9P3XtbD2KrdLbQYPvBuPAkkUda0OlkDg7io1Q6VEtfFPig/tqO', NULL, 1, 0, '2021-11-09 16:56:26', NULL, '2022-02-01 23:57:27', '1457995481966747649');

-- ----------------------------
-- 3. 用户表（管理员信息）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '真实姓名',
  `dept_id` varchar(20) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT 'Email',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户状态',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(256) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL COMMENT '个人简介',
  `correct_times` int DEFAULT NULL COMMENT '错题答对清除次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户';

INSERT IGNORE INTO `t_user` (`id`, `name`, `dept_id`, `gender`, `birthday`, `phone`, `email`, `avatar`, `status`, `is_deleted`, `create_at`, `create_by`, `update_at`, `update_by`, `profile`, `correct_times`) VALUES ('1457995481966747649', 'Admin', '1', 'F', NULL, '13800138000', 'surveyking@qq.com', NULL, 1, 0, '2021-11-09 16:56:26', NULL, '2022-02-11 13:29:17', '1457995481966747649', 'hello surveyking~', NULL);

-- ----------------------------
-- 4. 系统信息表（系统配置 + 开启注册）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_sys_info` (
  `id` varchar(64) NOT NULL,
  `sys_name` varchar(128) DEFAULT NULL COMMENT '系统名称',
  `description` varchar(512) DEFAULT NULL COMMENT '描述',
  `avatar` varchar(256) DEFAULT NULL,
  `locale` varchar(32) DEFAULT NULL,
  `version` varchar(64) DEFAULT NULL,
  `setting` varchar(2048) DEFAULT NULL COMMENT '其他设置(JSON)',
  `ai_setting` varchar(2048) DEFAULT NULL COMMENT 'AI设置(JSON)',
  `register_info` varchar(1024) DEFAULT NULL COMMENT '注册配置(JSON)',
  `is_default` tinyint(1) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 插入默认系统信息（开启注册功能）
INSERT IGNORE INTO `t_sys_info` (`id`, `sys_name`, `description`, `register_info`, `is_deleted`, `create_at`, `create_by`) VALUES (
  '1', 'SurveyKing', '问卷考试系统',
  '{"registerEnabled":true,"roles":[],"strongPasswordEnabled":false}',
  0, NOW(), NULL
);

-- ----------------------------
-- 5. 问答记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_answer` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `temp_answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `survey` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `answer` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `attachment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `meta_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `temp_save` int DEFAULT NULL,
  `exam_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `exam_exercise_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `exam_score` decimal(10,2) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `repo_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- 6. 字典项表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_comm_dict_item` (
  `id` varchar(64) NOT NULL,
  `dict_code` varchar(64) NOT NULL,
  `item_name` varchar(128) DEFAULT NULL,
  `item_value` varchar(128) DEFAULT NULL,
  `item_order` int DEFAULT NULL,
  `item_level` int DEFAULT NULL,
  `parent_item_value` varchar(64) DEFAULT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 7. 文件表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_file` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `file_name` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `file_path` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `file_url` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `file_size` bigint DEFAULT NULL,
  `file_ext` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `file_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `model_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- 8. 项目表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_project` (
  `id` varchar(64) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `project_code` varchar(64) DEFAULT NULL,
  `directory_id` varchar(64) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 9. 项目协作者表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_project_partner` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  `project_id` varchar(64) NOT NULL,
  `permission` int DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 10. 题库表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_repo` (
  `id` varchar(64) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `remark` text,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 11. 题库协作者表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_repo_partner` (
  `id` varchar(64) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  `repo_id` varchar(64) NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 12. 题库模板表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_repo_template` (
  `id` varchar(64) NOT NULL,
  `repo_id` varchar(64) DEFAULT NULL,
  `serial_no` int DEFAULT NULL,
  `name` varchar(1024) DEFAULT NULL,
  `question_type` varchar(32) DEFAULT NULL,
  `template` longtext,
  `mode` varchar(32) DEFAULT NULL,
  `category` varchar(128) DEFAULT NULL,
  `tag` varchar(256) DEFAULT NULL,
  `priority` int DEFAULT NULL,
  `preview_url` varchar(1024) DEFAULT NULL,
  `shared` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 13. 职位表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_position` (
  `id` varchar(64) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `code` varchar(64) DEFAULT NULL,
  `dept_id` varchar(64) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 14. 标签表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_tag` (
  `id` varchar(64) NOT NULL,
  `tag_name` varchar(128) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 15. 问卷模板表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_template` (
  `id` varchar(64) NOT NULL,
  `repo_id` varchar(64) DEFAULT NULL,
  `serial_no` int DEFAULT NULL,
  `name` varchar(1024) DEFAULT NULL,
  `question_type` varchar(32) DEFAULT NULL,
  `template` longtext,
  `mode` varchar(32) DEFAULT NULL,
  `category` varchar(128) DEFAULT NULL,
  `tag` varchar(256) DEFAULT NULL,
  `priority` int DEFAULT NULL,
  `preview_url` varchar(1024) DEFAULT NULL,
  `shared` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- 16. 错题本表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_user_book` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `name` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `template_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `wrong_times` int DEFAULT NULL,
  `correct_times` int DEFAULT NULL,
  `note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin,
  `status` int DEFAULT NULL,
  `type` int DEFAULT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `repo_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `is_marked` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- 17. 仪表盘表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_dashboard` (
  `id` varchar(64) NOT NULL,
  `key` varchar(256) NOT NULL,
  `type` int DEFAULT NULL,
  `project_id` varchar(64) DEFAULT NULL,
  `setting` varchar(1024) DEFAULT NULL,
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(256) DEFAULT NULL,
  `update_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 执行完成！现在可以用 admin / 123456 登录了。
-- ============================================================
