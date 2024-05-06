-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 03, 2024 at 09:13 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `viecoaching`
--

-- --------------------------------------------------------

--
-- Table structure for table `categorie`
--

CREATE TABLE `categorie` (
  `id` int(11) NOT NULL,
  `ressource_id` int(11) DEFAULT NULL,
  `nom_categorie` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `categorie`
--

INSERT INTO `categorie` (`id`, `ressource_id`, `nom_categorie`, `description`, `image`) VALUES
(1, 1, 'test', 'test', '420734711-206630509201239-9139645037034586170-n-65e9b06b4cfac.png'),
(2, 3, 'Carrière et Leadership', '1.	Cours en ligne sur les compétences professionnelles : Des programmes de formation pour améliorer les compétences techniques et relationnelles essentielles à la réussite professionnelle, tels que la communication, la gestion du temps, et la résolution d', '22-65e9baca3cdea.png'),
(3, 4, 'Santé Mentale et Bien-être Émotionnel', '1.	Articles sur la gestion du stress : Des conseils pratiques pour identifier les sources de stress, développer des stratégies de gestion du stress et favoriser la relaxation et la détente. 2.	Méditations guidées : Des séances audio pour la relaxation, la', '33-65e9bb0788450.png'),
(4, 5, 'Développement Personnel', '1.	Ateliers sur la confiance en soi : Des séances interactives pour renforcer l\'estime de soi, surmonter les doutes et les peurs, et développer une confiance inébranlable. 2.	Livres électroniques sur le développement personnel : Des ouvrages inspirants et', '44-65e9bb35e71f3.png'),
(5, 1, 'Nutrition et Diététique', '1.	Plans de repas équilibrés : Des guides détaillés pour élaborer des plans de repas sains et équilibrés, adaptés aux besoins individuels en matière de nutrition. 2.	Recettes saines : Une variété de recettes savoureuses et nutritives, avec des options pou', 'nu-65e9bb6905d42.png');

-- --------------------------------------------------------

--
-- Table structure for table `groupe`
--

CREATE TABLE `groupe` (
  `id` int(11) NOT NULL,
  `typegroupe_id` int(11) DEFAULT NULL,
  `nom` varchar(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `datecreation` datetime NOT NULL,
  `image` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `groupe`
--

INSERT INTO `groupe` (`id`, `typegroupe_id`, `nom`, `description`, `datecreation`, `image`) VALUES
(1, 1, 'groupe Nurtion 1', 'bien presentable', '2019-09-01 00:00:00', '420734711-206630509201239-9139645037034586170-n-65e9a7a12ade8.png'),
(3, 3, 'Développement profe', 'Ce groupe de coaching vise à développer les compétences en leadership des participants, en mettant l\'accent sur la communication efficace, la prise de décision stratégique, et la gestion d\'équipe. Les séances sont axées sur le renforcement de la confiance', '2019-01-01 00:00:00', 'aa-65e9bd34ae8aa.png'),
(5, 2, 'Transition de Carriè', ': Ce groupe de coaching est conçu pour aider les personnes en transition de carrière à naviguer avec succès dans les étapes de recherche d\'emploi, de réorientation professionnelle et de développement de compétences. Les séances fournissent un soutien indi', '2019-03-01 00:00:00', 'cc-65e9bdba3f951.png'),
(6, 6, 'AZEZR', 'HFHF', '2019-11-01 00:00:00', '420734711-206630509201239-9139645037034586170-n-65e9c5bc23301.png');

-- --------------------------------------------------------

--
-- Table structure for table `groupe_utilisateur`
--

CREATE TABLE `groupe_utilisateur` (
  `groupe_id` int(11) NOT NULL,
  `utilisateur_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `messenger_messages`
--

CREATE TABLE `messenger_messages` (
  `id` bigint(20) NOT NULL,
  `body` longtext NOT NULL,
  `headers` longtext NOT NULL,
  `queue_name` varchar(190) NOT NULL,
  `created_at` datetime NOT NULL,
  `available_at` datetime NOT NULL,
  `delivered_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

CREATE TABLE `reservation` (
  `id` int(11) NOT NULL,
  `sujet` varchar(50) NOT NULL,
  `date` datetime NOT NULL,
  `datefin` datetime NOT NULL,
  `description` varchar(70) NOT NULL,
  `background_color` varchar(7) NOT NULL,
  `border_color` varchar(7) NOT NULL,
  `text_color` varchar(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `reservation`
--

INSERT INTO `reservation` (`id`, `sujet`, `date`, `datefin`, `description`, `background_color`, `border_color`, `text_color`) VALUES
(1, 'santé', '2024-03-04 01:00:00', '2024-03-04 02:30:00', 'test', '#000000', '#000000', '#fbf9f9');

-- --------------------------------------------------------

--
-- Table structure for table `reset`
--

CREATE TABLE `reset` (
  `id` int(10) NOT NULL,
  `email` varchar(255) NOT NULL,
  `code` int(11) NOT NULL,
  `timeMils` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reset`
--

INSERT INTO `reset` (`id`, `email`, `code`, `timeMils`) VALUES
(6, 'medyassinee.messaoud@esprit.tn', 738000, '1714761353457'),
(7, 'medyassinee.messaoud@esprit.tn', 457945, '1714761627797'),
(8, 'medyassinee.messaoud@esprit.tn', 560568, '1714761783273'),
(9, 'medyassinee.messaoud@esprit.tn', 726823, '1714761916245'),
(10, 'medyassinee.messaoud@esprit.tn', 411748, '1714762043328'),
(11, 'medyassinee.messaoud@esprit.tn', 619130, '1714762878725'),
(12, 'medyassinee.messaoud@esprit.tn', 55743, '1714763323529'),
(13, 'medyassinee.messaoud@esprit.tn', 194906, '1714763481004');

-- --------------------------------------------------------

--
-- Table structure for table `ressources`
--

CREATE TABLE `ressources` (
  `id` int(11) NOT NULL,
  `titre_r` varchar(255) NOT NULL,
  `type_r` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `utilisateur_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `ressources`
--

INSERT INTO `ressources` (`id`, `titre_r`, `type_r`, `url`, `description`, `utilisateur_id`) VALUES
(1, 'Nutrition', 'image', '420734711-206630509201239-9139645037034586170-n-65e9b048b0c0d.png', 'Guide des repas équilibrés pour la semaine Recettes saines et rapides Vidéos d&#39;exercices de cuisine santé', NULL),
(2, 'Nutrition', 'image', 'nu-65e9b9b998cce.png', 'Guide des repas équilibrés pour la semaine Recettes saines et rapides Vidéos d\'exercices de cuisine santé', NULL),
(3, 'Carrière et Leadership', 'image', '22-65e9b9ff2509a.png', 'Guides pour rédiger un CV efficace Webinaires sur les techniques d\'entretien d\'embauche Articles sur le développement de compétences en leadership', NULL),
(4, 'Santé mentale et Bien-être émotionnel', 'image', '33-65e9ba4daabfb.png', 'Méditations guidées pour la relaxation Articles sur la gestion de l\'anxiété Supports pour la pratique de la pleine conscience', NULL),
(5, 'Développement personnel', 'image', '44-65e9ba8d808f1.png', 'Livres audio sur la confiance en soi Ateliers en ligne sur la gestion du stress Podcasts sur la croissance personnelle', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `nom_role` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `nom_role`) VALUES
(1, 'ROLE_PATIENT'),
(2, 'ROLE_COACH'),
(3, 'ROLE_ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `seance`
--

CREATE TABLE `seance` (
  `id` int(11) NOT NULL,
  `type_seance_id` int(11) DEFAULT NULL,
  `titre` varchar(50) NOT NULL,
  `duree` time NOT NULL,
  `lien` longtext NOT NULL,
  `mot_de_passe` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `seance`
--

INSERT INTO `seance` (`id`, `type_seance_id`, `titre`, `duree`, `lien`, `mot_de_passe`) VALUES
(1, 1, 'santé', '01:00:00', 'https://us05web.zoom.us/j/88175883233?pwd=wvPwDHxY1rrIMk1HAcnbcn4NeOQzSi.1', '2yb7iG');

-- --------------------------------------------------------

--
-- Table structure for table `typegroupe`
--

CREATE TABLE `typegroupe` (
  `id` int(11) NOT NULL,
  `nomtype` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `typegroupe`
--

INSERT INTO `typegroupe` (`id`, `nomtype`) VALUES
(1, 'groupe de nutrition'),
(2, 'Leadership Empowerment'),
(3, 'Bien-être Holistique'),
(6, 'AZER');

-- --------------------------------------------------------

--
-- Table structure for table `type_seance`
--

CREATE TABLE `type_seance` (
  `id` int(11) NOT NULL,
  `nom_type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `type_seance`
--

INSERT INTO `type_seance` (`id`, `nom_type`) VALUES
(1, 'individuel');

-- --------------------------------------------------------

--
-- Table structure for table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` int(11) NOT NULL,
  `role_id` int(11) DEFAULT NULL,
  `nom` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `email` varchar(255) NOT NULL,
  `tel` bigint(20) NOT NULL,
  `mdp` varchar(255) NOT NULL,
  `genre` varchar(30) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `ville` varchar(30) NOT NULL,
  `active` varchar(30) NOT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `reset_token_expires_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `role_id`, `nom`, `prenom`, `email`, `tel`, `mdp`, `genre`, `image`, `ville`, `active`, `reset_token`, `reset_token_expires_at`) VALUES
(9, 2, 'validation', 'validation', 'medyassinee.messaoud@esprit.tn', 241585555, '$2y$13$Lk.lrKnEOJyaBvhHvOBj7u64nbnZXFgM1tQ4duXi.432R0Y9hTlJu', 'femme', '420734711-206630509201239-9139645037034586170-n-65e9c1826d4e1028425081.png', 'tunis', '1', NULL, NULL),
(64, 2, 'zayd', 'chammem', 'zaydchammem@hotmail.fr', 12456789, '$2y$13$p77GcTEEUPhINyXD9o.2FueTA4miGUBnF/ApCm1fOy6G.brGXOIIa', 'Homme', 'téléchargement (18).jpg', 'bizerte', '1', NULL, NULL),
(66, 1, 'cccc', 'qqqq', 'qqqqq', 12365498, '$2y$13$RNNVATGheJ.Ymikv/wwoAu.W7Yc.m9dAJoNKBLVeqhxxpca3Mfevu', 'Femme', 'image2.jpg', 'vbvv', '1', NULL, NULL),
(67, 1, 'sinda', '12', 'ss@gmail.com', 12365498, '$2a$13$t7fWZtFldcut7sIfeESHCOFU6gX4CYqjtxWk8mI3l89ArAH8WJPHW', 'Homme', 'image2.jpg', 'sss', '1', NULL, NULL),
(68, 2, 'ss', 'sss', 'sss@gmail.com', 12456987, '$2y$13$H2kIMZJZ0jMNWU6lB1ox2.8pqNRK2md9cq7BO0gerpRw4tbZZYQne', 'Femme', 'image2.jpg', 'g', '1', NULL, NULL),
(69, 2, 'bb', 'bb', 'nnn@gmail.fr', 98765412, '$2y$13$ozKTrfvluuWvfHa0oAujbO8diiojc8XJig0BP0VnTSPvs2XOtaLGy', 'Femme', NULL, '12', '1', NULL, NULL),
(70, 2, 'zizou', 'zizou', 'zizou@gmail.com', 12456789, '$2y$13$InIlnWUW0gkdTLwGOaULK.asrXJ8wKx59lWe0Q.TdmIHAj6p2nHHm', 'Homme', NULL, 'bizerte', '1', NULL, NULL),
(71, 1, 'test2', 'test2', 'test2@gmail.com', 12456789, '$2a$13$t7fWZtFldcut7sIfeESHCOFU6gX4CYqjtxWk8mI3l89ArAH8WJPHW', 'Femme', 'téléchargement (18).jpg', 'bizerte', '1', NULL, NULL),
(73, 1, 'aa', 'bb', 'bb@gmail.com', 12456987, '$2y$13$G/PTE/ME97wOHZInNEBokuIeeLdIPneA/2IAQB.mwH5sd8Q.t3C12', 'Femme', NULL, 'bizerte', '1', NULL, NULL),
(74, 2, 'qsdfg', 'qsdfg', 'bb@gmail.com', 12456987, '$2y$13$QGw3WCu8x4Af6tUZZ01W2OxhJJEYn7CttibTLfnenh0jpiMLi58W2', 'Femme', NULL, 'bizerte', '1', NULL, NULL),
(88, 1, 'ali', 'ali', '2222@hotmail.com', 12456777, '$2y$13$OmJz8UoMQay7WoAu0HbIoOwX9yg8N0j83jUL.Vy7xnPYwvppCTtMy', 'Homme', 'téléchargement (17).jpg', 'tunis', '1', NULL, NULL),
(89, 1, 'ali', 'ali', '2024@kl.nh', 12456777, '$2y$13$5LDqB7RZTfQzqA9NQTf2legS4f3o9PBTPralGjxbrlH3mwbZURbDO', 'Homme', 'téléchargement (17).jpg', 'tunis', '1', NULL, NULL),
(90, 2, '12', '12', '2024@hotmail.com', 12654987, '$2y$13$9iW14cX80vYbc2D140YJ9.qZGyLQ/DVEyGNtXVOY.DzqvW7F4st/.', 'Femme', 'téléchargement (17).jpg', 'bizerte', '1', NULL, NULL),
(95, 1, 'lobna', 'lobna', 'lobna@hotmail.fr', 12456789, '$2y$13$lQEYaxuSvPdos/DEykHtzeEixiwCUUNiM5BIyD5.fITp9co9YNrxW', 'Femme', '60 coupes de cheveux mi-long les plus élégantes et modernes en 2023.jpg', 'bizerte', '1', NULL, NULL),
(97, 2, 'hamadi', 'hamadi', 'hamadi@gmail.fr', 12365498, '$2y$13$EvuGuOu/s/ocVo/2hluRTOvCg7lCTSTDwAheWYe/j5rTDHzl.t6Sm', 'Homme', 'profile_1713571083017.jpg', 'bizerte', '1', NULL, NULL),
(98, 2, 'aminA', 'amin', 'amina@gmai.lcom', 12365488, '$2y$13$NxcSekhM/2Au4kna9YfXLOst7BAGk6WxmS3TueQrs.46JRsrV7yiu', 'Homme', 'profile_1713583631901.jpg', 'bizerte', '1', NULL, NULL),
(99, 2, 'ss', 'abdou', 'abou17@mail.com', 12456987, '$2y$13$SR9PBkAvmTwUddEQxkLNk.3jnoBdNM5D/tnDd5WzcKgE1Lrh6AgRy', 'Homme', 'profile_1713583915189.jpg', 'bizerte', '1', NULL, NULL),
(103, 3, 'loulou', 'loulou', 'chammem@gmail.com', 12345632, '$2a$13$t7fWZtFldcut7sIfeESHCOFU6gX4CYqjtxWk8mI3l89ArAH8WJPHW', 'Femme', NULL, 'bbb', '1', NULL, NULL),
(104, 3, 'loulou', 'loulou', 'hechem@gmail.com', 12345632, '$2y$13$1tX8VdjvVLZPySuUD71RKOFPGUHdZHVCUHpjztNAXxqKoIyKqu/cC', 'Femme', NULL, 'bbb', '1', NULL, NULL),
(109, 3, 'loulou', 'loulou', 'kam@gmail.com', 12345632, '$2y$13$mXyoAVnys/aLw00Y0mWjeuV43oZ2rTf1xyLpXGxk19lK7H2xO3ZeC', 'Femme', NULL, 'bbb', '1', NULL, NULL),
(111, 3, 'loulou', 'loulou', 'kamis@gmail.com', 12345632, '$2y$13$sDg1KIT6OH.NF4OWvkJ01.zOmpj7bSxaETwX6T4gzV2qp9oGW66.q', 'Femme', NULL, 'bbb', '1', NULL, NULL),
(112, 3, 'loulou', 'loulou', 'wejden@gmail.com', 12345632, '$2y$13$3JAUOiwWUPalCHbNDKczEeENpOiJTV63qMGfMGY/JDFYReZ86WGei', 'Femme', 'profile_1713587676194.jpg', 'bbb', '1', NULL, NULL),
(113, 2, 'zizou', 'zizou', 'toutou@gmail.com', 12456789, '$2y$13$7vqXXVy0CgavZg8UkSiJ/.SUYBhiJmvSZvtMkOj/IgYW.huAZSI0u', 'Femme', 'profile_1713645357736.jpg', 'bizerte', '1', NULL, NULL),
(114, 2, 'zizou', 'zizou', 'hibar@gmail.com', 12456789, '$2y$13$GD6OCD5WbYnNs7BY6Pc6Oe.Df.SEdyQ2XuNvSqZpjR3gHseEluSa6', 'Homme', NULL, 'bizerte', '1', NULL, NULL),
(115, 2, 'zizou', 'zizou', 'koukou@gmail.com', 12456789, '$2y$13$.mw2xO2QM62z.CnUruWNX.sBZtJESbINSAy89mswDLoq648A.2hqS', 'Homme', 'profile_1713652533311.jpg', 'bizerte', '1', NULL, NULL),
(116, 1, 'ameni', 'ameny', 'ameny@gmail.com', 12345678, '$2y$13$jzkfjo.riyC//LUjyikyU.Q/awbxdx3FCerT.uNBdeKaDXr7WJ9wO', 'Femme', 'profile_1713781087561.jpg', 'bizerte', '1', NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_497DD634FC6CD52A` (`ressource_id`);

--
-- Indexes for table `groupe`
--
ALTER TABLE `groupe`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UNIQ_4B98C21997A5710` (`typegroupe_id`);

--
-- Indexes for table `groupe_utilisateur`
--
ALTER TABLE `groupe_utilisateur`
  ADD PRIMARY KEY (`groupe_id`,`utilisateur_id`),
  ADD KEY `IDX_92C1107D7A45358C` (`groupe_id`),
  ADD KEY `IDX_92C1107DFB88E14F` (`utilisateur_id`);

--
-- Indexes for table `messenger_messages`
--
ALTER TABLE `messenger_messages`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_75EA56E0FB7336F0` (`queue_name`),
  ADD KEY `IDX_75EA56E0E3BD61CE` (`available_at`),
  ADD KEY `IDX_75EA56E016BA31DB` (`delivered_at`);

--
-- Indexes for table `reservation`
--
ALTER TABLE `reservation`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reset`
--
ALTER TABLE `reset`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ressources`
--
ALTER TABLE `ressources`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_6A2CD5C7FB88E14F` (`utilisateur_id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `seance`
--
ALTER TABLE `seance`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_DF7DFD0E57BF3B84` (`type_seance_id`);

--
-- Indexes for table `typegroupe`
--
ALTER TABLE `typegroupe`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `type_seance`
--
ALTER TABLE `type_seance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD KEY `IDX_1D1C63B3D60322AC` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `groupe`
--
ALTER TABLE `groupe`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `messenger_messages`
--
ALTER TABLE `messenger_messages`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `reservation`
--
ALTER TABLE `reservation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `reset`
--
ALTER TABLE `reset`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `ressources`
--
ALTER TABLE `ressources`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `seance`
--
ALTER TABLE `seance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `typegroupe`
--
ALTER TABLE `typegroupe`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `type_seance`
--
ALTER TABLE `type_seance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `categorie`
--
ALTER TABLE `categorie`
  ADD CONSTRAINT `FK_497DD634FC6CD52A` FOREIGN KEY (`ressource_id`) REFERENCES `ressources` (`id`);

--
-- Constraints for table `groupe`
--
ALTER TABLE `groupe`
  ADD CONSTRAINT `FK_4B98C21997A5710` FOREIGN KEY (`typegroupe_id`) REFERENCES `typegroupe` (`id`);

--
-- Constraints for table `groupe_utilisateur`
--
ALTER TABLE `groupe_utilisateur`
  ADD CONSTRAINT `FK_92C1107D7A45358C` FOREIGN KEY (`groupe_id`) REFERENCES `groupe` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_92C1107DFB88E14F` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `ressources`
--
ALTER TABLE `ressources`
  ADD CONSTRAINT `FK_6A2CD5C7FB88E14F` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id`);

--
-- Constraints for table `seance`
--
ALTER TABLE `seance`
  ADD CONSTRAINT `FK_DF7DFD0E57BF3B84` FOREIGN KEY (`type_seance_id`) REFERENCES `type_seance` (`id`);

--
-- Constraints for table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `FK_1D1C63B3D60322AC` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
