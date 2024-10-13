CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE countries (
                                  id SERIAL PRIMARY KEY,
                                  created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  name VARCHAR(32),
                                  alpha2 VARCHAR(2),
                                  alpha3 VARCHAR(3),
                                  status VARCHAR(32)
);

CREATE TABLE addresses (
                                  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                  created TIMESTAMP NOT NULL,
                                  updated TIMESTAMP NOT NULL,
                                  country_id INTEGER REFERENCES countries (id),
                                  address VARCHAR(128),
                                  zip_code VARCHAR(32),
                                  archived TIMESTAMP NOT NULL,
                                  city VARCHAR(32),
                                  state VARCHAR(32)
);

CREATE TABLE users (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              secret_key VARCHAR(32),
                              created TIMESTAMP NOT NULL,
                              updated TIMESTAMP NOT NULL,
                              first_name VARCHAR(32),
                              last_name VARCHAR(32),
                              verified_at TIMESTAMP NOT NULL,
                              archived_at TIMESTAMP NOT NULL,
                              status VARCHAR(64),
                              filled BOOLEAN,
                              address_id UUID REFERENCES addresses(id)
);

CREATE TABLE individuals (
                                    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                    user_id UUID UNIQUE REFERENCES users(id),
                                    created TIMESTAMP NOT NULL,
                                    updated TIMESTAMP NOT NULL,
                                    passport_number VARCHAR(32),
                                    phone_number VARCHAR(32),
                                    email VARCHAR(32),
                                    verified_at TIMESTAMP NOT NULL,
                                    archived_at TIMESTAMP NOT NULL,
                                    status VARCHAR(32)
);

CREATE TABLE user_history (
                                     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                     created TIMESTAMP NOT NULL,
                                     user_id UUID REFERENCES users(id),
                                     user_type VARCHAR(32),
                                     reason VARCHAR(255),
                                     comment VARCHAR(255),
                                     changed_values VARCHAR(1024)
);
