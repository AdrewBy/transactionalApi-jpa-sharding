
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table IF NOT EXISTS wallet_types (
                                            uid uuid default uuid_generate_v4() primary key,
                                            created_at timestamp default now() not null,
                                            modified_at timestamp,
                                            name varchar(32) not null,
                                            currency_code varchar(3) not null,
                                            status varchar(18) not null,
                                            archived_at timestamp,
                                            user_type varchar(15),
                                            creator varchar(255),
                                            modifier varchar(255)
);

create table IF NOT EXISTS wallets (
                                                   uid uuid default uuid_generate_v4() primary key,
                                                   created_at timestamp default now() not null,
                                                   modified_at timestamp,
                                                   name varchar(32) not null,
                                                   wallet_type_uid uuid not null constraint fk_wallets_wallet_types references wallet_types,
                                                   user_uid uuid not null,
                                                   status varchar(30) not null,
                                                   balance decimal default 0.0 not null,
                                                   archived_at timestamp
);

create table IF NOT EXISTS payment_requests (
                                                            uid uuid default uuid_generate_v4() not null primary key,
                                                            created_at timestamp default now() not null,
                                                            modified_at timestamp,
                                                            user_uid uuid not null,
                                                            wallet_uid uuid not null references wallets (uid),
                                                            amount decimal default 0.0 not null,
                                                            status varchar,
                                                            comment varchar(256),
                                                            payment_method_id bigint
);

create table IF NOT EXISTS transactions (
                                                        uid uuid default uuid_generate_v4() not null primary key,
                                                        created_at timestamp default now() not null,
                                                        modified_at timestamp,
                                                        user_uid uuid not null,
                                                        wallet_uid uuid not null references wallets (uid),
                                                        wallet_name varchar(32) not null,
                                                        amount decimal default 0.0 not null,
                                                        type varchar(32) not null,
                                                        state varchar(32) not null,
                                                        payment_request_uid uuid not null references payment_requests on delete cascade
);
create table IF NOT EXISTS top_up_requests (
                                                           uid uuid default uuid_generate_v4() not null primary key,
                                                           created_at timestamp default now() not null,
                                                           provider varchar not null,
                                                           payment_request_uid uuid not null references payment_requests on delete cascade
);
create table IF NOT EXISTS withdrawal_requests (
                                                               uid uuid default uuid_generate_v4() not null primary key,
                                                               created_at timestamp default now() not null,
                                                               payment_request_uid uuid not null references payment_requests on delete cascade
);
create table IF NOT EXISTS transfer_requests (
                                                             uid uuid default uuid_generate_v4() not null primary key,
                                                             created_at timestamp default now() not null,
                                                             system_rate varchar not null,
                                                             payment_request_uid_from uuid not null references payment_requests on delete cascade,
                                                             payment_request_uid_to uuid not null references payment_requests on delete cascade
);
