import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;

public class DownloadPageArch {
	
	static String logFile = "C://Users/marko/Desktop/Vocabulary/logs/log 2022.txt";
	static String logErrorFile = "C://Users/marko/Desktop/Vocabulary/logs/log error 2022.txt";

    public static void main(String[] args) throws IOException {
    	
    	// code needed to get the relative url addresses of site categories
    	/*Categories[] categories = Categories.values();
    	for (Categories category : categories) {
    	    System.out.println(category.getLink());
    	}*/
   	
    	/*writePageContentToFile("TheConversation.xml","https://theconversation.com/uk");
    	Article article = new Article("TheConversation.xml");
    	article.getCategories();*/
    	    	
    	// choose here between different sources and categories 
    	//CategoriesRTV categories = CategoriesRTV.TUREAVANTURE;    	    	
    	
    	writeDateToLogFile(logFile);   
    	
    	processCategory(CategoriesTheGuardian.UK);
    	processCategory(CategoriesTheGuardian.WORLD);
   	   	processCategory(CategoriesTheGuardian.SPORT);
    	processCategory(CategoriesTheGuardian.FOOTBALL);
    	processCategory(CategoriesTheGuardian.OPINION);    	
    	processCategory(CategoriesTheGuardian.CULTURE);
    	processCategory(CategoriesTheGuardian.BUSINESS);
    	processCategory(CategoriesTheGuardian.LIFESTYLE);
    	processCategory(CategoriesTheGuardian.FASHION);
    	processCategory(CategoriesTheGuardian.ENVIRONMENT);
    	processCategory(CategoriesTheGuardian.TECH);
    	processCategory(CategoriesTheGuardian.TRAVEL);
    	processCategory(CategoriesTheGuardian.SCIENCE);
    	processCategory(CategoriesTheGuardian.GLOBALDEVELOPMENT);
    	//processCategory(CategoriesTheGuardian.CITIES); <-- NE OBSTAJA VEČ
    	//processCategory(CategoriesTheGuardian.OBITUARIES);
    	    	   	
    	//ArticleID=639 check numOfWordsInParagraph = 865 if not, deleteAll4Article
    	
    	/*DBConnection dBConnection = new DBConnection(1);
    	dBConnection.fillIgnoreWords("stopwords_stemming_en");*/ 
    	
      	//writeResultToLogFile(dBConnection.getNumOfTodayProcArticles(), logFile); 
       	//System.out.println("Num. of today proc. articles: " + dBConnection.getNumOfTodayProcArticles());
     	
    }
    
