Table User {
  id integer [pk, increment]
  username varchar(8) [not null]
  password varchar(12) [not null]
  login_attempts smallint [not null, default: 0]
  status smallint [not null, default: 1]
  role varchar(12) [not null, default: 'user']
}

Table GroceryItems {
  id integer [pk, increment]
  store_id integer [null]
  name varchar(355) [not null]
  category varchar(355) [not null]
  quantity integer [not null, default: 0]
  image longblob [null]
  stock smallint [not null, default: 0]
}

Table Pantry {
  id integer [pk, increment]
  user_id integer [not null]
  item_id integer [not null]
  quantity integer [not null]
}

Table Recipes {
  id integer [pk, increment]
  user_id integer [not null]
  name varchar(255) [not null]
  image_url varchar(500) [null]
  is_public boolean [not null, default: true]
  instructions_and_timers text [not null]
  rating float [null]
}

Table Recipe_Ingredients {
  id integer [pk, increment]
  recipe_id integer [not null]
  item_id integer [not null]
  quantity_needed integer [not null]
  measurement_unit varchar(50) [not null]
}

Ref: User.id < Pantry.user_id
Ref: User.id < Recipes.user_id
Ref: GroceryItems.id < Pantry.item_id
Ref: Recipes.id < Recipe_Ingredients.recipe_id
Ref: GroceryItems.id < Recipe_Ingredients.item_id


INSERT INTO User (username, password, login_attempts, status, role) VALUES
('adminusr', 'admPassword!', 0, 1, 'admin'),
('user1234', 'Password123!', 0, 1, 'user');

INSERT INTO GroceryItems (store_id, name, category, quantity, stock) VALUES
(1, 'Milk', 'Dairy', 100, 1),
(1, 'Lettuce', 'Vegetable', 200, 1),
(2, 'Bread', 'Bakery', 150, 1),
(2, 'Butter', 'Dairy', 80, 1),
(1, 'Chicken Breast', 'Meat', 50, 1);

INSERT INTO Pantry (user_id, item_id, quantity) VALUES
(1, 1, 2),
(1, 2, 1),
(2, 3, 3),
(2, 4, 1);

INSERT INTO Recipes (user_id, name, image_url, is_public, instructions_and_timers, rating) VALUES
(1, 'Grilled Chicken Salad', 'https://raw.githubusercontent.com/SoftwareCanCook/CanCookWebsite/refs/heads/main/apple.jpg', true, '1. Grill chicken breast for 5-7 minutes per side. 2. Chop lettuce and mix with grilled chicken. 3. Add dressing of choice.', 4.5),
(2, 'Butter Toast', 'https://raw.githubusercontent.com/SoftwareCanCook/CanCookWebsite/refs/heads/main/apple.jpg', true, '1. Toast bread until golden brown. 2. Spread butter on hot toast.', 4.0);

INSERT INTO Recipe_Ingredients (recipe_id, item_id, quantity_needed, measurement_unit) VALUES
(1, 1, 2, 'pieces'),
(1, 2, 1, 'head'),
(2, 3, 2, 'slices'),
(2, 4, 1, 'tablespoon');