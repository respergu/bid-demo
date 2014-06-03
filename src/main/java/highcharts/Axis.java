package highcharts;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Axis {

	private Collection<String> categories;
    private Boolean endOnTick;
    private Number max;
    private Number min;
    private Number tickInterval;
    private Title title;
    private boolean opposite;
    
    public Axis() {}

	@JsonCreator
    public Axis(
	    @JsonProperty("categories") Collection<String> categories,
	    @JsonProperty("endOnTick") Boolean endOnTick,
	    @JsonProperty("max") Number max,
	    @JsonProperty("min") Number min,
	    @JsonProperty("tickInterval") Number tickInterval,
	    @JsonProperty("title") Title title
    ) {
	this.categories = categories;
	this.endOnTick = endOnTick;
	this.max = max;
	this.min = min;
	this.tickInterval = tickInterval;
	this.title = title;
    }

	public Collection<String> getCategories() {
		return categories;
	}

	public void setCategories(Collection<String> categories) {
		this.categories = categories;
	}

	public Boolean getEndOnTick() {
		return endOnTick;
	}

	public void setEndOnTick(Boolean endOnTick) {
		this.endOnTick = endOnTick;
	}

	public Number getMax() {
		return max;
	}

	public void setMax(Number max) {
		this.max = max;
	}

	public Number getMin() {
		return min;
	}

	public void setMin(Number min) {
		this.min = min;
	}

	public Number getTickInterval() {
		return tickInterval;
	}

	public void setTickInterval(Number tickInterval) {
		this.tickInterval = tickInterval;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public boolean isOpposite() {
		return opposite;
	}

	public void setOpposite(boolean opposite) {
		this.opposite = opposite;
	}

}
