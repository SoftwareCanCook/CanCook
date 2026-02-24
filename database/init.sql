-- CanCook Database Schema
-- Drop tables if they exist
DROP TABLE IF EXISTS Ratings;
DROP TABLE IF EXISTS Comments;
DROP TABLE IF EXISTS Recipe_Ingredients;
DROP TABLE IF EXISTS Recipes;
DROP TABLE IF EXISTS Pantry;
DROP TABLE IF EXISTS GroceryItems;
DROP TABLE IF EXISTS User;

-- Create User table
CREATE TABLE User (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  login_attempts SMALLINT NOT NULL DEFAULT 0,
  status SMALLINT NOT NULL DEFAULT 1,
  role VARCHAR(12) NOT NULL DEFAULT 'user'
);

-- Create GroceryItems table
CREATE TABLE GroceryItems (
  id INT AUTO_INCREMENT PRIMARY KEY,
  store_id INT NULL,
  name VARCHAR(355) NOT NULL,
  category VARCHAR(355) NOT NULL,
  quantity INT NOT NULL DEFAULT 0,
  image LONGBLOB NULL,
  stock SMALLINT NOT NULL DEFAULT 0
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
