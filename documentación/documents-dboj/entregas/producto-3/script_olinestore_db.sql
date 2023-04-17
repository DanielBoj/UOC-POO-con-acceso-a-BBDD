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
  direccion varchar(255),
  ciudad varchar(255),
  provincia varchar(255),
  codigo_postal varchar(255),
  pais varchar(255),
  primary key (_id)
);

-- Creamos la tabla padre de clientes
create table if not exists clientes(
  _id int unsigned not null auto_increment,
  nombre varchar(255),
  -- Creamos la relación con la tabla de direcciones
  direccion_id int unsigned,
  nif varchar(255),
  email varchar(255),
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
  cuota_anual decimal(10,2),
  descuento decimal(10,2),
  cod_socio varchar(250),
  primary key (_id)
);

-- Creamos las relaciones FK
alter table clientes_premium
  add constraint fk_clientes_premium foreign key (cliente_id) references clientes(_id) on delete set null on update cascade;

-- Creamos la tabla para los artículos
create table if not exists articulos(
  _id int unsigned not null auto_increment,
  cod_articulo varchar(255),
  descripcion varchar(255),
  pvp decimal(10, 2),
  gastos_envio decimal(10, 2),
  tiempo_preparacion int unsigned,
  primary key (_id)
);

-- Creamos la tabla para los pedidos
create table if not exists pedidos(
  _id int unsigned not null auto_increment,
  numero_pedido int unsigned,
  -- Creamos la relación con el cliente
  cliente_id int unsigned,
  -- Creamos la relación con el artículo
  articulo_id int unsigned,
  unidades int unsigned,
  fecha_pedido date,
  es_enviado boolean,
  primary key (_id)
);

-- Creamos las referencias FK
alter table pedidos
	add constraint fk_clientes foreign key (cliente_id) references clientes(_id) on delete set null on update cascade,
	add constraint fk_articulos foreign key (articulo_id) references articulos(_id) on delete set null on update cascade;

-- FUNCIONES CRUD

-- CREATE

-- Creamos una función para añadir una dirección
drop procedure if exists add_direccion;
delimiter //
create procedure add_direccion(
    out idout int,
	in direccion varchar(255),
	in ciudad varchar(255),
	in provincia varchar(255),
	in codigo_postal varchar(255),
	in pais varchar(255)
)
    modifies sql data
	begin
	-- Creamos la dirección
	insert into direcciones (direccion, ciudad, provincia, codigo_postal, pais)
	values (direccion, ciudad, provincia, codigo_postal, pais);
    set idout = last_insert_id();
	end;
//
delimiter ;

-- Creamos las funciones para añadir un cliente, siguiendo la lógica de negocio
-- el tipo de cliente puede ser estandard o premium.
-- La toma de decisiones se realiza en la APP Java, así que en la base de datos
-- no se comprueba nada y se crean 3 funciones separadas para cada entidad y 
-- subentidades.
-- descuento y cuota_anual son nulos si el cliente es estandard
-- cod_socio es nulo si el cliente es estandard
drop procedure if exists add_cliente;
delimiter //
create procedure add_cliente(
    out cliente_id int,
    in nombre varchar(255),
    in direccion_id int unsigned,
    in nif varchar(255),
    in email varchar(255)
)
  modifies sql data
  begin
	-- Creamos el cliente
	insert into clientes (nombre, direccion_id, nif, email) values (nombre, direccion_id, nif, email);
	-- Devolvemos el id del cliente
	set cliente_id = last_insert_id();
  end;
//
delimiter ;

-- Manejo de la table hija clientes_estandard
drop procedure if exists add_cliente_estandard;
delimiter //
create procedure add_cliente_estandard(out idout int, 
    in cliente_id int unsigned)
  modifies sql data
  begin
    -- Creamos el cliente estandard
    insert into clientes_estandard (cliente_id) values (cliente_id);
    -- Devolvemos el id del cliente estandard
    set idout = last_insert_id();
  end;
//
delimiter ;

-- Manejo de la table hija clientes_premium
drop procedure if exists add_cliente_premium;
delimiter //
create procedure add_cliente_premium(
    out idout int,
    in cliente_id int unsigned,
    in cuota_anual decimal(10,2),
    in descuento decimal(10,2),
    in cod_socio varchar(250)
)
  modifies sql data
  begin
    -- Creamos el cliente premium
    insert into clientes_premium (cliente_id, cuota_anual, descuento, cod_socio) values (cliente_id, cuota_anual, descuento, cod_socio);
    -- Devolvemos el id del cliente premium
    set idout = last_insert_id();
  end;
