package demo;

import java.util.Date;

public class ChartRequest {

	private boolean compareDates;
	private Integer dateRange;
	private Date range1FromDate;
	private Date range1ToDate;
	private Date range2FromDate;
	private Date range2ToDate;
	
	public Date getRange1FromDate() {
		return range1FromDate;
	}
	public void setRange1FromDate(Date range1FromDate) {
		this.range1FromDate = range1FromDate;
	}
	public Date getRange1ToDate() {
		return range1ToDate;
	}
	public void setRange1ToDate(Date range1ToDate) {
		this.range1ToDate = range1ToDate;
	}
	public Date getRange2FromDate() {
		return range2FromDate;
	}
	public void setRange2FromDate(Date range2FromDate) {
		this.range2FromDate = range2FromDate;
	}
	public Date getRange2ToDate() {
		return range2ToDate;
	}
	public void setRange2ToDate(Date range2ToDate) {
		this.range2ToDate = range2ToDate;
	}
	public boolean isCompareDates() {
		return compareDates;
	}
	public void setCompareDates(boolean compareDates) {
		this.compareDates = compareDates;
	}
	public Integer getDateRange() {
		return dateRange;
	}
	public void setDateRange(Integer dateRange) {
		this.dateRange = dateRange;
	}
	
}
