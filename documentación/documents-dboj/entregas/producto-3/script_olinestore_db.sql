-- Creamos la base de datos
create database if not exists onlinestore_db;

-- Seleccionamos la base de datos
use onlinestore_db;

-- Para que funciones las funciones y procedimientos que modifican datos
SET GLOBAL log_bin_trust_function_creators = 1;

-- Path: script_olinestore-db.sql

-- Creamos la tabla de direcciones que usarán los clientes
create table if not exists direcciones(
  _id int unsigned not null auto_increment,
  direccion varchar(255) not null,
  ciudad varchar(255) not null,
  provincia varchar(255) not null,
  codigo_postal varchar(255) not null,
  pais varchar(255) not null,
  primary key (_id)
);

-- Creamos la tabla padre de clientes
create table if not exists clientes(
  _id int unsigned not null auto_increment,
  nombre varchar(255) not null,
  -- Creamos la relación con la tabla de direcciones
  direccion_id int unsigned,
  nif varchar(255) not null,
  email varchar(255) not null,
  primary key (_id)
);

-- Creamos las relaciones FK
alter table clientes
  add constraint fk_direcciones foreign key (direccion_id) references direcciones(_id) on delete set null on update cascade;

-- Crear un constraint para que el nif y el email sean únicos sea único
alter table clientes
	add constraint unique_nif unique (nif),
	add constraint unique_email unique (email);

-- Creamos un trigger para que si se elimina un cliente padre, se elimina el cliente hijo
-- que le corresponda
delimiter //
create trigger tdelete_cliente
  before delete on clientes
  for each row
  begin
    if old._id in (select cliente_id from clientes_estandard) then
      delete from clientes_estandard where cliente_id = old._id and _id > 0;
    elseif old._id in (select cliente_id from clientes_premium) then
      delete from clientes_premium where cliente_id = old._id and _id > 0;
    end if;
  end;
//
delimiter ;

-- Creamos la tabla hija tipo de cliente estandard
create table if not exists clientes_estandard(
  _id int unsigned not null auto_increment,
  -- Creamos la relación con la tabla padre
	cliente_id int unsigned,
	primary key (_id)
);

-- Creamos las relaciones FK
alter table clientes_estandard 
  add constraint fk_clientes_estandard foreign key (cliente_id) references clientes(_id) on delete set null on update cascade;

-- Creamos la tabla hija tipo de cliente premium
create table if not exists clientes_premium(
  _id int unsigned not null auto_increment,
  -- Creamos la relación con la tabla padre
  cliente_id int unsigned,
  cuota_anual decimal(10,2) not null,
  descuento decimal(10,2) not null,
  cod_socio varchar(250) not null,
  primary key (_id)
);

-- Creamos las relaciones FK
alter table clientes_premium
  add constraint fk_clientes_premium foreign key (cliente_id) references clientes(_id) on delete set null on update cascade;

-- Creamos la tabla para los artículos
create table if not exists articulos(
  _id int unsigned not null auto_increment,
  cod_articulo varchar(255) not null,
  descripcion varchar(255) not null,
  pvp decimal(10, 2) not null,
  gastos_envio decimal(10, 2) not null,
  tiempo_preparacion int unsigned not null,
  primary key (_id)
);

-- Creamos la tabla para los pedidos
create table if not exists pedidos(
  _id int unsigned not null auto_increment,
  numero_pedido int unsigned not null,
  -- Creamos la relación con el cliente
  cliente_id int unsigned,
  -- Creamos la relación con el artículo
  articulo_id int unsigned,
  unidades int unsigned not null,
  fecha_pedido date not null,
  es_enviado boolean not null,
  primary key (_id)
);

-- Creamos las referencias FK
alter table pedidos
	add constraint fk_clientes foreign key (cliente_id) references clientes(_id) on delete set null on update cascade,
	add constraint fk_articulos foreign key (articulo_id) references articulos(_id) on delete set null on update cascade;

-- FUNCIONES CRUD

-- CREATE

