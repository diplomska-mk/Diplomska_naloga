import java.util.*;
import java.util.stream.Collectors;
import java.io.File;
import java.time.LocalDate;
import java.time.format.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Article {

	Document doc;
	
	Article (String sourceFileName) { 
		this.doc = parseXMLFromFile(sourceFileName);
	}
	
	public void getCategories() { 
		for (int i = 0; i < doc.select("a").size(); i = i +  1) {
	     	System.out.println("" + doc.select("a").get(i).text());
	     	System.out.println("" + doc.select("a").get(i).attributes());
	     }
	} 
	
	public String deDup(String s) {
	    return Arrays.stream(s.split("\n")).distinct().collect(Collectors.joining("\n"));
	}
	
	public List<String> getArticleLinks(String aClassReference, String referenceLink, String referenceCharacter) { 
		List<String> articleLinks = new ArrayList<String>();
		String aClass = null; String hrefTemp = null; int intEnd;
		for (int i = 0; i < doc.select("a").size(); i = i +  1) {
			aClass  = "" + doc.select("a").get(i).attr("class");
			//System.out.println("" + doc.select("a").get(i));
			if (aClass.equals(aClassReference)) {
				hrefTemp = "" + doc.select("a").get(i).attr("href");
				intEnd = hrefTemp.length();
				if (hrefTemp.startsWith(referenceLink)) {
					//System.out.println(hrefTemp);
					//if (hrefTemp.substring(intEnd-6,intEnd-5).equals(referenceCharacter)){
						articleLinks.add(hrefTemp);	
					//}
				}
			}
		}
		return articleLinks;
	}
	
	public String getArticleID(String articleLink, int articleIDMaxLength) {
		String potentialID = articleLink.substring(Math.max(articleLink.length()-articleIDMaxLength,0));
		String articleID = potentialID.substring(potentialID.lastIndexOf("/") + 1, Math.min(potentialID.lastIndexOf("/") + 101, potentialID.length()));
		if(articleID.charAt(0)==('-')) {
			articleID=articleID.substring(1, articleID.length());
		}	
		return articleID;
	}
	
	
	
	
	
/*	// ------------ AUTHOR 2017-2022 with <meta> -------------
	// <meta name="author" content="Myfanwy Bugler" />
	
	public String getAuthor() {
		String author = "";
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			if (!doc.select("meta").get(i).getElementsByAttributeValue("name", "author").isEmpty()) {
				author = author + doc.select("meta").get(i).attr("content") + "\n";

			}
	    }
		System.out.println(author);
		return author;
	}
*/
	
	
/*	// ------------- AUTHOR 2023 with <meta> ----------------------
	// <meta property="article:author" content="https://www.theguardian.com/profile/andy-brassell" />
	
	public String getAuthor() {
		String author = "";
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			if (!doc.select("meta").get(i).getElementsByAttributeValue("property", "article:author").isEmpty()) {
				author = author + doc.select("meta").get(i).attr("content") + "\n";
				//ČE NAMESTO .attr VZAMEMO .text --> author = author.substring(41, author.length()-20);
			}
	    }
		System.out.println("Author: " + author);
		return author;
	}
*/	
	
	
	
	// -------------------------------- AUTHOR 2023  -------------------------------------
	// <meta name="author" content="Andy Brassell" />
	// <a rel="author" data-link-name="auto tag link" href="//www.theguardian.com/profile/andybrassell">Andy Brassell</a>
	// <meta property="article:author" content="https://www.theguardian.com/profile/andy-brassell" />
	// <meta property="article:author" content="Agencies" />
	
	public String getAuthor() {
		String author = "";
		String authorA = "";
			for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
				if (!doc.select("meta").get(i).getElementsByAttributeValue("name", "author").isEmpty()) {
					author = author + doc.select("meta").get(i).attr("content") + "\n";
				}
			}
			System.out.println("----- AuthorMETA: -----");
			System.out.println(author);
			
			
			// IMA PROBLEME, ČE JE <a rel> ZAPISAN VEČKRAT (ponavadi pri LIVE kategoriji)  <-- REŠENO Z deDup
			if (author == "") {
				for (int j = 0; j < doc.select("a").size(); j = j + 1) {
					if (!doc.select("a").get(j).getElementsByAttributeValue("rel", "author").isEmpty()) {
																						//&& (doc.select("a").get(j).text() != doc.select("a").get(j-1).text())
						author = author + doc.select("a").get(j).text() + "\n";
						authorA = author;
					}
				}
				System.out.println("----- AuthorA: -----");
				System.out.println(authorA);
				
				if (authorA == "") {
					for (int k = 0; k < doc.select("meta").size(); k = k + 1) {
						if (!doc.select("meta").get(k).getElementsByAttributeValue("property", "article:author").isEmpty()) {
							author = author + doc.select("meta").get(k).attr("content") + "\n";
							//ČE NAMESTO .attr VZAMEMO .text --> author = author.substring(41, author.length()-20);
						}
					}
					System.out.println("----- AuthorART-META: -----");
					System.out.println(author);
				
				} 	/*else {
				for (int k = 0; k < doc.select("meta").size(); k = k + 1) {
					if (!doc.select("meta").get(k).getElementsByAttributeValue("property", "article:author").isEmpty()) {
						author = author + doc.select("meta").get(k).attr("content") + "\n";
						//System.out.println(author);
					}
				}
				System.out.println("AuthorART-META: " + author);
*/
			}
			
		author = deDup(author);
		
		if ((author == "") && (authorA == "")) {
			author = "UNKNOWN";
		}
		
		System.out.println("--- FINALAuthor: ---");
		System.out.println(author + "\n");
		return author;
	}
	
	
	
	
	
