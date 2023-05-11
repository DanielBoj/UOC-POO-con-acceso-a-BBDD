package ciricefp.modelo.repositorio;

import ciricefp.modelo.Articulo;
import ciricefp.modelo.Cliente;
import ciricefp.modelo.Direccion;
import ciricefp.modelo.listas.Listas;
import ciricefp.modelo.utils.Conexion;
import ciricefp.modelo.utils.ConexionJpa;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;

import java.sql.*;
import java.text.MessageFormat;

/**
 * Esta clase implementa la interfaz Repositorio para nuestra entidad Direccion.
 * Esta clase será la encargada de gestionar los datos de la entidad Direccion.
 * Debe implementar todos los métodos de la interfaz Repositorio.
 *
 * @author Cirice
 * @version 1.0
 * @since 04-2023
 */
public class DireccionRepositorioImpl implements Repositorio<Direccion> {

    // Producto 4 -> Refactorizamos la clase para usar Entity Manager.
    // Creamos el atributo para nuestro Entity Manager.
    private EntityManager em;

    // Seteamos nuestro Entity Manager a través del constructor.
    public DireccionRepositorioImpl(EntityManager em) {
        this.em = em;
    }

    /* Producto 4 ≥ Refactoriazamos la clase para trabajar con Hibernate */

    /* Simplificamos al máximo la implementación de nuestro contrato con la interfaz ya que trasladaremos
     * toda la lógica a un servicio, para cumplir con las buenas prácticas. */

    @Override
    public Listas<Direccion> findAll() {

        // Creamos la lista de Direcciones que recibiremos de la BD.
        Listas<Direccion> direcciones = new Listas<>();

        // Creamos la query usando el lenguaje de consultas de JPA.
        em.createQuery("Select d from Direccion d", Direccion.class)
                // Obtenemos el resultado de la consulta y lo convertimos en un stream.
                .getResultStream().forEach(
                        // Pasamos el resultado a la lista de Direcciones.
                        direcciones::add
                );

        // Retornamos la lista de Articulos.
        return direcciones;
    }

    @Override
    public Direccion findById(Long id) {
        // Creamos la consulta con los métodos de Hibernate JPA.
        return em.find(Direccion.class, id);
    }

    // Direcciones no tiene ningún identificador único además de la llave, así que no será posible
    // implementar este método.
    @Override
    public Direccion findOne(String key) { return null; }

    @Override
    public void save(Direccion direccion) {
        // Comenzamos por discriminar si se trata de un Create o un Update.
        // Para ello, comprobaremos si el artículo tiene un id asignado que funcionará como un flag.
        if (direccion.getId() != null && direccion.getId() > 0) {
            // Si el id es mayor que 0, significa que ya existe en la BD.
            // Por lo tanto, ejecutaremos un Update.
            em.merge(direccion);
        } else {
            // Si el id es menor o igual a 0, significa que no existe en la BD.
            // Por lo tanto, ejecutaremos un Create.
            em.persist(direccion);
        }
    }

    @Override
    public void delete(Long id) {
        // Creamos la consulta usando métodos de Hibernate JPA.
        // Usaremos el método remove() para eliminar el artículo que recibe como argumento el artículo que queremos eliminar.
        // Tenemos que buscar el artículo por su Id, por lo que usaremos el método find() de la clase EntityManager.
        // Una vez localizado, eliminaremos el elemento.
        em.remove(em.find(Direccion.class, id));

        // TODO -> Pasar lógica a Servicio

        /*Repositorio<Pedido> pedidoRepo = new PedidoRepositorioImpl();
        if (pedidoRepo.findAll().getLista().stream()
            .anyMatch(ped -> ped.getArticulo().getId().equals((id)))) {
            System.out.println("No es posible eliminar el artículo porque está en algún pedido.");
            return false;
        }*/
    }

    @Override
    public int count() {
        // Creamos la consulta usando lenguaje HQL/JPQL. Si no hay ningún artículo nos devolverá 0.
        return em.createQuery("select count(d) from Direccion d", Long.class)
                // retornamos un único valor.
                .getSingleResult()
                // convertimos el resultado a int.
                .intValue();
    }

    @Override
    public Direccion getLast() {
        // Creamos la consulta usando lenguaje HQL/JPQL.
        // Obtenemos el último artículo de la BD recibiendo el primer resultado de la consulta
        // ordenada de forma descendente por el id.
        return em.createQuery("select d from Direccin d order by d._id desc", Direccion.class)
                // retornamos un único valor.
                .setMaxResults(1)
                // obtenemos el resultado.
                .getSingleResult();
    }

    @Override
    public boolean resetId() {
        // Creamos la consulta usando el método de Hibernate JPA para llamar a un procedimiento almacenado.
        // Si el procedimiento se ejecuta correctamente, nos devolverá true.
        return em.createStoredProcedureQuery("reset_id_direcciones")
                .execute();
    }

    public boolean deleteAll() {
        // Ejecutamos la consulta, usaremos el método createQuery de EntityManager y
        // executeUpdate(). Tenemos que asegurarnos de no romper la integridad referencial,
        // usamos un Truncate porque tiene mayor eficiencia en tiempo de ejecución.
        int res = em.createNativeQuery("truncate table direcciones")
                // Ejecutamos la consulta.
                .executeUpdate();

        return res > 0;
    }

    /* Producto 4 ≥ Como estamos usando Hibernate ya no es necesario mapear los objetos. */
    // Creamos un método para mapear los ResultSet. Lo vamos a usar únicamente dentro de la clase.
    // Recibe el ResultSet como parámetro.
    /*private static Direccion getDireccion(ResultSet res) throws SQLException {

        // Creamos la dirección y obtenemos los datos del ResultSet.
        Direccion direccion = new Direccion();

        direccion.setId(res.getLong("_id"));
        direccion.setDireccion(res.getString("direccion"));
        direccion.setCiudad(res.getString("ciudad"));
        direccion.setProvincia(res.getString("provincia"));
        direccion.setCodigoPostal(res.getString("codigo_postal"));
        direccion.setPais(res.getString("pais"));

        return direccion;
    }

    // Mapeamos un statement a partir de una dirección.
    // No es una función pura, pero modifica únicamente una variable que usamos en un método.
    private static void getStatement(Direccion direccion, CallableStatement stmt) throws SQLException {
        // Asignamos los parámetros a la consulta desde el artículo que recibimos por parámetro.
        // La elección del parámetro se hace por su posición.
        stmt.registerOutParameter(1, Types.BIGINT);
        stmt.setString(2, direccion.getDireccion());
        stmt.setString(3, direccion.getCiudad());
        stmt.setString(4, direccion.getProvincia());
        stmt.setString(5, direccion.getCodigoPostal());
        stmt.setString(6, direccion.getPais());
    }*/
}
