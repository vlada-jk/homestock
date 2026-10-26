-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 23. Okt 2026 um 08:43
-- Server-Version: 10.4.32-MariaDB
-- PHP-Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `mystock_db`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `inventory_item`
--

CREATE TABLE `inventory_item` (
  `id` int(11) NOT NULL,
  `barcode` varchar(50) DEFAULT NULL,
  `product_name` varchar(50) NOT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `storage_location` int(11) DEFAULT NULL,
  `content_amount` double DEFAULT NULL,
  `unit` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `expiration_date` date DEFAULT NULL,
  `notes` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `inventory_item`
--

INSERT INTO `inventory_item` (`id`, `barcode`, `product_name`, `brand`, `category`, `storage_location`, `content_amount`, `unit`, `quantity`, `expiration_date`, `notes`) VALUES
(1, '4 013300 031281', 'Chili Sauce', 'Knorr', 'Sauce', 2, 0.25, 4, 1, '2028-05-07', 'Sauce für \"Kebab\" oder einfach zum Fleisch.'),
(2, NULL, 'Heinz Beanz', 'Heinz', 'Konserven', 2, 415, 0, 1, '2026-05-01', NULL),
(3, '4 337256628396', 'Ananas in Dose', 'Rewe Beste Wahl', 'Konserven', 2, 432, 0, 1, '2026-07-01', NULL),
(4, NULL, 'Kartoffel', NULL, 'Gemüse', 3, 2, 1, 1, '2026-10-29', NULL),
(5, NULL, 'Jack Daniels Single Barrel', NULL, 'Sonstiges', 2, 0, 4, 1, '2029-10-06', 'Für psychische Gesundheit'),
(6, '4 009239 608694', 'Thunfisch', 'Deutschesee', 'Sonstiges', 2, 140, 0, 1, '2028-09-30', NULL),
(7, '4 009239 608694', 'Thunfisch', 'Deutschesee', 'Sonstiges', 2, 140, 0, 1, '2028-10-31', NULL),
(8, '4008 1236', 'Tomaten Stückig', 'Dro Di Parma', 'Sonstiges', 2, 1, 2, 2, '2028-12-01', NULL),
(9, '4 820062446808', 'Сардина в оливковій олії', 'Riga Gold', 'Sonstiges', 2, 240, 0, 1, '2027-11-20', NULL),
(10, '4 012839 597596', 'Dorschleber', 'RügenFisch', 'Sonstiges', 2, 120, 0, 1, '2029-01-23', NULL),
(11, '4 032549 002968', 'Riga Sprotten in Rapsöl', 'Dovgan', 'Sonstiges', 2, 160, 0, 1, '2026-11-14', NULL),
(12, NULL, 'Johnnie Walker Black Label', 'Johnnie Walker', 'Alkochol', 2, 0, 4, 1, '2029-08-01', NULL),
(13, NULL, 'Four Roses (Kentucky Straight Bourbon Whiskey)', 'Four Roses', 'Sonstiges', 2, 0, 4, 1, '2030-09-01', NULL),
(14, NULL, 'Birnen', NULL, 'Sonstiges', 0, 3, 3, 1, '2026-10-24', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `shopping_list_item`
--

CREATE TABLE `shopping_list_item` (
  `id` int(11) NOT NULL,
  `barcode` varchar(50) DEFAULT NULL,
  `product_name` varchar(50) NOT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `bought` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `shopping_list_item`
--

INSERT INTO `shopping_list_item` (`id`, `barcode`, `product_name`, `brand`, `category`, `quantity`, `bought`) VALUES
(1, NULL, 'Jogurt', 'Rewe Beste Wahl', 'Milchprodukte', 1, 0),
(2, NULL, 'Karotten', NULL, 'Gemüse', 3, 0),
(3, NULL, 'Bier', NULL, 'Sonstiges', 4, 0);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `inventory_item`
--
ALTER TABLE `inventory_item`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `shopping_list_item`
--
ALTER TABLE `shopping_list_item`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `inventory_item`
--
ALTER TABLE `inventory_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT für Tabelle `shopping_list_item`
--
ALTER TABLE `shopping_list_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
