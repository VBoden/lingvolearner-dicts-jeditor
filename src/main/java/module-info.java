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
	
	opens ua.vboden;
	opens ua.vboden.controllers;
	opens ua.vboden.dto;
}