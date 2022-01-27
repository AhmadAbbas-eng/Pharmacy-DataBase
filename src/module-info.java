module Database {
	requires transitive javafx.controls;
	requires transitive javafx.fxml;
	requires transitive java.sql;
	requires transitive mysql.connector.java;
	requires java.desktop;
	requires java.management;
	requires javafx.graphics;
	exports application;
	exports Relations;

	opens Relations to javafx.graphics, javafx.fxml,javafx.base;
	opens application to javafx.graphics, javafx.fxml;
}

