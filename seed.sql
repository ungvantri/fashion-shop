-- =========================================================================
-- SCRIPT TẠO DỮ LIỆU MẪU (SEED DATA) CHO HỆ THỐNG "NẮNG FASHION"
-- Hệ quản trị CSDL: MySQL 8.0+
-- Bảng mã: utf8mb4 / utf8mb4_unicode_ci
-- =========================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -------------------------------------------------------------------------
-- 1. XÓA DỮ LIỆU CŨ TRƯỚC KHI NẠP MỚI (TRÁNH TRÙNG LẶP KHÓA CHÍNH)
-- -------------------------------------------------------------------------
TRUNCATE TABLE shipping_orders;
TRUNCATE TABLE return_items;
TRUNCATE TABLE returns;
TRUNCATE TABLE order_payments;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE stock_transfer_items;
TRUNCATE TABLE stock_transfers;
TRUNCATE TABLE store_inventories;
TRUNCATE TABLE product_variants;
TRUNCATE TABLE products;
TRUNCATE TABLE categories;
TRUNCATE TABLE brands;
TRUNCATE TABLE vouchers;
TRUNCATE TABLE work_shifts;
TRUNCATE TABLE customers;
TRUNCATE TABLE customer_tiers;
TRUNCATE TABLE users;
TRUNCATE TABLE stores;

-- -------------------------------------------------------------------------
-- 2. BẢNG `stores`: 3 CHI NHÁNH TẠI TP.HCM
-- -------------------------------------------------------------------------
INSERT INTO stores (id, code, name, phone, address, is_active) VALUES
(1, 'CN-Q1', 'Nắng Fashion - Quận 1', '02838110001', '120 Hai Bà Trưng, P. Đa Kao, Quận 1, TP.HCM', true),
(2, 'CN-Q3', 'Nắng Fashion - Quận 3', '02838110002', '45 Lê Văn Sỹ, Phường 13, Quận 3, TP.HCM', true),
(3, 'CN-TD', 'Nắng Fashion - Thủ Đức', '02838110003', '88 Võ Văn Ngân, TP. Thủ Đức, TP.HCM', true);

-- -------------------------------------------------------------------------
-- 3. BẢNG `users`: TÀI KHOẢN NHÂN SỰ & PHÂN QUYỀN
-- Mật khẩu mặc định: 123456 (hash BCrypt)
-- -------------------------------------------------------------------------
INSERT INTO users (id, store_id, role, username, password_hash, full_name, phone, commission_rate, is_active) VALUES
(1, NULL, 'ADMIN', 'lan.admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoO.8Dq.E7zN7lUvX0Eeq2K0K.kE1GZ6Y2', 'Chị Lan (Chủ cửa hàng)', '0903000111', 0.00, true),
(2, 1, 'STORE_MANAGER', 'an.tran', '$2a$10$7EqJtq98hPqEX7fNZaFWoO.8Dq.E7zN7lUvX0Eeq2K0K.kE1GZ6Y2', 'Trần Văn An (Quản lý Q1)', '0903000222', 0.50, true),
(3, 1, 'CASHIER', 'binh.le', '$2a$10$7EqJtq98hPqEX7fNZaFWoO.8Dq.E7zN7lUvX0Eeq2K0K.kE1GZ6Y2', 'Lê Thị Bình (Thu ngân Q1)', '0903000333', 1.00, true),
(4, 2, 'CASHIER', 'chi.mai', '$2a$10$7EqJtq98hPqEX7fNZaFWoO.8Dq.E7zN7lUvX0Eeq2K0K.kE1GZ6Y2', 'Mai Kim Chi (Thu ngân Q3)', '0903000444', 1.00, true),
(5, 3, 'STORE_MANAGER', 'dung.hoang', '$2a$10$7EqJtq98hPqEX7fNZaFWoO.8Dq.E7zN7lUvX0Eeq2K0K.kE1GZ6Y2', 'Hoàng Quốc Dũng (Quản lý Thủ Đức)', '0903000555', 0.50, true),
(6, NULL, 'ACCOUNTANT', 'huong.ketoan', '$2a$10$7EqJtq98hPqEX7fNZaFWoO.8Dq.E7zN7lUvX0Eeq2K0K.kE1GZ6Y2', 'Phạm Thu Hương (Kế toán)', '0903000666', 0.00, true);

