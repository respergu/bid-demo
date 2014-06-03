package power;


public class BooleanMatch {

	private String name;
	private String metricsLabel;
	private boolean value;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "BooleanMatch [name=" + name + ", value=" + value + "]";
	}
	public String getMetricsLabel() {
		return metricsLabel;
	}
	public void setMetricsLabel(String metricsLabel) {
		this.metricsLabel = metricsLabel;
	}
	
}
