# Creating the Interest List Table

This document outlines the steps to create an `interest_list` table in your database using SQL.

## Table Structure

The `interest_list` table will contain the following fields:
- `id`: A unique identifier for each entry (primary key)
- `name`: The name of the person interested
- `email`: Email address for contact
- `phone`: Phone number (optional)
- `interest_area`: What they're interested in
- `comments`: Additional comments or notes
- `created_at`: Timestamp when the entry was created

## SQL Commands

Create the table using the following SQL:

```sql
CREATE TABLE interest_list (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    interest_area VARCHAR(50) NOT NULL,
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Adding Indexes

For better performance, add indexes to frequently searched fields:

```sql
CREATE INDEX idx_interest_list_email ON interest_list(email);
CREATE INDEX idx_interest_list_interest_area ON interest_list(interest_area);
```

## Sample Insert Statement

Here's an example of how to insert a record into the table:

```sql
INSERT INTO interest_list (name, email, phone, interest_area, comments)
VALUES ('John Doe', 'john.doe@example.com', '555-123-4567', 'Product Demo', 'Interested in scheduling a demo next month');
```

## Integration with Flyway

To include this in your Flyway migrations:

1. Create a new SQL migration file in your Flyway migrations directory
2. Name it following Flyway's naming convention (e.g., `V2__Create_interest_list_table.sql`)
3. Add the CREATE TABLE and CREATE INDEX statements to this file
4. Run Flyway migrate to apply the changes
