import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

public class NGramDriver{

  public static void main (String [] args){
    try{
    	
    	for(int i=1;i<=20;i++){
    		
    		//LECTURA DE TERMINOS EN CADA LICITACION
        	File f = new File("C:\\Temp\\terminos\\Terminos_licitacion"+i+".txt");
        	FileReader fr = new FileReader(f);
        	BufferedReader br = new BufferedReader(fr);
        	String text="";
        	
        	String linea;
            while((linea = br.readLine()) != null){
              //System.out.println(linea);
              text=text+" "+linea;
            }
            fr.close();
        	//System.out.println(text);
        	NGramExtractor extractor = new NGramExtractor();
        	extractor.extract(text, 4, 4, true);
        	
        	LinkedList<String> ngrams = extractor.getNGrams();
        	
        	System.out.println(extractor.getUniqueNGrams());
        	
        	//insertar tags recuperados de los cuerpos de cada licitacion
        	LinkedList<String> uniqueGrams = extractor.getUniqueNGrams();
        	ConexionBD.insertarArtifact(i, 4, 1);
        	for(String ngram: uniqueGrams){
        		ConexionBD.InsertarTag(i, ngram);
        		System.out.println("Termino "+ngram+" insertado correctamente ");
        	}
        	ConexionBD.cerrarConeccion();
        	
    	}
    }
    catch (Exception e){
      System.err.println(e.toString());
    }
  }
}