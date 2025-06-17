-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 18, 2025 at 12:03 AM
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
(47, 1, 82, 'adorei poder trazer esta receita, deixo minha nota', '2025-06-11 21:41:24'),
(48, 1, 83, 'hshdh', '2025-06-13 18:53:13'),
(50, 1, 82, 'jshxjnsbsjsnznnsnshrjrk', '2025-06-16 20:22:43');

-- --------------------------------------------------------

--
-- Table structure for table `ingredients`
--

CREATE TABLE `ingredients` (
  `ingredient_id` int(11) NOT NULL,
  `ingredient_name` varchar(100) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ingredients`
--

INSERT INTO `ingredients` (`ingredient_id`, `ingredient_name`, `category_id`, `image_url`) VALUES
(169, 'Sal', 1, 'http://localhost/PAP/CookUp_Core/uploads/sal.png'),
(170, 'Açúcar', 1, 'http://localhost/PAP/CookUp_Core/uploads/acucar.png'),
(171, 'Pimenta', 1, 'http://localhost/PAP/CookUp_Core/uploads/pimenta.png'),
(172, 'Alho', 2, 'http://localhost/PAP/CookUp_Core/uploads/alho.png'),
(173, 'Cebola', 2, 'http://localhost/PAP/CookUp_Core/uploads/cebola.png'),
(174, 'Tomate', 2, 'http://localhost/PAP/CookUp_Core/uploads/tomate.png'),
(175, 'Ovos', 14, 'http://localhost/PAP/CookUp_Core/uploads/ovos.png'),
(176, 'Leite', 4, 'http://localhost/PAP/CookUp_Core/uploads/leite.png'),
(177, 'Manteiga', 4, 'http://localhost/PAP/CookUp_Core/uploads/manteiga.png'),
(178, 'Queijo', 4, 'http://localhost/PAP/CookUp_Core/uploads/queijo.png'),
(179, 'Farinha de trigo', 7, 'http://localhost/PAP/CookUp_Core/uploads/farinha_de_trigo.png'),
(180, 'Fermento', 10, 'http://localhost/PAP/CookUp_Core/uploads/fermento.png'),
(181, 'Óleo de oliva', 12, 'http://localhost/PAP/CookUp_Core/uploads/oleo_de_oliva.png'),
(182, 'Vinagre', 12, 'http://localhost/PAP/CookUp_Core/uploads/vinagre.png'),
(183, 'Carne de frango', 5, 'http://localhost/PAP/CookUp_Core/uploads/carne_de_frango.png'),
(184, 'Carne de vaca', 5, 'carnedevaca.png'),
(185, 'Carne de porco', 5, 'http://localhost/PAP/CookUp_Core/uploads/carne_de_porco.png'),
(186, 'Peixe', 6, 'http://localhost/PAP/CookUp_Core/uploads/peixe.png'),
(187, 'Camarão', 6, 'http://localhost/PAP/CookUp_Core/uploads/camarao.png'),
(188, 'Lula', 6, 'http://localhost/PAP/CookUp_Core/uploads/lula.png'),
(189, 'Bacalhau', 6, 'http://localhost/PAP/CookUp_Core/uploads/bacalhau.png'),
(190, 'Arroz', 7, 'http://localhost/PAP/CookUp_Core/uploads/arroz.png'),
(191, 'Feijão', 8, 'http://localhost/PAP/CookUp_Core/uploads/feijao.png'),
(192, 'Macarrão', 7, 'http://localhost/PAP/CookUp_Core/uploads/macarrao.png'),
(193, 'Batata', 2, 'http://localhost/PAP/CookUp_Core/uploads/batata.png'),
(194, 'Cenoura', 2, 'http://localhost/PAP/CookUp_Core/uploads/cenoura.png'),
(195, 'Espinafre', 2, 'http://localhost/PAP/CookUp_Core/uploads/espinafre.png'),
(196, 'Brócolos', 2, 'http://localhost/PAP/CookUp_Core/uploads/brocolos.png'),
(197, 'Abobrinha', 2, 'http://localhost/PAP/CookUp_Core/uploads/abobrinha.png'),
(198, 'Cogumelos', 2, 'http://localhost/PAP/CookUp_Core/uploads/cogumelos.png'),
(199, 'Maçã', 3, 'http://localhost/PAP/CookUp_Core/uploads/maca.png'),
(200, 'Banana', 3, 'http://localhost/PAP/CookUp_Core/uploads/banana.png'),
(201, 'Laranja', 3, 'http://localhost/PAP/CookUp_Core/uploads/laranja.png'),
(202, 'Manga', 3, 'http://localhost/PAP/CookUp_Core/uploads/manga.png'),
(203, 'Morango', 3, 'http://localhost/PAP/CookUp_Core/uploads/morango.png'),
(204, 'Melancia', 3, 'http://localhost/PAP/CookUp_Core/uploads/melancia.png'),
(205, 'Uvas', 3, 'http://localhost/PAP/CookUp_Core/uploads/uvas.png'),
(206, 'Castanhas', 9, 'http://localhost/PAP/CookUp_Core/uploads/castanhas.png'),
(207, 'Nozes', 9, 'http://localhost/PAP/CookUp_Core/uploads/nozes.png'),
(208, 'Amêndoas', 9, 'http://localhost/PAP/CookUp_Core/uploads/amendoas.png'),
(209, 'Mel', 10, 'http://localhost/PAP/CookUp_Core/uploads/mel.png'),
(210, 'Chocolate', 10, 'http://localhost/PAP/CookUp_Core/uploads/chocolate.png'),
(211, 'Creme de leite', 4, 'http://localhost/PAP/CookUp_Core/uploads/creme_de_leite.png'),
(212, 'Iogurte natural', 4, 'http://localhost/PAP/CookUp_Core/uploads/iogurte_natural.png'),
(213, 'Pão', 11, 'http://localhost/PAP/CookUp_Core/uploads/pao.png'),
(214, 'Tofu', 13, 'http://localhost/PAP/CookUp_Core/uploads/tofu.png'),
(215, 'Grão-de-bico', 8, 'http://localhost/PAP/CookUp_Core/uploads/grao_de_bico.png'),
(216, 'Lentilha', 8, 'http://localhost/PAP/CookUp_Core/uploads/lentilha.png'),
(217, 'Soja', 8, 'http://localhost/PAP/CookUp_Core/uploads/soja.png'),
(218, 'Molho de soja', 12, 'http://localhost/PAP/CookUp_Core/uploads/molho_de_soja.png'),
(219, 'Curry', 1, 'http://localhost/PAP/CookUp_Core/uploads/curry.png'),
(220, 'Canela', 1, 'http://localhost/PAP/CookUp_Core/uploads/canela.png'),
(221, 'Gengibre', 1, 'http://localhost/PAP/CookUp_Core/uploads/gengibre.png'),
(222, 'Coentros', 1, 'http://localhost/PAP/CookUp_Core/uploads/coentros.png'),
(223, 'Manjericão', 1, 'http://localhost/PAP/CookUp_Core/uploads/manjericao.png'),
(224, 'Orégãos', 1, 'http://localhost/PAP/CookUp_Core/uploads/oregaos.png'),
(227, 'farinha', 14, 'flour.jpg'),
(233, 'abacate', 14, 'avocado.jpg'),
(234, 'hdhen', 14, 'default.png'),
(235, 'bdhd', 14, 'default.png'),
(236, 'hebd', 14, 'default.png'),
(237, 'Ingrediente', 14, 'default.png'),
(238, 'Peito de frando', 14, 'default.png'),
(239, 'Dente de alho', 14, 'love.jpg'),
(240, 'Ketchup', 14, 'etchup.jpg'),
(241, 'Mostarda', 14, 'ustard.jpg'),
(242, 'Molho inglês', 14, 'default.png'),
(243, 'Molho de tomate', 14, 'omatosauce.jpg'),
(244, 'Sal e pimenta-do-reino', 14, 'altandblackpepper.jpg'),
(245, 'Champignons fatiados', 14, 'default.png'),
(246, 'Batata palha', 14, 'default.png'),
(247, 'Arroz branco', 14, 'hiterice.jpg'),
(248, 'bdhsjwn', 14, 'default.png'),
(249, 'ccbj', 14, 'default.png');

-- --------------------------------------------------------

--
-- Table structure for table `ingredient_categories`
--

CREATE TABLE `ingredient_categories` (
  `category_id` int(11) NOT NULL,
  `category_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ingredient_categories`
--

INSERT INTO `ingredient_categories` (`category_id`, `category_name`) VALUES
(5, 'Carnes'),
(7, 'Cereais e Massas'),
(10, 'Doces e Sobremesas'),
(3, 'Frutas'),
(9, 'Frutos Secos'),
(4, 'Laticínios'),
(8, 'Leguminosas'),
(12, 'Molhos'),
(14, 'Outros'),
(11, 'Pães e Derivados'),
(6, 'Peixes e Mariscos'),
(1, 'Temperos'),
(13, 'Tofu e Derivados de Soja'),
(2, 'Vegetais');

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
(67, 1, 82, 3, '2025-06-16 20:22:43'),
(68, 1, 83, 5, '2025-06-13 18:53:13');

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
(18, 1, 'Panquecas de Aveia', 'Panquecas saudáveis feitas com aveia e banana.', '1. Mistura a aveia, o leite e os ovos... 2. Cozinha numa frigideira antiaderente...', NULL, 15, 2, 'https://www.anitahealthy.com/wp-content/uploads/2020/11/Panquecas-de-aveia-3.jpg', '2025-04-25 16:56:39', '2025-06-10 21:50:59', 47, 209, 5, 2),
(19, 1, 'Pizza Caseira', 'Pizza caseira com molho de tomate e queijo.', '1. Prepara a massa... 2. Acrescenta os ingredientes e leva ao forno...', NULL, 50, 4, 'https://images.mrcook.app/recipe-image/01932b85-f423-78fc-aeb1-dc2e3c50f40c?cacheKey=U3VuLCAxMiBKYW4gMjAyNSAwMzozODoyNCBHTVQ=', '2025-02-19 00:26:47', '2025-06-05 08:00:16', 0, 6, 0, 0),
(20, 1, 'Salada Caesar', 'Salada clássica com molho Caesar.', '1. Junta a alface, o frango grelhado e os croutons... 2. Mistura o molho e serve...', NULL, 10, 2, 'https://cdn.okchef.com/recipes/01j98qy4vamemvk9d964k15hdd/01jaqcwsn1rg9vy3byy2yf8n8y.jpg', '2025-02-19 00:26:47', '2025-06-10 19:36:16', 0, 11, 0, 0),
(21, 1, 'Tacos Mexicanos', 'Tacos recheados com carne temperada e guacamole.', '1. Cozinha a carne com os temperos... 2. Preenche as tortilhas e adiciona os acompanhamentos...', NULL, 25, 3, 'https://p2.trrsf.com/image/fget/cf/774/0/images.terra.com/2022/03/15/672899432-aprenda-como-fazer-tacos-mexicanos-em-casa-fonte-my-quiet-kitchen-500x500.jpg', '2025-02-19 00:26:47', '2025-06-05 08:00:22', 0, 6, 0, 0),
(22, 1, 'Sushi Tradicional', 'Sushi japonês feito com arroz e peixe fresco.', '1. Cozinha o arroz com vinagre de arroz... 2. Enrola com a alga e corta em pedaços...', NULL, 50, 4, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_1RcR6sp-afOKD4L5OcZ-pUb5rumF9f2NGQ&s', '2025-02-19 00:26:47', '2025-06-05 08:00:35', 0, 6, 0, 0),
(23, 1, 'Esparguete à Bolonhesa', 'Massa italiana com molho de carne e tomate.', '1. Cozinha a massa em água a ferver... 2. Prepara o molho de carne e mistura...', NULL, 35, 5, 'https://images.mrcook.app/recipe-image/0192a1c0-4f98-7c74-b4de-251935c92401?cacheKey=U3VuLCAxMiBKYW4gMjAyNSAwMzozODoyNCBHTVQ=', '2025-02-19 00:26:47', '2025-04-25 17:58:24', 0, 0, 0, 0),
(24, 1, 'Hambúrguer Artesanal', 'Hambúrguer caseiro com carne suculenta e pão brioche.', '1. Molda os hambúrgueres... 2. Grelha até ficar dourado...', NULL, 20, 2, 'https://aseguirniteroi.com.br/wp-content/uploads/2023/05/hamburguer-deck-7.jpg', '2025-02-19 00:26:47', '2025-05-03 15:30:51', 0, 6, 0, 0),
(30, 1, 'Bolo', 'Um fofo e delicioso.', 'Pré-aqueça o forno a 180°C. Misture ovos e mexa bem. Leve ao forno por 35 minutos.', 'Médio', 50, 8, 'https://exemplo.com/bolo.jpg', '2025-04-12 12:53:27', '2025-04-25 22:00:39', 0, 2, 0, 0),
(31, 1, 'Aspernatur', 'Repudiandae ipsum veniam repellendus aspernatur.', '1. Eius ad fugit occaecati tempora provident.\r\n2. Voluptas adipisci laudantium tempora sed ea.', 'Fácil', 41, 3, 'aspernatur.jpg', '2025-04-25 16:56:39', '2025-05-06 20:08:43', 23, 33, 0, 0),
(32, 1, 'Magni dolores', 'Natus est aperiam cupiditate quos error expedita non.', '1. Voluptate cum numquam excepturi temporibus distinctio reprehenderit neque.\r\n2. Cum quasi fugit iste.', NULL, 68, 1, 'magni_dolores.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 3, 2, 0, 0),
(33, 1, 'Sapiente reprehenderit', 'Quod quasi assumenda iusto. Nemo ratione possimus ex voluptatum repellendus sit.', '1. Eos minus ipsum sed ratione eum totam.\r\n2. Et a eveniet dignissimos hic deserunt cumque.', NULL, 52, 7, 'sapiente_reprehenderit.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 19, 41, 0, 0),
(34, 1, 'Iure similique', 'Soluta quasi quos. Labore voluptatum consequatur accusamus exercitationem odio voluptatem nostrum.', '1. Quae officiis eius dolorum aperiam dolores minima.\r\n2. Minima praesentium aperiam vero porro.', 'Fácil', 27, 3, 'iure_similique.jpg', '0000-00-00 00:00:00', '2025-04-25 23:01:01', 27, 11, 0, 0),
(35, 1, 'Cupiditate non nihil', 'Asperiores aut sed facilis dolorem officiis adipisci.', '1. Officiis voluptas molestias aspernatur sequi consectetur deserunt.\r\n2. Eos modi eligendi hic voluptatem.', 'Difícil', 39, 2, 'cupiditate_non_nihil.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 34, 79, 0, 0),
(37, 1, 'Blanditiis debitis', 'Quibusdam beatae perferendis repellat vero atque illo.', '1. Molestiae odit officia quam ad sed fuga.\r\n2. Laboriosam saepe non nisi itaque odio totam.', 'Fácil', 48, 4, 'blanditiis_debitis.jpg', '2025-04-25 16:56:39', '2025-04-26 11:00:01', 17, 202, 0, 0),
(38, 1, 'Ad hic dicta', 'Unde sed accusamus quae. Natus iure ipsam.', '1. Quaerat perspiciatis perspiciatis ipsum at in nam.\r\n2. Possimus ullam temporibus necessitatibus.', NULL, 86, 8, 'ad_hic_dicta.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 16, 21, 0, 0),
(40, 1, 'Voluptatum sint', 'Impedit atque excepturi.', '1. Animi iure doloremque error.\r\n2. Ducimus aliquid corporis et et suscipit aliquam.', NULL, 18, 5, 'voluptatum_sint.jpg', '0000-00-00 00:00:00', '2025-05-02 02:06:16', 24, 143, 0, 0),
(41, 1, 'Sed odio doloremque', 'Minima nihil voluptatibus cupiditate error cumque quos. In aliquam porro impedit pariatur.', '1. Explicabo aliquid nesciunt cumque.\r\n2. Magni accusamus facilis soluta cupiditate voluptatum.', 'Fácil', 74, 6, 'sed_odio_doloremque.jpg', '2025-04-25 16:56:39', '2025-05-06 20:12:32', 2, 140, 0, 0),
(42, 1, 'A unde numquam', 'Possimus totam exercitationem reprehenderit doloribus.', '1. Sunt rem aliquid.\r\n2. Hic quia doloribus eos quas fuga ex.', 'Médio', 66, 6, 'a_unde_numquam.jpg', '0000-00-00 00:00:00', '2025-05-06 20:12:34', 47, 195, 0, 0),
(43, 1, 'Fugit sapiente', 'Nobis dolorum delectus ullam a nemo odio. Eaque pariatur non facere cupiditate.', '1. Maxime quasi sunt dicta illo dolores nisi.\r\n2. Laborum amet possimus fuga non recusandae.', 'Médio', 30, 2, 'fugit_sapiente.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 32, 28, 0, 0),
(44, 1, 'Iusto vero delectus', 'Doloremque facilis quas ducimus illo. Molestias occaecati amet.', '1. Tempore ullam facere error expedita dignissimos cum quia.\r\n2. Praesentium aut aliquam.', 'Médio', 10, 3, 'iusto_vero_delectus.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 24, 96, 0, 0),
(45, 1, 'Nostrum consequatur a', 'Excepturi dicta praesentium doloremque quaerat et.', '1. Doloremque nam perspiciatis ea laborum.\r\n2. Pariatur possimus asperiores perspiciatis itaque magnam hic.', 'Fácil', 17, 3, 'nostrum_consequatur_a.jpg', '0000-00-00 00:00:00', '2025-05-06 20:12:39', 0, 129, 0, 0),
(46, 1, 'Necessitatibus minima velit', 'Rem nesciunt asperiores earum.', '1. Accusamus sequi doloremque nisi incidunt beatae eos.\r\n2. Voluptas sint dignissimos aut quod molestiae eos.', 'Fácil', 56, 5, 'necessitatibus_minima_velit.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 33, 96, 0, 0),
(47, 1, 'Dolorum alias natus', 'Odio dignissimos maxime soluta.', '1. Qui culpa neque dolore.\r\n2. Beatae reiciendis doloremque dolor odio a asperiores.', 'Fácil', 69, 5, 'dolorum_alias_natus.jpg', '0000-00-00 00:00:00', '2025-05-06 20:08:41', 41, 196, 0, 0),
(48, 1, 'Quod unde provident', 'Sed eius facere quam. Ratione consectetur maxime ea.', '1. Aut suscipit quam soluta maxime illum ex.\r\n2. Repellat illo repellat hic tenetur eos commodi.', 'Difícil', 38, 6, 'quod_unde_provident.jpg', '2025-04-25 16:56:39', '2025-05-01 14:30:18', 50, 50, 0, 0),
(49, 1, 'Tenetur accusamus', 'Sit culpa architecto consequuntur debitis. Dolores impedit fugit a cupiditate pariatur dolorum explicabo.', '1. Placeat ratione asperiores autem.\r\n2. Ex cumque rem inventore dolorum dolorum hic.', 'Médio', 59, 3, 'tenetur_accusamus.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 34, 167, 0, 0),
(50, 1, 'Amet iusto', 'Nesciunt mollitia sit alias fugit. Rerum nostrum ipsam vel neque quasi cupiditate.', '1. Fugit aut saepe veritatis temporibus amet labore dicta.\r\n2. Ad pariatur maiores accusamus quidem voluptas dolores hic.', 'Médio', 36, 7, 'amet_iusto.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 15, 159, 0, 0),
(51, 1, 'Ratione inventore rerum', 'Voluptatum illum error.', '1. Corrupti autem quae aperiam.\r\n2. Distinctio ipsa quo ut hic.', 'Difícil', 52, 3, 'ratione_inventore_rerum.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 44, 179, 0, 0),
(52, 1, 'Consequuntur aliquam blanditiis', 'Facere necessitatibus totam corrupti ab ad.', '1. Nihil dolorum molestias blanditiis atque sapiente hic quae.\r\n2. Officia modi distinctio aut modi nostrum iste.', 'Difícil', 49, 8, 'consequuntur_aliquam_blanditiis.jpg', '0000-00-00 00:00:00', '2025-05-06 20:12:36', 3, 176, 0, 0),
(53, 1, 'Porro quas nobis', 'Ipsum voluptas illo iure assumenda quo. Aut nisi quia.', '1. Sapiente sed exercitationem iusto.\r\n2. Rem occaecati unde voluptate.', 'Médio', 42, 4, 'porro_quas_nobis.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 35, 171, 0, 0),
(54, 1, 'Unde recusandae', 'Excepturi tempora animi debitis. Officia incidunt et non.', '1. Praesentium odio reprehenderit officiis corporis minima.\r\n2. Doloremque quam nulla vel assumenda asperiores quia.', 'Médio', 60, 3, 'unde_recusandae.jpg', '0000-00-00 00:00:00', '2025-05-06 17:36:30', 17, 206, 0, 0),
(55, 1, 'Reiciendis ratione corporis', 'Error aut qui soluta.', '1. Minus nesciunt non quae.\r\n2. Impedit reprehenderit dolore assumenda quis.', 'Difícil', 76, 8, 'reiciendis_ratione_corporis.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 47, 101, 0, 0),
(56, 1, 'Quia delectus eos', 'Possimus rem officiis hic. Earum harum deleniti saepe repellat ullam.', '1. Eos tempore voluptas ea beatae quibusdam.\r\n2. Repellat ipsa consequuntur corrupti sequi cum corrupti quas.', 'Fácil', 33, 3, 'quia_delectus_eos.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 21, 138, 0, 0),
(57, 1, 'Inventore exercitationem', 'Nihil dicta quidem voluptate aspernatur magni veniam.', '1. Sapiente voluptatum aperiam expedita ex ratione suscipit adipisci.\r\n2. Quo excepturi possimus eveniet officiis facilis dolorem.', NULL, 50, 7, 'inventore_exercitationem.jpg', '0000-00-00 00:00:00', '2025-05-01 16:21:36', 0, 175, 0, 0),
(58, 1, 'Deserunt reiciendis', 'Quasi excepturi esse natus iusto. Repudiandae dicta minus porro.', '1. Doloribus odio repudiandae.\r\n2. Porro ducimus blanditiis dolorum eius neque.', NULL, 75, 2, 'deserunt_reiciendis.jpg', '0000-00-00 00:00:00', '2025-05-05 15:44:13', 31, 126, 0, 0),
(82, 1, 'Estrogonofe', 'Estrogonofe de ótima qualidade, com ingredientes fáceis e que são um ar vivo ao seu prato, com meu toque mágico.', 'Modo de Preparo\n\nPreparando os ingredientes\nComece cortando o frango em cubinhos médios. Tempere com sal, pimenta-do-reino e, se quiser, um pouco de páprica para dar um toque defumado. Deixe descansar por alguns minutos enquanto pica a cebola e o alho.\n\n\nRefogando a base\nAqueça uma panela grande em fogo médio. Adicione o óleo ou azeite e refogue a cebola até começar a dourar. Acrescente o alho e refogue por mais 1 minuto, sem deixar queimar.\n\n\nCozinhando o frango\nColoque o frango na panela e mexa bem para que sele por todos os lados. Deixe cozinhar até que esteja bem dourado e cozido por dentro. Esse processo leva cerca de 10 a 15 minutos.\n\n\nAdicionando os temperos cremosos\nCom o frango cozido, abaixe o fogo e adicione o ketchup, a mostarda, o molho inglês e o molho de tomate. Misture bem e deixe ferver por uns 5 minutos, para que os sabores se incorporem.\nFinalizando com o creme de leite\n\nPor fim, desligue o fogo e misture o creme de leite delicadamente. Se quiser, adicione os champignons agora. O calor da panela será suficiente para aquecer o creme sem talhar. Ajuste o sal, se necessário.\n\nDicas Extras para um Estrogonofe Inesquecível\n\nFrango mais suculento: Use peito de frango cortado na diagonal, respeitando as fibras, para que fique mais macio.\n\nVersão com carne bovina: Basta substituir o frango por alcatra ou filé mignon, seguindo o mesmo preparo. O tempo de cozimento pode ser um pouco maior.\n\nVegetariano? Substitua o frango por palmito, cogumelos ou proteína de soja hidratada. O resultado também é surpreendente!\n\nMolho mais encorpado: Se quiser um molho mais espesso, adicione uma colher de sopa de farinha de trigo ou amido de milho diluído antes de misturar o creme de leite.', 'Médio', 90, 6, 'recipe_6849f7d12aef62.22357483_48d0c676.jpg', '2025-06-11 21:40:33', '2025-06-17 21:51:56', 34, 10000110, 3, 1),
(83, 1, 'teste', 'hshdusbwb', 'bshsjwbwb', 'Médio', 36, 21, 'recipe_684c738849ab30.15153477_788bce59.jpg', '2025-06-13 18:52:56', '2025-06-16 21:48:36', 27, 29, 5, 1),
(84, 1, 'Teste Semanal', 'Descrição teste', 'Instruções teste', 'Fácil', 20, 2, NULL, '2025-06-16 11:32:37', '2025-06-16 11:32:37', 10, 20, 0, 0);

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
(21, 26),
(82, 21),
(82, 23),
(82, 37),
(83, 33),
(83, 35),
(83, 36);

-- --------------------------------------------------------

--
-- Table structure for table `recipe_gallery`
--

CREATE TABLE `recipe_gallery` (
  `image_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `caption` varchar(100) DEFAULT NULL,
  `ordering` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipe_gallery`
--

INSERT INTO `recipe_gallery` (`image_id`, `recipe_id`, `image_url`, `caption`, `ordering`) VALUES
(154, 83, 'recipe_684c738849ab30.15153477_788bce59.jpg', NULL, 0),
(155, 83, 'recipe_684c738849cdf6.56560480_3d0858ad.jpg', NULL, 1),
(156, 83, 'recipe_684c738849dfb1.70059991_e9f45c6c.jpg', NULL, 2),
(159, 82, 'recipe_6849f7d12aef62.22357483_48d0c676.jpg', NULL, 0),
(160, 82, 'recipe_6849f7d12afd43.44581040_2e79e599.jpg', NULL, 1),
(161, 82, 'recipe_6849f7d12b0a61.86102693_0f3fab61.jpg', NULL, 2);

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
(165, 83, 248, '3d'),
(166, 83, 249, '300g'),
(169, 82, 238, '2'),
(170, 82, 173, '3'),
(171, 82, 239, '3'),
(172, 82, 176, '2'),
(173, 82, 240, '4');

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
(379, 1, 82, '2025-06-11 22:40:38'),
(380, 1, 82, '2025-06-11 22:40:47'),
(381, 1, 82, '2025-06-11 22:41:29'),
(382, 1, 82, '2025-06-11 22:41:55'),
(383, 1, 82, '2025-06-11 22:43:05'),
(384, 1, 82, '2025-06-11 22:43:39'),
(385, 1, 82, '2025-06-11 22:43:44'),
(386, 1, 82, '2025-06-11 22:43:50'),
(387, 1, 82, '2025-06-11 22:46:18'),
(388, 1, 82, '2025-06-11 22:46:52'),
(389, 1, 82, '2025-06-11 22:47:13'),
(390, 1, 82, '2025-06-11 22:48:02'),
(391, 1, 82, '2025-06-11 23:50:29'),
(392, 1, 82, '2025-06-11 23:50:32'),
(393, 1, 82, '2025-06-11 23:55:55'),
(394, 1, 82, '2025-06-11 23:56:05'),
(395, 1, 82, '2025-06-11 23:57:29'),
(396, 1, 82, '2025-06-11 23:57:44'),
(397, 1, 82, '2025-06-12 00:04:26'),
(398, 1, 82, '2025-06-12 00:05:25'),
(399, 1, 82, '2025-06-12 00:05:29'),
(400, 1, 82, '2025-06-12 00:05:34'),
(401, 1, 82, '2025-06-12 00:06:10'),
(402, 1, 82, '2025-06-12 00:06:46'),
(403, 1, 82, '2025-06-12 00:09:49'),
(404, 1, 82, '2025-06-12 00:10:12'),
(405, 1, 82, '2025-06-12 00:11:27'),
(406, 1, 82, '2025-06-12 00:12:06'),
(407, 1, 82, '2025-06-12 00:14:57'),
(408, 1, 82, '2025-06-12 00:16:12'),
(409, 1, 82, '2025-06-12 00:17:42'),
(410, 1, 82, '2025-06-12 00:18:01'),
(411, 1, 82, '2025-06-12 00:19:01'),
(412, 1, 82, '2025-06-12 00:21:45'),
(413, 1, 82, '2025-06-12 00:21:59'),
(414, 1, 82, '2025-06-12 00:22:43'),
(415, 1, 82, '2025-06-12 00:23:27'),
(416, 1, 82, '2025-06-12 00:23:43'),
(417, 1, 82, '2025-06-12 00:23:58'),
(418, 1, 82, '2025-06-12 00:24:11'),
(419, 1, 82, '2025-06-12 00:25:52'),
(420, 1, 82, '2025-06-12 00:26:37'),
(421, 1, 82, '2025-06-12 00:28:55'),
(422, 1, 82, '2025-06-12 00:29:10'),
(423, 1, 82, '2025-06-12 00:29:25'),
(424, 1, 82, '2025-06-12 00:29:39'),
(425, 1, 82, '2025-06-12 00:29:55'),
(426, 1, 82, '2025-06-12 00:32:26'),
(427, 1, 82, '2025-06-12 00:32:38'),
(428, 1, 82, '2025-06-12 00:32:47'),
(429, 1, 82, '2025-06-12 00:33:00'),
(430, 1, 82, '2025-06-12 00:33:13'),
(431, 1, 82, '2025-06-12 00:33:27'),
(432, 1, 82, '2025-06-12 00:36:10'),
(433, 1, 82, '2025-06-12 19:10:56'),
(434, 1, 82, '2025-06-13 11:32:38'),
(435, 1, 82, '2025-06-13 11:33:14'),
(436, 1, 82, '2025-06-13 11:35:32'),
(437, 1, 82, '2025-06-13 11:41:17'),
(438, 1, 82, '2025-06-13 11:44:01'),
(439, 1, 82, '2025-06-13 11:46:17'),
(440, 1, 82, '2025-06-13 11:46:23'),
(441, 1, 82, '2025-06-13 11:46:39'),
(442, 1, 82, '2025-06-13 11:47:03'),
(443, 1, 82, '2025-06-13 11:48:01'),
(444, 1, 82, '2025-06-13 11:48:13'),
(445, 1, 82, '2025-06-13 11:48:50'),
(446, 1, 82, '2025-06-13 11:49:06'),
(447, 1, 82, '2025-06-13 12:19:27'),
(448, 1, 82, '2025-06-13 12:27:28'),
(449, 1, 82, '2025-06-13 12:27:34'),
(450, 1, 82, '2025-06-13 12:27:50'),
(451, 1, 82, '2025-06-13 12:31:14'),
(452, 1, 82, '2025-06-13 12:31:19'),
(453, 1, 82, '2025-06-13 12:32:04'),
(454, 1, 82, '2025-06-13 12:32:08'),
(455, 1, 82, '2025-06-13 12:35:31'),
(456, 1, 82, '2025-06-13 12:45:33'),
(457, 1, 82, '2025-06-13 13:12:13'),
(458, 1, 82, '2025-06-13 13:12:26'),
(459, 1, 82, '2025-06-13 13:14:37'),
(460, 1, 82, '2025-06-13 13:16:03'),
(461, 1, 82, '2025-06-13 13:17:26'),
(462, 1, 82, '2025-06-13 13:17:46'),
(463, 1, 82, '2025-06-13 13:18:09'),
(464, 1, 82, '2025-06-13 13:18:22'),
(465, 1, 82, '2025-06-13 13:18:28'),
(466, 1, 82, '2025-06-13 13:18:33'),
(467, 1, 82, '2025-06-13 13:19:41'),
(468, 1, 82, '2025-06-13 13:19:56'),
(469, 1, 82, '2025-06-13 13:20:25'),
(470, 1, 82, '2025-06-13 13:31:57'),
(471, 1, 82, '2025-06-13 13:32:37'),
(472, 1, 82, '2025-06-13 13:33:54'),
(473, 1, 82, '2025-06-13 13:37:24'),
(474, 1, 82, '2025-06-13 13:38:22'),
(475, 1, 82, '2025-06-13 13:38:29'),
(476, 1, 82, '2025-06-13 13:38:43'),
(477, 1, 82, '2025-06-13 13:38:48'),
(478, 1, 82, '2025-06-13 13:38:54'),
(479, 1, 82, '2025-06-13 13:40:16'),
(480, 1, 82, '2025-06-13 13:42:09'),
(481, 1, 82, '2025-06-13 13:43:20'),
(482, 1, 82, '2025-06-13 13:56:19'),
(483, 1, 82, '2025-06-13 14:00:05'),
(484, 1, 82, '2025-06-13 14:02:00'),
(485, 1, 82, '2025-06-13 14:02:59'),
(486, 1, 82, '2025-06-13 14:11:49'),
(487, 1, 82, '2025-06-13 14:12:25'),
(488, 1, 82, '2025-06-13 14:29:31'),
(489, 1, 82, '2025-06-13 14:34:28'),
(490, 1, 82, '2025-06-13 14:36:52'),
(491, 1, 82, '2025-06-13 14:39:25'),
(492, 1, 82, '2025-06-13 14:39:29'),
(493, 1, 82, '2025-06-13 14:39:31'),
(494, 1, 82, '2025-06-13 14:41:42'),
(495, 1, 82, '2025-06-13 14:42:45'),
(496, 1, 82, '2025-06-13 14:43:15'),
(497, 1, 82, '2025-06-13 14:43:22'),
(498, 1, 82, '2025-06-13 14:43:40'),
(499, 1, 82, '2025-06-13 14:43:44'),
(500, 1, 82, '2025-06-13 14:44:02'),
(501, 1, 82, '2025-06-13 14:44:14'),
(502, 1, 82, '2025-06-13 14:44:28'),
(503, 1, 82, '2025-06-13 14:44:40'),
(504, 1, 82, '2025-06-13 14:44:52'),
(505, 1, 82, '2025-06-13 14:47:14'),
(506, 1, 82, '2025-06-13 14:47:34'),
(507, 1, 82, '2025-06-13 14:48:03'),
(508, 1, 82, '2025-06-13 14:48:11'),
(509, 1, 82, '2025-06-13 14:48:16'),
(510, 1, 82, '2025-06-13 15:39:14'),
(511, 1, 82, '2025-06-13 15:39:34'),
(512, 1, 82, '2025-06-13 15:43:12'),
(513, 1, 82, '2025-06-13 15:45:58'),
(514, 1, 82, '2025-06-13 15:47:41'),
(515, 1, 82, '2025-06-13 15:49:26'),
(516, 1, 82, '2025-06-13 15:50:13'),
(517, 1, 82, '2025-06-13 15:53:18'),
(518, 1, 82, '2025-06-13 15:58:12'),
(519, 1, 82, '2025-06-13 16:00:10'),
(520, 1, 82, '2025-06-13 16:14:47'),
(521, 1, 82, '2025-06-13 16:16:07'),
(522, 1, 82, '2025-06-13 16:16:26'),
(523, 1, 82, '2025-06-13 16:16:36'),
(524, 1, 82, '2025-06-13 16:17:02'),
(525, 1, 82, '2025-06-13 16:18:11'),
(526, 1, 82, '2025-06-13 16:18:19'),
(527, 1, 82, '2025-06-13 16:18:40'),
(528, 1, 82, '2025-06-13 16:18:49'),
(529, 1, 82, '2025-06-13 16:19:20'),
(530, 1, 82, '2025-06-13 16:21:55'),
(531, 1, 82, '2025-06-13 16:22:42'),
(532, 1, 82, '2025-06-13 16:23:52'),
(533, 1, 82, '2025-06-13 16:36:54'),
(534, 1, 82, '2025-06-13 16:38:49'),
(535, 1, 82, '2025-06-13 16:39:21'),
(536, 1, 82, '2025-06-13 16:39:42'),
(537, 1, 82, '2025-06-13 16:40:10'),
(538, 1, 82, '2025-06-13 16:40:42'),
(539, 1, 82, '2025-06-13 16:40:53'),
(540, 1, 82, '2025-06-13 16:41:04'),
(541, 1, 82, '2025-06-13 16:41:16'),
(542, 1, 82, '2025-06-13 16:41:29'),
(543, 1, 82, '2025-06-13 16:42:35'),
(544, 1, 82, '2025-06-13 16:43:24'),
(545, 1, 82, '2025-06-13 16:44:31'),
(546, 1, 82, '2025-06-13 16:44:39'),
(547, 1, 82, '2025-06-13 16:46:37'),
(548, 1, 82, '2025-06-13 16:47:34'),
(549, 1, 82, '2025-06-13 16:48:02'),
(550, 1, 82, '2025-06-13 16:48:08'),
(551, 1, 82, '2025-06-13 16:48:27'),
(552, 1, 82, '2025-06-13 16:50:21'),
(553, 1, 82, '2025-06-13 16:51:46'),
(554, 1, 82, '2025-06-13 16:52:01'),
(555, 1, 82, '2025-06-13 16:56:07'),
(556, 1, 82, '2025-06-13 16:56:15'),
(557, 1, 82, '2025-06-13 17:06:28'),
(558, 1, 82, '2025-06-13 17:08:20'),
(559, 1, 82, '2025-06-13 17:09:53'),
(560, 1, 82, '2025-06-13 17:10:16'),
(561, 1, 82, '2025-06-13 17:12:41'),
(562, 1, 82, '2025-06-13 17:52:18'),
(563, 1, 82, '2025-06-13 17:54:15'),
(564, 1, 82, '2025-06-13 17:55:11'),
(565, 1, 82, '2025-06-13 18:03:52'),
(566, 1, 82, '2025-06-13 18:04:43'),
(567, 1, 82, '2025-06-13 18:05:00'),
(568, 1, 82, '2025-06-13 18:05:20'),
(569, 1, 82, '2025-06-13 18:05:34'),
(570, 1, 82, '2025-06-13 18:05:49'),
(571, 1, 82, '2025-06-13 18:06:09'),
(572, 1, 82, '2025-06-13 18:06:53'),
(573, 1, 82, '2025-06-13 18:19:38'),
(574, 1, 82, '2025-06-13 18:19:55'),
(575, 1, 82, '2025-06-13 18:21:42'),
(576, 1, 82, '2025-06-13 18:21:57'),
(577, 1, 82, '2025-06-13 18:24:04'),
(578, 1, 82, '2025-06-13 18:24:56'),
(579, 1, 82, '2025-06-13 18:25:32'),
(580, 1, 82, '2025-06-13 18:27:52'),
(581, 1, 82, '2025-06-13 18:29:01'),
(582, 1, 82, '2025-06-13 18:29:17'),
(583, 1, 82, '2025-06-13 18:29:46'),
(584, 1, 82, '2025-06-13 18:30:48'),
(585, 1, 82, '2025-06-13 18:35:50'),
(586, 1, 82, '2025-06-13 18:36:11'),
(587, 1, 82, '2025-06-13 18:36:32'),
(588, 1, 82, '2025-06-13 18:38:28'),
(589, 1, 82, '2025-06-13 18:39:03'),
(590, 1, 82, '2025-06-13 18:43:03'),
(591, 1, 82, '2025-06-13 18:46:33'),
(592, 1, 82, '2025-06-13 18:47:58'),
(593, 1, 82, '2025-06-13 18:51:42'),
(594, 1, 82, '2025-06-13 18:54:08'),
(595, 1, 82, '2025-06-13 18:56:29'),
(596, 1, 82, '2025-06-13 18:57:17'),
(597, 1, 82, '2025-06-13 18:58:36'),
(598, 1, 82, '2025-06-13 18:59:10'),
(599, 1, 82, '2025-06-13 18:59:54'),
(600, 1, 82, '2025-06-13 19:02:53'),
(601, 1, 82, '2025-06-13 19:03:16'),
(602, 1, 82, '2025-06-13 19:04:30'),
(603, 1, 82, '2025-06-13 19:06:29'),
(604, 1, 82, '2025-06-13 19:11:50'),
(605, 1, 82, '2025-06-13 19:12:53'),
(606, 1, 82, '2025-06-13 19:13:02'),
(607, 1, 82, '2025-06-13 19:15:37'),
(608, 1, 82, '2025-06-13 19:30:37'),
(609, 1, 82, '2025-06-13 19:35:51'),
(610, 1, 82, '2025-06-13 19:35:53'),
(611, 1, 82, '2025-06-13 19:35:54'),
(612, 1, 83, '2025-06-13 19:53:09'),
(613, 1, 83, '2025-06-13 19:53:21'),
(614, 1, 83, '2025-06-13 20:26:29'),
(615, 1, 82, '2025-06-13 20:29:28'),
(616, 1, 83, '2025-06-13 20:32:51'),
(617, 1, 82, '2025-06-13 20:43:02'),
(618, 1, 82, '2025-06-13 20:43:14'),
(619, 1, 82, '2025-06-13 20:43:24'),
(620, 1, 82, '2025-06-13 20:52:15'),
(621, 1, 82, '2025-06-13 20:52:38'),
(622, 1, 82, '2025-06-13 21:17:05'),
(623, 1, 82, '2025-06-13 21:17:19'),
(624, 1, 82, '2025-06-13 21:17:23'),
(625, 1, 83, '2025-06-13 21:17:41'),
(626, 1, 82, '2025-06-13 21:18:29'),
(627, 1, 82, '2025-06-13 21:35:10'),
(628, 1, 82, '2025-06-13 21:35:14'),
(629, 1, 83, '2025-06-13 21:39:33'),
(630, 1, 83, '2025-06-13 21:39:35'),
(631, 1, 83, '2025-06-13 21:50:57'),
(632, 1, 82, '2025-06-13 21:51:00'),
(633, 1, 82, '2025-06-13 21:55:19'),
(635, 1, 82, '2025-06-13 22:07:37'),
(636, 1, 82, '2025-06-13 22:08:07'),
(637, 1, 82, '2025-06-13 22:08:12'),
(638, 1, 82, '2025-06-13 23:34:10'),
(639, 1, 82, '2025-06-14 15:26:00'),
(640, 1, 82, '2025-06-14 15:26:29'),
(641, 1, 83, '2025-06-14 15:42:15'),
(642, 1, 83, '2025-06-14 16:40:13'),
(643, 1, 82, '2025-06-14 16:40:15'),
(644, 1, 82, '2025-06-14 16:40:19'),
(645, 1, 82, '2025-06-14 16:40:22'),
(646, 1, 82, '2025-06-14 18:49:15'),
(647, 1, 82, '2025-06-14 19:38:12'),
(648, 1, 82, '2025-06-14 21:07:26'),
(649, 1, 83, '2025-06-14 21:07:31'),
(650, 1, 83, '2025-06-14 22:17:30'),
(651, 1, 82, '2025-06-14 22:32:04'),
(652, 1, 82, '2025-06-14 22:32:39'),
(653, 1, 82, '2025-06-14 22:49:03'),
(654, 1, 82, '2025-06-14 22:55:01'),
(655, 1, 82, '2025-06-14 22:58:06'),
(656, 1, 82, '2025-06-14 23:32:54'),
(657, 1, 82, '2025-06-14 23:35:18'),
(658, 1, 82, '2025-06-15 00:42:47'),
(659, 1, 82, '2025-06-15 00:43:01'),
(660, 1, 82, '2025-06-15 00:45:21'),
(661, 1, 82, '2025-06-15 00:45:26'),
(662, 1, 82, '2025-06-15 00:45:29'),
(663, 1, 82, '2025-06-15 00:45:42'),
(664, 1, 82, '2025-06-15 00:46:05'),
(665, 1, 82, '2025-06-15 00:46:31'),
(666, 1, 82, '2025-06-15 00:49:41'),
(667, 1, 82, '2025-06-15 00:50:02'),
(668, 1, 82, '2025-06-15 00:50:57'),
(669, 1, 82, '2025-06-15 00:51:20'),
(670, 1, 82, '2025-06-15 00:51:42'),
(671, 1, 82, '2025-06-15 00:59:36'),
(672, 1, 82, '2025-06-15 01:00:17'),
(673, 1, 82, '2025-06-15 01:00:36'),
(674, 1, 82, '2025-06-15 01:04:59'),
(675, 1, 83, '2025-06-15 01:05:06'),
(676, 1, 82, '2025-06-15 01:14:40'),
(677, 1, 82, '2025-06-15 01:15:32'),
(678, 1, 82, '2025-06-15 01:15:34'),
(679, 1, 82, '2025-06-15 01:15:36'),
(680, 1, 82, '2025-06-15 01:15:38'),
(681, 1, 82, '2025-06-15 01:15:41'),
(682, 1, 83, '2025-06-15 01:15:43'),
(683, 1, 83, '2025-06-15 01:15:46'),
(684, 1, 82, '2025-06-15 01:15:48'),
(685, 1, 82, '2025-06-15 01:18:20'),
(686, 1, 82, '2025-06-15 01:18:22'),
(687, 1, 82, '2025-06-15 01:18:27'),
(688, 1, 83, '2025-06-15 01:18:31'),
(689, 1, 82, '2025-06-15 01:21:10'),
(690, 1, 82, '2025-06-15 01:58:30'),
(691, 1, 82, '2025-06-15 01:58:56'),
(692, 1, 82, '2025-06-15 18:02:26'),
(693, 1, 82, '2025-06-15 19:35:33'),
(694, 1, 83, '2025-06-15 20:14:37'),
(695, 1, 82, '2025-06-15 20:39:14'),
(696, 1, 82, '2025-06-15 21:01:18'),
(697, 1, 82, '2025-06-15 21:01:21'),
(698, 1, 83, '2025-06-15 21:04:57'),
(699, 1, 83, '2025-06-15 21:05:22'),
(700, 1, 82, '2025-06-15 21:28:12'),
(701, 1, 83, '2025-06-15 21:28:46'),
(702, 1, 82, '2025-06-15 21:30:02'),
(703, 1, 82, '2025-06-15 21:32:28'),
(704, 1, 82, '2025-06-15 21:32:34'),
(705, 1, 83, '2025-06-15 21:32:36'),
(706, 1, 82, '2025-06-15 23:32:38'),
(707, 1, 82, '2025-06-15 23:37:44'),
(708, 1, 82, '2025-06-16 11:28:18'),
(709, 1, 82, '2025-06-16 11:28:21'),
(710, 1, 83, '2025-06-16 11:28:23'),
(711, 1, 83, '2025-06-16 11:28:25'),
(712, 1, 82, '2025-06-16 11:28:27'),
(713, 1, 82, '2025-06-16 12:20:24'),
(714, 1, 82, '2025-06-16 13:08:29'),
(715, 1, 82, '2025-06-16 14:14:49'),
(716, 1, 83, '2025-06-16 14:55:09'),
(717, 1, 83, '2025-06-16 14:55:11'),
(718, 1, 82, '2025-06-16 14:55:12'),
(719, 1, 82, '2025-06-16 15:01:13'),
(720, 1, 83, '2025-06-16 15:01:15'),
(721, 1, 82, '2025-06-16 15:46:59'),
(723, 1, 83, '2025-06-16 17:01:06'),
(724, 1, 82, '2025-06-16 17:06:16'),
(725, 1, 82, '2025-06-16 18:17:25'),
(726, 1, 82, '2025-06-16 20:40:43'),
(727, 1, 82, '2025-06-16 20:59:20'),
(728, 1, 82, '2025-06-16 21:06:11'),
(729, 1, 82, '2025-06-16 21:09:39'),
(730, 1, 82, '2025-06-16 21:19:25'),
(731, 1, 82, '2025-06-16 21:21:16'),
(732, 1, 82, '2025-06-16 21:21:57'),
(733, 1, 82, '2025-06-16 21:22:49'),
(734, 1, 82, '2025-06-16 21:28:04'),
(735, 1, 82, '2025-06-16 21:28:20'),
(736, 1, 82, '2025-06-16 21:41:44'),
(737, 1, 82, '2025-06-16 22:18:19'),
(738, 1, 82, '2025-06-16 22:21:02'),
(739, 1, 82, '2025-06-16 22:24:24'),
(740, 1, 82, '2025-06-16 22:28:29'),
(741, 1, 82, '2025-06-16 22:34:05'),
(742, 1, 82, '2025-06-16 22:34:10'),
(743, 1, 82, '2025-06-16 22:35:55'),
(744, 1, 82, '2025-06-16 22:39:28'),
(745, 1, 82, '2025-06-16 22:40:55'),
(746, 1, 82, '2025-06-16 22:42:15'),
(747, 1, 82, '2025-06-16 22:42:17'),
(748, 1, 82, '2025-06-16 22:43:40'),
(749, 1, 82, '2025-06-16 22:45:11'),
(750, 1, 82, '2025-06-16 22:46:25'),
(751, 1, 82, '2025-06-16 22:47:44'),
(752, 1, 82, '2025-06-16 22:48:33'),
(753, 1, 83, '2025-06-16 22:48:36'),
(754, 1, 82, '2025-06-16 22:49:37'),
(755, 1, 82, '2025-06-16 22:49:44'),
(756, 1, 82, '2025-06-16 22:51:44'),
(757, 1, 82, '2025-06-16 22:52:22'),
(758, 1, 82, '2025-06-16 23:12:30'),
(761, 1, 82, '2025-06-16 23:49:04'),
(762, 1, 82, '2025-06-16 23:49:16'),
(763, 1, 82, '2025-06-16 23:56:55'),
(764, 1, 82, '2025-06-17 00:03:31'),
(766, 1, 82, '2025-06-17 19:41:51'),
(770, 1, 82, '2025-06-17 22:51:05');

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
(35, 1, 'ola', '2025-04-11 13:40:31', '#FFF0F5'),
(63, 13, 'ola', '2025-04-30 22:29:23', '#FFF0F5'),
(66, 13, 'hdh', '2025-05-09 15:20:50', '#FAD6A5'),
(67, 113, 'Tralalero Tralala', '2025-06-05 16:22:46', '#B2EBF2'),
(74, 1, 'js', '2025-06-15 22:35:39', '#FFF9DB');

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
(499, 35, 21, '2025-06-15 22:37:16'),
(506, 35, 18, '2025-06-16 10:28:03'),
(515, 74, 19, '2025-06-16 16:24:45');

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
  `role` varchar(10) DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `firebase_uid`, `username`, `email`, `profile_picture`, `created_at`, `updated_at`, `role`) VALUES
(1, 'ZA3PDVCrOrPzQKA2lST3lQVU1c23', 'adm', 'admin@gmail.com', 'profile_685088c1e62dc6.84446908_8e03d1c2.jpg', '2025-04-05 12:56:27', '2025-06-16 23:05:51', 'user'),
(13, 'ngl7Ux7HDlRsW3Xe0MpVzl37Pwf2', 'sayo santos', 'sayo.santos1306@gmail.com', 'https://lh3.googleusercontent.com/a/ACg8ocL9ANhIPt5cXYQkxgBQrH4v-61VDx_IbjNWgZfDaJxuiHZLu2M=s96-c', '2025-03-31 14:33:20', '2025-06-06 11:07:01', 'user'),
(113, 'tnDlwSs79egAxuLkY48TLcxTQru2', 'Kauan Matias', 'kauanmatiasfortunato@gmail.com', 'https://lh3.googleusercontent.com/a/ACg8ocLPSS2ksk3GCdauX57r3wepuYSr2T0Th10xADnCbUIvibqjsGzR=s96-c', '2025-06-05 16:21:22', '2025-06-05 16:27:28', 'user');

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
(1, 21, 369, 35, 5),
(1, 22, 35, 9, 2),
(1, 23, 503, 51, 13),
(1, 24, 3, 6, 0),
(1, 26, 3, 2, 0),
(1, 31, 35, 1, 1),
(1, 32, 2, 0, 0),
(1, 33, 51, 33, 4),
(1, 35, 28, 26, 3),
(1, 36, 29, 26, 3),
(1, 37, 357, 34, 4),
(1, 38, 33, 1, 1),
(1, 40, 1, 0, 0),
(1, 41, 168, 15, 9),
(1, 42, 24, 7, 1),
(1, 49, 2, 0, 0),
(1, 52, 1, 0, 0),
(13, 22, 6, 1, 5),
(13, 23, 15, 2, 3),
(13, 33, 6, 1, 5),
(13, 41, 15, 2, 3),
(13, 42, 6, 1, 5),
(113, 22, 3, 1, 2),
(113, 23, 3, 0, 0),
(113, 33, 3, 1, 2),
(113, 41, 3, 0, 0),
(113, 42, 3, 1, 2);

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
(36, 1, 82, '2025-06-11 22:41:24'),
(37, 1, 83, '2025-06-13 19:53:13'),
(38, 1, 83, '2025-06-13 21:39:37'),
(39, 1, 83, '2025-06-13 21:50:59'),
(40, 1, 82, '2025-06-13 21:51:02'),
(41, 1, 82, '2025-06-13 21:55:21'),
(44, 1, 82, '2025-06-16 21:22:43');

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
  ADD UNIQUE KEY `ingredient_name` (`ingredient_name`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `ingredient_categories`
--
ALTER TABLE `ingredient_categories`
  ADD PRIMARY KEY (`category_id`),
  ADD UNIQUE KEY `category_name` (`category_name`);

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
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=62;

--
-- AUTO_INCREMENT for table `comments`
--
ALTER TABLE `comments`
  MODIFY `comment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT for table `ingredients`
--
ALTER TABLE `ingredients`
  MODIFY `ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=251;

--
-- AUTO_INCREMENT for table `ingredient_categories`
--
ALTER TABLE `ingredient_categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `rating_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=72;

--
-- AUTO_INCREMENT for table `recipes`
--
ALTER TABLE `recipes`
  MODIFY `recipe_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=89;

--
-- AUTO_INCREMENT for table `recipe_gallery`
--
ALTER TABLE `recipe_gallery`
  MODIFY `image_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=166;

--
-- AUTO_INCREMENT for table `recipe_ingredients`
--
ALTER TABLE `recipe_ingredients`
  MODIFY `recipe_ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=180;

--
-- AUTO_INCREMENT for table `recipe_views`
--
ALTER TABLE `recipe_views`
  MODIFY `view_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=772;

--
-- AUTO_INCREMENT for table `saved_lists`
--
ALTER TABLE `saved_lists`
  MODIFY `list_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;

--
-- AUTO_INCREMENT for table `saved_recipes`
--
ALTER TABLE `saved_recipes`
  MODIFY `saved_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=519;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT for table `user_recipe_finished`
--
ALTER TABLE `user_recipe_finished`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

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
-- Constraints for table `ingredients`
--
ALTER TABLE `ingredients`
  ADD CONSTRAINT `ingredients_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `ingredient_categories` (`category_id`) ON DELETE SET NULL;

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
