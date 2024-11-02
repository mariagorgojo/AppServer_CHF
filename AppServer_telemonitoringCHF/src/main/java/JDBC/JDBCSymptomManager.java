/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCSymptomManager implements SymptomManager{
    
    private Connection c;

	public JDBCSymptomManager(Connection c) {
		this.c = c;

	}
        @Override
	public void insertSymptom(Symptom symptom) {

		try {
			Statement s = c.createStatement();
			String sql = "INSERT INTO Symptom (id, symptom) VALUES ('" + symptom.getId() + "', '"
					+ symptom.getSymptom()+"')";
			s.executeUpdate(sql);
			s.close();
		} catch (SQLException e) {
			System.out.println("Database exception.");
			e.printStackTrace();
		}

	}
        @Override
        public void assignSymptomToEpisode(int symptom_id, int episode_id) {
                try {
                        String sql = "INSERT INTO Episode_Symptom (symptom_id, episode_id) VALUES (?,?)";
                        PreparedStatement p = c.prepareStatement(sql);
                        p.setInt(1, symptom_id);
                        p.setInt(2, episode_id);
                        p.executeUpdate();
                        p.close();
                } catch (SQLException e) {
                        System.out.println("Database error.");
                        e.printStackTrace();
                }
        }
        @Override
	public List<Symptom> getSymptomsByEpisode(int episode_id){
		List<Symptom> list = new ArrayList<Symptom>();
		try {
			String sql = "SELECT symptom_id FROM Episode_Symptom WHERE episode_id LIKE ?";
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, episode_id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {				
				Symptom s = new Symptom(rs.getInt("id"), rs.getString("symptom");
				list.add(s);
			}
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return list;
	}
}