//
delimiter ;

-- Creamos una función para añadir un  artículo y devolver el id del artículo
drop procedure if exists add_articulo;
delimiter //
create procedure add_articulo(
    out idout int,
    in cod_articulo varchar(255),
    in descripcion varchar(255),
    in pvp decimal(10, 2),
    in gastos_envio decimal(10, 2),
    in tiempo_preparacion int unsigned
)
  modifies sql data
  begin
	-- Creamos el artículo
	insert into articulos (cod_articulo, descripcion, pvp, gastos_envio, tiempo_preparacion)
	values (cod_articulo, descripcion, pvp, gastos_envio, tiempo_preparacion);
	-- Devolvemos el id del artículo
	set idout = last_insert_id();
  end;
//
delimiter ;

-- Creamos una función para añadir un pedido y devolver el id del pedido
-- es_enviado es false por defecto, pero se manejad desde la APP Java.
drop procedure if exists add_pedido;
delimiter //
create procedure add_pedido(
    out idout int,
    in numero_pedido int unsigned,
    in cliente_id int unsigned,
    in articulo_id int unsigned,
    in unidades int unsigned,
    in fecha_pedido date,
    in es_enviado boolean
)
  modifies sql data
  begin
	-- Creamos el pedido
	insert into pedidos (numero_pedido, cliente_id, articulo_id, unidades, fecha_pedido, es_enviado)
	values (numero_pedido, cliente_id, articulo_id, unidades, fecha_pedido, es_enviado);
	-- Devolvemos el id del pedido
	set idout = last_insert_id();
  end;
//
delimiter ;

-- READ
-- Creamos un procedimiento para obtener una lista con todos los clientes
drop procedure if exists get_clientes;
delimiter //
create procedure get_clientes()
reads sql data
	begin
        select * from clientes c
            left join clientes_estandard ce on (ce.cliente_id = c._id)
            left join clientes_premium cp on (cp.cliente_id = c._id)
            join direcciones d on (d._id = c.direccion_id)
            order by c._id;
	end;
//
delimiter ;

-- Creamos un procedimiento para obtener una lista con todos los clientes estandard
drop procedure if exists get_clientes_estandard;
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
drop procedure if exists get_clientes_premium;
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
drop procedure if exists get_cliente_by_id;
delimiter //
create procedure get_cliente_by_id(id int unsigned)
reads sql data
  begin
    select * from clientes c
        left join clientes_estandard ce on (ce.cliente_id = c._id)
        left join clientes_premium cp on (cp.cliente_id = c._id)
        join direcciones d on (d._id = c.direccion_id)
        where c._id = id
        order by c._id limit 1;
  end;
//
delimiter ;

-- Creamos un procedimiento para obtener un cliente por su nif
drop procedure if exists get_cliente_by_nif;
delimiter //
create procedure get_cliente_by_nif(nif varchar(255))
reads sql data
    begin
      select * from clientes c
        left join clientes_estandard ce on (ce.cliente_id = c._id)
        left join clientes_premium cp on (cp.cliente_id = c._id)
        join direcciones d on (d._id = c.direccion_id)
        where c.nif = nif
        order by c._id limit 1;
    end;
//
delimiter ;

-- Creamos un procedimiento para obtener una lista de todos los artículos
drop procedure if exists get_articulos;
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
drop procedure if exists get_articulo_by_id;
delimiter //
create procedure get_articulo_by_id(id int unsigned)
reads sql data
  begin
	select * from articulos where _id = id
	  order by _id limit 1;
  end;
//
delimiter ;

-- Creamos un procedimiento para obtener un artículo por su código
drop procedure if exists get_articulo_by_cod;
delimiter //
create procedure get_articulo_by_cod(cod_articulo varchar(255))
reads sql data
    begin
      select * from articulos a where a.cod_articulo = cod_articulo
        order by _id limit 1;
    end;
//
delimiter ;

-- Creamos un procedimiento para obtener una lista de todos los pedidos
drop procedure if exists get_pedidos;
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
drop procedure if exists get_pedido_by_id;
delimiter //
create procedure get_pedido_by_id(id int unsigned)
reads sql data
	begin
	select * from pedidos where _id = id
	  order by _id limit 1;
	end;
//
delimiter ;

-- Creamos un procedimiento para obtener un pedido por su numero de pedido
drop procedure if exists get_pedido_by_numero_pedido;
delimiter //
create procedure get_pedido_by_numero_pedido(_numero_pedido int unsigned)
reads sql data
    begin
      select * from pedidos p where p.numero_pedido = _numero_pedido
        order by _id limit 1;
    end;
