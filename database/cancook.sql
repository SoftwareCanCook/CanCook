Table User {
  id integer [pk, increment]
  username varchar(50) [not null, unique]
  email varchar(100) [not null, unique]
  password varchar(255) [not null]
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
  image longblob [null]
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

Table Comments {
  id integer [pk, increment]
  recipe_id integer [not null]
  user_id integer [not null]
  comment_text text [not null]
  created_at timestamp [not null, default: `CURRENT_TIMESTAMP`]
}

Table Ratings {
  id integer [pk, increment]
  recipe_id integer [not null]
  user_id integer [not null]
  rating float [not null]
  created_at timestamp [not null, default: `CURRENT_TIMESTAMP`]
}

Ref: User.id < Pantry.user_id
Ref: User.id < Recipes.user_id
Ref: GroceryItems.id < Pantry.item_id
Ref: Recipes.id < Recipe_Ingredients.recipe_id
Ref: GroceryItems.id < Recipe_Ingredients.item_id
Ref: Recipes.id < Comments.recipe_id
Ref: User.id < Comments.user_id
Ref: Recipes.id < Ratings.recipe_id
Ref: User.id < Ratings.user_id

-- Initialize the admins and grocery admins
INSERT INTO cancook.`User` (username, email, password, login_attempts, role, status)
VALUES ('admin1', 'admin1@example.com', '$2a$10$BM4V/w4mN2nBINJo4JS9AuzHMaUC7fvI/9bVB4AQhhMKKJvxSY1gi', 0, 'admin', 1);

INSERT INTO cancook.`User` (username, email, password, login_attempts, role, status)
VALUES ('admin2', 'admin2@example.com', '$2a$10$/uKI0lLnfcmaEgqhTHOdiO76u.QS4D84/QdUNTN1WfYZsrGyQ.x4O', 0, 'admin', 1);

INSERT INTO cancook.`User` (username, email, password, login_attempts, role, status)
VALUES ('groceryAdmin1', 'groceryAdmin1@example.com', '$2a$10$BM4V/w4mN2nBINJo4JS9AuzHMaUC7fvI/9bVB4AQhhMKKJvxSY1gi', 0, 'grocery', 1);

INSERT INTO cancook.`User` (username, email, password, login_attempts, role, status)
VALUES ('groceryAdmin2', 'groceryAdmin2@example.com', '$2a$10$/uKI0lLnfcmaEgqhTHOdiO76u.QS4D84/QdUNTN1WfYZsrGyQ.x4O', 0, 'grocery', 1);

-- Add a default user for testing
INSERT INTO cancook.`User` (username, email, password, login_attempts, role, status)
VALUES ('lyalls', 'lyalls2004@gmail.com', '$2a$10$u5UCobtnDz6jFq7Lg74MUOzNN2mKiL.jH9.QwAtgkpjkfw3MBfiSG', 0, 'user', 1);