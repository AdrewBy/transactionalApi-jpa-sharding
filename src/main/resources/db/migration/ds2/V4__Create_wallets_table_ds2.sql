CREATE TABLE transaction.wallets_${shard} (
                                  uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                  created_at timestamp DEFAULT now() NOT NULL,
                                  modified_at timestamp,
                                  name varchar(32) NOT NULL,
                                  wallet_type_uid uuid NOT NULL REFERENCES wallet_types(uid),
                                  user_uid uuid NOT NULL,
                                  status varchar(30) NOT NULL,
                                  balance decimal DEFAULT 0.0 NOT NULL,
                                  archived_at timestamp
);