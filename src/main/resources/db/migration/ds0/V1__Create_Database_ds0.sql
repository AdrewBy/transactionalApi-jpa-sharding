DO
$$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db0') THEN
            CREATE DATABASE db0;
        END IF;
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db1') THEN
            CREATE DATABASE db1;
        END IF;
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db2') THEN
            CREATE DATABASE db2;
        END IF;
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db3') THEN
            CREATE DATABASE db3;
        END IF;
    END
$$;