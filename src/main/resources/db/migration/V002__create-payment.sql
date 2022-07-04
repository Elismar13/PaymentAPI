create table if not exists Payments (
	id serial not null,
	amount numeric(5,2),
	creation_date timestamp default current_timestamp,
	owner_id uuid,
	
	primary key (id),
	foreign key (owner_id) references Wallets (id)
);