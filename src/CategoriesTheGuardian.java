import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public enum CategoriesTheGuardian {
	UK ("/uk-news", "UK", 12),													// BOLJ NE KOT JA (DOBI XML, A V VEČINI PRIMEROV NE OBDELUJE)
	WORLD ("/world", "WORLD", 13),												// BOLJ NE KOT JA (DOBI XML, A V VEČINI PRIMEROV NE OBDELUJE)
	SPORT ("/uk/sport", "SPORT", 14),											// DELUJE
	FOOTBALL ("/football", "FOOTBALL", 15),										// DELUJE
	OPINION ("/uk/commentisfree", "OPINION", 16),								// DOBI XML, NE DELUJE
	CULTURE ("/uk/culture", "CULTURE", 17),										// DOBI XML, NE DELUJE
	BUSINESS ("/uk/business", "BUSINESS", 18),									// DOBI XML, NE DELUJE
	LIFESTYLE ("/uk/lifeandstyle", "LIFESTYLE", 19),							// DELUJE, OBČASNE TEŽAVE PRI KRIŽANKAH
	FASHION ("/fashion", "FASHION", 20),										// DOBI XML, NE DELUJE
	ENVIRONMENT ("/uk/environment", "ENVIRONMENT", 21),							// DELUJE
	TECH ("/uk/technology", "TECH", 22),										// DOBI XML, NE DELUJE
	TRAVEL ("/uk/travel", "TRAVEL", 23),										// DELUJE
	SCIENCE ("/science", "SCIENCE", 25),										// DOBI XML, NE DELUJE
	CITIES ("/uk/cities", "CITIES", 26), 										// NE OBSTAJA VEČ
	GLOBALDEVELOPMENT ("/global-development", "GLOBAL-DEVELOPMENT", 27),		// DELUJE
	OBITUARIES ("/tone/obituaries", "OBITUARIES", 28);							// DELUJE
	
	//private int value; 
	private String relativeLink; private String name; private int sourceCategoryIDinDB;
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss"); 
	Date date = new Date();
	private String strDate = dateFormat.format(date);

	CategoriesTheGuardian (String relativeLink, String categoryName, int sourceCategoryIDinDB) { 
		this.relativeLink=relativeLink;
		this.name=categoryName;
		this.sourceCategoryIDinDB=sourceCategoryIDinDB;
	}
	
	private String parentLink = "https://www.theguardian.com";
			
	public String getParentLink() { return parentLink;}
	
	public String getParentLinkPart4Article() { return "";}
	
	public String getLinkOLD(String date) { return getParentLink() + this.relativeLink + date;}
	public String getLink() { return getParentLink() + this.relativeLink;}
	
	public int getSourceCategoryIDinDB() { return this.sourceCategoryIDinDB;}
	
	//public String getSourceFilePath() {return "SourceData/TheGuard2023/" + this.name + "/";};
	
	//public String getSourceFilePath() {return "C://Vocabulary/SourceData/TheGuard2023/" + this.name + "/";};
	public String getSourceFilePath() {return "SourceData/TheGuard2022/" + this.name + "/";};
	
	
	
	private String getSourceFileExtension() {return "xml";};
	
	public String createParentSourceFileNameOLD(String date) {return getSourceFilePath() + getDate() + "_" + this.name + "." + getSourceFileExtension();};
	public String createParentSourceFileName() {return getSourceFilePath() + getDate() + "_" + this.name + "." + getSourceFileExtension();};
	
	public String getArticleFilePathOLD(String date) {return getSourceFilePath() + date+ "/";};	
	public String getArticleFilePath() {return getSourceFilePath() + getDate() + "/";};
	
	public String getSourceFileNameOLD(String articleID, String date) {return getArticleFilePathOLD(date) + articleID + ".xml";}
	public String getSourceFileName(String articleID) {return getArticleFilePath() + articleID + ".xml";}	
	
	//public String getRelativeSourceFileAddr(String articleID) {return getDate() + "/" + articleID + ".xml";}
	
	//public String getRelativeDestinationFileAddr(String articleID) {return getDate() + "/" + articleID + ".txt";}
	
	public String getDate() {return strDate;}	
	
	public String getName() { return this.name;}
		
	public String createDestinationFileName(String articleFilePath, String articleID) {return articleFilePath + articleID + ".txt";};
		
	public String getReferenceLink() { return "https://www.theguardian.com/";}
	
	public String getAClassReference() { return "fc-item__link";}
	
	
	//data-link-name="article"
	
	public String getReferenceCharacterENDMIN6() { return "-";}
	
	public int getArticleIDLength(){ return 200;}
	
}


