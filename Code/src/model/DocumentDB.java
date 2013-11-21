package model;


public class DocumentDB {
	private String author ="";
	private String datePub ="" ;
	private String title ="";
	
	public DocumentDB(){

	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDatePub() {
		return datePub;
	}

	public void setDatePub(String datePub) {
		this.datePub = datePub;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append("Title is : ").append(this.title).append("\n");
		if (!("".equals(author))){
			str.append("Author is : ").append(this.author).append("\n");
		}
		return str.toString();
	}
	
	
}
