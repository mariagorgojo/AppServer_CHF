/*package pojos;


import java.util.Objects;



public class User {

      private Integer id; 
      private String dni;
      private String password;
      private Role role;
     
      
    public User(String dni, String password) {
		super();
		this.dni = dni;
		this.password = password;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDni() {
		return dni;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + dni + ", password=" + password + ", role="
				+ role + "]";
	} 
	
      public enum Role{
          DOCTOR, PATIENT 
      }
}
*/