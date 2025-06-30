-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 30, 2025 at 03:24 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cookup_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(100) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `color_hex` varchar(7) DEFAULT '#EEEEEE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `category_name`, `image_url`, `color_hex`) VALUES
(21, 'Carnes', 'meat.png', '#E53935'),
(22, 'Mariscos', 'fish.png', '#039BE5'),
(23, 'Massas', 'pasta.png', '#FB8C00'),
(24, 'Saladas', 'salad.png', '#43A047'),
(25, 'Sopas', 'soup.png', '#7E57C2'),
(26, 'Sobremesas', 'desserts.png', '#D81B60'),
(27, 'Lanches', 'lunch.png', '#6D4C41'),
(28, 'Bebidas', 'drink.png', '#00897B'),
(29, 'Vegetariano', 'vegetarian.png', '#2E7D32'),
(30, 'Vegano', 'vegan.png', '#00695C'),
(31, 'Sem Glúten', 'gluten-free.png', '#8E24AA'),
(32, 'Sem Lactose', 'lactose-free.png', '#5D4037'),
(33, 'Fitness', 'healthy-food.png', '#689F38'),
(35, 'Fast Food', 'fast-food.png', '#F4511E'),
(36, 'Comida de Rua', 'food-cart.png', '#795548'),
(37, 'Caseiras', 'earth.png', '#4E342E'),
(38, 'Gourmet', 'gourmet.png', '#3F51B5'),
(39, 'Portuguesa', 'portugal.png', '#1A237E'),
(40, 'Brasileira', 'brazil.png', '#0097A7'),
(41, 'Italiana', 'italy.png', '#B71C1C'),
(42, 'Japonesa', 'japan.png', '#C62828'),
(43, 'Chinesa', 'china.png', '#AD1457'),
(44, 'Indiana', 'india.png', '#EF6C00'),
(45, 'Mexicana', 'mexico.png', '#C2185B'),
(46, 'Árabe', 'arabic.png', '#4A148C'),
(47, 'Francesa', 'france.png', '#283593'),
(48, 'Americana', 'united-states.png', '#37474F'),
(49, 'Espanhola', 'spain.png', '#512DA8'),
(50, 'Café da Manhã', 'breakfast.png', '#3E2723'),
(52, 'Churrasco', 'barbecue.png', '#BF360C'),
(55, 'Rápidos', 'cooked.png', '#D84315');

-- --------------------------------------------------------

--
-- Table structure for table `comments`
--

