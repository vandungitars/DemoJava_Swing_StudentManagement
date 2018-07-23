package ConnectDatabaseStudent;

public class Student {
	private  int id;
	private String name;
	private double point;
	
	public Student() {}
	
	public Student(String name, double point) {
		super();
		this.name = name;
		this.point = point;
	}

	public Student(int id, String name, double point) {
		super();
		this.id = id;
		this.name = name;
		this.point = point;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}
}