/* 	// --------------- AUTHORS 2017-2022 with <a> ------------------

	public List<String> getAuthors() {
		List<String> authors = new ArrayList<String>();
		for (int i = 0; i < doc.select("a").size(); i = i + 1) {
			if (!doc.select("a").get(i).getElementsByAttributeValue("rel", "author").isEmpty()) {
				authors.add(doc.select("a").get(i).text());
				//System.out.println(doc.select("a").get(i).text());
			}
	    }
		return authors;
	}
*/
	

	
	// ---------------------------- AUTHORS 2023 ------------------------------
	// <meta name="author" content="Andy Brassell" />
	// <a rel="author" data-link-name="auto tag link" href="//www.theguardian.com/profile/andybrassell">Andy Brassell</a>
	// <meta property="article:author" content="https://www.theguardian.com/profile/andy-brassell" />
	// <meta property="article:author" content="Agencies" />
	
	public List<String> getAuthors() {
		List<String> authors = new ArrayList<String>();
		List<String> authorsA = new ArrayList<String>();
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			if (!doc.select("meta").get(i).getElementsByAttributeValue("name", "author").isEmpty()) {
				authors.add(doc.select("meta").get(i).attr("content"));
			}
		}
		System.out.println("----- AuthorsMETA: -----");
		System.out.println(authors);
		
		
		// IMA PROBLEME, ČE JE <a rel> ZAPISAN VEČKRAT (ponavadi pri LIVE kategoriji) <-- REŠENO Z LinkedHashSet
		if (authors.isEmpty()) {
			for (int j = 0; j < doc.select("a").size(); j = j + 1) {
				if (!doc.select("a").get(j).getElementsByAttributeValue("rel", "author").isEmpty()) {
					authors.add(doc.select("a").get(j).text());
				}
			}
			authorsA = authors;
			
			System.out.println("----- AuthorsA: -----");
			System.out.println(authorsA);
			
			if (authorsA.isEmpty()) {
				for (int k = 0; k < doc.select("meta").size(); k = k + 1) {
					if (!doc.select("meta").get(k).getElementsByAttributeValue("property", "article:author").isEmpty()) {
						authors.add(doc.select("meta").get(k).attr("content"));
						//ČE NAMESTO .attr VZAMEMO .text --> author = author.substring(41, author.length()-20);
					}
				}
				System.out.println("----- AuthorsART-META: -----");
				System.out.println(authors);
			
			} 	
		}
		
		/*for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			if (!doc.select("meta").get(i).getElementsByAttributeValue("property", "article:author").isEmpty()) {
				authors.add(doc.select("meta").get(i).attr("content"));
			} else if (!doc.select("meta").get(i).getElementsByAttributeValue("property", "author").isEmpty()) {
				authors.add(doc.select("meta").get(i).attr("content"));
			}
	    }*/
		

		Set<String> set = new LinkedHashSet<>(authors);
		authors.clear();
		authors.addAll(set);
		
		if ((authors.isEmpty()) && (authorsA.isEmpty())) {
			authors.add("UNKNOWN");		//VČASIH DELA, VČASIH NE
		}
			
		System.out.println("--- FINALAuthors: ---");
		System.out.println(authors);
		return authors;
	}
	
	

	
	
