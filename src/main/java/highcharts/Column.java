/*
 * Copyright 2014 Michael J. Simons.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package highcharts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Column {


    /**
     * The width of the border surronding each column or bar. Defaults to 1.
     */
    private Number borderWidth;

    /**
     * Padding between each column or bar, in x axis units. Defaults to 0.1.
     */
    private Number pointPadding;

    @JsonCreator
    public Column(
	    @JsonProperty("borderWidth") Number borderWidth,
	    @JsonProperty("pointPadding") Number pointPadding
    ) {
	this.borderWidth = borderWidth;
	this.pointPadding = pointPadding;
    }

	public Number getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(Number borderWidth) {
		this.borderWidth = borderWidth;
	}

	public Number getPointPadding() {
		return pointPadding;
	}

	public void setPointPadding(Number pointPadding) {
		this.pointPadding = pointPadding;
	}

}
