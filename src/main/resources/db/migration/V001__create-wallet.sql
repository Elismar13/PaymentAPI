create table if not exists Wallets (
	id uuid default uuid_generate_v4 (),
	owner_name varchar(255) not null,
	registration_date timestamp not null default current_timestamp,
	
	primary key (id)
);