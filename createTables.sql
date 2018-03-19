CREATE TABLE public.bank
(
    id text COLLATE pg_catalog."default",
    name text COLLATE pg_catalog."default",
    branch text COLLATE pg_catalog."default",
    city text COLLATE pg_catalog."default",
    state text COLLATE pg_catalog."default",
    country text COLLATE pg_catalog."default",
    ifsc text COLLATE pg_catalog."default",
    micr text COLLATE pg_catalog."default",
    url text COLLATE pg_catalog."default"
);


CREATE TABLE public.customerdetails
(
    firstname text COLLATE pg_catalog."default",
    lastname text COLLATE pg_catalog."default",
    password text COLLATE pg_catalog."default",
    accountno text COLLATE pg_catalog."default",
    bankname text COLLATE pg_catalog."default",
    ifsccode text COLLATE pg_catalog."default",
    userid text COLLATE pg_catalog."default"
);


CREATE TABLE public.transactiondetails
(
    accountno text COLLATE pg_catalog."default",
    type text COLLATE pg_catalog."default",
    material text COLLATE pg_catalog."default",
    datetime timestamp without time zone,
    "from" text COLLATE pg_catalog."default",
    ifsccode text COLLATE pg_catalog."default",
    amount numeric DEFAULT 0,
    balance numeric DEFAULT 0
);

alter table customerdetails add "adharno" text;
