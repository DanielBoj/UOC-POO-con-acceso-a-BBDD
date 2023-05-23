module CiriceFP.OnlineStore {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.web;
    requires spring.context;
    requires org.jetbrains.annotations;
    requires org.hibernate.orm.core;
    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires jakarta.persistence;
    requires java.dotenv;
    requires commons.dbcp2;

    opens ciricefp.controlador to javafx.fxml;
    exports ciricefp.controlador;
    exports ciricefp.modelo;
    opens ciricefp.modelo to org.hibernate.orm.core;
    exports ciricefp.modelo.utils;
    opens ciricefp.modelo.utils to org.hibernate.orm.core;
    exports ciricefp.vista;
}