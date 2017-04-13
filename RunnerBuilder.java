import java.time.*;
import java.time.format.DateTimeFormatter;

public class RunnerBuilder {
	/* Variable Declaration */
	private char gender;
	private int plc_overall;
	private int plc_gender;
	private int plc_division;
	private String last_name;
	private String first_name;
	private String country = "";
	private String location;
	private int bib;
	private String div;
	private int age;
	private String half;
	private String finish;

	public RunnerBuilder() {}
	
	public Runner buildRunner() {
		Runner runner = new Runner();
		runner.setGender(gender);
		runner.setPlaceOverall(plc_overall);
		runner.setPlacecGender(plc_gender);
		runner.setPlacecDivision(plc_division);
		runner.setLastName(last_name);
		runner.setFirstName(first_name);
		runner.setCountry(country);
		runner.setLocation(location);
		runner.setBib(bib);
		runner.setDiv(div);
		runner.setAge(age);
		runner.setHalf(half);
		runner.setFinish(finish);
		runner.calculate();
		return runner;
	}
	
	public RunnerBuilder Gender (char gender) { this.gender = gender; return this; }
	public RunnerBuilder PlaceOverall (int plc_overall) { this.plc_overall = plc_overall; return this; }
	public RunnerBuilder PlacecGender (int plc_gender) { this.plc_gender = plc_gender; return this; }
	public RunnerBuilder PlacecDivision (int plc_division) { this.plc_division = plc_division; return this; }
	public RunnerBuilder LastName (String last_name) { this.last_name = last_name; return this; }
	public RunnerBuilder FirstName (String first_name) { this.first_name = first_name; return this; }
	public RunnerBuilder Country (String country) { this.country = country; return this; }
	public RunnerBuilder Location (String location) { this.location = location; return this; }
	public RunnerBuilder Bib (int bib) { this.bib = bib; return this; }
	public RunnerBuilder Div (String div) { this.div = div; return this; }
	public RunnerBuilder Age (int age) { this.age = age; return this; }
	public RunnerBuilder Half (String half) { this.half = half; return this; }
	public RunnerBuilder Finish (String finish) { this.finish = finish; return this; }

	public static void main(String[] args) {
		Runner shun = new RunnerBuilder()
					.Gender('M')
					.PlaceOverall(4344)
					.PlacecGender(3583)
					.PlacecDivision(586)
					.LastName("Haga")
					.FirstName("Shunsuke")
					.Country("JPN")
					.Location("Somerville, MA")
					.Bib(9583)
					.Div("25-29")
					.Age(27)
					.Half("01:44:22")
					.Finish("03:28:24")
					.buildRunner();
		System.out.println(shun);


	}
}