/*	// ------------- PUBLISHED DATE with <summary> <- SAMO, ČE SO BILI POPRAVKI --------
	// <details class="dcr-eb59kw"><summary class="dcr-1ybxn6r"><span class="dcr-u0h1qy">Sun 28 May 2023 18.48 BST</span></summary>

	public String getPublicationDate() {
		String publicationDate = "";
		String shortPublicationDate = "";
		for (int i = 0; i < doc.select("summary").size(); i = i + 1) {
			if (!doc.select("summary").get(i).getElementsByAttributeValue("class", "dcr-1ybxn6r").isEmpty()) {
				publicationDate = publicationDate + doc.select("summary").get(i).text()+ "\n";
				shortPublicationDate = publicationDate.substring(4, 15);
				System.out.println("TestTime2: " + shortPublicationDate);
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
				LocalDate date = LocalDate.parse(shortPublicationDate, formatter);
				shortPublicationDate = date.format(DateTimeFormatter.ISO_DATE);
				
			} 
	    }
		System.out.println("Published on: " + shortPublicationDate);
		return shortPublicationDate;
	}
*/	
	
	
	
	// ------------------ PUBLICATION DATE with <meta> ---------------------------
	// <meta property="article:published_time" content="2023-05-31T07:00:18.000Z" />	<--- 2022 dalje
	
	public String getPublicationDate() {
		String publicationDate = "";
		String publicationDate2019 = "";
		String summaryPublicationDate = "";
		String shortPublicationDate = "";
		String safety = "0";
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			if (!doc.select("meta").get(i).getElementsByAttributeValue("property", "article:published_time").isEmpty()) {
				publicationDate = publicationDate + doc.select("meta").get(i).attr("content") + "\n";
			}
			
		}
		System.out.println("----- pubDate: -----");
		System.out.println(publicationDate);
		
		// <time> ZA STAREJŠE ČLANKE
		if (publicationDate == "") {
			for (int j = 0; j < doc.select("time").size(); j = j + 1) {
				if (!doc.select("time").get(j).getElementsByAttributeValue("itemprop", "datePublished").isEmpty()) {
					publicationDate = publicationDate + doc.select("time").get(j).text() + "\n";
					shortPublicationDate = publicationDate.substring(4, 15);
					System.out.println("----- shortPubDate: -----");
					System.out.println(shortPublicationDate);
					
				}
			}
			System.out.println("----- pubDate2019: -----");
			System.out.println(publicationDate2019);
			
			if (publicationDate2019 == "") {
				for (int k = 0; k < doc.select("summary").size(); k = k + 1) {
					if (!doc.select("summary").get(k).getElementsByAttributeValue("class", "dcr-1ybxn6r").isEmpty()) {
						publicationDate = publicationDate + doc.select("summary").get(k).text()+ "\n";
						summaryPublicationDate = publicationDate.substring(4, 15);
						System.out.println("----- summaryPubDate: -----");
						System.out.println(summaryPublicationDate);
					} 
			    }
				if (summaryPublicationDate.indexOf(" ") == 1) {							//<-- ČE JE DATUM: "1 Jun 2023 "
					summaryPublicationDate = safety + summaryPublicationDate;
					summaryPublicationDate = summaryPublicationDate.substring(0, 11);	//<--    DA DATUM: "01 Jun 2023"
				}
				
				if (summaryPublicationDate != "") {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH); 	//<-- KO JE DATUM: "01 Jun 2023"
				LocalDate date = LocalDate.parse(summaryPublicationDate, formatter);						//<-- SPREMENI V:  "2023-06-01"
				summaryPublicationDate = date.format(DateTimeFormatter.ISO_DATE);
				System.out.println("----- summaryPubDate2: -----");
				System.out.println(summaryPublicationDate);
				publicationDate = summaryPublicationDate;				
				}
				
				// NE DELUJE, KER ZARADI NEZNANIH RAZLOGOV VZAME TEXT NAD NJIM (AVTORJA)
				//if (summaryPublicationDate == "") {
				/*else  {
					for (int l = 0; l < doc.select("div").size(); l = l + 1) {
						if (!doc.select("div").get(l).getElementsByAttributeValue("class", "dcr-eb59kw").isEmpty()) {
							publicationDate = publicationDate + doc.select("div").get(l).text()+ "\n";
							shortPublicationDate = publicationDate.substring(4, 15);
							System.out.println("----- shortPubDate: -----");
							System.out.println(shortPublicationDate);
						} 
				    }
					
					if (shortPublicationDate.indexOf(" ") == 1) {
						shortPublicationDate = safety + shortPublicationDate;
						shortPublicationDate = shortPublicationDate.substring(0, 11);
					}
					
					DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);  //<-- ČE JE DATUM: 01 Jun 2023
					LocalDate date2 = LocalDate.parse(shortPublicationDate, formatter2);
					shortPublicationDate = date2.format(DateTimeFormatter.ISO_DATE);
					System.out.println("----- shortPubDate: -----");
					System.out.println(shortPublicationDate);	
				}*/
			}		
		}		
		
		// ČE NE USPE NIKJER PREBRATI DATUMA
		if ((publicationDate == "") && (publicationDate2019 == "") && (summaryPublicationDate == "") && (shortPublicationDate == "")) {
			publicationDate = "1000-01-01";
		}
		
		System.out.println("Full Time: " + publicationDate);
		publicationDate = publicationDate.substring(0, 10);
		System.out.println("Published on: " + publicationDate + "\n");
		return publicationDate;

	}
	
	
	
	
	
