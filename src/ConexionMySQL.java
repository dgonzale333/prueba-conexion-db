import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.Scanner;

public class ConexionMySQL {

    public static void main(String[] args) {
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRes = null;
        try {
            myConn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/project",
                    "root",
                    ""
            );
            System.out.println("Genial, nos conectamos");

            mostrarMenu(myConn);

            // realizamos una consulta a la base de datos
            myStmt = myConn.createStatement();
            myRes = myStmt.executeQuery("SELECT * FROM empleados");

            // iteramos los resultados para imprimir en consola.
            while (myRes.next()){
                System.out.println(myRes.getString("nombre"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Algo salio mal :(");
        }
    }

    // Menú
public static void mostrarMenu (Connection conexion) throws SQLException {
    Scanner scanner = new Scanner(System.in);
    int opcion = 0;

    do {
        System.out.println("\n-----Menu Empleados-----");
        System.out.println("1.Insertar Empleado");
        System.out.println("2.Consultar Empleado");
        System.out.println("3.Actualizar Empleado");
        System.out.println("4.Eliminar Empleado");
        System.out.println("5.Salir");
        System.out.println("Selecciona una opción");
        opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion){
            case 1:
                System.out.println("\n-----Insertar Empleado------");
                System.out.println("Nombre: ");
                String nombre = scanner.nextLine();
                System.out.println("Apellido: ");
                String apellido = scanner.nextLine();
                System.out.println ("Cargo: ");
                String cargo = scanner.nextLine();
                System.out.println("email: ");
                String email = scanner.nextLine();
                System.out.println("Salario: ");
                Double salario = scanner.nextDouble();
                scanner.nextLine();

                try {
                    insertarEmpleado(conexion, nombre, apellido, cargo, email, salario);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;


            case 2:
                System.out.println("\n------Consultar Empleado-----");
                consultarEmpleados(conexion);

                break;

            case 3:


        }
    } while (opcion !=5 );

    scanner.close();
}


    // Método para insertar un empleado
    private static void insertarEmpleado(Connection conexion, String nombre,String apellido, String cargo, String email, double salario)
            throws SQLException {
        String sql = "INSERT INTO empleados (nombre,apellido,cargo,email, salario) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, cargo);
            pstmt.setString(4, email);
            pstmt.setDouble(5, salario);
            pstmt.executeUpdate();
            System.out.println("Empleado insertado correctamente!");
        }
    }

    // Método para consultar empleados
    private static void consultarEmpleados(Connection conexion) throws SQLException {
        String sql = "SELECT * FROM empleados";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Nombre: %s, Cargo: %s, Salario: %.2f%n",
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("cargo"),
                        rs.getDouble("salario"));
            }
        }
    }

    // Método para actualizar un empleado
    private static void actualizarEmpleado(Connection conexion, int id, String nombre, String cargo, double salario)
            throws SQLException {
        String sql = "UPDATE empleados SET nombre = ?, cargo = ?, salario = ? WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, cargo);
            pstmt.setDouble(3, salario);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Empleado actualizado correctamente!");
        }
    }

    // Método para eliminar un empleado
    private static void eliminarEmpleado(Connection conexion, int id) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Empleado eliminado correctamente!");
        }
    }
}