//
delimiter ;

-- Creamos un procedimiento para obtener una lista de todas las direcciones
drop procedure if exists get_direcciones;
delimiter //
create procedure get_direcciones()
reads sql data
  begin
    select * from direcciones
      order by _id;
  end;
//
delimiter ;

-- Creamos un procedimiento para obtener una dirección por su id
drop procedure if exists get_direccion_by_id;
delimiter //
create procedure get_direccion_by_id(id int unsigned)
reads sql data
    begin
        select * from direcciones where _id = id
            order by _id limit 1;
    end;
//
delimiter ;

-- UPDATE
-- Creamos las funciones para actualizar un cliente
-- Si el cliente es premium, se actualiza la cuota anual y el descuento
-- Si el cliente es estandard, cuato_anual y descuento son nulos
-- Devolvemos el id del cliente
-- Como en el caso de la creación, la lógica de negocio se realiza en la función
-- de la APP Java.
drop procedure if exists update_cliente;
delimiter //
create procedure update_cliente(
    out result int,
    in nombre varchar(255),
    in direccion_id int unsigned,
    in nif varchar(255),
    in email varchar(255),
    in id int unsigned
)
modifies sql data
  begin
	-- Comprobamos si el cliente existe
	if exists (select * from clientes where _id = id) then
		-- Actualizamos el cliente
		update clientes cl set cl.nombre = nombre, cl.direccion_id = direccion_id, cl.nif = nif, cl.email = email where _id = id;
		set result = id;
	else
	  set result = -1;
	end if;
  end;
//
delimiter ;

-- Si el cliente es estandard, no necesitamos hacer nada porque los ID no deben modificarse.

-- Manejamos los clientes premium. Código de cliente no se puede cambiar ya que
-- es un código único asignado por la APP.
drop procedure if exists update_cliente_premium;
delimiter //
create procedure update_cliente_premium(
    out result int,
    in cuota_anual decimal(10,2),
    in descuento decimal(10,2),
    in id int unsigned
)
modifies sql data
  begin
    -- Comprobamos si el cliente existe
    if exists (select * from clientes_premium where cliente_id = id) then
        -- Actualizamos el cliente
        update clientes_premium cp set cp.cuota_anual = cuota_anual, cp.descuento = descuento where cliente_id = id;
        set result = id;
    else
      set result = -1;
    end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para actualizar un artículo
-- devolvemos el id del artículo, cod_articulo no es editable
drop procedure if exists update_articulo;
delimiter //
create procedure update_articulo(
    out result int,
    in cod_articulo varchar(255),
    in descripcion varchar(255),
    in pvp decimal(10, 2),
    in gastos_envio decimal(10, 2),
    in tiempo_preparacion int unsigned,
    in id int unsigned
)
modifies sql data
  begin
	-- Comprobamos si el artículo existe
	if exists (select * from articulos where _id = id) then
	  -- Actualizamos el artículo
	  update articulos a set a.cod_articulo = cod_articulo, a.descripcion = descripcion, a.pvp = pvp, a.gastos_envio = gastos_envio,
	  a.tiempo_preparacion = tiempo_preparacion where _id = id;
	  -- Devolvemos el id del artículo
	  set result = id;
	else
	  set result = -1;
	end if;
  end;
//
delimiter ;

-- Creamos una función para actualizar un pedido	
drop procedure if exists update_pedido;
delimiter //
create procedure update_pedido(
    out result int,
    in numero_pedido int unsigned,
    in cliente_id int unsigned,
    in articulo_id int unsigned,
    in unidades int unsigned,
    in fecha_pedido date,
    in es_enviado boolean,
    in id int unsigned
)
modifies sql data
    begin
      -- Comprobamos si existe el pedido
      if exists (select * from pedidos where _id = id) then
        -- Actualizamos el pedido
        update pedidos p set p.numero_pedido = numero_pedido, p.cliente_id = cliente_id, p.articulo_id = articulo_id, p.unidades = unidades,
        p.fecha_pedido = fecha_pedido, p.es_enviado = es_enviado where _id = id;
        -- Devolvemos el id del pedido
        set result = id;
      else
        set result = -1;
      end if;
    end;
//
delimiter ;

