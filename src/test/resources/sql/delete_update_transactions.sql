-- Insert users (already done in your schema)

-- Insert categories for users
-- User 1 (John Doe - PHYSICAL)
-- Insert categories

-- Insert transactions
INSERT INTO finances.transaction (id, user_id, category_name, "date", description, amount, status, sender_bank,
                                  sender_account, receiver_bank, receiver_account, receiver_inn, receiver_phone,
                                  receiver_user_type)
VALUES ('1b544a13-eef4-4a06-af51-b5df572157d7','123e4567-e89b-12d3-a456-426614174000', 'Groceries', current_date - interval '3 day',
        'Grocery run before guests arrived', 95.20, 'NEW', 'Bank A', '1010101010', 'Market Bank', '2020202020',
        '321654987012', '+1112223333', 'LEGAL'),
       ('6fea80df-10dd-4dcd-a85b-5cc6e668df2d', '123e4567-e89b-12d3-a456-426614174000', 'Groceries', current_date - interval '3 day',
        'Grocery run before guests arrived', 95.20, 'NEW', 'Bank A', '1010101010', 'Market Bank', '2020202020',
        '321654987012', '+1112223333', 'LEGAL');