CREATE TABLE `comments` (
  `comment_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  `comment` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `comments`
--

INSERT INTO `comments` (`comment_id`, `user_id`, `recipe_id`, `comment`, `created_at`) VALUES
(59, 1, 161, 'gostei', '2025-06-21 22:25:44'),
(60, 1, 112, 'adorei', '2025-06-23 16:02:00');

-- --------------------------------------------------------

--
-- Table structure for table `ingredients`
--

CREATE TABLE `ingredients` (
  `ingredient_id` int(11) NOT NULL,
  `ingredient_name` varchar(100) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ingredients`
--

INSERT INTO `ingredients` (`ingredient_id`, `ingredient_name`, `image_url`) VALUES
(169, 'Sal', 'ing_68583fe726f9c8.98204790.png'),
(170, 'Açúcar', 'ing_685845741b4f50.54924554.png'),
(171, 'Pimenta', 'ing_6858489471fb89.01788767.png'),
(172, 'Alho', 'ing_685848a4e474c1.33228530.png'),
(173, 'Cebola', 'ing_685848b6ce2bc7.49950341.png'),
(174, 'Tomate', 'ing_685848c842c6d8.18871787.png'),
(175, 'Ovos', 'ing_685848e04046a6.21648800.png'),
(176, 'Leite', 'ing_685848f900cf69.19224058.png'),
(177, 'Manteiga', 'ing_68584909a3f1b3.00433493.png'),
(178, 'Queijo', 'ing_68584920a0b301.53757698.png'),
(179, 'Farinha de trigo', 'ing_685849534cd449.65117334.png'),
(180, 'Fermento', 'ing_6858499a0c1af9.90736731.png'),
(181, 'Óleo de oliva', 'ing_68584a9f16f9f7.43233149.png'),
(182, 'Vinagre', 'ing_68584b974ef1f8.59245299.png'),
(183, 'Carne de frango', 'ing_68584bb51271f2.94313613.png'),
(184, 'Carne de vaca', 'ing_68627d0aeae7a4.77662513.png'),
(185, 'Carne de porco', 'ing_68584c660ae929.04637231.png'),
(186, 'Peixe', 'ing_68584c76dcdd36.72191262.png'),
(187, 'Camarão', 'ing_68584c97062417.95973876.png'),
(188, 'Lula', 'ing_68584cbbb7d746.39444468.png'),
(189, 'Bacalhau', 'ing_68584ccfb59330.06744173.png'),
(190, 'Arroz', 'ing_685856bc13e362.84923964.png'),
(191, 'Feijão', 'ing_685856f3c60a59.21933423.png'),
(192, 'Macarrão', 'ing_68585731466ab0.19866389.png'),
(193, 'Batata', 'ing_6858575747c337.26877995.png'),
(194, 'Cenoura', 'ing_685857677b5b92.03241832.png'),
(195, 'Espinafre', 'ing_68585776561d74.34759387.png'),
(196, 'Brócolis', 'ing_685857c4cc3fc4.54170216.png'),
(197, 'Abobrinha', 'ing_685857d39c5de1.70269422.png'),
(198, 'Cogumelos', 'ing_685857e283a886.10500042.png'),
(199, 'Maçã', 'ing_685857f1460587.13981851.png'),
(200, 'Banana', 'ing_6858580d9677c3.80369898.png'),
(201, 'Laranja', 'ing_68585844d45803.00845418.png'),
(202, 'Manga', 'ing_6858585503fe08.74482477.png'),
(203, 'Morango', 'ing_6858586570cd22.63291965.png'),
(204, 'Melancia', 'ing_68585884d947c6.70637599.png'),
(205, 'Uvas', 'ing_685858a500fc33.43121294.png'),
(206, 'Castanhas', 'ing_685858c1369ea6.31382513.png'),
(207, 'Nozes', 'ing_685858e4d3f2d8.94132019.png'),
(208, 'Amêndoas', 'ing_685858fb8b0485.38476613.png'),
(209, 'Mel', 'ing_6858590b812828.74186620.png'),
(210, 'Chocolate', 'ing_6858591cad7c87.49586907.png'),
(211, 'Creme de leite', 'ing_685859471bcf24.15117867.png'),
(212, 'Iogurte natural', 'ing_68585970c01538.14492589.png'),
(213, 'Pão', 'ing_6858597d3fb776.31571388.png'),
(214, 'Tofu', 'ing_6858598b1b45c4.36251310.png'),
(215, 'Grão-de-bico', 'ing_685859a4ae1a42.51610610.png'),
(216, 'Lentilha', 'ing_685859b56ebd18.84709813.png'),
(217, 'Soja', 'ing_685859c4beec75.79720733.png'),
(218, 'Molho de soja', 'ing_685859d1533376.07800410.png'),
(220, 'Canela', 'ing_68585a66aeafd5.55899208.png'),
(221, 'Gengibre', 'ing_68585a74705ca5.30060397.png'),
(222, 'Coentros', 'ing_68585a8493fe85.08692843.png'),
(223, 'Manjericão', 'ing_68585a91da4c73.12917566.png'),
(224, 'Orégãos', 'ing_68585b43bf5298.68004519.png'),
(227, 'farinha', 'ing_68627d24e71557.27213746.png'),
(233, 'abacate', 'ing_68585b57e02c12.84465536.png'),
(238, 'Peito de frando', 'ing_68585b96c8ca98.53510467.png'),
(239, 'Dente de alho', 'ing_68585ba43dad66.37309276.png'),
(240, 'Ketchup', 'ing_68585bc9ddfd68.05021285.png'),
(241, 'Mostarda', 'ing_68585bd9f23727.81648526.png'),
(242, 'Molho inglês', 'ing_68585beeb60069.85946655.png'),
(243, 'Molho de tomate', 'ing_68585c09c0ef60.96077730.png'),
(246, 'Batata palha', 'ing_685861f3bb6da0.25849867.jpg'),
(247, 'Arroz branco', 'ing_68586231148b11.98742201.png'),
(259, 'espargo', 'ing_68627d3ce251a4.52850142.png'),
(260, 'Filés de peito de frango', 'ing_68586249ed6631.03105289.png'),
(261, 'Esparguete', 'ing_6858626a255d66.87704664.png'),
(262, 'pepino', 'ing_68586278a1a782.89371654.png'),
(263, 'azeitonas', 'ing_68586322daa514.58758203.png'),
(264, 'queijo feta', 'ing_68585e52e410f9.28254006.png'),
(266, 'limão', 'ing_68627d807362a3.13255591.png'),
(267, 'Abóbora', 'ing_68585e725d8171.90924440.png'),
(268, 'caldo de legumes', 'ing_6858628e554d52.92667387.png'),
(269, 'cacau em pó', 'ing_6858629d6234e0.41635492.png'),
(270, 'Salmão', 'ing_685862bdf13c55.00626438.png'),
(271, 'cebola roxa', 'ing_685862caad89d6.29708680.png'),
(273, 'pimentão', 'ing_685862ef933a31.50146731.png'),
(274, 'especiarias', 'spice.jpg'),
(275, 'Carne moída', 'ing_685862fe155048.85068791.png'),
(276, 'massa de lasanha', 'ing_68586312363d52.13561189.png'),
(278, 'guacamole', 'ing_68586333d39558.58020115.png'),
(279, 'tortilhas', 'ing_68586340e8fa92.65781476.png'),
(280, 'Picanha', 'ing_68586381df8b46.45864738.png'),
(281, 'sal grosso', 'ing_68586392547b57.34294745.png'),
(282, 'Feijão preto', 'ing_685863a3ecacd4.28211188.png'),
(283, 'Massa de pizza', 'ing_685863c55133c6.50344303.png'),
(284, 'mozzarella', 'ing_685863e170d0d4.73584216.png'),
(285, 'Quinoa', 'ing_685863ed425ba1.01640044.png'),
(286, 'Bolacha', 'ing_68586407bc8a49.42102575.png'),
(287, 'leite condensado', 'ing_68627d91698fa4.17328381.png'),
(288, 'Arroz para sushi', 'ing_68586420b58a15.05981962.png'),
(289, 'algas nori', 'ing_68586439212f90.47598774.png'),
(290, 'vinagre de arroz', 'ing_68627da2dc4790.26367705.png'),
(292, 'café', 'ing_68586454a60319.29216351.png'),
(293, 'queijo mascarpone', 'ing_68586479148ec7.21131721.png'),
(295, 'bacon', 'ing_68627db3c3ebb7.68401556.png'),
(296, 'linguiça', 'ing_68627dc8ecfe29.14002193.png'),
(297, 'farinha de mandioca', 'ing_685864aacf8093.63706578.png'),
(300, 'vinho branco', 'ing_68627ded4b8fc4.33060079.png'),
(303, 'queijo parmesão ralado', 'ing_68627edd762f81.62876874.png'),
(304, 'Carne de boi', 'ing_68627f017d3922.56111533.png'),
(305, 'Massa folhada', 'ing_68627f15acf7a1.82545086.png'),
(306, 'alho-poró', 'ing_68627f6f862176.97511446.png'),
(309, 'óleo', 'ing_68627fa7414d45.88850430.png'),
(336, 'tomilho', 'ing_68628037a299c3.94253127.png'),
(337, 'vinho tinto', 'ing_6862802851d0e7.27788307.png'),
(340, 'Courgette', 'ourgette.jpg'),
(341, 'Cuscuz', 'ouscous.jpg'),
(342, 'Hortelã', 'int.jpg'),
(343, 'Batata-doce', 'weetpotato.jpg'),
(344, 'Alface', 'ettuce.jpg'),
(345, 'Natas', 'ream.jpg'),
(347, 'Baunilha', 'ing_68627fbc6e5a91.01113764.png'),
(348, 'Tomate cherry', 'herrytomato.jpg'),
(351, 'Alperce', 'ing_68627f5e5692d1.42993904.png');

-- --------------------------------------------------------

--
-- Table structure for table `ratings`
--

CREATE TABLE `ratings` (
  `rating_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  `rating` tinyint(4) NOT NULL CHECK (`rating` between 1 and 5),
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ratings`
--

INSERT INTO `ratings` (`rating_id`, `user_id`, `recipe_id`, `rating`, `created_at`) VALUES
(74, 1317, 127, 3, '2025-06-19 00:28:31'),
(75, 1317, 156, 5, '2025-06-19 00:28:40'),
(76, 1, 161, 3, '2025-06-21 22:25:44'),
(77, 1, 112, 5, '2025-06-23 16:02:00');

--
-- Triggers `ratings`
--
DELIMITER $$
CREATE TRIGGER `trg_update_average_rating_after_delete` AFTER DELETE ON `ratings` FOR EACH ROW BEGIN
    UPDATE recipes
    SET average_rating = (
            SELECT IFNULL(ROUND(AVG(rating), 1), 0)
            FROM ratings
            WHERE recipe_id = OLD.recipe_id
        ),
        ratings_count = (
            SELECT COUNT(*)
            FROM ratings
            WHERE recipe_id = OLD.recipe_id
        )
    WHERE recipe_id = OLD.recipe_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_update_average_rating_after_insert` AFTER INSERT ON `ratings` FOR EACH ROW BEGIN
    UPDATE recipes
    SET average_rating = (
            SELECT ROUND(AVG(rating), 1)
            FROM ratings
            WHERE recipe_id = NEW.recipe_id
        ),
        ratings_count = (
            SELECT COUNT(*)
            FROM ratings
            WHERE recipe_id = NEW.recipe_id
        )
    WHERE recipe_id = NEW.recipe_id;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_update_average_rating_after_update` AFTER UPDATE ON `ratings` FOR EACH ROW BEGIN
    UPDATE recipes
    SET average_rating = (
            SELECT ROUND(AVG(rating), 1)
            FROM ratings
            WHERE recipe_id = NEW.recipe_id
        ),
        ratings_count = (
            SELECT COUNT(*)
            FROM ratings
            WHERE recipe_id = NEW.recipe_id
        )
    WHERE recipe_id = NEW.recipe_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `recipes`
--

CREATE TABLE `recipes` (
  `recipe_id` int(11) NOT NULL,
  `author_id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `instructions` text NOT NULL,
  `difficulty` varchar(20) DEFAULT NULL,
  `preparation_time` int(11) NOT NULL,
  `servings` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `favorites_count` int(11) DEFAULT 0,
  `views_count` int(11) DEFAULT 0,
  `average_rating` float DEFAULT 0,
  `ratings_count` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipes`
--

INSERT INTO `recipes` (`recipe_id`, `author_id`, `title`, `description`, `instructions`, `difficulty`, `preparation_time`, `servings`, `image`, `created_at`, `updated_at`, `favorites_count`, `views_count`, `average_rating`, `ratings_count`) VALUES
(112, 1, 'Frango Grelhado com Ervas', 'Frango grelhado, temperado com ervas frescas e servido com legumes', 'Tempere o frango com ervas e sal. Grelhe por 30 minutos. Sirva com legumes.', 'Fácil', 60, 4, 'uy3549ji3jg3jt3g3498jgj9.jpg', '2025-06-18 20:10:34', '2025-06-30 12:20:47', 2, 5346, 5, 1),
(113, 1, 'Macarrão ao Alho e Óleo', 'Um clássico italiano de macarrão simples com alho, azeite e pimenta.', 'Cozinhe o macarrão e refogue o alho em azeite até dourar. Misture o macarrão ao alho e sirva.', 'Fácil', 10, 2, 'recipe_6853238fdd9755.42277942_66eecc72.jpg', '2025-06-18 20:37:35', '2025-06-30 12:33:19', 3, 436, 0, 0),
(114, 1, 'Salada Mediterrânea', 'Salada fresca com tomates, pepino, azeitonas e queijo feta.', 'Misture todos os ingredientes em uma tigela. Tempere com azeite e limão', 'Fácil', 20, 2, 'recipe_68532450e473f5.52860502_eeee5d22.jpg', '2025-06-18 20:40:48', '2025-06-19 00:25:20', 3, 534, 0, 0),
(116, 1, 'Sopa de Abóbora', 'Sopa cremosa de abóbora, perfeita para dias frios.', 'Cozinhe a abóbora e bata até virar um creme. Tempere com sal e pimenta.', 'Médio', 60, 4, 'recipe_685324f4774e84.37010838_9747a903.jpg', '2025-06-18 20:43:32', '2025-06-30 00:01:47', 2, 1799, 0, 0),
(117, 1, 'Bolo de Chocolate', 'Bolo fofo e molhadinho de chocolate.', 'Misture os ingredientes secos e úmidos. Asse por 40 minutos.', 'Fácil', 50, 3, 'recipe_685325bb737db2.23536620_43edd595.jpg', '2025-06-18 20:46:51', '2025-06-30 12:31:47', 2, 1, 0, 0),
(118, 1, 'Ceviche de Salmão', 'Salmão fresco marinado com limão, cebola e pimenta.', 'Corte o salmão em cubos e marine com os ingredientes por 1 hora.', 'Fácil', 30, 2, 'recipe_6853264b09fe86.93859372_19c2408a.jpg', '2025-06-18 20:49:15', '2025-06-18 21:57:00', 1, 1, 0, 0),
(119, 1, 'Hambúrguer Vegano', 'Hambúrguer de grão-de-bico com especiarias.', 'Misture os ingredientes, modele os hambúrgueres e f', 'Médio', 40, 1, 'recipe_685326f896e002.06556990_1e5400d3.jpg', '2025-06-18 20:52:08', '2025-06-20 14:06:41', 1, 2, 0, 0),
(120, 1, 'Lasanha de Carne', 'Lasanha com carne moída, molho de tomate e queijo.', 'Monte as camadas de lasanha e asse por 60 minutos.', 'Difícil', 80, 6, 'recipe_6853279e7f1760.01305317_eb885b98.jpg', '2025-06-18 20:54:54', '2025-06-21 22:25:57', 3, 3, 0, 0),
(122, 1, 'Taco Mexicano', 'Taco crocante recheado com carne, queijo e guacamole.', 'Monte os tacos com a carne refogada, queijo e guacamole.', 'Fácil', 15, 5, 'recipe_68532833311330.74678347_5a95fa28.jpg', '2025-06-18 20:57:23', '2025-06-30 12:35:43', 1, 5, 0, 0),
(123, 1, 'Strogonoff de Frango', 'Frango cremoso com molho de cogumelos e creme de leite.', 'Cozinhe o frango e adicione o molho de cogumelos. Sirva com arroz.', 'Difícil', 40, 4, 'recipe_6853295ed2b447.50436145_9ebe0e2c.jpg', '2025-06-18 21:02:22', '2025-06-18 23:25:13', 2, 0, 0, 0),
(124, 1312, 'Bacalhau à Brás', 'Bacalhau desfiado com batatas e ovos, prato típico português.', 'Refogue o bacalhau com batatas fritas e ovos mexidos.', 'Médio', 50, 4, 'recipe_68532a6ef2f4b3.13630779_822ae08f.jpg', '2025-06-18 21:06:54', '2025-06-18 23:24:16', 1, 0, 0, 0),
(125, 1312, 'Churrasco Gaúcho', 'Carne de boi grelhada no espeto, típica do Rio Grande do Sul.', 'Tempere a carne e asse no espeto por 1 hora.', 'Médio', 40, 7, 'recipe_68532b412ba405.44635743_db5aa007.jpg', '2025-06-18 21:10:25', '2025-06-19 00:24:18', 1, 5, 0, 0),
(126, 1312, 'Guacamole', 'Molho fresco mexicano feito com abacate e temperos.', 'Amasse o abacate e misture os ingredientes. Sirva com nachos.', 'Difícil', 20, 4, 'recipe_68532bbb9facb6.51266162_d83569da.jpg', '2025-06-18 21:12:27', '2025-06-18 21:12:27', 0, 0, 0, 0),
(127, 1312, 'Feijoada', 'Feijão preto cozido com carne de porco e servido com arroz.', 'Cozinhe o feijão com as carnes e sirva com arroz e laranja.', 'Difícil', 60, 7, 'recipe_68532c29e37b64.17794400_cf46ac73.jpg', '2025-06-18 21:14:17', '2025-06-28 13:57:58', 3, 7, 3, 1),
(128, 1312, 'Pizza Margherita', 'Pizza clássica italiana com molho de tomate, mozzarella e manjericão.', 'Modele a massa, adicione o molho e asse por 20 minutos.', 'Médio', 40, 2, 'recipe_68532ca51f99c1.41216084_b5960038.jpg', '2025-06-18 21:16:21', '2025-06-18 23:43:04', 1, 0, 0, 0),
(129, 1312, 'Salada de Quinoa', 'Salada fresca com quinoa, legumes e molho de limão.', 'Cozinhe a quinoa e misture com os legumes. Tempere com limão.', 'Médio', 30, 1, 'recipe_68532d1299e9e3.93770383_e3175966.jpg', '2025-06-18 21:18:10', '2025-06-27 14:29:25', 4, 1, 0, 0),
(130, 1312, 'Torta de Limão', 'Torta cremosa de limão com crosta de biscoito.', 'Faça a base, recheie com o creme de limão e asse.', 'Difícil', 50, 2, 'recipe_68532d77f09a89.52279901_975eac45.jpg', '2025-06-18 21:19:51', '2025-06-18 22:11:53', 1, 0, 0, 0),
(131, 1312, 'Sushi de Salmão', 'Sushi tradicional japonês com arroz e salmão fresco.', 'Modele os rolinhos de sushi com arroz e salmão.', 'Médio', 20, 1, 'recipe_68532deed8a1d6.56274322_8b8e53e4.jpg', '2025-06-18 21:21:50', '2025-06-18 22:12:16', 1, 0, 0, 0),
(132, 1312, 'Frango à Parmegiana', 'Frango empanado coberto com molho de tomate e queijo.', 'Empane o frango, frite e cubra com molho e queijo.', 'Difícil', 80, 4, 'recipe_68532e72c07755.75281468_b75b0650.jpg', '2025-06-18 21:24:02', '2025-06-30 12:25:44', 3, 1, 0, 0),
(133, 1312, 'Tiramisu', 'Sobremesa italiana feita com café, queijo mascarpone e cacau.', 'Faça camadas de biscoitos molhados em café e creme de mascarpone.', 'Médio', 30, 1, 'recipe_68532efcc71d76.73016521_f6c7b1bd.jpg', '2025-06-18 21:26:20', '2025-06-27 14:29:22', 4, 2, 0, 0),
(134, 1313, 'Feijão Tropeiro', 'Feijão tropeiro tradicional com linguiça, bacon e farofa.', 'Cozinhe o feijão, frite o bacon e linguiça, misture com a farofa e sirva.', 'Difícil', 40, 4, 'recipe_68533062dc26b5.43484915_2e86bb52.jpg', '2025-06-18 21:32:18', '2025-06-30 12:07:34', 3, 3, 0, 0),
(137, 1313, 'Frango à Cacciatore', 'Frango cozido em molho de tomate, azeitonas e ervas.', 'Cozinhe o frango com molho de tomate, alho, cebola e azeitonas até ficar macio.', 'Difícil', 60, 4, 'recipe_6853311346cf63.33178843_8d42bcbf.jpg', '2025-06-18 21:35:15', '2025-06-19 00:24:21', 0, 4, 0, 0),
(138, 1313, 'Risoto de Cogumelos', 'Risoto cremoso com cogumelos frescos e queijo parmesão.', 'Refogue os cogumelos e cozinhe o arroz até ficar cremoso. Adicione queijo parmesão e sirva.', 'Médio', 40, 3, 'recipe_6853319ee26cc1.04233295_1fd16dfe.jpg', '2025-06-18 21:37:34', '2025-06-18 21:37:34', 0, 0, 0, 0),
(139, 1313, 'Carne de Panela', 'Carne de boi cozida lentamente com batatas e cenouras.', 'Cozinhe a carne na panela de pressão com batatas, cenouras e especiarias até amaciar.', 'Difícil', 120, 5, 'recipe_685332b1c6fb89.66116647_177939e4.jpg', '2025-06-18 21:42:09', '2025-06-30 12:57:52', 1, 2, 0, 0),
(140, 1313, 'Quiche de Alho-poró', 'Quiche recheada com alho-poró, queijo e ovos, com uma crosta de massa folhada.', 'Faça a massa, recheie com alho-poró refogado e asse.', 'Médio', 50, 6, 'recipe_685333758b77c9.36394796_151763a7.jpg', '2025-06-18 21:45:25', '2025-06-21 22:25:54', 2, 2, 0, 0),
(141, 1313, 'Bife à Milanesa', 'Bife empanado e frito até ficar crocante e dourado.', 'Passe os bifes na farinha, ovo e farinha de rosca, e frite até dourar.', 'Difícil', 30, 4, 'recipe_6853344bd1f0b4.93105009_7bb5ceac.jpg', '2025-06-18 21:48:59', '2025-06-19 00:02:35', 2, 0, 0, 0),
(142, 1313, 'Salmão ao Forno', 'Salmão assado com ervas, limão e azeite.', 'Tempere o salmão e asse até que fique dourado e suculento.', 'Médio', 30, 4, 'recipe_685334c467c336.67602964_c15349b7.jpg', '2025-06-18 21:51:00', '2025-06-19 00:02:38', 1, 0, 0, 0),
(143, 1313, 'Torta de Maçã', 'Torta doce de maçã com crosta de massa folhada.', 'Modele a massa, recheie com maçãs e asse até dourar.', 'Médio', 60, 6, 'recipe_6853354feafd17.67262283_88aff663.jpg', '2025-06-18 21:53:19', '2025-06-18 23:32:28', 1, 0, 0, 0),
(144, 1313, 'Espaguete à Carbonara', 'Espaguete com molho cremoso de ovos, queijo parmesão e pancetta.', 'Cozinhe o espaguete, faça o molho com ovos e pancetta e misture.', 'Médio', 20, 2, 'recipe_685335fb3a7208.64489620_3ac9824c.jpg', '2025-06-18 21:56:11', '2025-06-30 12:47:56', 1, 1, 0, 0),
(145, 1313, 'Guarujá Pescado', 'Peixe assado com tempero tropical e ervas frescas.', 'Tempere o peixe com ervas e asse até ficar dourado.', 'Médio', 30, 4, 'recipe_6853395b6653f1.42499646_3a48f7d3.jpg', '2025-06-18 22:10:35', '2025-06-18 23:32:31', 1, 0, 0, 0),
(146, 1314, 'Camarão ao Alho e Óleo', 'Camarões salteados no alho e azeite.', 'Cozinhe os camarões no azeite com alho até ficarem dourados.', 'Difícil', 15, 3, 'recipe_68533de628e996.10773331_e1048c1a.jpg', '2025-06-18 22:29:58', '2025-06-18 22:29:58', 0, 0, 0, 0),
(147, 1314, 'Panquecas Americanas', 'Panquecas fofas com xarope de bordo.', 'Misture os ingredientes e frite as panquecas em uma frigideira quente.', 'Fácil', 20, 4, 'recipe_68533e409494b3.69646713_c0c90b30.jpg', '2025-06-18 22:31:28', '2025-06-27 14:29:21', 1, 0, 0, 0),
(148, 1314, 'Moussaka', 'Prato grego de berinjela, carne e molho béchamel.', 'Monte camadas de berinjela, carne moída e molho béchamel, depois asse.Berinjela', 'Médio', 60, 5, 'recipe_68533ea5619b60.14118141_1707e1f6.jpg', '2025-06-18 22:33:09', '2025-06-18 23:24:08', 1, 0, 0, 0),
(149, 1314, 'Amêijoas à Bulhão Pato', 'Amêijoas frescas cozidas com alho, coentro e azeite.', 'Cozinhe as amêijoas com alho, coentro e azeite até que abram.', 'Fácil', 20, 4, 'recipe_68533f1c6c5431.50902706_24739082.jpg', '2025-06-18 22:35:08', '2025-06-18 23:24:02', 1, 1, 0, 0),
(150, 1314, 'Arroz de Marisco', 'Arroz cremoso com camarões, amêijoas e mexilhões.', 'Refogue os frutos do mar, acrescente o arroz e cozinhe com caldo de peixe.', 'Difícil', 50, 4, 'recipe_6853401c3c8886.44433699_bb97d0f9.jpg', '2025-06-18 22:39:24', '2025-06-18 22:39:24', 0, 0, 0, 0),
(152, 1314, 'Feijoada à Portuguesa', 'Feijão preto com carnes como chouriço, morcela e costela de porco.', 'Cozinhe o feijão com as carnes até ficarem bem cozidas e misture com arroz.', 'Difícil', 60, 6, 'recipe_685340ab5062e3.78199688_e4415a45.jpg', '2025-06-18 22:41:47', '2025-06-30 12:39:45', 2, 2, 0, 0),
(153, 1314, 'Caldo Verde', 'Sopa com couve, batata e chouriço, típica do norte de Portugal.', 'Cozinhe as batatas, depois adicione a couve e o chouriço cortado em rodelas.', 'Difícil', 40, 4, 'recipe_6853428ed98706.81338656_a860e3fc.jpg', '2025-06-18 22:49:50', '2025-06-18 23:24:09', 1, 0, 0, 0),
(154, 1314, 'Pastéis de Bacalhau', 'Bolinhos fritos de bacalhau com batata e cebola.', 'Misture o bacalhau desfiado, batata e cebola, modele em bolinhos e frite.', 'Médio', 30, 4, 'recipe_685343079a9bd8.87327047_4ad5ff7d.jpg', '2025-06-18 22:51:51', '2025-06-18 23:24:18', 1, 0, 0, 0),
(155, 1314, 'Alheira de Mirandela', 'Alheira feita com carne de frango, pão e alho, assada ou frita.Carne de frango', 'Grelhe ou frite a alheira até dourar e sirva com arroz ou salada.', 'Fácil', 40, 4, 'recipe_6853437a853683.11885305_ad6458ee.jpg', '2025-06-18 22:53:46', '2025-06-18 22:53:46', 0, 0, 0, 0),
(156, 1317, 'Bolinhos de Bacalhau com Arroz', 'Bolinhos crocantes de bacalhau servidos com arroz branco.', 'Modele os bolinhos de bacalhau e frite-os. Sirva com arroz branco.', 'Fácil', 40, 4, 'recipe_685349c6a7b494.93121268_c0746537.jpg', '2025-06-18 23:20:38', '2025-06-19 00:28:40', 1, 3, 5, 1),
(157, 1317, 'Cozido à Portuguesa', 'Carne e legumes cozidos juntos, típicos da culinária portuguesa.', 'Cozinhe a carne, batatas, cenouras e couve até ficarem bem macios.', 'Difícil', 120, 6, 'recipe_68534b3ef22038.06999599_3617b660.jpg', '2025-06-18 23:26:54', '2025-06-30 12:43:06', 1, 3, 0, 0),
(158, 1317, 'Paella Valenciana', 'Prato tradicional da Espanha com arroz, frutos do mar, legumes e açafrão.', 'Cozinhe o arroz com açafrão, adicione frutos do mar e legumes e deixe cozinhar até tudo estar bem misturado.', 'Difícil', 120, 4, 'recipe_68534bd2724d26.32104326_f26cdd31.jpg', '2025-06-18 23:29:22', '2025-06-30 00:01:53', 434, 1, 0, 0),
(159, 1317, 'Ratatouille', 'Prato francês de vegetais assados, perfeito para acompanhar carnes ou como prato principal vegetariano.', 'Corte os legumes em rodelas e asse com ervas até ficarem macios e levemente caramelizados.', 'Difícil', 45, 4, 'recipe_68534c6be25f24.16967034_61b8f75c.jpg', '2025-06-18 23:31:55', '2025-06-23 13:18:04', 143422, 43, 0, 342),
(160, 1317, 'Biryani de Frango', 'Arroz aromático indiano cozido com frango e especiarias, servindo como prato único.', 'Cozinhe o arroz com especiarias, adicione o frango marinado e cozinhe até que tudo esteja bem incorporado.', 'Difícil', 135, 5, 'recipe_68534d1068e102.10130103_fda96071.jpg', '2025-06-18 23:34:40', '2025-06-30 12:53:40', 343, 4, 0, 0),
(161, 1317, 'Francesinha', 'A Francesinha é um prato tradicional português, originário do Porto, composto por pão de forma recheado com carnes, coberto com queijo e um molho picante à base de tomate e cerveja, que é depois levado ao forno para gratinar. Uma verdadeira delícia para quem aprecia pratos robustos e saborosos.', 'Preparação do molho:\n\nEm uma panela, aqueça o azeite e refogue a cebola até que fique dourada.\n\nAdicione os dentes de alho picados e refogue até ficarem aromáticos.\n\nColoque os tomates picados (ou o tomate pelado) e deixe cozinhar por cerca de 5 minutos até que o tomate se desfaça.\n\nAcrescente o louro, o caldo de carne, o molho de tomate, a cerveja, a pimenta e a mostarda. Misture bem.\n\nDeixe o molho ferver por 10 minutos em fogo baixo para apurar os sabores. Se necessário, acrescente o amido de milho diluído em um pouco de água para engrossar o molho.\n\nAjuste o sal e a pimenta a gosto. Retire do fogo e reserve.\n\nMontagem da Francesinha:\n\nEm uma frigideira, grelhe os bifes de carne de vaca a gosto. Tempere com sal e pimenta.\n\nCorte a linguiça em rodelas e frite-a até ficar dourada.\n\nComece a montar a francesinha: coloque uma fatia de pão de forma no fundo, adicione uma fatia de fiambre, uma fatia de queijo, o bife grelhado, as rodelas de linguiça e outra fatia de queijo. Cubra com a outra fatia de pão.\n\nColoque as sanduíches montadas em uma assadeira ou refratário e cubra com o molho quente que foi preparado.\n\nGratinando a Francesinha:\n\nLeve as francesinhas ao forno pré-aquecido a 180°C e deixe gratinar por cerca de 10-15 minutos, até que o queijo derreta completamente e o molho esteja borbulhando.\n\nFinalização:\n\nFrite os ovos a gosto (geralmente, a gema deve ficar mole) e coloque um ovo sobre cada francesinha já gratinada.\n\nSirva quente com batatas fritas ao lado.', 'Difícil', 35, 2, 'recipe_68534e66274f47.23280963_c0b241fb.jpg', '2025-06-18 23:39:39', '2025-06-30 13:14:02', 34236, 5243552, 3, 1),
(162, 1317, 'Dim Sum de Porco', 'Pequenos bolinhos chineses de carne de porco e vegetais, cozidos no vapor.', 'Modele os bolinhos com carne de porco, envolva-os em massa e cozinhe no vapor.', 'Médio', 40, 3, 'recipe_68535048a26a47.88292872_226b156f.jpg', '2025-06-18 23:48:24', '2025-06-30 11:46:58', 0, 1, 0, 0),
(163, 1317, 'Gumbo de Frango e Quiabo', 'Ensopado tradicional de New Orleans com frango, quiabo e especiarias.', 'Cozinhe o frango com legumes e especiarias, até que o gumbo fique espesso.', 'Difícil', 120, 4, 'recipe_68535129535261.62716887_a1445b91.jpg', '2025-06-18 23:52:09', '2025-06-18 23:52:09', 0, 0, 0, 0),
(164, 1318, 'Arroz de Pato', 'Arroz cozido com pato desfiado e linguiça, bem temperado.', 'Arroz cozido com pato desfiado e linguiça, bem tem', 'Difícil', 120, 6, 'recipe_6853537be6ea42.31404851_29b5af20.jpg', '2025-06-19 00:02:03', '2025-06-30 12:31:00', 1, 1, 0, 0),
(165, 1318, 'Coq au Vin', 'Frango cozido lentamente em vinho tinto com cogumelos e cebolas.', 'Frango cozido lentamente em vinho tinto com cogumelos e cebolas.', 'Médio', 120, 4, 'recipe_68535588b82cf6.01285379_a3c19870.jpg', '2025-06-19 00:10:48', '2025-06-30 12:39:37', 1, 3, 0, 0),
(166, 1318, 'Bouillabaisse', 'Sopa francesa de peixe e frutos do mar, com tomate e ervas.', 'Torta de maçã invertida caramelizada.', 'Médio', 90, 4, 'recipe_6853564d950892.47803143_69283629.jpg', '2025-06-19 00:14:05', '2025-06-19 00:14:05', 0, 0, 0, 0),
(167, 1318, 'Croquetas de Jamón', 'Croquetas de Jamón', 'Camarões Camarões Camarões', 'Fácil', 60, 5, 'recipe_6853569d5d5967.75097161_9b89efd1.jpg', '2025-06-19 00:15:25', '2025-06-19 00:15:25', 0, 0, 0, 0),
(168, 1318, 'Brigadeiro', 'Doce típico brasileiro feito com leite condensado, chocolate em pó e manteiga, enrolado em bolinhas e coberto com granulado.', 'Doce típico brasileiro feito com leite condensado, chocolate em pó e manteiga, enrolado em bolinhas e coberto com granulado.', 'Fácil', 49, 4, 'recipe_685357612b1cb3.66196659_51300ec1.jpg', '2025-06-19 00:18:41', '2025-06-19 00:18:41', 0, 0, 0, 0),
(169, 1318, 'Coxinha', 'Salgado empanado e frito, recheado com frango desfiado e cream cheese.', 'Salgado empanado e frito, recheado com frango desfiado e cream cheese.', 'Médio', 60, 1, 'recipe_685357dacb3013.82731381_4c1a89ef.jpg', '2025-06-19 00:20:42', '2025-06-30 13:14:07', 1, 8, 0, 0),
(170, 1318, 'Pão de Queijo', 'Pequenos pãezinhos crocantes por fora e macios por dentro, feitos com polvilho e queijo.', 'Pequenos pãezinhos crocantes por fora e macios por dentro, feitos com polvilho e queijo.', 'Difícil', 30, 4, 'recipe_6853582ad7bcb0.41414786_72dde564.jpg', '2025-06-19 00:22:02', '2025-06-30 00:01:44', 2, 0, 0, 0),
(174, 1, 'Espetadas de Salmão e Legumes', 'Espetadas leves de salmão intercaladas com legumes coloridos, ideais para grelhar.', 'Corte o salmão em cubos e legumes a gosto. Monte as espetadas, tempere com sal, pimenta e um fio de azeite. Grelhe até dourar.', 'Fácil', 25, 2, 'recipe_686273676c2ab2.59318871_6f7c1620.jpg', '2025-06-30 11:22:15', '2025-06-30 12:57:44', 0, 2, 0, 0),
(175, 1, 'Tosta de Abacate com Ovo Escalfado', 'Uma tosta crocante com abacate esmagado e um ovo escalfado por cima.', 'Torre o pão, esmague o abacate e tempere. Coloque por cima e finalize com ovo escalfado.', 'Fácil', 10, 1, 'recipe_68627432247926.14646317_49612a98.jpg', '2025-06-30 11:25:38', '2025-06-30 11:25:38', 0, 0, 0, 0),
(176, 1, 'Risoto de Espargos', 'Risoto cremoso com espargos frescos e parmesão.', ' Salteie os espargos, junte arroz arbório, vá regando com caldo até ficar cremoso. Termine com queijo parmesão.', 'Médio', 40, 3, 'recipe_68627493d6c8e3.99169625_4a2e7c45.jpg', '2025-06-30 11:27:15', '2025-06-30 12:57:47', 0, 1, 0, 0),
(177, 1, 'Salada Grega', 'Salada fresca de pepino, tomate, cebola roxa, azeitonas e queijo feta.', ' Corte os legumes, misture tudo numa tigela, tempere com azeite, orégãos e sal.', 'Fácil', 15, 2, 'recipe_686274ed9ae638.86206165_e092393d.jpg', '2025-06-30 11:28:45', '2025-06-30 11:28:45', 0, 0, 0, 0),
(178, 1, 'Cuscuz Marroquino com Frango', 'Prato aromático de cuscuz com frango e legumes.', 'Cozinhe o cuscuz com caldo. Salteie o frango e legumes, misture tudo e sirva.', 'Médio', 25, 2, 'recipe_6862753d0da117.03163686_90ff8f0a.jpg', '2025-06-30 11:29:54', '2025-06-30 11:30:05', 0, 0, 0, 0),
(179, 1, 'Sopa Fria de Melancia', 'Sopa doce e refrescante de melancia, perfeita para o verão.', 'Triture a melancia, adicione sumo de limão, folhas de hortelã e sirva bem fria.', 'Fácil', 10, 2, 'recipe_686275a8cbd255.88871480_21c4962a.jpg', '2025-06-30 11:31:52', '2025-06-30 11:31:52', 0, 0, 0, 0),
(180, 1, 'Chili Vegano', 'Ensopado picante de feijão, tomate e especiarias.\r\n\r\n', 'Refogue cebola, junte tomate, feijão, milho e especiarias. Deixe apurar.\r\n\r\n', 'Fácil', 30, 4, 'recipe_68627609b97e05.67166594_830936fa.jpg', '2025-06-30 11:33:29', '2025-06-30 11:33:29', 0, 0, 0, 0),
(181, 1, 'Batata-doce Assada com Alecrim', 'Batatas-doces assadas, crocantes por fora, aromatizadas com alecrim.', 'Corte as batatas em rodelas, tempere com azeite, sal e alecrim. Leve ao forno até dourar.', 'Fácil', 35, 2, 'recipe_68627650c82723.64058866_b270afa4.jpg', '2025-06-30 11:34:40', '2025-06-30 11:34:40', 0, 0, 0, 0),
(182, 1, 'Wrap de Frango com Legumes', 'Wrap saudável recheado com frango grelhado e legumes.', 'Grelhe o frango, corte em tiras. Recheie a tortilha com frango, alface, cenoura ralada e molho a gosto.', 'Fácil', 15, 2, 'recipe_686276a658ca16.17796454_4ea2f0b9.jpg', '2025-06-30 11:35:56', '2025-06-30 11:36:06', 0, 0, 0, 0),
(183, 1, 'Panacota de Baunilha', 'Sobremesa italiana cremosa com aroma de baunilha.', 'Ferva as natas com açúcar e baunilha, adicione gelatina, verta para taças e leve ao frio.', 'Fácil', 20, 4, 'recipe_68627711698821.50290929_c68c9866.jpg', '2025-06-30 11:37:53', '2025-06-30 11:37:53', 0, 0, 0, 0),
(184, 1, 'Pasta Primavera', 'Massa colorida com vegetais salteados e molho leve.', 'Cozinhe a massa, salteie vegetais a gosto, misture tudo e tempere.', 'Fácil', 20, 2, 'recipe_6862776de963f1.88305124_5dcc0119.jpg', '2025-06-30 11:39:25', '2025-06-30 11:39:25', 0, 0, 0, 0),
(185, 1, 'Crepioca', 'anqueca brasileira de tapioca e ovo, ideal para pequenos-almoços rápidos.', 'Misture ovo com tapioca e um pouco de sal. Frite dos dois lados.', 'Fácil', 8, 1, 'recipe_686277c5b568e0.74371946_53a71032.jpg', '2025-06-30 11:40:53', '2025-06-30 11:40:53', 0, 0, 0, 0),
(186, 1, 'Esparguete de Courgette ao Alho', 'Esparguete” de courgette salteado com alho e ervas.', ' Espiralize a courgette, salteie em azeite com alho picado e ervas frescas.', 'Fácil', 12, 1, 'recipe_6862783dbffb38.22535661_9397feb8.jpg', '2025-06-30 11:42:53', '2025-06-30 11:42:53', 0, 0, 0, 0),
(187, 1, 'Sumo Detox Verde', 'Bebida refrescante com couve, maçã e gengibre.', 'Bata todos os ingredientes no liquidificador e sirva fresco.', 'Fácil', 5, 2, 'recipe_686278c2770440.30306152_74a00698.jpg', '2025-06-30 11:45:06', '2025-06-30 11:45:06', 0, 0, 0, 0),
(188, 1, 'Tarte de Alperce', 'Sobremesa com base crocante e recheio de alperce fresco.', 'Forre uma forma com massa, coloque o recheio de alperce adoçado e leve ao forno.', 'Médio', 50, 6, 'recipe_6862815553d7a2.45258816_5d0967a9.jpg', '2025-06-30 11:46:34', '2025-06-30 12:43:17', 0, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `recipe_category`
--

CREATE TABLE `recipe_category` (
  `recipe_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipe_category`
--

INSERT INTO `recipe_category` (`recipe_id`, `category_id`) VALUES
(112, 40),
(112, 41),
(112, 52),
(113, 23),
(113, 35),
(113, 38),
(113, 41),
(114, 29),
(114, 33),
(114, 55),
(116, 33),
(116, 37),
(116, 38),
(116, 39),
(117, 26),
(117, 37),
(117, 48),
(118, 22),
(118, 24),
(118, 37),
(118, 38),
(118, 43),
(119, 30),
(119, 31),
(119, 32),
(119, 39),
(120, 21),
(120, 37),
(120, 40),
(122, 21),
(122, 35),
(122, 45),
(122, 55),
(123, 21),
(123, 38),
(123, 39),
(124, 37),
(124, 39),
(125, 21),
(125, 40),
(125, 52),
(126, 35),
(126, 38),
(126, 45),
(127, 37),
(127, 40),
(128, 35),
(128, 38),
(128, 41),
(129, 24),
(129, 26),
(129, 30),
(129, 33),
(130, 26),
(130, 27),
(130, 38),
(131, 22),
(131, 42),
(131, 43),
(132, 21),
(132, 48),
(133, 26),
(133, 37),
(133, 38),
(133, 46),
(134, 31),
(134, 36),
(134, 37),
(134, 40),
(137, 21),
(137, 46),
(138, 23),
(138, 41),
(138, 47),
(139, 21),
(139, 39),
(139, 47),
(139, 49),
(140, 29),
(140, 33),
(140, 38),
(141, 21),
(141, 37),
(141, 44),
(142, 22),
(142, 31),
(142, 33),
(142, 37),
(143, 30),
(143, 32),
(143, 47),
(144, 23),
(144, 38),
(145, 22),
(145, 42),
(145, 52),
(146, 22),
(146, 31),
(146, 48),
(147, 26),
(147, 50),
(147, 55),
(148, 38),
(148, 44),
(148, 46),
(149, 22),
(149, 32),
(149, 49),
(150, 22),
(150, 37),
(150, 39),
(152, 21),
(152, 37),
(152, 39),
(153, 25),
(153, 39),
(154, 37),
(154, 39),
(155, 35),
(155, 37),
(155, 39),
(155, 52),
(156, 27),
(156, 33),
(156, 37),
(156, 39),
(157, 21),
(157, 22),
(157, 37),
(157, 39),
(158, 22),
(158, 23),
(158, 35),
(158, 49),
(159, 29),
(159, 31),
(159, 47),
(160, 21),
(160, 33),
(160, 44),
(160, 55),
(161, 21),
(161, 37),
(161, 38),
(161, 39),
(162, 21),
(162, 27),
(162, 43),
(163, 21),
(163, 25),
(163, 46),
(164, 21),
(164, 25),
(164, 39),
(165, 21),
(165, 47),
(166, 22),
(166, 25),
(166, 47),
(167, 27),
(167, 49),
(168, 26),
(168, 40),
(169, 21),
(169, 27),
(169, 40),
(170, 27),
(170, 32),
(170, 40),
(174, 22),
(174, 33),
(174, 55),
(175, 29),
(175, 33),
(175, 50),
(175, 55),
(176, 29),
(176, 38),
(176, 41),
(177, 24),
(177, 29),
(177, 33),
(179, 25),
(179, 30),
(179, 33),
(180, 30),
(180, 33),
(180, 45),
(181, 30),
(181, 33),
(181, 55),
(182, 27),
(182, 33),
(182, 55),
(183, 26),
(183, 41),
(184, 23),
(184, 29),
(184, 41),
(185, 33),
(185, 40),
(185, 50),
(185, 55),
(186, 30),
(186, 33),
(186, 55),
(187, 28),
(187, 30),
(187, 33),
(188, 26),
(188, 37),
(188, 47);

-- --------------------------------------------------------

--
-- Table structure for table `recipe_gallery`
--

CREATE TABLE `recipe_gallery` (
  `image_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `ordering` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipe_gallery`
--

INSERT INTO `recipe_gallery` (`image_id`, `recipe_id`, `image_url`, `ordering`) VALUES
(193, 113, 'recipe_6853238fdd9755.42277942_66eecc72.jpg', 0),
(194, 114, 'recipe_68532450e473f5.52860502_eeee5d22.jpg', 0),
(195, 116, 'recipe_685324f4774e84.37010838_9747a903.jpg', 0),
(196, 117, 'recipe_685325bb737db2.23536620_43edd595.jpg', 0),
(197, 118, 'recipe_6853264b09fe86.93859372_19c2408a.jpg', 0),
(199, 120, 'recipe_6853279e7f1760.01305317_eb885b98.jpg', 0),
(200, 122, 'recipe_68532833311330.74678347_5a95fa28.jpg', 0),
(201, 123, 'recipe_6853295ed2b447.50436145_9ebe0e2c.jpg', 0),
(202, 124, 'recipe_68532a6ef2f4b3.13630779_822ae08f.jpg', 0),
(203, 125, 'recipe_68532b412ba405.44635743_db5aa007.jpg', 0),
(204, 126, 'recipe_68532bbb9facb6.51266162_d83569da.jpg', 0),
(205, 127, 'recipe_68532c29e37b64.17794400_cf46ac73.jpg', 0),
(206, 128, 'recipe_68532ca51f99c1.41216084_b5960038.jpg', 0),
(207, 129, 'recipe_68532d1299e9e3.93770383_e3175966.jpg', 0),
(208, 130, 'recipe_68532d77f09a89.52279901_975eac45.jpg', 0),
(209, 131, 'recipe_68532deed8a1d6.56274322_8b8e53e4.jpg', 0),
(210, 132, 'recipe_68532e72c07755.75281468_b75b0650.jpg', 0),
(211, 133, 'recipe_68532efcc71d76.73016521_f6c7b1bd.jpg', 0),
(212, 134, 'recipe_68533062dc26b5.43484915_2e86bb52.jpg', 0),
(214, 137, 'recipe_6853311346cf63.33178843_8d42bcbf.jpg', 0),
(215, 138, 'recipe_6853319ee26cc1.04233295_1fd16dfe.jpg', 0),
(216, 139, 'recipe_685332b1c6fb89.66116647_177939e4.jpg', 0),
(217, 140, 'recipe_685333758b77c9.36394796_151763a7.jpg', 0),
(218, 141, 'recipe_6853344bd1f0b4.93105009_7bb5ceac.jpg', 0),
(219, 142, 'recipe_685334c467c336.67602964_c15349b7.jpg', 0),
(220, 143, 'recipe_6853354feafd17.67262283_88aff663.jpg', 0),
(221, 144, 'recipe_685335fb3a7208.64489620_3ac9824c.jpg', 0),
(222, 145, 'recipe_6853395b6653f1.42499646_3a48f7d3.jpg', 0),
(223, 146, 'recipe_68533de628e996.10773331_e1048c1a.jpg', 0),
(224, 147, 'recipe_68533e409494b3.69646713_c0c90b30.jpg', 0),
(225, 148, 'recipe_68533ea5619b60.14118141_1707e1f6.jpg', 0),
(226, 149, 'recipe_68533f1c6c5431.50902706_24739082.jpg', 0),
(227, 150, 'recipe_6853401c3c8886.44433699_bb97d0f9.jpg', 0),
(229, 152, 'recipe_685340ab5062e3.78199688_e4415a45.jpg', 0),
(230, 153, 'recipe_6853428ed98706.81338656_a860e3fc.jpg', 0),
(231, 154, 'recipe_685343079a9bd8.87327047_4ad5ff7d.jpg', 0),
(232, 155, 'recipe_6853437a853683.11885305_ad6458ee.jpg', 0),
(233, 156, 'recipe_685349c6a7b494.93121268_c0746537.jpg', 0),
(234, 157, 'recipe_68534b3ef22038.06999599_3617b660.jpg', 0),
(235, 158, 'recipe_68534bd2724d26.32104326_f26cdd31.jpg', 0),
(236, 159, 'recipe_68534c6be25f24.16967034_61b8f75c.jpg', 0),
(237, 160, 'recipe_68534d1068e102.10130103_fda96071.jpg', 0),
(239, 161, 'recipe_68534e66274f47.23280963_c0b241fb.jpg', 0),
(240, 161, 'recipe_68534e66276233.51704980_d34f3664.jpg', 1),
(241, 161, 'recipe_68534e66277483.94766490_cbcf7f21.jpg', 2),
(242, 162, 'recipe_68535048a26a47.88292872_226b156f.jpg', 0),
(243, 162, 'recipe_68535048a27c62.85719191_1312ae98.jpg', 1),
(244, 163, 'recipe_68535129535261.62716887_a1445b91.jpg', 0),
(246, 165, 'recipe_68535588b82cf6.01285379_a3c19870.jpg', 0),
(247, 166, 'recipe_6853564d950892.47803143_69283629.jpg', 0),
(248, 167, 'recipe_6853569d5d5967.75097161_9b89efd1.jpg', 0),
(249, 168, 'recipe_685357612b1cb3.66196659_51300ec1.jpg', 0),
(250, 169, 'recipe_685357dacb3013.82731381_4c1a89ef.jpg', 0),
(251, 170, 'recipe_6853582ad7bcb0.41414786_72dde564.jpg', 0),
(253, 119, 'recipe_685326f896e002.06556990_1e5400d3.jpg', 0),
(259, 112, 'uy3549ji3jg3jt3g3498jgj9.jpg', 0),
(262, 164, 'recipe_6853537be6ea42.31404851_29b5af20.jpg', 0),
(263, 174, 'recipe_686273676c2ab2.59318871_6f7c1620.jpg', 0),
(264, 175, 'recipe_68627432247926.14646317_49612a98.jpg', 0),
(265, 176, 'recipe_68627493d6c8e3.99169625_4a2e7c45.jpg', 0),
(266, 177, 'recipe_686274ed9ae638.86206165_e092393d.jpg', 0),
(267, 179, 'recipe_686275a8cbd255.88871480_21c4962a.jpg', 0),
(268, 180, 'recipe_68627609b97e05.67166594_830936fa.jpg', 0),
(269, 181, 'recipe_68627650c82723.64058866_b270afa4.jpg', 0),
(270, 182, 'recipe_686276a658ca16.17796454_4ea2f0b9.jpg', 0),
(271, 183, 'recipe_68627711698821.50290929_c68c9866.jpg', 0),
(272, 184, 'recipe_6862776de963f1.88305124_5dcc0119.jpg', 0),
(273, 185, 'recipe_686277c5b568e0.74371946_53a71032.jpg', 0),
(274, 186, 'recipe_6862783dbffb38.22535661_9397feb8.jpg', 0),
(275, 187, 'recipe_686278c2770440.30306152_74a00698.jpg', 0),
(276, 188, 'recipe_6862815553d7a2.45258816_5d0967a9.jpg', 0);

-- --------------------------------------------------------

--
-- Table structure for table `recipe_ingredients`
--

CREATE TABLE `recipe_ingredients` (
  `recipe_ingredient_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  `ingredient_id` int(11) DEFAULT NULL,
  `quantity` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipe_ingredients`
--

INSERT INTO `recipe_ingredients` (`recipe_ingredient_id`, `recipe_id`, `ingredient_id`, `quantity`) VALUES
(206, 113, 261, '250g'),
(207, 113, 239, '2'),
(208, 113, 169, 'a gosto'),
(209, 113, 176, 'a gosto'),
(210, 113, 177, 'colher de sopa'),
(212, 114, 174, 'a gosto'),
(213, 114, 262, 'a gosto'),
(214, 114, 263, 'a gosto'),
(218, 114, 264, 'a gosto'),
(219, 114, 176, 'a gosto'),
(220, 114, 266, 'a gosto'),
(221, 116, 267, 'meia'),
(222, 116, 172, '2 dentes'),
(223, 116, 173, 'a gosto'),
(224, 116, 268, 'a gosto'),
(225, 116, 169, 'a  gosto'),
(226, 116, 171, '1/2'),
(227, 117, 179, 'meio copo'),
(228, 117, 170, '2 colheres'),
(229, 117, 269, '1 colher'),
(230, 117, 175, '3'),
(231, 117, 176, 'meio copo'),
(232, 117, 177, '1/2 colher'),
(233, 118, 270, '1  posta'),
(234, 118, 266, 'espremer meio'),
(235, 118, 271, '1/2'),
(237, 118, 222, 'a  gosto'),
(244, 120, 275, '700g'),
(245, 120, 243, 'meio copo'),
(246, 120, 178, 'umas fatias'),
(250, 120, 276, '1 pacote'),
(251, 120, 173, 'a gosto'),
(252, 122, 275, '500g'),
(253, 122, 178, 'a gosto'),
(254, 122, 278, '100ml'),
(255, 122, 279, '1 pacote'),
(256, 123, 238, '3/4'),
(257, 123, 198, 'a gosto'),
(258, 123, 211, '200ml'),
(259, 123, 173, 'meia'),
(260, 123, 190, 'a gosto'),
(261, 124, 189, '500g'),
(262, 124, 193, '11'),
(263, 124, 175, '3'),
(264, 124, 173, 'meia'),
(265, 124, 176, '3 fios'),
(266, 125, 280, '500g'),
(267, 125, 281, 'a gosto'),
(268, 125, 172, '1'),
(269, 125, 176, 'a gosto'),
(270, 126, 233, '2'),
(271, 126, 174, '2'),
(272, 126, 173, '1'),
(273, 126, 266, '2'),
(274, 126, 222, 'a gosto'),
(275, 127, 282, '500g'),
(276, 127, 185, '300g'),
(277, 127, 190, 'a gosto'),
(278, 127, 201, 'meia'),
(279, 127, 172, '1 dente'),
(280, 128, 283, '700g'),
(281, 128, 243, 'cobrir pizza'),
(282, 128, 284, '200g'),
(283, 128, 223, 'a gosto'),
(284, 129, 285, '3'),
(285, 129, 174, '2'),
(286, 129, 262, '1'),
(287, 129, 266, '1'),
(288, 129, 176, '2 fios'),
(289, 130, 286, 'a gosto'),
(290, 130, 177, '2 colheres'),
(291, 130, 287, '200 ml'),
(292, 130, 266, '3'),
(293, 131, 288, 'a gosto'),
(294, 131, 270, 'a gosto'),
(295, 131, 289, '1'),
(296, 131, 290, '2 fios'),
(297, 132, 238, '4'),
(298, 132, 243, 'a gosto'),
(299, 132, 178, 'a gosto'),
(300, 132, 179, '100g'),
(302, 133, 292, '400ml'),
(303, 133, 293, 'a gosto'),
(305, 134, 191, '400g'),
(306, 134, 295, '150g'),
(307, 134, 296, '150g'),
(308, 134, 297, '200g'),
(309, 134, 172, '2 dentes'),
(311, 134, 176, '2 fios'),
(315, 137, 203, '1kg'),
(316, 137, 174, '4'),
(318, 137, 172, '3 dentes'),
(319, 137, 173, '1'),
(320, 137, 300, '100 ml'),
(321, 137, 176, '2 fios'),
(322, 138, NULL, '200g'),
(323, 138, NULL, '300g'),
(324, 138, 303, '50g'),
(325, 138, 268, '500ml'),
(326, 139, 304, '1 kg'),
(327, 139, 193, '3'),
(328, 139, 194, '2'),
(329, 140, 305, '1 pacote'),
(330, 140, 306, '2'),
(332, 140, 175, '3'),
(334, 141, 179, '100g'),
(335, 141, 175, '2'),
(336, 141, 227, '150g'),
(337, 141, 309, ''),
(338, 142, 270, '4 files'),
(339, 142, 266, '4'),
(340, 142, 176, '50ml'),
(341, 142, 172, '2 dentes'),
(342, 143, 305, '1 pacote'),
(343, 143, 199, '4'),
(344, 143, 170, '100g'),
(345, 143, 220, '1 coler de cha'),
(346, 143, 177, '50g'),
(347, 144, 261, '250g'),
(348, 144, 175, '3'),
(351, 144, 172, '2'),
(353, 145, 266, '1'),
(354, 145, 176, '30ml'),
(357, 146, 187, '500g'),
(358, 146, 172, '3'),
(359, 146, 176, '50ml'),
(360, 147, 179, '200g'),
(361, 147, 176, '200ml'),
(362, 147, 175, '1'),
(363, 147, 170, '50g'),
(365, 148, 275, '500g'),
(366, 148, 174, '3'),
(369, 149, 208, '1 kg'),
(370, 149, 172, '4 dentes'),
(371, 149, 222, '50g'),
(372, 149, 176, '50ml'),
(373, 149, 266, ''),
(375, 150, 208, '200g'),
(377, 150, 190, '250g'),
(379, 150, 172, ''),
(380, 150, 173, ''),
(386, 152, 282, '500g'),
(390, 152, 172, '2 dentes'),
(391, 153, 193, '4'),
(394, 153, 172, '2 dentes'),
(395, 153, 173, '1'),
(396, 154, 189, '300g'),
(397, 154, 193, '300g'),
(398, 154, 173, '1'),
(399, 154, 175, '2 unidades'),
(400, 155, 183, '300g'),
(401, 155, 213, '100g'),
(402, 155, 172, '2 dentes'),
(403, 156, 189, '300g'),
(404, 156, 190, '250g'),
(405, 156, 193, '200g'),
(406, 156, 172, '2 dentes'),
(407, 156, 175, '2'),
(408, 157, 184, '500g'),
(409, 157, 183, '500g'),
(410, 157, 193, '4'),
(411, 157, 194, '2'),
(413, 157, 191, '200g'),
(414, 158, 190, '300g'),
(417, 158, 203, '200g'),
(420, 159, 197, '2'),
(422, 159, 273, '1'),
(423, 159, 174, '3'),
(424, 159, 172, '3 dentes'),
(426, 160, 203, '500g'),
(428, 160, 173, '1'),
(429, 160, 172, '2 dentes'),
(430, 160, 221, '1 pedaço'),
(442, 161, 176, '2 colheres'),
(443, 161, 173, '1'),
(444, 161, 172, '2 dentes'),
(445, 161, 243, '100ml'),
(448, 161, 243, '2 colheres'),
(450, 161, 241, '1 colher de sopa'),
(451, 161, 213, '4 fatias'),
(452, 162, 185, '300g'),
(456, 162, 218, '50ml'),
(457, 163, 183, '500g'),
(459, 163, 173, '1'),
(460, 163, 273, '1'),
(461, 163, 336, ''),
(463, 165, 183, '1kg'),
(464, 165, 337, '750ml'),
(467, 168, 287, '1 lata'),
(468, 168, 177, '1 colher'),
(469, 169, 183, '300g'),
(470, 169, 175, '3'),
(475, 119, 215, 'a gosto'),
(476, 119, 173, 'meia'),
(477, 119, 172, 'meio'),
(478, 119, 176, '1 fio'),
(479, 119, 273, 'a gosto'),
(480, 119, 274, 'a gosto'),
(491, 112, 260, '4'),
(493, 112, 177, '2 colheres de sopa'),
(494, 112, 179, '1 colher de cha'),
(497, 164, 213, '1'),
(498, 174, 270, '200g'),
(499, 174, 171, '1'),
(500, 174, 173, '1'),
(501, 174, 340, '1'),
(502, 174, 176, '1 fio'),
(503, 174, 169, 'a gosto'),
(504, 175, 213, '1 fatia'),
(505, 175, 233, '1/2'),
(506, 175, 175, '1'),
(507, 175, 266, 'algumas gotas'),
(508, 175, 169, 'a  gosto'),
(509, 176, 190, '200g'),
(510, 176, 259, '100g'),
(511, 176, 173, '1'),
(512, 176, 176, '1 fio'),
(513, 177, 262, '1'),
(514, 177, 174, '1'),
(515, 177, 264, '80g'),
(516, 178, 341, '120g'),
(517, 178, 238, '200g'),
(518, 178, 340, '1/2'),
(519, 179, 204, '300g'),
(520, 179, 266, '1/2'),
(521, 179, 342, 'a gosto'),
(522, 180, 191, '200g'),
(523, 180, 174, '2'),
(524, 180, 173, '1'),
(525, 180, 172, '200g'),
(526, 181, 343, '2'),
(528, 181, 176, '1 fio'),
(529, 181, 169, 'a gosto'),
(534, 182, 279, '2'),
(535, 182, 238, '150g'),
(536, 182, 344, '4'),
(537, 182, 194, '1'),
(538, 183, 345, '400ml'),
(539, 183, 170, '50g'),
(541, 183, 347, '1 colher'),
(542, 184, 202, '200g'),
(543, 184, 196, '50g'),
(544, 184, 340, '1/2'),
(545, 184, 194, '1'),
(546, 184, 348, '6'),
(547, 185, 175, '1'),
(549, 185, 169, 'a gosto'),
(550, 186, 340, '1'),
(551, 186, 172, '1 fio'),
(552, 186, 176, '1 fio'),
(554, 187, 199, '1'),
(555, 187, 221, '1 pedaco'),
(560, 188, 202, '1 base'),
(561, 188, 351, '400g'),
(562, 188, 227, '2.c folha');

-- --------------------------------------------------------

--
-- Table structure for table `recipe_views`
--

CREATE TABLE `recipe_views` (
  `view_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `recipe_id` int(11) DEFAULT NULL,
  `viewed_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipe_views`
--

INSERT INTO `recipe_views` (`view_id`, `user_id`, `recipe_id`, `viewed_at`) VALUES
(785, 1, 112, '2025-06-18 21:17:25'),
(786, 1, 112, '2025-06-18 21:29:20'),
(788, 1, 114, '2025-06-18 21:41:04'),
(790, 1, 114, '2025-06-18 21:41:08'),
(791, 1, 114, '2025-06-18 21:47:06'),
(792, 1, 114, '2025-06-18 21:47:08'),
(793, 1, 116, '2025-06-18 21:47:09'),
(795, 1, 120, '2025-06-18 21:55:05'),
(796, 1, 113, '2025-06-18 22:02:45'),
(797, 1, 113, '2025-06-18 22:02:46'),
(798, 1, 122, '2025-06-18 22:02:48'),
(799, 1312, 114, '2025-06-18 22:03:55'),
(800, 1312, 112, '2025-06-18 22:03:55'),
(801, 1312, 125, '2025-06-18 22:10:30'),
(802, 1313, 125, '2025-06-18 22:28:10'),
(803, 1313, 133, '2025-06-18 22:28:18'),
(804, 1313, 125, '2025-06-18 22:28:23'),
(805, 1313, 127, '2025-06-18 22:28:24'),
(806, 1313, 120, '2025-06-18 22:28:25'),
(807, 1313, 112, '2025-06-18 22:28:26'),
(809, 1313, 137, '2025-06-18 22:35:22'),
(811, 1313, 112, '2025-06-18 22:42:19'),
(812, 1313, 125, '2025-06-18 22:42:24'),
(813, 1313, 140, '2025-06-18 22:46:07'),
(814, 1313, 129, '2025-06-18 22:56:59'),
(815, 1313, 118, '2025-06-18 22:57:00'),
(816, 1314, 133, '2025-06-18 23:11:35'),
(818, 1314, 152, '2025-06-18 23:41:50'),
(820, 1314, 149, '2025-06-18 23:42:18'),
(821, 1317, 137, '2025-06-19 00:24:10'),
(822, 1317, 137, '2025-06-19 00:32:33'),
(823, 1317, 161, '2025-06-19 00:40:23'),
(824, 1317, 156, '2025-06-19 00:43:40'),
(825, 1317, 156, '2025-06-19 00:43:45'),
(826, 1317, 134, '2025-06-19 00:44:04'),
(827, 1318, 157, '2025-06-19 01:24:16'),
(828, 1318, 120, '2025-06-19 01:24:17'),
(829, 1318, 125, '2025-06-19 01:24:18'),
(830, 1318, 137, '2025-06-19 01:24:21'),
(831, 1318, 122, '2025-06-19 01:24:22'),
(832, 1318, 112, '2025-06-19 01:25:01'),
(833, 1318, 112, '2025-06-19 01:25:15'),
(834, 1318, 161, '2025-06-19 01:26:03'),
(835, 1318, 161, '2025-06-19 01:27:22'),
(836, 1318, 161, '2025-06-19 01:27:24'),
(837, 1317, 127, '2025-06-19 01:28:27'),
(838, 1317, 156, '2025-06-19 01:28:34'),
(839, 1, 119, '2025-06-20 14:50:27'),
(840, 1, 119, '2025-06-20 15:02:42'),
(843, 1, 140, '2025-06-21 23:25:11'),
(844, 1, 158, '2025-06-21 23:25:16'),
(845, 1, 161, '2025-06-21 23:25:26'),
(846, 1, 112, '2025-06-23 17:01:54'),
(847, 1, 116, '2025-06-28 14:37:59'),
(848, 1, 161, '2025-06-28 14:38:09'),
(849, 1, 127, '2025-06-28 14:51:03'),
(850, 1, 127, '2025-06-28 14:51:25'),
(851, 1, 127, '2025-06-28 14:51:37'),
(852, 1, 127, '2025-06-28 14:54:50'),
(853, 1, 161, '2025-06-28 14:55:22'),
(854, 1, 161, '2025-06-28 14:55:32'),
(855, 1, 161, '2025-06-28 14:57:12'),
(856, 1, 127, '2025-06-28 14:57:58'),
(857, 1, 161, '2025-06-28 14:58:05'),
(858, 1, 174, '2025-06-30 12:22:52'),
(859, 1, 162, '2025-06-30 12:46:58'),
(860, 1, 157, '2025-06-30 12:52:41'),
(861, 1, 134, '2025-06-30 12:53:16'),
(862, 1, 134, '2025-06-30 13:07:34'),
(863, 1, 112, '2025-06-30 13:20:47'),
(864, 1, 169, '2025-06-30 13:22:43'),
(865, 1, 169, '2025-06-30 13:22:57'),
(866, 1, 139, '2025-06-30 13:23:00'),
(867, 1, 169, '2025-06-30 13:23:07'),
(868, 1, 165, '2025-06-30 13:23:27'),
(869, 1, 161, '2025-06-30 13:23:33'),
(870, 1, 132, '2025-06-30 13:25:44'),
(871, 1, 161, '2025-06-30 13:27:14'),
(872, 1, 161, '2025-06-30 13:27:21'),
(873, 1, 161, '2025-06-30 13:29:47'),
(874, 1, 161, '2025-06-30 13:29:53'),
(875, 1, 161, '2025-06-30 13:29:54'),
(876, 1, 161, '2025-06-30 13:29:58'),
(877, 1, 160, '2025-06-30 13:30:00'),
(878, 1, 161, '2025-06-30 13:30:07'),
(879, 1, 160, '2025-06-30 13:30:08'),
(880, 1, 169, '2025-06-30 13:30:29'),
(881, 1, 113, '2025-06-30 13:30:39'),
(882, 1, 169, '2025-06-30 13:30:50'),
(883, 1, 164, '2025-06-30 13:31:00'),
(884, 1, 117, '2025-06-30 13:31:47'),
(885, 1, 161, '2025-06-30 13:32:50'),
(886, 1, 161, '2025-06-30 13:32:52'),
(887, 1, 161, '2025-06-30 13:32:54'),
(888, 1, 169, '2025-06-30 13:33:02'),
(889, 1, 122, '2025-06-30 13:33:12'),
(890, 1, 113, '2025-06-30 13:33:14'),
(891, 1, 122, '2025-06-30 13:34:00'),
(892, 1, 161, '2025-06-30 13:35:09'),
(893, 1, 165, '2025-06-30 13:35:11'),
(894, 1, 161, '2025-06-30 13:35:37'),
(895, 1, 122, '2025-06-30 13:35:43'),
(896, 1, 161, '2025-06-30 13:39:10'),
(897, 1, 161, '2025-06-30 13:39:17'),
(898, 1, 165, '2025-06-30 13:39:37'),
(899, 1, 161, '2025-06-30 13:39:40'),
(900, 1, 152, '2025-06-30 13:39:45'),
(901, 1, 161, '2025-06-30 13:43:00'),
(902, 1, 161, '2025-06-30 13:43:04'),
(903, 1, 157, '2025-06-30 13:43:06'),
(904, 1, 169, '2025-06-30 13:43:10'),
(905, 1, 188, '2025-06-30 13:43:17'),
(906, 1, 161, '2025-06-30 13:43:20'),
(907, 1, 161, '2025-06-30 13:47:14'),
(908, 1, 144, '2025-06-30 13:47:56'),
(909, 1, 161, '2025-06-30 13:52:04'),
(910, 1, 160, '2025-06-30 13:52:05'),
(911, 1, 161, '2025-06-30 13:53:39'),
(912, 1, 160, '2025-06-30 13:53:40'),
(913, 1, 174, '2025-06-30 13:57:44'),
(914, 1, 176, '2025-06-30 13:57:47'),
(915, 1, 139, '2025-06-30 13:57:52'),
(916, 1, 161, '2025-06-30 13:57:55'),
(917, 1, 161, '2025-06-30 14:14:02'),
(918, 1, 169, '2025-06-30 14:14:07');

--
-- Triggers `recipe_views`
--
DELIMITER $$
CREATE TRIGGER `after_view_insert` AFTER INSERT ON `recipe_views` FOR EACH ROW BEGIN
    UPDATE recipes
    SET views_count = views_count + 1
    WHERE recipe_id = NEW.recipe_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `saved_lists`
--

CREATE TABLE `saved_lists` (
  `list_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `list_name` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `color` varchar(10) DEFAULT '#FFFFFF'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `saved_lists`
--

INSERT INTO `saved_lists` (`list_id`, `user_id`, `list_name`, `created_at`, `color`) VALUES
(77, 1312, 'favoritos', '2025-06-18 21:03:40', '#FFECB3'),
(78, 1313, 'churrascada', '2025-06-18 21:28:47', '#FAD6A5'),
(79, 1313, 'saladas', '2025-06-18 21:56:42', '#C1E1C1'),
(80, 1314, 'sobremesas', '2025-06-18 22:11:49', '#E0BBE4'),
(81, 1314, 'outros', '2025-06-18 22:12:10', '#B2EBF2'),
(82, 1317, 'adorei', '2025-06-18 23:23:59', '#FFF0F5'),
(83, 1317, 'fixe', '2025-06-18 23:32:27', '#FFF0F5'),
(84, 1318, 'outros', '2025-06-19 00:02:32', '#FFF0F5'),
(85, 1, 'gostos', '2025-06-21 22:25:53', '#E0BBE4'),
(86, 1, 'sobremesas', '2025-06-27 14:29:09', '#C1E1C1');

-- --------------------------------------------------------

--
-- Table structure for table `saved_recipes`
--

CREATE TABLE `saved_recipes` (
  `saved_id` int(11) NOT NULL,
  `list_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  `added_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `saved_recipes`
--

INSERT INTO `saved_recipes` (`saved_id`, `list_id`, `recipe_id`, `added_at`) VALUES
(519, 77, 123, '2025-06-18 21:03:42'),
(520, 77, 122, '2025-06-18 21:03:43'),
(521, 77, 114, '2025-06-18 21:03:45'),
(522, 77, 113, '2025-06-18 21:03:47'),
(523, 78, 127, '2025-06-18 21:28:50'),
(524, 78, 125, '2025-06-18 21:28:52'),
(525, 78, 120, '2025-06-18 21:28:54'),
(526, 78, 112, '2025-06-18 21:28:55'),
(528, 79, 129, '2025-06-18 21:56:56'),
(529, 79, 118, '2025-06-18 21:56:58'),
(530, 80, 133, '2025-06-18 22:11:50'),
(531, 80, 129, '2025-06-18 22:11:52'),
(532, 80, 130, '2025-06-18 22:11:53'),
(533, 80, 117, '2025-06-18 22:11:54'),
(534, 81, 131, '2025-06-18 22:12:16'),
(535, 81, 132, '2025-06-18 22:12:17'),
(536, 81, 116, '2025-06-18 22:12:19'),
(537, 81, 141, '2025-06-18 22:12:21'),
(538, 82, 134, '2025-06-18 23:24:00'),
(539, 82, 149, '2025-06-18 23:24:02'),
(540, 82, 156, '2025-06-18 23:24:06'),
(541, 82, 148, '2025-06-18 23:24:08'),
(542, 82, 153, '2025-06-18 23:24:09'),
(543, 82, 120, '2025-06-18 23:24:15'),
(544, 82, 124, '2025-06-18 23:24:16'),
(545, 82, 154, '2025-06-18 23:24:18'),
(546, 82, 133, '2025-06-18 23:25:04'),
(547, 82, 127, '2025-06-18 23:25:06'),
(548, 82, 114, '2025-06-18 23:25:09'),
(549, 82, 129, '2025-06-18 23:25:11'),
(550, 82, 123, '2025-06-18 23:25:13'),
(551, 82, 140, '2025-06-18 23:25:18'),
(552, 83, 143, '2025-06-18 23:32:28'),
(553, 82, 139, '2025-06-18 23:32:30'),
(554, 83, 145, '2025-06-18 23:32:31'),
(555, 83, 119, '2025-06-18 23:32:40'),
(556, 83, 152, '2025-06-18 23:32:42'),
(557, 83, 144, '2025-06-18 23:32:45'),
(558, 82, 128, '2025-06-18 23:43:04'),
(559, 84, 132, '2025-06-19 00:02:33'),
(560, 84, 141, '2025-06-19 00:02:35'),
(561, 84, 142, '2025-06-19 00:02:38'),
(562, 84, 159, '2025-06-19 00:02:39'),
(563, 84, 164, '2025-06-19 00:02:41'),
(564, 84, 133, '2025-06-19 00:02:44'),
(565, 84, 113, '2025-06-19 00:02:46'),
(567, 85, 160, '2025-06-21 22:25:55'),
(568, 85, 120, '2025-06-21 22:25:57'),
(569, 86, 147, '2025-06-27 14:29:21'),
(570, 86, 133, '2025-06-27 14:29:22'),
(571, 86, 129, '2025-06-27 14:29:25'),
(572, 86, 117, '2025-06-27 14:29:27'),
(573, 86, 170, '2025-06-28 13:37:57'),
(574, 85, 127, '2025-06-28 13:54:58'),
(575, 78, 169, '2025-06-30 00:00:55'),
(579, 78, 158, '2025-06-30 00:01:50'),
(580, 79, 158, '2025-06-30 00:01:53'),
(583, 85, 134, '2025-06-30 11:53:24'),
(584, 86, 157, '2025-06-30 11:53:27'),
(585, 85, 132, '2025-06-30 12:25:43'),
(586, 85, 165, '2025-06-30 12:25:47'),
(587, 85, 161, '2025-06-30 12:27:20'),
(588, 85, 113, '2025-06-30 12:33:19'),
(589, 85, 152, '2025-06-30 12:39:45');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `firebase_uid` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `profile_picture` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `role` varchar(10) DEFAULT 'user',
  `blocked` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `firebase_uid`, `username`, `email`, `profile_picture`, `created_at`, `updated_at`, `role`, `blocked`) VALUES
(1, 'FpMqP3w1rwRzKto6H0A0TzdP0mR2', 'admin', 'admin@gmail.com', NULL, '2025-06-18 19:58:12', '2025-06-22 22:20:23', 'admin', 0),
(1312, '4jSMI2tw8xZz1sfmSPNLPuMqPni1', 'chefe kauan', 'kauan@gmail.com', NULL, '2025-06-18 21:03:26', '2025-06-22 22:20:25', 'user', 0),
(1313, 'EdctpOKw6IauUt4nBkznlv8Mksw2', 'diogo', 'diogo@gmail.com', NULL, '2025-06-18 21:28:01', '2025-06-30 11:14:51', 'user', 0),
(1314, 'o7mLSc1ZQmZFsn0jqA6JPMfa57i1', 'Diogo Esteves', 'diogoestevesmorgado@gmail.com', NULL, '2025-06-18 22:11:01', '2025-06-22 22:20:28', 'user', 0),
(1317, 'KnAXTewUWHZY1ITX3k88d4ATaUn2', 'diogoesteves1306@gmail.com', 'diogoesteves1306@gmail.com', 'https://lh3.googleusercontent.com/a/ACg8ocKQavgu7u1hrxgWikRMMXIzi-Ng7gMRthY6kE4VfF-VIwPaNQ=s96-c', '2025-06-18 23:10:05', '2025-06-20 14:42:08', 'user', 0),
(1318, 'yEWtYOykMIb5LgnclKaBea0WAYx2', 'cassandra', 'cassandra@gmail.com', NULL, '2025-06-18 23:56:13', '2025-06-30 11:14:54', 'user', 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_category_stats`
--

CREATE TABLE `user_category_stats` (
  `user_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `views_count` int(11) DEFAULT 0,
  `favorites_count` int(11) DEFAULT 0,
  `finished_count` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_category_stats`
--

INSERT INTO `user_category_stats` (`user_id`, `category_id`, `views_count`, `favorites_count`, `finished_count`) VALUES
(1, 21, 61, 7, 1),
(1, 22, 7, 1, 0),
(1, 23, 6, 1, 0),
(1, 24, 0, 1, 0),
(1, 25, 1, 0, 0),
(1, 26, 2, 4, 0),
(1, 27, 9, 1, 0),
(1, 29, 8, 1, 0),
(1, 30, 2, 1, 0),
(1, 31, 4, 1, 0),
(1, 32, 2, 1, 0),
(1, 33, 15, 3, 0),
(1, 35, 9, 1, 0),
(1, 36, 2, 1, 0),
(1, 37, 46, 8, 1),
(1, 38, 39, 4, 1),
(1, 39, 40, 3, 1),
(1, 40, 21, 4, 1),
(1, 41, 9, 1, 1),
(1, 43, 1, 0, 0),
(1, 44, 4, 1, 0),
(1, 45, 4, 0, 0),
(1, 46, 0, 1, 0),
(1, 47, 6, 1, 0),
(1, 48, 2, 2, 0),
(1, 49, 3, 0, 0),
(1, 50, 0, 1, 0),
(1, 52, 4, 0, 1),
(1, 55, 16, 2, 0),
(1312, 21, 1, 2, 0),
(1312, 23, 0, 1, 0),
(1312, 29, 1, 1, 0),
(1312, 33, 1, 1, 0),
(1312, 35, 0, 2, 0),
(1312, 38, 0, 2, 0),
(1312, 39, 0, 1, 0),
(1312, 40, 2, 0, 0),
(1312, 41, 1, 1, 0),
(1312, 45, 0, 1, 0),
(1312, 52, 2, 0, 0),
(1312, 55, 1, 2, 0),
(1313, 21, 7, 3, 0),
(1313, 22, 2, 3, 0),
(1313, 23, 1, 2, 0),
(1313, 24, 2, 2, 0),
(1313, 26, 2, 1, 0),
(1313, 27, 0, 2, 0),
(1313, 29, 1, 1, 0),
(1313, 30, 1, 1, 0),
(1313, 31, 0, 1, 0),
(1313, 32, 0, 1, 0),
(1313, 33, 2, 3, 0),
(1313, 35, 0, 2, 0),
(1313, 36, 0, 1, 0),
(1313, 37, 4, 5, 0),
(1313, 38, 3, 2, 0),
(1313, 39, 0, 1, 0),
(1313, 40, 7, 7, 0),
(1313, 41, 2, 1, 0),
(1313, 43, 1, 1, 0),
(1313, 46, 3, 0, 0),
(1313, 49, 0, 2, 0),
(1313, 52, 5, 2, 0),
(1313, 55, 0, 1, 0),
(1314, 21, 3, 2, 0),
(1314, 22, 1, 1, 0),
(1314, 24, 0, 1, 0),
(1314, 26, 1, 4, 0),
(1314, 27, 0, 1, 0),
(1314, 30, 0, 1, 0),
(1314, 32, 1, 0, 0),
(1314, 33, 0, 2, 0),
(1314, 37, 4, 4, 0),
(1314, 38, 1, 3, 0),
(1314, 39, 3, 1, 0),
(1314, 42, 0, 1, 0),
(1314, 43, 0, 1, 0),
(1314, 44, 0, 1, 0),
(1314, 46, 1, 1, 0),
(1314, 48, 0, 2, 0),
(1314, 49, 1, 0, 0),
(1317, 21, 3, 4, 0),
(1317, 22, 0, 2, 0),
(1317, 23, 0, 1, 0),
(1317, 24, 0, 1, 0),
(1317, 25, 0, 1, 0),
(1317, 26, 0, 2, 0),
(1317, 27, 3, 1, 1),
(1317, 29, 0, 2, 0),
(1317, 30, 0, 3, 0),
(1317, 31, 1, 2, 0),
(1317, 32, 0, 3, 0),
(1317, 33, 3, 4, 1),
(1317, 35, 0, 1, 0),
(1317, 36, 1, 1, 0),
(1317, 37, 6, 8, 2),
(1317, 38, 1, 6, 0),
(1317, 39, 4, 8, 1),
(1317, 40, 2, 3, 1),
(1317, 41, 0, 1, 0),
(1317, 42, 0, 1, 0),
(1317, 44, 0, 1, 0),
(1317, 46, 2, 2, 0),
(1317, 47, 0, 2, 0),
(1317, 49, 0, 2, 0),
(1317, 52, 0, 1, 0),
(1317, 55, 0, 1, 0),
(1318, 21, 8, 3, 0),
(1318, 22, 1, 1, 0),
(1318, 23, 0, 1, 0),
(1318, 25, 0, 1, 0),
(1318, 26, 0, 1, 0),
(1318, 29, 0, 1, 0),
(1318, 31, 0, 2, 0),
(1318, 33, 0, 1, 0),
(1318, 35, 1, 1, 0),
(1318, 37, 5, 3, 0),
(1318, 38, 3, 2, 0),
(1318, 39, 4, 1, 0),
(1318, 40, 4, 0, 0),
(1318, 41, 2, 1, 0),
(1318, 44, 0, 1, 0),
(1318, 45, 1, 0, 0),
(1318, 46, 1, 1, 0),
(1318, 47, 0, 1, 0),
(1318, 48, 0, 1, 0),
(1318, 52, 3, 0, 0),
(1318, 55, 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_recipe_finished`
--

CREATE TABLE `user_recipe_finished` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  `finished_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_recipe_finished`
--

INSERT INTO `user_recipe_finished` (`id`, `user_id`, `recipe_id`, `finished_at`) VALUES
(48, 1317, 127, '2025-06-19 01:28:31'),
(49, 1317, 156, '2025-06-19 01:28:40'),
(50, 1, 161, '2025-06-21 23:25:44'),
(51, 1, 112, '2025-06-23 17:02:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `category_name` (`category_name`);

--
-- Indexes for table `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`comment_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `recipe_id` (`recipe_id`);

--
-- Indexes for table `ingredients`
--
ALTER TABLE `ingredients`
  ADD PRIMARY KEY (`ingredient_id`),
  ADD UNIQUE KEY `ingredient_name` (`ingredient_name`);

--
-- Indexes for table `ratings`
--
ALTER TABLE `ratings`
  ADD PRIMARY KEY (`rating_id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`recipe_id`),
  ADD KEY `recipe_id` (`recipe_id`);

--
-- Indexes for table `recipes`
--
ALTER TABLE `recipes`
  ADD PRIMARY KEY (`recipe_id`),
  ADD KEY `idx_recipe_author` (`author_id`);

--
-- Indexes for table `recipe_category`
--
ALTER TABLE `recipe_category`
  ADD PRIMARY KEY (`recipe_id`,`category_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `recipe_gallery`
--
ALTER TABLE `recipe_gallery`
  ADD PRIMARY KEY (`image_id`),
  ADD KEY `recipe_id` (`recipe_id`);

--
-- Indexes for table `recipe_ingredients`
--
ALTER TABLE `recipe_ingredients`
  ADD PRIMARY KEY (`recipe_ingredient_id`),
  ADD KEY `recipe_id` (`recipe_id`),
  ADD KEY `ingredient_id` (`ingredient_id`);

--
-- Indexes for table `recipe_views`
--
ALTER TABLE `recipe_views`
  ADD PRIMARY KEY (`view_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `recipe_id` (`recipe_id`);

--
-- Indexes for table `saved_lists`
--
ALTER TABLE `saved_lists`
  ADD PRIMARY KEY (`list_id`),
  ADD KEY `fk_savedlists_user` (`user_id`);

--
-- Indexes for table `saved_recipes`
--
ALTER TABLE `saved_recipes`
  ADD PRIMARY KEY (`saved_id`),
  ADD KEY `fk_saved_list` (`list_id`),
  ADD KEY `fk_saved_recipes_recipe` (`recipe_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `firebase_uid` (`firebase_uid`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `user_category_stats`
--
ALTER TABLE `user_category_stats`
  ADD PRIMARY KEY (`user_id`,`category_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `user_recipe_finished`
--
ALTER TABLE `user_recipe_finished`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `recipe_id` (`recipe_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `comment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT for table `ingredients`
--
ALTER TABLE `ingredients`
  MODIFY `ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=352;

--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `rating_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=78;

--
-- AUTO_INCREMENT for table `recipes`
--
ALTER TABLE `recipes`
  MODIFY `recipe_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=189;

--
-- AUTO_INCREMENT for table `recipe_gallery`
--
ALTER TABLE `recipe_gallery`
  MODIFY `image_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=277;

--
-- AUTO_INCREMENT for table `recipe_ingredients`
--
ALTER TABLE `recipe_ingredients`
  MODIFY `recipe_ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=563;

--
-- AUTO_INCREMENT for table `recipe_views`
--
ALTER TABLE `recipe_views`
  MODIFY `view_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=919;

--
-- AUTO_INCREMENT for table `saved_lists`
--
ALTER TABLE `saved_lists`
  MODIFY `list_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=89;

--
-- AUTO_INCREMENT for table `saved_recipes`
--
ALTER TABLE `saved_recipes`
  MODIFY `saved_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=590;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1330;

--
-- AUTO_INCREMENT for table `user_recipe_finished`
--
ALTER TABLE `user_recipe_finished`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE;

--
-- Constraints for table `ratings`
--
ALTER TABLE `ratings`
  ADD CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ratings_ibfk_2` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE;

--
-- Constraints for table `recipes`
--
ALTER TABLE `recipes`
  ADD CONSTRAINT `recipes_ibfk_1` FOREIGN KEY (`author_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `recipe_category`
--
ALTER TABLE `recipe_category`
  ADD CONSTRAINT `recipe_category_ibfk_1` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `recipe_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE CASCADE;

--
-- Constraints for table `recipe_gallery`
--
ALTER TABLE `recipe_gallery`
  ADD CONSTRAINT `recipe_gallery_ibfk_1` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE;

--
-- Constraints for table `recipe_ingredients`
--
ALTER TABLE `recipe_ingredients`
  ADD CONSTRAINT `recipe_ingredients_ibfk_1` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `recipe_ingredients_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`) ON DELETE SET NULL;

--
-- Constraints for table `recipe_views`
--
ALTER TABLE `recipe_views`
  ADD CONSTRAINT `recipe_views_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `recipe_views_ibfk_2` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE;

--
-- Constraints for table `saved_lists`
--
ALTER TABLE `saved_lists`
  ADD CONSTRAINT `fk_savedlists_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `saved_recipes`
--
ALTER TABLE `saved_recipes`
  ADD CONSTRAINT `fk_saved_list` FOREIGN KEY (`list_id`) REFERENCES `saved_lists` (`list_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_saved_recipes_list` FOREIGN KEY (`list_id`) REFERENCES `saved_lists` (`list_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_saved_recipes_recipe` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE;

--
-- Constraints for table `user_category_stats`
--
ALTER TABLE `user_category_stats`
  ADD CONSTRAINT `user_category_stats_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `user_category_stats_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE CASCADE;

--
-- Constraints for table `user_recipe_finished`
--
ALTER TABLE `user_recipe_finished`
  ADD CONSTRAINT `user_recipe_finished_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `user_recipe_finished_ibfk_2` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`recipe_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
