package highcharts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Options {
    
    private Chart chart;

    private Credits credits;

    private PlotOptions plotOptions;
   
    private Title title;

    private Tooltip tooltip;

    private Axis xAxis;

    private Axis yAxis;
    
    private Axis xAxis2;
    

    @JsonCreator
    public Options(
	    @JsonProperty("chart") Chart chart,
	    @JsonProperty("credits") Credits credits,
	    @JsonProperty("plotOptions") PlotOptions plotOptions,
	    @JsonProperty("title") Title title,
	    @JsonProperty("tooltip") Tooltip tooltip,
	    @JsonProperty("xAxis") Axis xAxis,
	    @JsonProperty("yAxis") Axis yAxis
    ) {
	this.chart = chart;
	this.credits = credits;
	this.plotOptions = plotOptions;
	this.title = title;
	this.tooltip = tooltip;
	this.xAxis = xAxis;
	this.yAxis = yAxis;
    }

	public Chart getChart() {
		return chart;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
	}

	public Credits getCredits() {
		return credits;
	}

	public void setCredits(Credits credits) {
		this.credits = credits;
	}

	public PlotOptions getPlotOptions() {
		return plotOptions;
	}

	public void setPlotOptions(PlotOptions plotOptions) {
		this.plotOptions = plotOptions;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public Tooltip getTooltip() {
		return tooltip;
	}

	public void setTooltip(Tooltip tooltip) {
		this.tooltip = tooltip;
	}

	public Axis getxAxis() {
		return xAxis;
	}

	public void setxAxis(Axis xAxis) {
		this.xAxis = xAxis;
	}

	public Axis getyAxis() {
		return yAxis;
	}

	public void setyAxis(Axis yAxis) {
		this.yAxis = yAxis;
	}

	public Axis getxAxis2() {
		return xAxis2;
	}

	public void setxAxis2(Axis xAxis2) {
		this.xAxis2 = xAxis2;
	}
	
}
