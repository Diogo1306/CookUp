-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 17, 2025 at 03:31 PM
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
(32, 1, 3, 'Fixe', '2025-05-11 18:40:24'),
(33, 1, 3, 'gostei muito', '2025-05-11 18:40:37'),
(34, 1, 18, 'Top', '2025-05-11 18:41:44'),
(35, 1, 3, 'top', '2025-05-11 18:49:55'),
(36, 1, 3, 'Acrecente mais sal se nao gosta do peixe', '2025-05-11 18:50:11'),
(37, 1, 3, 'top', '2025-05-11 19:00:30'),
(38, 1, 3, 'Gostei', '2025-05-11 19:00:35');

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
(224, 'Orégãos', 1, 'http://localhost/PAP/CookUp_Core/uploads/oregaos.png');

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
(51, 1, 3, 4, '2025-05-11 19:00:30'),
(52, 1, 18, 3, '2025-05-11 18:41:48');

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
  `views_count` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipes`
--

INSERT INTO `recipes` (`recipe_id`, `author_id`, `title`, `description`, `instructions`, `difficulty`, `preparation_time`, `servings`, `image`, `created_at`, `updated_at`, `favorites_count`, `views_count`) VALUES
(3, 13, 'Churros Recheados', 'Churros crocantes por fora e macios por dentro, recheados com diversos sabores como Nutella, doce de leite e baunilha.ggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg', '1. Aqueça a água com manteiga, açúcar e sal.\n2. Adicione a farinha e mexa até formar uma massa homogênea.\n3. Deixe esfriar e misture o ovo.\n4. Modele com bico de confeitar e frite até dourar.\n5. Passe no açúcar e canela.\n6. Recheie com Nutella, doce de leite, creme de baunilha ou geleia.\n7. Sirva quente e aproveite!', 'dificil', 30, 6, 'https://static.itdg.com.br/images/640-440/b0a2d7797c9b1174ec771c88d64d2322/324392-original.jpg', '2025-04-25 17:56:39', '2025-05-11 19:00:51', 29, 124),
(18, 13, 'Panquecas de Aveia', 'Panquecas saudáveis feitas com aveia e banana.', '1. Mistura a aveia, o leite e os ovos... 2. Cozinha numa frigideira antiaderente...', NULL, 15, 2, 'https://www.anitahealthy.com/wp-content/uploads/2020/11/Panquecas-de-aveia-3.jpg', '2025-04-25 17:56:39', '2025-05-17 12:46:42', 39, 179),
(19, 13, 'Pizza Caseira', 'Pizza caseira com molho de tomate e queijo.', '1. Prepara a massa... 2. Acrescenta os ingredientes e leva ao forno...', NULL, 50, 4, 'https://images.mrcook.app/recipe-image/01932b85-f423-78fc-aeb1-dc2e3c50f40c?cacheKey=U3VuLCAxMiBKYW4gMjAyNSAwMzozODoyNCBHTVQ=', '2025-02-19 00:26:47', '2025-05-06 21:12:23', 0, 6),
(20, 13, 'Salada Caesar', 'Salada clássica com molho Caesar.', '1. Junta a alface, o frango grelhado e os croutons... 2. Mistura o molho e serve...', NULL, 10, 2, 'https://cdn.okchef.com/recipes/01j98qy4vamemvk9d964k15hdd/01jaqcwsn1rg9vy3byy2yf8n8y.jpg', '2025-02-19 00:26:47', '2025-05-06 21:12:20', 0, 10),
(21, 13, 'Tacos Mexicanos', 'Tacos recheados com carne temperada e guacamole.', '1. Cozinha a carne com os temperos... 2. Preenche as tortilhas e adiciona os acompanhamentos...', NULL, 25, 3, 'https://p2.trrsf.com/image/fget/cf/774/0/images.terra.com/2022/03/15/672899432-aprenda-como-fazer-tacos-mexicanos-em-casa-fonte-my-quiet-kitchen-500x500.jpg', '2025-02-19 00:26:47', '2025-04-26 11:48:37', 0, 6),
(22, 13, 'Sushi Tradicional', 'Sushi japonês feito com arroz e peixe fresco.', '1. Cozinha o arroz com vinagre de arroz... 2. Enrola com a alga e corta em pedaços...', NULL, 50, 4, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_1RcR6sp-afOKD4L5OcZ-pUb5rumF9f2NGQ&s', '2025-02-19 00:26:47', '2025-05-06 21:08:40', 0, 6),
(23, 13, 'Esparguete à Bolonhesa', 'Massa italiana com molho de carne e tomate.', '1. Cozinha a massa em água a ferver... 2. Prepara o molho de carne e mistura...', NULL, 35, 5, 'https://images.mrcook.app/recipe-image/0192a1c0-4f98-7c74-b4de-251935c92401?cacheKey=U3VuLCAxMiBKYW4gMjAyNSAwMzozODoyNCBHTVQ=', '2025-02-19 00:26:47', '2025-04-25 18:58:24', 0, 0),
(24, 13, 'Hambúrguer Artesanal', 'Hambúrguer caseiro com carne suculenta e pão brioche.', '1. Molda os hambúrgueres... 2. Grelha até ficar dourado...', NULL, 20, 2, 'https://aseguirniteroi.com.br/wp-content/uploads/2023/05/hamburguer-deck-7.jpg', '2025-02-19 00:26:47', '2025-05-03 16:30:51', 0, 6),
(30, 1, 'Bolo', 'Um fofo e delicioso.', 'Pré-aqueça o forno a 180°C. Misture ovos e mexa bem. Leve ao forno por 35 minutos.', 'Médio', 50, 8, 'https://exemplo.com/bolo.jpg', '2025-04-12 13:53:27', '2025-04-25 23:00:39', 0, 2),
(31, 1, 'Aspernatur', 'Repudiandae ipsum veniam repellendus aspernatur.', '1. Eius ad fugit occaecati tempora provident.\r\n2. Voluptas adipisci laudantium tempora sed ea.', 'Fácil', 41, 3, 'aspernatur.jpg', '2025-04-25 17:56:39', '2025-05-06 21:08:43', 23, 33),
(32, 1, 'Magni dolores', 'Natus est aperiam cupiditate quos error expedita non.', '1. Voluptate cum numquam excepturi temporibus distinctio reprehenderit neque.\r\n2. Cum quasi fugit iste.', NULL, 68, 1, 'magni_dolores.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 3, 2),
(33, 1, 'Sapiente reprehenderit', 'Quod quasi assumenda iusto. Nemo ratione possimus ex voluptatum repellendus sit.', '1. Eos minus ipsum sed ratione eum totam.\r\n2. Et a eveniet dignissimos hic deserunt cumque.', NULL, 52, 7, 'sapiente_reprehenderit.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 19, 41),
(34, 1, 'Iure similique', 'Soluta quasi quos. Labore voluptatum consequatur accusamus exercitationem odio voluptatem nostrum.', '1. Quae officiis eius dolorum aperiam dolores minima.\r\n2. Minima praesentium aperiam vero porro.', 'Fácil', 27, 3, 'iure_similique.jpg', '0000-00-00 00:00:00', '2025-04-26 00:01:01', 27, 11),
(35, 1, 'Cupiditate non nihil', 'Asperiores aut sed facilis dolorem officiis adipisci.', '1. Officiis voluptas molestias aspernatur sequi consectetur deserunt.\r\n2. Eos modi eligendi hic voluptatem.', 'Difícil', 39, 2, 'cupiditate_non_nihil.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 34, 79),
(36, 1, 'Omnis assumenda', 'Iure sit beatae veritatis sequi provident blanditiis.', '1. Sapiente veniam eum necessitatibus cumque praesentium doloribus.\r\n2. Tempora in vero illo rerum tempore quod.', 'Difícil', 83, 2, 'omnis_assumenda.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 19, 161),
(37, 1, 'Blanditiis debitis', 'Quibusdam beatae perferendis repellat vero atque illo.', '1. Molestiae odit officia quam ad sed fuga.\r\n2. Laboriosam saepe non nisi itaque odio totam.', 'Fácil', 48, 4, 'blanditiis_debitis.jpg', '2025-04-25 17:56:39', '2025-04-26 12:00:01', 17, 202),
(38, 1, 'Ad hic dicta', 'Unde sed accusamus quae. Natus iure ipsam.', '1. Quaerat perspiciatis perspiciatis ipsum at in nam.\r\n2. Possimus ullam temporibus necessitatibus.', NULL, 86, 8, 'ad_hic_dicta.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 16, 21),
(39, 1, 'Voluptas autem', 'Vel deserunt consequuntur nulla odit eaque.', '1. Aliquam libero vitae assumenda quia inventore.\r\n2. Aperiam itaque unde labore.', 'Médio', 76, 7, 'voluptas_autem.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 40, 6),
(40, 1, 'Voluptatum sint', 'Impedit atque excepturi.', '1. Animi iure doloremque error.\r\n2. Ducimus aliquid corporis et et suscipit aliquam.', NULL, 18, 5, 'voluptatum_sint.jpg', '0000-00-00 00:00:00', '2025-05-02 03:06:16', 24, 143),
(41, 1, 'Sed odio doloremque', 'Minima nihil voluptatibus cupiditate error cumque quos. In aliquam porro impedit pariatur.', '1. Explicabo aliquid nesciunt cumque.\r\n2. Magni accusamus facilis soluta cupiditate voluptatum.', 'Fácil', 74, 6, 'sed_odio_doloremque.jpg', '2025-04-25 17:56:39', '2025-05-06 21:12:32', 2, 140),
(42, 1, 'A unde numquam', 'Possimus totam exercitationem reprehenderit doloribus.', '1. Sunt rem aliquid.\r\n2. Hic quia doloribus eos quas fuga ex.', 'Médio', 66, 6, 'a_unde_numquam.jpg', '0000-00-00 00:00:00', '2025-05-06 21:12:34', 47, 195),
(43, 1, 'Fugit sapiente', 'Nobis dolorum delectus ullam a nemo odio. Eaque pariatur non facere cupiditate.', '1. Maxime quasi sunt dicta illo dolores nisi.\r\n2. Laborum amet possimus fuga non recusandae.', 'Médio', 30, 2, 'fugit_sapiente.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 32, 28),
(44, 1, 'Iusto vero delectus', 'Doloremque facilis quas ducimus illo. Molestias occaecati amet.', '1. Tempore ullam facere error expedita dignissimos cum quia.\r\n2. Praesentium aut aliquam.', 'Médio', 10, 3, 'iusto_vero_delectus.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 24, 96),
(45, 1, 'Nostrum consequatur a', 'Excepturi dicta praesentium doloremque quaerat et.', '1. Doloremque nam perspiciatis ea laborum.\r\n2. Pariatur possimus asperiores perspiciatis itaque magnam hic.', 'Fácil', 17, 3, 'nostrum_consequatur_a.jpg', '0000-00-00 00:00:00', '2025-05-06 21:12:39', 0, 129),
(46, 1, 'Necessitatibus minima velit', 'Rem nesciunt asperiores earum.', '1. Accusamus sequi doloremque nisi incidunt beatae eos.\r\n2. Voluptas sint dignissimos aut quod molestiae eos.', 'Fácil', 56, 5, 'necessitatibus_minima_velit.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 33, 96),
(47, 1, 'Dolorum alias natus', 'Odio dignissimos maxime soluta.', '1. Qui culpa neque dolore.\r\n2. Beatae reiciendis doloremque dolor odio a asperiores.', 'Fácil', 69, 5, 'dolorum_alias_natus.jpg', '0000-00-00 00:00:00', '2025-05-06 21:08:41', 41, 196),
(48, 1, 'Quod unde provident', 'Sed eius facere quam. Ratione consectetur maxime ea.', '1. Aut suscipit quam soluta maxime illum ex.\r\n2. Repellat illo repellat hic tenetur eos commodi.', 'Difícil', 38, 6, 'quod_unde_provident.jpg', '2025-04-25 17:56:39', '2025-05-01 15:30:18', 50, 50),
(49, 1, 'Tenetur accusamus', 'Sit culpa architecto consequuntur debitis. Dolores impedit fugit a cupiditate pariatur dolorum explicabo.', '1. Placeat ratione asperiores autem.\r\n2. Ex cumque rem inventore dolorum dolorum hic.', 'Médio', 59, 3, 'tenetur_accusamus.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 34, 167),
(50, 1, 'Amet iusto', 'Nesciunt mollitia sit alias fugit. Rerum nostrum ipsam vel neque quasi cupiditate.', '1. Fugit aut saepe veritatis temporibus amet labore dicta.\r\n2. Ad pariatur maiores accusamus quidem voluptas dolores hic.', 'Médio', 36, 7, 'amet_iusto.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 15, 159),
(51, 1, 'Ratione inventore rerum', 'Voluptatum illum error.', '1. Corrupti autem quae aperiam.\r\n2. Distinctio ipsa quo ut hic.', 'Difícil', 52, 3, 'ratione_inventore_rerum.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 44, 179),
(52, 1, 'Consequuntur aliquam blanditiis', 'Facere necessitatibus totam corrupti ab ad.', '1. Nihil dolorum molestias blanditiis atque sapiente hic quae.\r\n2. Officia modi distinctio aut modi nostrum iste.', 'Difícil', 49, 8, 'consequuntur_aliquam_blanditiis.jpg', '0000-00-00 00:00:00', '2025-05-06 21:12:36', 3, 176),
(53, 1, 'Porro quas nobis', 'Ipsum voluptas illo iure assumenda quo. Aut nisi quia.', '1. Sapiente sed exercitationem iusto.\r\n2. Rem occaecati unde voluptate.', 'Médio', 42, 4, 'porro_quas_nobis.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 35, 171),
(54, 1, 'Unde recusandae', 'Excepturi tempora animi debitis. Officia incidunt et non.', '1. Praesentium odio reprehenderit officiis corporis minima.\r\n2. Doloremque quam nulla vel assumenda asperiores quia.', 'Médio', 60, 3, 'unde_recusandae.jpg', '0000-00-00 00:00:00', '2025-05-06 18:36:30', 17, 206),
(55, 1, 'Reiciendis ratione corporis', 'Error aut qui soluta.', '1. Minus nesciunt non quae.\r\n2. Impedit reprehenderit dolore assumenda quis.', 'Difícil', 76, 8, 'reiciendis_ratione_corporis.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 47, 101),
(56, 1, 'Quia delectus eos', 'Possimus rem officiis hic. Earum harum deleniti saepe repellat ullam.', '1. Eos tempore voluptas ea beatae quibusdam.\r\n2. Repellat ipsa consequuntur corrupti sequi cum corrupti quas.', 'Fácil', 33, 3, 'quia_delectus_eos.jpg', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 21, 138),
(57, 1, 'Inventore exercitationem', 'Nihil dicta quidem voluptate aspernatur magni veniam.', '1. Sapiente voluptatum aperiam expedita ex ratione suscipit adipisci.\r\n2. Quo excepturi possimus eveniet officiis facilis dolorem.', NULL, 50, 7, 'inventore_exercitationem.jpg', '0000-00-00 00:00:00', '2025-05-01 17:21:36', 0, 175),
(58, 1, 'Deserunt reiciendis', 'Quasi excepturi esse natus iusto. Repudiandae dicta minus porro.', '1. Doloribus odio repudiandae.\r\n2. Porro ducimus blanditiis dolorum eius neque.', NULL, 75, 2, 'deserunt_reiciendis.jpg', '0000-00-00 00:00:00', '2025-05-05 16:44:13', 31, 126),
(59, 1, 'Dolorem quo', 'Mollitia earum cum pariatur. Corrupti quod iste mollitia atque.', '1. Ab architecto enim molestiae quae eius.\r\n2. Fuga suscipit explicabo magnam.', 'Médio', 62, 6, 'dolorem_quo.jpg', '0000-00-00 00:00:00', '2025-04-26 11:56:02', 3, 78),
(60, 1, 'Illo eos', 'Accusantium nostrum illo. Voluptatum est ratione facilis aliquam commodi.', '1. Amet quis velit aspernatur exercitationem reiciendis.\r\n2. Dolorum dolor ab laboriosam exercitationem rerum repudiandae.', 'Fácil', 21, 7, 'illo_eos.jpg', '0000-00-00 00:00:00', '2025-05-02 03:06:37', 23, 122);

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
(3, 23),
(3, 41),
(18, 22),
(18, 33),
(18, 42),
(19, 21),
(19, 37),
(19, 40),
(20, 21),
(20, 36),
(20, 52),
(21, 50);

-- --------------------------------------------------------

--
-- Table structure for table `recipe_ingredients`
--

CREATE TABLE `recipe_ingredients` (
  `recipe_ingredient_id` int(11) NOT NULL,
  `recipe_id` int(11) NOT NULL,
  `ingredient_id` int(11) DEFAULT NULL,
  `custom_name` varchar(100) DEFAULT NULL,
  `quantity` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipe_ingredients`
