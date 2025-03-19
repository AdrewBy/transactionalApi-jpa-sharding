DO
$$
    DECLARE
        shard INT;
    BEGIN
        FOR shard IN 0..3
            LOOP
                EXECUTE format('
      CREATE TABLE transaction.wallets_%s (
        uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
        created_at TIMESTAMP DEFAULT now() NOT NULL,
        modified_at TIMESTAMP,
        name VARCHAR(32) NOT NULL,
        wallet_type_uid UUID NOT NULL REFERENCES transaction.wallet_types(uid),
        user_uid UUID NOT NULL,
        status VARCHAR(30) NOT NULL,
        balance DECIMAL DEFAULT 0.0 NOT NULL,
        archived_at TIMESTAMP
      );
    ', shard);
            END LOOP;
    END
$$;
