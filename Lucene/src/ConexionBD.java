import java.sql.*;


public class ConexionBD {
	
	private static Connection conexion;
	private static Statement comando;
	
	public static void conexion(String sql){
		//********************************************** 
		// Se definen datos para la conexion a la base de datos* 
		//********************************************** 
    
	    String driver = "com.mysql.jdbc.Driver";//DRIVER MYSQL 
	    String connectString = "jdbc:mysql://localhost/cpmtest";//STRING CONECCION MYSQL
	    String user = "root";
	    String password = "1234"; 
	    //************************************************************************* 
	    // Se realiza la conexión a la Base de Datos tomando los datos anteriormente definidos* 
	    //************************************************************************* 
	    
	    try{
	    	//*********************************************************************** 
	        Class.forName(driver); 
	        conexion = DriverManager.getConnection(connectString, user, password); 
	        // ACA YA ESTAMOS CONECTADOS A LA BASE DE DATOS
	        
	        System.out.println("conexion lista");
	        comando = conexion.createStatement();
	        comando.execute(sql);
	        System.out.println("El comando "+sql+"se ha ejecutado correctamente");
	        
	    }catch(Exception e){
	    	System.out.println(e);
	    }
    
	}
	
	//cerrar coneccion con base de datos
	public static void cerrarConeccion() throws SQLException{
		conexion.close();
		comando.close();	
		System.out.println("Conexion cerrada!");
	}
	
	//inserta en tag
	public static void InsertarTag(int idLicitacion, String tag){	
        String sql ="insert into tags(artifact_id,tag) values ('"+idLicitacion+"','"+tag+"')";
        ConexionBD.conexion(sql);  
	}
	//inserta artifact
	public static void insertarArtifact(int id, int artifact_type_id, int catalog_id){
		String sql ="insert into artifacts values("+id+","+artifact_type_id+","+catalog_id+")";
		ConexionBD.conexion(sql);
	}
}