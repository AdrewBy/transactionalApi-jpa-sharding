CREATE TABLE transaction.transactions_${shard} (
                                       uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                       created_at timestamp DEFAULT now() NOT NULL,
                                       modified_at timestamp,
                                       user_uid uuid NOT NULL,
                                       wallet_uid uuid NOT NULL REFERENCES wallets_${shard}(uid),
                                       wallet_name varchar(32) NOT NULL,
                                       amount decimal DEFAULT 0.0 NOT NULL,
                                       type varchar(32) NOT NULL,
                                       state varchar(32) NOT NULL,
                                       payment_request_uid uuid NOT NULL REFERENCES payment_requests_${shard}(uid) ON DELETE CASCADE
);