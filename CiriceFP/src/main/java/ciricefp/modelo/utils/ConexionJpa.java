package ciricefp.modelo.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

// Importamos las variables de entorno
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Esta clase implementa la lógica para realizar el acceso a la base de datos mediante JPA/Hibernate.
 * Aplica los patrones de diseño Singleton y Factory.
 *
 * @Author Cirice
 * @Version 1.0
 * @Since 2023-05
 */
public class ConexionJpa {
    // Para trabajar de forma programática, hemos de añadir aquí el nombre de la unidad de persistencia
    private static final String PERSISTENCE_UNIT_NAME = "onlinestoreJPA";

    // Creamos el atributo para instanciar los objetos de tipo EntityManagerFactory.
    private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

    // Atributo env para manejar las variables de entorno
    private static final Dotenv dotenv = Dotenv.load();

    /* Para mantener las variables de env secretas, podemos configurar Hibernate programáticamente
    * en lugar de mediante el archivo xml. */
    private static Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();

        // Añadimos el provider
        properties.put("jakarta.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");

        // Añadimos el nombre de la unidad de persistencia
        properties.put("jakarta.persistence.unitName", PERSISTENCE_UNIT_NAME);

        // Agregar la propiedad transaction-type
        properties.put("jakarta.persistence.transactionType", "RESOURCE_LOCAL");

        // Configuramos las propiedades de la conexión a la base de datos.
        properties.put("jakarta.persistence.jdbc.url", "jdbc:mysql://localhost:3306/onlinestore_db?useUnicode=true&amp;useJDBCCompliantTimezoneShift=true&amp;useLegacyDatetimeCode=false&amp;serverTimezone=Europe/Madrid");
        properties.put("jakarta.persistence.jdbc.user", dotenv.get("DB_LOCAL_USER"));
        properties.put("jakarta.persistence.jdbc.password", dotenv.get("DB_LOCAL_PASS"));
        properties.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.show_sql", "true");

        // Añadimos las clases
        properties.put("jakarta.persistence.mapping.resources", "ciricefp.modelo.Articulo, ciricefp.modelo.Cliente, ciricefp.modelo.ClienteEstandard, ciricefp.modelo.ClientePremium, ciricefp.modelo.Direccion, ciricefp.modelo.Pedido");

        return properties;
    }

    /* Métodos de Clase */
    // Generamos el método Factory para la conexión a la base de datos, además, aplicamos el patrón Singleton.
    private static EntityManagerFactory buildEntityManagerFactory() { return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, getProperties()); }

    /* Generamos el getter para obtener la conexión al objeto EntityManagerFactory, lo hacemos
    * por getter para respetar el patrón Singleton, solo tendremos una conexión por cliente. */
    public static EntityManager getEntityManagerFactory() { return entityManagerFactory.createEntityManager(); }

    // Cerramos la conexión a la base de datos.
    public static int close() {
        try {
            // Cerramos la instancia Singleton
            entityManagerFactory.close();
            return 0;
        } catch (Exception e) {
            System.out.println("Error al cerrar la conexión a la base de datos.");
            return 1;
        }
    }
}
