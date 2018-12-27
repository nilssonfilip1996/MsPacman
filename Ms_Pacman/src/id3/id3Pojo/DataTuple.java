package id3.id3Pojo;

public class DataTuple {
	private String attributeValues[]; 
	private String age;
	private String income;
	private String student;
	private String cr_rating;
	private String className;

	public DataTuple(String[] tokens, String age, String income, String student, String cr_rating, String className) {
		attributeValues = tokens;
		this.age = age;
		this.income = income;
		this.student = student;
		this.cr_rating = cr_rating;
		this.className = className;
	}
	public String getAttributeValueAt(int i) {
		return attributeValues[i];
	}

	public String getAge() {
		return age;
	}

	public String getIncome() {
		return income;
	}

	public String getStudent() {
		return student;
	}

	public String getCr_rating() {
		return cr_rating;
	}
	
	public String getClassName() {
		return className;
	}

	@Override
	public String toString() {
		return "DataTuple [age=" + age + ", income=" + income + ", student=" + student + ", cr_rating=" + cr_rating
				+ ", label=" + className + "]" + "\n";
	}



	
}
