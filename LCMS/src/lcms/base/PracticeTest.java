package lcms.base;

import java.util.ArrayList;

public class PracticeTest {
	public ArrayList<Question> questions;
	public String name;
	
	public PracticeTest(){
		questions = new ArrayList<Question>();
	}
	public void addQuestion(Question q){
		questions.add(q);
	}
	public void setName(String n){
		name = n;
	}
}
