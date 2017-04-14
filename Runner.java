//package finalproject;
// Created by Shunsuke Haga
// Inherited the code wrote by IntelliJ IDEA (JCIAN)
import java.time.*;
import java.time.format.DateTimeFormatter;

// Gender, Place Overall, Place Gender, Place Division, Last Name, First Name, Country, Location, Bib, Division, Age, Half, Finish
public class Runner {
	/* Variable Declaration */
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");

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
	private LocalTime half;
	private LocalTime finish;

	// Used in a method
	private Duration duration = null;
	private LocalTime pace_per_mile = null;
	private LocalTime pace_per_kilo = null;
	private double miles_per_hour = 0.0;
	private double kilo_per_hour = 0.0;
    private boolean is_sub_three;

	/* Constructor */
	// Too many argument to pass to constructor.
	// Instead I use a class called RunnerBuilder to initiate Runner with arguments.
	// Usage: 
	/*
			Runner shun = new RunnerBuilder()
					.Gender('M')
					.PlaceOverall(4344)
					.PlaceGender(3583)
					.PlaceDivision(586)
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
	*/
	
	/* Setter and Getter */
	// Setter
	public void setGender (char gender) { this.gender = gender; }
	public void setPlaceOverall (int plc_overall) { this.plc_overall = plc_overall; }
	public void setPlaceGender (int plc_gender) { this.plc_gender = plc_gender; }
	public void setPlaceDivision (int plc_division) { this.plc_division = plc_division; }
	public void setLastName (String last_name) { this.last_name = last_name; }
	public void setFirstName (String first_name) { this.first_name = first_name; }
	public void setCountry (String country) { this.country = country; }
	public void setLocation (String location) { this.location = location; }
	public void setBib (int bib) { this.bib = bib; }
	public void setDiv (String div) { this.div = div; }
	public void setAge (int age) { this.age = age; }
	public void setHalf (String half) {
		this.half = LocalTime.parse((half != null && !half.isEmpty() )? "0" + half : "00:00:00"); }
	public void setFinish (String finish) { this.finish = LocalTime.parse("0" + finish); } 

	public void setDuration() { this.duration = calcDuration(); }
	public void setPacePerMile() { this.pace_per_mile = calcPacePerMile(); }
	public void setPacePerKilo() { this.pace_per_kilo = calcPacePerKilo(); }
	public void setMilesPerHour() { this.miles_per_hour = calcMilesPerHour(); }
	public void setKiloPerHour() { this.kilo_per_hour = calcKiloPerHour(); }
    public void setIsSubThree() { this.is_sub_three = isSubThree(); }

	// Getter
	public char getGender() { return gender; }
	public int getPlaceOverall() { return plc_overall; }
	public int getPlaceGender() { return plc_gender; }
	public int getPlaceDivision() { return plc_division; }
	public String getLastName() { return last_name; }
	public String getFirstName() { return first_name; }
	public String getCountry() { return country; }
	public String getLocation() { return location; }
	public int getBib() { return bib; }
	public String getDiv() { return div; }
	public int getAge() { return age; }
	public LocalTime getHalf() { return half; }
	public LocalTime getFinish() { return finish; }

	public Duration getDuration() { return duration; }
	public LocalTime getPacePerMile() { return pace_per_mile; }
	public LocalTime getPacePerKilo() { return pace_per_kilo; }
	public double getMilesPerHour() { return miles_per_hour; }
	public double getKiloPerHour() { return kilo_per_hour; }
    public boolean getIsSubThree() { return is_sub_three; }

	public String getName() {return first_name + " " + last_name; }
	public String getHalfAsString() { return half.format(format); }
	public String getFinishAsString() { return finish.format(format); }
	public String getPacePerMileAsString() { return pace_per_mile.format(format); }
	public String getPacePerKiloAsString() { return pace_per_kilo.format(format); }
	
	/* Methods */
	public void calculate () {
		this.setDuration(); this.setPacePerMile(); this.setPacePerKilo();
		this.setMilesPerHour(); this.setKiloPerHour(); this.setIsSubThree();
	}
	
	Duration calcDuration() { return Duration.between ( LocalTime.MIN , this.finish ); }
	LocalTime calcPacePerMile() {
		Duration duration = this.duration.dividedBy(262188).multipliedBy(10000); // 1 marathon is 26.21 miles
		return LocalTime.MIN.plus(duration);
	}
	LocalTime calcPacePerKilo() {
		Duration duration = this.duration.dividedBy(42195).multipliedBy(1000); // 1 marathon is 42.195 km
		return LocalTime.MIN.plus(duration);
	}
    double calcMilesPerHour() {
		Duration duration = Duration.between ( LocalTime.MIN , this.finish );
		return 26.2188 / (double)duration.toMillis() * ( 60 * 60 * 1000 );
	}
	double calcKiloPerHour() {
		Duration duration = Duration.between ( LocalTime.MIN , this.finish );
		return 42.195 / (double)duration.toMillis() * ( 60 * 60 * 1000 );
	}
	boolean isSubThree() { return this.duration.toMillis() <= 1080000L; }
    

    public String name() { return first_name + " " + last_name; }
	/*
	public String toString() {
		String result = 
		"[ Gender:" + gender +
			", PlaceOverall:" + plc_overall +
			", PlaceGender:" + plc_gender +
			", PlaceDivision:" + plc_division +
			", LastName:" + last_name +
			", FirstName:" + first_name +
			", Country:" + country +
			", Location:" + location +
			", Bib:" + bib +
			", Div:" + div +
			", Age:" + age +
			", Half:" + this.getHalfAsString() +
			", Finish:" + this.getFinishAsString() +

			", Duration:" + duration +
			", PacePerMile:" + this.getPacePerMileAsString() +
			", PacePerKilo:" + this.getPacePerKiloAsString() +
			", MilesPerHour:" + miles_per_hour +
			", KiloPerHour:" + kilo_per_hour +
			", IsSubThree:" + is_sub_three + " ]";
		return result;
	}
	*/
	@Override
	public String toString() { return this.name(); }
}