-- Creamos un proceso para actualizar una dirección.
drop procedure if exists update_direccion;
delimiter //
create procedure update_direccion(
    out result int,
    in direccion varchar(255),
    in ciudad varchar(255),
    in provincia varchar(255),
    in codigo_postal varchar(255),
    in pais varchar(255),
    in id int unsigned
)
modifies sql data
    begin
        -- Comprobamos si la dirección existe
        if exists (select * from direcciones where _id = id) then
            -- Actualizamos la dirección
            update direcciones d set d.direccion = direccion, d.ciudad = ciudad, d.provincia = provincia, d.codigo_postal = codigo_postal,
            d.pais = pais where _id = id;
            -- Devolvemos el id de la dirección
            set result = id;
        else
            set result = -1;
        end if;
    end;
//
delimiter ;

-- DELETE
-- Creamos una función para eliminar un cliente
-- Devolvemos el id del cliente
-- Solo manejamos la eliminación del cliente ya que hemos generado 
-- un trigger que se encarga del resto.
-- La lógica para eliminar la dirección se maneja desde la aplicación Java.
drop procedure if exists delete_cliente;
delimiter //
create procedure delete_cliente(
    out result int,
    in id int unsigned
    )
  modifies sql data
  begin
	-- Comprobamos si el cliente existe
	if exists (select * from clientes where _id = id) then
	  -- Eliminamos el cliente
	  delete from clientes where _id = id;
	  -- Devolvemos el id del cliente
	  set result = id;
	else
	  set result = -1;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para eliminar todos los clientes, devolvemos el número de clientes eliminados
drop procedure if exists delete_clientes;
delimiter //
create procedure delete_clientes(out total_deleted int)
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
drop procedure if exists delete_articulo;
delimiter //
create procedure delete_articulo(
    out result int,
    in id int unsigned
    )
  modifies sql data
  begin
	-- Comprobamos si el artículo existe
	if exists (select * from articulos where _id = id) then
	  -- Eliminamos el artículo
	  delete from articulos where _id = id;
	  -- Devolvemos el id del artículo
	  set result = id;
	else
	  set result = -1;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para eliminar todos los artículos, devolvemos el número de artículos eliminados
drop procedure if exists delete_articulos;
delimiter //
create procedure delete_articulos(out total_deleted int)
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

-- Creamos una función para eliminar un pedido
-- devolvemos el id del pedido
drop procedure if exists delete_pedido;
delimiter //
create procedure delete_pedido(
    out result int,
    in id int unsigned
    )
  modifies sql data
  begin
	-- Comprobamos si el pedido existe
	if exists (select * from pedidos where _id = id) then
	  -- Eliminamos el pedido
	  delete from pedidos where _id = id;
	  -- Devolvemos el id del pedido
	  set result = id;
	else
	  set result = -1;
	end if;
  end;
//
delimiter ;

-- Creamos un procedimiento para eliminar todos los pedidos, devolvemos el número de pedidos eliminados
drop procedure if exists delete_pedidos;
delimiter //
create procedure delete_pedidos(out total_deleted int)
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

-- Creamos una función para eliminar una dirección.
-- Devolvemos el id de la dirección.
drop procedure if exists delete_direccion;
delimiter //
create procedure delete_direccion(
    out result int,
    in id int unsigned
    ) 
    modifies sql data
    begin
        -- Comprobamos si la dirección existe
        if exists (select * from direcciones where _id = id) then
            -- Eliminamos la direccion
            delete from direcciones where _id = id;
            -- Devolvemos el id de la dirección
            set result = id;
        else
            set result = -1;
        end if;
    end;
//
delimiter ;

-- Creamos un procedimiento para eliminar todas las direcciones, devolvemos el número de direcciones eliminadas
drop procedure if exists delete_direcciones;
delimiter //
create procedure delete_direcciones(out total_deleted int)
    modifies sql data
    begin
        -- Creamos una variable para guardar el número de direcciones eliminadas
        declare num_direcciones int unsigned;
        -- Contamos el número de direcciones
        set num_direcciones = (select count(*) from direcciones);

        -- Compobamos si la tabla contiene direcciones
        if num_direcciones > 0 then
            -- Eliminamos todas las direcciones de la tabla direcciones
            truncate table direcciones;

            set total_deleted = num_direcciones;
        else
            set total_deleted = 0;
        end if;
    end;
//
delimiter ;

-- Función para resetear los AUTO_INCREMENT en caso de limpiar una tabla.
drop procedure if exists reset_autoincrement;
delimiter //
create procedure reset_autoincrement()
    modifies sql data
    begin
        -- Ejecutamos los querys de reseteo de valores.
        alter table articulos AUTO_INCREMENT = 1;
        alter table clientes AUTO_INCREMENT = 1;
        alter table clientes_estandard AUTO_INCREMENT = 1;
        alter table clientes_premium AUTO_INCREMENT = 1;
        alter table pedidos AUTO_INCREMENT = 1;
        alter table direcciones AUTO_INCREMENT = 1;
    end;
