CREATE TABLE transaction.payment_requests_${shard} (
                                           uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                           created_at timestamp DEFAULT now() NOT NULL,
                                           modified_at timestamp,
                                           user_uid uuid NOT NULL,
                                           wallet_uid uuid NOT NULL REFERENCES wallets_${shard}(uid),
                                           amount decimal DEFAULT 0.0 NOT NULL,
                                           status varchar,
                                           comment varchar(256),
                                           payment_method_id bigint
);