/*	// ----------- TITLES 2017-2022 with <h1> ---------- ŠE VEDNO DELUJE --------
	
	public String getTitlesText() {
		String titlesText = "";
		if(!doc.select("h1").isEmpty()){
			for (int i = 0; i < 1; i = i + 1) {
				titlesText = titlesText + doc.select("h1").get(i).text() + ".\n";
			}
		}
		System.out.println("Title: " + titlesText);
		return titlesText;
	}
*/	
	
	
	//  ----- TO DO - TITLES with <meta> ------------
	// <meta property="og:title" content="IMF: higher taxes for rich will cut inequality without hitting growth" />
	public String getTitlesText() {
		String titlesText = "";
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			Elements divAB = doc.select("meta").get(i).getElementsByAttributeValue("property", "og:title");
			if (!divAB.isEmpty()) {				
				for (int j = 0; j < divAB.size(); j = j + 1) {
					titlesText = titlesText + divAB.get(j).attr("content") + ".\n";
				}
				break;
			}
		}
		System.out.println("Title: " + titlesText);
		return titlesText;
	}
	
	
	// ------------- SUBTITLES --- JIH DEJANSKO SPLOH NI -----------------------
	
	public String getSubtitlesText() {
		String subtitlesText = "";
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			Elements divAB = doc.select("div").get(i).getElementsByAttributeValue("itemprop", "articleBody");
			if (!divAB.isEmpty()) {				
				for (int j = 0; j < divAB.select("h2").size(); j = j + 1) {
					subtitlesText = subtitlesText + divAB.select("h2").get(j).text() + ".\n";
				}
				break;
			}
		}
		System.out.println("Subtitle: " + subtitlesText);
		return subtitlesText;
	}
	
	
	
	// ---------------------- DESCRIPTION with <meta> ------------------------
	// ------ NI NAJBOLJŠI, KER PRIVZAME NPR. <strong>  <-- <og:descrition> REŠIL PROBLEM  ---------
	// <meta name="description" content="Former Spain and Real Madrid manager ..." />
	
	public String getDescriptionText() {
		String descriptionText = "";
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			//Elements divAB = doc.select("meta").get(i).getElementsByAttributeValue("name", "description");
			Elements divAB = doc.select("meta").get(i).getElementsByAttributeValue("property", "og:description");
			if (!divAB.isEmpty()) {		
				//System.out.println(divAB);
				for (int j = 0; j < divAB.size(); j = j + 1) {
					descriptionText = descriptionText + divAB.get(j).attr("content") + ".\n";
				}
				break;
			}
		}
			System.out.println("Description Text: " + descriptionText);
		return descriptionText;
	}
	
	
	
	
	
