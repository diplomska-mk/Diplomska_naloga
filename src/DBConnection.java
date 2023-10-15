import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.List;

public class DBConnection {
	
	/*String userName = "MarkoKaferle";
	String password = "******";
	
	 define here the sql server and the used database
	String url = "jdbc:sqlserver://212.235.190.207;databaseName=Vocabulary";
	String url = "jdbc:sqlserver://212.235.190.207;databaseName=Vocabulary;trustServerCertificate=true;encrypt=true";
	String url = "jdbc:sqlserver://212.235.190.237;databaseName=Vocabulary;integratedSecurity=true;\" +\n"
			+ "     \"encrypt=true;trustServerCertificate=true";
	*/
	
	String userName = "sa";
	String password = "******"; // PRIVATNO :)
	
	//String url = "jdbc:sqlserver://212.235.190.204;databaseName=Vocabulary";
	
	String url = "jdbc:sqlserver://212.235.190.204;databaseName=Vocabulary;trustServerCertificate=true;encrypt=true";
	

	
	int sourceCategoryIDinDB; String articleID; String articleLink; int articleIDInDB;
		
	DBConnection (int sourceCategoryIDinDB) { 
		this.sourceCategoryIDinDB=sourceCategoryIDinDB;
	}
	
	void setArticle(String articleID, String articleLink){
		this.articleID=articleID;
		this.articleLink=articleLink;
	}
	
	boolean articleNotInDB(){
		boolean articleNotInDB = false;
		try  {
			Connection conn = DriverManager.getConnection(url, userName, password);
			CallableStatement cstmt = conn.prepareCall("{call dbo.spArticleExists(?, ?, ?, ?)}");
			cstmt.setInt(1, sourceCategoryIDinDB); 
			cstmt.setString(2, articleID); 
			
			cstmt.registerOutParameter(3, java.sql.Types.BOOLEAN);
			cstmt.registerOutParameter(4, java.sql.Types.INTEGER);
			cstmt.execute();
			//System.out.println(sourceCategoryIDinDB + ", " + articleID + ", " + cstmt.getBoolean(3));
			if (cstmt.getBoolean(3)){
				articleNotInDB = false;
				this.articleIDInDB = cstmt.getInt(4);
			}
			else {
				articleNotInDB = true;
			}
			conn.close();
		}
		catch (Exception e) {
    		e.printStackTrace();
    		writeExceptionToLogErrorFile(e);
    	}
		finally {
		}
		return articleNotInDB;
	}
	
	//public int getArticleIDInDB(){return this.articleIDInDB;}
	
	void updateArticleEndDate(String processingDate){
		try  {
			Connection conn = DriverManager.getConnection(url, userName, password);
			CallableStatement cstmt = conn.prepareCall("{call dbo.spUpdateArticleEndDate(?, ?)}");
			cstmt.setInt(1, this.articleIDInDB); 
			cstmt.setDate(2, java.sql.Date.valueOf(processingDate.substring(0, 10)));
			cstmt.execute();
			conn.close();
		}
		catch (Exception e) {
    		e.printStackTrace();
    		writeExceptionToLogErrorFile(e);
    	}
		finally {
		}
	}
	
