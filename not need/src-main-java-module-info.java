module editorfx {
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires spring.context;
	requires spring.boot;
	requires spring.core;
	requires spring.beans;
	requires spring.boot.autoconfigure;
	requires spring.data.jpa;
	requires spring.data.commons;
	requires spring.jdbc;
	requires jakarta.persistence;
	requires org.hibernate.orm.core;
	requires java.xml.bind;
    requires net.bytebuddy;
	
	opens ua.vboden;
	opens ua.vboden.controllers;
	opens ua.vboden.dto;
	opens ua.vboden.entities;
	opens ua.vboden.repositories;
	
}