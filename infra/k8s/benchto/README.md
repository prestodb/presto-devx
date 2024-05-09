## MySQL - Benchto
```
CREATE TABLE `benchto_runs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `run_name` varchar(255) NOT NULL,
  `user_name` varchar(64) NOT NULL,
  `prestodb_branch` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `build_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `cluster_size` varchar(15) NOT NULL,
  `workload_type` varchar(15) NOT NULL,
  `workload_size` varchar(10) NOT NULL,
  `workload_dataset` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `is_prestissimo` tinyint(1) NOT NULL,
  `started` timestamp NOT NULL,
  `ended` timestamp NULL DEFAULT NULL,
  `status` varchar(10) NOT NULL,
  `jenkins_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `baseline_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `run_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `total_avg_query_wall_time_ms` bigint DEFAULT NULL COMMENT 'Total average (per query) wall time from start to end taken by a query (includes both queuing and execution)',
  `total_avg_query_execution_time_ms` bigint DEFAULT NULL COMMENT 'Total average (per query) wall time that the query spent executing (does not include queue time)',
  `total_avg_input_bytes` bigint DEFAULT NULL COMMENT 'Total average (per query) input bytes read by the query',
  `total_avg_output_bytes` bigint DEFAULT NULL COMMENT 'Total average (per query) bytes returned back to the client',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=414 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `environments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `version` bigint NOT NULL,
  `started` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uk_environments_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `environment_attributes` (
  `environment_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(1024) NOT NULL,
  PRIMARY KEY (`environment_id`,`name`),
  CONSTRAINT `environment_attributes_ibfk_1` FOREIGN KEY (`environment_id`) REFERENCES `environments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `benchmark_runs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `unique_name` varchar(1024) NOT NULL,
  `sequence_id` varchar(64) NOT NULL,
  `started` timestamp NOT NULL,
  `ended` timestamp NULL DEFAULT NULL,
  `version` bigint NOT NULL,
  `environment_id` bigint NOT NULL,
  `status` varchar(10) NOT NULL,
  `executions_mean_duration` double NOT NULL DEFAULT '-1',
  `executions_stddev_duration` double NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uk_benchmarks_unique_name_seq_id` (`unique_name`(700),`sequence_id`),
  KEY `environment_id` (`environment_id`),
  CONSTRAINT `benchmark_runs_ibfk_1` FOREIGN KEY (`environment_id`) REFERENCES `environments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4898 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `benchmark_runs_attributes` (
  `benchmark_run_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(1024) NOT NULL,
  PRIMARY KEY (`benchmark_run_id`,`name`),
  CONSTRAINT `benchmark_runs_attributes_ibfk_1` FOREIGN KEY (`benchmark_run_id`) REFERENCES `benchmark_runs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `benchmark_runs_variables` (
  `benchmark_run_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(1024) NOT NULL,
  PRIMARY KEY (`benchmark_run_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `measurements` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `unit` varchar(16) NOT NULL,
  `value` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `benchmark_run_measurements` (
  `benchmark_run_id` bigint NOT NULL,
  `measurement_id` bigint NOT NULL,
  PRIMARY KEY (`benchmark_run_id`,`measurement_id`),
  UNIQUE KEY `idx_uk_benchmark_measurements_mes_id` (`measurement_id`),
  CONSTRAINT `benchmark_run_measurements_ibfk_1` FOREIGN KEY (`benchmark_run_id`) REFERENCES `benchmark_runs` (`id`),
  CONSTRAINT `benchmark_run_measurements_ibfk_2` FOREIGN KEY (`measurement_id`) REFERENCES `measurements` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `executions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sequence_id` varchar(64) NOT NULL,
  `benchmark_run_id` bigint NOT NULL,
  `started` timestamp NOT NULL,
  `ended` timestamp NULL DEFAULT NULL,
  `version` bigint NOT NULL,
  `status` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_executions_benchmark_run_id` (`benchmark_run_id`),
  CONSTRAINT `executions_ibfk_1` FOREIGN KEY (`benchmark_run_id`) REFERENCES `benchmark_runs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14801 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `execution_measurements` (
  `execution_id` bigint NOT NULL,
  `measurement_id` bigint NOT NULL,
  PRIMARY KEY (`execution_id`,`measurement_id`),
  UNIQUE KEY `idx_uk_execution_measurements_mes_id` (`measurement_id`),
  CONSTRAINT `execution_measurements_ibfk_1` FOREIGN KEY (`execution_id`) REFERENCES `executions` (`id`),
  CONSTRAINT `execution_measurements_ibfk_2` FOREIGN KEY (`measurement_id`) REFERENCES `measurements` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `execution_attributes` (
  `execution_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(1024) NOT NULL,
  PRIMARY KEY (`execution_id`,`name`),
  CONSTRAINT `execution_attributes_ibfk_1` FOREIGN KEY (`execution_id`) REFERENCES `executions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `schema_version` (
  `version_rank` int NOT NULL,
  `installed_rank` int NOT NULL,
  `version` varchar(50) NOT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`version`),
  KEY `schema_version_vr_idx` (`version_rank`),
  KEY `schema_version_ir_idx` (`installed_rank`),
  KEY `schema_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `tags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `created` timestamp NOT NULL,
  `environment_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tags_environment_id` (`environment_id`),
  KEY `idx_tags_created` (`created`),
  CONSTRAINT `tags_ibfk_1` FOREIGN KEY (`environment_id`) REFERENCES `environments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

INSERT INTO `schema_version` (`version_rank`, `installed_rank`, `version`, `description`, `type`, `script`, `checksum`, `installed_by`, `installed_on`, `execution_time`, `success`) VALUES
(1,	1,	'001',	'inital schema',	'SQL',	'V001__inital_schema.sql',	-1563535335,	'benchto',	'2022-06-13 08:47:30',	4572,	1),
(2,	2,	'002',	'aggregated duration measurements',	'SQL',	'V002__aggregated_duration_measurements.sql',	1288136033,	'benchto',	'2022-06-13 08:47:32',	1114,	1),
(3,	3,	'003',	'tags table',	'SQL',	'V003__tags_table.sql',	-1902219500,	'benchto',	'2022-06-13 08:47:34',	1239,	1);
```
