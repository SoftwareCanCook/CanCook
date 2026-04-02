-- CanCook Database Schema
-- Drop tables if they exist
DROP TABLE IF EXISTS Ratings;
DROP TABLE IF EXISTS Comments;
DROP TABLE IF EXISTS Recipe_Ingredients;
DROP TABLE IF EXISTS Recipes;
DROP TABLE IF EXISTS Pantry;
DROP TABLE IF EXISTS GroceryItems;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Stores;

-- Create Stores table
CREATE TABLE Stores (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(500) NULL,
  city VARCHAR(100) NULL,
  state VARCHAR(50) NULL,
  zip_code VARCHAR(20) NULL,
  phone VARCHAR(20) NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create User table
CREATE TABLE User (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  login_attempts SMALLINT NOT NULL DEFAULT 0,
  status SMALLINT NOT NULL DEFAULT 1,
  role VARCHAR(12) NOT NULL DEFAULT 'user',
  store_id INT NULL,
  FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE SET NULL
);

-- Create GroceryItems table
CREATE TABLE GroceryItems (
  id INT AUTO_INCREMENT PRIMARY KEY,
  store_id INT NOT NULL,
  name VARCHAR(355) NOT NULL,
  category VARCHAR(355) NOT NULL,
  unit VARCHAR(50) NULL,
  image VARCHAR(1000) NULL,
  stock INT NOT NULL DEFAULT 0,
  FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE CASCADE
);

-- Create Pantry table
CREATE TABLE Pantry (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  item_id INT NOT NULL,
  quantity INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
  FOREIGN KEY (item_id) REFERENCES GroceryItems(id) ON DELETE CASCADE
);

-- Create Recipes table
CREATE TABLE Recipes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  name VARCHAR(255) NOT NULL,
  image VARCHAR(1000) NULL,
  is_public BOOLEAN NOT NULL DEFAULT TRUE,
  instructions TEXT NOT NULL,
  timers TEXT NULL,
  rating FLOAT NULL,
  FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- Create Recipe_Ingredients table
CREATE TABLE Recipe_Ingredients (
  id INT AUTO_INCREMENT PRIMARY KEY,
  recipe_id INT NOT NULL,
  item_id INT NOT NULL,
  quantity_needed INT NOT NULL,
  measurement_unit VARCHAR(50) NOT NULL,
  FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE,
  FOREIGN KEY (item_id) REFERENCES GroceryItems(id) ON DELETE CASCADE
);

-- Create Comments table
CREATE TABLE Comments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  recipe_id INT NOT NULL,
  user_id INT NOT NULL,
  comment_text TEXT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

-- Create Ratings table
CREATE TABLE Ratings (
  id INT AUTO_INCREMENT PRIMARY KEY,
  recipe_id INT NOT NULL,
  user_id INT NOT NULL,
  rating FLOAT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (recipe_id) REFERENCES Recipes(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
  UNIQUE KEY unique_user_recipe_rating (user_id, recipe_id)
);

-- Insert stores
INSERT INTO Stores (name, address, city, state, zip_code, phone) VALUES
('Fresh Market Downtown', '123 Main Street', 'Springfield', 'IL', '62701', '(217) 555-0100'),
('Green Grocers Westside', '456 Oak Avenue', 'Springfield', 'IL', '62702', '(217) 555-0200'),
('Organic Plus', '789 Elm Street', 'Springfield', 'IL', '62703', '(217) 555-0300');

-- Insert default admin accounts and test users
-- Password for admin accounts is "admin123", regular users is "pass123"
INSERT INTO User (username, email, password, login_attempts, status, role, store_id) VALUES
('admin', 'admin@cancook.local', 'admin123', 0, 1, 'admin', NULL),
('groceryadmin', 'groceryadmin@cancook.local', 'admin123', 0, 1, 'groceryadmin', 1),
('groceryadmin2', 'groceryadmin2@cancook.local', 'admin123', 0, 1, 'groceryadmin', 2),
('johndoe', 'john.doe@email.com', 'pass123', 0, 1, 'user', NULL),
('janedoe', 'jane.doe@email.com', 'pass123', 0, 1, 'user', NULL),
('chefmike', 'chef.mike@email.com', 'pass123', 0, 1, 'user', NULL),
('bakerlisa', 'baker.lisa@email.com', 'pass123', 0, 1, 'user', NULL);

-- Insert grocery items
INSERT INTO GroceryItems (store_id, name, category, unit, image, stock) VALUES
-- Produce
(1, 'Tomatoes', 'Produce', 'lb', 'https://i5.walmartimages.com/seo/Fresh-Slicing-Tomato-Each_87d9362e-14de-4d0d-ac8a-76e0f45036ee.cc24439f3db08493212df9f0d12f48dd.jpeg?odnHeight=768&odnWidth=768&odnBg=FFFFFF', 100),
(1, 'Onions', 'Produce', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQE_TaCRUyCB-UmltWmo1YV8uGcgWiCV6QqCQ&s', 150),
(1, 'Garlic', 'Produce', 'cloves', 'https://images.heb.com/is/image/HEBGrocery/000325168-1', 200),
(1, 'Bell Peppers', 'Produce', 'each', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXhbEB6E8PHp7ErZ20JWF22iax136unK3iCw&s', 80),
(1, 'Carrots', 'Produce', 'lb', 'https://www.shutterstock.com/image-photo/carrot-isolated-on-white-background-600nw-795704785.jpg', 120),
(1, 'Celery', 'Produce', 'bunch', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThaIzCjSupd2CZqi13urRfIFKCgAZH0Gs2xg&s', 90),
(1, 'Potatoes', 'Produce', 'lb', 'https://i5.walmartimages.com/seo/Russet-Baking-Potatoes-Whole-Fresh-Each_c638c006-a982-48f7-aa33-6d3a8dc2983c.8fd015937ebfdd46c8fcb6177d0d1b1d.jpeg?odnHeight=768&odnWidth=768&odnBg=FFFFFF', 200),
(1, 'Lettuce', 'Produce', 'cup', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQkym2BFCtpK1etDMciFrYw8gwx9W-bI_aBA&s', 60),
(1, 'Spinach', 'Produce', 'bunch', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDr6TxSU65t995cB7IJqr5f-OW2rD9XBdLyA&s', 75),
(1, 'Mushrooms', 'Produce', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwB2476acvFPOH0uDKPUJGuejVC9XN8uhkoQ&s', 55),
-- Meats & Seafood
(1, 'Chicken Breast', 'Meats & Seafood', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3c8TPpfOuf2MJlFaruD8RoPrR0YDA6r2yfA&s', 50),
(1, 'Ground Beef', 'Meats & Seafood', 'lb', 'https://cdn11.bigcommerce.com/s-qieb3njg5z/images/stencil/1280x1280/products/626/2806/IMG_3660__58558.1741627909.jpg?c=1', 60),
(1, 'Salmon Fillet', 'Meats & Seafood', 'lb', 'https://static.vecteezy.com/system/resources/previews/002/009/102/large_2x/fillet-of-salmon-on-white-background-photo.jpg', 30),
(1, 'Shrimp', 'Meats & Seafood', 'lb', 'https://t4.ftcdn.net/jpg/07/53/24/43/360_F_753244363_DIFIDvCAYuldRkICfNQnG4n362mFrw5H.jpg', 40),
(1, 'Eggs', 'Dairy', 'dozen', 'https://media.istockphoto.com/id/1447419799/photo/one-chicken-egg-isolated-on-white-background.jpg?s=612x612&w=0&k=20&c=l9Muo1lMAnmq7ngrICvn0FSSlUgpTGuoLd6MW7WSdw8=', 200),
(1, 'Bacon', 'Meats & Seafood', 'lb', 'https://img.freepik.com/premium-photo/raw-bacon-slices-isolated-white-background_188078-22815.jpg', 70),
-- Dairy
(1, 'Milk', 'Dairy', 'tablespoon', 'https://media.istockphoto.com/id/484022736/photo/gallon-milk-bottle-with-red-cap-isolated-on-white.jpg?s=612x612&w=0&k=20&c=OKbtAyrLquL5tfHafJiDxuw_Cae6tLp8DKy4scCwUuM=', 100),
(1, 'Butter', 'Dairy', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPC8d5i14AozhRZyisQopv5qTyIGUD9cGvTA&s', 80),
(1, 'Cheddar Cheese', 'Dairy', 'cup', 'https://t4.ftcdn.net/jpg/02/48/16/33/360_F_248163313_zlTapa5Hqt5Tnchqk2vesGyFt7wNE3j4.jpg', 65),
(1, 'Parmesan Cheese', 'Dairy', 'oz', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRosplBxgfucZMeZHzt0tP4JIAkgSKUxYxL7w&s', 50),
(1, 'Mozzarella Cheese', 'Dairy', 'lb', 'https://t4.ftcdn.net/jpg/02/37/01/79/360_F_237017942_4IjGEOc1sZ6fetDIL4pB8PCOaImFQQnL.jpg', 55),
(1, 'Heavy Cream', 'Dairy', 'pint', 'https://t4.ftcdn.net/jpg/06/67/35/73/360_F_667357328_PyRyFNhdtZIw7M5HV9RT7tE46hh6n2Nu.jpg', 45),
-- Bakery & Pantry Staples
(1, 'All-Purpose Flour', 'Bakery', 'lb', 'https://www.sunorganicfarm.com/cdn/shop/files/organic-whole-wheat-flour-8.jpg?v=1764983675', 100),
(1, 'Sugar', 'Bakery', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCQ38eQDzyRQlv9tN6YrgF3w_T1GZS2busXw&s', 120),
(1, 'Salt', 'Pantry Staples', 'oz', 'https://t3.ftcdn.net/jpg/02/14/08/44/360_F_214084425_SjvXMLNevveNuiVX66BXhlj1cEa8vmqD.jpg', 200),
(1, 'Black Pepper', 'Pantry Staples', 'oz', 'https://static.vecteezy.com/system/resources/previews/002/980/504/large_2x/black-peppers-on-the-white-background-free-photo.jpg', 150),
(1, 'Olive Oil', 'Pantry Staples', 'tablespoon', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRR5vp9xUhmjTrEBR4sHHCjcF5_XPRByTAoiA&s', 90),
-- Store 2 (Green Grocers Westside) - Items
-- Produce
(2, 'Tomatoes', 'Produce', 'lb', 'https://i5.walmartimages.com/seo/Fresh-Slicing-Tomato-Each_87d9362e-14de-4d0d-ac8a-76e0f45036ee.cc24439f3db08493212df9f0d12f48dd.jpeg?odnHeight=768&odnWidth=768&odnBg=FFFFFF', 85),
(2, 'Onions', 'Produce', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQE_TaCRUyCB-UmltWmo1YV8uGcgWiCV6QqCQ&s', 130),
(2, 'Garlic', 'Produce', 'cloves', 'https://images.heb.com/is/image/HEBGrocery/000325168-1', 180),
(2, 'Bell Peppers', 'Produce', 'each', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXhbEB6E8PHp7ErZ20JWF22iax136unK3iCw&s', 70),
(2, 'Broccoli', 'Produce', 'bunch', 'https://thumbs.dreamstime.com/b/broccoli-isolated-white-background-close-up-view-green-raw-vegetable-131618524.jpg', 60),
(2, 'Cucumbers', 'Produce', 'each', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTH5y6_IM0itC8vYoDZTkUS4L2EHnhHuz68bw&s', 90),
(2, 'Avocados', 'Produce', 'each', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkh9i2DeFqOH_6ejrIzEGUbfnMh1li-ejPTw&s', 50),
-- Meats & Seafood
(2, 'Chicken Breast', 'Meats & Seafood', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3c8TPpfOuf2MJlFaruD8RoPrR0YDA6r2yfA&s', 45),
(2, 'Pork Chops', 'Meats & Seafood', 'lb', 'https://media.istockphoto.com/id/1464224200/photo/raw-fresh-pork-chop-isolated-on-white-background.jpg?s=612x612&w=0&k=20&c=_m5gZSMg__R5z5NwcSERS_daicaI8JnxG37C2xqrc5Y=', 35),
(2, 'Eggs', 'Dairy', 'dozen', 'https://media.istockphoto.com/id/1447419799/photo/one-chicken-egg-isolated-on-white-background.jpg?s=612x612&w=0&k=20&c=l9Muo1lMAnmq7ngrICvn0FSSlUgpTGuoLd6MW7WSdw8=', 180),
-- Dairy
(2, 'Milk', 'Dairy', 'tablespoon', 'https://media.istockphoto.com/id/484022736/photo/gallon-milk-bottle-with-red-cap-isolated-on-white.jpg?s=612x612&w=0&k=20&c=OKbtAyrLquL5tfHafJiDxuw_Cae6tLp8DKy4scCwUuM=', 95),
(2, 'Butter', 'Dairy', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPC8d5i14AozhRZyisQopv5qTyIGUD9cGvTA&s', 70),
(2, 'Greek Yogurt', 'Dairy', 'oz', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgEADVM68xiwVGHGx_Nnqu31KR28JYYTMnjQ&s', 60),
(2, 'Cream Cheese', 'Dairy', 'oz', 'https://media.istockphoto.com/id/1400138219/photo/fresh-cream-cheese.jpg?s=612x612&w=0&k=20&c=hXS6xAYSmkz5Frae-AXej739arGh9UraeOl75GutouQ=', 55),
-- Bakery & Pantry Staples
(2, 'All-Purpose Flour', 'Bakery', 'lb', 'https://www.sunorganicfarm.com/cdn/shop/files/organic-whole-wheat-flour-8.jpg?v=1764983675', 90),
(2, 'Brown Sugar', 'Bakery', 'lb', 'https://t3.ftcdn.net/jpg/01/15/79/78/360_F_115797810_LCbJCUngEJiJvMv80HAABAYqWL6hu8I6.jpg', 85),
(2, 'Salt', 'Pantry Staples', 'oz', 'https://t3.ftcdn.net/jpg/02/14/08/44/360_F_214084425_SjvXMLNevveNuiVX66BXhlj1cEa8vmqD.jpg', 180),
(2, 'Olive Oil', 'Pantry Staples', 'tablespoon', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRR5vp9xUhmjTrEBR4sHHCjcF5_XPRByTAoiA&s', 80),
(2, 'Honey', 'Pantry Staples', 'tablespoon', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFRp3dSIOrj0Gl71YPuOL_GFmteybj0XeJJQ&s', 45),
(2, 'Spaghetti', 'Pantry Staples', 'lb', 'https://t4.ftcdn.net/jpg/17/38/75/51/360_F_1738755191_RaEiZNMpHjYQT1AaMKqDCWRAmjYV0FFx.jpg', 130),
(2, 'Rice', 'Pantry Staples', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNRMiVbBGRyvM-VQlvWhhWQx2nVMabG1v5Lg&s', 160),
(2, 'Oatmeal', 'Pantry Staples', 'lb', 'https://thumbs.dreamstime.com/b/bowl-raw-oatmeal-white-background-150142005.jpg', 100),
-- Store 3 (Organic Plus) - Premium organic items
-- Produce
(3, 'Organic Tomatoes', 'Produce', 'lb', 'https://i5.walmartimages.com/seo/Fresh-Slicing-Tomato-Each_87d9362e-14de-4d0d-ac8a-76e0f45036ee.cc24439f3db08493212df9f0d12f48dd.jpeg?odnHeight=768&odnWidth=768&odnBg=FFFFFF', 60),
(3, 'Organic Spinach', 'Produce', 'bunch', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDr6TxSU65t995cB7IJqr5f-OW2rD9XBdLyA&s', 55),
(3, 'Organic Kale', 'Produce', 'bunch', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGycqflfbF_szPVC-Th3Z31GvVmlffcpRQKw&s', 45),
(3, 'Organic Carrots', 'Produce', 'lb', 'https://www.shutterstock.com/image-photo/carrot-isolated-on-white-background-600nw-795704785.jpg', 70),
(3, 'Organic Bell Peppers', 'Produce', 'each', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRXhbEB6E8PHp7ErZ20JWF22iax136unK3iCw&s', 50),
-- Meats & Seafood
(3, 'Organic Chicken Breast', 'Meats & Seafood', 'lb', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3c8TPpfOuf2MJlFaruD8RoPrR0YDA6r2yfA&s', 30),
(3, 'Organic Eggs', 'Dairy', 'dozen', 'https://media.istockphoto.com/id/1447419799/photo/one-chicken-egg-isolated-on-white-background.jpg?s=612x612&w=0&k=20&c=l9Muo1lMAnmq7ngrICvn0FSSlUgpTGuoLd6MW7WSdw8=', 120),
(3, 'Wild Salmon', 'Meats & Seafood', 'lb', 'https://thumbs.dreamstime.com/b/salmon-white-background-7632375.jpg', 25),
-- Dairy
(3, 'Organic Milk', 'Dairy', 'tablespoon', 'https://media.istockphoto.com/id/484022736/photo/gallon-milk-bottle-with-red-cap-isolated-on-white.jpg?s=612x612&w=0&k=20&c=OKbtAyrLquL5tfHafJiDxuw_Cae6tLp8DKy4scCwUuM=', 80),
(3, 'Organic Butter', 'Dairy', 'tablespoon', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPC8d5i14AozhRZyisQopv5qTyIGUD9cGvTA&s', 60),
(3, 'Almond Milk', 'Dairy', 'tablespoon', 'https://t4.ftcdn.net/jpg/03/83/26/55/360_F_383265527_9gMPCt0FwTQXJruIBlDD6ilqL6tlqtVp.jpg', 70),
-- Bakery & Pantry Staples  
(3, 'Organic Flour', 'Bakery', 'lb', 'https://www.sunorganicfarm.com/cdn/shop/files/organic-whole-wheat-flour-8.jpg?v=1764983675', 75),
(3, 'Sea Salt', 'Pantry Staples', 'oz', 'https://t3.ftcdn.net/jpg/02/14/08/44/360_F_214084425_SjvXMLNevveNuiVX66BXhlj1cEa8vmqD.jpg', 150),
(3, 'Extra Virgin Olive Oil', 'Pantry Staples', 'tablespoon', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRR5vp9xUhmjTrEBR4sHHCjcF5_XPRByTAoiA&s', 65),
(3, 'Organic Quinoa', 'Pantry Staples', 'lb', 'https://static.vecteezy.com/system/resources/previews/022/844/835/large_2x/quinoa-seeds-in-wooden-bowl-isolated-on-white-background-close-up-quinoa-seeds-in-wooden-plate-isolated-on-white-background-quinoa-seeds-in-spoon-isolated-photo.jpg', 80),
(3, 'Brown Rice', 'Pantry Staples', 'lb', 'https://img.freepik.com/premium-vector/red-brown-rice-isolated-white-background_858664-20476.jpg', 90);

-- Insert pantry items for users
INSERT INTO Pantry (user_id, item_id, quantity) VALUES
-- John's pantry (user_id = 4)
(4, 1, 5),  -- Tomatoes
(4, 2, 3),  -- Onions
(4, 3, 2),  -- Garlic
(4, 15, 12), -- Eggs
(4, 17, 1),  -- Milk
(4, 18, 1),  -- Butter
(4, 23, 2),  -- Flour
(4, 25, 1),  -- Salt
(4, 27, 1),  -- Olive Oil
(4, 31, 2),  -- Spaghetti
(4, 33, 3),  -- Rice
-- Jane's pantry (user_id = 5)
(5, 11, 2),  -- Chicken Breast
(5, 2, 4),   -- Onions
(5, 3, 3),   -- Garlic
(5, 4, 2),   -- Bell Peppers
(5, 25, 1),  -- Salt
(5, 26, 1),  -- Black Pepper
(5, 27, 1),  -- Olive Oil
(5, 33, 2),  -- Rice
(5, 37, 1),  -- Chicken Broth
-- Chef Mike's pantry (user_id = 6)
(6, 12, 3),  -- Ground Beef
(6, 1, 6),   -- Tomatoes
(6, 2, 5),   -- Onions
(6, 3, 4),   -- Garlic
(6, 31, 3),  -- Spaghetti
(6, 35, 2),  -- Crushed Tomatoes
(6, 39, 1),  -- Basil
(6, 40, 1),  -- Oregano
(6, 20, 1),  -- Parmesan Cheese
-- Baker Lisa's pantry (user_id = 7)
(7, 15, 24), -- Eggs
(7, 17, 2),  -- Milk
(7, 18, 2),  -- Butter
(7, 23, 5),  -- Flour
(7, 24, 3),  -- Sugar
(7, 25, 1),  -- Salt
(7, 18, 1);  -- Butter

-- Insert recipes
INSERT INTO Recipes (user_id, name, image, is_public, instructions, timers, rating) VALUES
(6, 'Classic Spaghetti Carbonara', 'https://www.budgetbytes.com/wp-content/uploads/2016/05/Spaghetti-Carbonara-Plated-500x500.jpg', TRUE, 
'1. Cook spaghetti according to package directions (10 minutes)
2. While pasta cooks, dice bacon and cook until crispy (8 minutes)
3. Beat eggs with Parmesan cheese in a bowl
4. Drain pasta, reserving 1 cup pasta water
5. Toss hot pasta with bacon, remove from heat
6. Add egg mixture, tossing quickly (2 minutes)
7. Add pasta water to reach desired consistency
8. Season with black pepper and serve immediately',
'Step 1: 10 minutes; Step 2: 8 minutes; Step 6: 2 minutes', 4.5),

(6, 'Chicken Stir Fry', 'https://i2.wp.com/www.downshiftology.com/wp-content/uploads/2021/05/Chicken-Stir-Fry-main.jpg', TRUE,
'1. Cut chicken into bite-sized pieces
2. Heat oil in wok over high heat (2 minutes)
3. Cook chicken until golden (6 minutes)
4. Remove chicken, add vegetables (5 minutes)
5. Return chicken to wok with soy sauce (2 minutes)
6. Serve over rice',
'Step 2: 2 minutes; Step 3: 6 minutes; Step 4: 5 minutes; Step 5: 2 minutes', 4.2),

(4, 'Tomato Basil Pasta', 'https://www.spoonfulofflavor.com/wp-content/uploads/2023/10/tomato-basil-pasta-recipe.jpg', TRUE,
'1. Cook pasta according to package (10 minutes)
2. Sauté garlic in olive oil (2 minutes)
3. Add crushed tomatoes and simmer (15 minutes)
4. Add fresh basil and season (2 minutes)
5. Toss with cooked pasta and serve',
'Step 1: 10 minutes; Step 2: 2 minutes; Step 3: 15 minutes; Step 4: 2 minutes', 4.7),

(5, 'Garlic Butter Shrimp', 'https://www.pantsdownapronson.com/wp-content/uploads/garlic-butter-shrimp.jpg', TRUE,
'1. Peel and devein shrimp
2. Melt butter in pan (2 minutes)
3. Add minced garlic (1 minute)
4. Cook shrimp until pink (4-5 minutes)
5. Season with salt, pepper, and lemon juice
6. Garnish with parsley and serve',
'Step 2: 2 minutes; Step 3: 1 minute; Step 4: 4-5 minutes', 4.8),

(7, 'Chocolate Chip Cookies', 'https://images.aws.nestle.recipes/resized/5b069c3ed2feea79377014f6766fcd49_Original_NTH_Chocolate_Chip_Cookie_448_448.jpg', TRUE,
'1. Preheat oven to 375°F (10 minutes)
2. Cream butter and sugars (3 minutes)
3. Beat in eggs and vanilla (2 minutes)
4. Mix in flour, salt, and baking soda (2 minutes)
5. Fold in chocolate chips (1 minute)
6. Drop spoonfuls on baking sheet
7. Bake for 10-12 minutes
8. Cool on wire rack (15 minutes)',
'Step 1: 10 minutes; Step 2: 3 minutes; Step 3: 2 minutes; Step 4: 2 minutes; Step 5: 1 minute; Step 7: 10-12 minutes; Step 8: 15 minutes', 4.9),

(4, 'Simple Scrambled Eggs', 'https://i2.wp.com/www.downshiftology.com/wp-content/uploads/2025/03/Scrambled-Eggs-main.jpg', FALSE,
'1. Beat eggs with milk and salt (1 minute)
2. Heat butter in pan over medium heat (1 minute)
3. Pour in eggs and let sit (30 seconds)
4. Gently stir until fluffy (3-4 minutes)
5. Remove from heat while slightly wet
6. Season and serve immediately',
'Step 1: 1 minute; Step 2: 1 minute; Step 3: 30 seconds; Step 4: 3-4 minutes', 4.0),

(6, 'Beef Tacos', 'https://loveandgoodstuff.com/wp-content/uploads/2020/08/classic-ground-beef-tacos-1200x1200.jpg', TRUE,
'1. Brown ground beef in skillet (8 minutes)
2. Add taco seasoning and water (5 minutes)
3. Simmer until thickened (5 minutes)
4. Warm taco shells (3 minutes)
5. Assemble with lettuce, cheese, tomatoes
6. Serve with sour cream and salsa',
'Step 1: 8 minutes; Step 2: 5 minutes; Step 3: 5 minutes; Step 4: 3 minutes', 4.6);

-- Insert recipe ingredients
INSERT INTO Recipe_Ingredients (recipe_id, item_id, quantity_needed, measurement_unit) VALUES
-- Spaghetti Carbonara (recipe_id = 1)
(1, 31, 1, 'lb'),
(1, 16, 1, 'lb'),
(1, 15, 4, 'whole'),
(1, 20, 1, 'cup'),
(1, 26, 1, 'tablespoon'),
-- Chicken Stir Fry (recipe_id = 2)
(2, 11, 1, 'lb'),
(2, 4, 2, 'whole'),
(2, 2, 1, 'whole'),
(2, 3, 3, 'cloves'),
(2, 29, 3, 'tablespoons'),
(2, 28, 2, 'tablespoons'),
(2, 33, 2, 'cups'),
-- Tomato Basil Pasta (recipe_id = 3)
(3, 32, 1, 'bunch'),
(3, 35, 1, 'can'),
(3, 3, 4, 'cloves'),
(3, 39, 1, 'cup'),
(3, 27, 3, 'tablespoons'),
(3, 25, 1, 'teaspoon'),
(3, 26, 1, 'teaspoon'),
-- Garlic Butter Shrimp (recipe_id = 4)
(4, 14, 1, 'lb'),
(4, 18, 4, 'tablespoons'),
(4, 3, 6, 'cloves'),
(4, 25, 1, 'teaspoon'),
(4, 26, 1, 'teaspoon'),
-- Chocolate Chip Cookies (recipe_id = 5)
(5, 18, 1, 'cup'),
(5, 24, 1.5, 'cups'),
(5, 15, 2, 'whole'),
(5, 23, 2.25, 'cups'),
(5, 25, 1, 'teaspoon'),
-- Simple Scrambled Eggs (recipe_id = 6)
(6, 15, 3, 'whole'),
(6, 17, 2, 'tablespoons'),
(6, 18, 1, 'tablespoon'),
(6, 25, 0.25, 'teaspoon'),
-- Beef Tacos (recipe_id = 7)
(7, 12, 1, 'lb'),
(7, 2, 1, 'whole'),
(7, 1, 2, 'whole'),
(7, 8, 2, 'cups'),
(7, 19, 1, 'cup'),
(7, 45, 2, 'tablespoons');

-- Insert comments
INSERT INTO Comments (recipe_id, user_id, comment_text, created_at) VALUES
(1, 4, 'This is my go-to carbonara recipe! So creamy and delicious.', '2026-03-01 10:30:00'),
(1, 5, 'Made this last night and my family loved it. The timing instructions were perfect!', '2026-03-02 18:45:00'),
(2, 7, 'Quick and easy weeknight dinner. I added broccoli for extra veggies.', '2026-03-03 19:20:00'),
(3, 5, 'Simple but so flavorful! The fresh basil makes all the difference.', '2026-03-04 12:15:00'),
(3, 6, 'One of the best pasta recipes I''ve tried. Will make again!', '2026-03-05 14:30:00'),
(4, 4, 'The garlic butter sauce is incredible. Served it with pasta.', '2026-03-06 20:10:00'),
(4, 6, 'Restaurant quality! My guests were impressed.', '2026-03-07 13:45:00'),
(5, 4, 'Best chocolate chip cookies ever! Nice and chewy.', '2026-03-08 16:25:00'),
(5, 5, 'My kids request these every week now. Perfect recipe!', '2026-03-09 11:50:00'),
(5, 6, 'These turned out amazing. I added walnuts too.', '2026-03-10 15:35:00'),
(7, 5, 'Easy taco night! The seasoning proportions are just right.', '2026-03-11 19:00:00'),
(2, 4, 'Great recipe! I used tofu instead of chicken and it was still delicious.', '2026-03-12 12:30:00');

-- Insert ratings
INSERT INTO Ratings (recipe_id, user_id, rating, created_at) VALUES
(1, 4, 5.0, '2026-03-01 10:30:00'),
(1, 5, 4.0, '2026-03-02 18:45:00'),
(2, 7, 4.0, '2026-03-03 19:20:00'),
(2, 4, 4.5, '2026-03-12 12:30:00'),
(3, 5, 5.0, '2026-03-04 12:15:00'),
(3, 6, 4.5, '2026-03-05 14:30:00'),
(4, 4, 5.0, '2026-03-06 20:10:00'),
(4, 6, 4.5, '2026-03-07 13:45:00'),
(5, 4, 5.0, '2026-03-08 16:25:00'),
(5, 5, 5.0, '2026-03-09 11:50:00'),
(5, 6, 4.5, '2026-03-10 15:35:00'),
(7, 5, 4.5, '2026-03-11 19:00:00');