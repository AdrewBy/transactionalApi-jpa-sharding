CREATE TABLE transaction.wallet_types (
                              uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                              created_at timestamp DEFAULT now() NOT NULL,
                              modified_at timestamp,
                              name varchar(32) NOT NULL,
                              currency_code varchar(3) NOT NULL,
                              status varchar(18) NOT NULL,
                              archived_at timestamp,
                              user_type varchar(15),
                              creator varchar(255),
                              modifier varchar(255)
);