-- -------------------------------------------------------------------------
-- 4. BẢNG `customer_tiers`: CHÍNH SÁCH HẠNG THÀNH VIÊN
-- -------------------------------------------------------------------------
INSERT INTO customer_tiers (id, tier_name, min_spent, discount_percent) VALUES
(1, 'Thường', 0.00, 0.00),
(2, 'Bạc', 3000000.00, 5.00),
(3, 'Vàng', 10000000.00, 10.00),
(4, 'VIP', 25000000.00, 15.00);

-- -------------------------------------------------------------------------
-- 5. BẢNG `customers`: KHÁCH HÀNG THÂN THIẾT (CRM)
-- -------------------------------------------------------------------------
INSERT INTO customers (id, tier_id, phone, full_name, birthday, total_spent, current_points) VALUES
(1001, 3, '0909123456', 'Nguyễn Thị Ngọc Anh', '1995-11-05', 12500000.00, 125),
(1002, 1, '0988777888', 'Trần Thu Hằng', '2001-04-12', 640000.00, 6),
(1003, 4, '0912345678', 'Vũ Thị Minh Thư', '1990-08-20', 28000000.00, 280),
(1004, 2, '0933111222', 'Lê Hoàng Yến', '1998-02-14', 4500000.00, 45);

-- -------------------------------------------------------------------------
-- 6. BẢNG `categories`: DANH MỤC THỜI TRANG ĐA CẤP (CHA - CON)
-- -------------------------------------------------------------------------
INSERT INTO categories (id, name, slug, parent_id) VALUES
(1, 'Thời trang nữ', 'thoi-trang-nu', NULL),
(2, 'Đầm & Váy', 'dam-vay', 1),
(3, 'Áo nữ', 'ao-nu', 1),
(4, 'Quần & Chân váy', 'quan-chan-vay', 1),
(5, 'Áo sơ mi', 'ao-so-mi', 3),
(6, 'Áo thun & Len', 'ao-thun-len', 3);

-- -------------------------------------------------------------------------
-- 7. BẢNG `brands`: THƯƠNG HIỆU
-- -------------------------------------------------------------------------
INSERT INTO brands (id, name) VALUES
(1, 'Nắng Studio'),
(2, 'Chic & Co'),
(3, 'Zara Basic');

-- -------------------------------------------------------------------------
-- 8. BẢNG `products`: SẢN PHẨM CHA (KIỂU DÁNG)
-- -------------------------------------------------------------------------
INSERT INTO products (id, category_id, brand_id, code, name, season, is_active, description) VALUES
(10, 2, 1, 'DAM-HOA-01', 'Đầm Hoa Nhí Vintage', 'Thu Đông 2026', true, 'Đầm xòe hoa nhí phong cách vintage nữ tính, chất vải voan lụa cao cấp.'),
(11, 5, 1, 'SM-LUA-02', 'Áo Sơ Mi Lụa Hàn', 'Thu Đông 2026', true, 'Áo sơ mi lụa mềm mịn, cổ đức thanh lịch phù hợp công sở.'),
(12, 3, 2, 'BLAZER-03', 'Áo Blazer Dáng Rộng', 'Thu Đông 2026', true, 'Blazer 2 lớp phong cách Hàn Quốc, form rộng trẻ trung.'),
(13, 4, 1, 'QUAN-ONG-RONG', 'Quần Tây Ống Rộng Cạp Cao', 'Mùa Hè 2026', true, 'Quần tây ống suông hack dáng, cạp cao tôn eo.');

