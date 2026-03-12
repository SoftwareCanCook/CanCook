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
  quantity INT NOT NULL DEFAULT 0,
  image LONGBLOB NULL,
  stock SMALLINT NOT NULL DEFAULT 0,
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
  image LONGBLOB NULL,
  is_public BOOLEAN NOT NULL DEFAULT TRUE,
  instructions_and_timers TEXT NOT NULL,
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
INSERT INTO GroceryItems (store_id, name, category, quantity, stock) VALUES
-- Produce
(1, 'Tomatoes', 'Produce', 100, 1),
(1, 'Onions', 'Produce', 150, 1),
(1, 'Garlic', 'Produce', 200, 1),
(1, 'Bell Peppers', 'Produce', 80, 1),
(1, 'Carrots', 'Produce', 120, 1),
(1, 'Celery', 'Produce', 90, 1),
(1, 'Potatoes', 'Produce', 200, 1),
(1, 'Lettuce', 'Produce', 60, 1),
(1, 'Spinach', 'Produce', 75, 1),
(1, 'Mushrooms', 'Produce', 55, 1),
-- Proteins
(1, 'Chicken Breast', 'Meat', 50, 1),
(1, 'Ground Beef', 'Meat', 60, 1),
(1, 'Salmon Fillet', 'Seafood', 30, 1),
(1, 'Shrimp', 'Seafood', 40, 1),
(1, 'Eggs', 'Dairy', 200, 1),
(1, 'Bacon', 'Meat', 70, 1),
-- Dairy
(1, 'Milk', 'Dairy', 100, 1),
(1, 'Butter', 'Dairy', 80, 1),
(1, 'Cheddar Cheese', 'Dairy', 65, 1),
(1, 'Parmesan Cheese', 'Dairy', 50, 1),
(1, 'Mozzarella Cheese', 'Dairy', 55, 1),
(1, 'Heavy Cream', 'Dairy', 45, 1),
-- Pantry Staples
(1, 'All-Purpose Flour', 'Baking', 100, 1),
(1, 'Sugar', 'Baking', 120, 1),
(1, 'Salt', 'Spices', 200, 1),
(1, 'Black Pepper', 'Spices', 150, 1),
(1, 'Olive Oil', 'Oils', 90, 1),
(1, 'Vegetable Oil', 'Oils', 85, 1),
(1, 'Soy Sauce', 'Condiments', 70, 1),
(1, 'Balsamic Vinegar', 'Condiments', 60, 1),
-- Pasta & Grains
(1, 'Spaghetti', 'Pasta', 150, 1),
(1, 'Penne Pasta', 'Pasta', 140, 1),
(1, 'Rice', 'Grains', 180, 1),
(1, 'Quinoa', 'Grains', 70, 1),
-- Canned Goods
(1, 'Crushed Tomatoes', 'Canned', 100, 1),
(1, 'Tomato Paste', 'Canned', 90, 1),
(1, 'Chicken Broth', 'Canned', 80, 1),
(1, 'Coconut Milk', 'Canned', 65, 1),
-- Spices & Herbs
(1, 'Basil', 'Spices', 100, 1),
(1, 'Oregano', 'Spices', 95, 1),
(1, 'Thyme', 'Spices', 90, 1),
(1, 'Rosemary', 'Spices', 85, 1),
(1, 'Paprika', 'Spices', 80, 1),
(1, 'Cumin', 'Spices', 75, 1),
(1, 'Chili Powder', 'Spices', 70, 1),
-- Store 2 (Green Grocers Westside) - Items
-- Produce
(2, 'Tomatoes', 'Produce', 85, 1),
(2, 'Onions', 'Produce', 130, 1),
(2, 'Garlic', 'Produce', 180, 1),
(2, 'Bell Peppers', 'Produce', 70, 1),
(2, 'Broccoli', 'Produce', 60, 1),
(2, 'Cucumbers', 'Produce', 90, 1),
(2, 'Avocados', 'Produce', 50, 1),
-- Proteins
(2, 'Chicken Breast', 'Meat', 45, 1),
(2, 'Ground Turkey', 'Meat', 40, 1),
(2, 'Pork Chops', 'Meat', 35, 1),
(2, 'Eggs', 'Dairy', 180, 1),
-- Dairy
(2, 'Milk', 'Dairy', 95, 1),
(2, 'Butter', 'Dairy', 70, 1),
(2, 'Greek Yogurt', 'Dairy', 60, 1),
(2, 'Cream Cheese', 'Dairy', 55, 1),
-- Pantry
(2, 'All-Purpose Flour', 'Baking', 90, 1),
(2, 'Brown Sugar', 'Baking', 85, 1),
(2, 'Salt', 'Spices', 180, 1),
(2, 'Olive Oil', 'Oils', 80, 1),
(2, 'Honey', 'Condiments', 45, 1),
-- Pasta & Grains  
(2, 'Spaghetti', 'Pasta', 130, 1),
(2, 'Rice', 'Grains', 160, 1),
(2, 'Oatmeal', 'Grains', 100, 1),
-- Store 3 (Organic Plus) - Premium organic items
-- Produce
(3, 'Organic Tomatoes', 'Produce', 60, 1),
(3, 'Organic Spinach', 'Produce', 55, 1),
(3, 'Organic Kale', 'Produce', 45, 1),
(3, 'Organic Carrots', 'Produce', 70, 1),
(3, 'Organic Bell Peppers', 'Produce', 50, 1),
-- Proteins
(3, 'Organic Chicken Breast', 'Meat', 30, 1),
(3, 'Organic Eggs', 'Dairy', 120, 1),
(3, 'Wild Salmon', 'Seafood', 25, 1),
-- Dairy
(3, 'Organic Milk', 'Dairy', 80, 1),
(3, 'Organic Butter', 'Dairy', 60, 1),
(3, 'Almond Milk', 'Dairy', 70, 1),
-- Pantry
(3, 'Organic Flour', 'Baking', 75, 1),
(3, 'Coconut Sugar', 'Baking', 55, 1),
(3, 'Sea Salt', 'Spices', 150, 1),
(3, 'Extra Virgin Olive Oil', 'Oils', 65, 1),
(3, 'Organic Quinoa', 'Grains', 80, 1),
(3, 'Brown Rice', 'Grains', 90, 1);

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
INSERT INTO Recipes (user_id, name, is_public, instructions_and_timers, rating) VALUES
(6, 'Classic Spaghetti Carbonara', TRUE, 
'1. Cook spaghetti according to package directions (10 minutes)
2. While pasta cooks, dice bacon and cook until crispy (8 minutes)
3. Beat eggs with Parmesan cheese in a bowl
4. Drain pasta, reserving 1 cup pasta water
5. Toss hot pasta with bacon, remove from heat
6. Add egg mixture, tossing quickly (2 minutes)
7. Add pasta water to reach desired consistency
8. Season with black pepper and serve immediately', 4.5),

(6, 'Chicken Stir Fry', TRUE,
'1. Cut chicken into bite-sized pieces
2. Heat oil in wok over high heat (2 minutes)
3. Cook chicken until golden (6 minutes)
4. Remove chicken, add vegetables (5 minutes)
5. Return chicken to wok with soy sauce (2 minutes)
6. Serve over rice', 4.2),

(4, 'Tomato Basil Pasta', TRUE,
'1. Cook pasta according to package (10 minutes)
2. Sauté garlic in olive oil (2 minutes)
3. Add crushed tomatoes and simmer (15 minutes)
4. Add fresh basil and season (2 minutes)
5. Toss with cooked pasta and serve', 4.7),

(5, 'Garlic Butter Shrimp', TRUE,
'1. Peel and devein shrimp
2. Melt butter in pan (2 minutes)
3. Add minced garlic (1 minute)
4. Cook shrimp until pink (4-5 minutes)
5. Season with salt, pepper, and lemon juice
6. Garnish with parsley and serve', 4.8),

(7, 'Chocolate Chip Cookies', TRUE,
'1. Preheat oven to 375°F (10 minutes)
2. Cream butter and sugars (3 minutes)
3. Beat in eggs and vanilla (2 minutes)
4. Mix in flour, salt, and baking soda (2 minutes)
5. Fold in chocolate chips (1 minute)
6. Drop spoonfuls on baking sheet
7. Bake for 10-12 minutes
8. Cool on wire rack (15 minutes)', 4.9),

(4, 'Simple Scrambled Eggs', FALSE,
'1. Beat eggs with milk and salt (1 minute)
2. Heat butter in pan over medium heat (1 minute)
3. Pour in eggs and let sit (30 seconds)
4. Gently stir until fluffy (3-4 minutes)
5. Remove from heat while slightly wet
6. Season and serve immediately', 4.0),

(6, 'Beef Tacos', TRUE,
'1. Brown ground beef in skillet (8 minutes)
2. Add taco seasoning and water (5 minutes)
3. Simmer until thickened (5 minutes)
4. Warm taco shells (3 minutes)
5. Assemble with lettuce, cheese, tomatoes
6. Serve with sour cream and salsa', 4.6);

-- Insert recipe ingredients
INSERT INTO Recipe_Ingredients (recipe_id, item_id, quantity_needed, measurement_unit) VALUES
-- Spaghetti Carbonara (recipe_id = 1)
(1, 31, 1, 'pound'),
(1, 16, 8, 'slices'),
(1, 15, 4, 'whole'),
(1, 20, 1, 'cup'),
(1, 26, 1, 'tablespoon'),
-- Chicken Stir Fry (recipe_id = 2)
(2, 11, 1, 'pound'),
(2, 4, 2, 'whole'),
(2, 2, 1, 'whole'),
(2, 3, 3, 'cloves'),
(2, 29, 3, 'tablespoons'),
(2, 28, 2, 'tablespoons'),
(2, 33, 2, 'cups'),
-- Tomato Basil Pasta (recipe_id = 3)
(3, 32, 1, 'pound'),
(3, 35, 1, 'can'),
(3, 3, 4, 'cloves'),
(3, 39, 1, 'cup'),
(3, 27, 3, 'tablespoons'),
(3, 25, 1, 'teaspoon'),
(3, 26, 1, 'teaspoon'),
-- Garlic Butter Shrimp (recipe_id = 4)
(4, 14, 1, 'pound'),
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
(7, 12, 1, 'pound'),
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