-- public.booking определение

-- Drop table

-- DROP TABLE public.booking;

CREATE TABLE IF NOT EXISTS public.booking (
	id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	start_date timestamp NOT NULL,
	end_date timestamp NOT NULL,
	item_id int4 NOT NULL,
	booker_id int4 NOT NULL,
	status public.request_state NOT NULL,
	CONSTRAINT booking_pk PRIMARY KEY (id)
);


-- public.items определение

-- Drop table

-- DROP TABLE public.items;

CREATE TABLE public.items (
	item_id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar(255) NOT NULL,
	description varchar NOT NULL,
	is_available bit(1) NULL,
	owner_id int4 NOT NULL,
	request_id int4 NULL,
	CONSTRAINT newtable_pk PRIMARY KEY (item_id)
);


-- public.users определение

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
	"name" varchar(255) NOT NULL,
	email varchar NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_unique UNIQUE (email)
);