DO
$$
    DECLARE
        shard INT;
    BEGIN
        FOR shard IN 0..3
            LOOP
                EXECUTE format('
      CREATE TABLE transaction.top_up_requests_%s (
                                          uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                          created_at timestamp DEFAULT now() NOT NULL,
                                          provider varchar NOT NULL,
                                          payment_request_uid uuid NOT NULL REFERENCES transaction.payment_requests_%s(uid) ON DELETE CASCADE
);
    ', shard, shard);
            END LOOP;
    END
$$;