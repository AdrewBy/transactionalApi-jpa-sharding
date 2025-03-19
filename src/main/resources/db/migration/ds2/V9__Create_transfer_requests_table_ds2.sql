CREATE TABLE transaction.transfer_requests_${shard} (
                                            uid uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
                                            created_at timestamp DEFAULT now() NOT NULL,
                                            system_rate varchar NOT NULL,
                                            payment_request_uid_from uuid NOT NULL REFERENCES payment_requests_${shard}(uid) ON DELETE CASCADE,
                                            payment_request_uid_to uuid NOT NULL REFERENCES payment_requests_${shard}(uid) ON DELETE CASCADE
);