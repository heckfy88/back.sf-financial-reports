-- Insert users (already done in your schema)

-- Insert categories for users
-- User 1 (John Doe - PHYSICAL)
-- Insert categories
INSERT INTO finances.category (id, user_id, name, description, type, created_at)
VALUES ('11111111-1111-1111-1111-111111111111', '123e4567-e89b-12d3-a456-426614174000', 'Groceries',
        'Daily food and supermarket expenses', 'EXPENSE', now()),
       ('22222222-2222-2222-2222-222222222222', '123e4567-e89b-12d3-a456-426614174000', 'Salary',
        'Monthly income fromDomain job', 'INCOME', now()),
       ('33333333-3333-3333-3333-333333333333', '123e4567-e89b-12d3-a456-426614174000', 'Utilities',
        'Electricity, water, and internet bills', 'EXPENSE', now()),
       ('44444444-4444-4444-4444-444444444444', '123e4567-e89b-12d3-a456-426614174000', 'Freelance',
        'Income fromDomain freelance work', 'INCOME', now());

-- Insert transactions
INSERT INTO finances.transaction (user_id, category_name, "date", description, amount, status, sender_bank,
                                  sender_account, receiver_bank, receiver_account, receiver_inn, receiver_phone,
                                  receiver_user_type)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'Groceries', current_date - interval '10 day',
        'Weekly grocery shopping', 150.75, 'COMPLETED', 'Bank A', '1234567890', 'Store Bank', '9876543210',
        '123456789012', '+1234567890', 'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Salary', current_date - interval '30 day', 'Monthly salary for April',
        3000.00, 'COMPLETED', 'Employer Bank', '2233445566', 'User Bank', '6677889900', '987654321098', '+10987654321',
        'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Utilities', current_date - interval '5 day', 'Internet bill payment',
        50.00, 'CONFIRMED', 'User Bank', '1122334455', 'ISP Bank', '9988776655', '123123123123', '+11234567890',
        'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Freelance', current_date - interval '20 day',
        'Payment fromDomain freelance client', 800.00, 'COMPLETED', 'Client Bank', '5566778899', 'User Bank',
        '1234432112',
        '999888777666', '+19876543210', 'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Groceries', current_date - interval '3 day',
        'Grocery run before guests arrived', 95.20, 'NEW', 'Bank A', '1010101010', 'Market Bank', '2020202020',
        '321654987012', '+1112223333', 'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Salary', current_date - interval '60 day', 'Salary for March', 3000.00,
        'COMPLETED', 'Employer Bank', '9988776655', 'User Bank', '5566778899', '987987987987', '+1212121212', 'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Utilities', current_date - interval '35 day', 'Electricity bill',
        120.40, 'PROCESSING', 'User Bank', '3344556677', 'Utility Bank', '7766554433', '456456456456', '+1346792580',
        'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Freelance', current_date - interval '12 day', 'Short gig payment',
        450.00, 'CONFIRMED', 'Client Bank', '7788990011', 'User Bank', '1100220033', '112233445566', '+1444555666',
        'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Groceries', current_date - interval '7 day', 'Weekend shopping', 80.30,
        'RETURNED', 'Bank A', '1029384756', 'Supermarket Bank', '5647382910', '654321098765', '+1555666777', 'LEGAL'),
       ('123e4567-e89b-12d3-a456-426614174000', 'Utilities', current_date - interval '15 day', 'Water bill payment',
        30.00, 'CANCELLED', 'User Bank', '4455667788', 'Water Company Bank', '9988776655', '111222333444',
        '+1999888777', 'LEGAL');