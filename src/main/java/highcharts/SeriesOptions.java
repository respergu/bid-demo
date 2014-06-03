package highcharts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesOptions {


    /**
     * Enable or disable the initial animation when a series is displayed. The
     * animation can also be set as a configuration object. Please note that
     * this option only applies to the initial animation of the series itself.
     * For other animations, see chart.animation and the animation parameter
     * under the API methods.	The following properties are supported:
     */
    private Boolean animation;

    @JsonCreator
    public SeriesOptions(
	    @JsonProperty("animation") Boolean animation
    ) {
	this.animation = animation;
    }

	public Boolean getAnimation() {
		return animation;
	}

	public void setAnimation(Boolean animation) {
		this.animation = animation;
	}

}
