CREATE TABLE transaction.top_up_requests_${shard} (
                                          uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                          created_at timestamp DEFAULT now() NOT NULL,
                                          provider varchar NOT NULL,
                                          payment_request_uid uuid NOT NULL REFERENCES payment_requests_${shard}(uid) ON DELETE CASCADE
);