package highcharts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tooltip {

    /**
     * A string to append to the tooltip format. Defaults to false.
     */
    private String footerFormat;

    /**
     * The HTML of the tooltip header line. Variables are enclosed by curly
     * brackets. Available variables	are point.key, series.name, series.color
     * and other members from the point and series objects. The point.key
     * variable contains the category name, x value or datetime string depending
     * on the type of axis. For datetime axes, the point.key date format can be
     * set using tooltip.xDateFormat.
     */
    private String headerFormat;

    /**
     * The HTML of the point's line in the tooltip. Variables are enclosed by
     * curly brackets. Available variables are point.x, point.y, series.name and
     * series.color and other properties on the same form. Furthermore, point.y
     * can be extended by the tooltip.yPrefix and tooltip.ySuffix variables.
     * This can also be overridden for each series, which makes it a good hook
     * for displaying units.
     */
    private String pointFormat;

    /**
     * When the tooltip is shared, the entire plot area will capture mouse
     * movement. Tooltip texts for series types with ordered data (not pie,
     * scatter, flags etc) will be shown in a single bubble. This is recommended
     * for single series charts and for tablet/mobile optimized charts. Defaults
     * to false.
     */
    private Boolean shared;

    /**
     * Use HTML to render the contents of the tooltip instead of SVG. Using HTML
     * allows advanced formatting like tables and images in the tooltip. It is
     * also recommended for rtl languages as it works around rtl bugs in early
     * Firefox. Defaults to false.
     */
    private Boolean useHTML;

    @JsonCreator
    public Tooltip(
	    @JsonProperty("footerFormat") String footerFormat, 
	    @JsonProperty("headerFormat") String headerFormat, 
	    @JsonProperty("pointFormat") String pointFormat, 
	    @JsonProperty("shared") Boolean shared, 
	    @JsonProperty("useHTML") Boolean useHTML
    ) {
	this.footerFormat = footerFormat;
	this.headerFormat = headerFormat;
	this.pointFormat = pointFormat;
	this.shared = shared;
	this.useHTML = useHTML;
    }

	public String getFooterFormat() {
		return footerFormat;
	}

	public void setFooterFormat(String footerFormat) {
		this.footerFormat = footerFormat;
	}

	public String getHeaderFormat() {
		return headerFormat;
	}

	public void setHeaderFormat(String headerFormat) {
		this.headerFormat = headerFormat;
	}

	public String getPointFormat() {
		return pointFormat;
	}

	public void setPointFormat(String pointFormat) {
		this.pointFormat = pointFormat;
	}

	public Boolean getShared() {
		return shared;
	}

	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	public Boolean getUseHTML() {
		return useHTML;
	}

	public void setUseHTML(Boolean useHTML) {
		this.useHTML = useHTML;
	}

    
  
}
