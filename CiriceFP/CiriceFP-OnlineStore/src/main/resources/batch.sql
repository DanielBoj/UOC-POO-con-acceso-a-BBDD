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
                ('Calle 1', 'Madrid', 'Madrid', '28001', 'España'),
                ('Calle 2', 'Madrid', 'Madrid', '28001', 'España'),
                ('Calle 3', 'Madrid', 'Madrid', '28001', 'España')
                ;

            -- Insertamos 3 clientes de prueba
            insert into clientes
                (nombre, direccion_id, nif, email)
            values
                ('Cirice Helada', 1, '12345678A', 'cirice@algo.com'),
                ('Socrates Helada', 2, '12345678B', 'socartes@algo.com'),
                ('Platon Helada', 3, '12345678C', 'platon@algo.com');

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