-- Creamos una función para añadir una dirección y devolver el id de la dirección
delimiter //
create function add_direccion(
	direccion varchar(255),
	ciudad varchar(255),
	provincia varchar(255),
	codigo_postal varchar(255),
	pais varchar(255)
)
	returns int unsigned
    modifies sql data
	begin
	-- Creamos la dirección
	insert into direcciones (direccion, ciudad, provincia, codigo_postal, pais)
	values (direccion, ciudad, provincia, codigo_postal, pais);
	-- Devolvemos el id de la dirección
	return last_insert_id();
	end;
//
delimiter ;

-- Creamos una función para añadir un cliente, ya sea estandard o premium, y devolver el id del cliente
-- descuento y cuota_anual son nulos si el cliente es estandard
-- cod_socio es nulo si el cliente es estandard
delimiter //
create function add_cliente(
  nombre varchar(255),
  direccion_id int unsigned,
  nif varchar(255),
  email varchar(255),
  tipo_cliente varchar(255),
  cuota_anual decimal(10,2),
  descuento decimal(10,2),
  cod_socio varchar(250)
) 
  returns int unsigned
  modifies sql data
  begin
	-- Creamos el cliente
	insert into clientes (nombre, direccion_id, nif, email) values (nombre, direccion_id, nif, email);
	-- Obtenemos el id del cliente
	set @cliente_id = last_insert_id();
	-- Creamos el cliente estandard
	if tipo_cliente = 'estandard' then
		insert into clientes_estandard (cliente_id) values (@cliente_id);
	-- Creamos el cliente premium
	else
		insert into clientes_premium (cliente_id, cuota_anual, descuento, cod_socio) values (@cliente_id, cuota_anual, descuento, cod_socio);
	end if;
	-- Devolvemos el id del cliente
	return @cliente_id;
  end;
//
delimiter ;

-- Creamos una función para añadir un  artículo y devolver el id del artículo
delimiter //
create function add_articulo(
  cod_articulo varchar(255),
  descripcion varchar(255),
  pvp decimal(10, 2),
  gastos_envio decimal(10, 2),
  tiempo_preparacion int unsigned
)
  returns int unsigned
  modifies sql data
  begin
	-- Creamos el artículo
	insert into articulos (cod_articulo, descripcion, pvp, gastos_envio, tiempo_preparacion)
	values (cod_articulo, descripcion, pvp, gastos_envio, tiempo_preparacion);
	-- Devolvemos el id del artículo
	return last_insert_id();
  end;
//
delimiter ;

-- Creamos una función para añadir un pedido y devolver el id del pedido
-- es_enviado es false por defecto
delimiter //
create function add_pedido(
  cliente_id int unsigned,
  articulo_id int unsigned,
  unidades int unsigned,
  fecha_pedido date
)
  returns int unsigned
  modifies sql data
  begin
	-- Creamos el pedido
	insert into pedidos (cliente_id, articulo_id, unidades, fecha_pedido, es_enviado)
	values (cliente_id, articulo_id, unidades, fecha_pedido, false);
	-- Devolvemos el id del pedido
	return last_insert_id();
  end;
//
delimiter ;

-- READ
-- Creamos un procedimiento para obtener una lista con todos los clientes
delimiter //
create procedure get_clientes()
reads sql data
	begin
	select * from clientes
	  order by _id;
	end;
//
delimiter ;

-- Creamos un procedimiento para obtener una lista con todos los clientes estandard
delimiter //
create procedure get_clientes_estandard()
reads sql data
	begin
	select * from clientes_estandard
	  order by _id;
	end;
//
delimiter ;

-- Creamos un procedimiento para obtener una lista con todos los clientes premium
delimiter //
create procedure get_clientes_premium()
reads sql data
	begin
	select * from clientes_premium
	  order by _id;
	end;
//
delimiter ;

-- Creamos un procedimiento para obtener un cliente por su id
delimiter //
create procedure get_clilente_by_id(id int unsigned)
reads sql data
  begin
	select * from clientes where _id = id
	  order by _id limit 1;
  end;
//
delimiter ;