/*	-------------------- KEYWORDS 2019 with <meta> ---------------------------


	public String getKeywords() {
		String keywords = "";
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			Elements divAB = doc.select("meta").get(i).getElementsByAttributeValue("name", "keywords");
			if (!divAB.isEmpty()) {		
				//System.out.println(divAB);
				for (int j = 0; j < divAB.size(); j = j + 1) {
					keywords = keywords + divAB.get(j).attr("content") + ".\n";
					System.out.println(keywords);
				}
				break;
			}
		}
		return keywords;
	}
*/
	
	
	//	---------------- KEYWORDS 2023 with <meta> ------------------------


	public String getKeywords() {
		String keywords = "";
		for (int i = 0; i < doc.select("meta").size(); i = i + 1) {
			Elements divAB = doc.select("meta").get(i).getElementsByAttributeValue("property", "article:tag");
			if (!divAB.isEmpty()) {		
				//System.out.println("divAB:" + divAB);
				for (int j = 0; j < divAB.size(); j = j + 1) {
					keywords = keywords + divAB.get(j).attr("content") + ".\n";
					System.out.println("Keywords:" + keywords);
				}
				break;
			}
		}
		return keywords;
	}

	

	
/*	// ---------------- KEWWORDS with <script> ---------------------------
	
	public String getKeywords() {
		String keywords = "";
		String scriptCont; String scriptKeywordsSeq;
		for (int i = 0; i < doc.select("script").size(); i = i + 1) {
			//System.out.println(doc.select("script").get(i));
			//System.out.println("TestTest:  " + doc.select("script").get(i).data().indexOf("keywords"));
			int n = doc.select("script").get(i).data().indexOf("keywords");
			//Elements divAB = doc.select("script").get(i).getElementsByAttributeValue("name", "keywords");
			if (n>-1) {	
				scriptCont = doc.select("script").get(i).data();
				//System.out.println(scriptCont.substring(n,n+300));
				int n1 = scriptCont.substring(n+9,n+12).indexOf("\"") + 1;
				int n2 = scriptCont.substring(n+9+n1,n+300).indexOf("\"");
				scriptKeywordsSeq = scriptCont.substring(n+9+n1,n+9+n2+n1);
				System.out.println("Keywords: " + scriptKeywordsSeq);
				int n3 = scriptKeywordsSeq.indexOf(",");
				int nKeywordStart; int nKeywordEnd;	
				nKeywordStart = 0; nKeywordEnd = n3;
				//System.out.println("NUMofKeywords = " + n3 + "\n");
				if (n3 > -1) {
					do {
						//System.out.println(scriptKeywordsSeq.substring(nKeywordStart, nKeywordEnd));
						keywords = keywords + scriptKeywordsSeq.substring(nKeywordStart, nKeywordEnd) + ".\n";
						scriptKeywordsSeq = scriptKeywordsSeq.substring(n3+1);
						n3 = scriptKeywordsSeq.indexOf(",");
						nKeywordStart = 0; nKeywordEnd = n3;
					}					
					while (n3 > -1);
					//System.out.println(scriptKeywordsSeq.substring(nKeywordStart));
					keywords = keywords + scriptKeywordsSeq.substring(nKeywordStart) + ".\n";	
				}
			}
		}
		return keywords;
	}
*/
	
	
	
	/*public String getParagraphsText() {
		String paragraphsText = "";
		for (int i = 0; i < doc.select("div").size(); i = i + 1) {
			Elements divAB = doc.select("div").get(i).getElementsByAttributeValue("itemprop", "articleBody");
			if (!divAB.isEmpty()) {				
				//paragraphsText = doc.select("div").get(i).getElementsByAttributeValue("itemprop", "articleBody").text();
				for (int j = 0; j < divAB.select("p").size(); j = j +  1) {
					paragraphsText = paragraphsText + divAB.select("p").get(j).text() + "\n";
			    }
				//System.out.println(paragraphsText);
				break;
			}			
		}
		//System.out.println(paragraphsText);
		return paragraphsText;
	}*/
	
	public String getParagraphsText() {
		String paragraphsText = "";
		for (int i = 0; i < doc.select("p").size(); i = i + 1) {
			// if (!doc.select("a").get(i).text().isEmpty()) {		// <-- STARA VERZIJA
			if (!doc.select("p").get(i).text().isEmpty()) {
				paragraphsText = paragraphsText + doc.select("p").get(i).text() + "\n";
			}			
		}
		//System.out.println(paragraphsText);
		return paragraphsText;		
	}
	
	// delete <section class="content-partners">
	// delete <section class="content-republish">
	
	
	
    private static Document parseXMLFromFile(String sourceFileName) {
    	File input = new File(sourceFileName);
    	Document doc = null;
        try {
        	doc = Jsoup.parse(input, "utf-8", "");
        }
    	catch (Exception e) {
        	e.printStackTrace();
        }
    	return doc;
    }
    
}