//
delimiter ;

drop procedure if exists reset_id_articulos;
delimiter //
create procedure reset_id_articulos()
    modifies sql data
    begin
        -- Ejecutamos los querys de reseteo de valores.
        alter table articulos AUTO_INCREMENT = 1;
    end;
//
delimiter ;

drop procedure if exists reset_id_clientes;
delimiter //
create procedure reset_id_clientes()
    modifies sql data
    begin
        -- Ejecutamos los querys de reseteo de valores.
        alter table clientes AUTO_INCREMENT = 1;
        alter table clientes_estandard AUTO_INCREMENT = 1;
        alter table clientes_premium AUTO_INCREMENT = 1;
        alter table direcciones AUTO_INCREMENT = 1;
    end;
//
delimiter ;

drop procedure if exists reset_id_pedidos;
delimiter //
create procedure reset_id_pedidos()
    modifies sql data
    begin
        -- Ejecutamos los querys de reseteo de valores.
        alter table pedidos AUTO_INCREMENT = 1;
    end;
//
delimiter ;

drop procedure if exists reset_id_direcciones;
delimiter //
create procedure reset_id_direcciones()
    modifies sql data
    begin
        -- Ejecutamos los querys de reseteo de valores.
        alter table direcciones AUTO_INCREMENT = 1;
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

-- Por último, creamos dos procesos para realizar una carga de datos inicial
-- en la base de datos si el usuario lo desea.
-- Creamos los datos de testing
drop procedure if exists add_datos_test;
delimiter //
create procedure add_datos_test (out p_res int)
    modifies sql data
    begin
        declare exit handler for sqlexception
        begin
            rollback;
            set p_res = -1;
        end;
    start transaction;
        set p_res = 0;
        if (select count(_id) from clientes) = 0 then
            -- Insertamos una dirección de prueba
            insert into direcciones
                (direccion, ciudad, provincia, codigo_postal, pais)
            values
                ('Calle 1', 'Madrid', 'Madrid', '28001', 'España');

            -- Insertamos 3 clientes de prueba
            insert into clientes
                (nombre, direccion_id, nif, email)
            values
                ('Cirice Helada', 1, '12345678A', 'cirice@algo.com'),
                ('Socrates Helada', 1, '12345678B', 'socartes@algo.com'),
                ('Platon Helada', 1, '12345678C', 'platon@algo.com');

            -- Los 2 primeros clientes serán estandard
            insert into clientes_estandard
                (cliente_id)
            values
                (1),
                (2);

            -- El último cliente será premium
            insert into clientes_premium
                (cliente_id, cuota_anual, descuento, cod_socio)
            values
                (3, 30.0, 0.2, 'PREMIUM0000');
            set p_res = 1;
        end if;
        if (select count(_id) from articulos) = 0 then
            -- Insertamos 3 productos de prueba
            insert into articulos
                (cod_articulo, descripcion, pvp, gastos_envio, tiempo_preparacion)
            values
                ('A0001', 'Corbatero', 10.50, 10.0, 1),
                ('A0002', 'Camiseta', 3.50, 20.0, 2),
                ('A0003', 'Pantalones', 9.85, 30.0, 3);
            set p_res = 1;
        end if;
        if (select count(_id) from pedidos) = 0 then
        -- Insertamos 1 pedido de prueba
        insert into pedidos
            (numero_pedido, cliente_id, articulo_id, unidades, fecha_pedido, es_enviado)
        values
            (1, 1, 1, 5, '2023-04-01', 0);
            set p_res = 1;
        end if;
    commit;
    end;
//
delimiter ;

-- Creamos un procedimiento para comprobar si existen datos en la BD.
drop procedure if exists check_datos;
delimiter //
create procedure check_datos (out p_res int)
    begin
        declare exit handler for sqlexception
        begin
            rollback;
            set p_res = -1;
        end;
        start transaction;
            set p_res = 0;
            if (select count(_id) from clientes) > 0 then
                set p_res = 1;
            end if;
            if (select count(_id) from articulos) > 0 then
                set p_res = 1;
            end if;
            if (select count(_id) from pedidos) > 0 then
                set p_res = 1;
            end if;
        commit;
    end;
//
delimiter ;
