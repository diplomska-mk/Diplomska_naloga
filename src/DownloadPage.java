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

public class DownloadPage {
	
	static String logFile = "C://Users/marko/Desktop/Vocabulary/logs/log 2023.txt";
	static String logErrorFile = "C://Users/marko/Desktop/Vocabulary/logs/log error 2023.txt";

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
    	    	
    	/*processCategory(CategoriesTheConversation.EDUCATION);
    	processCategory(CategoriesTheConversation.HOME);
    	processCategory(CategoriesTheConversation.ARTS_CULTURE);
    	processCategory(CategoriesTheConversation.BUSINESS_ECONOMY);
    	processCategory(CategoriesTheConversation.ENVIRONMENT_ENERGY);
    	processCategory(CategoriesTheConversation.HEALTH_MEDICINE);
    	processCategory(CategoriesTheConversation.POLITICS_SOCIETY);
    	processCategory(CategoriesTheConversation.SCIENCE_TECHNOLOGY);
    	processCategory(CategoriesTheConversation.BREXIT);
    	processCategory(CategoriesTheConversation.CITIES);*/
    	
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
    	//processCategory(CategoriesTheGuardian.CITIES);		<-- NE OBSTAJA VEČ
    	processCategory(CategoriesTheGuardian.GLOBALDEVELOPMENT);
    	processCategory(CategoriesTheGuardian.OBITUARIES);
    	    	   	
    	//ArticleID=639 check numOfWordsInParagraph = 865 if not, deleteAll4Article
    	
    	/*DBConnection dBConnection = new DBConnection(1);
    	dBConnection.fillIgnoreWords("stopwords_stemming_en");*/ 
    	
      	//writeResultToLogFile(dBConnection.getNumOfTodayProcArticles(), logFile); 
       	//System.out.println("Num. of today proc. articles: " + dBConnection.getNumOfTodayProcArticles());
     	
    }
    
    //private static void processCategory(CategoriesTheConversation category) {
    private static void processCategory(CategoriesTheGuardian category) {
    	String link = category.getLink();
    	//String referenceLink = categories.getReferenceLink();
    	System.out.println(link);
    	
    	final String parentSourceFileName = category.createParentSourceFileName(); // xml datoteka za vsebino kategorije na dan    	
    	final String articleFilePath = category.getArticleFilePath(); // direktorij za vso vsebino za dan
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
    	
		//2019  ??????? PONEKOD NE DOBI NIČ V XML, PONEKOD NE DELAJO AVTORJI IN/ALI ČAS (Boris za primer)
		//String articleLink = "http://www.theguardian.com/politics/2019/jun/09/boris-johnson-kept-from-media-in-ruthlessly-organised-campaign";
		//String articleLink = "http://www.theguardian.com/society/2019/jun/09/6000-residential-care-workers-suffer-violent-attacks";
		//DELA NORMALNO
		//String articleLink = "https://www.theguardian.com/australia-news/2019/jun/11/big-stick-energy-bill-coalition-mp-wants-economy-wide-power-to-break-up-big-companies";
		
		//2021  <-- POPRAVI ZA ČAS
		//String articleLink = "https://www.theguardian.com/football/ng-interactive/2021/oct/07/next-generation-2021-60-of-the-best-young-talents-in-world-football";
		
		//2023 NORMAL ONE
		//String articleLink = "https://www.theguardian.com/football/2023/may/29/bundesliga-borussia-dortmund-bayern-munich-mainz-koln";
    	
		//NO <a rel> -> <meta article:author>
		//String articleLink = "https://www.theguardian.com/football/2023/may/30/coventry-condemn-racist-abuse-of-fankaty-dabo-following-defeat-to-luton";
		//String articleLink = "https://www.theguardian.com/sport/2023/may/31/dragons-coaching-target-and-major-sponsor-walk-away-from-nrl-club";
    	
		
    	//NO published_time -> SUMMARY  <-- ZGLEDA OK
       	//String articleLink = "https://www.theguardian.com/football/ng-interactive/2023/may/30/david-squires-premier-league-season-awards";
		
		//no summary -> INDEX FOR published_time IS WRONG (Index 0 out of bound for length 0)  <-- ZGLEDA OK
		//String articleLink = "https://www.theguardian.com/football/2023/may/30/galatasaray-win-first-turkish-league-title-since-2019-with-a-game-to-spare";
    	//String articleLink = "https://www.theguardian.com/football/2023/may/31/julen-lopetegui-when-i-came-to-wolves-a-lot-of-friends-asked-me-why";
		
		
		//AUDIO		<-- ZGLEDA OK
		//String articleLink = "https://www.theguardian.com/news/audio/2023/jun/02/is-manchester-city-dominance-english-football-fair-podcast";
	
       	//BOLD SUBTITLE, DOUBLE <a rel>, ki ne vpiše v bazo  <-- REŠENO ZA <a>, treba še bold
       	//String articleLink = "https://www.theguardian.com/sport/live/2023/jun/02/england-v-ireland-lords-test-day-two-live";
       		
       		System.out.println("LINK: " + articleLink);
    		
       		String articleID = article.getArticleID(articleLink, category.getArticleIDLength());
    		sourceFileName = category.getSourceFileName(articleID);    				
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
        		publicationDate = article.getPublicationDate();    
        		//publicationDate = "1000-01-01";
        		titleText = article.getTitlesText();
        		subtitleText = article.getSubtitlesText();
        		descriptionText = article.getDescriptionText();
        		keywords = article.getKeywords();
        		paragraphText = article.getParagraphsText();
        		
        		// ZAPIŠE STVARI V TXT DATOTEKO
        		writeContentToFile(titleText + System.lineSeparator() + subtitleText + System.lineSeparator() + descriptionText + System.lineSeparator() + keywords + System.lineSeparator() + System.lineSeparator() + authors + System.lineSeparator() + publicationDate + System.lineSeparator() + System.lineSeparator() + System.lineSeparator() + paragraphText, destinationFileName);
        		
        		dBConnection.addArticle2DB(article.getAuthors(), publicationDate, category.getDate(), titleText, subtitleText, descriptionText, keywords, paragraphText);
        		
    		}
    		else {
    			System.out.println("NotInDB: NO \n");
    			dBConnection.updateArticleEndDate(category.getDate());
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