-- -------------------------------------------------------------------------
-- 9. BẢNG `product_variants`: BIẾN THỂ (SIZE × MÀU × BARCODE QUÉT POS)
-- -------------------------------------------------------------------------
INSERT INTO product_variants (id, product_id, sku, barcode, size, color, cost_price, retail_price, image_url) VALUES
(201, 10, 'DAM01-VANG-S', '893850100101', 'S', 'Vàng', 160000.00, 380000.00, 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1'),
(202, 10, 'DAM01-VANG-M', '893850100102', 'M', 'Vàng', 160000.00, 380000.00, 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1'),
(203, 10, 'DAM01-VANG-L', '893850100103', 'L', 'Vàng', 160000.00, 380000.00, 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1'),
(204, 11, 'SM02-TRANG-M', '893850200201', 'M', 'Trắng', 110000.00, 260000.00, 'https://images.unsplash.com/photo-1598532163257-ae3c6b2524b6'),
(205, 11, 'SM02-TRANG-L', '893850200202', 'L', 'Trắng', 110000.00, 260000.00, 'https://images.unsplash.com/photo-1598532163257-ae3c6b2524b6'),
(206, 12, 'BLAZER03-DEN-F', '893850300301', 'Free', 'Đen', 240000.00, 550000.00, 'https://images.unsplash.com/photo-1584273143981-41c073dfe8f8'),
(207, 13, 'QOR-DEN-S', '893850400401', 'S', 'Đen', 130000.00, 320000.00, 'https://images.unsplash.com/photo-1509631179647-0177331693ae'),
(208, 13, 'QOR-DEN-M', '893850400402', 'M', 'Đen', 130000.00, 320000.00, 'https://images.unsplash.com/photo-1509631179647-0177331693ae');

-- -------------------------------------------------------------------------
-- 10. BẢNG `store_inventories`: TỒN KHO THỰC TẾ THEO CỬA HÀNG
-- -------------------------------------------------------------------------
INSERT INTO store_inventories (id, store_id, product_variant_id, quantity, min_threshold) VALUES
-- Chi nhánh Quận 1 (Store 1)
(1, 1, 201, 15, 5), -- Đầm Vàng S: Còn đủ
(2, 1, 202, 8, 5),  -- Đầm Vàng M: Còn đủ
(3, 1, 204, 2, 5),  -- Sơ mi Trắng M: CẢNH BÁO SẮP HẾT HÀNG (2 <= 5)
(4, 1, 206, 10, 3), -- Blazer Đen: Còn đủ
(5, 1, 207, 12, 5), -- Quần Đen S: Còn đủ

-- Chi nhánh Quận 3 (Store 2)
(6, 2, 201, 0, 5),  -- Đầm Vàng S: HẾT HÀNG (0)
(7, 2, 204, 18, 5), -- Sơ mi Trắng M: Còn nhiều (có thể điều chuyển qua Q1)
(8, 2, 206, 6, 3),  -- Blazer Đen: Còn đủ
(9, 2, 208, 14, 5), -- Quần Đen M: Còn đủ

-- Chi nhánh Thủ Đức (Store 3)
(10, 3, 201, 10, 5),
(11, 3, 204, 10, 5),
(12, 3, 206, 5, 3),
(13, 3, 207, 8, 5);

-- -------------------------------------------------------------------------
-- 11. BẢNG `work_shifts`: CA LÀM VIỆC & ĐỐI SOÁT TIỀN MẶT POS
-- -------------------------------------------------------------------------
INSERT INTO work_shifts (id, store_id, user_id, opened_at, closed_at, initial_cash, system_cash, actual_cash, difference, status) VALUES
(101, 1, 3, '2026-10-30 08:00:00', '2026-10-30 15:00:00', 1000000.00, 4250000.00, 5250000.00, 0.00, 'CLOSED'),
(102, 1, 3, '2026-10-30 15:00:00', NULL, 1000000.00, 1500000.00, NULL, NULL, 'OPEN');

-- -------------------------------------------------------------------------
-- 12. BẢNG `vouchers`: MÃ GIẢM GIÁ & KHUYẾN MÃI
-- -------------------------------------------------------------------------
INSERT INTO vouchers (id, code, discount_type, discount_value, min_order_amount, max_uses, used_count, valid_from, valid_to) VALUES
(1, 'CHAOMUNG2026', 'AMOUNT', 50000.00, 300000.00, 100, 42, '2026-10-01', '2026-12-31'),
(2, 'TRIANVIP', 'PERCENT', 10.00, 500000.00, 500, 89, '2026-10-15', '2026-11-30'),
(3, 'SALEHE50K', 'AMOUNT', 50000.00, 250000.00, 200, 15, '2026-10-01', '2026-11-15');

-- -------------------------------------------------------------------------
-- 13. BẢNG `orders`: HÓA ĐƠN BÁN HÀNG ĐA KÊNH
-- -------------------------------------------------------------------------
INSERT INTO orders (id, store_id, customer_id, shift_id, cashier_id, seller_id, channel, order_code, total_item_amount, discount_amount, final_amount, status, created_at) VALUES
(8001, 1, 1001, 101, 3, 3, 'POS', 'HD-Q1-261030-01', 640000.00, 114000.00, 526000.00, 'COMPLETED', '2026-10-30 09:30:00'),
(8002, 1, 1002, NULL, 2, NULL, 'SHOPEE', 'HD-ONL-261030-02', 260000.00, 0.00, 260000.00, 'COMPLETED', '2026-10-30 11:15:00'),
(8003, 2, 1003, NULL, 4, 4, 'POS', 'HD-Q3-261030-03', 550000.00, 82500.00, 467500.00, 'COMPLETED', '2026-10-30 14:00:00');

-- -------------------------------------------------------------------------
-- 14. BẢNG `order_items`: CHI TIẾT SẢN PHẨM TRONG ĐƠN HÀNG
-- Lưu cost_price tại thời điểm bán để tính lãi gộp chính xác
-- -------------------------------------------------------------------------
INSERT INTO order_items (id, order_id, product_variant_id, quantity, retail_price, cost_price, discount_amount, total_amount) VALUES
(1, 8001, 201, 1, 380000.00, 160000.00, 50000.00, 330000.00), -- Đầm hoa nhí Vàng S
(2, 8001, 204, 1, 260000.00, 110000.00, 0.00, 260000.00),       -- Sơ mi lụa Trắng M
(3, 8002, 204, 1, 260000.00, 110000.00, 0.00, 260000.00),       -- Sơ mi lụa Trắng M (Shopee)
(4, 8003, 206, 1, 550000.00, 240000.00, 0.00, 550000.00);       -- Áo Blazer Đen Free

-- -------------------------------------------------------------------------
-- 15. BẢNG `order_payments`: THANH TOÁN (HỖ TRỢ TÁCH NHIỀU HÌNH THỨC)
-- -------------------------------------------------------------------------
INSERT INTO order_payments (id, order_id, payment_method, amount, transaction_reference) VALUES
(1, 8001, 'CASH', 300000.00, NULL),                          -- Khách trả 300k tiền mặt
(2, 8001, 'CARD', 226000.00, 'POS-VCB-982341'),               -- Quẹt thẻ 226k (Tổng khớp 526k)
(3, 8002, 'BANK_TRANSFER', 260000.00, 'SHOPEEPAY-991244'),    -- Thanh toán ShopeePay
(4, 8003, 'CARD', 467500.00, 'POS-TECH-551299');              -- Quẹt thẻ toàn bộ

-- -------------------------------------------------------------------------
-- 16. BẢNG `returns` & `return_items`: ĐỔI TRẢ TRONG VÒNG 7 NGÀY
-- -------------------------------------------------------------------------
INSERT INTO returns (id, original_order_id, store_id, created_by_user_id, return_code, refund_amount, status, created_at) VALUES
(901, 8001, 1, 3, 'RET-261102-01', 0.00, 'COMPLETED', '2026-11-02 10:00:00');

INSERT INTO return_items (id, return_id, product_variant_id, quantity, return_reason) VALUES
(1, 901, 204, 1, 'Khách mặc chật ngực, muốn đổi từ size M sang size L');

-- -------------------------------------------------------------------------
-- 17. BẢNG `stock_transfers` & `stock_transfer_items`: CHUYỂN KHO LIÊN CỬA HÀNG
-- Điều chuyển 5 áo sơ mi từ Quận 3 sang Quận 1
-- -------------------------------------------------------------------------
INSERT INTO stock_transfers (id, code, from_store_id, to_store_id, status, sent_by_user_id, received_by_user_id, shipped_at, received_at, note) VALUES
(501, 'TRF-20261030-01', 2, 1, 'RECEIVED', 4, 2, '2026-10-30 10:00:00', '2026-10-30 14:30:00', 'Điều chuyển gấp 5 áo sơ mi lụa trắng cho chi nhánh Q1');

INSERT INTO stock_transfer_items (id, transfer_id, product_variant_id, quantity) VALUES
(1, 501, 204, 5);

-- -------------------------------------------------------------------------
-- 18. BẢNG `shipping_orders`: VẬN CHUYỂN ĐƠN HÀNG ONLINE (GHTK/GHN)
-- -------------------------------------------------------------------------
INSERT INTO shipping_orders (id, order_id, carrier, tracking_code, shipping_fee, cod_amount, status) VALUES
(701, 8002, 'GHTK', 'S2026.GHTK.99281', 32000.00, 0.00, 'DELIVERING');

SET FOREIGN_KEY_CHECKS = 1;

-- =========================================================================
-- HOÀN TẤT NẠP DỮ LIỆU SEED CHO NẮNG FASHION!
-- =========================================================================
