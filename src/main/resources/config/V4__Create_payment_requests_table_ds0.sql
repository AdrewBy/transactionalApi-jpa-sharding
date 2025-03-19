DO
$$
    DECLARE
        shard INT;
    BEGIN
        FOR shard IN 0..3
            LOOP
                EXECUTE format('
      CREATE TABLE transaction.payment_requests_%s (
        uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
        created_at TIMESTAMP DEFAULT now() NOT NULL,
        modified_at TIMESTAMP,
        user_uid UUID NOT NULL,
        wallet_uid UUID NOT NULL REFERENCES transaction.wallets_%s(uid),
        amount DECIMAL DEFAULT 0.0 NOT NULL,
        status VARCHAR(255),
        comment VARCHAR(256),
        payment_method_id BIGINT
      );
    ', shard, shard);
            END LOOP;
    END
$$;
