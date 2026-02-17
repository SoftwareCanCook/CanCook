const express = require('express');
const mariadb = require('mariadb');
const cors = require('cors');

const app = express();
const port = 3000;

app.use(cors());
app.use(express.json());

const pool = mariadb.createPool({
  host: process.env.DB_HOST || 'localhost',
  user: process.env.DB_USER || 'user',
  password: process.env.DB_PASSWORD || 'password',
  database: process.env.DB_NAME || 'cancook',
  connectionLimit: 5,
  port: process.env.DB_PORT || 3306
});

// Attempts to retrieve data from the recipes table
app.get('api/recipes', async (req, res) => {
  let conn;
  try{
    conn = await pool.getConnection();
    const rows = await conn.query("SELECT * FROM recipes");
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  } finally {
    if (conn) conn.release();
  }
});

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});