DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS public.users (
	user_id BIGINT GENERATED ALWAYS AS IDENTITY,
	"name" varchar(255) NOT NULL,
	email varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_id),
	CONSTRAINT users_unique UNIQUE (email)
);


-- public.requests определение

-- Drop table

-- DROP TABLE requests;

CREATE TABLE IF NOT EXISTS public.requests (
	request_id BIGINT GENERATED ALWAYS AS IDENTITY,
	description varchar NOT NULL,
	requestor_id BIGINT NOT NULL,
	CONSTRAINT requests_pk PRIMARY KEY (request_id),
	CONSTRAINT requests_users_fk FOREIGN KEY (requestor_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE SET NULL
);


-- public.items определение

-- Drop table

-- DROP TABLE items;

CREATE TABLE IF NOT EXISTS public.items (
	item_id BIGINT GENERATED ALWAYS AS IDENTITY,
	"name" varchar(255) NOT NULL,
	description varchar NOT NULL,
	available bool NULL,
	owner_id BIGINT NOT NULL,
	request_id BIGINT NULL,
	CONSTRAINT newtable_pk PRIMARY KEY (item_id),
	CONSTRAINT items_requests_fk FOREIGN KEY (request_id) REFERENCES requests(request_id) ON DELETE SET NULL ON UPDATE CASCADE,
	CONSTRAINT items_users_fk FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- public.booking определение

-- Drop table

-- DROP TABLE booking;

CREATE TABLE IF NOT EXISTS public.bookings (
	booking_id BIGINT GENERATED ALWAYS AS IDENTITY,
	start_date timestamp NOT NULL,
	end_date timestamp NOT NULL,
	item_id BIGINT NOT NULL,
	booker_id BIGINT NOT NULL,
	status varchar(10) NOT NULL,
	CONSTRAINT status CHECK (status IN ('WAITING', 'APPROVED', 'REJECTED')),
	CONSTRAINT booking_pk PRIMARY KEY (booking_id),
	CONSTRAINT booking_items_fk FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE SET NULL ON UPDATE CASCADE,
	CONSTRAINT booking_users_fk FOREIGN KEY (booker_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE
);


-- public."comments" определение

-- Drop table

-- DROP TABLE "comments";

CREATE TABLE IF NOT EXISTS public."comments" (
	comment_id BIGINT GENERATED ALWAYS AS IDENTITY,
	"text" varchar NOT NULL,
	item_id BIGINT NOT NULL,
	author_id BIGINT NOT NULL,
	created timestamp NOT NULL,
	CONSTRAINT comments_pk PRIMARY KEY (comment_id),
	CONSTRAINT comments_items_fk FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE SET NULL ON UPDATE CASCADE,
	CONSTRAINT comments_users_fk FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);