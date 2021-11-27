package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn = null;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;	
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?,?,?,?,?)", 
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DBException("Unexpected error! No rows affected!");
			}
			
		}catch(SQLException e) {
			throw new DBException(e.getMessage()); 
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DBException(e.getMessage()); 
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deletebyId(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE id = ?");
			st.setInt(1, id);
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DBException(e.getMessage()); 
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"select seller. *, department.Name as depName "
				+"from seller inner join department "
				+"on seller.DepartmentID = department.Id "
				+"where seller.id = ? ");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Department dep = instanciateDepartment(rs);
				Seller obj = instanciateSeller(rs, dep);
				return obj;
			}
			return null;
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instanciateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instanciateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("departmentid"));
		dep.setName(rs.getString("depName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller. *, department.Name as depName "
					+"from seller inner join department "
					+"on seller.DepartmentID = department.Id "
					+"order by Name");
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			//controle para nao instaciar um departmento varias vezes do mesmo ID
			Map<Integer, Department> map = new HashMap<>();
			
			
			while (rs.next()) {
				// se o departamento ja existir o map pega ele
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				// faz a verificação se ja tem um departmento instanciado
				if(dep == null) {
					dep = instanciateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instanciateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller. *, department.Name as depName "
					+"from seller inner join department "
					+"on seller.DepartmentID = department.Id "
					+"where departmentid = ? "
					+"order by Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			//controle para nao instaciar um departmento varias vezes do mesmo ID
			Map<Integer, Department> map = new HashMap<>();
			
			
			while (rs.next()) {
				// se o departamento ja existir o map pega ele
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				// faz a verificação se ja tem um departmento instanciado
				if(dep == null) {
					dep = instanciateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller obj = instanciateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
