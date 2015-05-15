package lcms.base;

public class Question {
	public String question, answerA, answerB, answerC, correctanswer;
	
	public Question(String question, String answerA,String answerB,String answerC,String correctanswer){
		this.question = question;
		this.answerA = answerA;
		this.answerB = answerB;
		this.answerC = answerC;
		this.correctanswer = correctanswer;
	}
}
