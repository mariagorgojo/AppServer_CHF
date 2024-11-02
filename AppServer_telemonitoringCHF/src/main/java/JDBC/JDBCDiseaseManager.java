/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

/**
 *
 * @author carmengarciaprieto
 */
public class JDBCDiseaseManager implements DiseaseManager{
    
    private Connection c;

	public JDBCDiseaseManager(Connection c) {
		this.c = c;

	}
        @Override
	public void insertDisease(Disease disease) {

		try {
			Statement s = c.createStatement();
			String sql = "INSERT INTO Disease (id, disease) VALUES ('" + disease.getId() + "', '"
					+ disease.getDisease()+"')";
			s.executeUpdate(sql);
			s.close();
		} catch (SQLException e) {
			System.out.println("Database exception.");
			e.printStackTrace();
		}

	}
        @Override
        public void assignDiseaseToEpisode(int disease_id, int episode_id) {
                try {
                        String sql = "INSERT INTO Episode_Disease (disease_id, episode_id) VALUES (?,?)";
                        PreparedStatement p = c.prepareStatement(sql);
                        p.setInt(1, disease_id);
                        p.setInt(2, episode_id);
                        p.executeUpdate();
                        p.close();
                } catch (SQLException e) {
                        System.out.println("Database error.");
                        e.printStackTrace();
                }
        }
	@Override
	public List<Disease> getDiseasesByEpisode(int episode_id){
		List<Disease> list = new ArrayList<Disease>();
		try {
			String sql = "SELECT disease_id FROM Episode_Disease WHERE episode_id LIKE ?";
			PreparedStatement p = c.prepareStatement(sql);
			p.setInt(1, episode_id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {				
				Disease d = new Disease(rs.getInt("id"), rs.getString("disease");
				list.add(d);
			}
		} catch (SQLException e) {
			System.out.println("database error");
			e.printStackTrace();
		}
		return list;
	}
}