	void addArticle2DB(List<String> authors, String publicationDate, String processingDate, String titleText, String subtitleText, String descriptionText, String keywords, String paragraphText) {
		int articleIDinDB = 0; 		
		Content titleContent = new Content(titleText);
		Content subtitleContent = new Content(subtitleText);
		Content descriptionContent = new Content(descriptionText);
		Content keywordContent = new Content(keywords.replaceAll(",", ". ")); keywordContent.setKeywords();
		Content paragraphContent = new Content(paragraphText);
		// ZAPIŠE V CONSOLE IME ČLANKA; 1, X; 0, 0; XX, XX;
		//System.out.println(articleID + "; " + titleContent.getNumOfSenteces() + ", " + titleContent.getNumOfWords() + "; " + subtitleContent.getNumOfSenteces() + ", " + subtitleContent.getNumOfWords() + "; " + paragraphContent.getNumOfSenteces() + ", " + paragraphContent.getNumOfWords());;
		
		
		/*for (int i=0; i<titleContent.getWords().size(); i++){
			System.out.print(titleContent.getWords().get(i) + " ");
		}
		System.out.print("\n");*/
			
		// ZAPIŠE V CONSOLE DATUM IZDAJE
		//System.out.println(publicationDate.substring(0, 10));
		//System.out.println("PublDate: " + publicationDate);	
		//System.out.println("ProcDate: " + processingDate);
		
		try {
			
			Connection conn = DriverManager.getConnection(url, userName, password);
			CallableStatement cstmt = null;
			
			try {
				// prvo dodamo članek 
				// IZPIS V CONSOLE
				
				
				//System.out.println("----------");
				System.out.println("\n------- DBConnection.java FILE SUMMARY -------\n");
				
				System.out.println("SourceID IN DB ->  " + this.sourceCategoryIDinDB);
				System.out.println("Article ID ->      " + this.articleID);
				System.out.println("Article LINK ->    " + this.articleLink);
				System.out.println("PublDate ->        " + java.sql.Date.valueOf(publicationDate));
				System.out.println("ProcDate ->        " + java.sql.Date.valueOf(processingDate.substring(0, 10)));
				System.out.println("TitleSents ->      " + titleContent.getNumOfSenteces());
				System.out.println("TitleWords ->      " + titleContent.getNumOfWords());
				System.out.println("SubtitleSents  ->  " + subtitleContent.getNumOfSenteces());
				System.out.println("SubtitleWords  ->  " + subtitleContent.getNumOfWords());
				System.out.println("ParagraphSents ->  " + paragraphContent.getNumOfSenteces());
				System.out.println("ParagraphWords ->  " + paragraphContent.getNumOfWords());
				System.out.println("\n");
				
				//System.out.println("----------");
				

				cstmt = conn.prepareCall("{call dbo.spAddArticle(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
				cstmt.setInt(1, this.sourceCategoryIDinDB); 
				cstmt.setString(2, this.articleID); 
				cstmt.setString(3, this.articleLink); 
				//cstmt.setDate(4, java.sql.Date.valueOf(publicationDate.substring(0, 10)));
				cstmt.setDate(4, java.sql.Date.valueOf(publicationDate));
				cstmt.setDate(5, java.sql.Date.valueOf(processingDate.substring(0, 10)));	
				cstmt.setInt(6, titleContent.getNumOfSenteces()); 
				cstmt.setInt(7, titleContent.getNumOfWords()); 	
				cstmt.setInt(8, subtitleContent.getNumOfSenteces()); 
				cstmt.setInt(9, subtitleContent.getNumOfWords()); 
				cstmt.setInt(10, paragraphContent.getNumOfSenteces()); 
				cstmt.setInt(11, paragraphContent.getNumOfWords()); 
				cstmt.registerOutParameter(12, java.sql.Types.INTEGER);
				cstmt.execute();
				
				articleIDinDB = cstmt.getInt(12);
				System.out.println("IDinDB ->  " + articleIDinDB);
				
				// sedaj dodamo avtorje clanka (če avtorja v bazi ni, ga bo sp dodala)
				cstmt = conn.prepareCall("{call dbo.spAddArticleAuthor(?, ?)}");
				cstmt.setInt(1, articleIDinDB); 
				for (String author: authors){
					cstmt.setString(2, author); 
					System.out.println("Authors: " + author);
					cstmt.execute();
				}
				
				
				System.out.println("\n----- DBConnection.java FILE SUMMARY END -----\n\n\n\n");
				
				
				// na koncu pa dodame vse besede
				for (int i=0; i<titleContent.getWords().size(); i++){
					cstmt = conn.prepareCall("{call dbo.spAddTitleWord(?, ?, ?, ?, ?)}");
					cstmt.setInt(1, articleIDinDB); 
					cstmt.setString(2, titleContent.getWords().get(i));
					cstmt.setString(3, titleContent.getWordLemmas().get(i));
					cstmt.setInt(4, titleContent.getWordsPosition().get(i));				
					cstmt.setInt(5, titleContent.getSentecesPostion().get(i)); 
					
					cstmt.execute();
				}
				
				for (int i=0; i<subtitleContent.getWords().size(); i++){
					cstmt = conn.prepareCall("{call dbo.spAddSubtitleWord(?, ?, ?, ?, ?)}");
					cstmt.setInt(1, articleIDinDB); 
					cstmt.setString(2, subtitleContent.getWords().get(i)); 	
					cstmt.setString(3, subtitleContent.getWordLemmas().get(i));
					cstmt.setInt(4, subtitleContent.getWordsPosition().get(i));				
					cstmt.setInt(5, subtitleContent.getSentecesPostion().get(i)); 
					
					cstmt.execute();
				}
				
				for (int i=0; i<descriptionContent.getWords().size(); i++){
					cstmt = conn.prepareCall("{call dbo.spAddDescriptionWord(?, ?, ?, ?, ?)}");
					cstmt.setInt(1, articleIDinDB); 
					cstmt.setString(2, descriptionContent.getWords().get(i)); 	
					cstmt.setString(3, descriptionContent.getWordLemmas().get(i));
					cstmt.setInt(4, descriptionContent.getWordsPosition().get(i));				
					cstmt.setInt(5, descriptionContent.getSentecesPostion().get(i)); 
					
					cstmt.execute();
				}
				
				for (int i=0; i<keywordContent.getWords().size(); i++){
					cstmt = conn.prepareCall("{call dbo.spAddKeyword(?, ?, ?)}");
					cstmt.setInt(1, articleIDinDB); 
					cstmt.setString(2, keywordContent.getWords().get(i)); 
					cstmt.setString(3, keywordContent.getWordLemmas().get(i)); 
					
					cstmt.execute();
				}
				
				for (int i=0; i<paragraphContent.getWords().size(); i++){
					cstmt = conn.prepareCall("{call dbo.spAddParagraphWord(?, ?, ?, ?, ?)}");
					cstmt.setInt(1, articleIDinDB); 
					cstmt.setString(2, paragraphContent.getWords().get(i)); 
					cstmt.setString(3, paragraphContent.getWordLemmas().get(i));
					cstmt.setInt(4, paragraphContent.getWordsPosition().get(i));				
					cstmt.setInt(5, paragraphContent.getSentecesPostion().get(i));  
					
					cstmt.execute();
				}
				
			}
			catch (Exception e) {
	    		e.printStackTrace();
	    		writeExceptionToLogErrorFile(e);
	    	}
			finally {
				// na koncu preverimo, če so bile vse besede dodane
				if (articleIDinDB>0) {
					cstmt = conn.prepareCall("{call dbo.spReverseIfError(?, ?, ?, ?, ?, ?)}");
					cstmt.setInt(1, articleIDinDB); 
					cstmt.setInt(2, titleContent.getNumOfWords()); 
					cstmt.setInt(3, subtitleContent.getNumOfWords()); 
					cstmt.setInt(4, descriptionContent.getNumOfWords()); 
					cstmt.setInt(5, paragraphContent.getNumOfWords()); 					
					cstmt.registerOutParameter(6, java.sql.Types.BIT);
					
					cstmt.execute();
					
					if(cstmt.getBoolean(6)){
						System.out.println("SQL Insertion Error!");
						writeToLogErrorFile(System.lineSeparator() + "Insertion problem _ " + processingDate + ": " + articleIDinDB + " " + this.articleLink + " " + this.sourceCategoryIDinDB);
					}
					else {
						writeToLogFile(System.lineSeparator() + this.articleLink);
					}
		
				}
				else {
					writeToLogErrorFile(System.lineSeparator() + "Connection problem _ " + processingDate + ": " + articleIDinDB + " " + this.articleLink + " " + this.sourceCategoryIDinDB);
				}
				conn.close();
			}
		}
		catch (Exception e) {
    		e.printStackTrace();
    		//writeExceptionToLogErrorFile(e);
    	}
		finally {

		}				
		
	}

	
	void fillIgnoreWords(String fileName){
		String lineWord;	
		
		try  {
			Connection conn = DriverManager.getConnection(url, userName, password);
			CallableStatement cstmt = conn.prepareCall("{call dbo.spAddIgnoreWord(?)}");
		
			InputStream fis = new FileInputStream(fileName);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		
		    while ((lineWord = br.readLine()) != null) {
		    	cstmt.setString(1, lineWord); 
		    	cstmt.execute();
		    }
		    br.close();
		}
		catch (Exception e) {
    		e.printStackTrace();
    	}
		finally {
		}
	}
	
	public static void writeToLogFile(String articleData) {		
		try {
		    Files.write(Paths.get("C://Users/marko/Desktop/Vocabulary/logs/log 2022.txt"), articleData.getBytes(), StandardOpenOption.APPEND);
		}
		catch (IOException e) {
			e.printStackTrace();
        }
    }
	
	public static void writeToLogErrorFile(String articleData) {		
		try {
		    Files.write(Paths.get("C://Users/marko/Desktop/Vocabulary/logs/log error 2022.txt"), articleData.getBytes(), StandardOpenOption.APPEND);
		}
		catch (IOException e) {
			e.printStackTrace();
        }
    }
	
	public static void writeExceptionToLogErrorFile(Exception e) {		
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		writeToLogErrorFile(System.lineSeparator() + exceptionAsString); 
    }
	
	int getNumOfTodayProcArticles(){
		int numOfArticles = 0;
		try  {
			Connection conn = DriverManager.getConnection(url, userName, password);
			//CallableStatement cstmt = conn.prepareCall("{?=dbo.fnNumOfTodayProcArticles(?)}");
			CallableStatement cstmt = conn.prepareCall("{? = call dbo.fnNumOfTodayProcArticles()}");
			cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
			cstmt.execute();
			numOfArticles = cstmt.getInt(1);
			conn.close();
		}
		catch (Exception e) {
    		e.printStackTrace();
    	}
		finally {
		}
		return numOfArticles;
	}
	
	
	
}