-- Creamos un procedimiento para obtenr una lista de todos los artículos
delimiter //
create procedure get_articulos()
reads sql data
  begin
	select * from articulos
	  order by _id;
  end;
//
delimiter ;

-- Creamos un procedimiento para obtener un artículo por su id
delimiter //
create procedure get_articulo_by_id(id int unsigned)
reads sql data
  begin
	select * from articulos where _id = id
	  order by _id limit 1;
  end;
//
delimiter ;

-- Creamos un procedimiento para obtener una lista de todos los pedidos
delimiter //
create procedure get_pedidos()
reads sql data
  begin
	select * from pedidos
	  order by _id;
  end;
//
delimiter ;

-- Creamos un procedimiento para obtener un pedido por su id
delimiter //
create procedure get_pedido_by_id(id int unsigned)
reads sql data
	begin
	select * from pedidos where _id = id
	  order by _id limit 1;
	end;
//
delimiter ;

-- UPDATE
-- Creamos un procedimiento para actualizar un cliente
-- Si el cliente es premium, se actualiza la cuota anual y el descuento
-- Si el cliente es estandard, cuato_anual y descuento son nulos
-- Devolvemos el id del cliente
delimiter //
create function update_cliente(
  id int unsigned,
  nombre varchar(255),
  direccion_id int unsigned,
  direccion varchar(255),
  ciudad varchar(255),
  provincia varchar(255),
  codigo_postal varchar(255),
  pais varchar(255),
  nif varchar(255),
  email varchar(255),
  tipo_cliente varchar(255),
  cuota_anual decimal(10,2),
  descuento decimal(10,2)
)
returns int
modifies sql data
  begin
	-- Comprobamos si el cliente existe
	if exists (select * from clientes where _id = id) then
		-- Actualizamos el cliente
		update clientes set nombre = nombre, direccion_id = direccion_id, nif = nif, email = email where _id = id;
		-- Actualizamos la dirección
		update direcciones set direccion = direccion, ciudad = ciudad, provincia = provincia, 
		codigo_postal = codigo_postal, pais = pais where _id = direccion_id;
		-- Si el cliente es estandard
		if tipo_cliente = 'estandard' then
		  -- Actualizamos el cliente estandard, realmente no hace falta porque no tiene campos
		  -- pero así queda más claro
		  update clientes_estandard set cliente_id = id where cliente_id = id;
		end if;
		if tipo_cliente = 'premium' then
		  -- Actualizamos el cliente premium
		  update clientes_premium set cliente_id = id, cuota_anual = cuota_anual, descuento = descuento where cliente_id = id;
		end if;
		return id;
	else
	  return -1;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para actualizar un artículo
-- devolvemos el id del artículo, cod_articulo no es editable
delimiter //
create function update_articulo(
  id int unsigned,
  descripcion varchar(255),
  pvp decimal(10, 2),
  gastos_envio decimal(10, 2),
  tiempo_preparacion int unsigned
)
returns int
modifies sql data
  begin
	-- Comprobamos si el artículo existe
	if exists (select * from articulos where _id = id) then
	  -- Actualizamos el artículo
	  update articulos set descripcion = descripcion, pvp = pvp, gastos_envio = gastos_envio,
	  tiempo_preparacion = tiempo_preparacion where _id = id;
	  -- Devolvemos el id del artículo
	  return id;
	else
	  return -1;
	end if;
  end;
//
delimiter ;


-- Creamos un procedimiento para actualizar el estado de un pedido, es decir, el campo es_enviado
-- Devolvemos el id del pedido
delimiter //
create function update_pedido(id int unsigned)
  returns int
  modifies sql data
  begin
	-- Comprobamos si el pedido existe
	if exists (select * from pedidos where _id = id) then
	  -- Actualizamos el pedido
	  update pedidos set es_enviado = true where _id = id;
	  -- Devolvemos el id del pedido
	  return id;
	else
	  return -1;
	end if;
  end;
//
delimiter ;

