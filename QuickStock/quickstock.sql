-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Waktu pembuatan: 26 Jul 2025 pada 00.19
-- Versi server: 8.0.30
-- Versi PHP: 8.3.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `quickstock`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `products`
--

CREATE TABLE `products` (
  `product_id` int NOT NULL,
  `supplier_id` int DEFAULT NULL,
  `product_name` varchar(100) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `buying_price` decimal(10,2) NOT NULL,
  `selling_price` decimal(10,2) NOT NULL,
  `stock_quantity` int NOT NULL DEFAULT '0',
  `min_stock_level` int DEFAULT '10',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data untuk tabel `products`
--

INSERT INTO `products` (`product_id`, `supplier_id`, `product_name`, `category`, `buying_price`, `selling_price`, `stock_quantity`, `min_stock_level`, `created_at`, `updated_at`) VALUES
(1, NULL, 'Classic White T-Shirt', 'Tops', 45000.00, 500000.00, 100, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(2, NULL, 'Slim Fit Polo Shirt', 'Tops', 60000.00, 125000.00, 85, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(3, NULL, 'Oversized Graphic Tee', 'Tops', 50000.00, 109000.00, 71, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(4, NULL, 'V-Neck Cotton Shirt', 'Tops', 55000.00, 119000.00, 40, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(5, NULL, 'Long Sleeve Henley', 'Tops', 65000.00, 135000.00, 30, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(6, NULL, 'Basic Tank Top', 'Tops', 30000.00, 75000.00, 110, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(7, NULL, 'Linen Short Sleeve Shirt', 'Tops', 85000.00, 179000.00, 24, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(8, NULL, 'Crew Neck Tee', 'Tops', 40000.00, 95000.00, 94, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(9, NULL, 'Retro Print Shirt', 'Tops', 72000.00, 155000.00, 54, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(10, NULL, 'Tie Dye T-Shirt', 'Tops', 57000.00, 129000.00, 60, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(11, NULL, 'Slim Fit Jeans', 'Bottoms', 120000.00, 249000.00, 50, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(12, NULL, 'High Waist Mom Jeans', 'Bottoms', 125000.00, 259000.00, 35, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(13, NULL, 'Cargo Jogger Pants', 'Bottoms', 95000.00, 199000.00, 44, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(14, NULL, 'Formal Trousers', 'Bottoms', 110000.00, 230000.00, 30, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(15, NULL, 'Chino Pants', 'Bottoms', 98000.00, 210000.00, 37, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(16, NULL, 'Cropped Denim Pants', 'Bottoms', 115000.00, 239000.00, 20, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(17, NULL, 'Flare Pants', 'Bottoms', 89000.00, 195000.00, 41, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(18, NULL, 'Jogger Sweatpants', 'Bottoms', 75000.00, 165000.00, 51, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(19, NULL, 'Short Denim Skirt', 'Bottoms', 80000.00, 169000.00, 27, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(20, NULL, 'Pleated Midi Skirt', 'Bottoms', 105000.00, 229000.00, 34, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(21, NULL, 'Denim Jacket', 'Outerwear', 150000.00, 299000.00, 20, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(22, NULL, 'Hooded Bomber Jacket', 'Outerwear', 170000.00, 339000.00, 15, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(23, NULL, 'Windbreaker Jacket', 'Outerwear', 135000.00, 285000.00, 14, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(24, NULL, 'Oversized Blazer', 'Outerwear', 160000.00, 320000.00, 19, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(25, NULL, 'Puffer Vest', 'Outerwear', 140000.00, 295000.00, 22, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(26, NULL, 'Wool Coat', 'Outerwear', 200000.00, 390000.00, 14, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(27, NULL, 'Corduroy Jacket', 'Outerwear', 155000.00, 310000.00, 18, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(28, NULL, 'Leather Biker Jacket', 'Outerwear', 180000.00, 360000.00, 11, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(29, NULL, 'Varsity Jacket', 'Outerwear', 165000.00, 325000.00, 16, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(30, NULL, 'Knitted Cardigan', 'Outerwear', 130000.00, 275000.00, 7, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(31, NULL, 'Floral Summer Dress', 'Dresses', 90000.00, 189000.00, 30, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(32, NULL, 'Bodycon Mini Dress', 'Dresses', 95000.00, 199000.00, 2, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(33, NULL, 'Maxi Dress', 'Dresses', 105000.00, 219000.00, 27, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(34, NULL, 'A-Line Midi Dress', 'Dresses', 99000.00, 209000.00, 19, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(35, NULL, 'Satin Slip Dress', 'Dresses', 110000.00, 230000.00, 21, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(36, NULL, 'Smocked Dress', 'Dresses', 95000.00, 200000.00, 26, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(37, NULL, 'Off-Shoulder Dress', 'Dresses', 87000.00, 185000.00, 21, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(38, NULL, 'Puff Sleeve Dress', 'Dresses', 92000.00, 190000.00, 28, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(39, NULL, 'Tiered Ruffle Dress', 'Dresses', 98000.00, 205000.00, 25, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(40, NULL, 'Wrap Midi Dress', 'Dresses', 102000.00, 215000.00, 15, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(41, NULL, 'Leather Belt', 'Accessories', 30000.00, 75000.00, 60, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(42, NULL, 'Canvas Cap', 'Accessories', 25000.00, 65000.00, 80, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(43, NULL, 'Knitted Beanie', 'Accessories', 22000.00, 59000.00, 77, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(44, NULL, 'Cotton Socks (3-pack)', 'Accessories', 28000.00, 70000.00, 120, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(45, NULL, 'Silk Scarf', 'Accessories', 35000.00, 89000.00, 40, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(46, NULL, 'Fashion Sunglasses', 'Accessories', 27000.00, 79000.00, 58, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(47, NULL, 'Crossbody Bag', 'Accessories', 60000.00, 135000.00, 3, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(48, NULL, 'Bucket Hat', 'Accessories', 29000.00, 69000.00, 62, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(49, NULL, 'Chunky Necklace', 'Accessories', 32000.00, 85000.00, 32, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57'),
(50, NULL, 'Canvas Tote Bag', 'Accessories', 45000.00, 50000.00, 20, 10, '2025-07-17 07:39:34', '2025-07-26 00:12:57');

-- --------------------------------------------------------

--
-- Struktur dari tabel `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int NOT NULL,
  `supplier_name` varchar(100) NOT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data untuk tabel `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `supplier_name`, `contact_person`, `phone`, `address`, `created_at`) VALUES
(1, 'Urban Style Ltd.', 'Jason Blake', '081234567890', 'Jl. Sudirman No. 10, Jakarta', '2025-07-17 07:46:16'),
(2, 'Trendy Wearhouse', 'Maya Sari', '081298765432', 'Jl. Gatot Subroto No. 25, Bandung', '2025-07-17 07:46:16'),
(3, 'Metro Apparel Co.', 'Dedi Santoso', '082112345678', 'Jl. Diponegoro No. 33, Surabaya', '2025-07-17 07:46:16'),
(4, 'Luxe Fashion Hub', 'Rani Kumala', '081377712345', 'Jl. Malioboro No. 5, Yogyakarta', '2025-07-17 07:46:16'),
(5, 'Cotton & Co.', 'Albert Wijaya', '085612345678', 'Jl. Merdeka No. 9, Medan', '2025-07-17 07:46:16'),
(6, 'Denim Brothers', 'Niko Fernando', '087812341234', 'Jl. Gajah Mada No. 18, Semarang', '2025-07-17 07:46:16'),
(7, 'Style Nation', 'Lia Putri', '089912345678', 'Jl. Ahmad Yani No. 11, Palembang', '2025-07-17 07:46:16'),
(8, 'Outfit Supplier Indo', 'Dian Cahya', '081234567821', 'Jl. Kartini No. 20, Makassar', '2025-07-17 07:46:16'),
(9, 'Global Trendwear', 'Kevin Tan', '082198765432', 'Jl. Cokroaminoto No. 14, Malang', '2025-07-17 07:46:16'),
(10, 'Elite Supplier Group', 'Ratna Amelia', '087755512345', 'Jl. Imam Bonjol No. 6, Bogor', '2025-07-17 07:46:16'),
(11, 'Modern Stitch Co.', 'Yusuf Rahman', '082100112233', 'Jl. Teuku Umar No. 7, Denpasar', '2025-07-17 07:46:16'),
(12, 'Casual Supply', 'Gina Sari', '083812345622', 'Jl. Hasanudin No. 19, Banjarmasin', '2025-07-17 07:46:16'),
(13, 'Signature Style Ltd.', 'Bayu Mahendra', '085312345678', 'Jl. Sisingamangaraja No. 21, Pekanbaru', '2025-07-17 07:46:16'),
(14, 'UrbanLine Apparel', 'Winda Lestari', '088812345678', 'Jl. RE Martadinata No. 9, Padang', '2025-07-17 07:46:16'),
(15, 'Basics Clothing Co.', 'Fikri Alamsyah', '089998877665', 'Jl. Asia Afrika No. 12, Bandung', '2025-07-17 07:46:16'),
(16, 'Eclipse Fashion', 'Tasya Adinda', '081123456789', 'Jl. Panglima Sudirman No. 4, Surabaya', '2025-07-17 07:46:16'),
(17, 'Aura Wearhouse', 'Rizky Pratama', '087712345000', 'Jl. Pemuda No. 7, Yogyakarta', '2025-07-17 07:46:16'),
(18, 'IndoTex Supplier', 'Linda Kusuma', '085700011122', 'Jl. Cendrawasih No. 17, Medan', '2025-07-17 07:46:16'),
(19, 'Harmony Supplier', 'Tommy Nugroho', '081998877665', 'Jl. Daan Mogot No. 10, Jakarta', '2025-07-17 07:46:16'),
(20, 'BestWear Trading', 'Selvi Hartono', '082211100099', 'Jl. Kamboja No. 2, Bekasi', '2025-07-17 07:46:16'),
(21, 'Knit & Stitch Ltd.', 'Anton Marbun', '087711122233', 'Jl. Cik Ditiro No. 15, Banda Aceh', '2025-07-17 07:46:16'),
(22, 'Classic Outfitters', 'Monica Angel', '088899977766', 'Jl. Jendral Sudirman No. 22, Pontianak', '2025-07-17 07:46:16'),
(23, 'Smart Apparel', 'Rafi Ahmad', '081234556789', 'Jl. HOS Cokroaminoto No. 19, Mataram', '2025-07-17 07:46:16'),
(24, 'Young Trendy Co.', 'Desi Kartika', '082255566677', 'Jl. Pemuda No. 13, Jambi', '2025-07-17 07:46:16'),
(25, 'Blue Ocean Textiles', 'Rio Budi', '083366677788', 'Jl. Yos Sudarso No. 31, Samarinda', '2025-07-17 07:46:16'),
(26, 'Prime Clothing Supply', 'Veronica T.', '084477788899', 'Jl. Sultan Agung No. 18, Tangerang', '2025-07-17 07:46:16'),
(27, 'Asia Fashion Link', 'Adrian H.', '085588899900', 'Jl. Letjen Suprapto No. 5, Cirebon', '2025-07-17 07:46:16'),
(28, 'Quality Garment', 'Amira Dewi', '086699900011', 'Jl. Pattimura No. 8, Kupang', '2025-07-17 07:46:16'),
(29, 'SimpleWear Supplier', 'Hendra Gunawan', '087700011122', 'Jl. Haryono MT No. 9, Palu', '2025-07-17 07:46:16'),
(30, 'Textile Harmony', 'Sinta Maharani', '088811122233', 'Jl. A. Yani No. 12, Batam', '2025-07-17 07:46:16'),
(31, 'Outfit Central', 'Dicky Alfan', '081222334455', 'Jl. Sudirman No. 77, Serang', '2025-07-17 07:46:16'),
(32, 'Millennial Clothes Inc.', 'Ayu Kartini', '082233445566', 'Jl. Pemuda No. 19, Manado', '2025-07-17 07:46:16'),
(33, 'Zeta Clothing House', 'Fadli M.', '083344556677', 'Jl. Soekarno-Hatta No. 14, Pekalongan', '2025-07-17 07:46:16'),
(34, 'TrendX Indonesia', 'Dewi Asmarani', '084455667788', 'Jl. Jend. A. Yani No. 30, Tegal', '2025-07-17 07:46:16'),
(35, 'Comfort Wear Ltd.', 'Reza Ardiansyah', '085566778899', 'Jl. WR Supratman No. 27, Solo', '2025-07-17 07:46:16'),
(36, 'Fresh Fit Apparel', 'Salma Nur', '086677889900', 'Jl. Cipto Mangunkusumo No. 5, Cianjur', '2025-07-17 07:46:16'),
(37, 'Tropic Outfit Supply', 'Andi Ramli', '087788990011', 'Jl. S Parman No. 21, Balikpapan', '2025-07-17 07:46:16'),
(38, 'Bright Wear', 'Melani Putra', '088899001122', 'Jl. Juanda No. 16, Cilegon', '2025-07-17 07:46:16'),
(39, 'Silk & Thread', 'Ivan Saputra', '089900112233', 'Jl. Hasan Basri No. 3, Purwokerto', '2025-07-17 07:46:16'),
(40, 'Gaya Supplier Indo', 'Clara Mutiara', '081101122334', 'Jl. Letda Sujono No. 23, Binjai', '2025-07-17 07:46:16'),
(41, 'Neo Apparel Network', 'Billy T.', '082211223344', 'Jl. Adi Sucipto No. 20, Salatiga', '2025-07-17 07:46:16'),
(42, 'Stylish Clothing Partner', 'Putri R.', '083322334455', 'Jl. Gagak Rimang No. 12, Rembang', '2025-07-17 07:46:16'),
(43, 'Unity Wear', 'Dimas Akbar', '084433445566', 'Jl. Merapi No. 3, Kudus', '2025-07-17 07:46:16'),
(44, 'Hexa Outfit Supply', 'Lina Q.', '085544556677', 'Jl. Jendral Sudirman No. 19, Magelang', '2025-07-17 07:46:16'),
(45, 'Oasis Clothing Co.', 'Yani S.', '086655667788', 'Jl. Dr. Wahidin No. 27, Klaten', '2025-07-17 07:46:16'),
(46, 'Nova Wearhouse', 'Galih Perdana', '087766778899', 'Jl. Cendana No. 2, Temanggung', '2025-07-17 07:46:16'),
(47, 'Indonesia Texlink', 'Irma Budi', '088877889900', 'Jl. Kalimantan No. 10, Singkawang', '2025-07-17 07:46:16'),
(48, 'MixApparel Supply', 'Riko Hartono', '089988990011', 'Jl. Jawa No. 4, Bangka', '2025-07-17 07:46:16'),
(49, 'Fine Fashion Partner', 'Tania Putra', '081198877665', 'Jl. Mangga Besar No. 8, Jakarta', '2025-07-17 07:46:16'),
(50, 'Supplier Nusantara', 'Halim Syahputra', '082198765432', 'Jl. Pisang No. 1, Depok', '2025-07-17 07:46:16');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` int NOT NULL,
  `user_id` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `payment_method` enum('Cash','QRIS') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `transaction_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data untuk tabel `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `user_id`, `total_amount`, `payment_method`, `transaction_date`) VALUES
(1, 3, 504000.00, 'QRIS', '2025-07-17 11:49:34'),
(2, 3, 500000.00, 'Cash', '2025-07-17 11:51:32'),
(3, 3, 360000.00, 'QRIS', '2025-07-17 11:51:56'),
(4, 3, 504000.00, 'Cash', '2025-07-17 11:52:58'),
(5, 3, 185000.00, 'QRIS', '2025-07-17 11:53:21'),
(6, 3, 549000.00, 'QRIS', '2025-07-17 13:40:50'),
(7, 3, 334000.00, 'Cash', '2025-07-18 06:08:34'),
(8, 3, 454000.00, 'Cash', '2025-07-23 15:57:20'),
(9, 3, 204000.00, 'QRIS', '2025-07-25 03:00:00'),
(10, 3, 199000.00, 'Cash', '2025-07-25 16:50:37');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaction_details`
--

CREATE TABLE `transaction_details` (
  `detail_id` int NOT NULL,
  `transaction_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data untuk tabel `transaction_details`
--

INSERT INTO `transaction_details` (`detail_id`, `transaction_id`, `product_id`, `quantity`, `price`, `subtotal`) VALUES
(1, 1, 3, 1, 109000.00, 109000.00),
(2, 1, 18, 1, 165000.00, 165000.00),
(3, 1, 14, 1, 230000.00, 230000.00),
(4, 2, 49, 1, 85000.00, 85000.00),
(5, 2, 37, 1, 185000.00, 185000.00),
(6, 2, 14, 1, 230000.00, 230000.00),
(7, 3, 28, 1, 360000.00, 360000.00),
(8, 4, 18, 1, 165000.00, 165000.00),
(9, 4, 22, 1, 339000.00, 339000.00),
(10, 5, 37, 1, 185000.00, 185000.00),
(11, 6, 15, 1, 210000.00, 210000.00),
(12, 6, 22, 1, 339000.00, 339000.00),
(13, 7, 7, 1, 179000.00, 179000.00),
(14, 7, 9, 1, 155000.00, 155000.00),
(15, 8, 23, 1, 285000.00, 285000.00),
(16, 8, 19, 1, 169000.00, 169000.00),
(17, 9, 3, 1, 109000.00, 109000.00),
(18, 9, 8, 1, 95000.00, 95000.00),
(19, 10, 13, 1, 199000.00, 199000.00);

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role` enum('owner','admin','cashier','warehouse') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `full_name`, `role`, `created_at`, `is_active`) VALUES
(1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Admin Demo', 'admin', '2025-07-06 15:00:18', 1),
(2, 'owner', '43a0d17178a9d26c9e0fe9a74b0b45e38d32f27aed887a008a54bf6e033bf7b9', 'Owner Demo', 'owner', '2025-07-06 15:43:26', 1),
(3, 'cashier', 'b4c94003c562bb0d89535eca77f07284fe560fd48a7cc1ed99f0a56263d616ba', 'Cashier Demo', 'cashier', '2025-07-06 15:43:26', 1),
(4, 'warehouse', '0e842cbe0341154ee33e0ed3bc18282cd69e016a8d56fda05ec92e7ff20a0f31', 'Warehouse Demo', 'warehouse', '2025-07-06 15:43:26', 1),
(5, 'abid', 'f417efb9c4168d0f62e9c4adcec316546f3684c66d3634e75ace34280e0a9fb3', 'Abid Hanan', 'owner', '2025-07-15 05:21:44', 1),
(6, 'priska', '88398c3fc8e8bb774ac5dbd45a774cb4a14ab94d435274b72e7960b561037e3e', 'Priska Intan', 'admin', '2025-07-16 04:08:29', 1),
(7, 'zaky', 'bf0b52439529700a4a04cea0ab5a0302283f86be667d49d8edc5209a22cab01d', 'Zaky Purnama', 'warehouse', '2025-07-17 06:07:19', 1),
(8, 'daveka', '45c1cf18c705531dbffe44f7e4ff81509c522384df61883a066bc784582f96a0', 'Moch Daveka', 'warehouse', '2025-07-17 06:08:24', 1),
(9, 'caroline', '57acef3c5f32757d9ea2d29c4cb0ab49b7d362986b1d26d46ee16303bce5b378', 'Caroline Chatarina', 'cashier', '2025-07-17 06:09:15', 1);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `supplier_id` (`supplier_id`),
  ADD KEY `supplier_id_2` (`supplier_id`);

--
-- Indeks untuk tabel `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`);

--
-- Indeks untuk tabel `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `transaction_details`
--
ALTER TABLE `transaction_details`
  ADD PRIMARY KEY (`detail_id`),
  ADD KEY `transaction_id` (`transaction_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=119;

--
-- AUTO_INCREMENT untuk tabel `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT untuk tabel `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `transaction_details`
--
ALTER TABLE `transaction_details`
  MODIFY `detail_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT untuk tabel `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`);

--
-- Ketidakleluasaan untuk tabel `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Ketidakleluasaan untuk tabel `transaction_details`
--
ALTER TABLE `transaction_details`
  ADD CONSTRAINT `transaction_details_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `transactions` (`transaction_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `transaction_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
