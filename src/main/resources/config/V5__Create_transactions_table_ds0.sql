DO
$$
    DECLARE
        shard INT;
    BEGIN
        FOR shard IN 0..3
            LOOP
                EXECUTE format('
      CREATE TABLE transaction.transactions_%s (
                                       uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                       created_at timestamp DEFAULT now() NOT NULL,
                                       modified_at timestamp,
                                       user_uid uuid NOT NULL,
                                       wallet_uid uuid NOT NULL REFERENCES transaction.wallets_%s(uid),
                                       wallet_name varchar(32) NOT NULL,
                                       amount decimal DEFAULT 0.0 NOT NULL,
                                       type varchar(32) NOT NULL,
                                       state varchar(32) NOT NULL,
                                       payment_request_uid uuid NOT NULL REFERENCES transaction.payment_requests_%s(uid) ON DELETE CASCADE
);
    ', shard, shard, shard);
            END LOOP;
    END
$$;
