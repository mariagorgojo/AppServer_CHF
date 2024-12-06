package pojos;

/**
 *
 * @author carmengarciaprieto
 */
public class Administrator {

    private int id;
    private String dni;
    private String password;

    // Constructor vacío
    public Administrator() {
    }

    // Constructor con parámetros
    public Administrator(String dni, String password) {
        this.dni = dni;
        this.password = password;
    }

    public Administrator(Integer id) {
        this.id = id;
    }

    // Getter para id
    public int getId() {
        return id;
    }

    // Setter para id
    public void setId(int id) {
        this.id = id;
    }

    // Getter para dni
    public String getDni() {
        return dni;
    }

    // Setter para dni
    public void setDni(String dni) {
        this.dni = dni;
    }

    // Getter para password
    public String getPassword() {
        return password;
    }

    // Setter para password
    public void setPassword(String password) {
        this.password = password;
    }

    // Método toString
    @Override
    public String toString() {
        return "Administrator{"
                + "id=" + id
                + ", dni='" + dni + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
