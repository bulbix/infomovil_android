package mlk.androidchartapi;

public class MlkSimpleFormatter implements MlkValueFormatter{

	public String format(double value) {
		return String.format("%.0f", value);
	}

}