--

INSERT INTO `recipe_ingredients` (`recipe_ingredient_id`, `recipe_id`, `ingredient_id`, `custom_name`, `quantity`) VALUES
(1, 3, 213, NULL, '200g'),
(2, 3, 184, NULL, '1 kg'),
(3, 3, NULL, 'bala de amenduim', '3 balas');

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
(137, 13, 18, '2025-05-11 18:45:39'),
(138, 13, 18, '2025-05-11 18:45:56'),
(139, 13, 18, '2025-05-11 18:46:16'),
(140, 13, 3, '2025-05-11 18:57:29'),
(141, 13, 18, '2025-05-11 19:02:22'),
(142, 13, 3, '2025-05-11 19:02:25'),
(143, 13, 18, '2025-05-11 19:02:27'),
(144, 1, 18, '2025-05-11 19:14:43'),
(145, 1, 3, '2025-05-11 19:14:53'),
(146, 1, 3, '2025-05-11 19:14:55'),
(147, 1, 18, '2025-05-11 19:14:57'),
(148, 1, 3, '2025-05-11 19:14:59'),
(149, 1, 18, '2025-05-11 19:15:02'),
(150, 1, 18, '2025-05-11 19:29:17'),
(151, 1, 3, '2025-05-11 19:29:55'),
(152, 1, 3, '2025-05-11 19:30:53'),
(153, 1, 3, '2025-05-11 19:31:01'),
(154, 1, 18, '2025-05-11 19:31:12'),
(155, 1, 3, '2025-05-11 19:31:33'),
(156, 1, 3, '2025-05-11 19:31:37'),
(157, 1, 3, '2025-05-11 19:39:29'),
(158, 1, 3, '2025-05-11 19:41:06'),
(159, 1, 18, '2025-05-11 19:41:39'),
(160, 1, 3, '2025-05-11 19:49:03'),
(161, 1, 3, '2025-05-11 19:50:55'),
(162, 1, 3, '2025-05-11 19:59:58'),
(163, 1, 3, '2025-05-11 20:00:51'),
(164, 1, 18, '2025-05-17 13:46:42');

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
(35, 1, 'sobre', '2025-04-11 13:40:31', '#FFF0F5'),
(50, 1, 'heheufjhf', '2025-04-11 20:18:03', '#E6FFE9'),
(63, 13, 'ola', '2025-04-30 22:29:23', '#FFF0F5'),
(66, 13, 'hdh', '2025-05-09 15:20:50', '#FAD6A5');

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
(291, 35, 24, '2025-05-06 17:18:55'),
(298, 35, 41, '2025-05-06 17:35:35'),
(344, 66, 32, '2025-05-11 15:04:35'),
(354, 63, 22, '2025-05-11 16:02:07'),
(361, 63, 3, '2025-05-11 17:45:22'),
(362, 63, 23, '2025-05-11 17:45:24'),
(363, 63, 47, '2025-05-11 17:45:27'),
(364, 63, 43, '2025-05-11 17:45:29'),
(366, 63, 18, '2025-05-11 18:02:24'),
(372, 35, 22, '2025-05-11 19:00:47'),
(374, 35, 44, '2025-05-17 12:43:46');

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
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `firebase_uid`, `username`, `email`, `profile_picture`, `created_at`, `updated_at`) VALUES
(1, 'ZA3PDVCrOrPzQKA2lST3lQVU1c23', 'admin', 'admin@gmail.com', '', '2025-04-05 12:56:27', '2025-04-05 12:56:37'),
(13, 'ngl7Ux7HDlRsW3Xe0MpVzl37Pwf2', 'sayo santos', 'sayo.santos1306@gmail.com', 'https://lh3.googleusercontent.com/a/ACg8ocL9ANhIPt5cXYQkxgBQrH4v-61VDx_IbjNWgZfDaJxuiHZLu2M=s96-c', '2025-03-31 14:33:20', '2025-05-08 12:32:39');

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
(1, 22, 7, 2, 1),
(1, 23, 14, 4, 4),
(1, 33, 7, 2, 1),
(1, 41, 14, 4, 4),
(1, 42, 7, 2, 1),
(13, 22, 5, 2, 1),
(13, 23, 2, 1, 0),
(13, 33, 5, 2, 1),
(13, 41, 2, 1, 0),
(13, 42, 5, 2, 1);

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
(10, 13, 18, '2025-05-11 18:45:58'),
(11, 1, 3, '2025-05-11 19:30:45'),
(12, 1, 3, '2025-05-11 19:40:37'),
(13, 1, 18, '2025-05-11 19:41:48'),
(14, 1, 3, '2025-05-11 19:49:55'),
(15, 1, 3, '2025-05-11 20:00:30');

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
  MODIFY `comment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;

--
-- AUTO_INCREMENT for table `ingredients`
--
ALTER TABLE `ingredients`
  MODIFY `ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=225;

--
-- AUTO_INCREMENT for table `ingredient_categories`
--
ALTER TABLE `ingredient_categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `rating_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT for table `recipes`
--
ALTER TABLE `recipes`
  MODIFY `recipe_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT for table `recipe_ingredients`
--
ALTER TABLE `recipe_ingredients`
  MODIFY `recipe_ingredient_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `recipe_views`
--
ALTER TABLE `recipe_views`
  MODIFY `view_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=165;

--
-- AUTO_INCREMENT for table `saved_lists`
--
ALTER TABLE `saved_lists`
  MODIFY `list_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;

--
-- AUTO_INCREMENT for table `saved_recipes`
--
ALTER TABLE `saved_recipes`
  MODIFY `saved_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=375;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=113;

--
-- AUTO_INCREMENT for table `user_recipe_finished`
--
ALTER TABLE `user_recipe_finished`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

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
