CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `user` (`id`, `username`, `creator`, `status`, `create_time`, `update_time`) VALUES (1, 'z1', 'z1', 1, '2026-01-20 10:18:58', '2026-01-20 10:18:55');
INSERT INTO `user` (`id`, `username`, `creator`, `status`, `create_time`, `update_time`) VALUES (2, 'z2', 'z2', 1, '2026-01-20 10:18:48', '2026-01-20 10:18:52');
INSERT INTO `user` (`id`, `username`, `creator`, `status`, `create_time`, `update_time`) VALUES (3, 'z1', 'z1', 1, '2026-01-20 10:19:12', '2026-01-20 10:19:15');
