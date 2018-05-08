package board.comment;

public class CommentVo {
	private int commentNumber;
    
    private String commentContent;
    private String commentDate;
    private int articleNumber; 
	
    
    public int getCommentNumber() {
		return commentNumber;
	}
	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}
	
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}
	public int getArticleNumber() {
		return articleNumber;
	}
	public void setArticleNumber(int articleNumber) {
		this.articleNumber = articleNumber;
	}
	 @Override
	    public String toString() {
	        return "CommentVo [commentNumber=" + commentNumber +   " commentContent=" + commentContent
	                + ", commentDate=" + commentDate + ", articleNumber=" + articleNumber + "]";
	    }


	


}
