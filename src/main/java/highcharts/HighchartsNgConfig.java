package highcharts;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HighchartsNgConfig {


    private Options options;

    private Collection<Series> series = new ArrayList<Series>();
    
    private Object userData;

    @JsonCreator
    public HighchartsNgConfig(
	    @JsonProperty("options") Options options, 
	    @JsonProperty("series") Collection<Series> series)	    
    {
	this(options, series, null);
    }

    public HighchartsNgConfig(Options options, Collection<Series> series, Object userData) {
	this.options = options;
	this.series = series;
	this.userData = userData;
    }

	public Options getOptions() {
		return options;
	}

	public void setOptions(Options options) {
		this.options = options;
	}

	public Collection<Series> getSeries() {
		return series;
	}

	public void setSeries(Collection<Series> series) {
		this.series = series;
	}

	public Object getUserData() {
		return userData;
	}

	public void setUserData(Object userData) {
		this.userData = userData;
	}

}
