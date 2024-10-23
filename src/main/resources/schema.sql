--create types
--DO $$
--BEGIN
--    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'request_state') THEN
--        CREATE TYPE public.request_state AS ENUM (
--        	'WAITING',
--        	'APPROVED',
--        	'REJECTED',
--        	'CANCELED');
--    END IF;
--    --more types here...
--END$$;

-- public.users определение

-- Drop table

-- DROP TABLE users;

CREATE TABLE IF NOT EXISTS public.users (
	user_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar(255) NOT NULL,
	email varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_id),
	CONSTRAINT users_unique UNIQUE (email)
);


-- public.requests определение

-- Drop table

-- DROP TABLE requests;

CREATE TABLE IF NOT EXISTS public.requests (
	request_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	description varchar NOT NULL,
	requestor_id int4 NOT NULL,
	CONSTRAINT requests_pk PRIMARY KEY (request_id),
	CONSTRAINT requests_users_fk FOREIGN KEY (requestor_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE SET NULL
);


-- public.items определение

-- Drop table

-- DROP TABLE items;

CREATE TABLE IF NOT EXISTS public.items (
	item_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar(255) NOT NULL,
	description varchar NOT NULL,
	is_available bit(1) NULL,
	owner_id int4 NOT NULL,
	request_id int4 NULL,
	CONSTRAINT newtable_pk PRIMARY KEY (item_id),
	CONSTRAINT items_requests_fk FOREIGN KEY (request_id) REFERENCES requests(request_id) ON DELETE SET NULL ON UPDATE CASCADE,
	CONSTRAINT items_users_fk FOREIGN KEY (owner_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- public.booking определение

-- Drop table

-- DROP TABLE booking;

CREATE TABLE IF NOT EXISTS public.bookings (
	booking_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	start_date timestamp NOT NULL,
	end_date timestamp NOT NULL,
	item_id int4 NOT NULL,
	booker_id int4 NOT NULL,
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
	comment_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"text" varchar NOT NULL,
	item_id int4 NOT NULL,
	author_id int4 NOT NULL,
	created timestamp NOT NULL,
	CONSTRAINT comments_pk PRIMARY KEY (comment_id),
	CONSTRAINT comments_items_fk FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE SET NULL ON UPDATE CASCADE,
	CONSTRAINT comments_users_fk FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);