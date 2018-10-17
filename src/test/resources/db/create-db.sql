CREATE TABLE `album` (
  `id` varchar(32) NOT NULL,
  `name` varchar(200) NOT NULL,
  `path` varchar(500) NOT NULL,
  `createdDate` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `lastRefreshed` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
