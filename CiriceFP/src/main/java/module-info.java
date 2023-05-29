module CiriceFP.OnlineStore {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.context;
    requires org.jetbrains.annotations;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.dotenv;
    requires commons.dbcp2;
    requires commons.validator;

    exports ciricefp.controlador;
    opens ciricefp.controlador to javafx.fxml;
    exports ciricefp.modelo;
    opens ciricefp.modelo to org.hibernate.orm.core;
    exports ciricefp.modelo.utils;
    opens ciricefp.modelo.utils to org.hibernate.orm.core;
    exports ciricefp.vista;
    exports ciricefp.vista.controladores;
}