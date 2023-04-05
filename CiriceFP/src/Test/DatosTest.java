package Test;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.Datos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatosTest {
    Datos datos = new Datos();


    @Test
    void createArticulo() {
        Articulo articulo = new Articulo("001","Vestido Rojo", 20, 5, 120);
        datos.createArticulo(articulo);

        assertEquals("001",datos.listArticulos().get(0).getCodArticulo());
        assertEquals("Vestido Rojo",datos.listArticulos().get(0).getDescripcion());
        assertEquals(20,datos.listArticulos().get(0).getPvp());
        assertEquals(5,datos.listArticulos().get(0).getGastosEnvio());
        assertEquals(120,datos.listArticulos().get(0).getTiempoPreparacion());
    }

    @Test
    void searchArticulo() {
        Articulo articulo = new Articulo("001","Vestido Rojo", 20, 5, 120);
        Articulo articulo2 = new Articulo("002","Vestido Azul", 22, 4, 150);
        datos.createArticulo(articulo);
        datos.createArticulo(articulo2);

        Articulo buscado = datos.searchArticulo("001");

        assertEquals("001", buscado.getCodArticulo());
        assertEquals("Vestido Rojo", buscado.getDescripcion());
        assertEquals(20, buscado.getPvp());
        assertEquals(5, buscado.getGastosEnvio());
        assertEquals(120, buscado.getTiempoPreparacion());

        Articulo buscado2 = datos.searchArticulo("002");

        assertEquals("002", buscado.getCodArticulo());
        assertEquals("Vestido Azul", buscado.getDescripcion());
        assertEquals(22, buscado.getPvp());
        assertEquals(4, buscado.getGastosEnvio());
        assertEquals(150, buscado.getTiempoPreparacion());
    }
}