    //private static void processCategory(CategoriesTheConversation category) {
    private static void processCategory(CategoriesTheGuardian category) {
    	for (int i=1; i < 31; i++) {
    		//84125
    		String dateSourceLink; String dateLocal; String datePublication;
    		if (i > 9) {
	    		dateSourceLink = "/2022/jun/"+ Integer.toString(i);
	    		dateLocal = "2022-jun-" + Integer.toString(i);
	    		datePublication = "2022-06-" + Integer.toString(i);
    		} else {
	    		dateSourceLink = "/2022/jun/0"+ Integer.toString(i);
	    		dateLocal = "2022-jun-0" + Integer.toString(i);
	    		datePublication = "2022-06-0" + Integer.toString(i);
    		}
	    	String link = category.getLinkOLD(dateSourceLink);
	    	//String link = category.getLink();
	    	//String referenceLink = categories.getReferenceLink();
	    	System.out.println(link); 
	    		    	
		    	final String parentSourceFileName = category.createParentSourceFileNameOLD(dateLocal); // xml datoteka za vsebino kategorije na dan    	
		    	//final String parentSourceFileName = category.createParentSourceFileName();
		    	final String articleFilePath = category.getArticleFilePathOLD(dateLocal); // direktorij za vso vsebino za dan
		    	//final String articleFilePath = category.getArticleFilePath();
		    	System.out.println(parentSourceFileName);
		    	System.out.println(articleFilePath);
		    	
		    	writePageContentToFile(parentSourceFileName, link);
		    	createFolder(articleFilePath);
		    	
		    	Article article = new Article(parentSourceFileName);
		    	
		    	List<String> articleLinks = article.getArticleLinks(category.getAClassReference(), category.getReferenceLink(), category.getReferenceCharacterENDMIN6());
		    	String sourceFileName = null;
		    	String destinationFileName = null;
		    	
		    	DBConnection dBConnection = new DBConnection(category.getSourceCategoryIDinDB());
		    	//System.out.println("Num. of today proc. articles: " + dBConnection.getNumOfTodayProcArticles());
		    	
		    	String authors = null;
				String publicationDate = null;    		
				String titleText = null;
				String subtitleText = null;;
				String descriptionText = null;
				String keywords = null;
				String paragraphText = null;
		    	
		       	for (String articleLink: articleLinks){ 
		       		//System.out.println("ArticleLink: " + articleLink.substring(articleLink.length()-5, articleLink.length())); 
		    	//String articleLink = "https://www.theguardian.com/world/2022/may/19/putins-daughter-flew-to-munich-more-than-50-times-investigation-suggests";
		    	//String articleLink = "https://www.theguardian.com/world/live/2022/may/19/russia-ukraine-war-latest-russian-use-of-laser-weapons-shows-invasion-a-failure-says-zelenskiy-live";
		    	//articleLink = "https://www.theguardian.com/world/2022/may/19/vadim-shishimarin-russian-soldier-asks-ukrainian-widow-to-forgive-him-during-first-war-crimes-trial";
		    	//String articleLink = "/how-mount-agungs-eruption-can-create-the-worlds-most-fertile-soil-85134";
		       	//String articleLink = "/remainer-or-re-leaver-the-philosophical-conundrum-posed-by-brexit-78810";
		       		System.out.println("LINK: " + articleLink);	
		       		if (articleLink.substring(articleLink.length()-5, articleLink.length()).equals("video")
			    		|| 
			    		articleLink.substring(articleLink.length()-8, articleLink.length()).equals("pictures")
			    		) {}
			    	else {
		    		
		       		String articleID = article.getArticleID(articleLink, category.getArticleIDLength());
		    		sourceFileName = category.getSourceFileNameOLD(articleID, dateLocal);
		    		//sourceFileName = category.getSourceFileName(articleID);
		    		destinationFileName = category.createDestinationFileName(articleFilePath, articleID);
		    		System.out.println("New article ID: " + articleID);
		    		//System.out.println(destinationFileName);
		    		
		    		dBConnection.setArticle(articleID, articleLink);
		 
		    		if (dBConnection.articleNotInDB()){
		    			System.out.println("NotInDB: YES \n");
		    			//System.out.println(category.getParentLinkPart4Article() + articleLink);
		    			writePageContentToFile(sourceFileName, category.getParentLinkPart4Article() + articleLink);
		    			//System.out.println(sourceFileName);    			
		    			//System.out.println(destinationFileName);    			
		    			
		        		article = new Article(sourceFileName);
		        		authors = article.getAuthor();
		        		//publicationDate = article.getPublicationDate(); 
		        		publicationDate = datePublication;; 
		        		titleText = article.getTitlesText();
		        		subtitleText = article.getSubtitlesText();
		        		//subtitleText = article.getDescriptionText();
		        		descriptionText = article.getDescriptionText();
		        		keywords = article.getKeywords();
		        		paragraphText = article.getParagraphsText();
		
		        		writeContentToFile(titleText + System.lineSeparator() + subtitleText + System.lineSeparator() + descriptionText + System.lineSeparator() + keywords + System.lineSeparator() + System.lineSeparator() + authors + System.lineSeparator() + publicationDate + System.lineSeparator() + System.lineSeparator() + System.lineSeparator() + paragraphText, destinationFileName);
		        		
		        		dBConnection.addArticle2DB(article.getAuthors(), publicationDate, category.getDate(), titleText, subtitleText, descriptionText, keywords, paragraphText);
		        		
		    		} 
		    		else {
		    			System.out.println("NotInDB: NO \n");
		    			dBConnection.updateArticleEndDate(category.getDate());
		    		}
		       	}
	    	}
    	}
    }	    
    
    private static void writePageContentToFile(String sourceFileName, String link) {
    	try {
	    	URL url = new URL(link);
	        URLConnection con = url.openConnection();
	        InputStream is = con.getInputStream();
	        
	        HtmlCleaner cleaner = new HtmlCleaner();
	        CleanerProperties props = cleaner.getProperties();
	        TagNode node = cleaner.clean(is);
	        
	        final SimpleHtmlSerializer htmlSerializer = new SimpleHtmlSerializer(props);

	        htmlSerializer.writeToFile(node, sourceFileName, "utf-8");
    	}
    	catch (Exception e) {
        	e.printStackTrace();
        }
		
	}
    
    private static void createFolder(String folderName) {
    	File dir = new File(folderName);
    	try {
    		dir.mkdir();
    	}
    	catch (Exception e) {
        	e.printStackTrace();
        }    
    }
    
    public static void writeContentToFile(String content, String file) {
    	try {
        	FileWriter fw = new FileWriter(file);
        	fw.write(content);
        	fw.close();
    	 }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    public static void appendToLogFile(String content, String logFile) {
    	try {
    	    Files.write(Paths.get(logFile), content.getBytes(), StandardOpenOption.APPEND);
   		}
   		catch (IOException e) {
   			e.printStackTrace();
   		}
    }
    
    private static void writeDateToLogFile(String logFile){
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss"); 
    	Date date = new Date();
    	String strDate = System.lineSeparator() + dateFormat.format(date) + " TheGuard; ";
    	//String strDate = dateFormat.format(date) + " TheGuard; ";
    	appendToLogFile(strDate, logFile); 
    	appendToLogFile(strDate, logErrorFile); 
    }
    
    private static void writeResultToLogFile(int numOfProcArticles, String logFile){
    	appendToLogFile(System.lineSeparator() + "Fin. Num of added articles: " + numOfProcArticles, logFile); 
    }
	
	
}