-- DELETE
-- Creamos un procedimiento para eliminar un cliente
-- Devolvemos el id del cliente
delimiter //
create function delete_cliente_by_id(id int unsigned)
  returns int
  modifies sql data
  begin
	-- Comprobamos si el cliente existe
	if exists (select * from clientes where _id = id) then
	  -- Eliminamos el cliente
	  delete from clientes where _id = id;
	  -- Devolvemos el id del cliente
	  return id;
	else
	  return -1;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para eliminar todos los clientes, devolvemos el número de clientes eliminados
delimiter //
create procedure delete_clientes(out total_deleted int unsigned)
  modifies sql data
  begin
	-- Creamos una variable para guardar el número de clientes eliminados
	declare num_clientes int unsigned;
	-- Contamos el número de clientes
	set num_clientes = (select count(*) from clientes);

	-- Compobamos si la tabla contiene clientes
	if num_clientes > 0 then
	  -- Eliminamos todos los clientes de la tabla clientes
	  truncate table clientes;
	  -- Eliminamos todos los clientes de la tabla clientes_estandard
	  truncate table clientes_estandard;
	  -- Eliminamos todos los clientes de la tabla clientes_premium
	  truncate table clientes_premium;
	  -- Eliminamos todos los clientes de la tabla direcciones
	  truncate table direcciones;

	  set total_deleted =  num_clientes;
	else
	  set total_deleted =  0;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para eliminar un artículo
-- devolvemos el id del artículo
delimiter //
create function delete_articulo_by_id(id int unsigned)
  returns int
  modifies sql data
  begin
	-- Comprobamos si el artículo existe
	if exists (select * from articulos where _id = id) then
	  -- Eliminamos el artículo
	  delete from articulos where _id = id;
	  -- Devolvemos el id del artículo
	  return id;
	else
	  return -1;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para eliminar todos los artículos, devolvemos el número de artículos eliminados
delimiter //
create procedure delete_articulos(out total_deleted int unsigned)
    modifies sql data
	begin
	-- Creamos una variable para guardar el número de artículos eliminados
	declare num_articulos int unsigned;
	-- Contamos el número de artículos
	set num_articulos = (select count(*) from articulos);

	-- Compobamos si la tabla contiene artículos
	if num_articulos > 0 then
	  -- Eliminamos todos los artículos de la tabla articulos
	  truncate table articulos;

	  set  total_deleted = num_articulos;
	else
	  set total_deleted = 0;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para eliminar un pedido
-- devolvemos el id del pedido
delimiter //
create function delete_pedido_by_id(id int unsigned)
  returns int
  modifies sql data
  begin
	-- Comprobamos si el pedido existe
	if exists (select * from pedidos where _id = id) then
	  -- Eliminamos el pedido
	  delete from pedidos where _id = id;
	  -- Devolvemos el id del pedido
	  return id;
	else
	  return -1;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para eliminar todos los pedidos, devolvemos el número de pedidos eliminados
delimiter //
create procedure delete_pedidos(out total_deleted int unsigned)
	modifies sql data
	begin
	-- Creamos una variable para guardar el número de pedidos eliminados
	declare num_pedidos int unsigned;
	-- Contamos el número de pedidos
	set num_pedidos = (select count(*) from pedidos);

	-- Compobamos si la tabla contiene pedidos
	if num_pedidos > 0 then
	  -- Eliminamos todos los pedidos de la tabla pedidos
	  truncate table pedidos;

	  set total_deleted = num_pedidos;
	else
	  set total_deleted = 0;
	end if;
  end;
//
delimiter ;

-- CREACION DE ROLES
-- Creamos un rol para el usuario administrador de la empresa
create role if not exists os_admin_role;

-- Damos permisos al rol admin_role para usar todas las funciones y procedimientos
grant execute on onlinestore_db.* to os_admin_role;

-- Creamos un usuario os_admin que, para nuestro ejemplo, puede conectarse desde cualquier host
-- y le adjudicamos el rol os_admin_role. Le damos la clave por defecto de la BD.
create user if not exists 'os_admin'@'%' identified by 'ciricefp';
grant os_admin_role to os_admin;