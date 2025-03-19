DO
$$
    DECLARE
        shard INT;
    BEGIN
        FOR shard IN 0..3
            LOOP
                EXECUTE format('
    CREATE TABLE transaction.transfer_requests_%s (
                                            uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                            created_at timestamp DEFAULT now() NOT NULL,
                                            system_rate varchar NOT NULL,
                                            payment_request_uid_from uuid NOT NULL REFERENCES transaction.payment_requests_%s(uid) ON DELETE CASCADE,
                                            payment_request_uid_to uuid NOT NULL REFERENCES transaction.payment_requests_%s(uid) ON DELETE CASCADE
);
    ', shard, shard, shard);
            END LOOP;
    END
$$;